package info.journeymap.shaded.ar.com.hjg.pngj.pixels;

import info.journeymap.shaded.ar.com.hjg.pngj.PngjOutputException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

public class CompressorStreamDeflater extends CompressorStream {

    protected Deflater deflater;

    protected byte[] buf = new byte[4092];

    protected boolean deflaterIsOwn = true;

    public CompressorStreamDeflater(OutputStream os, int maxBlockLen, long totalLen) {
        this(os, maxBlockLen, totalLen, null);
    }

    public CompressorStreamDeflater(OutputStream os, int maxBlockLen, long totalLen, Deflater def) {
        super(os, maxBlockLen, totalLen);
        this.deflater = def == null ? new Deflater() : def;
        this.deflaterIsOwn = def == null;
    }

    public CompressorStreamDeflater(OutputStream os, int maxBlockLen, long totalLen, int deflaterCompLevel, int deflaterStrategy) {
        this(os, maxBlockLen, totalLen, new Deflater(deflaterCompLevel));
        this.deflaterIsOwn = true;
        this.deflater.setStrategy(deflaterStrategy);
    }

    @Override
    public void mywrite(byte[] b, int off, int len) {
        if (!this.deflater.finished() && !this.done && !this.closed) {
            this.deflater.setInput(b, off, len);
            this.bytesIn += (long) len;
            while (!this.deflater.needsInput()) {
                this.deflate();
            }
        } else {
            throw new PngjOutputException("write beyond end of stream");
        }
    }

    protected void deflate() {
        int len = this.deflater.deflate(this.buf, 0, this.buf.length);
        if (len > 0) {
            this.bytesOut += (long) len;
            try {
                if (this.os != null) {
                    this.os.write(this.buf, 0, len);
                }
            } catch (IOException var3) {
                throw new PngjOutputException(var3);
            }
        }
    }

    @Override
    public void done() {
        if (!this.done) {
            if (!this.deflater.finished()) {
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    this.deflate();
                }
            }
            this.done = true;
            this.flush();
        }
    }

    @Override
    public void close() {
        this.done();
        try {
            if (this.deflaterIsOwn) {
                this.deflater.end();
            }
        } catch (Exception var2) {
        }
        super.close();
    }

    @Override
    public void reset() {
        super.reset();
        this.deflater.reset();
    }
}