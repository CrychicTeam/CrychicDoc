package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkPLTE extends PngChunkSingle {

    public static final String ID = "PLTE";

    private int nentries = 0;

    private int[] entries;

    public PngChunkPLTE(ImageInfo info) {
        super("PLTE", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    @Override
    public ChunkRaw createRawChunk() {
        int len = 3 * this.nentries;
        int[] rgb = new int[3];
        ChunkRaw c = this.createEmptyChunk(len, true);
        int n = 0;
        for (int i = 0; n < this.nentries; n++) {
            this.getEntryRgb(n, rgb);
            c.data[i++] = (byte) rgb[0];
            c.data[i++] = (byte) rgb[1];
            c.data[i++] = (byte) rgb[2];
        }
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        this.setNentries(chunk.len / 3);
        int n = 0;
        for (int i = 0; n < this.nentries; n++) {
            this.setEntry(n, chunk.data[i++] & 255, chunk.data[i++] & 255, chunk.data[i++] & 255);
        }
    }

    public void setNentries(int n) {
        this.nentries = n;
        if (this.nentries >= 1 && this.nentries <= 256) {
            if (this.entries == null || this.entries.length != this.nentries) {
                this.entries = new int[this.nentries];
            }
        } else {
            throw new PngjException("invalid pallette - nentries=" + this.nentries);
        }
    }

    public int getNentries() {
        return this.nentries;
    }

    public void setEntry(int n, int r, int g, int b) {
        this.entries[n] = r << 16 | g << 8 | b;
    }

    public int getEntry(int n) {
        return this.entries[n];
    }

    public void getEntryRgb(int n, int[] rgb) {
        this.getEntryRgb(n, rgb, 0);
    }

    public void getEntryRgb(int n, int[] rgb, int offset) {
        int v = this.entries[n];
        rgb[offset + 0] = (v & 0xFF0000) >> 16;
        rgb[offset + 1] = (v & 0xFF00) >> 8;
        rgb[offset + 2] = v & 0xFF;
    }

    public int minBitDepth() {
        if (this.nentries <= 2) {
            return 1;
        } else if (this.nentries <= 4) {
            return 2;
        } else {
            return this.nentries <= 16 ? 4 : 8;
        }
    }
}