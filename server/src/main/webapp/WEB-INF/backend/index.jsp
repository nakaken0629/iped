<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/backend/partial/header.jsp"/>
    <title>iPED - トップ</title>
</head>
<body>
<jsp:include page="/WEB-INF/backend/partial/menu.jsp"/>
<div class="container">
    <div class="starter-template">
        <h1>トップ</h1>
        <ul>
            <li><a href="/backend/users">ユーザ一覧</a></li>
            <li><a href="/backend/remarks">ミーティング投稿一覧</a></li>
        </ul>
    </div>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</body>
</html>
