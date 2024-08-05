package com.github.alexthe666.citadel.repack.jaad.mp4.od;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class SLConfigDescriptor extends Descriptor {

    private boolean useAccessUnitStart;

    private boolean useAccessUnitEnd;

    private boolean useRandomAccessPoint;

    private boolean usePadding;

    private boolean useTimeStamp;

    private boolean useWallClockTimeStamp;

    private boolean useIdle;

    private boolean duration;

    private long timeStampResolution;

    private long ocrResolution;

    private int timeStampLength;

    private int ocrLength;

    private int instantBitrateLength;

    private int degradationPriorityLength;

    private int seqNumberLength;

    private long timeScale;

    private int accessUnitDuration;

    private int compositionUnitDuration;

    private long wallClockTimeStamp;

    private long startDecodingTimeStamp;

    private long startCompositionTimeStamp;

    private boolean ocrStream;

    private int ocrES_ID;

    @Override
    void decode(MP4InputStream in) throws IOException {
        boolean predefined = in.read() == 1;
        if (!predefined) {
            int tmp = in.read();
            this.useAccessUnitStart = (tmp >> 7 & 1) == 1;
            this.useAccessUnitEnd = (tmp >> 6 & 1) == 1;
            this.useRandomAccessPoint = (tmp >> 5 & 1) == 1;
            this.usePadding = (tmp >> 4 & 1) == 1;
            this.useTimeStamp = (tmp >> 3 & 1) == 1;
            this.useWallClockTimeStamp = (tmp >> 2 & 1) == 1;
            this.useIdle = (tmp >> 1 & 1) == 1;
            this.duration = (tmp & 1) == 1;
            this.timeStampResolution = in.readBytes(4);
            this.ocrResolution = in.readBytes(4);
            this.timeStampLength = in.read();
            this.ocrLength = in.read();
            this.instantBitrateLength = in.read();
            tmp = in.read();
            this.degradationPriorityLength = tmp >> 4 & 15;
            this.seqNumberLength = tmp & 15;
            if (this.duration) {
                this.timeScale = in.readBytes(4);
                this.accessUnitDuration = (int) in.readBytes(2);
                this.compositionUnitDuration = (int) in.readBytes(2);
            }
            if (!this.useTimeStamp) {
                if (this.useWallClockTimeStamp) {
                    this.wallClockTimeStamp = in.readBytes(4);
                }
                tmp = (int) Math.ceil((double) (2 * this.timeStampLength) / 8.0);
                long tmp2 = in.readBytes(tmp);
                long mask = (long) ((1 << this.timeStampLength) - 1);
                this.startDecodingTimeStamp = tmp2 >> this.timeStampLength & mask;
                this.startCompositionTimeStamp = tmp2 & mask;
            }
        }
        int tmpx = in.read();
        this.ocrStream = (tmpx >> 7 & 1) == 1;
        if (this.ocrStream) {
            this.ocrES_ID = (int) in.readBytes(2);
        }
    }
}