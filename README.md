# SQL Parser (jsqlparser)

## About

[JSqlParser](https://github.com/JSQLParser/JSqlParser/) code demo script

## Demos

  * [Parser basic](src/test/java/com/gerardnico/sqlparser/ParserGeneralTest.java)
  * [Select Statement](src/test/java/com/gerardnico/sqlparser/SelectStatementTest.java)
  * [Insert Statement](src/test/java/com/gerardnico/sqlparser/InsertStatementTest.java)
  * [Visitor (Replace)](src/test/java/com/gerardnico/sqlparser/VisitorTest.java)
  * [Expression (Visitor, Execution)](src/test/java/com/gerardnico/sqlparser/ExpressionTest.java)

## Note

### Architecture

Interface and their implementation 
  * Statement can be 
     * Select, 
     * CreateTable
  * SelectBody interface is implemented by:
     * PlainSelect, 
     * Union
  * FromItem: 
    * Table, 
    * Join, 
    * SubSelect
  * SelectItem: 
    * AllColumns, 
    * AllTableColumns, 
    * SelectExpressionItem
  * Expression: 
    * LongValue, 
    * AddExpression, 
    * GreaterThan

### Statement 
#### Plain Select
A  PlainSelect is of the form 
```sql
SELECT [distinct] [selectItems]
	FROM [fromItem], [joins, ...]
	WHERE [where]
	GROUP BY [groupByColumnReferences]
	HAVING [having]
	ORDER BY [orderByElements]
	LIMIT [limit]
```
Everything in [brackets] has a method in [PlainSelect](https://github.com/JSQLParser/JSqlParser/blob/master/src/main/java/net/sf/jsqlparser/statement/select/PlainSelect.java)
The core of a "SELECT" statement (no UNION, no ORDER BY)

#### Union

A Union has the following form: (SELECT * FROM ...) UNION ALL (SELECT ...)

### SelectItems

The following select item implementation represent the following form:

  * AllColumns: SELECT *
  * AllTableColumns: SELECT R.*
  * SelectExpressionItem: SELECT R.A or SELECT R.A AS Q
  
  
## Ref

  * https://odin.cse.buffalo.edu/teaching/cse-562/2019sp/index.html