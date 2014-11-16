<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.iped_system.iped.common.RoleType" %>
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
  <c:set var="PATIENT" value="<%= RoleType.PATIENT %>" />
  <c:if test="${authInfo.role != PATIENT}">
    <li role="presentation"><a href="/web/secure/meeting">ミーティング</a></li>
  </c:if>
  <li role="presentation" class="active"><a href="#">インタビュー</a></li>
</ul>
<div class="container">
<c:forEach var="talk" items="${talks}">
  <div class="panel">
    <div class="panel-body">
      <c:if test='${!empty talk.youText}'>
      　<div>
          <c:if test='${!empty talk.faceId}'>
            <img class="face" src='/web/face/<c:out value="${talk.faceId}" />' />
          </c:if>
          <c:if test='${empty talk.faceId}'>
            <img class="face" src="/web/image/anonymous.png" />
          </c:if>
        </div>
        <div style="margin-left: 70px;">
          <b><c:out value="${talk.authorName}"/></b><br />
          <div class="you_box"><c:out value="${talk.youText}"/></div>
        </div>
      </c:if>
      <c:if test='${!empty talk.meText}'>
        <div class="text-right" style="margin-right: 30px;">
          <div class="me_box text-left"><c:out value="${talk.meText}"/></div>
        </div>
      </c:if>
    </div>
    <div class="panel-footer">
      <p><fmt:formatDate value="${talk.createdAt}" pattern="yyyy/M/d H:mm" timeZone="Asia/Tokyo" /></p>
    </div>
  </div>
</c:forEach>
</div>
<div class="container">
<form id="talkForm" class="form-inline" role="form" action="/web/interview" method="post">
  <input id="tokenId" type="hidden" value='<c:out value="${token}" />' />
  <div class="form-group">
    <label class="sr-only" for="talk">発言</label>
    <textarea id="talk" class="form-control" rows="2" name="talk"></textarea>
  </div>
  <button id="talkButton" type="submit" class="btn btn-primary">発言</button>
</form>
</div>
<jsp:include page="/WEB-INF/web/partial/footer.jsp"/>
<script src="/web/js/interview.js"></script>
</body>
</html>
