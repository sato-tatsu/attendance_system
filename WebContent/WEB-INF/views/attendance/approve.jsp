<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>承認画面</h2>

        <c:choose>
            <c:when test="${errors != null}">
                <div id="flush_error">
                    <c:forEach var="error" items="${errors}">
                        <c:out value="${error}" /><br />
                    </c:forEach>
                </div>
            </c:when>
            <c:when test="${flush != null}">
                <div id="flush_success">
                    <c:out value="${flush}" />
                </div>
            </c:when>
        </c:choose>

        <form id="approve_form" method="post" action="<c:url value='/attendance/approve/update' />">
            <input type="hidden" name="_token" value="${_token}" />
            <input type="hidden" name="admin_flag" value="true" />
            <button id="allFunc"type="submit" disabled>一括処理</button>
        </form>
        <br><br>
        <table id="approve_list">
            <tbody>
                <tr>
                    <th class="select">選択</th>
                    <th class="employee_name">従業員名</th>
                    <th class="attendance_date">日付</th>
                    <th class="time">時刻</th>
                    <th class="attendance_type">種別</th>
                    <th class="total_time">勤務時間(h)</th>
                    <th class="over_time">残業時間(h)</th>
                    <th class="absence_time">欠勤時間(h)</th>
                    <th class="approve_sta">状態</th>
                    <th class="approve">操作</th>
                </tr>
                <c:forEach var="attendance" items="${attendance}" varStatus="status">
                    <tr id="attendance${status.index}">
                        <td class="select"><input class="approve_check" id="check${status.index}" value="${attendance.id}" type="checkBox" onchange="changeForm(${status.index})"></td>
                        <td class="employee_name">${attendance.employee.name}</td>
                        <td class="attendance_date"><fmt:formatDate value="${attendance.date}" pattern="yyyy/MM/dd (E)"/></td>
                        <td class="time"><fmt:formatDate value="${attendance.begin_time}" pattern="HH:mm"/>-<fmt:formatDate value="${attendance.finish_time}" pattern="HH:mm"/></td>
                        <td class="attendance_type">${type[attendance.type].type_str}</td>
                        <td class="total_time">${attendance.regulation_time + attendance.overtime}</td>
                        <td class="over_time">${attendance.overtime}</td>
                        <td class="absence_time">${attendance.absence}</td>
                        <td class="approve_sta">${approve[attendance.approve-1].state}</td>
                        <td class="approve"><button onclick="funcApprove(${attendance.id})">${approve[attendance.approve-1].admin_action}</button></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <script>

            function changeForm(id)
            {
                var thisCheckBox = document.getElementById("check"+id);
                var value = thisCheckBox.value;

                if (thisCheckBox.checked)
                {
                    addFormInput(value);
                }
                else
                {
                    removeFormInput(value);
                }

                checkAllChecked();
            }

            function checkAllChecked()
            {
                var checkBox = document.getElementsByClassName("approve_check")
                var allFuncButton = document.getElementById("allFunc")
                for (var i = 0; i < checkBox.length; i++)
                {
                    if (checkBox[i].checked)
                    {
                        allFuncButton.disabled = false;
                        return;
                    }
                }
                allFuncButton.disabled = true;
            }

            function funcApprove(id)
            {
                addFormInput(id);
                document.forms[0].submit();
            }

            function addFormInput(id)
            {
                var input = document.createElement('input');
                input.type = "hidden";
                input.name = "attendance_id[]";
                input.value = id;
                var form = document.getElementsByTagName("form");
                form.item(0).appendChild(input);
            }

            function removeFormInput(id)
            {
                var inputs = document.getElementById("approve_form").getElementsByTagName("input");
                for (var i = 0; i < inputs.length; i++)
                {
                    if (inputs[i].value == id)
                    {
                        inputs[i].remove();
                        break;
                    }
                }
            }

        </script>
    </c:param>
</c:import>


