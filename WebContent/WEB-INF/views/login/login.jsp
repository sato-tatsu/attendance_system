<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>ログイン</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="login_header"></div>
        <div id="content">
            <c:if test="${hasError}">
                <div id="flush_error">
                    社員番号かパスワードが間違っています。
                </div>
            </c:if>
            <h2>ログイン</h2>
            <form method="post" action="<c:url value='/login'/>">
                <label for="id">社員番号</label><br />
                <input type="text" name="id" value="${code}" />
                <br /><br />

                <label for="password">パスワード</label><br />
                <input type="password" name="password" />
                <br /><br />

                <input type="hidden" name="_token" value="${_token}"/>
                <button type="submit">ログイン</button>
            </form>
        </div>
        <div id="footer">
            by Tatsuya Sato
        </div>
    </body>
</html>