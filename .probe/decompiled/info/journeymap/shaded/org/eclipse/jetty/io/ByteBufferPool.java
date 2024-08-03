package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public interface ByteBufferPool {

    ByteBuffer acquire(int var1, boolean var2);

    void release(ByteBuffer var1);

    default ByteBuffer newByteBuffer(int capacity, boolean direct) {
        return direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity);
    }

    public static class Bucket {

        private final Deque<ByteBuffer> _queue = new ConcurrentLinkedDeque();

        private final ByteBufferPool _pool;

        private final int _capacity;

        private final AtomicInteger _space;

        public Bucket(ByteBufferPool pool, int bufferSize, int maxSize) {
            this._pool = pool;
            this._capacity = bufferSize;
            this._space = maxSize > 0 ? new AtomicInteger(maxSize) : null;
        }

        public ByteBuffer acquire(boolean direct) {
            ByteBuffer buffer = this.queuePoll();
            if (buffer == null) {
                return this._pool.newByteBuffer(this._capacity, direct);
            } else {
                if (this._space != null) {
                    this._space.incrementAndGet();
                }
                return buffer;
            }
        }

        public void release(ByteBuffer buffer) {
            BufferUtil.clear(buffer);
            if (this._space == null) {
                this.queueOffer(buffer);
            } else if (this._space.decrementAndGet() >= 0) {
                this.queueOffer(buffer);
            } else {
                this._space.incrementAndGet();
            }
        }

        public void clear() {
            if (this._space == null) {
                this.queueClear();
            } else {
                int s = this._space.getAndSet(0);
                while (s-- > 0) {
                    if (this.queuePoll() == null) {
                        this._space.incrementAndGet();
                    }
                }
            }
        }

        private void queueOffer(ByteBuffer buffer) {
            this._queue.offerFirst(buffer);
        }

        private ByteBuffer queuePoll() {
            return (ByteBuffer) this._queue.poll();
        }

        private void queueClear() {
            this._queue.clear();
        }

        boolean isEmpty() {
            return this._queue.isEmpty();
        }

        int size() {
            return this._queue.size();
        }

        public String toString() {
            return String.format("Bucket@%x{%d/%d}", this.hashCode(), this.size(), this._capacity);
        }
    }

    public static class Lease {

        private final ByteBufferPool byteBufferPool;

        private final List<ByteBuffer> buffers;

        private final List<Boolean> recycles;

        public Lease(ByteBufferPool byteBufferPool) {
            this.byteBufferPool = byteBufferPool;
            this.buffers = new ArrayList();
            this.recycles = new ArrayList();
        }

        public ByteBuffer acquire(int capacity, boolean direct) {
            ByteBuffer buffer = this.byteBufferPool.acquire(capacity, direct);
            BufferUtil.clearToFill(buffer);
            return buffer;
        }

        public void append(ByteBuffer buffer, boolean recycle) {
            this.buffers.add(buffer);
            this.recycles.add(recycle);
        }

        public void insert(int index, ByteBuffer buffer, boolean recycle) {
            this.buffers.add(index, buffer);
            this.recycles.add(index, recycle);
        }

        public List<ByteBuffer> getByteBuffers() {
            return this.buffers;
        }

        public long getTotalLength() {
            long length = 0L;
            for (int i = 0; i < this.buffers.size(); i++) {
                length += (long) ((ByteBuffer) this.buffers.get(i)).remaining();
            }
            return length;
        }

        public int getSize() {
            return this.buffers.size();
        }

        public void recycle() {
            for (int i = 0; i < this.buffers.size(); i++) {
                ByteBuffer buffer = (ByteBuffer) this.buffers.get(i);
                if ((Boolean) this.recycles.get(i)) {
                    this.byteBufferPool.release(buffer);
                }
            }
            this.buffers.clear();
            this.recycles.clear();
        }
    }
}