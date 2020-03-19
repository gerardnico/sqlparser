package com.gerardnico.sqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectStatementTest {

    /**
     * How to build a select statement
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building
     *
     * @throws JSQLParserException
     */
    @Test
    public void sqlSelectStatementBuilding() throws JSQLParserException {

        /**
         * select * from myTable
         */
        Select select = SelectUtils.buildSelectFromTable(new Table("mytable"));
        System.out.println("Select has now the value: " + select);

        /**
         * With column object
         * select a, b from mytable.
         */
        select = SelectUtils.buildSelectFromTableAndExpressions(new Table("mytable"), new Column("a"), new Column("b"));
        System.out.println("Select has now the value: " + select);

        /**
         * With expression
         * select a + b from mytable.
         */
        select = SelectUtils.buildSelectFromTableAndExpressions(new Table("mytable"), "a+b", "test");
        System.out.println("Select has now the value: " + select);
    }

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building#replacing-string-values
     */
    @Test
    public void replaceSelectTest() throws JSQLParserException {
        String sql = "SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN ('11111111111111', '22222222222222');";
        Select select = (Select) CCJSqlParserUtil.parse(sql);

        /**
         * Start of value modification
         */
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser() {

            @Override
            public void visit(StringValue stringValue) {
                this.getBuffer().append("XXXX");
            }

        };
        SelectDeParser deparser = new SelectDeParser(expressionDeParser, buffer);
        expressionDeParser.setSelectVisitor(deparser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(deparser);

        /**
         * End of value modification
         */


        /**
         * Result is: SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN (XXXX, XXXX)
         */
        System.out.println(buffer.toString());

    }

    /**
     * Cast to a {@link PlainSelect} to be able to change every column
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building--part-2
     * @throws JSQLParserException
     */
    @Test
    public void replaceEveryColumnWithPlainSelectCast() throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col_1 = 10 AND col_2 = 20 AND col_3 = 30");
        System.out.println("before " + stmt.toString());

        ((PlainSelect)stmt.getSelectBody()).getWhere().accept(new ExpressionVisitorAdapter() {
            @Override
            public void visit(Column column) {
                column.setColumnName(column.getColumnName().replace("_", ""));
            }
        });

        System.out.println("after " + stmt.toString());
    }

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-analyzing
     * @throws JSQLParserException
     */
    @Test
    public void selectExtractGetColumns() throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col1 = 10 AND col2 = 20 AND col3 = 30");

        Map<String, Expression> columns = new HashMap<>();
        for (SelectItem selectItem : ((PlainSelect)stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    columns.put(item.getAlias().getName(), item.getExpression());
                }
            });
        }

        System.out.println("Columns " + columns);

    }

    /**
     * Extract the tables names from a select statement
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing#extract-table-names-from-sql
     */
    @Test
    public void extractTableNames() throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse("SELECT * FROM MY_TABLE1, MY_TABLE2");
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
        System.out.println(tableList);
    }

    /**
     * Add an alias to every expression in the select clause
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing#apply-aliases-to-all-expressions
     * @throws JSQLParserException
     */
    @Test
    public void addAliasToColumnExpressionProjectedTest() throws JSQLParserException {
        String originalSelect = "select a,b,c from test";
        Select select = (Select) CCJSqlParserUtil.parse(originalSelect);
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);
        System.out.println("Original Select           : "+originalSelect);
        System.out.println("Modified Select with alias: " +select.toString().toLowerCase());
    }
}
