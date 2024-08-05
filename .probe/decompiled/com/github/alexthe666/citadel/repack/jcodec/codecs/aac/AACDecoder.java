package com.github.alexthe666.citadel.repack.jcodec.codecs.aac;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.Decoder;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleBuffer;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.AudioBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AACDecoder implements AudioDecoder {

    private Decoder decoder;

    public AACDecoder(ByteBuffer decoderSpecific) throws AACException {
        if (decoderSpecific.remaining() >= 7) {
            ADTSParser.Header header = ADTSParser.read(decoderSpecific);
            if (header != null) {
                decoderSpecific = ADTSParser.adtsToStreamInfo(header);
            }
            Logger.info("Creating AAC decoder from ADTS header.");
        }
        this.decoder = new Decoder(NIOUtils.toArray(decoderSpecific));
    }

    @Override
    public AudioBuffer decodeFrame(ByteBuffer frame, ByteBuffer dst) throws IOException {
        ADTSParser.read(frame);
        SampleBuffer sampleBuffer = new SampleBuffer();
        this.decoder.decodeFrame(NIOUtils.toArray(frame), sampleBuffer);
        if (sampleBuffer.isBigEndian()) {
            sampleBuffer.setBigEndian(false);
        }
        return new AudioBuffer(ByteBuffer.wrap(sampleBuffer.getData()), this.toAudioFormat(sampleBuffer), 0);
    }

    private AudioFormat toAudioFormat(SampleBuffer sampleBuffer) {
        return new AudioFormat(sampleBuffer.getSampleRate(), sampleBuffer.getBitsPerSample(), sampleBuffer.getChannels(), true, sampleBuffer.isBigEndian());
    }

    @Override
    public AudioCodecMeta getCodecMeta(ByteBuffer data) throws IOException {
        SampleBuffer sampleBuffer = new SampleBuffer();
        this.decoder.decodeFrame(NIOUtils.toArray(data), sampleBuffer);
        sampleBuffer.setBigEndian(false);
        return AudioCodecMeta.fromAudioFormat(this.toAudioFormat(sampleBuffer));
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        if (data.remaining() < 7) {
            return 0;
        } else {
            ADTSParser.Header header = ADTSParser.read(data);
            return header != null ? 100 : 0;
        }
    }
}