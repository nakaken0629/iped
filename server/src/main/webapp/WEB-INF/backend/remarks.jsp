<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>iPED - ミーティング投稿一覧</title>
</head>
<body>
    <h1>ミーティング投稿一覧</h1>
    <table>
        <tr>
            <th>名前</th>
            <th>患者ID</th>
            <th>テキスト</th>
        </tr>
        <c:forEach var="remark" items="${remarks}">
            <tr>
                <td><c:out value="${remark.authorName}" /></td>
                <td><c:out value="${remark.patientId}" /></td>
                <td><c:out value="${remark.text}" /></td>
            </tr>
        </c:forEach>
    </table>
    <ul>
        <li><a href="/backend/index">トップに戻る</a></li>
    </ul>
</body>
</html>