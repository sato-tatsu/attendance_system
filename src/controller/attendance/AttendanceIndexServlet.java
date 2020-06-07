package controller.attendance;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Approve;
import model.Attendance;
import model.AttendanceType;
import model.Employee;
import model.RegularTime;
import model.analysis.AttendanceAnalysis;
import model.struct.CalendarStruct;
import util.DBUtil;

/**
 * Servlet implementation class AttendanceIndexServlet
 */
@WebServlet("/attendance/index")
public class AttendanceIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Calendar c = Calendar.getInstance(); // 現在日時で生成
        Date date = new Date(System.currentTimeMillis());  // 現在日時で生成
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        CalendarStruct calendar;
        Attendance select_attendance = null;
        List<Attendance> attendance;

        Double regular = 0.0;
        Double over     = 0.0;
        Double absence = 0.0;
        Double paid     = 0.0;
        Double rema_paid = 0.0;

        String _token = request.getSession().getId();

        try
        {
            // 指定がある場合はその日付に変更
            date   = Date.valueOf(request.getParameter("date"));
        }
        catch(IllegalArgumentException e)
        {
            // 指定がない場合はここに入り、何もしない
        }
        c.setTime(date);

        // カレンダーペイロードを生成
        calendar = new CalendarStruct(c);

        try
        {
            // 指定日の勤怠データを取得
            select_attendance = em.createNamedQuery("getSelectAttendance", Attendance.class).setParameter("employee", login_employee).setParameter("select",date).getSingleResult();
        }
        catch(NoResultException e)
        {

        }
        calendar.setSelect_attendance(select_attendance);
        // 該当月の勤怠データを取得
        Date start = calendar.getFirstDate(c);
        Date end = calendar.getMonthLastDate(c);
        attendance = em.createNamedQuery("getMonthAttendance", Attendance.class).setParameter("employee", login_employee).setParameter("start", start).setParameter("end", end).getResultList();
        List<AttendanceType> at = em.createNamedQuery("getAllType", AttendanceType.class).getResultList();

        // 該当月の総計データを取得
        regular = AttendanceAnalysis.getTotalRegularTime(login_employee, start, end);
        over    = AttendanceAnalysis.getTotalOvertime(login_employee, start, end);
        absence = AttendanceAnalysis.getTotalAbsenceTime(login_employee, start, end);
        paid    = AttendanceAnalysis.getTotalPaid(attendance);
        Employee employee = em.find(Employee.class, login_employee.getId());
        rema_paid = employee.getPaid();

        // 承認ステータス情報をすべて取得する
        List<Approve> approve = em.createNamedQuery("getAllApprove", Approve.class).getResultList();

        // お昼休みのデータを取得
        RegularTime rt = em.find(RegularTime.class, login_employee.getRegular_type());
        Time break_start = rt.getBreak_start();
        Time break_finish = rt.getBreak_finish();

        em.close();

        // カレンダーデータを作成
        calendar.createCalenderData(c, attendance);

        request.setAttribute("calendar", calendar);
        request.setAttribute("type", at);
        request.setAttribute("approve", approve);
        request.setAttribute("regular", regular);
        request.setAttribute("over", over);
        request.setAttribute("absence", absence);
        request.setAttribute("paid", paid);
        request.setAttribute("rema_paid", rema_paid);
        request.setAttribute("break_start", break_start);
        request.setAttribute("break_finish", break_finish);

        // フラッシュメッセージの取得
        if (request.getSession().getAttribute("flush") != null)
        {
            request.setAttribute("flush",  request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        if (request.getSession().getAttribute("errors") != null)
        {
            request.setAttribute("errors",  request.getSession().getAttribute("errors"));
            request.getSession().removeAttribute("errors");
        }

        request.setAttribute("_token", _token);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/attendance/index.jsp");
        rd.forward(request, response);
    }

}
