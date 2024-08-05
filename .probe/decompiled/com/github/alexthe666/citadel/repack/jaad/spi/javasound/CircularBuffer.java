package com.github.alexthe666.citadel.repack.jaad.spi.javasound;

class CircularBuffer {

    private static final int BUFFER_SIZE = 327670;

    private final byte[] data;

    private final CircularBuffer.Trigger trigger;

    private long readPos;

    private long writePos;

    private boolean open;

    CircularBuffer(CircularBuffer.Trigger trigger) {
        this.trigger = trigger;
        this.data = new byte[327670];
        this.readPos = 0L;
        this.writePos = 0L;
        this.open = true;
    }

    public void close() {
        this.open = false;
    }

    public boolean isOpen() {
        return this.open;
    }

    public int availableRead() {
        return (int) (this.writePos - this.readPos);
    }

    public int availableWrite() {
        return 327670 - this.availableRead();
    }

    private int getReadPos() {
        return (int) (this.readPos % 327670L);
    }

    private int getWritePos() {
        return (int) (this.writePos % 327670L);
    }

    public int read(byte[] b) {
        return this.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) {
        if (!this.isOpen()) {
            if (this.availableRead() <= 0) {
                return -1;
            }
            len = Math.min(len, this.availableRead());
        }
        synchronized (this) {
            if (this.trigger != null && this.availableRead() < len) {
                this.trigger.execute();
            }
            len = Math.min(this.availableRead(), len);
            int remaining = len;
            while (remaining > 0) {
                while (this.availableRead() == 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException var9) {
                    }
                }
                int available = Math.min(this.availableRead(), remaining);
                while (available > 0) {
                    int toRead = Math.min(available, 327670 - this.getReadPos());
                    System.arraycopy(this.data, this.getReadPos(), b, off, toRead);
                    this.readPos += (long) toRead;
                    off += toRead;
                    available -= toRead;
                    remaining -= toRead;
                }
                this.notifyAll();
            }
            return len;
        }
    }

    public int write(byte[] b) {
        return this.write(b, 0, b.length);
    }

    public int write(byte[] b, int off, int len) {
        synchronized (this) {
            int remaining = len;
            while (remaining > 0) {
                while (this.availableWrite() == 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException var9) {
                    }
                }
                int available = Math.min(this.availableWrite(), remaining);
                while (available > 0) {
                    int toWrite = Math.min(available, 327670 - this.getWritePos());
                    System.arraycopy(b, off, this.data, this.getWritePos(), toWrite);
                    this.writePos += (long) toWrite;
                    off += toWrite;
                    available -= toWrite;
                    remaining -= toWrite;
                }
                this.notifyAll();
            }
            return len;
        }
    }

    interface Trigger {

        void execute();
    }
}