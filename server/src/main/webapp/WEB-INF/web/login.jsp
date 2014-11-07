<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<c:if test='${!empty globalError}'>
  <p class="text-danger">ユーザーID、またはパスワードが正しくありません</p>
</c:if>
<form role="form" action="/web/login" method="post">
  <div class="form-group">
    <label for="userId">ユーザーID</label>
    <input type="text" class="form-control" id="userId" name="userId" placeholder="ユーザーID" value='<c:out value="${userId}" />' >
  </div>
  <div class="form-group">
    <label for="password">パスワード</label>
    <input type="password" class="form-control" id="password" name="password" placeholder="パスワード">
  </div>
  <p class="text-center"><button type="submit" class="btn btn-primary">ログイン</button></p>
</form>
</div>
<jsp:include page="/WEB-INF/web/partial/footer.jsp"/>
</body>
</html>
