/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comdotcom.webpage1.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ryan Schissel
 */
public interface AuthorDaoStrategy {

    List<Author> getAuthorList(int maxRecords) throws ClassNotFoundException, SQLException;

   
    Author getAuthorById(String authorId) throws ClassNotFoundException, SQLException;
    void addAuthor(String authorName) throws ClassNotFoundException, SQLException;
    void removeAuthorById(String id) throws ClassNotFoundException, SQLException;
    void modifyAuthorInfo(String authorName, String authorId) throws ClassNotFoundException, SQLException;
    }
