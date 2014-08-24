<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - ユーザの編集</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <h1>ユーザの編集</h1>
    <c:set var="method" value="edit" scope="request" />
    <jsp:include page="/WEB-INF/backend/partial/userform.jsp"/>
</div>
</body>
</html>