<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - ミーティング発言一覧</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <div class="page-header">
        <h1>ミーティング発言一覧</h1>
    </div>
    <table class="table">
        <tr>
            <th>患者ID</th>
            <th>投稿者名</th>
            <th>投稿日</th>
            <th>テキスト</th>
        </tr>
        <c:forEach var="remark" items="${remarks}">
            <tr>
                <td>
                    <c:out value="${remark.patientId}"/>
                </td>
                <td>
                    <c:out value="${remark.authorName}"/>
                </td>
                <td>
                    <c:out value="${remark.createdAt}"/>
                </td>
                <td>
                    <c:out value="${remark.text}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<jsp:include page="/WEB-INF/backend/partial/footer.jsp"/>
</body>
</html>