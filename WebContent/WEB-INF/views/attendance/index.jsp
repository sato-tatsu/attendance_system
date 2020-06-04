<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <div class="side">
            <c:choose>
                <c:when test="${calendar.select_attendance == null}">
                    <form id="attendance_form" method="post" action="<c:url value='/attendance/create'/>">
                        <c:import url="_form.jsp" />
                    </form>
                </c:when>
                <c:otherwise>
                    <form id="attendance_form" method="post" action="<c:url value='/attendance/update'/>">
                        <c:import url="_form.jsp" />
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="main">
            <div class="main-date">
                <h3><fmt:formatDate value="${calendar.select_date}" pattern="yyyy"/>年</h3>
                <h2 class="month"><fmt:formatDate value="${calendar.select_date}" pattern="M"/>月</h2>
                <div class="prev-next">
                    <a href="<c:url value='/attendance/index?date=${calendar.prev_month}'/>">前の月へ</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value='/attendance/index?date=${calendar.next_month}'/>">次の月へ</a>&nbsp;&nbsp;&nbsp;&nbsp;
                </div>
            </div>
            <div class="total">
                <table class="total_1">
                    <tbody>
                        <tr>
                            <td class="head">社員番号</td>
                            <td><c:out value="${login_employee.code}"/></td>
                            <td class="head">社員名</td>
                            <td><c:out value="${login_employee.name}"/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="total_2">
                    <tbody>
                        <tr>
                            <td class="head">所定時間内勤務(h)</td>
                            <td><c:out value="${regular}"/></td>
                            <td class="head">所定時間外勤務(h)</td>
                            <td><c:out value="${over}"/></td>
                            <td class="head">欠勤(h)</td>
                            <td><c:out value="${absence}"/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="total_3">
                    <tbody>
                        <tr>
                            <td class="head">有給取得日数</td>
                            <td><c:out value="${paid}"/></td>
                            <td class="head">有給残日数</td>
                            <td><c:out value="${rema_paid}"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <br>

            <br>
            <table id="calender_format">
                <tbody>
                    <tr>
                        <th class="sunday">日</th>
                        <th class="monday">月</th>
                        <th class="tuesday">火</th>
                        <th class="wednsday">水</th>
                        <th class="thursday">木</th>
                        <th class="friday">金</th>
                        <th class="saturday">土</th>
                    </tr>
                    <c:forEach var="week" items="${calendar.date}" varStatus="w_s">
                        <tr>
                            <c:forEach var="day" items="${week}" varStatus="d_s">
                                <c:choose>
                                    <c:when test="${day != null}">
                                        <td valign="top">
                                            <div>
                                                <p class="day"><a class="day" href="<c:url value='/attendance/index?date=${day}'/>"><fmt:formatDate value="${day}" pattern="d"/></a></p>
                                                <p class="approve"><c:if test="${calendar.attendance[w_s.index][d_s.index].approve != 1}">${approve[calendar.attendance[w_s.index][d_s.index].approve-1].state}</c:if></p>
                                            </div>

                                            <p class="time">
                                               <c:if test="${calendar.attendance[w_s.index][d_s.index].begin_time != '00:00:00' && calendar.attendance[w_s.index][d_s.index].finish_time != '00:00:00'}">
                                                    <fmt:formatDate value="${calendar.attendance[w_s.index][d_s.index].begin_time}" pattern="HH:mm"/>
                                               </c:if>
                                                -
                                                <c:if test="${calendar.attendance[w_s.index][d_s.index].begin_time != '00:00:00' && calendar.attendance[w_s.index][d_s.index].finish_time != '00:00:00'}">
                                                    <fmt:formatDate value="${calendar.attendance[w_s.index][d_s.index].finish_time}" pattern="HH:mm"/>
                                                </c:if>
                                            </p>
                                            <p class="type">${type[calendar.attendance[w_s.index][d_s.index].type].type_str}</p>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>  ―</td>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <br>
        <br>
    </c:param>
</c:import>


