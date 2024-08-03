package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class SegmentReader {

    private ReadableByteChannel channel;

    private ByteBuffer buf;

    protected int curMarker;

    private int fetchSize;

    protected boolean done;

    private long pos;

    private int bytesInMarker;

    private int bufferIncrement = 32768;

    public SegmentReader(ReadableByteChannel channel, int fetchSize) throws IOException {
        this.channel = channel;
        this.fetchSize = fetchSize;
        this.buf = NIOUtils.fetchFromChannel(channel, 4);
        this.pos = (long) this.buf.remaining();
        this.curMarker = this.buf.getInt();
        this.bytesInMarker = 4;
    }

    public int getBufferIncrement() {
        return this.bufferIncrement;
    }

    public void setBufferIncrement(int bufferIncrement) {
        this.bufferIncrement = bufferIncrement;
    }

    public final SegmentReader.State readToNextMarkerPartial(ByteBuffer out) throws IOException {
        if (this.done) {
            return SegmentReader.State.STOP;
        } else {
            int skipOneMarker = this.curMarker >= 256 && this.curMarker <= 511 ? 1 : 0;
            int written = out.position();
            while (true) {
                while (!this.buf.hasRemaining()) {
                    this.buf = NIOUtils.fetchFromChannel(this.channel, this.fetchSize);
                    this.pos = this.pos + (long) this.buf.remaining();
                    if (!this.buf.hasRemaining()) {
                        written = out.position() - written;
                        if (written > 0 && this.curMarker >= 256 && this.curMarker <= 511) {
                            return SegmentReader.State.DONE;
                        }
                        while (this.bytesInMarker > 0 && out.hasRemaining()) {
                            out.put((byte) (this.curMarker >>> 24));
                            this.curMarker <<= 8;
                            this.bytesInMarker--;
                            if (this.curMarker >= 256 && this.curMarker <= 511) {
                                return SegmentReader.State.DONE;
                            }
                        }
                        if (this.bytesInMarker == 0) {
                            this.done = true;
                            return SegmentReader.State.STOP;
                        }
                        return SegmentReader.State.MORE_DATA;
                    }
                }
                if (this.curMarker >= 256 && this.curMarker <= 511) {
                    if (skipOneMarker == 0) {
                        return SegmentReader.State.DONE;
                    }
                    skipOneMarker--;
                }
                if (!out.hasRemaining()) {
                    return SegmentReader.State.MORE_DATA;
                }
                out.put((byte) (this.curMarker >>> 24));
                this.curMarker = this.curMarker << 8 | this.buf.get() & 255;
            }
        }
    }

    public ByteBuffer readToNextMarkerNewBuffer() throws IOException {
        if (this.done) {
            return null;
        } else {
            List<ByteBuffer> buffers = new ArrayList();
            this.readToNextMarkerBuffers(buffers);
            return NIOUtils.combineBuffers(buffers);
        }
    }

    public void readToNextMarkerBuffers(List<ByteBuffer> buffers) throws IOException {
        SegmentReader.State state;
        do {
            ByteBuffer curBuffer = ByteBuffer.allocate(this.bufferIncrement);
            state = this.readToNextMarkerPartial(curBuffer);
            curBuffer.flip();
            buffers.add(curBuffer);
        } while (state == SegmentReader.State.MORE_DATA);
    }

    public final boolean readToNextMarker(ByteBuffer out) throws IOException {
        SegmentReader.State state = this.readToNextMarkerPartial(out);
        if (state == SegmentReader.State.MORE_DATA) {
            throw new BufferOverflowException();
        } else {
            return state == SegmentReader.State.DONE;
        }
    }

    public final boolean skipToMarker() throws IOException {
        if (this.done) {
            return false;
        } else {
            do {
                while (!this.buf.hasRemaining()) {
                    this.buf = NIOUtils.fetchFromChannel(this.channel, this.fetchSize);
                    this.pos = this.pos + (long) this.buf.remaining();
                    if (!this.buf.hasRemaining()) {
                        this.done = true;
                        return false;
                    }
                }
                this.curMarker = this.curMarker << 8 | this.buf.get() & 255;
            } while (this.curMarker < 256 || this.curMarker > 511);
            return true;
        }
    }

    public final boolean read(ByteBuffer out, int length) throws IOException {
        if (this.done) {
            return false;
        } else {
            while (true) {
                while (!this.buf.hasRemaining()) {
                    this.buf = NIOUtils.fetchFromChannel(this.channel, this.fetchSize);
                    this.pos = this.pos + (long) this.buf.remaining();
                    if (!this.buf.hasRemaining()) {
                        out.putInt(this.curMarker);
                        this.done = true;
                        return false;
                    }
                }
                if (length-- == 0) {
                    return true;
                }
                out.put((byte) (this.curMarker >>> 24));
                this.curMarker = this.curMarker << 8 | this.buf.get() & 255;
            }
        }
    }

    public final long curPos() {
        return this.pos - (long) this.buf.remaining() - 4L;
    }

    public static enum State {

        MORE_DATA, DONE, STOP
    }
}