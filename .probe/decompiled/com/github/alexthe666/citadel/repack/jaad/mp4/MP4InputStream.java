package com.github.alexthe666.citadel.repack.jaad.mp4;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

public class MP4InputStream {

    public static final int MASK8 = 255;

    public static final int MASK16 = 65535;

    public static final String UTF8 = "UTF-8";

    public static final String UTF16 = "UTF-16";

    private static final int BYTE_ORDER_MASK = 65279;

    private final InputStream in;

    private final RandomAccessFile fin;

    private final LinkedList<Byte> peeked = new LinkedList();

    private long offset;

    MP4InputStream(InputStream in) {
        this.in = in;
        this.fin = null;
        this.offset = 0L;
    }

    MP4InputStream(RandomAccessFile fin) {
        this.fin = fin;
        this.in = null;
    }

    public int peek() throws IOException {
        int i = 0;
        if (!this.peeked.isEmpty()) {
            i = (Byte) this.peeked.remove() & 255;
        } else if (this.in != null) {
            i = this.in.read();
        } else if (this.fin != null) {
            i = this.fin.read();
        }
        if (i == -1) {
            throw new EOFException();
        } else {
            this.peeked.addFirst((byte) i);
            return i;
        }
    }

    public int read() throws IOException {
        int i = 0;
        if (!this.peeked.isEmpty()) {
            i = (Byte) this.peeked.remove() & 255;
        } else if (this.in != null) {
            i = this.in.read();
        } else if (this.fin != null) {
            i = this.fin.read();
        }
        if (i == -1) {
            throw new EOFException();
        } else {
            this.offset++;
            return i;
        }
    }

    public void peek(byte[] b, int off, int len) throws IOException {
        int read = 0;
        int i;
        for (i = 0; read < len && read < this.peeked.size(); read++) {
            b[off + read] = (Byte) this.peeked.get(read);
        }
        while (read < len) {
            if (this.in != null) {
                i = this.in.read(b, off + read, len - read);
            } else if (this.fin != null) {
                i = this.fin.read(b, off + read, len - read);
            }
            if (i < 0) {
                throw new EOFException();
            }
            for (int j = 0; j < i; j++) {
                this.peeked.add(b[off + j]);
            }
            read += i;
        }
    }

    public void read(byte[] b, int off, int len) throws IOException {
        int read = 0;
        int i;
        for (i = 0; read < len && !this.peeked.isEmpty(); read++) {
            b[off + read] = (Byte) this.peeked.remove();
        }
        while (read < len) {
            if (this.in != null) {
                i = this.in.read(b, off + read, len - read);
            } else if (this.fin != null) {
                i = this.fin.read(b, off + read, len - read);
            }
            if (i < 0) {
                throw new EOFException();
            }
            read += i;
        }
        this.offset += (long) read;
    }

    public long peekBytes(int n) throws IOException {
        if (n >= 1 && n <= 8) {
            byte[] b = new byte[n];
            this.peek(b, 0, n);
            long result = 0L;
            for (int i = 0; i < n; i++) {
                result = result << 8 | (long) (b[i] & 255);
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("invalid number of bytes to read: " + n);
        }
    }

    public long readBytes(int n) throws IOException {
        if (n >= 1 && n <= 8) {
            byte[] b = new byte[n];
            this.read(b, 0, n);
            long result = 0L;
            for (int i = 0; i < n; i++) {
                result = result << 8 | (long) (b[i] & 255);
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("invalid number of bytes to read: " + n);
        }
    }

    public void peekBytes(byte[] b) throws IOException {
        this.peek(b, 0, b.length);
    }

    public void readBytes(byte[] b) throws IOException {
        this.read(b, 0, b.length);
    }

    public String readString(int n) throws IOException {
        int i = -1;
        int pos = 0;
        char[] c;
        for (c = new char[n]; pos < n; pos++) {
            i = this.read();
            c[pos] = (char) i;
        }
        return new String(c, 0, pos);
    }

    public String readUTFString(int max, String encoding) throws IOException {
        return new String(this.readTerminated(max, 0), Charset.forName(encoding));
    }

    public String readUTFString(int max) throws IOException {
        byte[] bom = new byte[2];
        this.read(bom, 0, 2);
        if (bom[0] != 0 && bom[1] != 0) {
            int i = bom[0] << 8 | bom[1];
            byte[] b = this.readTerminated(max - 2, 0);
            byte[] b2 = new byte[b.length + bom.length];
            System.arraycopy(bom, 0, b2, 0, bom.length);
            System.arraycopy(b, 0, b2, bom.length, b.length);
            return new String(b2, Charset.forName(i == 65279 ? "UTF-16" : "UTF-8"));
        } else {
            return new String();
        }
    }

    public byte[] readTerminated(int max, int terminator) throws IOException {
        byte[] b = new byte[max];
        int pos = 0;
        int i = 0;
        while (pos < max && i != -1) {
            i = this.read();
            if (i != -1) {
                b[pos++] = (byte) i;
            }
        }
        return Arrays.copyOf(b, pos);
    }

    public double readFixedPoint(int m, int n) throws IOException {
        int bits = m + n;
        if (bits % 8 != 0) {
            throw new IllegalArgumentException("number of bits is not a multiple of 8: " + (m + n));
        } else {
            long l = this.readBytes(bits / 8);
            double x = Math.pow(2.0, (double) n);
            return (double) l / x;
        }
    }

    public void skipBytes(long n) throws IOException {
        long l;
        for (l = 0L; l < n && !this.peeked.isEmpty(); l++) {
            this.peeked.remove();
        }
        while (l < n) {
            if (this.in != null) {
                l += this.in.skip(n - l);
            } else if (this.fin != null) {
                l += (long) this.fin.skipBytes((int) (n - l));
            }
        }
        this.offset += l;
    }

    public long getOffset() throws IOException {
        return this.offset;
    }

    public void seek(long pos) throws IOException {
        this.peeked.clear();
        if (this.fin != null) {
            this.fin.seek(pos);
        } else {
            throw new IOException("could not seek: no random access");
        }
    }

    public boolean hasRandomAccess() {
        return this.fin != null;
    }

    public boolean hasLeft() throws IOException {
        boolean b;
        if (!this.peeked.isEmpty()) {
            b = true;
        } else if (this.fin != null) {
            b = this.fin.getFilePointer() < this.fin.length() - 1L;
        } else {
            int i = this.in.read();
            b = i != -1;
            if (b) {
                this.peeked.add((byte) i);
            }
        }
        return b;
    }

    void close() throws IOException {
        this.peeked.clear();
        if (this.in != null) {
            this.in.close();
        } else if (this.fin != null) {
            this.fin.close();
        }
    }
}