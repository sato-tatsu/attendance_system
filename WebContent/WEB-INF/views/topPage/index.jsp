<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>マイページ</h2>
        <br>
        <div class="news">
            <h3>おしらせ</h3>
            <ul style="list-style: disc;">
                <c:forEach var="news" items="${news}">
                    <li>${news}</li>
                </c:forEach>
            </ul>
        </div>

    </c:param>
</c:import>


