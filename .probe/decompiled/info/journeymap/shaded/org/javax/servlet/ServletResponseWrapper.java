package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class ServletResponseWrapper implements ServletResponse {

    private ServletResponse response;

    public ServletResponseWrapper(ServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        } else {
            this.response = response;
        }
    }

    public ServletResponse getResponse() {
        return this.response;
    }

    public void setResponse(ServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        } else {
            this.response = response;
        }
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.response.setCharacterEncoding(charset);
    }

    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    @Override
    public void setContentLength(int len) {
        this.response.setContentLength(len);
    }

    @Override
    public void setContentLengthLong(long len) {
        this.response.setContentLengthLong(len);
    }

    @Override
    public void setContentType(String type) {
        this.response.setContentType(type);
    }

    @Override
    public String getContentType() {
        return this.response.getContentType();
    }

    @Override
    public void setBufferSize(int size) {
        this.response.setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
        return this.response.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        this.response.flushBuffer();
    }

    @Override
    public boolean isCommitted() {
        return this.response.isCommitted();
    }

    @Override
    public void reset() {
        this.response.reset();
    }

    @Override
    public void resetBuffer() {
        this.response.resetBuffer();
    }

    @Override
    public void setLocale(Locale loc) {
        this.response.setLocale(loc);
    }

    @Override
    public Locale getLocale() {
        return this.response.getLocale();
    }

    public boolean isWrapperFor(ServletResponse wrapped) {
        if (this.response == wrapped) {
            return true;
        } else {
            return this.response instanceof ServletResponseWrapper ? ((ServletResponseWrapper) this.response).isWrapperFor(wrapped) : false;
        }
    }

    public boolean isWrapperFor(Class<?> wrappedType) {
        if (!ServletResponse.class.isAssignableFrom(wrappedType)) {
            throw new IllegalArgumentException("Given class " + wrappedType.getName() + " not a subinterface of " + ServletResponse.class.getName());
        } else if (wrappedType.isAssignableFrom(this.response.getClass())) {
            return true;
        } else {
            return this.response instanceof ServletResponseWrapper ? ((ServletResponseWrapper) this.response).isWrapperFor(wrappedType) : false;
        }
    }
}