package controller.attendance;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
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
import util.DBUtil;

/**
 * Servlet implementation class ConferenceIndexServlet
 */
@WebServlet("/attendance/approve/index")
public class AttendanceApproveIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceApproveIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = request.getSession().getId();

        EntityManager em = DBUtil.createEntityManager();

        Employee e = (Employee)request.getSession().getAttribute("login_employee");

        // 未承認の勤怠情報を取得(日付順)
        List<Attendance> attendance = em.createNamedQuery("getAllAttendanceByAdminApprove", Attendance.class).setParameter("approved", 2).setParameter("pull_wait", 4).setParameter("admin", e.getAdmin_flag()).getResultList();
        // 承認フローマスターデータを取得する
        List<Approve> approve = em.createNamedQuery("getAllApprove", Approve.class).getResultList();
        // 勤怠種別マスターデータを取得する
        List<AttendanceType> type = em.createNamedQuery("getAllType", AttendanceType.class).getResultList();
        em.close();

        request.setAttribute("_token", _token);
        request.setAttribute("attendance", attendance);
        request.setAttribute("approve", approve);
        request.setAttribute("type", type);

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

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/attendance/approve.jsp");
        rd.forward(request, response);
    }

}
