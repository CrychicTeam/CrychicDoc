package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.TrackType;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.QTTimeUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AudioSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PCMMP4DemuxerTrack extends AbstractMP4DemuxerTrack {

    private int defaultSampleSize;

    private int posShift;

    protected int totalFrames;

    private SeekableByteChannel input;

    private MovieBox movie;

    public PCMMP4DemuxerTrack(MovieBox movie, TrakBox trak, SeekableByteChannel input) {
        super(trak);
        this.movie = movie;
        this.input = input;
        SampleSizesBox stsz = NodeBox.findFirstPath(trak, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        this.defaultSampleSize = stsz.getDefaultSize();
        int chunks = 0;
        for (int i = 1; i < this.sampleToChunks.length; i++) {
            int ch = (int) (this.sampleToChunks[i].getFirst() - this.sampleToChunks[i - 1].getFirst());
            this.totalFrames = this.totalFrames + ch * this.sampleToChunks[i - 1].getCount();
            chunks += ch;
        }
        this.totalFrames = this.totalFrames + this.sampleToChunks[this.sampleToChunks.length - 1].getCount() * (this.chunkOffsets.length - chunks);
    }

    @Override
    public Packet nextFrame() throws IOException {
        int frameSize = this.getFrameSize();
        int chSize = this.sampleToChunks[this.stscInd].getCount() * frameSize - this.posShift;
        return this.getNextFrame(ByteBuffer.allocate(chSize));
    }

    @Override
    public synchronized MP4Packet getNextFrame(ByteBuffer buffer) throws IOException {
        if (this.stcoInd >= this.chunkOffsets.length) {
            return null;
        } else {
            int frameSize = this.getFrameSize();
            int se = this.sampleToChunks[this.stscInd].getEntry();
            int chSize = this.sampleToChunks[this.stscInd].getCount() * frameSize;
            long pktOff = this.chunkOffsets[this.stcoInd] + (long) this.posShift;
            int pktSize = chSize - this.posShift;
            ByteBuffer result = this.readPacketData(this.input, buffer, pktOff, pktSize);
            long ptsRem = this.pts;
            int doneFrames = pktSize / frameSize;
            this.shiftPts((long) doneFrames);
            MP4Packet pkt = new MP4Packet(result, QTTimeUtil.mediaToEdited(this.box, ptsRem, this.movie.getTimescale()), this.timescale, (long) ((int) (this.pts - ptsRem)), this.curFrame, Packet.FrameType.KEY, null, 0, ptsRem, se - 1, pktOff, pktSize, true);
            this.curFrame += (long) doneFrames;
            this.posShift = 0;
            this.stcoInd++;
            if (this.stscInd < this.sampleToChunks.length - 1 && (long) (this.stcoInd + 1) == this.sampleToChunks[this.stscInd + 1].getFirst()) {
                this.stscInd++;
            }
            return pkt;
        }
    }

    @Override
    public boolean gotoSyncFrame(long frameNo) {
        return this.gotoFrame(frameNo);
    }

    public int getFrameSize() {
        SampleEntry entry = this.sampleEntries[this.sampleToChunks[this.stscInd].getEntry() - 1];
        return entry instanceof AudioSampleEntry && this.defaultSampleSize == 0 ? ((AudioSampleEntry) entry).calcFrameSize() : this.defaultSampleSize;
    }

    @Override
    protected void seekPointer(long frameNo) {
        this.stcoInd = 0;
        this.stscInd = 0;
        this.curFrame = 0L;
        while (true) {
            long nextFrame = this.curFrame + (long) this.sampleToChunks[this.stscInd].getCount();
            if (nextFrame > frameNo) {
                this.posShift = (int) ((frameNo - this.curFrame) * (long) this.getFrameSize());
                this.curFrame = frameNo;
                return;
            }
            this.curFrame = nextFrame;
            this.nextChunk();
        }
    }

    @Override
    public long getFrameCount() {
        return (long) this.totalFrames;
    }

    @Override
    public DemuxerTrackMeta getMeta() {
        AudioSampleEntry ase = (AudioSampleEntry) this.getSampleEntries()[0];
        AudioCodecMeta audioCodecMeta = AudioCodecMeta.fromAudioFormat(ase.getFormat());
        return new DemuxerTrackMeta(TrackType.AUDIO, Codec.codecByFourcc(this.getFourcc()), (double) this.duration / (double) this.timescale, null, this.totalFrames, null, null, audioCodecMeta);
    }
}