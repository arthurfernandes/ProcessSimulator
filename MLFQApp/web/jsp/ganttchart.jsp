<%-- 
    Document   : ganttchart
    Created on : Jun 9, 2014, 5:06:42 AM
    Author     : arthurfernandes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
        <title>Gantt Chart</title>
    </head>
    <body>
        <div class="container center-block">
            <h1>Multilevel Feedback Queue</h1>
            <h3>Uso da CPU:</h3><br>
            <span>0 </span>
            <c:forEach items="${listaUsoCPU}" var="cpuNode">
                    
                <span>| ${cpuNode.proc.nome} </span><span> | ${cpuNode.finalProc}</span>
            
                                
            </c:forEach>
                <br>
               <h3>IO</h3>
               
            <c:forEach items="${listaUsoIO}" var="ioNode">
                ${ioNode.inicioProc}<span> | ${ioNode.proc.nome}</span>
                
                <br>
            </c:forEach>
            
        </div>
        
    </body>
</html>
