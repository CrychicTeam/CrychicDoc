package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import java.util.ArrayList;
import java.util.List;

public class PngMetadata {

    private final ChunksList chunkList;

    private final boolean readonly;

    public PngMetadata(ChunksList chunks) {
        this.chunkList = chunks;
        if (chunks instanceof ChunksListForWrite) {
            this.readonly = false;
        } else {
            this.readonly = true;
        }
    }

    public void queueChunk(final PngChunk c, boolean lazyOverwrite) {
        ChunksListForWrite cl = this.getChunkListW();
        if (this.readonly) {
            throw new PngjException("cannot set chunk : readonly metadata");
        } else {
            if (lazyOverwrite) {
                ChunkHelper.trimList(cl.getQueuedChunks(), new ChunkPredicate() {

                    @Override
                    public boolean match(PngChunk c2) {
                        return ChunkHelper.equivalent(c, c2);
                    }
                });
            }
            cl.queue(c);
        }
    }

    public void queueChunk(PngChunk c) {
        this.queueChunk(c, true);
    }

    private ChunksListForWrite getChunkListW() {
        return (ChunksListForWrite) this.chunkList;
    }

    public double[] getDpi() {
        PngChunk c = this.chunkList.getById1("pHYs", true);
        return c == null ? new double[] { -1.0, -1.0 } : ((PngChunkPHYS) c).getAsDpi2();
    }

    public void setDpi(double x) {
        this.setDpi(x, x);
    }

    public void setDpi(double x, double y) {
        PngChunkPHYS c = new PngChunkPHYS(this.chunkList.imageInfo);
        c.setAsDpi2(x, y);
        this.queueChunk(c);
    }

    public PngChunkTIME setTimeNow(int secsAgo) {
        PngChunkTIME c = new PngChunkTIME(this.chunkList.imageInfo);
        c.setNow(secsAgo);
        this.queueChunk(c);
        return c;
    }

    public PngChunkTIME setTimeNow() {
        return this.setTimeNow(0);
    }

    public PngChunkTIME setTimeYMDHMS(int yearx, int monx, int dayx, int hourx, int minx, int secx) {
        PngChunkTIME c = new PngChunkTIME(this.chunkList.imageInfo);
        c.setYMDHMS(yearx, monx, dayx, hourx, minx, secx);
        this.queueChunk(c, true);
        return c;
    }

    public PngChunkTIME getTime() {
        return (PngChunkTIME) this.chunkList.getById1("tIME");
    }

    public String getTimeAsString() {
        PngChunkTIME c = this.getTime();
        return c == null ? "" : c.getAsString();
    }

    public PngChunkTextVar setText(String k, String val, boolean useLatin1, boolean compress) {
        if (compress && !useLatin1) {
            throw new PngjException("cannot compress non latin text");
        } else {
            PngChunkTextVar c;
            if (useLatin1) {
                if (compress) {
                    c = new PngChunkZTXT(this.chunkList.imageInfo);
                } else {
                    c = new PngChunkTEXT(this.chunkList.imageInfo);
                }
            } else {
                c = new PngChunkITXT(this.chunkList.imageInfo);
                ((PngChunkITXT) c).setLangtag(k);
            }
            c.setKeyVal(k, val);
            this.queueChunk(c, true);
            return c;
        }
    }

    public PngChunkTextVar setText(String k, String val) {
        return this.setText(k, val, false, false);
    }

    public List<? extends PngChunkTextVar> getTxtsForKey(String k) {
        List c = new ArrayList();
        c.addAll(this.chunkList.getById("tEXt", k));
        c.addAll(this.chunkList.getById("zTXt", k));
        c.addAll(this.chunkList.getById("iTXt", k));
        return c;
    }

    public String getTxtForKey(String k) {
        List<? extends PngChunkTextVar> li = this.getTxtsForKey(k);
        if (li.isEmpty()) {
            return "";
        } else {
            StringBuilder t = new StringBuilder();
            for (PngChunkTextVar c : li) {
                t.append(c.getVal()).append("\n");
            }
            return t.toString().trim();
        }
    }

    public PngChunkPLTE getPLTE() {
        return (PngChunkPLTE) this.chunkList.getById1("PLTE");
    }

    public PngChunkPLTE createPLTEChunk() {
        PngChunkPLTE plte = new PngChunkPLTE(this.chunkList.imageInfo);
        this.queueChunk(plte);
        return plte;
    }

    public PngChunkTRNS getTRNS() {
        return (PngChunkTRNS) this.chunkList.getById1("tRNS");
    }

    public PngChunkTRNS createTRNSChunk() {
        PngChunkTRNS trns = new PngChunkTRNS(this.chunkList.imageInfo);
        this.queueChunk(trns);
        return trns;
    }
}