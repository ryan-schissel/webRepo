/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comdotcom.webpage1.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 *
 * @author Ryan Schissel
 */
public class AuthorDao implements AuthorDaoStrategy {

    private static final String TABLE_NAME = "author";
    private static final String PRIMARY_KEY_COL_NAME = "author_id";
    private DBStrategy db;
    private String driverClass;
    private String url;
    private String userName;
    private String password;

    public AuthorDao(DBStrategy db, String driverClass, String url, String userName, String password) {
        this.db = db;
        this.driverClass = driverClass;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public List<Author> getAuthorList(int maxRecords) throws ClassNotFoundException, SQLException {

        List<Author> authorList = new ArrayList<>();
        db.openConnection(driverClass, url, userName, password);
        List<Map<String, Object>> rawData = db.findRecordsFor(TABLE_NAME, maxRecords);
        db.closeConnection();

        for (Map<String, Object> recData : rawData) {
            Author author = new Author();

            author.setAuthorId((Integer) recData.get("author_id"));
            Object objName = recData.get("author_name");
            String name = objName != null ? objName.toString() : "";
            author.setAuthorName(name);

            Object objDate = recData.get("date_added");
            Date dateAdded = objDate != null ? (Date) objDate : null;
            author.setDateAdded(dateAdded);
            authorList.add(author);
        }
        return authorList;
    }

    public DBStrategy getDb() {
        return db;
    }

    public void setDb(DBStrategy db) {
        this.db = db;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void main(String[] args) throws Exception {
//        DBStrategy db = new MySqlDbAccessor();
//
//        String driverClassName = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://localhost:3306/book";
//        String userName = "root";
//        String password = "admin";
//        List<Object> values = new ArrayList<>();
//        values.add("Ronda McChickenfri-berger");
//        values.add(new Date());
//        AuthorDaoStrategy dao = new AuthorDao(db, driverClassName, url, userName, password);
//        dao.addAuthor("author", Arrays.asList("author_name", "date_added"), values);
////        List<Author> authors = dao.getAuthorList("author", 50);
////        
////        for (Author a :authors){
////            System.out.println(a);
////        }
//        Author author = dao.getAuthorById("author", "author_id", 12);
//        System.out.println(author);
    }

    @Override
    public void removeAuthorById(String id) throws ClassNotFoundException, SQLException {
        db.openConnection(driverClass, url, userName, password);
        db.deleteByColumnValue(TABLE_NAME, PRIMARY_KEY_COL_NAME, id);
        db.closeConnection();
    }

    @Override
    public void modifyAuthorInfo(String authorName, String authorId) throws ClassNotFoundException, SQLException {
        db.openConnection(driverClass, url, userName, password);
        db.updateByColumnValue("author", Arrays.asList("author_name"), Arrays.asList((Object) authorName), "author_id", new Integer(authorId));
        db.closeConnection();
    }

    public void addAuthor(String authorName) throws ClassNotFoundException, SQLException {
        db.openConnection(driverClass, url, userName, password);
        db.createNewRecord(TABLE_NAME, Arrays.asList("author_name", "date_added"), Arrays.asList((Object) authorName, (Object)new Date()));
        db.closeConnection();
    }

    @Override
    public Author getAuthorById(String authorId) throws ClassNotFoundException, SQLException {
        String colName = "author_id";
        String tableName = "author";
        Author author = new Author();
        db.openConnection(driverClass, url, userName, password);
        Map<String, Object> rawData = db.findByColumnValue(tableName, colName, authorId);
        db.closeConnection();
        author.setAuthorId((Integer) rawData.get("author_id"));
        Object objName = rawData.get("author_name");
        String name = objName != null ? objName.toString() : "";
        author.setAuthorName(name);
        Object objDate = rawData.get("date_added");
        Date dateAdded = objDate != null ? (Date) objDate : null;
        author.setDateAdded(dateAdded);
        return author;
    }

}
