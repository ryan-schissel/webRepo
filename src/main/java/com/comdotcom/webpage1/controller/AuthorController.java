/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comdotcom.webpage1.controller;

import com.comdotcom.webpage1.model.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Ryan Schissel
 */
@WebServlet(name = "AuthorController", urlPatterns = {"/AuthorController"})
public class AuthorController extends HttpServlet {

    private String dbStrategyClassName;
    private String daoClassName;
    private String jndiName;
    private static final String AUTHOR_NAME_ADD = "authorNameAdd";
    private static final String AUTHOR_NAME_EDIT = "authorNameEdit";
    private static final String DATE_ADDED = "dateAdded";
    private static final String TABLE_NAME = "author";
    private static final String AUTHOR_ID = "authorId";
    private static final String SELECTED_AUTHOR_ID = "selectedAuthorId";
    private static final String ERR_PAGE = "/errorpage.html";
    private static final String ACTION = "action";
    private static final String LIST_PAGE = "/authorList.jsp";
    private static final String LIST_ACTION = "list";
    private static final String ADD_ACTION = "add";
    private static final String EDIT_ACTION = "edit";
    private static final String DELETE_ACTION = "delete";
    private String driverClass; //= "com.mysql.jdbc.Driver";
    private String url; // = "jdbc:mysql://localhost:3306/book";
    private String userName; // = "root";
    private String password; // = "admin"

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDateTime update = LocalDateTime.now();
        response.setContentType("text/html;charset=UTF-8");
        String destination = LIST_PAGE;
        try {
            AuthorService as = injectDependenciesAndGetAuthorService();
            String action = request.getParameter(ACTION);
            switch (action) {
                case ADD_ACTION:
                    addAuthor(as, request);
                    refreshList(as, request);
                    break;
                case EDIT_ACTION:
                    editAuthor(as, request);
                    refreshList(as, request);
                    break;
                case LIST_ACTION:
                    refreshList(as, request);
                    break;
                case DELETE_ACTION:
                    removeAuthor(as, request);
                    refreshList(as, request);
                    break;
                default:
                    refreshList(as, request);
                    break;
            }

        } catch (Exception ex) {
            request.setAttribute("errMsg", ex.getMessage());
        }

        RequestDispatcher dispatcher
                = getServletContext().getRequestDispatcher(destination);
        dispatcher.forward(request, response);

    }

    /*
        This helper method just makes the code more modular and readable.
        It's single responsibility principle for a method.
     */
    private AuthorService injectDependenciesAndGetAuthorService() throws Exception {
        // Use Liskov Substitution Principle and Java Reflection to
        // instantiate the chosen DBStrategy based on the class name retrieved
        // from web.xml
        Class dbClass = Class.forName(dbStrategyClassName);
        // Use Java reflection to instanntiate the DBStrategy object
        // Note that DBStrategy classes have no constructor params
        DBStrategy db = (DBStrategy) dbClass.newInstance();

        // Use Liskov Substitution Principle and Java Reflection to
        // instantiate the chosen DAO based on the class name retrieved above.
        // This one is trickier because the available DAO classes have
        // different constructor params
        AuthorDaoStrategy authorDao = null;
        Class daoClass = Class.forName(daoClassName);
        Constructor constructor = null;

        // This will only work for the non-pooled AuthorDao
        try {
            constructor = daoClass.getConstructor(new Class[]{
                DBStrategy.class, String.class, String.class, String.class, String.class
            });
        } catch (NoSuchMethodException nsme) {
            // do nothing, the exception means that there is no such constructor,
            // so code will continue executing below
        }

        // constructor will be null if using connectin pool dao because the
        // constructor has a different number and type of arguments
        if (constructor != null) {
            // conn pool NOT used so constructor has these arguments
            Object[] constructorArgs = new Object[]{
                db, driverClass, url, userName, password
            };
            authorDao = (AuthorDaoStrategy) constructor
                    .newInstance(constructorArgs);

        } else {
            /*
             Here's what the connection pool version looks like. First
             we lookup the JNDI name of the Glassfish connection pool
             and then we use Java Refletion to create the needed
             objects based on the servlet init params
             */
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup(jndiName);
            constructor = daoClass.getConstructor(new Class[]{
                DataSource.class, DBStrategy.class
            });
            Object[] constructorArgs = new Object[]{
                ds, db
            };

            authorDao = (AuthorDaoStrategy) constructor
                    .newInstance(constructorArgs);
        }

        return new AuthorService(authorDao);
    }

    private void editAuthor(AuthorService as, HttpServletRequest request) throws ClassNotFoundException, SQLException {
        String authorName = request.getParameter(AUTHOR_NAME_EDIT);
        String authorId = request.getParameter(SELECTED_AUTHOR_ID);
        if (!authorName.isEmpty() || authorName == null || authorId.isEmpty() || authorId == null) {
            as.modifyAuthorInfo(authorName, authorId);
        }
    }

    private void addAuthor(AuthorService as, HttpServletRequest request) throws ClassNotFoundException, SQLException {
        String authorName = request.getParameter(AUTHOR_NAME_ADD);
        if (!authorName.isEmpty() || authorName == null) {
            as.addAuthor(authorName);
        }
    }

    private void removeAuthor(AuthorService as, HttpServletRequest request) throws ClassNotFoundException, SQLException {
        String authorId = request.getParameter(SELECTED_AUTHOR_ID);
        if (!authorId.isEmpty() || authorId == null) {
            as.removeAuthorById(authorId);
        }
    }

    private void refreshList(AuthorService as, HttpServletRequest request) throws SQLException, ClassNotFoundException {
        List<Author> authors = as.getAuthors(50);
        request.setAttribute("authors", authors);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    @Override
    public void init() throws ServletException {
        driverClass = getServletContext().getInitParameter("db.driver.class");
        url = getServletContext().getInitParameter("db.bookUrl");
        userName = getServletContext().getInitParameter("db.username");
        password = getServletContext().getInitParameter("db.password");
        dbStrategyClassName = getServletContext().getInitParameter("dbAccessor");
        daoClassName = getServletContext().getInitParameter("authorDao");
        jndiName = getServletContext().getInitParameter("connPoolName");

    }// </editor-fold>

}
