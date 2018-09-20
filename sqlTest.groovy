/**
 * sqlTest.groovy
 *
 * Demonstrates a bug with 'groovy.sql.Sql', possibly in the protected method 'asSql()'.
 *
 * Calling 'sql.rows(string)' works. Calling 'sql.rows(gstring)' returns an empty result set.
 * It behaves the same in groovy versions 2.4.15, 2.5.2, 2.6.0-alpha-4, and 3.0.0-alpha-3, all
 * running under OpenJDK Java 8.0.181-zulu. Run this program by simply calling
 * 'groovy sqlTest.groovy'.
 */

@GrabConfig(systemClassLoader=true)
@Grab(group='com.h2database', module='h2', version='1.4.197')

import groovy.sql.Sql

// connect to an in-memory H2 database; turn on INFO level logging
def db = [url:'jdbc:h2:mem:;TRACE_LEVEL_SYSTEM_OUT=2', user:'sa', password:'', driver:'org.h2.Driver']
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

// create a table
def create = """
     create table employee (
         id varchar(11) not null,
         name varchar(50),
     )
"""
sql.execute(create)

// store some data
def data = [ [id:'1', name:'John Doe'], [id:'2', name:'John Smith'] ]
for (map in data) {
    def insert = """
        insert into employee (id, name)
        values ($map.id, $map.name)
    """
    sql.execute(insert)
}

// define ids to lookup
def ids = "'1', '2'"

// look up some data using a String statement
String stringSelect = """
    SELECT id, name
      FROM employee
     WHERE id IN ($ids)
"""
rows = sql.rows(stringSelect)

println ""
println "sql.rows(" + stringSelect.class + ") returned $rows.size rows; expected $data.size rows"
println ""

// lookup some data using a GString statement
def gStringSelect = """
    SELECT id, name
      FROM employee
     WHERE id IN ($ids)
"""
def rows = sql.rows(gStringSelect)

println ""
println "sql.rows(" + gStringSelect.class + ") returned $rows.size rows; expected $data.size rows"
println ""

// clean up
sql.close()

