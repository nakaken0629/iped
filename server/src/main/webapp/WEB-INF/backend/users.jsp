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
    <h1>ユーザ一覧</h1>
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
                    <input class="btn btn-info" type="button" value="編集" />
                    <input class="btn btn-danger" type="button" value="削除" />
                </td>
            </tr>
        </c:forEach>
    </table>
    <ul>
        <li><a href="/backend/users/new">新しいユーザの作成</a></li>
        <li><a href="/backend/index">トップに戻る</a></li>
    </ul>
</div>
</body>
</html>