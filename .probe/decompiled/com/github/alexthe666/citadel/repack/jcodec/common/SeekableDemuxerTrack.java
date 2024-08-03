package com.github.alexthe666.citadel.repack.jcodec.common;

import java.io.IOException;

public interface SeekableDemuxerTrack extends DemuxerTrack {

    boolean gotoFrame(long var1) throws IOException;

    boolean gotoSyncFrame(long var1) throws IOException;

    long getCurFrame();

    void seek(double var1) throws IOException;
}