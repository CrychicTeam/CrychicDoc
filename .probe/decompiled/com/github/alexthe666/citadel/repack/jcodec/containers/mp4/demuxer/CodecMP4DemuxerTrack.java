package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.AACUtils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.ADTSParser;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.mp4.AvcCBox;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoSampleEntry;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CodecMP4DemuxerTrack extends MP4DemuxerTrack {

    private ByteBuffer codecPrivate;

    private AvcCBox avcC;

    public CodecMP4DemuxerTrack(MovieBox mov, TrakBox trak, SeekableByteChannel input) {
        super(mov, trak, input);
        if (Codec.codecByFourcc(this.getFourcc()) == Codec.H264) {
            this.avcC = H264Utils.parseAVCC((VideoSampleEntry) this.getSampleEntries()[0]);
        }
        this.codecPrivate = MP4DemuxerTrackMeta.getCodecPrivate(this);
    }

    @Override
    public ByteBuffer convertPacket(ByteBuffer result) {
        if (this.codecPrivate != null) {
            if (Codec.codecByFourcc(this.getFourcc()) == Codec.H264) {
                ByteBuffer annexbCoded = H264Utils.decodeMOVPacket(result, this.avcC);
                if (H264Utils.isByteBufferIDRSlice(annexbCoded)) {
                    return NIOUtils.combineBuffers(Arrays.asList(this.codecPrivate, annexbCoded));
                }
                return annexbCoded;
            }
            if (Codec.codecByFourcc(this.getFourcc()) == Codec.AAC) {
                ADTSParser.Header adts = AACUtils.streamInfoToADTS(this.codecPrivate, true, 1, result.remaining());
                ByteBuffer adtsRaw = ByteBuffer.allocate(7);
                ADTSParser.write(adts, adtsRaw);
                return NIOUtils.combineBuffers(Arrays.asList(adtsRaw, result));
            }
        }
        return result;
    }
}