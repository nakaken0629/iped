<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - 新しいユーザの作成</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <h1>新しいユーザの作成</h1>

    <form action='<%=request.getAttribute("javax.servlet.forward.request_uri")%>' method="POST"
          role="form">
        <div class="form-group">
            <label for="userId">ユーザID<span class="text-info">（必須）</span></label>
            <input type="text" class="form-control" id="userId" placeholder="例：yasui.hiroki"
                   name="userId" value='<c:out value="${user.userId}" />'>
        </div>
        <div class="form-group">
            <label for="lastName">苗字<span class="text-info">（必須）</span></label>
            <input type="text" class="form-control" id="lastName" placeholder="例：安井"
                   name="lastName" value='<c:out value="${user.lastName}" />'>
        </div>
        <div class="form-group">
            <label for="firstName">名前<span class="text-info">（必須）</span></label>
            <input type="text" class="form-control" id="firstName" placeholder="例：浩樹"
                   name="firstName" value='<c:out value="${user.firstName}" />'>
        </div>
        <div class="form-group">
            <label for="password">パスワード<span class="text-info">（必須）</span></label>
            <input type="password" class="form-control" id="password" name="password">
        </div>
        <div class="form-group">
            <label for="firstName">役割<span class="text-info">（必須）</span></label>
            <c:forEach var="role" items="${roles}">
                <label class="radio-inline">
                    <input type="radio" name="role" value='<c:out value="${role}" />'
                    <c:if test="${role == user.role}"> checked="checked"</c:if>>
                    <c:out value="${role}"/>
                </label>
            </c:forEach>
        </div>
        <div class="form-group">
            <label for="patientId">担当患者ID<span class="text-info">（役割が患者以外のとき必須）</span></label>
            <input type="text" class="form-control" id="patientId" placeholder="例：nakagaki.kenji"
                   name="patientId" value='<c:out value="${user.patientId}" />'>
        </div>
        <p>
            <input type="submit" class="btn btn-primary" value="登録する"/>
        </p>
    </form>
    <ul>
        <li><a href="/backend/users">ユーザ一覧に戻る</a></li>
        <li><a href="/backend/index">トップに戻る</a></li>
    </ul>
</div>
</body>
</html>