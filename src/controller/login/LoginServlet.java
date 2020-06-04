package controller.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import util.DBUtil;
import util.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getSession().getId();
        request.setAttribute("_token", _token);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");

        if ( (_token != null) && (_token.equals(request.getSession().getId())) )
        {
            Boolean check_result = false;

            String employee_id = request.getParameter("id");
            String employee_pw = request.getParameter("password");

            Employee employee = null;

            // idとpwの入力チェック
            if ( (employee_id != null) && (!employee_id.equals("")) && (employee_pw != null) && (!employee_pw.equals("")) )
            {
                EntityManager em = DBUtil.createEntityManager();
                String password;
                // 回避アカウント
                if (employee_id.equals("suser") && employee_pw.equals("suser"))
                {
                    password = "suser";
                }
                else
                {
                    // アプリケーションスコープからソルト文字列を取得
                    String salt = (String)this.getServletContext().getAttribute("salt");
                    password = EncryptUtil.getPasswordEncrypt(employee_pw, salt);
                }

                // 社員番号とパスワードが一致しているかを確認する。
                try
                {
                    employee = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class).setParameter("code", employee_id).setParameter("pass", password).getSingleResult();
                }
                catch(NoResultException e)
                {
                }

                if (employee != null)
                {
                    check_result = true;
                }

                em.close();

            }

            if (check_result == false)
            {// id,pwの入力不正もしくは不一致
                _token = request.getSession().getId();
                request.setAttribute("_token", _token);
                request.setAttribute("hasError", true);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
                rd.forward(request, response);
            }
            else
            {
                // 従業員をログインスコープに格納
                request.getSession().setAttribute("login_employee", employee);

                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }

}
