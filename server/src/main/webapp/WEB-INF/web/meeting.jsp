<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <jsp:include page="/WEB-INF/web/partial/header.jsp"/>
    <title>iPED - メイン画面</title>
</head>
<body>
<nav class="navbar navbar-default" role="navigation">
  <p class="navbar-text">iPED - メイン画面</p>
</nav>
<ul class="nav nav-tabs" role="tablist">
  <li role="presentation" class="active"><a href="#">ミーティング</a></li>
  <li role="presentation"><a href="/web/secure/interview">インタビュー</a></li>
</ul>
<div class="container">
<form id="remarkForm" class="form-inline" role="form" action="/web/meeting" method="post">
  <input id="tokenId" type="hidden" value='<c:out value="${token}" />' />
  <div class="form-group">
    <label class="sr-only" for="remark">発言</label>
    <textarea id="remark" class="form-control" rows="2" name="remark"></textarea>
  </div>
  <button id="remarkButton" type="submit" class="btn btn-primary">発言</button>
</form>
</div>
<div class="container">
<c:forEach var="remark" items="${remarks}">
  <div class="panel">
    <div class="panel-heading">
      <c:if test='${!empty remark.faceId}'>
        <img class="face" src='/web/face/<c:out value="${remark.faceId}" />' />
      </c:if>
      <c:if test='${empty remark.faceId}'>
        <img class="face" src="/web/image/anonymous.png" />
      </c:if>
      <p>
        <b><c:out value="${remark.authorName}"/></b><br />
        <fmt:formatDate value="${remark.createdAt}" pattern="yyyy/M/d H:mm" timeZone="Asia/Tokyo" />
      </p>
    </div>
    <div class="panel-body">
      <p><c:out value="${remark.text}"/></p>
      <div style="overflow: scroll">
      <c:forEach var="picture" items="${remark.pictureIdList}">
        <img class="picture" src='/web/face/<c:out value="${picture}" />' />
      </c:forEach>
      </div>
    </div>
  </div>
</c:forEach>
</div>
<jsp:include page="/WEB-INF/web/partial/footer.jsp"/>
<script src="/web/js/meeting.js"></script>
</body>
</html>
