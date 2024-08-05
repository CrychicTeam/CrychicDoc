package info.journeymap.shaded.org.eclipse.jetty.io;

import java.nio.ByteBuffer;

public class ArrayByteBufferPool implements ByteBufferPool {

    private final int _min;

    private final int _maxQueue;

    private final ByteBufferPool.Bucket[] _direct;

    private final ByteBufferPool.Bucket[] _indirect;

    private final int _inc;

    public ArrayByteBufferPool() {
        this(-1, -1, -1, -1);
    }

    public ArrayByteBufferPool(int minSize, int increment, int maxSize) {
        this(minSize, increment, maxSize, -1);
    }

    public ArrayByteBufferPool(int minSize, int increment, int maxSize, int maxQueue) {
        if (minSize <= 0) {
            minSize = 0;
        }
        if (increment <= 0) {
            increment = 1024;
        }
        if (maxSize <= 0) {
            maxSize = 65536;
        }
        if (minSize >= increment) {
            throw new IllegalArgumentException("minSize >= increment");
        } else if (maxSize % increment == 0 && increment < maxSize) {
            this._min = minSize;
            this._inc = increment;
            this._direct = new ByteBufferPool.Bucket[maxSize / increment];
            this._indirect = new ByteBufferPool.Bucket[maxSize / increment];
            this._maxQueue = maxQueue;
            int size = 0;
            for (int i = 0; i < this._direct.length; i++) {
                size += this._inc;
                this._direct[i] = new ByteBufferPool.Bucket(this, size, this._maxQueue);
                this._indirect[i] = new ByteBufferPool.Bucket(this, size, this._maxQueue);
            }
        } else {
            throw new IllegalArgumentException("increment must be a divisor of maxSize");
        }
    }

    @Override
    public ByteBuffer acquire(int size, boolean direct) {
        ByteBufferPool.Bucket bucket = this.bucketFor(size, direct);
        return bucket == null ? this.newByteBuffer(size, direct) : bucket.acquire(direct);
    }

    @Override
    public void release(ByteBuffer buffer) {
        if (buffer != null) {
            ByteBufferPool.Bucket bucket = this.bucketFor(buffer.capacity(), buffer.isDirect());
            if (bucket != null) {
                bucket.release(buffer);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < this._direct.length; i++) {
            this._direct[i].clear();
            this._indirect[i].clear();
        }
    }

    private ByteBufferPool.Bucket bucketFor(int size, boolean direct) {
        if (size <= this._min) {
            return null;
        } else {
            int b = (size - 1) / this._inc;
            if (b >= this._direct.length) {
                return null;
            } else {
                return direct ? this._direct[b] : this._indirect[b];
            }
        }
    }

    ByteBufferPool.Bucket[] bucketsFor(boolean direct) {
        return direct ? this._direct : this._indirect;
    }
}