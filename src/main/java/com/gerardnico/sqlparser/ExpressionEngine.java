package com.gerardnico.sqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.Stack;

public class ExpressionEngine {

    static Long evaluate(String expr)  {
        final Stack<Long> stack = new Stack<>();
        Expression parseExpression;
        try {
            parseExpression = CCJSqlParserUtil.parseExpression(expr);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        ExpressionDeParser deparser = new ExpressionDeParser() {
            @Override
            public void visit(Addition addition) {
                super.visit(addition);
                long sum1 = stack.pop();
                long sum2 = stack.pop();
                stack.push(sum1 + sum2);
            }

            @Override
            public void visit(Multiplication multiplication) {
                super.visit(multiplication);

                long fac1 = stack.pop();
                long fac2 = stack.pop();

                stack.push(fac1 * fac2);
            }

            @Override
            public void visit(LongValue longValue) {
                super.visit(longValue);
                stack.push(longValue.getValue());
            }

        };
        StringBuilder b = new StringBuilder();
        deparser.setBuffer(b);
        parseExpression.accept(deparser);

        return stack.pop();
    }

}
