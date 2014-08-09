<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>iPED - ユーザー一覧</title>
</head>
<body>
    <h1>ユーザー一覧</h1>
    <form action='<%=request.getAttribute("javax.servlet.forward.request_uri")%>' method="POST">
        <table>
            <tr>
                <th>ユーザID</th>
                <td>
                    <input type="text" name="userId" value='<c:out value="${user.userId}" />'/>
                    <c:if test="${user.userIdValid}"><span class="error">NG</span></c:if>
                </td>
            </tr>
            <tr>
                <th>苗字</th>
                <td>
                    <input type="text" name="lastName" value='<c:out value="${user.lastName}" />'/>
                    <c:if test="${user.lastNameValid}"><span class="error">NG</span></c:if>
                </td>
            </tr>
            <tr>
                <th>名前</th>
                <td>
                    <input type="text" name="firstName" value='<c:out value="${user.firstName}" />'/>
                    <c:if test="${user.firstNameValid}"><span class="error">NG</span></c:if>
                </td>
            </tr>
            <tr>
                <th>パスワード</th>
                <td>
                    <input type="password" name="password" />
                    <c:if test="${user.passwordValid}"><span class="error">NG</span></c:if>
                </td>
            </tr>
        </table>
        <p>
            <input type="submit" id="save" value="登録する" />
        </p>
    </form>
    <ul>
        <li><a href="/backend/users">ユーザー一覧に戻る</a></li>
        <li><a href="/backend/index">トップに戻る</a></li>
    </ul>
</body>
</html>