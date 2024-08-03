package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;

public interface Servlet {

    void init(ServletConfig var1) throws ServletException;

    ServletConfig getServletConfig();

    void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException;

    String getServletInfo();

    void destroy();
}