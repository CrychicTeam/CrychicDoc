package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.util.B64Code;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class AcceptHash {

    private static final byte[] MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11".getBytes(StandardCharsets.ISO_8859_1);

    public static String hashKey(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(key.getBytes(StandardCharsets.UTF_8));
            md.update(MAGIC);
            return new String(B64Code.encode(md.digest()));
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }
}