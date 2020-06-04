package controller.toppage;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import util.DBUtil;

/**
 * Servlet implementation class TopPageServlet
 */
@WebServlet("/index.html")
public class TopPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        List<String> news = new ArrayList<String>();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Long attendance_count = 0L;
        Date date = new Date(System.currentTimeMillis());

        // 本日の勤怠入力の催促
        attendance_count = em.createNamedQuery("getAttendanceCountSelectEmployeeAndDate", Long.class).setParameter("employee", login_employee).setParameter("date", date).getSingleResult();
        if(attendance_count == 0)
        {
            news.add("本日の勤怠が入力されていません。");
        }

        if (login_employee.getAdmin_flag() > 0)
        {
            // 承認が必要な案件の表示
            attendance_count = em.createNamedQuery("getAttendanceCountSelectApprove", Long.class).setParameter("approve", 2).getSingleResult();
            if(attendance_count != 0)
            {
                news.add("承認が必要な勤怠が " + attendance_count + "件 あります。");
            }

            // 引戻が必要な案件の表示
            attendance_count = em.createNamedQuery("getAttendanceCountSelectApprove", Long.class).setParameter("approve", 4).getSingleResult();
            if(attendance_count != 0)
            {
                news.add("引戻が必要な勤怠が " + attendance_count + "件 あります。");
            }
        }

        // 引戻済みの案件の表示
        attendance_count = em.createNamedQuery("getAttendanceCountSelectEmployeeAndApprove", Long.class).setParameter("employee", login_employee).setParameter("approve", 5).getSingleResult();
        if(attendance_count != 0)
        {
            news.add("勤怠が " + attendance_count + "件 引戻されています。");
        }

        em.close();

        request.setAttribute("news", news);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
