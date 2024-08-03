package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkFactory;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkHelper;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunksList;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunk;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunkIHDR;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkSeqReaderPng extends ChunkSeqReader {

    protected ImageInfo imageInfo;

    protected Deinterlacer deinterlacer;

    protected int currentChunkGroup = -1;

    protected ChunksList chunksList = null;

    protected final boolean callbackMode;

    private long bytesChunksLoaded = 0L;

    private boolean checkCrc = true;

    private boolean includeNonBufferedChunks = false;

    private Set<String> chunksToSkip = new HashSet();

    private long maxTotalBytesRead = 0L;

    private long skipChunkMaxSize = 0L;

    private long maxBytesMetadata = 0L;

    private IChunkFactory chunkFactory;

    private ChunkLoadBehaviour chunkLoadBehaviour = ChunkLoadBehaviour.LOAD_CHUNK_ALWAYS;

    public ChunkSeqReaderPng(boolean callbackMode) {
        this.callbackMode = callbackMode;
        this.chunkFactory = new ChunkFactory();
    }

    private void updateAndCheckChunkGroup(String id) {
        if (id.equals("IHDR")) {
            if (this.currentChunkGroup >= 0) {
                throw new PngjInputException("unexpected chunk " + id);
            }
            this.currentChunkGroup = 0;
        } else if (id.equals("PLTE")) {
            if (this.currentChunkGroup != 0 && this.currentChunkGroup != 1) {
                throw new PngjInputException("unexpected chunk " + id);
            }
            this.currentChunkGroup = 2;
        } else if (id.equals("IDAT")) {
            if (this.currentChunkGroup < 0 || this.currentChunkGroup > 4) {
                throw new PngjInputException("unexpected chunk " + id);
            }
            this.currentChunkGroup = 4;
        } else if (id.equals("IEND")) {
            if (this.currentChunkGroup < 4) {
                throw new PngjInputException("unexpected chunk " + id);
            }
            this.currentChunkGroup = 6;
        } else if (this.currentChunkGroup <= 1) {
            this.currentChunkGroup = 1;
        } else if (this.currentChunkGroup <= 3) {
            this.currentChunkGroup = 3;
        } else {
            this.currentChunkGroup = 5;
        }
    }

    @Override
    public boolean shouldSkipContent(int len, String id) {
        if (super.shouldSkipContent(len, id)) {
            return true;
        } else if (ChunkHelper.isCritical(id)) {
            return false;
        } else if (this.maxTotalBytesRead > 0L && (long) len + this.getBytesCount() > this.maxTotalBytesRead) {
            throw new PngjInputException("Maximum total bytes to read exceeeded: " + this.maxTotalBytesRead + " offset:" + this.getBytesCount() + " len=" + len);
        } else if (this.chunksToSkip.contains(id)) {
            return true;
        } else if (this.skipChunkMaxSize > 0L && (long) len > this.skipChunkMaxSize) {
            return true;
        } else if (this.maxBytesMetadata > 0L && (long) len > this.maxBytesMetadata - this.bytesChunksLoaded) {
            return true;
        } else {
            switch(this.chunkLoadBehaviour) {
                case LOAD_CHUNK_IF_SAFE:
                    if (!ChunkHelper.isSafeToCopy(id)) {
                        return true;
                    }
                default:
                    return false;
                case LOAD_CHUNK_NEVER:
                    return true;
            }
        }
    }

    public long getBytesChunksLoaded() {
        return this.bytesChunksLoaded;
    }

    public int getCurrentChunkGroup() {
        return this.currentChunkGroup;
    }

    public void setChunksToSkip(String... chunksToSkip) {
        this.chunksToSkip.clear();
        for (String c : chunksToSkip) {
            this.chunksToSkip.add(c);
        }
    }

    public void addChunkToSkip(String chunkToSkip) {
        this.chunksToSkip.add(chunkToSkip);
    }

    public boolean firstChunksNotYetRead() {
        return this.getCurrentChunkGroup() < 4;
    }

    @Override
    protected void postProcessChunk(ChunkReader chunkR) {
        super.postProcessChunk(chunkR);
        if (chunkR.getChunkRaw().id.equals("IHDR")) {
            PngChunkIHDR ch = new PngChunkIHDR(null);
            ch.parseFromRaw(chunkR.getChunkRaw());
            this.imageInfo = ch.createImageInfo();
            if (ch.isInterlaced()) {
                this.deinterlacer = new Deinterlacer(this.imageInfo);
            }
            this.chunksList = new ChunksList(this.imageInfo);
        }
        if (chunkR.mode == ChunkReader.ChunkReaderMode.BUFFER || this.includeNonBufferedChunks) {
            PngChunk chunk = this.chunkFactory.createChunk(chunkR.getChunkRaw(), this.getImageInfo());
            this.chunksList.appendReadChunk(chunk, this.currentChunkGroup);
        }
        if (this.isDone()) {
            this.processEndPng();
        }
    }

    @Override
    protected DeflatedChunksSet createIdatSet(String id) {
        IdatSet ids = new IdatSet(id, this.imageInfo, this.deinterlacer);
        ids.setCallbackMode(this.callbackMode);
        return ids;
    }

    public IdatSet getIdatSet() {
        DeflatedChunksSet c = this.getCurReaderDeflatedSet();
        return c instanceof IdatSet ? (IdatSet) c : null;
    }

    @Override
    protected boolean isIdatKind(String id) {
        return id.equals("IDAT");
    }

    @Override
    public int consume(byte[] buf, int off, int len) {
        return super.consume(buf, off, len);
    }

    public void setChunkFactory(IChunkFactory chunkFactory) {
        this.chunkFactory = chunkFactory;
    }

    protected void processEndPng() {
    }

    public ImageInfo getImageInfo() {
        return this.imageInfo;
    }

    public boolean isInterlaced() {
        return this.deinterlacer != null;
    }

    public Deinterlacer getDeinterlacer() {
        return this.deinterlacer;
    }

    @Override
    protected void startNewChunk(int len, String id, long offset) {
        this.updateAndCheckChunkGroup(id);
        super.startNewChunk(len, id, offset);
    }

    @Override
    public void close() {
        if (this.currentChunkGroup != 6) {
            this.currentChunkGroup = 6;
        }
        super.close();
    }

    public List<PngChunk> getChunks() {
        return this.chunksList.getChunks();
    }

    public void setMaxTotalBytesRead(long maxTotalBytesRead) {
        this.maxTotalBytesRead = maxTotalBytesRead;
    }

    public long getSkipChunkMaxSize() {
        return this.skipChunkMaxSize;
    }

    public void setSkipChunkMaxSize(long skipChunkMaxSize) {
        this.skipChunkMaxSize = skipChunkMaxSize;
    }

    public long getMaxBytesMetadata() {
        return this.maxBytesMetadata;
    }

    public void setMaxBytesMetadata(long maxBytesMetadata) {
        this.maxBytesMetadata = maxBytesMetadata;
    }

    public long getMaxTotalBytesRead() {
        return this.maxTotalBytesRead;
    }

    @Override
    protected boolean shouldCheckCrc(int len, String id) {
        return this.checkCrc;
    }

    public boolean isCheckCrc() {
        return this.checkCrc;
    }

    public void setCheckCrc(boolean checkCrc) {
        this.checkCrc = checkCrc;
    }

    public boolean isCallbackMode() {
        return this.callbackMode;
    }

    public Set<String> getChunksToSkip() {
        return this.chunksToSkip;
    }

    public void setChunkLoadBehaviour(ChunkLoadBehaviour chunkLoadBehaviour) {
        this.chunkLoadBehaviour = chunkLoadBehaviour;
    }

    public void setIncludeNonBufferedChunks(boolean includeNonBufferedChunks) {
        this.includeNonBufferedChunks = includeNonBufferedChunks;
    }
}