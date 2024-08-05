package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;

public class VideoFrameWithPacket implements Comparable<VideoFrameWithPacket> {

    private Packet packet;

    private PixelStore.LoanerPicture frame;

    public VideoFrameWithPacket(Packet inFrame, PixelStore.LoanerPicture dec2) {
        this.packet = inFrame;
        this.frame = dec2;
    }

    public int compareTo(VideoFrameWithPacket arg) {
        if (arg == null) {
            return -1;
        } else {
            long pts1 = this.packet.getPts();
            long pts2 = arg.packet.getPts();
            return pts1 > pts2 ? 1 : (pts1 == pts2 ? 0 : -1);
        }
    }

    public Packet getPacket() {
        return this.packet;
    }

    public PixelStore.LoanerPicture getFrame() {
        return this.frame;
    }
}