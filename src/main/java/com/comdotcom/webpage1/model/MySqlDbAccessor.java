/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comdotcom.webpage1.model;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.sql.DataSource;

/**
 *
 * @author Ryan Schissel
 */
public class MySqlDbAccessor implements DBStrategy {

    private Connection conn;

    //needs validation
    //consider custom exception
    @Override
    public void openConnection(String driverClass, String url, String userName, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        conn = DriverManager.getConnection(url, userName, password);
    }
    /**
     * Open a connection using a connection pool configured on server.
     *
     * @param ds - a reference to a connection pool via a JNDI name, producing
     * this object. Typically done in a servlet using InitalContext object.
     * @throws SQLException - if ds cannot be established
     */
    @Override
    public final void openConnection(DataSource ds) throws SQLException {
        conn = ds.getConnection();
    }
    @Override
    public void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public List<Map<String, Object>> findRecordsFor(String tableName, int maxRecords) throws SQLException {

        String sqlCmd = "";
        if (maxRecords > 0) {
            sqlCmd = "SELECT * FROM " + tableName + " LIMIT " + maxRecords;
        } else {
            sqlCmd = "SELECT * FROM" + tableName;
        }

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlCmd);

        List<Map<String, Object>> results = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        Map<String, Object> record = null;

        while (rs.next()) {
            record = new LinkedHashMap<>();
            for (int colNo = 1; colNo <= colCount; colNo++) {
                //long way
                //String colName = rsmd.getColumnName(colNo);
                //record.put(colName, rs.getObject(colNo));

                //short way
                record.put(rsmd.getColumnName(colNo), rs.getObject(colNo));
            }
            results.add(record);
        }
        return results;
    }

    @Override
    public void createNewRecord(String tableName, List<String> columnNames, List<Object> values) throws SQLException {
        // INSERT INTO author (author_name,date) VALUES(?,?,?)
        String sql = "INSERT INTO " + tableName + " ";
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (String col : columnNames) {
            sj.add(col);
        }
        sql += sj.toString();
        sj = new StringJoiner(",", "(", ")");
        sql += " VALUES ";
        for (Object val : values) {
            sj.add("?");
        }
        sql += sj.toString();
        PreparedStatement pstm = conn.prepareStatement(sql);
        for (Object val : values) {
            pstm.setObject(values.indexOf(val) + 1, val);
        }
        pstm.executeUpdate();
    }

    @Override
    public void deleteByColumnValue(String tableName, String whereClauseColumnName, Object whereClauseColumnValue) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = null;
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClauseColumnName + " = ?";
        pstm = conn.prepareStatement(sql);
        pstm.setObject(1, whereClauseColumnValue);
        pstm.executeUpdate();
    }

    @Override
    public void updateByColumnValue(String tableName, List<String> columnNames, List<Object> newValues, String whereClauseColumnName, Object whereClauseValue) throws SQLException {
        PreparedStatement pstm = null;
        // UPDATE author SET author_name = ?,author_date = ?  WHERE authorId = ?
        String sql = "UPDATE " + tableName + " SET ";
        StringJoiner sj = new StringJoiner("= ? ");

        for (String col : columnNames) {
            sj.add(col);
        }
        sql += sj.toString();
        sql += " = ?";
        sql += " WHERE " + whereClauseColumnName + " = ?";
        System.out.println(sql);
        pstm = conn.prepareStatement(sql);
        for (int i = 0; i < newValues.size(); i++) {
            pstm.setObject(i + 1, newValues.get(i));
        }
        pstm.setObject(newValues.size() + 1, whereClauseValue);
        pstm.executeUpdate();
    }

    public String removeEndingComma(String sql) {
        return sql.substring(0, sql.length() - 1);
    }

    @Override
    public Map<String, Object> findByColumnValue(String tableName, String columnName, Object value) throws SQLException {
        PreparedStatement pstm = null;
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        pstm = conn.prepareStatement(sql);
        pstm.setObject(1, value);
        ResultSet rs = pstm.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        Map<String, Object> record = null;
        while (rs.next()) {
            record = new LinkedHashMap<>();
            for (int colNo = 1; colNo <= colCount; colNo++) {
                //long way
                //String colName = rsmd.getColumnName(colNo);
                //record.put(colName, rs.getObject(colNo));

                //short way
                record.put(rsmd.getColumnName(colNo), rs.getObject(colNo));
            }
        }
        return record;

    }

    public static void main(String[] args) throws Exception {
        DBStrategy db = new MySqlDbAccessor();

        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/book";
        String userName = "root";
        String password = "admin";
        db.openConnection(driverClassName, url, userName, password);

        List<String> columnNames = new ArrayList<>();
        List<Object> newValues = new ArrayList<>();
        columnNames.add("author_name");
        newValues.add("Luke Warm");
        String columnName = "author_id";
        Object value = 12;
        //db.updateByColumnValue("author", columnNames, newValues, whereClauseColumnName, whereClauseValue);

        Object o = db.findByColumnValue("author", columnName, value);
        System.out.println(o);
//        List<Map<String, Object>> records = db.findRecordsFor("author", 50);
//        db.closeConnection();
//
//        for (Map<String, Object> record : records) {
//            System.out.println(record);
//        }
    }

}
