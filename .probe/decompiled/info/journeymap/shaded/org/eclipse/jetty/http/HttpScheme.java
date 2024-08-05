package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import java.nio.ByteBuffer;

public enum HttpScheme {

    HTTP("http"), HTTPS("https"), WS("ws"), WSS("wss");

    public static final Trie<HttpScheme> CACHE = new ArrayTrie<>();

    private final String _string;

    private final ByteBuffer _buffer;

    private HttpScheme(String s) {
        this._string = s;
        this._buffer = BufferUtil.toBuffer(s);
    }

    public ByteBuffer asByteBuffer() {
        return this._buffer.asReadOnlyBuffer();
    }

    public boolean is(String s) {
        return s != null && this._string.equalsIgnoreCase(s);
    }

    public String asString() {
        return this._string;
    }

    public String toString() {
        return this._string;
    }

    static {
        for (HttpScheme version : values()) {
            CACHE.put(version.asString(), version);
        }
    }
}