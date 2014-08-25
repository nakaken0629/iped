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
    <div class="page-header">
        <h1>新しいユーザの作成</h1>
    </div>
    <c:set var="method" value="new" scope="request"/>
    <jsp:include page="/WEB-INF/backend/partial/userform.jsp"/>
</div>
</body>
</html>