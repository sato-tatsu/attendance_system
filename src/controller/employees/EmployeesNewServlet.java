package controller.employees;

import java.io.IOException;
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
 * Servlet implementation class EmployeesNewServlet
 */
@WebServlet("/employees/new")
public class EmployeesNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        request.setAttribute("_token", request.getSession().getId());
        Employee e = new Employee();
        request.setAttribute("employee",  e);


        List<Admin> admin = em.createNamedQuery("getAllAdmin", Admin.class).getResultList();
        long admin_count = em.createNamedQuery("getAdminCount", Long.class).getSingleResult();
        request.setAttribute("admin", admin);
        request.setAttribute("admin_count", admin_count);
        em.close();

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employee/new.jsp");
        rd.forward(request, response);
    }

}
