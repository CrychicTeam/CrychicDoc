package info.journeymap.shaded.org.javax.servlet;

public interface SessionCookieConfig {

    void setName(String var1);

    String getName();

    void setDomain(String var1);

    String getDomain();

    void setPath(String var1);

    String getPath();

    void setComment(String var1);

    String getComment();

    void setHttpOnly(boolean var1);

    boolean isHttpOnly();

    void setSecure(boolean var1);

    boolean isSecure();

    void setMaxAge(int var1);

    int getMaxAge();
}