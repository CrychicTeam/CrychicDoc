package info.journeymap.shaded.org.eclipse.jetty.server;

import java.io.IOException;

public class HttpInputOverHTTP extends HttpInput {

    public HttpInputOverHTTP(HttpChannelState state) {
        super(state);
    }

    @Override
    protected void produceContent() throws IOException {
        ((HttpConnection) this.getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).fillAndParseForContent();
    }

    @Override
    protected void blockForContent() throws IOException {
        ((HttpConnection) this.getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).blockingReadFillInterested();
        try {
            super.blockForContent();
        } catch (Throwable var2) {
            ((HttpConnection) this.getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).blockingReadException(var2);
        }
    }
}