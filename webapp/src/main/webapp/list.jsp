<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>codename</th>
        <th>info</th>
        <th>issue date</th>
    </tr>
    </thead>
    <c:forEach items="${mission}" var="mission">
        <tr>
            <td><c:out value="${mission.codename}"/></td>
            <td><c:out value="${mission.info}"/></td>
            <td><c:out value="${mission.issueDate}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/mission/delete?id=${mission.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Smazat"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Zadejte knihu</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/mission/add" method="post">
    <table>
        <tr>
            <th>codename:</th>
            <td><input type="text" name="codename" value="<c:out value='${param.codename}'/>"/></td>
        </tr>
        <tr>
            <th>info:</th>
            <td><input type="text" name="info" value="<c:out value='${param.info}'/>"/></td>
        </tr>
        <tr>
            <th>issue date:</th>
            <td><input type="text" name="issueDate" value="<c:out value='${param.issueDate}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Zadat" />
</form>

</body>
</html>