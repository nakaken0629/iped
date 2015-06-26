<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - インタビュー投稿一覧</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <div class="page-header">
        <h1>インタビュー投稿一覧</h1>
    </div>
    <table class="table">
        <tr>
            <th>ユーザID</th>
            <th>患者ID</th>
            <th>投稿日</th>
            <th>テキスト</th>
            <th>スタンプ</th>
            <th>写真</th>
        </tr>
        <c:forEach var="talk" items="${talks}">
            <tr>
                <td>
                    <c:out value="${talk.userId}"/>
                </td>
                <td>
                    <c:out value="${talk.patientId}"/>
                </td>
                <td>
                    <fmt:formatDate value="${talk.createdAt}" pattern="yyyy/MM/dd HH:mm:ss" timeZone="JST" />
                </td>
                <td>
                    <c:out value="${talk.text}"/>
                </td>
                <td>
                    <c:out value="${talk.pictogramKey}"/>
                </td>
                <td>
                    <c:out value="${talk.pictureId}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<jsp:include page="/WEB-INF/backend/partial/footer.jsp"/>
</body>
</html>