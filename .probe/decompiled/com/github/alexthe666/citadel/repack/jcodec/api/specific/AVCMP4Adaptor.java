package com.github.alexthe666.citadel.repack.jcodec.api.specific;

import com.github.alexthe666.citadel.repack.jcodec.api.MediaInfo;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Decoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnitType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Packet;
import java.nio.ByteBuffer;

public class AVCMP4Adaptor implements ContainerAdaptor {

    private H264Decoder decoder;

    private int curENo;

    private Size size;

    private DemuxerTrackMeta meta;

    public AVCMP4Adaptor(DemuxerTrackMeta meta) {
        this.meta = meta;
        this.curENo = -1;
        this.calcBufferSize();
    }

    private void calcBufferSize() {
        int w = Integer.MIN_VALUE;
        int h = Integer.MIN_VALUE;
        ByteBuffer bb = this.meta.getCodecPrivate().duplicate();
        ByteBuffer b;
        while ((b = H264Utils.nextNALUnit(bb)) != null) {
            NALUnit nu = NALUnit.read(b);
            if (nu.type == NALUnitType.SPS) {
                SeqParameterSet sps = H264Utils.readSPS(b);
                int ww = sps.picWidthInMbsMinus1 + 1;
                if (ww > w) {
                    w = ww;
                }
                int hh = SeqParameterSet.getPicHeightInMbs(sps);
                if (hh > h) {
                    h = hh;
                }
            }
        }
        this.size = new Size(w << 4, h << 4);
    }

    @Override
    public Picture decodeFrame(Packet packet, byte[][] data) {
        this.updateState(packet);
        Picture pic = this.decoder.decodeFrame(packet.getData(), data);
        Rational pasp = this.meta.getVideoCodecMeta().getPixelAspectRatio();
        if (pasp != null) {
        }
        return pic;
    }

    private void updateState(Packet packet) {
        int eNo = ((MP4Packet) packet).getEntryNo();
        if (eNo != this.curENo) {
            this.curENo = eNo;
        }
        if (this.decoder == null) {
            this.decoder = H264Decoder.createH264DecoderFromCodecPrivate(this.meta.getCodecPrivate());
        }
    }

    @Override
    public boolean canSeek(Packet pkt) {
        this.updateState(pkt);
        return H264Utils.idrSlice(H264Utils.splitFrame(pkt.getData()));
    }

    @Override
    public byte[][] allocatePicture() {
        return Picture.create(this.size.getWidth(), this.size.getHeight(), ColorSpace.YUV444).getData();
    }

    @Override
    public MediaInfo getMediaInfo() {
        return new MediaInfo(this.size);
    }
}