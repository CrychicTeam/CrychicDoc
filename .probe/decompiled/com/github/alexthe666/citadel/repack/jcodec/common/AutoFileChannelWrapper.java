package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.io.AutoPool;
import com.github.alexthe666.citadel.repack.jcodec.common.io.AutoResource;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class AutoFileChannelWrapper implements SeekableByteChannel, AutoResource {

    private static final long THRESHOLD = 5000L;

    private FileChannel ch;

    private File file;

    private long savedPos;

    private long curTime;

    private long accessTime;

    public AutoFileChannelWrapper(File file) throws IOException {
        this.file = file;
        this.curTime = System.currentTimeMillis();
        AutoPool.getInstance().add(this);
        this.ensureOpen();
    }

    private void ensureOpen() throws IOException {
        this.accessTime = this.curTime;
        if (this.ch == null || !this.ch.isOpen()) {
            this.ch = new FileInputStream(this.file).getChannel();
            this.ch.position(this.savedPos);
        }
    }

    public int read(ByteBuffer arg0) throws IOException {
        this.ensureOpen();
        int r = this.ch.read(arg0);
        this.savedPos = this.ch.position();
        return r;
    }

    public void close() throws IOException {
        if (this.ch != null && this.ch.isOpen()) {
            this.savedPos = this.ch.position();
            this.ch.close();
            this.ch = null;
        }
    }

    public boolean isOpen() {
        return this.ch != null && this.ch.isOpen();
    }

    public int write(ByteBuffer arg0) throws IOException {
        this.ensureOpen();
        int w = this.ch.write(arg0);
        this.savedPos = this.ch.position();
        return w;
    }

    @Override
    public long position() throws IOException {
        this.ensureOpen();
        return this.ch.position();
    }

    @Override
    public SeekableByteChannel setPosition(long newPosition) throws IOException {
        this.ensureOpen();
        this.ch.position(newPosition);
        this.savedPos = newPosition;
        return this;
    }

    @Override
    public long size() throws IOException {
        this.ensureOpen();
        return this.ch.size();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        this.ensureOpen();
        this.ch.truncate(size);
        this.savedPos = this.ch.position();
        return this;
    }

    @Override
    public void setCurTime(long curTime) {
        this.curTime = curTime;
        if (this.ch != null && this.ch.isOpen() && curTime - this.accessTime > 5000L) {
            try {
                this.close();
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
    }
}