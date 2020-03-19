package com.gerardnico.sqlparser;

import org.junit.Test;

public class VisitorTest {

    /**
     * A visitor that replace the string and long value with a question mark ?
     *
     * <a href="https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-building#more-general-replacing-of-string-values-in-statements">More general replacing of String values in Statements</a>
     *
     *
     */
    @Test
    public void replace() {

        System.out.println(VisitorReplaceWithQuestionMark.replace("SELECT 'abc', 5 FROM mytable WHERE col='test'"));
        System.out.println(VisitorReplaceWithQuestionMark.replace("UPDATE table1 A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'"));
        System.out.println(VisitorReplaceWithQuestionMark.replace("INSERT INTO example (num, name, address, tel) VALUES (1, 'name', 'test ', '1234-1234')"));
        System.out.println(VisitorReplaceWithQuestionMark.replace("DELETE FROM table1 where col=5 and col2=4"));

    }
}
