<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
<c:if test='${method != "new"}'>
    <form id="faceForm" action='<%= blobstoreService.createUploadUrl("/backend/user/faceupload") %>' method="POST" enctype="multipart/form-data" role="form">
        <div class="form-group">
            <label for="face">顔アイコン</label>
            <input type="hidden" name="userId" value='<c:out value="${user.userId}" />'>
            <input type="file" class="form-control" name="face" />
            <img src='/backend/face/<c:out value="${user.faceId}" />' />
            <input type="button" name="register" class="btn btn-success" value="アップロード"/>
        </div>
    </form>
</c:if>
<form id="userForm" action='<%=request.getAttribute("javax.servlet.forward.request_uri")%>' method="POST" role="form">
    <div class="form-group">
        <label for="userId">ユーザID<span class="text-info">（必須）</span></label>
        <c:if test='${method == "new"}'>
            <input type="text" class="form-control" id="userId" placeholder="例：yasui.hiroki"
                   name="userId" value='<c:out value="${user.userId}" />'>
        </c:if>
        <c:if test='${method != "new"}'>
            <p class="form-control-static" id="userId">
                <c:out value="${user.userId}"/>
            </p>
            <input type="hidden" name="userId" value='<c:out value="${user.userId}" />'>
        </c:if>
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
                <c:if test="${role == user.role}"> checked="checked"</c:if>
                >
                <c:out value="${role.displayName}"/>
            </label>
        </c:forEach>
    </div>
    <div class="form-group">
        <label for="patientId">担当患者ID<span class="text-info">（役割が患者のときはユーザIDと同じ）</span></label>
        <input type="text" class="form-control" id="patientId" placeholder="例：nakagaki.kenji"
               name="patientIdList" value='<c:forEach var="patientId" items="${user.patientIdList}" varStatus="status"><c:out value="${patientId}" /><c:if test="${!status.last}">,</c:if></c:forEach>' />
    </div>
    <p>
        <input type="submit" name="register" class="btn btn-primary" value="登録する"/>
    </p>
    <c:if test='${method != "new"}'>
        <p>
            <input type="submit" name="delete" class="btn btn-danger" value="削除する"/>
            <span class="text-danger">元に戻せません！！</span>
        </p>
    </c:if>
</form>
