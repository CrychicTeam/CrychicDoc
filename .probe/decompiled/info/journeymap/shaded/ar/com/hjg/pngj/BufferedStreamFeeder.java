package info.journeymap.shaded.ar.com.hjg.pngj;

import java.io.IOException;
import java.io.InputStream;

public class BufferedStreamFeeder {

    private InputStream stream;

    private byte[] buf;

    private int pendinglen;

    private int offset;

    private boolean eof = false;

    private boolean closeStream = true;

    private boolean failIfNoFeed = false;

    private static final int DEFAULTSIZE = 8192;

    public BufferedStreamFeeder(InputStream is) {
        this(is, 8192);
    }

    public BufferedStreamFeeder(InputStream is, int bufsize) {
        this.stream = is;
        this.buf = new byte[bufsize < 1 ? 8192 : bufsize];
    }

    public InputStream getStream() {
        return this.stream;
    }

    public int feed(IBytesConsumer consumer) {
        return this.feed(consumer, -1);
    }

    public int feed(IBytesConsumer consumer, int maxbytes) {
        int n = 0;
        if (this.pendinglen == 0) {
            this.refillBuffer();
        }
        int tofeed = maxbytes > 0 && maxbytes < this.pendinglen ? maxbytes : this.pendinglen;
        if (tofeed > 0) {
            n = consumer.consume(this.buf, this.offset, tofeed);
            if (n > 0) {
                this.offset += n;
                this.pendinglen -= n;
            }
        }
        if (n < 1 && this.failIfNoFeed) {
            throw new PngjInputException("failed feed bytes");
        } else {
            return n;
        }
    }

    public boolean feedFixed(IBytesConsumer consumer, int nbytes) {
        int remain = nbytes;
        while (remain > 0) {
            int n = this.feed(consumer, remain);
            if (n < 1) {
                return false;
            }
            remain -= n;
        }
        return true;
    }

    protected void refillBuffer() {
        if (this.pendinglen <= 0 && !this.eof) {
            try {
                this.offset = 0;
                this.pendinglen = this.stream.read(this.buf);
                if (this.pendinglen < 0) {
                    this.close();
                }
            } catch (IOException var2) {
                throw new PngjInputException(var2);
            }
        }
    }

    public boolean hasMoreToFeed() {
        if (this.eof) {
            return this.pendinglen > 0;
        } else {
            this.refillBuffer();
            return this.pendinglen > 0;
        }
    }

    public void setCloseStream(boolean closeStream) {
        this.closeStream = closeStream;
    }

    public void close() {
        this.eof = true;
        this.buf = null;
        this.pendinglen = 0;
        this.offset = 0;
        if (this.stream != null && this.closeStream) {
            try {
                this.stream.close();
            } catch (Exception var2) {
            }
        }
        this.stream = null;
    }

    public void setInputStream(InputStream is) {
        this.stream = is;
        this.eof = false;
    }

    public boolean isEof() {
        return this.eof;
    }

    public void setFailIfNoFeed(boolean failIfNoFeed) {
        this.failIfNoFeed = failIfNoFeed;
    }
}