package me.jellysquid.mods.sodium.client.util.collections;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DoubleBufferedQueue<E> {

    private DoubleBufferedQueue.QueueImpl<E> read = new DoubleBufferedQueue.QueueImpl<>();

    private DoubleBufferedQueue.QueueImpl<E> write = new DoubleBufferedQueue.QueueImpl<>();

    public boolean flip() {
        if (this.write.size() == 0) {
            return false;
        } else {
            DoubleBufferedQueue.QueueImpl<E> tmp = this.read;
            this.read = this.write;
            this.write = tmp;
            this.write.clear();
            return true;
        }
    }

    public void reset() {
        this.read.clear();
        this.write.clear();
    }

    public ReadQueue<E> read() {
        return this.read;
    }

    public WriteQueue<E> write() {
        return this.write;
    }

    private static final class QueueImpl<E> implements ReadQueue<E>, WriteQueue<E> {

        private E[] elements;

        private int readIndex;

        private int writeIndex;

        QueueImpl() {
            this(256);
        }

        QueueImpl(int capacity) {
            this.elements = (E[]) (new Object[capacity]);
        }

        @Override
        public void ensureCapacity(int numElements) {
            int len = this.writeIndex + numElements;
            if (len > this.elements.length) {
                this.grow(len);
            }
        }

        @Nullable
        @Override
        public E dequeue() {
            return this.readIndex == this.writeIndex ? null : this.elements[this.readIndex++];
        }

        @Override
        public void enqueue(@NotNull E e) {
            if (this.writeIndex >= this.elements.length) {
                this.resize(this.writeIndex + 1);
            }
            this.elements[this.writeIndex++] = e;
        }

        public void clear() {
            if (this.writeIndex != 0) {
                Arrays.fill(this.elements, 0, this.writeIndex, null);
            }
            this.readIndex = 0;
            this.writeIndex = 0;
        }

        public int size() {
            return this.writeIndex - this.readIndex;
        }

        private void grow(int minimumSize) {
            this.resize(getNextSize(minimumSize, this.elements.length));
        }

        private void resize(int length) {
            E[] elements = (E[]) (new Object[length]);
            System.arraycopy(this.elements, 0, elements, 0, this.writeIndex);
            this.elements = elements;
        }

        private static int getNextSize(int minimumSize, int currentSize) {
            return Math.max(minimumSize, currentSize << 1);
        }
    }
}