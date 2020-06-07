<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${errors != null}">
            <div id="flush_error">
                <c:forEach var="error" items="${errors}">
                    <c:out value="${error}" /><br />
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}" />
            </div>
        </c:if>
        <h2>従業員 一覧</h2>
        <form id="regulartime_form" method="post" action="<c:url value='/employees/regulartime/update' />">
            <input type="hidden" name="_token" value="${_token}" />
            <button id="regist"type="submit" disabled>定時登録</button>
        </form>
        <br><br>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>定時選択</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.name}" /></td>
                        <td>
                            <select id="type${employee.id}" name="type"  onchange="addUpdateForm(${employee.id})">
                                <c:forEach var="t" items="${regular_time}">
                                    <option value="${t.id}"<c:if test="${employee.regular_type == t.id}"> selected</c:if>>${t.type}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${employee.delete_flag == 1}" >
                                    (削除済み)
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/show?id=${employee.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${employees_count} 件) <br>
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' /> "><c:out value="${i}"/></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/employees/new'/>">新規従業員の登録</a></p>

        <script>

            function addUpdateForm(id)
            {
                var select = document.getElementById('type'+id);
                var type = select.value;
                var registButton = document.getElementById("regist");

                var type_id = document.getElementById('type_id' + id);
                if (type_id == null)
                {
                    // 該当IDのInputタグが見つからないため、追加
                    addFormInput(id, type);
                }
                else
                {
                    // 該当IDのtype値だけ変更
                    type_id.value = type;
                }

                registButton.disabled = false;
            }

            function addFormInput(id, type)
            {
                var form = document.getElementsByTagName("form");
                var input1 = document.createElement('input');
                input1.id = ""
                input1.type = "hidden";
                input1.name = "id[]";
                input1.value = id;
                form.item(0).appendChild(input1);
                var input2 = document.createElement('input');
                input2.id = "type_id"+id;
                input2.type = "hidden";
                input2.name = "type[]";
                input2.value = type;
                form.item(0).appendChild(input2);
            }

        </script>
    </c:param>
</c:import>