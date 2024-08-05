package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Destroyable;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

public class GZIPContentDecoder implements Destroyable {

    private final Inflater _inflater = new Inflater(true);

    private final ByteBufferPool _pool;

    private final int _bufferSize;

    private GZIPContentDecoder.State _state;

    private int _size;

    private int _value;

    private byte _flags;

    private ByteBuffer _inflated;

    public GZIPContentDecoder() {
        this(null, 2048);
    }

    public GZIPContentDecoder(int bufferSize) {
        this(null, bufferSize);
    }

    public GZIPContentDecoder(ByteBufferPool pool, int bufferSize) {
        this._bufferSize = bufferSize;
        this._pool = pool;
        this.reset();
    }

    public ByteBuffer decode(ByteBuffer compressed) {
        this.decodeChunks(compressed);
        if (!BufferUtil.isEmpty(this._inflated) && this._state != GZIPContentDecoder.State.CRC && this._state != GZIPContentDecoder.State.ISIZE) {
            ByteBuffer result = this._inflated;
            this._inflated = null;
            return result;
        } else {
            return BufferUtil.EMPTY_BUFFER;
        }
    }

    protected boolean decodedChunk(ByteBuffer chunk) {
        if (this._inflated == null) {
            this._inflated = chunk;
        } else {
            int size = this._inflated.remaining() + chunk.remaining();
            if (size <= this._inflated.capacity()) {
                BufferUtil.append(this._inflated, chunk);
                BufferUtil.put(chunk, this._inflated);
                this.release(chunk);
            } else {
                ByteBuffer bigger = this.acquire(size);
                int pos = BufferUtil.flipToFill(bigger);
                BufferUtil.put(this._inflated, bigger);
                BufferUtil.put(chunk, bigger);
                BufferUtil.flipToFlush(bigger, pos);
                this.release(this._inflated);
                this.release(chunk);
                this._inflated = bigger;
            }
        }
        return false;
    }

