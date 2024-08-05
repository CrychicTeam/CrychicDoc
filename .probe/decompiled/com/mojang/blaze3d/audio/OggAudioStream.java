package com.mojang.blaze3d.audio;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.util.Mth;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class OggAudioStream implements AudioStream {

    private static final int EXPECTED_MAX_FRAME_SIZE = 8192;

    private long handle;

    private final AudioFormat audioFormat;

    private final InputStream input;

    private ByteBuffer buffer = MemoryUtil.memAlloc(8192);

    public OggAudioStream(InputStream inputStream0) throws IOException {
        this.input = inputStream0;
        this.buffer.limit(0);
        MemoryStack $$1 = MemoryStack.stackPush();
        try {
            IntBuffer $$2 = $$1.mallocInt(1);
            IntBuffer $$3 = $$1.mallocInt(1);
            while (this.handle == 0L) {
                if (!this.refillFromStream()) {
                    throw new IOException("Failed to find Ogg header");
                }
                int $$4 = this.buffer.position();
                this.buffer.position(0);
                this.handle = STBVorbis.stb_vorbis_open_pushdata(this.buffer, $$2, $$3, null);
                this.buffer.position($$4);
                int $$5 = $$3.get(0);
                if ($$5 == 1) {
                    this.forwardBuffer();
                } else if ($$5 != 0) {
                    throw new IOException("Failed to read Ogg file " + $$5);
                }
            }
            this.buffer.position(this.buffer.position() + $$2.get(0));
            STBVorbisInfo $$6 = STBVorbisInfo.mallocStack($$1);
            STBVorbis.stb_vorbis_get_info(this.handle, $$6);
            this.audioFormat = new AudioFormat((float) $$6.sample_rate(), 16, $$6.channels(), true, false);
        } catch (Throwable var8) {
            if ($$1 != null) {
                try {
                    $$1.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if ($$1 != null) {
            $$1.close();
        }
    }

    private boolean refillFromStream() throws IOException {
        int $$0 = this.buffer.limit();
        int $$1 = this.buffer.capacity() - $$0;
        if ($$1 == 0) {
            return true;
        } else {
            byte[] $$2 = new byte[$$1];
            int $$3 = this.input.read($$2);
            if ($$3 == -1) {
                return false;
            } else {
                int $$4 = this.buffer.position();
                this.buffer.limit($$0 + $$3);
                this.buffer.position($$0);
                this.buffer.put($$2, 0, $$3);
                this.buffer.position($$4);
                return true;
            }
        }
    }

    private void forwardBuffer() {
        boolean $$0 = this.buffer.position() == 0;
        boolean $$1 = this.buffer.position() == this.buffer.limit();
        if ($$1 && !$$0) {
            this.buffer.position(0);
            this.buffer.limit(0);
        } else {
            ByteBuffer $$2 = MemoryUtil.memAlloc($$0 ? 2 * this.buffer.capacity() : this.buffer.capacity());
            $$2.put(this.buffer);
            MemoryUtil.memFree(this.buffer);
            $$2.flip();
            this.buffer = $$2;
        }
    }

    private boolean readFrame(OggAudioStream.OutputConcat oggAudioStreamOutputConcat0) throws IOException {
        if (this.handle == 0L) {
            return false;
        } else {
            MemoryStack $$1 = MemoryStack.stackPush();
            int $$5;
            label79: {
                boolean var15;
                label80: {
                    try {
                        PointerBuffer $$2 = $$1.mallocPointer(1);
                        IntBuffer $$3 = $$1.mallocInt(1);
                        IntBuffer $$4 = $$1.mallocInt(1);
                        while (true) {
                            $$5 = STBVorbis.stb_vorbis_decode_frame_pushdata(this.handle, this.buffer, $$3, $$2, $$4);
                            this.buffer.position(this.buffer.position() + $$5);
                            int $$6 = STBVorbis.stb_vorbis_get_error(this.handle);
                            if ($$6 == 1) {
                                this.forwardBuffer();
                                if (!this.refillFromStream()) {
                                    $$5 = 0;
                                    break label79;
                                }
                            } else {
                                if ($$6 != 0) {
                                    throw new IOException("Failed to read Ogg file " + $$6);
                                }
                                int $$7 = $$4.get(0);
                                if ($$7 != 0) {
                                    int $$8 = $$3.get(0);
                                    PointerBuffer $$9 = $$2.getPointerBuffer($$8);
                                    if ($$8 == 1) {
                                        this.convertMono($$9.getFloatBuffer(0, $$7), oggAudioStreamOutputConcat0);
                                        var15 = true;
                                        break label80;
                                    }
                                    if ($$8 != 2) {
                                        throw new IllegalStateException("Invalid number of channels: " + $$8);
                                    }
                                    this.convertStereo($$9.getFloatBuffer(0, $$7), $$9.getFloatBuffer(1, $$7), oggAudioStreamOutputConcat0);
                                    var15 = true;
                                    break;
                                }
                            }
                        }
                    } catch (Throwable var13) {
                        if ($$1 != null) {
                            try {
                                $$1.close();
                            } catch (Throwable var12) {
                                var13.addSuppressed(var12);
                            }
                        }
                        throw var13;
                    }
                    if ($$1 != null) {
                        $$1.close();
                    }
                    return var15;
                }
                if ($$1 != null) {
                    $$1.close();
                }
                return var15;
            }
            if ($$1 != null) {
                $$1.close();
            }
            return (boolean) $$5;
        }
    }

    private void convertMono(FloatBuffer floatBuffer0, OggAudioStream.OutputConcat oggAudioStreamOutputConcat1) {
        while (floatBuffer0.hasRemaining()) {
            oggAudioStreamOutputConcat1.put(floatBuffer0.get());
        }
    }

    private void convertStereo(FloatBuffer floatBuffer0, FloatBuffer floatBuffer1, OggAudioStream.OutputConcat oggAudioStreamOutputConcat2) {
        while (floatBuffer0.hasRemaining() && floatBuffer1.hasRemaining()) {
            oggAudioStreamOutputConcat2.put(floatBuffer0.get());
            oggAudioStreamOutputConcat2.put(floatBuffer1.get());
        }
    }

    public void close() throws IOException {
        if (this.handle != 0L) {
            STBVorbis.stb_vorbis_close(this.handle);
            this.handle = 0L;
        }
        MemoryUtil.memFree(this.buffer);
        this.input.close();
    }

    @Override
    public AudioFormat getFormat() {
        return this.audioFormat;
    }

    @Override
    public ByteBuffer read(int int0) throws IOException {
        OggAudioStream.OutputConcat $$1 = new OggAudioStream.OutputConcat(int0 + 8192);
        while (this.readFrame($$1) && $$1.byteCount < int0) {
        }
        return $$1.get();
    }

    public ByteBuffer readAll() throws IOException {
        OggAudioStream.OutputConcat $$0 = new OggAudioStream.OutputConcat(16384);
        while (this.readFrame($$0)) {
        }
        return $$0.get();
    }

    static class OutputConcat {

        private final List<ByteBuffer> buffers = Lists.newArrayList();

        private final int bufferSize;

        int byteCount;

        private ByteBuffer currentBuffer;

        public OutputConcat(int int0) {
            this.bufferSize = int0 + 1 & -2;
            this.createNewBuffer();
        }

        private void createNewBuffer() {
            this.currentBuffer = BufferUtils.createByteBuffer(this.bufferSize);
        }

        public void put(float float0) {
            if (this.currentBuffer.remaining() == 0) {
                this.currentBuffer.flip();
                this.buffers.add(this.currentBuffer);
                this.createNewBuffer();
            }
            int $$1 = Mth.clamp((int) (float0 * 32767.5F - 0.5F), -32768, 32767);
            this.currentBuffer.putShort((short) $$1);
            this.byteCount += 2;
        }

        public ByteBuffer get() {
            this.currentBuffer.flip();
            if (this.buffers.isEmpty()) {
                return this.currentBuffer;
            } else {
                ByteBuffer $$0 = BufferUtils.createByteBuffer(this.byteCount);
                this.buffers.forEach($$0::put);
                $$0.put(this.currentBuffer);
                $$0.flip();
                return $$0;
            }
        }
    }
}