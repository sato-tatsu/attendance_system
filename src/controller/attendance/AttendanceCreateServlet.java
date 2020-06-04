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
import model.analysis.AttendanceAnalysis;
import model.validators.AttendanceValidator;
import util.DBUtil;

/**
 * Servlet implementation class AttendanceCreateServlet
 */
@WebServlet("/attendance/create")
public class AttendanceCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceCreateServlet() {
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

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
            Employee employee = em.find(Employee.class, login_employee.getId());
            Date d = Date.valueOf(request.getParameter("date"));
            Attendance at = new Attendance();
            Integer type = Integer.parseInt(request.getParameter("type"));

            at.setEmployee(employee);

            // 有給の計算
            AttendanceAnalysis.updatePaid(type, at);

            at.setDate(d);
            at.setType(type);
            at.setApprove(2);
            // フォーム入力では秒単位がなくsetでこけてしまうため、秒まで付与する処理
            // 最初に5文字抜き出しているのは、input初期値のままsubmitされた値は秒まで
            // あるため、処理を統一化するため。
            String begin_time = request.getParameter("begin_time").substring(0, 5) + ":00";
            String finish_time = request.getParameter("finish_time").substring(0, 5) + ":00";
            at.setBegin_time(Time.valueOf(begin_time));
            at.setFinish_time(Time.valueOf(finish_time));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            at.setCreated_at(currentTime);
            at.setUpdated_at(currentTime);
            // 勤怠時間の計算
            check_validate_time_flag = AttendanceAnalysis.calucAttendance_time(at);

            // バリデーションチェック
            List<String> errors = AttendanceValidator.validator(at.getBegin_time(), at.getFinish_time(), at.getEmployee().getPaid(), check_validate_time_flag);
            if (errors.size() > 0)
            {
                em.close();

                request.getSession().setAttribute("errors", errors);
            }
            else
            {
                em.getTransaction().begin();
                em.persist(at);
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush", "更新が完了しました。");
            }

            response.sendRedirect(request.getContextPath() + "/attendance/index?date=" + d.toString());
        }
    }

}
