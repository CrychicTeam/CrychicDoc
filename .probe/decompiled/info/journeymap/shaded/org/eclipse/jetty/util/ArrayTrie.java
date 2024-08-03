package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArrayTrie<V> extends AbstractTrie<V> {

    private static final int ROW_SIZE = 32;

    private static final int[] __lookup = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, -1, 27, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1 };

    private final char[] _rowIndex;

    private final String[] _key;

    private final V[] _value;

    private char[][] _bigIndex;

    private char _rows;

    public ArrayTrie() {
        this(128);
    }

    public ArrayTrie(int capacity) {
        super(true);
        this._value = (V[]) (new Object[capacity]);
        this._rowIndex = new char[capacity * 32];
        this._key = new String[capacity];
    }

    @Override
    public void clear() {
        this._rows = 0;
        Arrays.fill(this._value, null);
        Arrays.fill(this._rowIndex, '\u0000');
        Arrays.fill(this._key, null);
    }

    @Override
    public boolean put(String s, V v) {
        int t = 0;
        int limit = s.length();
        for (int k = 0; k < limit; k++) {
            char c = s.charAt(k);
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                t = this._rowIndex[idx];
                if (t == 0) {
                    if (++this._rows >= this._value.length) {
                        return false;
                    }
                    t = this._rowIndex[idx] = this._rows;
                }
            } else {
                if (c > 127) {
                    throw new IllegalArgumentException("non ascii character");
                }
                if (this._bigIndex == null) {
                    this._bigIndex = new char[this._value.length][];
                }
                if (t >= this._bigIndex.length) {
                    return false;
                }
                char[] big = this._bigIndex[t];
                if (big == null) {
                    big = this._bigIndex[t] = new char[128];
                }
                t = big[c];
                if (t == 0) {
                    if (this._rows == this._value.length) {
                        return false;
                    }
                    t = big[c] = ++this._rows;
                }
            }
        }
        if (t >= this._key.length) {
            this._rows = (char) this._key.length;
            return false;
        } else {
            this._key[t] = v == null ? null : s;
            this._value[t] = v;
            return true;
        }
    }

    @Override
    public V get(String s, int offset, int len) {
        int t = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(offset + i);
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                t = this._rowIndex[idx];
                if (t == 0) {
                    return null;
                }
            } else {
                char[] big = this._bigIndex == null ? null : this._bigIndex[t];
                if (big == null) {
                    return null;
                }
                t = big[c];
                if (t == 0) {
                    return null;
                }
            }
        }
        return this._value[t];
    }

    @Override
    public V get(ByteBuffer b, int offset, int len) {
        int t = 0;
        for (int i = 0; i < len; i++) {
            byte c = b.get(offset + i);
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                t = this._rowIndex[idx];
                if (t == 0) {
                    return null;
                }
            } else {
                char[] big = this._bigIndex == null ? null : this._bigIndex[t];
                if (big == null) {
                    return null;
                }
                t = big[c];
                if (t == 0) {
                    return null;
                }
            }
        }
        return this._value[t];
    }

    @Override
    public V getBest(byte[] b, int offset, int len) {
        return this.getBest(0, b, offset, len);
    }

    @Override
    public V getBest(ByteBuffer b, int offset, int len) {
        return b.hasArray() ? this.getBest(0, b.array(), b.arrayOffset() + b.position() + offset, len) : this.getBest(0, b, offset, len);
    }

    @Override
    public V getBest(String s, int offset, int len) {
        return this.getBest(0, s, offset, len);
    }

    private V getBest(int t, String s, int offset, int len) {
        int pos = offset;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(pos++);
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                int nt = this._rowIndex[idx];
                if (nt == 0) {
                    break;
                }
                t = nt;
            } else {
                char[] big = this._bigIndex == null ? null : this._bigIndex[t];
                if (big == null) {
                    return null;
                }
                int nt = big[c];
                if (nt == 0) {
                    break;
                }
                t = nt;
            }
            if (this._key[t] != null) {
                V best = this.getBest(t, s, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                return this._value[t];
            }
        }
        return this._value[t];
    }

    private V getBest(int t, byte[] b, int offset, int len) {
        for (int i = 0; i < len; i++) {
            byte c = b[offset + i];
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                int nt = this._rowIndex[idx];
                if (nt == 0) {
                    break;
                }
                t = nt;
            } else {
                char[] big = this._bigIndex == null ? null : this._bigIndex[t];
                if (big == null) {
                    return null;
                }
                int nt = big[c];
                if (nt == 0) {
                    break;
                }
                t = nt;
            }
            if (this._key[t] != null) {
                V best = this.getBest(t, b, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                break;
            }
        }
        return this._value[t];
    }

    private V getBest(int t, ByteBuffer b, int offset, int len) {
        int pos = b.position() + offset;
        for (int i = 0; i < len; i++) {
            byte c = b.get(pos++);
            int index = __lookup[c & 127];
            if (index >= 0) {
                int idx = t * 32 + index;
                int nt = this._rowIndex[idx];
                if (nt == 0) {
                    break;
                }
                t = nt;
            } else {
                char[] big = this._bigIndex == null ? null : this._bigIndex[t];
                if (big == null) {
                    return null;
                }
                int nt = big[c];
                if (nt == 0) {
                    break;
                }
                t = nt;
            }
            if (this._key[t] != null) {
                V best = this.getBest(t, b, offset + i + 1, len - i - 1);
                if (best != null) {
                    return best;
                }
                break;
            }
        }
        return this._value[t];
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        this.toString(buf, 0);
        if (buf.length() == 0) {
            return "{}";
        } else {
            buf.setCharAt(0, '{');
            buf.append('}');
            return buf.toString();
        }
    }

    private void toString(Appendable out, int t) {
        if (this._value[t] != null) {
            try {
                out.append(',');
                out.append(this._key[t]);
                out.append('=');
                out.append(this._value[t].toString());
            } catch (IOException var8) {
                throw new RuntimeException(var8);
            }
        }
        for (int i = 0; i < 32; i++) {
            int idx = t * 32 + i;
            if (this._rowIndex[idx] != 0) {
                this.toString(out, this._rowIndex[idx]);
            }
        }
        char[] big = this._bigIndex == null ? null : this._bigIndex[t];
        if (big != null) {
            for (int ix : big) {
                if (ix != 0) {
                    this.toString(out, ix);
                }
            }
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet();
        this.keySet(keys, 0);
        return keys;
    }

    private void keySet(Set<String> set, int t) {
        if (t < this._value.length && this._value[t] != null) {
            set.add(this._key[t]);
        }
        for (int i = 0; i < 32; i++) {
            int idx = t * 32 + i;
            if (idx < this._rowIndex.length && this._rowIndex[idx] != 0) {
                this.keySet(set, this._rowIndex[idx]);
            }
        }
        char[] big = this._bigIndex != null && t < this._bigIndex.length ? this._bigIndex[t] : null;
        if (big != null) {
            for (int ix : big) {
                if (ix != 0) {
                    this.keySet(set, ix);
                }
            }
        }
    }

    @Override
    public boolean isFull() {
        return this._rows + 1 >= this._key.length;
    }
}