<%--
    Document   : index
    Created on : Feb 6, 2017, 1:42:17 PM
    Author     : Ryan Schissel
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>Book Web App</title>
    </head>
    <body>
        <%
            ServletContext ctx = request.getServletContext();
            ctx.setAttribute("update", new Date());
        %>
        <div class="alert alert-success" role="alert">Book will be temporarily be down for an update on <fmt:formatDate value="${update}" pattern="yyyy-MM-dd HH:mm a" /></div>
        <nav class="navbar navbar-inverse">
            <jsp:include page="navbarHeader.jsp"/>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Action<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a id="edit" data-toggle="modal" data-target="#addModal">Add</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a id="edit" data-toggle="modal" data-target="#editModal">Edit</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a id="delete"  data-toggle="modal" data-target="#deleteModal">Delete</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a name="reload" href="AuthorController?action=list">Reload</a></li>
                        </ul>
                    </li>
                    <jsp:include page="accountMenu.jsp"/>
                </ul>
            </div>
        </nav>
        <form class="authorForm" method="post">
            <table border="2">
                <c:forEach items="${authors}" var="current">
                    <tr>
                        <td><input type="checkbox" name="selectedAuthorId" id="selectedAuthorId" value="${current.authorId}"><c:out value=" ${current.authorId}"/><td>
                        <td><c:out value="${current.authorName}"/></td>
                        <td><c:out value="${current.dateAdded}"/></td>
                    </tr>
                </c:forEach>
            </table>
            <div id="addModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add</h4>
                            <div class="modal-body">
                                <label for="authorName" class="ui-hidden-accessible">Author Name:</label>
                                <input type="text" name="authorNameAdd" id="authorNameAdd" placeholder="Author Name"><br
                            </div>
                            <div class="modal-footer">
                                <input type="button" name="add" class="btn btn-default" onclick='$(".authorForm").attr("action", "AuthorController?action=add").submit();' data-dismiss="modal" value="Save"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="editModal" class="modal fade" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Edit</h4>
                        <div class="modal-body">
                            <label for="authorName" class="ui-hidden-accessible">Author Name:</label>
                            <input type="text" name="authorNameEdit" id="authorNameEdit" placeholder="Author Name"><br>
                        </div>
                        <div class="modal-footer">
                            <input type="button" name="edit" class="btn btn-default" onclick='$(".authorForm").attr("action", "AuthorController?action=edit").submit();' data-dismiss="modal" value="Save"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="deleteModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Delete</h4>
                        <div class="modal-body">
                            <p>Are you sure you want to delete the selected author?<br></p>
                        </div>
                        <div class="modal-footer">
                            <input type="button" name="delete" class="btn btn-default" onclick='$(".authorForm").attr("action", "AuthorController?action=delete").submit();' data-dismiss="modal" value="Yes"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    ${errMsg}
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="resources/js/bookwebapp.js" type="text/javascript"></script>
</body>
</html>
