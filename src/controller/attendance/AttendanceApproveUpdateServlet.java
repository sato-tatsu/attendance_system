package controller.attendance;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Attendance;
import model.analysis.ApproveAnalysis;
import util.DBUtil;

/**
 * Servlet implementation class AttendanceApproveUpdateServlet
 */
@WebServlet("/attendance/approve/update")
public class AttendanceApproveUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceApproveUpdateServlet() {
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
            String attendance_id[] = request.getParameterValues("attendance_id[]");
            Boolean admin_flag = Boolean.parseBoolean(request.getParameter("admin_flag"));
            Attendance attendance;
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());


            for (String id : attendance_id)
            {
                // idに対応する勤怠情報を取得
                attendance = em.find(Attendance.class, Integer.parseInt(id));
                // 承認フローマスターから遷移先を照会
                Integer next_state = ApproveAnalysis.getNextApproveState(attendance.getApprove(), admin_flag);

                if (next_state == null)
                {
                    request.getSession().setAttribute("errors", "すでに承認済みの内容がありました。");
                }
                else if (next_state != 0)
                {
                    // 取得した勤怠情報の承認情報を更新
                    attendance.setApprove(next_state);
                    attendance.setUpdated_at(currentTime);
                }
            }

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "承認処理が完了しました。");

            response.sendRedirect(request.getContextPath() + "/attendance/approve/index");
        }

    }

}
