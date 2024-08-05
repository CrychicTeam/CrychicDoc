package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.kotlin.spark.utils.IOUtils;
import info.journeymap.shaded.org.javax.servlet.ReadListener;
import info.journeymap.shaded.org.javax.servlet.ServletInputStream;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private byte[] cachedBytes;

    private boolean notConsumed = false;

    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public boolean notConsumed() {
        return this.notConsumed;
    }

    public void notConsumed(boolean notConsumed) {
        this.notConsumed = notConsumed;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String transferEncoding = ((HttpServletRequest) super.getRequest()).getHeader("Transfer-Encoding");
        if ("chunked".equals(transferEncoding)) {
            return super.getInputStream();
        } else {
            if (this.cachedBytes == null) {
                this.cacheInputStream();
            }
            return new HttpRequestWrapper.CachedServletInputStream();
        }
    }

    private void cacheInputStream() throws IOException {
        this.cachedBytes = IOUtils.toByteArray(super.getInputStream());
    }

    private class CachedServletInputStream extends ServletInputStream {

        private ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(HttpRequestWrapper.this.cachedBytes);

        public CachedServletInputStream() {
        }

        public int read() {
            return this.byteArrayInputStream.read();
        }

        public int available() {
            return this.byteArrayInputStream.available();
        }

        @Override
        public boolean isFinished() {
            return this.available() <= 0;
        }

        @Override
        public boolean isReady() {
            return this.available() >= 0;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}