package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PngChunkSPLT extends PngChunkMultiple {

    public static final String ID = "sPLT";

    private String palName;

    private int sampledepth;

    private int[] palette;

    public PngChunkSPLT(ImageInfo info) {
        super("sPLT", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ba.write(ChunkHelper.toBytes(this.palName));
            ba.write(0);
            ba.write((byte) this.sampledepth);
            int nentries = this.getNentries();
            for (int n = 0; n < nentries; n++) {
                for (int i = 0; i < 4; i++) {
                    if (this.sampledepth == 8) {
                        PngHelperInternal.writeByte(ba, (byte) this.palette[n * 5 + i]);
                    } else {
                        PngHelperInternal.writeInt2(ba, this.palette[n * 5 + i]);
                    }
                }
                PngHelperInternal.writeInt2(ba, this.palette[n * 5 + 4]);
            }
            byte[] b = ba.toByteArray();
            ChunkRaw chunk = this.createEmptyChunk(b.length, false);
            chunk.data = b;
            return chunk;
        } catch (IOException var5) {
            throw new PngjException(var5);
        }
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        int t = -1;
        for (int i = 0; i < c.data.length; i++) {
            if (c.data[i] == 0) {
                t = i;
                break;
            }
        }
        if (t > 0 && t <= c.data.length - 2) {
            this.palName = ChunkHelper.toString(c.data, 0, t);
            this.sampledepth = PngHelperInternal.readInt1fromByte(c.data, t + 1);
            t += 2;
            int nentries = (c.data.length - t) / (this.sampledepth == 8 ? 6 : 10);
            this.palette = new int[nentries * 5];
            int ne = 0;
            for (int ix = 0; ix < nentries; ix++) {
                int r;
                int g;
                int b;
                int a;
                if (this.sampledepth == 8) {
                    r = PngHelperInternal.readInt1fromByte(c.data, t++);
                    g = PngHelperInternal.readInt1fromByte(c.data, t++);
                    b = PngHelperInternal.readInt1fromByte(c.data, t++);
                    a = PngHelperInternal.readInt1fromByte(c.data, t++);
                } else {
                    r = PngHelperInternal.readInt2fromBytes(c.data, t);
                    t += 2;
                    g = PngHelperInternal.readInt2fromBytes(c.data, t);
                    t += 2;
                    b = PngHelperInternal.readInt2fromBytes(c.data, t);
                    t += 2;
                    a = PngHelperInternal.readInt2fromBytes(c.data, t);
                    t += 2;
                }
                int f = PngHelperInternal.readInt2fromBytes(c.data, t);
                t += 2;
                this.palette[ne++] = r;
                this.palette[ne++] = g;
                this.palette[ne++] = b;
                this.palette[ne++] = a;
                this.palette[ne++] = f;
            }
        } else {
            throw new PngjException("bad sPLT chunk: no separator found");
        }
    }

    public int getNentries() {
        return this.palette.length / 5;
    }

    public String getPalName() {
        return this.palName;
    }

    public void setPalName(String palName) {
        this.palName = palName;
    }

    public int getSampledepth() {
        return this.sampledepth;
    }

    public void setSampledepth(int sampledepth) {
        this.sampledepth = sampledepth;
    }

    public int[] getPalette() {
        return this.palette;
    }

    public void setPalette(int[] palette) {
        this.palette = palette;
    }
}