package info.journeymap.shaded.ar.com.hjg.pngj;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class DeflatedChunksSet {

    protected byte[] row;

    private int rowfilled;

    private int rowlen;

    private int rown;

    DeflatedChunksSet.State state = DeflatedChunksSet.State.WAITING_FOR_INPUT;

    private Inflater inf;

    private final boolean infOwn;

    private DeflatedChunkReader curChunk;

    private boolean callbackMode = true;

    private long nBytesIn = 0L;

    private long nBytesOut = 0L;

    public final String chunkid;

    public DeflatedChunksSet(String chunkid, int initialRowLen, int maxRowLen, Inflater inflater, byte[] buffer) {
        this.chunkid = chunkid;
        this.rowlen = initialRowLen;
        if (initialRowLen >= 1 && maxRowLen >= initialRowLen) {
            if (inflater != null) {
                this.inf = inflater;
                this.infOwn = false;
            } else {
                this.inf = new Inflater();
                this.infOwn = true;
            }
            this.row = buffer != null && buffer.length >= initialRowLen ? buffer : new byte[maxRowLen];
            this.rown = -1;
            this.state = DeflatedChunksSet.State.WAITING_FOR_INPUT;
            try {
                this.prepareForNextRow(initialRowLen);
            } catch (RuntimeException var7) {
                this.close();
                throw var7;
            }
        } else {
            throw new PngjException("bad inital row len " + initialRowLen);
        }
    }

    public DeflatedChunksSet(String chunkid, int initialRowLen, int maxRowLen) {
        this(chunkid, initialRowLen, maxRowLen, null, null);
    }

    protected void appendNewChunk(DeflatedChunkReader cr) {
        if (!this.chunkid.equals(cr.getChunkRaw().id)) {
            throw new PngjInputException("Bad chunk inside IdatSet, id:" + cr.getChunkRaw().id + ", expected:" + this.chunkid);
        } else {
            this.curChunk = cr;
        }
    }

    protected void processBytes(byte[] buf, int off, int len) {
        this.nBytesIn += (long) len;
        if (len >= 1 && !this.state.isDone()) {
            if (this.state == DeflatedChunksSet.State.ROW_READY) {
                throw new PngjInputException("this should only be called if waitingForMoreInput");
            } else if (!this.inf.needsDictionary() && this.inf.needsInput()) {
                this.inf.setInput(buf, off, len);
                if (this.isCallbackMode()) {
                    while (this.inflateData()) {
                        int nextRowLen = this.processRowCallback();
                        this.prepareForNextRow(nextRowLen);
                        if (this.isDone()) {
                            this.processDoneCallback();
                        }
                    }
                } else {
                    this.inflateData();
                }
            } else {
                throw new RuntimeException("should not happen");
            }
        }
    }

    private boolean inflateData() {
        try {
            if (this.state == DeflatedChunksSet.State.ROW_READY) {
                throw new PngjException("invalid state");
            } else if (this.state.isDone()) {
                return false;
            } else {
                int ninflated = 0;
                if (this.row == null || this.row.length < this.rowlen) {
                    this.row = new byte[this.rowlen];
                }
                if (this.rowfilled < this.rowlen && !this.inf.finished()) {
                    try {
                        ninflated = this.inf.inflate(this.row, this.rowfilled, this.rowlen - this.rowfilled);
                    } catch (DataFormatException var3) {
                        throw new PngjInputException("error decompressing zlib stream ", var3);
                    }
                    this.rowfilled += ninflated;
                    this.nBytesOut += (long) ninflated;
                }
                DeflatedChunksSet.State nextstate = null;
                if (this.rowfilled == this.rowlen) {
                    nextstate = DeflatedChunksSet.State.ROW_READY;
                } else if (!this.inf.finished()) {
                    nextstate = DeflatedChunksSet.State.WAITING_FOR_INPUT;
                } else if (this.rowfilled > 0) {
                    nextstate = DeflatedChunksSet.State.ROW_READY;
                } else {
                    nextstate = DeflatedChunksSet.State.WORK_DONE;
                }
                this.state = nextstate;
                if (this.state == DeflatedChunksSet.State.ROW_READY) {
                    this.preProcessRow();
                    return true;
                } else {
                    return false;
                }
            }
        } catch (RuntimeException var4) {
            this.close();
            throw var4;
        }
    }

    protected void preProcessRow() {
    }

    protected int processRowCallback() {
        throw new PngjInputException("not implemented");
    }

    protected void processDoneCallback() {
    }

    public byte[] getInflatedRow() {
        return this.row;
    }

    public void prepareForNextRow(int len) {
        this.rowfilled = 0;
        this.rown++;
        if (len < 1) {
            this.rowlen = 0;
            this.done();
        } else if (this.inf.finished()) {
            this.rowlen = 0;
            this.done();
        } else {
            this.state = DeflatedChunksSet.State.WAITING_FOR_INPUT;
            this.rowlen = len;
            if (!this.callbackMode) {
                this.inflateData();
            }
        }
    }

    public boolean isWaitingForMoreInput() {
        return this.state == DeflatedChunksSet.State.WAITING_FOR_INPUT;
    }

    public boolean isRowReady() {
        return this.state == DeflatedChunksSet.State.ROW_READY;
    }

    public boolean isDone() {
        return this.state.isDone();
    }

    public boolean isTerminated() {
        return this.state.isTerminated();
    }

    public boolean ackNextChunkId(String id) {
        if (this.state.isTerminated()) {
            return false;
        } else if (id.equals(this.chunkid)) {
            return true;
        } else if (!this.allowOtherChunksInBetween(id)) {
            if (this.state.isDone()) {
                if (!this.isTerminated()) {
                    this.terminate();
                }
                return false;
            } else {
                throw new PngjInputException("Unexpected chunk " + id + " while " + this.chunkid + " set is not done");
            }
        } else {
            return true;
        }
    }

    protected void terminate() {
        this.close();
    }

    public void close() {
        try {
            if (!this.state.isTerminated()) {
                this.state = DeflatedChunksSet.State.TERMINATED;
            }
            if (this.infOwn && this.inf != null) {
                this.inf.end();
                this.inf = null;
            }
        } catch (Exception var2) {
        }
    }

    public void done() {
        if (!this.isDone()) {
            this.state = DeflatedChunksSet.State.WORK_DONE;
        }
    }

    public int getRowLen() {
        return this.rowlen;
    }

    public int getRowFilled() {
        return this.rowfilled;
    }

    public int getRown() {
        return this.rown;
    }

    public boolean allowOtherChunksInBetween(String id) {
        return false;
    }

    public boolean isCallbackMode() {
        return this.callbackMode;
    }

    public void setCallbackMode(boolean callbackMode) {
        this.callbackMode = callbackMode;
    }

    public long getBytesIn() {
        return this.nBytesIn;
    }

    public long getBytesOut() {
        return this.nBytesOut;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("idatSet : " + this.curChunk.getChunkRaw().id + " state=" + this.state + " rows=" + this.rown + " bytes=" + this.nBytesIn + "/" + this.nBytesOut);
        return sb.toString();
    }

    private static enum State {

        WAITING_FOR_INPUT, ROW_READY, WORK_DONE, TERMINATED;

        public boolean isDone() {
            return this == WORK_DONE || this == TERMINATED;
        }

        public boolean isTerminated() {
            return this == TERMINATED;
        }
    }
}