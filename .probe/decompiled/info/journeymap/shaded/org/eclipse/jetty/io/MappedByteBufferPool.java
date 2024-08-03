package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MappedByteBufferPool implements ByteBufferPool {

    private final ConcurrentMap<Integer, ByteBufferPool.Bucket> directBuffers = new ConcurrentHashMap();

    private final ConcurrentMap<Integer, ByteBufferPool.Bucket> heapBuffers = new ConcurrentHashMap();

    private final int _factor;

    private final int _maxQueue;

    private final Function<Integer, ByteBufferPool.Bucket> _newBucket;

    public MappedByteBufferPool() {
        this(-1);
    }

    public MappedByteBufferPool(int factor) {
        this(factor, -1, null);
    }

    public MappedByteBufferPool(int factor, int maxQueue) {
        this(factor, maxQueue, null);
    }

    public MappedByteBufferPool(int factor, int maxQueue, Function<Integer, ByteBufferPool.Bucket> newBucket) {
        this._factor = factor <= 0 ? 1024 : factor;
        this._maxQueue = maxQueue;
        this._newBucket = newBucket != null ? newBucket : i -> new ByteBufferPool.Bucket(this, i * this._factor, this._maxQueue);
    }

    @Override
    public ByteBuffer acquire(int size, boolean direct) {
        int b = this.bucketFor(size);
        ConcurrentMap<Integer, ByteBufferPool.Bucket> buffers = this.bucketsFor(direct);
        ByteBufferPool.Bucket bucket = (ByteBufferPool.Bucket) buffers.get(b);
        return bucket == null ? this.newByteBuffer(b * this._factor, direct) : bucket.acquire(direct);
    }

    @Override
    public void release(ByteBuffer buffer) {
        if (buffer != null) {
            assert buffer.capacity() % this._factor == 0;
            int b = this.bucketFor(buffer.capacity());
            ConcurrentMap<Integer, ByteBufferPool.Bucket> buckets = this.bucketsFor(buffer.isDirect());
            ByteBufferPool.Bucket bucket = (ByteBufferPool.Bucket) buckets.computeIfAbsent(b, this._newBucket);
            bucket.release(buffer);
        }
    }

    public void clear() {
        this.directBuffers.values().forEach(ByteBufferPool.Bucket::clear);
        this.directBuffers.clear();
        this.heapBuffers.values().forEach(ByteBufferPool.Bucket::clear);
        this.heapBuffers.clear();
    }

    private int bucketFor(int size) {
        int bucket = size / this._factor;
        if (size % this._factor > 0) {
            bucket++;
        }
        return bucket;
    }

    ConcurrentMap<Integer, ByteBufferPool.Bucket> bucketsFor(boolean direct) {
        return direct ? this.directBuffers : this.heapBuffers;
    }

    public static class Tagged extends MappedByteBufferPool {

        private final AtomicInteger tag = new AtomicInteger();

        @Override
        public ByteBuffer newByteBuffer(int capacity, boolean direct) {
            ByteBuffer buffer = super.newByteBuffer(capacity + 4, direct);
            buffer.limit(buffer.capacity());
            buffer.putInt(this.tag.incrementAndGet());
            ByteBuffer slice = buffer.slice();
            BufferUtil.clear(slice);
            return slice;
        }
    }
}