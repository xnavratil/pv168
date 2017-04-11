<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>Name</th>
        <th>Born</th>
        <th>Recruitment date</th>
    </tr>
    </thead>
    <c:forEach items="${agents}" var="agent">
        <tr>
            <td><c:out value="${agent.name}"/></td>
            <td><c:out value="${agent.born}"/></td>
            <td><c:out value="${agent.recruitmentDate}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/agents/delete?id=${agent.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Smazat"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Add new agent</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/agents/add" method="post">
    <table>
        <tr>
            <th>Name:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>Born:</th>
            <td><input type="text" name="born" value="<c:out value='${param.born}'/>"/></td>
        </tr>
        <tr>
            <th>Recruitment date:</th>
            <td><input type="text" name="recruitmentDate" value="<c:out value='${param.recruitmentDate}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Submit" />
</form>

</body>
</html>