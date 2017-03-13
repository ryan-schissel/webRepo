<%-- 
    Document   : login
    Created on : Mar 8, 2017, 10:39:00 AM
    Author     : Ryan Schissel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<li><a id=logOut data-toggle="modal" data-target="#logOutModal">Log Out</a></li>
<li><a id=logIn data-toggle="modal" data-target="#logInModal">Log In</a></li>
<div id="logOutModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Delete</h4>
                <div class="modal-body">
                    <p>Are you sure you want to log out?<br></p>
                    <input type="hidden" name="action" value="logout">
                </div>
                <div class="modal-footer">
                    <input type="button" name="delete" class="btn btn-default" data-dismiss="modal" value="No"/>
                    <button type="button" class="btn btn-default" href="AccountController?action=out" data-dismiss="modal">Yes</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="logInModal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Log in</h4>
                <form name="authorForm" class="authorForm">
                    <div class="modal-body">
                        <label for="userName" class="ui-hidden-accessible">User Name:</label>
                        <input type="text" required="true" name="userName" id="authorNameEdit" placeholder="User Name"><br>
                        <label for="password" class="ui-hidden-accessible">Password :</label>
                        <input type="text" required="true" name="password" id="authorNameEdit" placeholder="Password"><br>
                    </div>
                </form>
                <div class="modal-footer">
                    <input type="button" name="edit" class="btn btn-default" onclick='$(".authorForm").attr("action", "AccountController?action=in").submit();' data-dismiss="modal" value="Log In"/>
                </div>
            </div>
        </div>
    </div>
</div>
