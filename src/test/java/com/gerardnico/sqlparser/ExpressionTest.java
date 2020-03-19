package com.gerardnico.sqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Test;



public class ExpressionTest {

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing#simple-expression-parsing
     * @throws JSQLParserException
     */
    @Test
    public void parsingTest() throws JSQLParserException {
        Expression expr = CCJSqlParserUtil.parseExpression("a*(5+mycolumn)");
        /**
         * A visitor can be used
         */
        System.out.println(expr.toString());
    }

    /**
     * https://github.com/JSQLParser/JSqlParser/wiki/Example-of-expression-evaluation
     */
    @Test
    public void evaluationTest() {
        String expr = "4+5*6";
        System.out.println(expr+"="+ExpressionEngine.evaluate(expr));
        expr = "4*5+6";
        System.out.println(expr+"="+ExpressionEngine.evaluate(expr));
        expr = "4*(5+6)";
        System.out.println(expr+"="+ExpressionEngine.evaluate(expr));
        expr = "4*(5+6)*(2+3)";
        System.out.println(expr+"="+ExpressionEngine.evaluate(expr));
    }
}
