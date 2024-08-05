package info.journeymap.shaded.org.javax.servlet;

public class UnavailableException extends ServletException {

    private Servlet servlet;

    private boolean permanent;

    private int seconds;

    @Deprecated
    public UnavailableException(Servlet servlet, String msg) {
        super(msg);
        this.servlet = servlet;
        this.permanent = true;
    }

    @Deprecated
    public UnavailableException(int seconds, Servlet servlet, String msg) {
        super(msg);
        this.servlet = servlet;
        if (seconds <= 0) {
            this.seconds = -1;
        } else {
            this.seconds = seconds;
        }
        this.permanent = false;
    }

    public UnavailableException(String msg) {
        super(msg);
        this.permanent = true;
    }

    public UnavailableException(String msg, int seconds) {
        super(msg);
        if (seconds <= 0) {
            this.seconds = -1;
        } else {
            this.seconds = seconds;
        }
        this.permanent = false;
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    @Deprecated
    public Servlet getServlet() {
        return this.servlet;
    }

    public int getUnavailableSeconds() {
        return this.permanent ? -1 : this.seconds;
    }
}