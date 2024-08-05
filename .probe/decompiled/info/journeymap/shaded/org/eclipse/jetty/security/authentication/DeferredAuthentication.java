package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.security.IdentityService;
import info.journeymap.shaded.org.eclipse.jetty.security.LoginService;
import info.journeymap.shaded.org.eclipse.jetty.security.ServerAuthException;
import info.journeymap.shaded.org.eclipse.jetty.security.UserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.IO;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletOutputStream;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.WriteListener;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class DeferredAuthentication implements Authentication.Deferred {

    private static final Logger LOG = Log.getLogger(DeferredAuthentication.class);

    protected final LoginAuthenticator _authenticator;

    private Object _previousAssociation;

    static final HttpServletResponse __deferredResponse = new HttpServletResponse() {

        @Override
        public void addCookie(Cookie cookie) {
        }

        @Override
        public void addDateHeader(String name, long date) {
        }

        @Override
        public void addHeader(String name, String value) {
        }

        @Override
        public void addIntHeader(String name, int value) {
        }

        @Override
        public boolean containsHeader(String name) {
            return false;
        }

        @Override
        public String encodeRedirectURL(String url) {
            return null;
        }

        @Override
        public String encodeRedirectUrl(String url) {
            return null;
        }

        @Override
        public String encodeURL(String url) {
            return null;
        }

        @Override
        public String encodeUrl(String url) {
            return null;
        }

        @Override
        public void sendError(int sc) throws IOException {
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
        }

        @Override
        public void sendRedirect(String location) throws IOException {
        }

        @Override
        public void setDateHeader(String name, long date) {
        }

        @Override
        public void setHeader(String name, String value) {
        }

        @Override
        public void setIntHeader(String name, int value) {
        }

        @Override
        public void setStatus(int sc) {
        }

        @Override
        public void setStatus(int sc, String sm) {
        }

        @Override
        public void flushBuffer() throws IOException {
        }

        @Override
        public int getBufferSize() {
            return 1024;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return DeferredAuthentication.__nullOut;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return IO.getNullPrintWriter();
        }

        @Override
        public boolean isCommitted() {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public void resetBuffer() {
        }

        @Override
        public void setBufferSize(int size) {
        }

        @Override
        public void setCharacterEncoding(String charset) {
        }

        @Override
        public void setContentLength(int len) {
        }

        @Override
        public void setContentLengthLong(long len) {
        }

        @Override
        public void setContentType(String type) {
        }

        @Override
        public void setLocale(Locale loc) {
        }

        @Override
        public Collection<String> getHeaderNames() {
            return Collections.emptyList();
        }

        @Override
        public String getHeader(String arg0) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(String arg0) {
            return Collections.emptyList();
        }

        @Override
        public int getStatus() {
            return 0;
        }
    };

    private static ServletOutputStream __nullOut = new ServletOutputStream() {

        public void write(int b) throws IOException {
        }

        @Override
        public void print(String s) throws IOException {
        }

        @Override
        public void println(String s) throws IOException {
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }

        @Override
        public boolean isReady() {
            return false;
        }
    };

    public DeferredAuthentication(LoginAuthenticator authenticator) {
        if (authenticator == null) {
            throw new NullPointerException("No Authenticator");
        } else {
            this._authenticator = authenticator;
        }
    }

    @Override
    public Authentication authenticate(ServletRequest request) {
        try {
            Authentication authentication = this._authenticator.validateRequest(request, __deferredResponse, true);
            if (authentication != null && authentication instanceof Authentication.User && !(authentication instanceof Authentication.ResponseSent)) {
                LoginService login_service = this._authenticator.getLoginService();
                IdentityService identity_service = login_service.getIdentityService();
                if (identity_service != null) {
                    this._previousAssociation = identity_service.associate(((Authentication.User) authentication).getUserIdentity());
                }
                return authentication;
            }
        } catch (ServerAuthException var5) {
            LOG.debug(var5);
        }
        return this;
    }

    @Override
    public Authentication authenticate(ServletRequest request, ServletResponse response) {
        try {
            LoginService login_service = this._authenticator.getLoginService();
            IdentityService identity_service = login_service.getIdentityService();
            Authentication authentication = this._authenticator.validateRequest(request, response, true);
            if (authentication instanceof Authentication.User && identity_service != null) {
                this._previousAssociation = identity_service.associate(((Authentication.User) authentication).getUserIdentity());
            }
            return authentication;
        } catch (ServerAuthException var6) {
            LOG.debug(var6);
            return this;
        }
    }

    @Override
    public Authentication login(String username, Object password, ServletRequest request) {
        if (username == null) {
            return null;
        } else {
            UserIdentity identity = this._authenticator.login(username, password, request);
            if (identity != null) {
                IdentityService identity_service = this._authenticator.getLoginService().getIdentityService();
                UserAuthentication authentication = new UserAuthentication("API", identity);
                if (identity_service != null) {
                    this._previousAssociation = identity_service.associate(identity);
                }
                return authentication;
            } else {
                return null;
            }
        }
    }

    public Object getPreviousAssociation() {
        return this._previousAssociation;
    }

    public static boolean isDeferred(HttpServletResponse response) {
        return response == __deferredResponse;
    }
}