package info.journeymap.shaded.ar.com.hjg.pngj;

public class DeflatedChunkReader extends ChunkReader {

    protected final DeflatedChunksSet deflatedChunksSet;

    protected boolean alsoBuffer = false;

    public DeflatedChunkReader(int clen, String chunkid, boolean checkCrc, long offsetInPng, DeflatedChunksSet iDatSet) {
        super(clen, chunkid, offsetInPng, ChunkReader.ChunkReaderMode.PROCESS);
        this.deflatedChunksSet = iDatSet;
        iDatSet.appendNewChunk(this);
    }

    @Override
    protected void processData(int offsetInchunk, byte[] buf, int off, int len) {
        if (len > 0) {
            this.deflatedChunksSet.processBytes(buf, off, len);
            if (this.alsoBuffer) {
                System.arraycopy(buf, off, this.getChunkRaw().data, this.read, len);
            }
        }
    }

    @Override
    protected void chunkDone() {
    }

    public void setAlsoBuffer() {
        if (this.read > 0) {
            throw new RuntimeException("too late");
        } else {
            this.alsoBuffer = true;
            this.getChunkRaw().allocData();
        }
    }
}