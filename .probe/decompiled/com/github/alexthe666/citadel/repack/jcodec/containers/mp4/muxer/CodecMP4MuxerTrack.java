package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.ADTSParser;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.mp4.EsdsBox;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AudioSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.PixelAspectExt;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodecMP4MuxerTrack extends MP4MuxerTrack {

    private static Map<Codec, String> codec2fourcc = new HashMap();

    private Codec codec;

    private List<ByteBuffer> spsList;

    private List<ByteBuffer> ppsList;

    private ADTSParser.Header adtsHeader;

    public CodecMP4MuxerTrack(int trackId, MP4TrackType type, Codec codec) {
        super(trackId, type);
        this.codec = codec;
        this.spsList = new ArrayList();
        this.ppsList = new ArrayList();
    }

    @Override
    public void addFrame(Packet pkt) throws IOException {
        if (this.codec == Codec.H264) {
            ByteBuffer result = pkt.getData();
            if (pkt.frameType == Packet.FrameType.UNKNOWN) {
                pkt.setFrameType(H264Utils.isByteBufferIDRSlice(result) ? Packet.FrameType.KEY : Packet.FrameType.INTER);
            }
            H264Utils.wipePSinplace(result, this.spsList, this.ppsList);
            result = H264Utils.encodeMOVPacket(result);
            pkt = Packet.createPacketWithData(pkt, result);
        } else if (this.codec == Codec.AAC) {
            ByteBuffer result = pkt.getData();
            this.adtsHeader = ADTSParser.read(result);
            pkt = Packet.createPacketWithData(pkt, result);
        }
        super.addFrame(pkt);
    }

    @Override
    public void addFrameInternal(Packet pkt, int entryNo) throws IOException {
        Preconditions.checkState(!this.finished, "The muxer track has finished muxing");
        if (this._timescale == -1) {
            if (this.adtsHeader != null) {
                this._timescale = this.adtsHeader.getSampleRate();
            } else {
                this._timescale = pkt.getTimescale();
            }
        }
        if (this._timescale != pkt.getTimescale()) {
            pkt.setPts(pkt.getPts() * (long) this._timescale / (long) pkt.getTimescale());
            pkt.setDuration(pkt.getPts() * (long) this._timescale / pkt.getDuration());
        }
        if (this.adtsHeader != null) {
            pkt.setDuration(1024L);
        }
        super.addFrameInternal(pkt, entryNo);
    }

    @Override
    protected Box finish(MovieHeaderBox mvhd) throws IOException {
        Preconditions.checkState(!this.finished, "The muxer track has finished muxing");
        if (this.getEntries().isEmpty()) {
            if (this.codec == Codec.H264 && !this.spsList.isEmpty()) {
                SeqParameterSet sps = SeqParameterSet.read(((ByteBuffer) this.spsList.get(0)).duplicate());
                Size size = H264Utils.getPicSize(sps);
                VideoCodecMeta meta = VideoCodecMeta.createSimpleVideoCodecMeta(size, ColorSpace.YUV420);
                this.addVideoSampleEntry(meta);
            } else {
                Logger.warn("CodecMP4MuxerTrack: Creating a track without sample entry");
            }
        }
        this.setCodecPrivateIfNeeded();
        return super.finish(mvhd);
    }

    void addVideoSampleEntry(VideoCodecMeta meta) {
        SampleEntry se = VideoSampleEntry.videoSampleEntry((String) codec2fourcc.get(this.codec), meta.getSize(), "JCodec");
        if (meta.getPixelAspectRatio() != null) {
            se.add(PixelAspectExt.createPixelAspectExt(meta.getPixelAspectRatio()));
        }
        this.addSampleEntry(se);
    }

    private static List<ByteBuffer> selectUnique(List<ByteBuffer> bblist) {
        Set<CodecMP4MuxerTrack.ByteArrayWrapper> all = new HashSet();
        for (ByteBuffer byteBuffer : bblist) {
            all.add(new CodecMP4MuxerTrack.ByteArrayWrapper(byteBuffer));
        }
        List<ByteBuffer> result = new ArrayList();
        for (CodecMP4MuxerTrack.ByteArrayWrapper bs : all) {
            result.add(bs.get());
        }
        return result;
    }

    public void setCodecPrivateIfNeeded() {
        if (this.codec == Codec.H264) {
            List<ByteBuffer> sps = selectUnique(this.spsList);
            List<ByteBuffer> pps = selectUnique(this.ppsList);
            if (!sps.isEmpty() && !pps.isEmpty()) {
                ((SampleEntry) this.getEntries().get(0)).add(H264Utils.createAvcCFromPS(sps, pps, 4));
            } else {
                Logger.warn("CodecMP4MuxerTrack: Not adding a sample entry for h.264 track, missing any SPS/PPS NAL units");
            }
        } else if (this.codec == Codec.AAC) {
            if (this.adtsHeader != null) {
                ((SampleEntry) this.getEntries().get(0)).add(EsdsBox.fromADTS(this.adtsHeader));
            } else {
                Logger.warn("CodecMP4MuxerTrack: Not adding a sample entry for AAC track, missing any ADTS headers.");
            }
        }
    }

    void addAudioSampleEntry(AudioFormat format) {
        AudioSampleEntry ase = AudioSampleEntry.compressedAudioSampleEntry((String) codec2fourcc.get(this.codec), 1, 16, format.getChannels(), format.getSampleRate(), 0, 0, 0);
        this.addSampleEntry(ase);
    }

    static {
        codec2fourcc.put(Codec.MP1, ".mp1");
        codec2fourcc.put(Codec.MP2, ".mp2");
        codec2fourcc.put(Codec.MP3, ".mp3");
        codec2fourcc.put(Codec.H264, "avc1");
        codec2fourcc.put(Codec.AAC, "mp4a");
        codec2fourcc.put(Codec.PRORES, "apch");
        codec2fourcc.put(Codec.JPEG, "mjpg");
        codec2fourcc.put(Codec.PNG, "png ");
        codec2fourcc.put(Codec.V210, "v210");
    }

    private static class ByteArrayWrapper {

        private byte[] bytes;

        public ByteArrayWrapper(ByteBuffer bytes) {
            this.bytes = NIOUtils.toArray(bytes);
        }

        public ByteBuffer get() {
            return ByteBuffer.wrap(this.bytes);
        }

        public boolean equals(Object obj) {
            return !(obj instanceof CodecMP4MuxerTrack.ByteArrayWrapper) ? false : Platform.arrayEqualsByte(this.bytes, ((CodecMP4MuxerTrack.ByteArrayWrapper) obj).bytes);
        }

        public int hashCode() {
            return Arrays.hashCode(this.bytes);
        }
    }
}