package icyllis.modernui.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.annotation.Nonnull;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class VorbisPullDecoder extends SoundSample {

    private ByteBuffer mPayload;

    private long mHandle;

    public VorbisPullDecoder(@Nonnull ByteBuffer nativeEncodedAudioBuffer) {
        this.mPayload = nativeEncodedAudioBuffer;
        MemoryStack stack = MemoryStack.stackPush();
        try {
            IntBuffer error = stack.mallocInt(1);
            this.mHandle = STBVorbis.stb_vorbis_open_memory(nativeEncodedAudioBuffer, error, null);
            int er = error.get(0);
            if (er != 0) {
                throw new IllegalStateException("Failed to open Vorbis file " + er);
            }
            if (this.mHandle == 0L) {
                throw new AssertionError();
            }
            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
            STBVorbis.stb_vorbis_get_info(this.mHandle, info);
            this.mSampleRate = info.sample_rate();
            int channels = info.channels();
            if (channels != 1 && channels != 2) {
                throw new IllegalStateException("Not 1 or 2 channels but " + channels);
            }
            this.mChannels = channels;
        } catch (Throwable var8) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if (stack != null) {
            stack.close();
        }
        this.mTotalSamples = STBVorbis.stb_vorbis_stream_length_in_samples(this.mHandle);
    }

    @Override
    public boolean seek(int sampleOffset) {
        return STBVorbis.stb_vorbis_seek(this.mHandle, sampleOffset);
    }

    @Override
    public int getSamplesShortInterleaved(ShortBuffer pcmBuffer) {
        return STBVorbis.stb_vorbis_get_samples_short_interleaved(this.mHandle, this.mChannels, pcmBuffer);
    }

    @Override
    public void close() {
        MemoryUtil.memFree(this.mPayload);
        this.mPayload = null;
        if (this.mHandle != 0L) {
            STBVorbis.stb_vorbis_close(this.mHandle);
            this.mHandle = 0L;
        }
    }
}