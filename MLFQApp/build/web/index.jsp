<%-- 
    Document   : index
    Created on : Jun 9, 2014, 5:26:06 AM
    Author     : arthurfernandes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/pages.css" rel="stylesheet">
        <title>Multilevel Feedback Queue</title>
    </head>
    <body>
        <div class="container center-block ">
            <h1>Multilevel Feedback Queue</h1>
            <form action="ganttchart.html">
                <div class="table-responsive">
                    <table class="table" >
                        <caption class="caption">Lista de Processos</caption>
                        <thead>
                            <tr>
                                <th>
                                    Index
                                </th>
                                <th>
                                    Nome
                                </th>
                                <th>
                                    Burst
                                </th>
                                <th>
                                    I/O
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach begin="1" end="5" var="i">
                                <tr>
                                    <td>
                                        ${i}
                                    </td>
                                    <td>
                                        <textarea name="nome${i}" rows="1"></textarea>
                                    </td>
                                    <td>
                                        <input type="number" name="burst${i}">
                                    </td>
                                    <td>
                                        <input type="number" name="io${i}">
                                    </td>
                                </tr>
                            </c:forEach>
                            
                        </tbody>
                    </table>
                </div>
                
                
                <button type="submit" class="btn btn-primary center-block">Submit</button>
            </form>
        </div>
        
    </body>
</html>
