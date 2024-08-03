package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkRaw;

public abstract class ChunkReader {

    public final ChunkReader.ChunkReaderMode mode;

    private final ChunkRaw chunkRaw;

    private boolean crcCheck;

    protected int read = 0;

    private int crcn = 0;

    public ChunkReader(int clen, String id, long offsetInPng, ChunkReader.ChunkReaderMode mode) {
        if (mode != null && id.length() == 4 && clen >= 0) {
            this.mode = mode;
            this.chunkRaw = new ChunkRaw(clen, id, mode == ChunkReader.ChunkReaderMode.BUFFER);
            this.chunkRaw.setOffset(offsetInPng);
            this.crcCheck = mode != ChunkReader.ChunkReaderMode.SKIP;
        } else {
            throw new PngjExceptionInternal("Bad chunk paramenters: " + mode);
        }
    }

    public ChunkRaw getChunkRaw() {
        return this.chunkRaw;
    }

    public final int feedBytes(byte[] buf, int off, int len) {
        if (len == 0) {
            return 0;
        } else if (len < 0) {
            throw new PngjException("negative length??");
        } else {
            if (this.read == 0 && this.crcn == 0 && this.crcCheck) {
                this.chunkRaw.updateCrc(this.chunkRaw.idbytes, 0, 4);
            }
            int bytesForData = this.chunkRaw.len - this.read;
            if (bytesForData > len) {
                bytesForData = len;
            }
            if (bytesForData > 0 || this.crcn == 0) {
                if (this.crcCheck && this.mode != ChunkReader.ChunkReaderMode.BUFFER && bytesForData > 0) {
                    this.chunkRaw.updateCrc(buf, off, bytesForData);
                }
                if (this.mode == ChunkReader.ChunkReaderMode.BUFFER) {
                    if (this.chunkRaw.data != buf && bytesForData > 0) {
                        System.arraycopy(buf, off, this.chunkRaw.data, this.read, bytesForData);
                    }
                } else if (this.mode == ChunkReader.ChunkReaderMode.PROCESS) {
                    this.processData(this.read, buf, off, bytesForData);
                }
                this.read += bytesForData;
                off += bytesForData;
                len -= bytesForData;
            }
            int crcRead = 0;
            if (this.read == this.chunkRaw.len) {
                crcRead = 4 - this.crcn;
                if (crcRead > len) {
                    crcRead = len;
                }
                if (crcRead > 0) {
                    if (buf != this.chunkRaw.crcval) {
                        System.arraycopy(buf, off, this.chunkRaw.crcval, this.crcn, crcRead);
                    }
                    this.crcn += crcRead;
                    if (this.crcn == 4) {
                        if (this.crcCheck) {
                            if (this.mode == ChunkReader.ChunkReaderMode.BUFFER) {
                                this.chunkRaw.updateCrc(this.chunkRaw.data, 0, this.chunkRaw.len);
                            }
                            this.chunkRaw.checkCrc();
                        }
                        this.chunkDone();
                    }
                }
            }
            return bytesForData + crcRead;
        }
    }

    public final boolean isDone() {
        return this.crcn == 4;
    }

    public void setCrcCheck(boolean crcCheck) {
        if (this.read != 0 && crcCheck && !this.crcCheck) {
            throw new PngjException("too late!");
        } else {
            this.crcCheck = crcCheck;
        }
    }

    protected abstract void processData(int var1, byte[] var2, int var3, int var4);

    protected abstract void chunkDone();

    public int hashCode() {
        int prime = 31;
        int result = 1;
        return 31 * result + (this.chunkRaw == null ? 0 : this.chunkRaw.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ChunkReader other = (ChunkReader) obj;
            if (this.chunkRaw == null) {
                if (other.chunkRaw != null) {
                    return false;
                }
            } else if (!this.chunkRaw.equals(other.chunkRaw)) {
                return false;
            }
            return true;
        }
    }

    public String toString() {
        return this.chunkRaw.toString();
    }

    public static enum ChunkReaderMode {

        BUFFER, PROCESS, SKIP
    }
}