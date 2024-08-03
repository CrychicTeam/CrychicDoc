package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import java.io.File;
import java.io.IOException;

public class SetBaseTimestamp extends FixTimestamp {

    private int baseTs;

    private long firstPts = -1L;

    private boolean video;

    public SetBaseTimestamp(boolean video, int baseTs) {
        this.video = video;
        this.baseTs = baseTs;
    }

    public static void main1(String[] args) throws IOException {
        File file = new File(args[0]);
        new SetBaseTimestamp("video".equalsIgnoreCase(args[1]), Integer.parseInt(args[2])).fix(file);
    }

    @Override
    protected long doWithTimestamp(int streamId, long pts, boolean isPts) {
        if (this.video && this.isVideo(streamId) || !this.video && this.isAudio(streamId)) {
            if (this.firstPts == -1L) {
                this.firstPts = pts;
            }
            return pts - this.firstPts + (long) this.baseTs;
        } else {
            return pts;
        }
    }
}