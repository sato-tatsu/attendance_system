<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${errors != null}">
        <div id="flush_error">
            入力内容にエラーがあります。<br />
            <c:forEach var="error" items="${errors}">
                ・<c:out value="${error}" /><br />
            </c:forEach>
        </div>
    </c:when>
    <c:when test="${flush != null}">
        <div id="flush_success">
            <c:out value="${flush}" />
        </div>
    </c:when>
</c:choose>

<p class="side-date"><fmt:formatDate value="${calendar.select_date}" pattern="M月d日(E)"/></p>
<label for="type">種別</label><br>
<select id="type" name="type" onchange="changeLimitTime()">
    <c:forEach var="t" items="${type}">
        <option value="${t.type}"<c:if test="${calendar.select_attendance.type == t.type}"> selected</c:if>>${t.type_str}</option>
    </c:forEach>
</select>
<br><br>
<label for="begin_time">出勤時間</label><br>
<input id="begin_time" name="begin_time" type="time" value="" required/>
<br><br>
<label for="finish_time">退勤時間</label><br>
<input id="finish_time" name="finish_time" type="time" value="" required/>
<br><br>
<input type="hidden" name="_token" value="${_token}"/>
<input type="hidden" name="date" value="${calendar.select_date}"/>
<c:choose>
    <c:when test="${calendar.select_attendance.approve != 4}">
        <button type="submit">
            <c:choose>
                <c:when test="${calendar.select_attendance == null}">申請</c:when>
                <c:otherwise>${approve[calendar.select_attendance.approve-1].normal_action}</c:otherwise>
            </c:choose>
        </button>
    </c:when>
    <c:otherwise>
        <p>引戻し申請中</p>
    </c:otherwise>
</c:choose>


<script>
    // 種別変更時
    function changeLimitTime()
    {
        var select = document.getElementById('type');
        var type = select.value;
        var begin_time = document.getElementById("begin_time");
        var finish_time = document.getElementById("finish_time");

        select.removeAttribute("readonly");
        begin_time.removeAttribute("readonly");
        finish_time.removeAttribute("readonly");

        switch(type)
        {
            case '1':// AM休
                begin_time.value = "${break_finish}";
                finish_time.value = "${login_employee.regular_finish}";
                begin_time.setAttribute("min", "${break_start}");
                finish_time.removeAttribute("max");
                break;
            case '2':// PM休
                begin_time.value = "${login_employee.regular_start}";
                finish_time.value = "${break_start}";
                finish_time.setAttribute("max", "${break_finish}");
                begin_time.removeAttribute("min");
                break;
            case '3':// 有給休暇
            case '5':// 欠勤
                begin_time.value = "00:00";
                finish_time.value = "00:00";
                begin_time.setAttribute("readonly", null);
                finish_time.setAttribute("readonly", null);
                break;
            default:
                begin_time.value = "${login_employee.regular_start}";
                finish_time.value = "${login_employee.regular_finish}";
                begin_time.removeAttribute("min");
                finish_time.removeAttribute("max");
                break;
        }

        if ( "${calendar.select_attendance.approve}" != 1 && "${calendar.select_attendance.approve}" != 5 && "${calendar.select_attendance.approve}" != "")
        {
            for (var i = 0; i < 6; i++)
            {
                if(i != type)
                {
                    select.options[i].disabled = true;
                }
            }
            begin_time.setAttribute("readonly", null);
            finish_time.setAttribute("readonly", null);
        }

    }
    // ページ読み込み時
    function showAttendanceTime()
    {
        var select = document.getElementById('type');
        var type = select.value;
        var begin_time = document.getElementById("begin_time");
        var finish_time = document.getElementById("finish_time");

        select.removeAttribute("readonly");
        begin_time.removeAttribute("readonly");
        finish_time.removeAttribute("readonly");

        if ("${calendar.select_attendance}" == "")
        {
            var begin = "${login_employee.regular_start}";
            var finish = "${login_employee.regular_finish}";
        }
        else
        {
            begin = "${calendar.select_attendance.begin_time}";
            finish = "${calendar.select_attendance.finish_time}";
        }

        begin_time.value = begin;
        finish_time.value = finish;

        switch(type)
        {
            case '1':// AM休
                begin_time.setAttribute("min", "${break_finish}");
                finish_time.removeAttribute("max");
                break;
            case '2':// PM休
                finish_time.setAttribute("max", "${break_start}");
                begin_time.removeAttribute("min");
                break;
            case '3':// 有給休暇
            case '5':// 欠勤
                begin_time.setAttribute("readonly", null);
                finish_time.setAttribute("readonly", null);
                break;
            default:
                begin_time.removeAttribute("min");
                finish_time.removeAttribute("max");
                break;
        }

        if ( "${calendar.select_attendance.approve}" != 1 && "${calendar.select_attendance.approve}" != 5 && "${calendar.select_attendance.approve}" != "")
        {
            for (var i = 0; i < 6; i++)
            {
                if(i != type)
                {
                    select.options[i].disabled = true;
                }
            }
            begin_time.setAttribute("readonly", null);
            finish_time.setAttribute("readonly", null);
        }
    }
    showAttendanceTime();
</script>