    protected void decodeChunks(ByteBuffer compressed) {
        ByteBuffer buffer = null;
        try {
            label227: while (true) {
                switch(this._state) {
                    case INITIAL:
                        this._state = GZIPContentDecoder.State.ID;
                        break;
                    case FLAGS:
                        if ((this._flags & 4) == 4) {
                            this._state = GZIPContentDecoder.State.EXTRA_LENGTH;
                            this._size = 0;
                            this._value = 0;
                        } else if ((this._flags & 8) == 8) {
                            this._state = GZIPContentDecoder.State.NAME;
                        } else if ((this._flags & 16) == 16) {
                            this._state = GZIPContentDecoder.State.COMMENT;
                        } else {
                            if ((this._flags & 2) != 2) {
                                this._state = GZIPContentDecoder.State.DATA;
                                continue;
                            }
                            this._state = GZIPContentDecoder.State.HCRC;
                            this._size = 0;
                            this._value = 0;
                        }
                        break;
                    case DATA:
                        while (true) {
                            if (buffer == null) {
                                buffer = this.acquire(this._bufferSize);
                            }
                            try {
                                int length = this._inflater.inflate(buffer.array(), buffer.arrayOffset(), buffer.capacity());
                                buffer.limit(length);
                            } catch (DataFormatException var8) {
                                throw new ZipException(var8.getMessage());
                            }
                            if (buffer.hasRemaining()) {
                                ByteBuffer chunk = buffer;
                                buffer = null;
                                if (this.decodedChunk(chunk)) {
                                    return;
                                }
                            } else if (this._inflater.needsInput()) {
                                if (!compressed.hasRemaining()) {
                                    return;
                                }
                                if (compressed.hasArray()) {
                                    this._inflater.setInput(compressed.array(), compressed.arrayOffset() + compressed.position(), compressed.remaining());
                                    compressed.position(compressed.limit());
                                } else {
                                    byte[] input = new byte[compressed.remaining()];
                                    compressed.get(input);
                                    this._inflater.setInput(input);
                                }
                            } else if (this._inflater.finished()) {
                                int remaining = this._inflater.getRemaining();
                                compressed.position(compressed.limit() - remaining);
                                this._state = GZIPContentDecoder.State.CRC;
                                this._size = 0;
                                this._value = 0;
                                continue label227;
                            }
                        }
                }
                if (!compressed.hasRemaining()) {
                    return;
                }
                byte currByte = compressed.get();
                switch(this._state) {
                    case ID:
                        this._value = this._value + ((currByte & 255) << 8 * this._size);
                        this._size++;
                        if (this._size == 2) {
                            if (this._value != 35615) {
                                throw new ZipException("Invalid gzip bytes");
                            }
                            this._state = GZIPContentDecoder.State.CM;
                        }
                        break;
                    case CM:
                        if ((currByte & 255) != 8) {
                            throw new ZipException("Invalid gzip compression method");
                        }
                        this._state = GZIPContentDecoder.State.FLG;
                        break;
                    case FLG:
                        this._flags = currByte;
                        this._state = GZIPContentDecoder.State.MTIME;
                        this._size = 0;
                        this._value = 0;
                        break;
                    case MTIME:
                        this._size++;
                        if (this._size == 4) {
                            this._state = GZIPContentDecoder.State.XFL;
                        }
                        break;
                    case XFL:
                        this._state = GZIPContentDecoder.State.OS;
                        break;
                    case OS:
                        this._state = GZIPContentDecoder.State.FLAGS;
                        break;
                    case EXTRA_LENGTH:
                        this._value = this._value + ((currByte & 255) << 8 * this._size);
                        this._size++;
                        if (this._size == 2) {
                            this._state = GZIPContentDecoder.State.EXTRA;
                        }
                        break;
                    case EXTRA:
                        this._value--;
                        if (this._value == 0) {
                            this._flags &= -5;
                            this._state = GZIPContentDecoder.State.FLAGS;
                        }
                        break;
                    case NAME:
                        if (currByte == 0) {
                            this._flags &= -9;
                            this._state = GZIPContentDecoder.State.FLAGS;
                        }
                        break;
                    case COMMENT:
                        if (currByte == 0) {
                            this._flags &= -17;
                            this._state = GZIPContentDecoder.State.FLAGS;
                        }
                        break;
                    case HCRC:
                        this._size++;
                        if (this._size == 2) {
                            this._flags &= -3;
                            this._state = GZIPContentDecoder.State.FLAGS;
                        }
                        break;
                    case CRC:
                        this._value = this._value + ((currByte & 255) << 8 * this._size);
                        this._size++;
                        if (this._size == 4) {
                            this._state = GZIPContentDecoder.State.ISIZE;
                            this._size = 0;
                            this._value = 0;
                        }
                        break;
                    case ISIZE:
                        this._value = this._value + ((currByte & 255) << 8 * this._size);
                        this._size++;
                        if (this._size != 4) {
                            break;
                        }
                        if ((long) this._value != this._inflater.getBytesWritten()) {
                            throw new ZipException("Invalid input size");
                        }
                        this.reset();
                        return;
                    default:
                        throw new ZipException();
                }
            }
        } catch (ZipException var9) {
            throw new RuntimeException(var9);
        } finally {
            if (buffer != null) {
                this.release(buffer);
            }
        }
    }

    private void reset() {
        this._inflater.reset();
        this._state = GZIPContentDecoder.State.INITIAL;
        this._size = 0;
        this._value = 0;
        this._flags = 0;
    }

    @Override
    public void destroy() {
        this._inflater.end();
    }

    public boolean isFinished() {
        return this._state == GZIPContentDecoder.State.INITIAL;
    }

    public ByteBuffer acquire(int capacity) {
        return this._pool == null ? BufferUtil.allocate(capacity) : this._pool.acquire(capacity, false);
    }

    public void release(ByteBuffer buffer) {
        if (this._pool != null && buffer != BufferUtil.EMPTY_BUFFER) {
            this._pool.release(buffer);
        }
    }

    private static enum State {

        INITIAL,
        ID,
        CM,
        FLG,
        MTIME,
        XFL,
        OS,
        FLAGS,
        EXTRA_LENGTH,
        EXTRA,
        NAME,
        COMMENT,
        HCRC,
        DATA,
        CRC,
        ISIZE
    }
}