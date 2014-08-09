<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>iPED - ユーザー一覧</title>
</head>
<body>
    <h1>ユーザー一覧</h1>
    <table>
        <tr>
            <th>ユーザID</th>
            <th>名字</th>
            <th>名前</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td><c:out value="${user.userId}" /></td>
                <td><c:out value="${user.lastName}" /></td>
                <td><c:out value="${user.firstName}" /></td>
            </tr>
        </c:forEach>
    </table>
    <ul>
        <li><a href="/backend/index">戻る</a></li>
    </ul>
</body>
</html>