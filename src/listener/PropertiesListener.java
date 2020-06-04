package listener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
         // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
        ServletContext context = arg0.getServletContext();

        String prop_path = context.getRealPath("/WEB-INF/application.properties");
        try
        {
            InputStream is = new FileInputStream(prop_path);
            Properties prop = new Properties();
            // プロパティファイルの読み込み
            prop.load(is);
            is.close();

            // 取得したプロパティ情報のキー名を格納
            Iterator<String> p_names = prop.stringPropertyNames().iterator();
            while(p_names.hasNext())
            {
                String p_name = p_names.next();
                String p_value = prop.getProperty(p_name);
                // アプリケーションスコープにプロパティを格納
                context.setAttribute(p_name, p_value);
            }
        }
        catch(FileNotFoundException e)
        {
        }
        catch(IOException e)
        {

        }

    }

}
