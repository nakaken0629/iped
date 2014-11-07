<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/web/partial/header.jsp"/>
    <title>iPED - ログイン</title>
</head>
<body>
<nav class="navbar navbar-default" role="navigation">
  <p class="navbar-text">iPED - ログイン画面</p>
</nav>
<div class="container">
<form role="form" action="/web/login" method="post">
  <div class="form-group">
    <label for="userId">ユーザーID</label>
    <input type="text" class="form-control" id="userId" placeholder="ユーザーID">
  </div>
  <div class="form-group">
    <label for="password">パスワード</label>
    <input type="password" class="form-control" id="password" placeholder="パスワード">
  </div>
  <p class="text-center"><button type="submit" class="btn btn-primary">ログイン</button></p>
</form>
</div>
<jsp:include page="/WEB-INF/web/partial/footer.jsp"/>
</body>
</html>
