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
            <li><a href="/backend/talks">インタビュー投稿一覧</a></li>
            <li><a href="/backend/talksummaries/">インタビュー統計情報更新</a></li>
        </ul>
    </div>
</div>
<jsp:include page="/WEB-INF/backend/partial/footer.jsp"/>
</body>
</html>
