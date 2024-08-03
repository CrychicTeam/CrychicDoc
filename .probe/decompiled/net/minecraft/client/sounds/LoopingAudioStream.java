package net.minecraft.client.sounds;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;

public class LoopingAudioStream implements AudioStream {

    private final LoopingAudioStream.AudioStreamProvider provider;

    private AudioStream stream;

    private final BufferedInputStream bufferedInputStream;

    public LoopingAudioStream(LoopingAudioStream.AudioStreamProvider loopingAudioStreamAudioStreamProvider0, InputStream inputStream1) throws IOException {
        this.provider = loopingAudioStreamAudioStreamProvider0;
        this.bufferedInputStream = new BufferedInputStream(inputStream1);
        this.bufferedInputStream.mark(Integer.MAX_VALUE);
        this.stream = loopingAudioStreamAudioStreamProvider0.create(new LoopingAudioStream.NoCloseBuffer(this.bufferedInputStream));
    }

    @Override
    public AudioFormat getFormat() {
        return this.stream.getFormat();
    }

    @Override
    public ByteBuffer read(int int0) throws IOException {
        ByteBuffer $$1 = this.stream.read(int0);
        if (!$$1.hasRemaining()) {
            this.stream.close();
            this.bufferedInputStream.reset();
            this.stream = this.provider.create(new LoopingAudioStream.NoCloseBuffer(this.bufferedInputStream));
            $$1 = this.stream.read(int0);
        }
        return $$1;
    }

    public void close() throws IOException {
        this.stream.close();
        this.bufferedInputStream.close();
    }

    @FunctionalInterface
    public interface AudioStreamProvider {

        AudioStream create(InputStream var1) throws IOException;
    }

    static class NoCloseBuffer extends FilterInputStream {

        NoCloseBuffer(InputStream inputStream0) {
            super(inputStream0);
        }

        public void close() {
        }
    }
}