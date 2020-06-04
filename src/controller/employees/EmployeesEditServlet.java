package controller.employees;

import java.io.IOException;
import java.util.ArrayList;
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
import util.DBUtil;

/**
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        List<String> errors = new ArrayList<String>();
        List<Admin> admin = em.createNamedQuery("getAllAdmin", Admin.class).getResultList();
        long admin_count = em.createNamedQuery("getAdminCount", Long.class).getSingleResult();

        em.close();

        if (e.getDelete_flag() == 1)
        {
            errors.add("選択した従業員はすでに削除されています。");

            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("employee", e);
            request.getSession().setAttribute("errors", errors);

            request.getSession().removeAttribute("employee_id");

            response.sendRedirect(request.getContextPath() + "/employees/index");
        }
        else
        {

            request.setAttribute("employee", e);
            request.setAttribute("_token", request.getSession().getId());
            request.getSession().setAttribute("employee_id", e.getId());
            request.setAttribute("admin", admin);
            request.setAttribute("admin_count", admin_count);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employee/edit.jsp");
            rd.forward(request, response);
        }
    }

}
