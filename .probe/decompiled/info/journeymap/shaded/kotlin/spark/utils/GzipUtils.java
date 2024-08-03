package info.journeymap.shaded.kotlin.spark.utils;

import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {

    private static final String ACCEPT_ENCODING = "Accept-Encoding";

    private static final String CONTENT_ENCODING = "Content-Encoding";

    private static final String GZIP = "gzip";

    private static final GzipUtils.StringMatch STRING_MATCH = new GzipUtils.StringMatch();

    private GzipUtils() {
    }

    public static OutputStream checkAndWrap(HttpServletRequest httpRequest, HttpServletResponse httpResponse, boolean requireWantsHeader) throws IOException {
        OutputStream responseStream = httpResponse.getOutputStream();
        boolean acceptsGzip = Collections.list(httpRequest.getHeaders("Accept-Encoding")).stream().anyMatch(STRING_MATCH);
        boolean wantGzip = httpResponse.getHeaders("Content-Encoding").contains("gzip");
        if (acceptsGzip && (!requireWantsHeader || wantGzip)) {
            responseStream = new GZIPOutputStream(responseStream, true);
            addContentEncodingHeaderIfMissing(httpResponse, wantGzip);
        }
        return responseStream;
    }

    private static void addContentEncodingHeaderIfMissing(HttpServletResponse response, boolean wantsGzip) {
        if (!wantsGzip) {
            response.setHeader("Content-Encoding", "gzip");
        }
    }

    private static class StringMatch implements Predicate<String> {

        private StringMatch() {
        }

        public boolean test(String s) {
            return s == null ? false : s.contains("gzip");
        }
    }
}