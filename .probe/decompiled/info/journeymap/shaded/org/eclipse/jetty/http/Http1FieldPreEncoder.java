package info.journeymap.shaded.org.eclipse.jetty.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Http1FieldPreEncoder implements HttpFieldPreEncoder {

    @Override
    public HttpVersion getHttpVersion() {
        return HttpVersion.HTTP_1_0;
    }

    @Override
    public byte[] getEncodedField(HttpHeader header, String headerString, String value) {
        if (header != null) {
            int cbl = header.getBytesColonSpace().length;
            byte[] bytes = Arrays.copyOf(header.getBytesColonSpace(), cbl + value.length() + 2);
            System.arraycopy(value.getBytes(StandardCharsets.ISO_8859_1), 0, bytes, cbl, value.length());
            bytes[bytes.length - 2] = 13;
            bytes[bytes.length - 1] = 10;
            return bytes;
        } else {
            byte[] n = headerString.getBytes(StandardCharsets.ISO_8859_1);
            byte[] v = value.getBytes(StandardCharsets.ISO_8859_1);
            byte[] bytes = Arrays.copyOf(n, n.length + 2 + v.length + 2);
            bytes[n.length] = 58;
            bytes[n.length] = 32;
            bytes[bytes.length - 2] = 13;
            bytes[bytes.length - 1] = 10;
            return bytes;
        }
    }
}