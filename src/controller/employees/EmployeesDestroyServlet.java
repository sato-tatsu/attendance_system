package controller.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import util.DBUtil;

/**
 * Servlet implementation class EmployeesDestroyServlet
 */
@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesDestroyServlet() {
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
            List<String> errors = new ArrayList<String>();

            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));

            if (e.getDelete_flag() == 1)
            {
                em.close();
                errors.add("選択した従業員はすでに削除されています。");

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.getSession().setAttribute("errors", errors);

                request.getSession().removeAttribute("employee_id");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
            else
            {
                e.setDelete_flag(1);
                e.setUpdated_at(new Timestamp(System.currentTimeMillis()));

                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "削除が完了しました。");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
