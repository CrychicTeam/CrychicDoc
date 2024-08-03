package info.journeymap.shaded.org.eclipse.jetty.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTrie<V> implements Trie<V> {

    final boolean _caseInsensitive;

    protected AbstractTrie(boolean insensitive) {
        this._caseInsensitive = insensitive;
    }

    @Override
    public boolean put(V v) {
        return this.put(v.toString(), v);
    }

    @Override
    public V remove(String s) {
        V o = this.get(s);
        this.put(s, null);
        return o;
    }

    @Override
    public V get(String s) {
        return this.get(s, 0, s.length());
    }

    @Override
    public V get(ByteBuffer b) {
        return this.get(b, 0, b.remaining());
    }

    @Override
    public V getBest(String s) {
        return this.getBest(s, 0, s.length());
    }

    @Override
    public V getBest(byte[] b, int offset, int len) {
        return this.getBest(new String(b, offset, len, StandardCharsets.ISO_8859_1));
    }

    @Override
    public boolean isCaseInsensitive() {
        return this._caseInsensitive;
    }
}