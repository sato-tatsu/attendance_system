<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="code">社員番号</label><br />
<input type="text" name="code" value="${employee.code}" />
<br /><br />

<label for="name">氏名</label><br />
<input type="text" name="name" value="${employee.name}" />
<br /><br />

<label for="password">パスワード</label><br />
<input type="password" name="password" />
<br /><br />

<label for="admin_flag">権限</label><br />
<select name="admin_flag">
    <c:forEach var="a" items="${admin}">
        <option value="${a.strong}"<c:if test="${employee.admin_flag == a.strong}"> selected</c:if>>${a.admin_str}</option>
    </c:forEach>
</select>
<br /><br />

<label for="paid">有給日数</label><br />
<input type="number" name="paid" value="${employee.paid}" required/>
<br /><br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">登録</button>