package controller.attendance;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Attendance;
import model.Employee;
import model.RegularTime;
import model.analysis.ApproveAnalysis;
import model.analysis.AttendanceAnalysis;
import model.validators.AttendanceValidator;
import util.DBUtil;

/**
 * Servlet implementation class AttemdanceUpdateServlet
 */
@WebServlet("/attendance/update")
public class AttendanceUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId()))
        {
            EntityManager em = DBUtil.createEntityManager();

            Boolean check_validate_time_flag;

            Date d = Date.valueOf(request.getParameter("date"));
            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

            Attendance attendance = em.createNamedQuery("getSelectAttendance", Attendance.class).setParameter("employee", login_employee).setParameter("select",d).getSingleResult();
            Integer type = Integer.parseInt(request.getParameter("type"));
            Boolean admin_flag = Boolean.parseBoolean(request.getParameter("admin_flag"));

            // 有給の解析
            AttendanceAnalysis.updatePaid(type, attendance);
            // 承認ステータス遷移
            Integer approve = attendance.getApprove();
            approve = ApproveAnalysis.getNextApproveState(approve, admin_flag);
            attendance.setApprove(approve);

            attendance.setDate(d);
            attendance.setType(Integer.parseInt(request.getParameter("type")));
            // フォーム入力では秒単位がなくsetでこけてしまうため、秒まで付与する処理
            // 最初に5文字抜き出しているのは、input初期値のままsubmitされた値は秒まで
            // あるため、処理を統一化するため。
            String begin_time = request.getParameter("begin_time").substring(0, 5) + ":00";
            String finish_time = request.getParameter("finish_time").substring(0, 5) + ":00";
            attendance.setBegin_time(Time.valueOf(begin_time));
            attendance.setFinish_time(Time.valueOf(finish_time));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            attendance.setUpdated_at(currentTime);
            // 定時データの取得
            RegularTime rt = em.find(RegularTime.class, login_employee.getRegular_type());
            // 勤怠時間の計算
            check_validate_time_flag = AttendanceAnalysis.calucAttendance_time(attendance, rt);

            // バリデーションチェック
            List<String> errors = AttendanceValidator.validator(attendance.getBegin_time(), attendance.getFinish_time(), attendance.getEmployee().getPaid(), check_validate_time_flag);
            if (errors.size() > 0)
            {
                em.close();

                request.getSession().setAttribute("errors", errors);
            }
            else
            {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush", "更新が完了しました。");
            }
            response.sendRedirect(request.getContextPath() + "/attendance/index?date=" + d.toString());
        }
    }

}
