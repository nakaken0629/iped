<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - ユーザ一覧</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <div class="page-header">
        <h1>ユーザ一覧</h1>
    </div>
    <table class="table">
        <tr>
            <th>ユーザID</th>
            <th>苗字</th>
            <th>名前</th>
            <th>役割</th>
            <th>担当患者ID</th>
            <th>操作</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>
                    <c:out value="${user.userId}"/>
                </td>
                <td>
                    <c:out value="${user.lastName}"/>
                </td>
                <td>
                    <c:out value="${user.firstName}"/>
                </td>
                <td>
                    <c:out value="${user.role}"/>
                </td>
                <td>
                    <c:out value="${user.patientId}"/>
                </td>
                <td>
                    <a class="btn btn-info"
                       href='/backend/user/edit/<c:out value="${user.userId}"/>'>編集</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <ul>
        <li><a href="/backend/users/new">新しいユーザの作成</a></li>
    </ul>
</div>
</body>
</html>