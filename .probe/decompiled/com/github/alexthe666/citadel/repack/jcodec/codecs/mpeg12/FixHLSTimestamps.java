package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FixHLSTimestamps extends FixTimestamp {

    private long[] lastPts = new long[256];

    public static void main1(String[] args) throws IOException {
        String wildCard = args[0];
        int startIdx = Integer.parseInt(args[1]);
        new FixHLSTimestamps().doIt(wildCard, startIdx);
    }

    private void doIt(String wildCard, int startIdx) throws IOException {
        Arrays.fill(this.lastPts, -1L);
        int i = startIdx;
        while (true) {
            File file = new File(String.format(wildCard, i));
            System.out.println(file.getAbsolutePath());
            if (!file.exists()) {
                return;
            }
            this.fix(file);
            i++;
        }
    }

    @Override
    protected long doWithTimestamp(int streamId, long pts, boolean isPts) {
        if (!isPts) {
            return pts;
        } else if (this.lastPts[streamId] == -1L) {
            this.lastPts[streamId] = pts;
            return pts;
        } else if (this.isVideo(streamId)) {
            this.lastPts[streamId] = this.lastPts[streamId] + 3003L;
            return this.lastPts[streamId];
        } else if (this.isAudio(streamId)) {
            this.lastPts[streamId] = this.lastPts[streamId] + 1920L;
            return this.lastPts[streamId];
        } else {
            throw new RuntimeException("Unexpected!!!");
        }
    }
}