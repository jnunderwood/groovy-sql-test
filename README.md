# groovy-sql-test

Simple groovy program which demonstrates a possible bug with groovy Sql.

## Details

Calling `sql.rows(string)` works while calling `sql.rows(gstring)` fails silently.
This indicates a possible bug in the protected method `groovy.sql.Sql.asSql()`.

The behavior is the same under groovy version 2.4.15, 2.5.2, 2.6.0-alpha-4, and 3.0.0-alpha-3
all running under OpenJDK Java 8.0.181-zulu.

Run this program by simply calling `groovy sqlTest.groovy`.

