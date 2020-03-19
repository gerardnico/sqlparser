
package com.gerardnico.sqlparser;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.parser.Provider;
import net.sf.jsqlparser.parser.StreamProvider;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

public class ParserGeneralTest {

    @Test
    public void basic() throws ParseException {
        // StringReaders create a reader from a string
        Reader input = new StringReader("SELECT * FROM R");
        Provider provider = new StreamProvider(input);
        // CCJSqlParser takes a Reader or InputStream
        CCJSqlParser parser = new CCJSqlParser(provider);

        // CCJSqlParser.Statement() returns the next
        // complete Statement object from the reader or
        // input stream (or null if the stream is empty).
        Statement statement = parser.Statement();
        if (statement instanceof Select) {
            Select select = (Select) statement;
            // Do something with `select`
            SelectBody body = select.getSelectBody();
            if (body instanceof PlainSelect) {
                PlainSelect plain = (PlainSelect) body;
                // ...
            }
            System.out.println("Select " + select);

        } else if (statement instanceof CreateTable) {
            CreateTable create = (CreateTable) statement;
            // Do something with `create`

        } else {
            throw new RuntimeException("Can't handle: " + statement);
        }
    }

    /**
     * See Visualize parsing
     * https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing#visualize-parsing
     */
    @Test
    public void visualisationTest() {

    }
}



