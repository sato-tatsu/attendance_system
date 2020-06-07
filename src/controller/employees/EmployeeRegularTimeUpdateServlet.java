package controller.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import model.RegularTime;
import util.DBUtil;

/**
 * Servlet implementation class EmployeeRegularTimeUpdate
 */
@WebServlet("/employees/regulartime/update")
public class EmployeeRegularTimeUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeRegularTimeUpdateServlet() {
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

            String employee_id[] = request.getParameterValues("id[]");
            String type[] = request.getParameterValues("type[]");
            Employee employee;
            RegularTime r_time;
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());


            for (int i = 0; i < employee_id.length; i++)
            {
                // idに対応する従業員を取得
                employee = em.find(Employee.class, Integer.parseInt(employee_id[i]));
                // 定時マスターから定時タイプ情報を照会
                r_time = em.find(RegularTime.class, Integer.parseInt(type[i]));

                // 定時タイプの更新
                employee.setRegular_type(Integer.parseInt(type[i]));
                // 出勤時間の更新
                employee.setRegular_start(r_time.getRegular_start());
                // 退勤時間の更新
                employee.setRegular_finish(r_time.getRegular_finish());
                // 更新日時の更新
                employee.setUpdated_at(currentTime);
            }

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "定時登録が完了しました。");

            response.sendRedirect(request.getContextPath() + "/employees/index");
        }
    }

}
