package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PngChunkZTXT extends PngChunkTextVar {

    public static final String ID = "zTXt";

    public PngChunkZTXT(ImageInfo info) {
        super("zTXt", info);
    }

    @Override
    public ChunkRaw createRawChunk() {
        if (this.key != null && this.key.trim().length() != 0) {
            try {
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
                ba.write(ChunkHelper.toBytes(this.key));
                ba.write(0);
                ba.write(0);
                byte[] textbytes = ChunkHelper.compressBytes(ChunkHelper.toBytes(this.val), true);
                ba.write(textbytes);
                byte[] b = ba.toByteArray();
                ChunkRaw chunk = this.createEmptyChunk(b.length, false);
                chunk.data = b;
                return chunk;
            } catch (IOException var5) {
                throw new PngjException(var5);
            }
        } else {
            throw new PngjException("Text chunk key must be non empty");
        }
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        int nullsep = -1;
        for (int i = 0; i < c.data.length; i++) {
            if (c.data[i] == 0) {
                nullsep = i;
                break;
            }
        }
        if (nullsep >= 0 && nullsep <= c.data.length - 2) {
            this.key = ChunkHelper.toString(c.data, 0, nullsep);
            int compmet = c.data[nullsep + 1];
            if (compmet != 0) {
                throw new PngjException("bad zTXt chunk: unknown compression method");
            } else {
                byte[] uncomp = ChunkHelper.compressBytes(c.data, nullsep + 2, c.data.length - nullsep - 2, false);
                this.val = ChunkHelper.toString(uncomp);
            }
        } else {
            throw new PngjException("bad zTXt chunk: no separator found");
        }
    }
}