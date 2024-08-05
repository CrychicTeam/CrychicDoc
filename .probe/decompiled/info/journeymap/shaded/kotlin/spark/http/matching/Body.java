package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.serialization.SerializerChain;
import info.journeymap.shaded.kotlin.spark.utils.GzipUtils;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

final class Body {

    private Object content;

    public static Body create() {
        return new Body();
    }

    private Body() {
    }

    public boolean notSet() {
        return this.content == null;
    }

    public boolean isSet() {
        return this.content != null;
    }

    public Object get() {
        return this.content;
    }

    public void set(Object content) {
        this.content = content;
    }

    public void serializeTo(HttpServletResponse httpResponse, SerializerChain serializerChain, HttpServletRequest httpRequest) throws IOException {
        if (!httpResponse.isCommitted()) {
            if (httpResponse.getContentType() == null) {
                httpResponse.setContentType("text/html; charset=utf-8");
            }
            OutputStream responseStream = GzipUtils.checkAndWrap(httpRequest, httpResponse, true);
            serializerChain.process(responseStream, this.content);
            responseStream.flush();
            responseStream.close();
        }
    }
}