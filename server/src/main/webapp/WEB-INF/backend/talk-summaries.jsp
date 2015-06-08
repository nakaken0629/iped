<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - インタビュー投稿統計更新</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <div class="page-header">
        <h1>インタビュー投稿統計更新</h1>
    </div>
    <form action="/backend/talksummaries/" method="POST">
        <p><input type="submit" value="投稿統計の更新" /></p>
        <c:if test="${pageContext.request.method=='POST'}">
            <p>更新件数 = <c:out value="${count}" /></p>
            <p>件数が1件以上ある場合は、再更新してください</p>
        </c:if>
    </form>

    <table class="table">
        <tr>
            <th>ユーザID</th>
            <th>日付</th>
            <th>発言数</th>
            <th>ピクトグラム数</th>
            <th>写真投稿数</p>
        </tr>
        <c:forEach var="summary" items="${summaries}">
            <tr>
                <td>
                    <c:out value="${summary.userId}"/>
                </td>
                <td>
                    <c:out value="${summary.talkDate}"/>
                </td>
                <td>
                    <c:out value="${summary.talkCount}"/>
                </td>
                <td>
                    <c:out value="${summary.pictogramCount}"/>
                </td>
                <td>
                    <c:out value="${summary.pictureCount}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<jsp:include page="/WEB-INF/backend/partial/footer.jsp"/>
</body>
</html>