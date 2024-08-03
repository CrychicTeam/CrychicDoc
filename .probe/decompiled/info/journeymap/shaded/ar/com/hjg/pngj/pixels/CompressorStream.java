package info.journeymap.shaded.ar.com.hjg.pngj.pixels;

import info.journeymap.shaded.ar.com.hjg.pngj.PngjOutputException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class CompressorStream extends FilterOutputStream {

    protected OutputStream os;

    public final int blockLen;

    public final long totalbytes;

    boolean closed = false;

    protected boolean done = false;

    protected long bytesIn = 0L;

    protected long bytesOut = 0L;

    protected int block = -1;

    private byte[] firstBytes;

    protected boolean storeFirstByte = false;

    public CompressorStream(OutputStream os, int blockLen, long totalbytes) {
        super(os);
        if (blockLen < 0) {
            blockLen = 4096;
        }
        if (totalbytes < 0L) {
            totalbytes = Long.MAX_VALUE;
        }
        if (blockLen >= 1 && totalbytes >= 1L) {
            this.os = os;
            this.blockLen = blockLen;
            this.totalbytes = totalbytes;
        } else {
            throw new RuntimeException(" maxBlockLen or totalLen invalid");
        }
    }

    public void close() {
        this.done();
        this.closed = true;
    }

    public abstract void done();

    public final void write(byte[] b, int off, int len) {
        this.block++;
        if (len <= this.blockLen) {
            this.mywrite(b, off, len);
            if (this.storeFirstByte && this.block < this.firstBytes.length) {
                this.firstBytes[this.block] = b[off];
            }
        } else {
            while (len > 0) {
                this.mywrite(b, off, this.blockLen);
                off += this.blockLen;
                len -= this.blockLen;
            }
        }
        if (this.bytesIn >= this.totalbytes) {
            this.done();
        }
    }

    protected abstract void mywrite(byte[] var1, int var2, int var3);

    public final void write(byte[] b) {
        this.write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        throw new PngjOutputException("should not be used");
    }

    public void reset() {
        this.reset(this.os);
    }

    public void reset(OutputStream os) {
        if (this.closed) {
            throw new PngjOutputException("cannot reset, discarded object");
        } else {
            this.done();
            this.bytesIn = 0L;
            this.bytesOut = 0L;
            this.block = -1;
            this.done = false;
            this.os = os;
        }
    }

    public final double getCompressionRatio() {
        return this.bytesOut == 0L ? 1.0 : (double) this.bytesOut / (double) this.bytesIn;
    }

    public final long getBytesRaw() {
        return this.bytesIn;
    }

    public final long getBytesCompressed() {
        return this.bytesOut;
    }

    public OutputStream getOs() {
        return this.os;
    }

    public void flush() {
        if (this.os != null) {
            try {
                this.os.flush();
            } catch (IOException var2) {
                throw new PngjOutputException(var2);
            }
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isDone() {
        return this.done;
    }

    public byte[] getFirstBytes() {
        return this.firstBytes;
    }

    public void setStoreFirstByte(boolean storeFirstByte, int nblocks) {
        this.storeFirstByte = storeFirstByte;
        if (this.storeFirstByte) {
            if (this.firstBytes == null || this.firstBytes.length < nblocks) {
                this.firstBytes = new byte[nblocks];
            }
        } else {
            this.firstBytes = null;
        }
    }
}