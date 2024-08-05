package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjOutputException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ChunksListForWrite extends ChunksList {

    private final List<PngChunk> queuedChunks = new ArrayList();

    private HashMap<String, Integer> alreadyWrittenKeys = new HashMap();

    public ChunksListForWrite(ImageInfo imfinfo) {
        super(imfinfo);
    }

    public List<? extends PngChunk> getQueuedById(String id) {
        return this.getQueuedById(id, null);
    }

    public List<? extends PngChunk> getQueuedById(String id, String innerid) {
        return getXById(this.queuedChunks, id, innerid);
    }

    public PngChunk getQueuedById1(String id, String innerid, boolean failIfMultiple) {
        List<? extends PngChunk> list = this.getQueuedById(id, innerid);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() <= 1 || !failIfMultiple && ((PngChunk) list.get(0)).allowsMultiple()) {
            return (PngChunk) list.get(list.size() - 1);
        } else {
            throw new PngjException("unexpected multiple chunks id=" + id);
        }
    }

    public PngChunk getQueuedById1(String id, boolean failIfMultiple) {
        return this.getQueuedById1(id, null, failIfMultiple);
    }

    public PngChunk getQueuedById1(String id) {
        return this.getQueuedById1(id, false);
    }

    public List<PngChunk> getQueuedEquivalent(final PngChunk c2) {
        return ChunkHelper.filterList(this.queuedChunks, new ChunkPredicate() {

            @Override
            public boolean match(PngChunk c) {
                return ChunkHelper.equivalent(c, c2);
            }
        });
    }

    public boolean removeChunk(PngChunk c) {
        return c == null ? false : this.queuedChunks.remove(c);
    }

    public boolean queue(PngChunk c) {
        this.queuedChunks.add(c);
        return true;
    }

    private static boolean shouldWrite(PngChunk c, int currentGroup) {
        if (currentGroup == 2) {
            return c.id.equals("PLTE");
        } else if (currentGroup % 2 == 0) {
            throw new PngjOutputException("bad chunk group?");
        } else {
            int minChunkGroup;
            int maxChunkGroup;
            if (c.getOrderingConstraint().mustGoBeforePLTE()) {
                maxChunkGroup = 1;
                minChunkGroup = 1;
            } else if (c.getOrderingConstraint().mustGoBeforeIDAT()) {
                maxChunkGroup = 3;
                minChunkGroup = c.getOrderingConstraint().mustGoAfterPLTE() ? 3 : 1;
            } else {
                maxChunkGroup = 5;
                minChunkGroup = 1;
            }
            int preferred = maxChunkGroup;
            if (c.hasPriority()) {
                preferred = minChunkGroup;
            }
            if (ChunkHelper.isUnknown(c) && c.getChunkGroup() > 0) {
                preferred = c.getChunkGroup();
            }
            return currentGroup == preferred ? true : currentGroup > preferred && currentGroup <= maxChunkGroup;
        }
    }

    public int writeChunks(OutputStream os, int currentGroup) {
        int cont = 0;
        Iterator<PngChunk> it = this.queuedChunks.iterator();
        while (it.hasNext()) {
            PngChunk c = (PngChunk) it.next();
            if (shouldWrite(c, currentGroup)) {
                if (ChunkHelper.isCritical(c.id) && !c.id.equals("PLTE")) {
                    throw new PngjOutputException("bad chunk queued: " + c);
                }
                if (this.alreadyWrittenKeys.containsKey(c.id) && !c.allowsMultiple()) {
                    throw new PngjOutputException("duplicated chunk does not allow multiple: " + c);
                }
                c.write(os);
                this.chunks.add(c);
                this.alreadyWrittenKeys.put(c.id, this.alreadyWrittenKeys.containsKey(c.id) ? (Integer) this.alreadyWrittenKeys.get(c.id) + 1 : 1);
                c.setChunkGroup(currentGroup);
                it.remove();
                cont++;
            }
        }
        return cont;
    }

    public List<PngChunk> getQueuedChunks() {
        return this.queuedChunks;
    }

    @Override
    public String toString() {
        return "ChunkList: written: " + this.getChunks().size() + " queue: " + this.queuedChunks.size();
    }

    @Override
    public String toStringFull() {
        StringBuilder sb = new StringBuilder(this.toString());
        sb.append("\n Written:\n");
        for (PngChunk chunk : this.getChunks()) {
            sb.append(chunk).append(" G=" + chunk.getChunkGroup() + "\n");
        }
        if (!this.queuedChunks.isEmpty()) {
            sb.append(" Queued:\n");
            for (PngChunk chunk : this.queuedChunks) {
                sb.append(chunk).append("\n");
            }
        }
        return sb.toString();
    }
}