package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeTrie<V> extends AbstractTrie<V> {

    private static final int[] __lookup = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, -1, 27, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1 };

    private static final int INDEX = 32;

    private final TreeTrie<V>[] _nextIndex;

    private final List<TreeTrie<V>> _nextOther = new ArrayList();

    private final char _c;

    private String _key;

    private V _value;

    public TreeTrie() {
        super(true);
        this._nextIndex = new TreeTrie[32];
        this._c = 0;
    }

    private TreeTrie(char c) {
        super(true);
        this._nextIndex = new TreeTrie[32];
        this._c = c;
    }

    @Override
    public void clear() {
        Arrays.fill(this._nextIndex, null);
        this._nextOther.clear();
        this._key = null;
        this._value = null;
    }

    @Override
    public boolean put(String s, V v) {
        TreeTrie<V> t = this;
        int limit = s.length();
        for (int k = 0; k < limit; k++) {
            char c = s.charAt(k);
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    t._nextIndex[index] = new TreeTrie<>(c);
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int i = t._nextOther.size(); i-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(i);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    n = new TreeTrie<>(c);
                    t._nextOther.add(n);
                }
                t = n;
            }
        }
        t._key = v == null ? null : s;
        t._value = v;
        return true;
    }

    @Override
    public V get(String s, int offset, int len) {
        TreeTrie<V> t = this;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(offset + i);
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    return null;
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int j = t._nextOther.size(); j-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(j);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    return null;
                }
                t = n;
            }
        }
        return t._value;
    }

    @Override
    public V get(ByteBuffer b, int offset, int len) {
        TreeTrie<V> t = this;
        for (int i = 0; i < len; i++) {
            byte c = b.get(offset + i);
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    return null;
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int j = t._nextOther.size(); j-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(j);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    return null;
                }
                t = n;
            }
        }
        return t._value;
    }

    @Override
    public V getBest(byte[] b, int offset, int len) {
        TreeTrie<V> t = this;
        for (int i = 0; i < len; i++) {
            byte c = b[offset + i];
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    break;
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int j = t._nextOther.size(); j-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(j);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    break;
                }
                t = n;
            }
            if (t._key != null) {
                V best = t.getBest(b, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                break;
            }
        }
        return t._value;
    }

    @Override
    public V getBest(String s, int offset, int len) {
        TreeTrie<V> t = this;
        for (int i = 0; i < len; i++) {
            byte c = (byte) (255 & s.charAt(offset + i));
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    break;
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int j = t._nextOther.size(); j-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(j);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    break;
                }
                t = n;
            }
            if (t._key != null) {
                V best = t.getBest(s, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                break;
            }
        }
        return t._value;
    }

    @Override
    public V getBest(ByteBuffer b, int offset, int len) {
        return b.hasArray() ? this.getBest(b.array(), b.arrayOffset() + b.position() + offset, len) : this.getBestByteBuffer(b, offset, len);
    }

    private V getBestByteBuffer(ByteBuffer b, int offset, int len) {
        TreeTrie<V> t = this;
        int pos = b.position() + offset;
        for (int i = 0; i < len; i++) {
            byte c = b.get(pos++);
            int index = c >= 0 && c < 127 ? __lookup[c] : -1;
            if (index >= 0) {
                if (t._nextIndex[index] == null) {
                    break;
                }
                t = t._nextIndex[index];
            } else {
                TreeTrie<V> n = null;
                for (int j = t._nextOther.size(); j-- > 0; n = null) {
                    n = (TreeTrie<V>) t._nextOther.get(j);
                    if (n._c == c) {
                        break;
                    }
                }
                if (n == null) {
                    break;
                }
                t = n;
            }
            if (t._key != null) {
                V best = t.getBest(b, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                break;
            }
        }
        return t._value;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf, this);
        if (buf.length() == 0) {
            return "{}";
        } else {
            buf.setCharAt(0, '{');
            buf.append('}');
            return buf.toString();
        }
    }

    private static <V> void toString(Appendable out, TreeTrie<V> t) {
        if (t != null) {
            if (t._value != null) {
                try {
                    out.append(',');
                    out.append(t._key);
                    out.append('=');
                    out.append(t._value.toString());
                } catch (IOException var3) {
                    throw new RuntimeException(var3);
                }
            }
            for (int i = 0; i < 32; i++) {
                if (t._nextIndex[i] != null) {
                    toString(out, t._nextIndex[i]);
                }
            }
            int ix = t._nextOther.size();
            while (ix-- > 0) {
                toString(out, (TreeTrie<V>) t._nextOther.get(ix));
            }
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet();
        keySet(keys, this);
        return keys;
    }

    private static <V> void keySet(Set<String> set, TreeTrie<V> t) {
        if (t != null) {
            if (t._key != null) {
                set.add(t._key);
            }
            for (int i = 0; i < 32; i++) {
                if (t._nextIndex[i] != null) {
                    keySet(set, t._nextIndex[i]);
                }
            }
            int ix = t._nextOther.size();
            while (ix-- > 0) {
                keySet(set, (TreeTrie<V>) t._nextOther.get(ix));
            }
        }
    }

    @Override
    public boolean isFull() {
        return false;
    }
}