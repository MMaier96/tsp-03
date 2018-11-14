package data;

import main.Configuration;
import statistics.Statistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.DoubleUnaryOperator;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private String driverName = "jdbc:hsqldb:";
    private String username = "sa";
    private String password = "";

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void init() {
        dropTable();
        createTable();
    }

    public synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);
            if (result == -1)
                System.out.println("error executing " + sqlStatement);
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void dropTable() {
        System.out.println("--- dropTable");

/*
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE data");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
*/

        StringBuilder sqlStringBuilder1 = new StringBuilder();
        sqlStringBuilder1.append("DROP TABLE iterations");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder1.toString());
        update(sqlStringBuilder1.toString());
    }

    public void createTable() {
/*        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE data ").append(" ( ");
        sqlStringBuilder.append("id BIGINT NOT NULL").append(",");
        sqlStringBuilder.append("test VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder.append("PRIMARY KEY (id)");
        sqlStringBuilder.append(" );");
        System.out.println(sqlStringBuilder);
        update(sqlStringBuilder.toString());*/


        StringBuilder sqlStringBuilder1 = new StringBuilder();
        sqlStringBuilder1.append("CREATE TABLE iterations ").append(" ( ");
        sqlStringBuilder1.append("id VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder1.append("iterationid  VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder1.append("fitnessvalue VARCHAR(20) NOT NULL");
        sqlStringBuilder1.append(" );");
        System.out.println(sqlStringBuilder1);
        update(sqlStringBuilder1.toString());
    }

    public void addFitnessToScenario(String id, int iterationid, double fitnessvalue) {
        String statement = "INSERT INTO iterations(id, iterationid, fitnessvalue) VALUES (" +
                "'" + id + "'," +
                "'" + iterationid + "'," +
                "'" + fitnessvalue + "')";
        update(statement);
    }

    public void checkTable(int i){
        String query = "SELECT fitnessvalue FROM iterations where id='s01'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                                    //output[j] = result.getString(j+1);
                    System.out.println(result.getDouble("fitnessvalue"));
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void writeCsv(String scenarioId,int i) {
        Statistics stat = new Statistics();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("SELECT fitnessvalue FROM iterations where id='");
        sqlStringBuilder.append(scenarioId).append("'");
            try {
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sqlStringBuilder.toString());
                int size = 0;
                if (result != null) {
/*                    result.beforeFirst();
                    result.last();*/
                    size = result.getFetchSize();
                }
                ArrayList<Double> output = new ArrayList<Double>(size);
                while (result.next()) {
                        output.add(result.getDouble("fitnessvalue"));
                    }

                Double[] values = output.toArray(new Double[output.size()]);
                stat.writeCSVFile(scenarioId, values);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }




    public String buildSQLStatement(long id, String test) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO data (id,test) VALUES (");
        stringBuilder.append(id).append(",");
        stringBuilder.append("'").append(test).append("'");
        stringBuilder.append(")");
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public void insert(String test) {
        update(buildSQLStatement(System.nanoTime(), test));
    }

    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}