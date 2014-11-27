<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/backend/index">iPED - メイン画面</a>
        </div>
        <div class="collapse navbar-collapse">
            <div class="nav navbar-nav">
                <select id="patientId" class="form-control">
                    <c:forEach var="patient" items="${patients}">
                        <option value='<c:out value="${patient.userId}" />' <c:if test="${patientId == patient.userId}">selected="selected"</c:if>>
                          <c:out value="${patient.lastName} ${patient.firstName}" />
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div><!--/.nav-collapse -->
    </div>
</div>
