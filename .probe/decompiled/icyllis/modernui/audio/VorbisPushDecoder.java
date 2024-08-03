package icyllis.modernui.audio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class VorbisPushDecoder extends SoundStream {

    private final FileChannel mChannel;

    private ByteBuffer mBuffer;

    private int mTotalSamples;

    private long mHandle;

    public VorbisPushDecoder(@Nonnull FileChannel channel) throws IOException {
        this.mChannel = channel;
        this.mBuffer = MemoryUtil.memAlloc(4096).flip();
        MemoryStack stack = MemoryStack.stackPush();
        try {
            label70: {
                IntBuffer consumed = stack.mallocInt(1);
                IntBuffer error = stack.mallocInt(1);
                while (!this.read()) {
                    long handle = STBVorbis.stb_vorbis_open_pushdata(this.mBuffer, consumed, error, null);
                    int er = error.get(0);
                    if (er == 1) {
                        this.forward();
                    } else if (er != 0) {
                        throw new IOException("Failed to open Vorbis file " + er);
                    }
                    if (handle != 0L) {
                        this.mHandle = handle;
                        er = consumed.get(0);
                        this.mBuffer.position(this.mBuffer.position() + er);
                        STBVorbisInfo info = STBVorbisInfo.malloc(stack);
                        STBVorbis.stb_vorbis_get_info(handle, info);
                        this.mSampleRate = info.sample_rate();
                        int channels = info.channels();
                        if (channels != 1 && channels != 2) {
                            throw new IOException("Not 1 or 2 channels but " + channels);
                        }
                        this.mChannels = channels;
                        ByteBuffer temp = stack.malloc(14);
                        temp.order(ByteOrder.LITTLE_ENDIAN);
                        long size = channel.size();
                        for (int i = 1 + temp.capacity(); i <= 16384; i++) {
                            channel.read(temp, size - (long) i);
                            if (temp.getInt(0) == 1399285583) {
                                this.mTotalSamples = temp.getInt(6);
                                break label70;
                            }
                            temp.clear();
                        }
                        break label70;
                    }
                }
                throw new IOException("No header found");
            }
        } catch (Throwable var15) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var14) {
                    var15.addSuppressed(var14);
                }
            }
            throw var15;
        }
        if (stack != null) {
            stack.close();
        }
    }

    public int getTotalSamples() {
        return this.mTotalSamples;
    }

    private boolean read() throws IOException {
        ByteBuffer buffer = this.mBuffer;
        int limit = buffer.limit();
        int require = buffer.capacity() - limit;
        if (require > 0) {
            int pos = buffer.position();
            buffer.position(limit);
            buffer.limit(limit + require);
            int read = this.mChannel.read(buffer);
            if (read == -1) {
                return true;
            }
            buffer.position(pos);
            buffer.limit(limit + read);
        }
        return false;
    }

    private void forward() {
        int pos = this.mBuffer.position();
        if (pos > 0 && pos == this.mBuffer.limit()) {
            this.mBuffer.rewind().flip();
        } else {
            ByteBuffer buffer = MemoryUtil.memAlloc(this.mBuffer.capacity() << (pos == 0 ? 1 : 0));
            buffer.put(this.mBuffer);
            MemoryUtil.memFree(this.mBuffer);
            this.mBuffer = buffer.flip();
        }
    }

    @Nullable
    @Override
    public FloatBuffer decodeFrame(@Nullable FloatBuffer output) throws IOException {
        MemoryStack stack = MemoryStack.stackPush();
        Object var12;
        label105: {
            FloatBuffer src;
            try {
                PointerBuffer samples = stack.mallocPointer(1);
                IntBuffer count = stack.mallocInt(1);
                while (true) {
                    int n = STBVorbis.stb_vorbis_decode_frame_pushdata(this.mHandle, this.mBuffer, null, samples, count);
                    this.mBuffer.position(this.mBuffer.position() + n);
                    if (n == 0) {
                        this.forward();
                        if (this.read()) {
                            var12 = null;
                            break label105;
                        }
                    } else if ((n = count.get(0)) > 0) {
                        this.mSampleOffset = STBVorbis.stb_vorbis_get_sample_offset(this.mHandle);
                        PointerBuffer data = samples.getPointerBuffer(this.mChannels);
                        if (this.mChannels == 1) {
                            src = data.getFloatBuffer(0, n);
                            while (src.hasRemaining()) {
                                if (output == null || !output.hasRemaining()) {
                                    output = MemoryUtil.memRealloc(output, output == null ? 256 : output.capacity() + 256);
                                }
                                output.put(src.get());
                            }
                        } else {
                            if (this.mChannels != 2) {
                                throw new IllegalStateException();
                            }
                            src = data.getFloatBuffer(0, n);
                            FloatBuffer srcL = data.getFloatBuffer(1, n);
                            while (src.hasRemaining()) {
                                if (output == null || output.remaining() < 2) {
                                    output = MemoryUtil.memRealloc(output, output == null ? 256 : output.capacity() + 256);
                                }
                                output.put(src.get()).put(srcL.get());
                            }
                        }
                        src = output;
                        break;
                    }
                }
            } catch (Throwable var10) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }
                throw var10;
            }
            if (stack != null) {
                stack.close();
            }
            return src;
        }
        if (stack != null) {
            stack.close();
        }
        return (FloatBuffer) var12;
    }

    @Override
    public void close() throws IOException {
        if (this.mHandle != 0L) {
            STBVorbis.stb_vorbis_close(this.mHandle);
            this.mHandle = 0L;
        }
        MemoryUtil.memFree(this.mBuffer);
        this.mBuffer = null;
        this.mChannel.close();
    }
}