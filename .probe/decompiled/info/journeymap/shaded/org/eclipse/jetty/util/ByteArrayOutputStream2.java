package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class ByteArrayOutputStream2 extends ByteArrayOutputStream {

    public ByteArrayOutputStream2() {
    }

    public ByteArrayOutputStream2(int size) {
        super(size);
    }

    public byte[] getBuf() {
        return this.buf;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void reset(int minSize) {
        this.reset();
        if (this.buf.length < minSize) {
            this.buf = new byte[minSize];
        }
    }

    public void writeUnchecked(int b) {
        this.buf[this.count++] = (byte) b;
    }

    public String toString(Charset charset) {
        return new String(this.buf, 0, this.count, charset);
    }
}