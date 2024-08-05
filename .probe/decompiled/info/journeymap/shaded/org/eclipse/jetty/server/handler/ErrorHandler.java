package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpStatus;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.server.Dispatcher;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ErrorHandler extends AbstractHandler {

    private static final Logger LOG = Log.getLogger(ErrorHandler.class);

    public static final String ERROR_PAGE = "info.journeymap.shaded.org.eclipse.jetty.server.error_page";

    boolean _showStacks = true;

    boolean _showMessageInTitle = true;

    String _cacheControl = "must-revalidate,no-cache,no-store";

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doError(target, baseRequest, request, response);
    }

    @Override
    public void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if (!HttpMethod.GET.is(method) && !HttpMethod.POST.is(method) && !HttpMethod.HEAD.is(method)) {
            baseRequest.setHandled(true);
        } else {
            if (this instanceof ErrorHandler.ErrorPageMapper) {
                String error_page = ((ErrorHandler.ErrorPageMapper) this).getErrorPage(request);
                if (error_page != null) {
                    String old_error_page = (String) request.getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.error_page");
                    ServletContext servlet_context = request.getServletContext();
                    if (servlet_context == null) {
                        servlet_context = ContextHandler.getCurrentContext();
                    }
                    if (servlet_context == null) {
                        LOG.warn("No ServletContext for error page {}", error_page);
                    } else if (old_error_page != null && old_error_page.equals(error_page)) {
                        LOG.warn("Error page loop {}", error_page);
                    } else {
                        request.setAttribute("info.journeymap.shaded.org.eclipse.jetty.server.error_page", error_page);
                        Dispatcher dispatcher = (Dispatcher) servlet_context.getRequestDispatcher(error_page);
                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("error page dispatch {}->{}", error_page, dispatcher);
                            }
                            if (dispatcher != null) {
                                dispatcher.error(request, response);
                                return;
                            }
                            LOG.warn("No error page found " + error_page);
                        } catch (ServletException var11) {
                            LOG.warn("EXCEPTION ", var11);
                            return;
                        }
                    }
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("No Error Page mapping for request({} {}) (using default)", request.getMethod(), request.getRequestURI());
                }
            }
            if (this._cacheControl != null) {
                response.setHeader(HttpHeader.CACHE_CONTROL.asString(), this._cacheControl);
            }
            this.generateAcceptableResponse(baseRequest, request, response, response.getStatus(), baseRequest.getResponse().getReason());
        }
    }

    protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message) throws IOException {
        List<String> acceptable = baseRequest.getHttpFields().getQualityCSV(HttpHeader.ACCEPT);
        if (acceptable.isEmpty() && !baseRequest.getHttpFields().contains(HttpHeader.ACCEPT)) {
            this.generateAcceptableResponse(baseRequest, request, response, code, message, MimeTypes.Type.TEXT_HTML.asString());
        } else {
            for (String mimeType : acceptable) {
                this.generateAcceptableResponse(baseRequest, request, response, code, message, mimeType);
                if (baseRequest.isHandled()) {
                    break;
                }
            }
        }
        baseRequest.setHandled(true);
        baseRequest.getResponse().closeOutput();
    }

    protected Writer getAcceptableWriter(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> acceptable = baseRequest.getHttpFields().getQualityCSV(HttpHeader.ACCEPT_CHARSET);
        if (acceptable.isEmpty()) {
            response.setCharacterEncoding(StandardCharsets.ISO_8859_1.name());
            return response.getWriter();
        } else {
            for (String charset : acceptable) {
                try {
                    if ("*".equals(charset)) {
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    } else {
                        response.setCharacterEncoding(Charset.forName(charset).name());
                    }
                    return response.getWriter();
                } catch (Exception var8) {
                    LOG.ignore(var8);
                }
            }
            return null;
        }
    }

    protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message, String mimeType) throws IOException {
        switch(mimeType) {
            case "text/html":
            case "text/*":
            case "*/*":
                baseRequest.setHandled(true);
                Writer writer = this.getAcceptableWriter(baseRequest, request, response);
                if (writer != null) {
                    response.setContentType(MimeTypes.Type.TEXT_HTML.asString());
                    this.handleErrorPage(request, writer, code, message);
                }
        }
    }

    protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
        this.writeErrorPage(request, writer, code, message, this._showStacks);
    }

    protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        if (message == null) {
            message = HttpStatus.getMessage(code);
        }
        writer.write("<html>\n<head>\n");
        this.writeErrorPageHead(request, writer, code, message);
        writer.write("</head>\n<body>");
        this.writeErrorPageBody(request, writer, code, message, showStacks);
        writer.write("\n</body>\n</html>\n");
    }

    protected void writeErrorPageHead(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
        writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"/>\n");
        writer.write("<title>Error ");
        writer.write(Integer.toString(code));
        if (this._showMessageInTitle) {
            writer.write(32);
            this.write(writer, message);
        }
        writer.write("</title>\n");
    }

    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        String uri = request.getRequestURI();
        this.writeErrorPageMessage(request, writer, code, message, uri);
        if (showStacks) {
            this.writeErrorPageStacks(request, writer);
        }
        Request.getBaseRequest(request).getHttpChannel().getHttpConfiguration().writePoweredBy(writer, "<hr>", "<hr/>\n");
    }

    protected void writeErrorPageMessage(HttpServletRequest request, Writer writer, int code, String message, String uri) throws IOException {
        writer.write("<h2>HTTP ERROR ");
        writer.write(Integer.toString(code));
        writer.write("</h2>\n<p>Problem accessing ");
        this.write(writer, uri);
        writer.write(". Reason:\n<pre>    ");
        this.write(writer, message);
        writer.write("</pre></p>");
    }

    protected void writeErrorPageStacks(HttpServletRequest request, Writer writer) throws IOException {
        for (Throwable th = (Throwable) request.getAttribute("info.journeymap.shaded.org.javax.servlet.error.exception"); th != null; th = th.getCause()) {
            writer.write("<h3>Caused by:</h3><pre>");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            pw.flush();
            this.write(writer, sw.getBuffer().toString());
            writer.write("</pre>\n");
        }
    }

    public ByteBuffer badMessageError(int status, String reason, HttpFields fields) {
        if (reason == null) {
            reason = HttpStatus.getMessage(status);
        }
        fields.put(HttpHeader.CONTENT_TYPE, MimeTypes.Type.TEXT_HTML_8859_1.asString());
        return BufferUtil.toBuffer("<h1>Bad Message " + status + "</h1><pre>reason: " + reason + "</pre>");
    }

    public String getCacheControl() {
        return this._cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this._cacheControl = cacheControl;
    }

    public boolean isShowStacks() {
        return this._showStacks;
    }

    public void setShowStacks(boolean showStacks) {
        this._showStacks = showStacks;
    }

    public void setShowMessageInTitle(boolean showMessageInTitle) {
        this._showMessageInTitle = showMessageInTitle;
    }

    public boolean getShowMessageInTitle() {
        return this._showMessageInTitle;
    }

    protected void write(Writer writer, String string) throws IOException {
        if (string != null) {
            writer.write(StringUtil.sanitizeXmlString(string));
        }
    }

    public static ErrorHandler getErrorHandler(Server server, ContextHandler context) {
        ErrorHandler error_handler = null;
        if (context != null) {
            error_handler = context.getErrorHandler();
        }
        if (error_handler == null && server != null) {
            error_handler = server.getBean(ErrorHandler.class);
        }
        return error_handler;
    }

    public interface ErrorPageMapper {

        String getErrorPage(HttpServletRequest var1);
    }
}