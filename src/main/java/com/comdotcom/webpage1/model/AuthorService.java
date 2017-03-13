/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comdotcom.webpage1.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ryan Schissel
 */
public class AuthorService {
  
    private AuthorDaoStrategy dao;
    public List<Author> getAuthors(int maxRecords) throws ClassNotFoundException, SQLException{
//    Author a1 = new Author(1,"Ronda McChiken-Berger", new Date());
//    Author a2 = new Author(2,"Jimi Johnson", new Date());
//    Author a3 = new Author(3,"Roger Rogers", new Date());
//    
//    authorList.add(0, a1);
//    authorList.add(0, a2);
//    authorList.add(0, a3);

        return dao.getAuthorList(maxRecords);
    }
    
    public void removeAuthorById(String id) throws ClassNotFoundException, SQLException{
        dao.removeAuthorById(id);
    }
    public void modifyAuthorInfo(String authorName, String authorId) throws ClassNotFoundException, SQLException{
        dao.modifyAuthorInfo(authorName,authorId);
    }
    public void addAuthor(String authorName) throws ClassNotFoundException, SQLException{
        dao.addAuthor(authorName);
 }
    public AuthorService(AuthorDaoStrategy dao) {
        this.dao = dao;
    }
    
    public Author getAuthorById(String authorId) throws ClassNotFoundException, SQLException {
        return dao.getAuthorById(authorId);
    }

    public AuthorDaoStrategy getDao() {
        return dao;
    }

    public void setDao(AuthorDaoStrategy dao) {
        this.dao = dao;
    }
    public static void main(String[] args) throws Exception{
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/book";
        String userName = "root";
	String password = "admin";
        
        AuthorService as = new AuthorService(
                new AuthorDao(
                        new MySqlDbAccessor(), driverClassName, url, userName,password
                )
        );
        
        as.modifyAuthorInfo("Timmy Thompson", "12");
//        List<String> columns = new ArrayList<>();
//        columns.add("author_name");
//        List<Object> values = new ArrayList<>();
//        values.add("Ronda McChekin-Freiberger");
//        as.modifyAuthorInfo("author", columns, values, "author_name" , "Ronda McChickenFri-Berger");
//        
        List<Author> authors = as.getAuthors(50);
        
        for (Author a : authors){
            System.out.println(a);
        }
    }
}
