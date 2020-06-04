<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>勤怠管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="wrapper" >
            <div id="header">
                <div id="header_title">
                    <h1><a href="<c:url value='/'/>">E.A.System</a></h1>
                </div>
                <c:if test="${sessionScope.login_employee != null}">
                    <div id="employee_name">
                        <c:out value="${sessionScope.login_employee.name}"/>&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout'/>">ログアウト</a>
                    </div>
                    <div id="header_menu">
                        <a id="/" href="<c:url value='/' />">マイページ</a>&nbsp;&nbsp;
                        <a id=attendance href="<c:url value='/attendance/index'/>">勤怠入力</a>&nbsp;&nbsp;
                        <c:if test="${sessionScope.login_employee.admin_flag > 0}">
                            <a id=approve href="<c:url value='/attendance/approve/index'/>">勤怠承認</a>&nbsp;&nbsp;
                        </c:if>
                        <c:if test="${sessionScope.login_employee.admin_flag > 1}">
                            <a id=employees href="<c:url value='/employees/index'/>">従業員登録</a>&nbsp;&nbsp;
                        </c:if>
                    </div>
                    <script>
                        // pathを取得
                        var path = location.pathname;

                        var menus = document.getElementById('header_menu').getElementsByTagName("a");

                        // メニュー分ループ
                        for (var i = menus.length - 1; i >= 0; i--)
                        {
                            var id = menus[i].getAttribute("id");
                            if (path.match(id))
                            {// pathにidが含まれている
                                var menu = document.getElementById(id);
                                // タブのクラスをactにする。
                                menu.className = 'act';
                                break;
                            }
                        }
                    </script>
                </c:if>

            </div>
            <div id="content">
                ${param.content}
            </div>
            <div id="footer">
                by Tatsuya Sato
            </div>
        </div>

    </body>
</html>