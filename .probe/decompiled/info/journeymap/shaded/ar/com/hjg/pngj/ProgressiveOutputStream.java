package info.journeymap.shaded.ar.com.hjg.pngj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

abstract class ProgressiveOutputStream extends ByteArrayOutputStream {

    private int size;

    private long countFlushed = 0L;

    public ProgressiveOutputStream(int size) {
        this.size = size;
    }

    public final void close() throws IOException {
        try {
            this.flush();
        } catch (Exception var2) {
        }
        super.close();
    }

    public final void flush() throws IOException {
        super.flush();
        this.checkFlushBuffer(true);
    }

    public final void write(byte[] b, int off, int len) {
        super.write(b, off, len);
        this.checkFlushBuffer(false);
    }

    public final void write(byte[] b) throws IOException {
        super.write(b);
        this.checkFlushBuffer(false);
    }

    public final void write(int arg0) {
        super.write(arg0);
        this.checkFlushBuffer(false);
    }

    public final synchronized void reset() {
        super.reset();
    }

    private final void checkFlushBuffer(boolean forced) {
        while (forced || this.count >= this.size) {
            int nb = this.size;
            if (nb > this.count) {
                nb = this.count;
            }
            if (nb == 0) {
                return;
            }
            this.flushBuffer(this.buf, nb);
            this.countFlushed += (long) nb;
            int bytesleft = this.count - nb;
            this.count = bytesleft;
            if (bytesleft > 0) {
                System.arraycopy(this.buf, nb, this.buf, 0, bytesleft);
            }
        }
    }

    protected abstract void flushBuffer(byte[] var1, int var2);

    public void setSize(int size) {
        this.size = size;
        System.out.println("setting size: " + size + " count" + this.count);
        this.checkFlushBuffer(false);
    }

    public long getCountFlushed() {
        return this.countFlushed;
    }
}