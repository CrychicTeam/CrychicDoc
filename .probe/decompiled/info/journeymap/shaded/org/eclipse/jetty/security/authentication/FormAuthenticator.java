package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeaderValue;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.security.Authenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.ServerAuthException;
import info.journeymap.shaded.org.eclipse.jetty.security.UserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Response;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiMap;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.RequestDispatcher;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequestWrapper;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponseWrapper;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

public class FormAuthenticator extends LoginAuthenticator {

    private static final Logger LOG = Log.getLogger(FormAuthenticator.class);

    public static final String __FORM_LOGIN_PAGE = "info.journeymap.shaded.org.eclipse.jetty.security.form_login_page";

    public static final String __FORM_ERROR_PAGE = "info.journeymap.shaded.org.eclipse.jetty.security.form_error_page";

    public static final String __FORM_DISPATCH = "info.journeymap.shaded.org.eclipse.jetty.security.dispatch";

    public static final String __J_URI = "info.journeymap.shaded.org.eclipse.jetty.security.form_URI";

    public static final String __J_POST = "info.journeymap.shaded.org.eclipse.jetty.security.form_POST";

    public static final String __J_METHOD = "info.journeymap.shaded.org.eclipse.jetty.security.form_METHOD";

    public static final String __J_SECURITY_CHECK = "/j_security_check";

    public static final String __J_USERNAME = "j_username";

    public static final String __J_PASSWORD = "j_password";

    private String _formErrorPage;

    private String _formErrorPath;

    private String _formLoginPage;

    private String _formLoginPath;

    private boolean _dispatch;

    private boolean _alwaysSaveUri;

    public FormAuthenticator() {
    }

    public FormAuthenticator(String login, String error, boolean dispatch) {
        this();
        if (login != null) {
            this.setLoginPage(login);
        }
        if (error != null) {
            this.setErrorPage(error);
        }
        this._dispatch = dispatch;
    }

    public void setAlwaysSaveUri(boolean alwaysSave) {
        this._alwaysSaveUri = alwaysSave;
    }

    public boolean getAlwaysSaveUri() {
        return this._alwaysSaveUri;
    }

    @Override
    public void setConfiguration(Authenticator.AuthConfiguration configuration) {
        super.setConfiguration(configuration);
        String login = configuration.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.security.form_login_page");
        if (login != null) {
            this.setLoginPage(login);
        }
        String error = configuration.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.security.form_error_page");
        if (error != null) {
            this.setErrorPage(error);
        }
        String dispatch = configuration.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.security.dispatch");
        this._dispatch = dispatch == null ? this._dispatch : Boolean.valueOf(dispatch);
    }

    @Override
    public String getAuthMethod() {
        return "FORM";
    }

    private void setLoginPage(String path) {
        if (!path.startsWith("/")) {
            LOG.warn("form-login-page must start with /");
            path = "/" + path;
        }
        this._formLoginPage = path;
        this._formLoginPath = path;
        if (this._formLoginPath.indexOf(63) > 0) {
            this._formLoginPath = this._formLoginPath.substring(0, this._formLoginPath.indexOf(63));
        }
    }

    private void setErrorPage(String path) {
        if (path != null && path.trim().length() != 0) {
            if (!path.startsWith("/")) {
                LOG.warn("form-error-page must start with /");
                path = "/" + path;
            }
            this._formErrorPage = path;
            this._formErrorPath = path;
            if (this._formErrorPath.indexOf(63) > 0) {
                this._formErrorPath = this._formErrorPath.substring(0, this._formErrorPath.indexOf(63));
            }
        } else {
            this._formErrorPath = null;
            this._formErrorPage = null;
        }
    }

    @Override
    public UserIdentity login(String username, Object password, ServletRequest request) {
        UserIdentity user = super.login(username, password, request);
        if (user != null) {
            HttpSession session = ((HttpServletRequest) request).getSession(true);
            Authentication cached = new SessionAuthentication(this.getAuthMethod(), user, password);
            session.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity", cached);
        }
        return user;
    }

