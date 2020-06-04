package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String context_path = ((HttpServletRequest)request).getContextPath();
        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if (!servlet_path.matches("/css.*"))
        {
            HttpSession session = ((HttpServletRequest)request).getSession();

            // セッションスコープに格納されているログイン情報を取得
            Employee employee = (Employee)session.getAttribute("login_employee");

            if (employee == null)
            {// ログインしていない場合
                if (!servlet_path.equals("/login"))
                {// ログインページ以外への遷移だった場合
                    // ログインページへ強制遷移
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }
            }
            else
            {// ログインしている場合
                if (servlet_path.equals("/login"))
                {// ログインページへの遷移だった場合
                    // ログインしているため、トップページに自動遷移
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
                else if ( (servlet_path.equals("/admin.*")) && (employee.getAdmin_flag() == 0) )
                {// 管理者権限が必要なページへの遷移かつ管理者権限がない場合
                    // アクセスできないため、トップページに自動遷移
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
