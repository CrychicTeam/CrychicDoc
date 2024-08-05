package icyllis.modernui.audio;

import icyllis.modernui.ModernUI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import javax.annotation.Nonnull;

public class WaveDecoder {

    public int mSampleRate;

    public float[] mSamples;

    public short[] mData;

    public WaveDecoder(@Nonnull FileChannel channel) throws Exception {
        ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0L, channel.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        if (buffer.getInt() != 1179011410) {
            throw new IllegalArgumentException("Not RIFF");
        } else {
            buffer.position(8);
            if (buffer.getInt() != 1163280727) {
                throw new IllegalArgumentException("Not WAVE");
            } else if (buffer.getInt() != 544501094) {
                throw new IllegalArgumentException("Not fmt chunk");
            } else {
                int chunk = buffer.getInt();
                if (chunk < 16) {
                    throw new IllegalArgumentException("Chunk size is invalid");
                } else {
                    chunk += 20;
                    short format = buffer.getShort();
                    if (format != 1) {
                        throw new IllegalArgumentException("Not PCM format");
                    } else {
                        short channels = buffer.getShort();
                        ModernUI.LOGGER.info("Channels: {}", channels);
                        int sampleRate = buffer.getInt();
                        ModernUI.LOGGER.info("Sample Rate: {}", sampleRate);
                        if (buffer.getInt() == sampleRate * channels << 1 && buffer.getShort() == channels << 1 && buffer.getShort() == 16) {
                            buffer.position(chunk);
                            if (buffer.getInt() != 1635017060) {
                                throw new IllegalArgumentException("Not data chunk");
                            } else {
                                float f = 3.0518044E-5F;
                                int dataSize = buffer.getInt();
                                short[] data = new short[dataSize >> 1];
                                float[] samples = new float[data.length / channels];
                                for (int i = 0; i < samples.length; i++) {
                                    float sample = 0.0F;
                                    for (int j = 0; j < channels; j++) {
                                        short v = buffer.getShort();
                                        data[i * channels + j] = v;
                                        sample = (float) ((double) sample + ((double) v + 0.5) * (double) f);
                                    }
                                    samples[i] = sample / (float) channels;
                                }
                                this.mSampleRate = sampleRate;
                                this.mSamples = samples;
                                this.mData = data;
                            }
                        } else {
                            throw new IllegalArgumentException("Not 16-bit sample");
                        }
                    }
                }
            }
        }
    }
}