package com.github.alexthe666.citadel.repack.jaad.spi.javasound;

import com.github.alexthe666.citadel.repack.jaad.aac.Decoder;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleBuffer;
import com.github.alexthe666.citadel.repack.jaad.adts.ADTSDemultiplexer;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;

class AACAudioInputStream extends AsynchronousAudioInputStream {

    private final ADTSDemultiplexer adts;

    private final Decoder decoder;

    private final SampleBuffer sampleBuffer;

    private AudioFormat audioFormat = null;

    private byte[] saved;

    AACAudioInputStream(InputStream in, AudioFormat format, long length) throws IOException {
        super(in, format, length);
        this.adts = new ADTSDemultiplexer(in);
        this.decoder = new Decoder(this.adts.getDecoderSpecificInfo());
        this.sampleBuffer = new SampleBuffer();
    }

    public AudioFormat getFormat() {
        if (this.audioFormat == null) {
            try {
                this.decoder.decodeFrame(this.adts.readNextFrame(), this.sampleBuffer);
                this.audioFormat = new AudioFormat((float) this.sampleBuffer.getSampleRate(), this.sampleBuffer.getBitsPerSample(), this.sampleBuffer.getChannels(), true, true);
                this.saved = this.sampleBuffer.getData();
            } catch (IOException var2) {
                return null;
            }
        }
        return this.audioFormat;
    }

    @Override
    public void execute() {
        try {
            if (this.saved == null) {
                this.decoder.decodeFrame(this.adts.readNextFrame(), this.sampleBuffer);
                this.buffer.write(this.sampleBuffer.getData());
            } else {
                this.buffer.write(this.saved);
                this.saved = null;
            }
        } catch (IOException var2) {
            this.buffer.close();
        }
    }
}