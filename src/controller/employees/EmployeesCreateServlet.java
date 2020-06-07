package controller.employees;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Admin;
import model.Employee;
import model.validators.EmployeeValidator;
import util.DBUtil;
import util.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
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

            Employee e = new Employee();                // 従業員のインスタンス生成

            e.setCode(request.getParameter("code"));
            e.setName(request.getParameter("name"));
            e.setPassword(EncryptUtil.getPasswordEncrypt(request.getParameter("password"), (String)this.getServletContext().getAttribute("salt")));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            e.setPaid(Double.parseDouble(request.getParameter("paid")));
            // 登録時の定時はデフォルトで定時1(8時開始)を設定する
            String start_time = "8:00:00";
            String finish_time = "17:00:00";
            e.setRegular_start(Time.valueOf(start_time));
            e.setRegular_finish(Time.valueOf(finish_time));
            e.setRegular_type(1);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            e.setDelete_flag(0);

            List<String> errors = EmployeeValidator.validate(e, true, true);
            if (errors.size() > 0)
            {
                List<Admin> admin = em.createNamedQuery("getAllAdmin", Admin.class).getResultList();
                long admin_count = em.createNamedQuery("getAdminCount", Long.class).getSingleResult();
                em.close();

                request.setAttribute("_token",  request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);
                request.setAttribute("admin", admin);
                request.setAttribute("admin_count", admin_count);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employee/new.jsp");
                rd.forward(request, response);
            }
            else
            {
                em.getTransaction().begin();
                em.persist(e);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
