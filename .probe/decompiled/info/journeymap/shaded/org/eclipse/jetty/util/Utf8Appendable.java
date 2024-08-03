package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Utf8Appendable {

    protected static final Logger LOG = Log.getLogger(Utf8Appendable.class);

    public static final char REPLACEMENT = '�';

    public static final byte[] REPLACEMENT_UTF8 = new byte[] { -17, -65, -67 };

    private static final int UTF8_ACCEPT = 0;

    private static final int UTF8_REJECT = 12;

    protected final Appendable _appendable;

    protected int _state = 0;

    private static final byte[] BYTE_TABLE = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };

    private static final byte[] TRANS_TABLE = new byte[] { 0, 12, 24, 36, 60, 96, 84, 12, 12, 12, 48, 72, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 0, 12, 12, 12, 12, 12, 0, 12, 0, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };

    private int _codep;

    public Utf8Appendable(Appendable appendable) {
        this._appendable = appendable;
    }

    public abstract int length();

    protected void reset() {
        this._state = 0;
    }

    private void checkCharAppend() throws IOException {
        if (this._state != 0) {
            this._appendable.append('�');
            int state = this._state;
            this._state = 0;
            throw new Utf8Appendable.NotUtf8Exception("char appended in state " + state);
        }
    }

    public void append(char c) {
        try {
            this.checkCharAppend();
            this._appendable.append(c);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void append(String s) {
        try {
            this.checkCharAppend();
            this._appendable.append(s);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void append(String s, int offset, int length) {
        try {
            this.checkCharAppend();
            this._appendable.append(s, offset, offset + length);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public void append(byte b) {
        try {
            this.appendByte(b);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void append(ByteBuffer buf) {
        try {
            while (buf.remaining() > 0) {
                this.appendByte(buf.get());
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void append(byte[] b, int offset, int length) {
        try {
            int end = offset + length;
            for (int i = offset; i < end; i++) {
                this.appendByte(b[i]);
            }
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public boolean append(byte[] b, int offset, int length, int maxChars) {
        try {
            int end = offset + length;
            for (int i = offset; i < end; i++) {
                if (this.length() > maxChars) {
                    return false;
                }
                this.appendByte(b[i]);
            }
            return true;
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    protected void appendByte(byte b) throws IOException {
        if (b > 0 && this._state == 0) {
            this._appendable.append((char) (b & 255));
        } else {
            int i = b & 255;
            int type = BYTE_TABLE[i];
            this._codep = this._state == 0 ? 255 >> type & i : i & 63 | this._codep << 6;
            int next = TRANS_TABLE[this._state + type];
            switch(next) {
                case 0:
                    this._state = next;
                    if (this._codep < 55296) {
                        this._appendable.append((char) this._codep);
                    } else {
                        for (char c : Character.toChars(this._codep)) {
                            this._appendable.append(c);
                        }
                    }
                    break;
                case 12:
                    String reason = "byte " + TypeUtil.toHexString(b) + " in state " + this._state / 12;
                    this._codep = 0;
                    this._state = 0;
                    this._appendable.append('�');
                    throw new Utf8Appendable.NotUtf8Exception(reason);
                default:
                    this._state = next;
            }
        }
    }

    public boolean isUtf8SequenceComplete() {
        return this._state == 0;
    }

    protected void checkState() {
        if (!this.isUtf8SequenceComplete()) {
            this._codep = 0;
            this._state = 0;
            try {
                this._appendable.append('�');
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
            throw new Utf8Appendable.NotUtf8Exception("incomplete UTF8 sequence");
        }
    }

    public String toReplacedString() {
        if (!this.isUtf8SequenceComplete()) {
            this._codep = 0;
            this._state = 0;
            try {
                this._appendable.append('�');
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
            Throwable th = new Utf8Appendable.NotUtf8Exception("incomplete UTF8 sequence");
            LOG.warn(th.toString());
            LOG.debug(th);
        }
        return this._appendable.toString();
    }

    public static class NotUtf8Exception extends IllegalArgumentException {

        public NotUtf8Exception(String reason) {
            super("Not valid UTF8! " + reason);
        }
    }
}