    @Override
    public void prepareRequest(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity") != null) {
            String juri = (String) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI");
            if (juri != null && juri.length() != 0) {
                String method = (String) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_METHOD");
                if (method != null && method.length() != 0) {
                    StringBuffer buf = httpRequest.getRequestURL();
                    if (httpRequest.getQueryString() != null) {
                        buf.append("?").append(httpRequest.getQueryString());
                    }
                    if (juri.equals(buf.toString())) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Restoring original method {} for {} with method {}", method, juri, httpRequest.getMethod());
                        }
                        Request base_request = Request.getBaseRequest(request);
                        base_request.setMethod(method);
                    }
                }
            }
        }
    }

    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        Request base_request = Request.getBaseRequest(request);
        Response base_response = base_request.getResponse();
        String uri = request.getRequestURI();
        if (uri == null) {
            uri = "/";
        }
        mandatory |= this.isJSecurityCheck(uri);
        if (!mandatory) {
            return new DeferredAuthentication(this);
        } else if (this.isLoginOrErrorPage(URIUtil.addPaths(request.getServletPath(), request.getPathInfo())) && !DeferredAuthentication.isDeferred(response)) {
            return new DeferredAuthentication(this);
        } else {
            HttpSession session = null;
            try {
                session = request.getSession(true);
            } catch (Exception var23) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(var23);
                }
            }
            if (session == null) {
                return Authentication.UNAUTHENTICATED;
            } else {
                try {
                    if (this.isJSecurityCheck(uri)) {
                        String username = request.getParameter("j_username");
                        String password = request.getParameter("j_password");
                        UserIdentity user = this.login(username, password, request);
                        LOG.debug("jsecuritycheck {} {}", username, user);
                        session = request.getSession(true);
                        if (user != null) {
                            String nuri;
                            FormAuthenticator.FormAuthentication form_auth;
                            synchronized (session) {
                                nuri = (String) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI");
                                if (nuri == null || nuri.length() == 0) {
                                    nuri = request.getContextPath();
                                    if (nuri.length() == 0) {
                                        nuri = "/";
                                    }
                                }
                                form_auth = new FormAuthenticator.FormAuthentication(this.getAuthMethod(), user);
                            }
                            LOG.debug("authenticated {}->{}", form_auth, nuri);
                            response.setContentLength(0);
                            int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
                            base_response.sendRedirect(redirectCode, response.encodeRedirectURL(nuri));
                            return form_auth;
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Form authentication FAILED for " + StringUtil.printable(username));
                            }
                            if (this._formErrorPage == null) {
                                LOG.debug("auth failed {}->403", username);
                                if (response != null) {
                                    response.sendError(403);
                                }
                            } else if (this._dispatch) {
                                LOG.debug("auth failed {}=={}", username, this._formErrorPage);
                                RequestDispatcher dispatcher = request.getRequestDispatcher(this._formErrorPage);
                                response.setHeader(HttpHeader.CACHE_CONTROL.asString(), HttpHeaderValue.NO_CACHE.asString());
                                response.setDateHeader(HttpHeader.EXPIRES.asString(), 1L);
                                dispatcher.forward(new FormAuthenticator.FormRequest(request), new FormAuthenticator.FormResponse(response));
                            } else {
                                LOG.debug("auth failed {}->{}", username, this._formErrorPage);
                                int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
                                base_response.sendRedirect(redirectCode, response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), this._formErrorPage)));
                            }
                            return Authentication.SEND_FAILURE;
                        }
                    } else {
                        Authentication authentication = (Authentication) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity");
                        if (authentication != null) {
                            if (!(authentication instanceof Authentication.User) || this._loginService == null || this._loginService.validate(((Authentication.User) authentication).getUserIdentity())) {
                                synchronized (session) {
                                    String j_uri = (String) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI");
                                    if (j_uri != null) {
                                        LOG.debug("auth retry {}->{}", authentication, j_uri);
                                        StringBuffer buf = request.getRequestURL();
                                        if (request.getQueryString() != null) {
                                            buf.append("?").append(request.getQueryString());
                                        }
                                        if (j_uri.equals(buf.toString())) {
                                            MultiMap<String> j_post = (MultiMap<String>) session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_POST");
                                            if (j_post != null) {
                                                LOG.debug("auth rePOST {}->{}", authentication, j_uri);
                                                base_request.setContentParameters(j_post);
                                            }
                                            session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI");
                                            session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_METHOD");
                                            session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_POST");
                                        }
                                    }
                                }
                                LOG.debug("auth {}", authentication);
                                return authentication;
                            }
                            LOG.debug("auth revoked {}", authentication);
                            session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity");
                        }
                        if (DeferredAuthentication.isDeferred(response)) {
                            LOG.debug("auth deferred {}", session.getId());
                            return Authentication.UNAUTHENTICATED;
                        } else {
                            synchronized (session) {
                                if (session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI") == null || this._alwaysSaveUri) {
                                    StringBuffer bufx = request.getRequestURL();
                                    if (request.getQueryString() != null) {
                                        bufx.append("?").append(request.getQueryString());
                                    }
                                    session.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_URI", bufx.toString());
                                    session.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_METHOD", request.getMethod());
                                    if (MimeTypes.Type.FORM_ENCODED.is(req.getContentType()) && HttpMethod.POST.is(request.getMethod())) {
                                        MultiMap<String> formParameters = new MultiMap<>();
                                        base_request.extractFormParameters(formParameters);
                                        session.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.form_POST", formParameters);
                                    }
                                }
                            }
                            if (this._dispatch) {
                                LOG.debug("challenge {}=={}", session.getId(), this._formLoginPage);
                                RequestDispatcher dispatcher = request.getRequestDispatcher(this._formLoginPage);
                                response.setHeader(HttpHeader.CACHE_CONTROL.asString(), HttpHeaderValue.NO_CACHE.asString());
                                response.setDateHeader(HttpHeader.EXPIRES.asString(), 1L);
                                dispatcher.forward(new FormAuthenticator.FormRequest(request), new FormAuthenticator.FormResponse(response));
                            } else {
                                LOG.debug("challenge {}->{}", session.getId(), this._formLoginPage);
                                int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
                                base_response.sendRedirect(redirectCode, response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), this._formLoginPage)));
                            }
                            return Authentication.SEND_CONTINUE;
                        }
                    }
                } catch (ServletException | IOException var22) {
                    throw new ServerAuthException(var22);
                }
            }
        }
    }

    public boolean isJSecurityCheck(String uri) {
        int jsc = uri.indexOf("/j_security_check");
        if (jsc < 0) {
            return false;
        } else {
            int e = jsc + "/j_security_check".length();
            if (e == uri.length()) {
                return true;
            } else {
                char c = uri.charAt(e);
                return c == ';' || c == '#' || c == '/' || c == '?';
            }
        }
    }

    public boolean isLoginOrErrorPage(String pathInContext) {
        return pathInContext != null && (pathInContext.equals(this._formErrorPath) || pathInContext.equals(this._formLoginPath));
    }

    @Override
    public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
        return true;
    }

    public static class FormAuthentication extends UserAuthentication implements Authentication.ResponseSent {

        public FormAuthentication(String method, UserIdentity userIdentity) {
            super(method, userIdentity);
        }

        @Override
        public String toString() {
            return "Form" + super.toString();
        }
    }

    protected static class FormRequest extends HttpServletRequestWrapper {

        public FormRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public long getDateHeader(String name) {
            return name.toLowerCase(Locale.ENGLISH).startsWith("if-") ? -1L : super.getDateHeader(name);
        }

        @Override
        public String getHeader(String name) {
            return name.toLowerCase(Locale.ENGLISH).startsWith("if-") ? null : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return Collections.enumeration(Collections.list(super.getHeaderNames()));
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            return name.toLowerCase(Locale.ENGLISH).startsWith("if-") ? Collections.enumeration(Collections.emptyList()) : super.getHeaders(name);
        }
    }

    protected static class FormResponse extends HttpServletResponseWrapper {

        public FormResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void addDateHeader(String name, long date) {
            if (this.notIgnored(name)) {
                super.addDateHeader(name, date);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (this.notIgnored(name)) {
                super.addHeader(name, value);
            }
        }

        @Override
        public void setDateHeader(String name, long date) {
            if (this.notIgnored(name)) {
                super.setDateHeader(name, date);
            }
        }

        @Override
        public void setHeader(String name, String value) {
            if (this.notIgnored(name)) {
                super.setHeader(name, value);
            }
        }

        private boolean notIgnored(String name) {
            return !HttpHeader.CACHE_CONTROL.is(name) && !HttpHeader.PRAGMA.is(name) && !HttpHeader.ETAG.is(name) && !HttpHeader.EXPIRES.is(name) && !HttpHeader.LAST_MODIFIED.is(name) && !HttpHeader.AGE.is(name);
        }
    }
}