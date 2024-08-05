package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkHelper;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkRaw;
import java.io.OutputStream;

public class PngIDatChunkOutputStream extends ProgressiveOutputStream {

    private static final int SIZE_DEFAULT = 32768;

    private final OutputStream outputStream;

    private byte[] prefix = null;

    public PngIDatChunkOutputStream(OutputStream outputStream) {
        this(outputStream, 0);
    }

    public PngIDatChunkOutputStream(OutputStream outputStream, int size) {
        super(size > 0 ? size : '耀');
        this.outputStream = outputStream;
    }

    @Override
    protected final void flushBuffer(byte[] b, int len) {
        int len2 = this.prefix == null ? len : len + this.prefix.length;
        ChunkRaw c = new ChunkRaw(len2, ChunkHelper.b_IDAT, false);
        if (len == len2) {
            c.data = b;
        }
        c.writeChunk(this.outputStream);
    }

    void setPrefix(byte[] pref) {
        if (pref == null) {
            this.prefix = null;
        } else {
            this.prefix = new byte[pref.length];
            System.arraycopy(pref, 0, this.prefix, 0, pref.length);
        }
    }
}