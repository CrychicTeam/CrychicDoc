package com.github.alexthe666.citadel.repack.jcodec.containers.imgseq;

import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.TrackType;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageSequenceDemuxer implements Demuxer, DemuxerTrack {

    private static final int VIDEO_FPS = 25;

    private String namePattern;

    private int frameNo;

    private Packet curFrame;

    private Codec codec;

    private int maxAvailableFrame;

    private int maxFrames;

    private String prevName;

    private static final int MAX_MAX = 5184000;

    public ImageSequenceDemuxer(String namePattern, int maxFrames) throws IOException {
        this.namePattern = namePattern;
        this.maxFrames = maxFrames;
        this.maxAvailableFrame = -1;
        this.curFrame = this.loadFrame();
        String lowerCase = namePattern.toLowerCase();
        if (lowerCase.endsWith(".png")) {
            this.codec = Codec.PNG;
        } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            this.codec = Codec.JPEG;
        }
    }

    public void close() throws IOException {
    }

    @Override
    public List<? extends DemuxerTrack> getTracks() {
        ArrayList<DemuxerTrack> tracks = new ArrayList();
        tracks.add(this);
        return tracks;
    }

    @Override
    public List<? extends DemuxerTrack> getVideoTracks() {
        return this.getTracks();
    }

    @Override
    public List<? extends DemuxerTrack> getAudioTracks() {
        return new ArrayList();
    }

    @Override
    public Packet nextFrame() throws IOException {
        Packet var1;
        try {
            var1 = this.curFrame;
        } finally {
            this.curFrame = this.loadFrame();
        }
        return var1;
    }

    private Packet loadFrame() throws IOException {
        if (this.frameNo > this.maxFrames) {
            return null;
        } else {
            File file = null;
            do {
                String name = String.format(this.namePattern, this.frameNo);
                if (name.equals(this.prevName)) {
                    return null;
                }
                this.prevName = name;
                file = new File(name);
                if (file.exists() || this.frameNo > 0) {
                    break;
                }
                this.frameNo++;
            } while (this.frameNo < 2);
            if (file != null && file.exists()) {
                Packet ret = new Packet(NIOUtils.fetchFromFile(file), (long) this.frameNo, 25, 1L, (long) this.frameNo, Packet.FrameType.KEY, null, this.frameNo);
                this.frameNo++;
                return ret;
            } else {
                return null;
            }
        }
    }

    public int getMaxAvailableFrame() {
        if (this.maxAvailableFrame == -1) {
            int firstPoint = 0;
            for (int i = 5184000; i > 0; i /= 2) {
                if (new File(String.format(this.namePattern, i)).exists()) {
                    firstPoint = i;
                    break;
                }
            }
            int pos = firstPoint;
            for (int interv = firstPoint / 2; interv > 1; interv /= 2) {
                if (new File(String.format(this.namePattern, pos + interv)).exists()) {
                    pos += interv;
                }
            }
            this.maxAvailableFrame = pos;
            Logger.info("Max frame found: " + this.maxAvailableFrame);
        }
        return Math.min(this.maxAvailableFrame, this.maxFrames);
    }

    @Override
    public DemuxerTrackMeta getMeta() {
        int durationFrames = this.getMaxAvailableFrame();
        return new DemuxerTrackMeta(TrackType.VIDEO, this.codec, (double) ((durationFrames + 1) * 25), null, durationFrames + 1, null, null, null);
    }
}