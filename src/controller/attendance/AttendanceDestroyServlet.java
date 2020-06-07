package controller.attendance;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Attendance;
import util.DBUtil;

/**
 * Servlet implementation class AttendanceDestroyServlet
 */
@WebServlet("/attendance/destroy")
public class AttendanceDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if ( (_token != null) && (_token.equals(request.getSession().getId())) )
        {
            EntityManager em = DBUtil.createEntityManager();

            Attendance at = em.find(Attendance.class, Integer.parseInt(request.getParameter("attendance_id")));

            em.getTransaction().begin();
            em.remove(at);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "削除が完了しました。");

            response.sendRedirect(request.getContextPath() + "/attendance/index");
        }
    }

}
