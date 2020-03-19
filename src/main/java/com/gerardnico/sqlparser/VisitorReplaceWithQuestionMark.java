package com.gerardnico.sqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * A visitor that replace the string and long value with a question mark ?
 *
 * <a href="https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building#more-general-replacing-of-string-values-in-statements">More general replacing of String values in Statements</a>
 *
 * The main entry is {@link #replace(String)}
 *
 */
public class VisitorReplaceWithQuestionMark extends ExpressionDeParser {

    @Override
    public void visit(StringValue stringValue) {
        this.getBuffer().append("?");
    }

    @Override
    public void visit(LongValue longValue) {
        this.getBuffer().append("?");
    }

    /**
     * Get a statementDeparser based on this visitor
     * @return
     */
    protected static StatementDeParser create() {
        ExpressionDeParser visitor = new VisitorReplaceWithQuestionMark();
        StringBuilder buffer = new StringBuilder();
        SelectDeParser selectDeparser = new SelectDeParser(visitor, buffer);
        visitor.setSelectVisitor(selectDeparser);
        visitor.setBuffer(buffer);
        return new StatementDeParser(visitor, selectDeparser, buffer);
    }

    /**
     *
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static String replace(String sql)  {

        /**
         * Visitor building
         */
        StatementDeParser visitor = VisitorReplaceWithQuestionMark.create();

        /**
         * Call
         */
        Statement stmt = null;
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        stmt.accept(visitor);

        /**
         * Return the result of the visit
         */
        return visitor.getBuffer().toString();

    }
}
