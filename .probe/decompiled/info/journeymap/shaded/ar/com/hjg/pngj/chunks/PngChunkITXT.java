package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PngChunkITXT extends PngChunkTextVar {

    public static final String ID = "iTXt";

    private boolean compressed = false;

    private String langTag = "";

    private String translatedTag = "";

    public PngChunkITXT(ImageInfo info) {
        super("iTXt", info);
    }

    @Override
    public ChunkRaw createRawChunk() {
        if (this.key != null && this.key.trim().length() != 0) {
            try {
                ByteArrayOutputStream ba = new ByteArrayOutputStream();
                ba.write(ChunkHelper.toBytes(this.key));
                ba.write(0);
                ba.write(this.compressed ? 1 : 0);
                ba.write(0);
                ba.write(ChunkHelper.toBytes(this.langTag));
                ba.write(0);
                ba.write(ChunkHelper.toBytesUTF8(this.translatedTag));
                ba.write(0);
                byte[] textbytes = ChunkHelper.toBytesUTF8(this.val);
                if (this.compressed) {
                    textbytes = ChunkHelper.compressBytes(textbytes, true);
                }
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
        int nullsFound = 0;
        int[] nullsIdx = new int[3];
        for (int i = 0; i < c.data.length; i++) {
            if (c.data[i] == 0) {
                nullsIdx[nullsFound] = i;
                if (++nullsFound == 1) {
                    i += 2;
                }
                if (nullsFound == 3) {
                    break;
                }
            }
        }
        if (nullsFound != 3) {
            throw new PngjException("Bad formed PngChunkITXT chunk");
        } else {
            this.key = ChunkHelper.toString(c.data, 0, nullsIdx[0]);
            int ix = nullsIdx[0] + 1;
            this.compressed = c.data[ix] != 0;
            if (this.compressed && c.data[++ix] != 0) {
                throw new PngjException("Bad formed PngChunkITXT chunk - bad compression method ");
            } else {
                this.langTag = ChunkHelper.toString(c.data, ix, nullsIdx[1] - ix);
                this.translatedTag = ChunkHelper.toStringUTF8(c.data, nullsIdx[1] + 1, nullsIdx[2] - nullsIdx[1] - 1);
                ix = nullsIdx[2] + 1;
                if (this.compressed) {
                    byte[] bytes = ChunkHelper.compressBytes(c.data, ix, c.data.length - ix, false);
                    this.val = ChunkHelper.toStringUTF8(bytes);
                } else {
                    this.val = ChunkHelper.toStringUTF8(c.data, ix, c.data.length - ix);
                }
            }
        }
    }

    public boolean isCompressed() {
        return this.compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getLangtag() {
        return this.langTag;
    }

    public void setLangtag(String langtag) {
        this.langTag = langtag;
    }

    public String getTranslatedTag() {
        return this.translatedTag;
    }

    public void setTranslatedTag(String translatedTag) {
        this.translatedTag = translatedTag;
    }
}