package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.api.transcode.filters.ColorTransformFilter;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Transcoder {

    static final int REORDER_BUFFER_SIZE = 7;

    private Source[] sources;

    private Sink[] sinks;

    private List<Filter>[] extraFilters;

    private int[] seekFrames;

    private int[] maxFrames;

    private Transcoder.Mapping[] videoMappings;

    private Transcoder.Mapping[] audioMappings;

    private Transcoder(Source[] source, Sink[] sink, Transcoder.Mapping[] videoMappings, Transcoder.Mapping[] audioMappings, List<Filter>[] extraFilters, int[] seekFrames, int[] maxFrames) {
        this.extraFilters = extraFilters;
        this.videoMappings = videoMappings;
        this.audioMappings = audioMappings;
        this.seekFrames = seekFrames;
        this.maxFrames = maxFrames;
        this.sources = source;
        this.sinks = sink;
    }

    // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public void transcode() throws IOException {
        PixelStore pixelStore = new PixelStoreImpl();
        List<Transcoder.Stream>[] videoStreams = new List[this.sources.length];
        List<Transcoder.Stream>[] audioStreams = new List[this.sources.length];
        boolean[] decodeVideo = new boolean[this.sources.length];
        boolean[] decodeAudio = new boolean[this.sources.length];
        boolean[] finishedVideo = new boolean[this.sources.length];
        boolean[] finishedAudio = new boolean[this.sources.length];
        Transcoder.Stream[] allStreams = new Transcoder.Stream[this.sinks.length];
        int[] videoFramesRead = new int[this.sources.length];
        for (int s = 0; s < this.sources.length; s++) {
            videoStreams[s] = new ArrayList();
            audioStreams[s] = new ArrayList();
        }
        for (int i = 0; i < this.sinks.length; i++) {
            this.sinks[i].init();
        }
        for (int i = 0; i < this.sources.length; i++) {
            this.sources[i].init(pixelStore);
            this.sources[i].seekFrames(this.seekFrames[i]);
        }
        for (int s = 0; s < this.sinks.length; s++) {
            Transcoder.Stream stream = new Transcoder.Stream(this.sinks[s], this.videoMappings[s].copy, this.audioMappings[s].copy, this.extraFilters[s], pixelStore);
            allStreams[s] = stream;
            if (this.sources[this.videoMappings[s].source].isVideo()) {
                videoStreams[this.videoMappings[s].source].add(stream);
                if (!this.videoMappings[s].copy) {
                    decodeVideo[this.videoMappings[s].source] = true;
                }
            } else {
                finishedVideo[this.videoMappings[s].source] = true;
            }
            if (this.sources[this.audioMappings[s].source].isAudio()) {
                audioStreams[this.audioMappings[s].source].add(stream);
                if (!this.audioMappings[s].copy) {
                    decodeAudio[this.audioMappings[s].source] = true;
                }
            } else {
                finishedAudio[this.audioMappings[s].source] = true;
            }
        }
        while (true) {
            boolean var20 = false;
            try {
                var20 = true;
                for (int var25 = 0; var25 < this.sources.length; var25++) {
                    Source source = this.sources[var25];
                    boolean needsVideoFrame = !finishedVideo[var25];
                    for (Transcoder.Stream streamx : videoStreams[var25]) {
                        needsVideoFrame &= streamx.needsVideoFrame() || streamx.hasLeadingAudio() || finishedAudio[var25];
                    }
                    if (needsVideoFrame) {
                        VideoFrameWithPacket nextVideoFrame;
                        if (videoFramesRead[var25] >= this.maxFrames[var25]) {
                            nextVideoFrame = null;
                            finishedVideo[var25] = true;
                        } else if (!decodeVideo[var25] && source instanceof PacketSource) {
                            Packet packet = ((PacketSource) source).inputVideoPacket();
                            if (packet == null) {
                                finishedVideo[var25] = true;
                            } else {
                                videoFramesRead[var25]++;
                            }
                            nextVideoFrame = new VideoFrameWithPacket(packet, null);
                        } else {
                            nextVideoFrame = source.getNextVideoFrame();
                            if (nextVideoFrame == null) {
                                finishedVideo[var25] = true;
                            } else {
                                videoFramesRead[var25]++;
                                this.printLegend((int) nextVideoFrame.getPacket().getFrameNo(), 0, nextVideoFrame.getPacket());
                            }
                        }
                        if (finishedVideo[var25]) {
                            for (Transcoder.Stream streamx : videoStreams[var25]) {
                                for (int ss = 0; ss < audioStreams.length; ss++) {
                                    audioStreams[ss].remove(streamx);
                                }
                            }
                            videoStreams[var25].clear();
                        }
                        if (nextVideoFrame != null) {
                            for (Transcoder.Stream streamx : videoStreams[var25]) {
                                streamx.addVideoPacket(nextVideoFrame, source.getVideoCodecMeta());
                            }
                            if (nextVideoFrame.getFrame() != null) {
                                pixelStore.putBack(nextVideoFrame.getFrame());
                            }
                        }
                    }
                    if (audioStreams[var25].isEmpty()) {
                        finishedAudio[var25] = true;
                    } else {
                        AudioFrameWithPacket nextAudioFrame;
                        if (!decodeAudio[var25] && source instanceof PacketSource) {
                            Packet packet = ((PacketSource) source).inputAudioPacket();
                            if (packet == null) {
                                finishedAudio[var25] = true;
                                nextAudioFrame = null;
                            } else {
                                nextAudioFrame = new AudioFrameWithPacket(null, packet);
                            }
                        } else {
                            nextAudioFrame = source.getNextAudioFrame();
                            if (nextAudioFrame == null) {
                                finishedAudio[var25] = true;
                            }
                        }
                        if (nextAudioFrame != null) {
                            for (Transcoder.Stream streamx : audioStreams[var25]) {
                                streamx.addAudioPacket(nextAudioFrame, source.getAudioCodecMeta());
                            }
                        }
                    }
                }
                for (int s = 0; s < allStreams.length; s++) {
                    allStreams[s].tryFlushQueues();
                }
                boolean allFinished = true;
                for (int s = 0; s < this.sources.length; s++) {
                    allFinished &= finishedVideo[s] & finishedAudio[s];
                }
                if (allFinished) {
                    for (int s = 0; s < allStreams.length; s++) {
                        allStreams[s].finalFlushQueues();
                    }
                    var20 = false;
                    break;
                }
            } finally {
                if (var20) {
                    for (int i = 0; i < this.sources.length; i++) {
                        this.sources[0].finish();
                    }
                    for (int i = 0; i < this.sinks.length; i++) {
                        this.sinks[i].finish();
                    }
                }
            }
        }
        for (int i = 0; i < this.sources.length; i++) {
            this.sources[0].finish();
        }
        for (int i = 0; i < this.sinks.length; i++) {
            this.sinks[i].finish();
        }
    }

    private void printLegend(int frameNo, int maxFrames, Packet inVideoPacket) {
        if (frameNo % 100 == 0) {
            System.out.print(String.format("[%6d]\r", frameNo));
        }
    }

    public static Transcoder.TranscoderBuilder newTranscoder() {
        return new Transcoder.TranscoderBuilder();
    }

    private static class Mapping {

        private int source;

        private boolean copy;

        public Mapping(int source, boolean copy) {
            this.source = source;
            this.copy = copy;
        }
    }

    private static class Stream {

        private static final double AUDIO_LEADING_TIME = 0.2;

        private LinkedList<VideoFrameWithPacket> videoQueue;

        private LinkedList<AudioFrameWithPacket> audioQueue;

        private List<Filter> filters;

        private List<Filter> extraFilters;

        private Sink sink;

        private boolean videoCopy;

        private boolean audioCopy;

        private PixelStore pixelStore;

        private VideoCodecMeta videoCodecMeta;

        private AudioCodecMeta audioCodecMeta;

        private static final int REORDER_LENGTH = 5;

        public Stream(Sink sink, boolean videoCopy, boolean audioCopy, List<Filter> extraFilters, PixelStore pixelStore) {
            this.sink = sink;
            this.videoCopy = videoCopy;
            this.audioCopy = audioCopy;
            this.extraFilters = extraFilters;
            this.pixelStore = pixelStore;
            this.videoQueue = new LinkedList();
            this.audioQueue = new LinkedList();
        }

        private List<Filter> initColorTransform(ColorSpace sourceColor, List<Filter> extraFilters, Sink sink) {
            List<Filter> filters = new ArrayList();
            for (Filter filter : extraFilters) {
                ColorSpace inputColor = filter.getInputColor();
                if (!sourceColor.matches(inputColor)) {
                    filters.add(new ColorTransformFilter(inputColor));
                }
                filters.add(filter);
                if (filter.getOutputColor() != ColorSpace.SAME) {
                    sourceColor = filter.getOutputColor();
                }
            }
            ColorSpace inputColorx = sink.getInputColor();
            if (inputColorx != null && inputColorx != sourceColor) {
                filters.add(new ColorTransformFilter(inputColorx));
            }
            return filters;
        }

        public void tryFlushQueues() throws IOException {
            if (this.videoQueue.size() > 0) {
                if (!this.videoCopy || this.videoQueue.size() >= 5) {
                    if (this.hasLeadingAudio()) {
                        VideoFrameWithPacket firstVideoFrame = (VideoFrameWithPacket) this.videoQueue.get(0);
                        if (this.videoCopy) {
                            for (VideoFrameWithPacket videoFrame : this.videoQueue) {
                                if (videoFrame.getPacket().getFrameNo() < firstVideoFrame.getPacket().getFrameNo()) {
                                    firstVideoFrame = videoFrame;
                                }
                            }
                        }
                        int aqSize = this.audioQueue.size();
                        for (int af = 0; af < aqSize; af++) {
                            AudioFrameWithPacket audioFrame = (AudioFrameWithPacket) this.audioQueue.get(0);
                            if (audioFrame.getPacket().getPtsD() >= firstVideoFrame.getPacket().getPtsD() + 0.2) {
                                break;
                            }
                            this.audioQueue.remove(0);
                            if (this.audioCopy && this.sink instanceof PacketSink) {
                                ((PacketSink) this.sink).outputAudioPacket(audioFrame.getPacket(), this.audioCodecMeta);
                            } else {
                                this.sink.outputAudioFrame(audioFrame);
                            }
                        }
                        this.videoQueue.remove(firstVideoFrame);
                        if (this.videoCopy && this.sink instanceof PacketSink) {
                            ((PacketSink) this.sink).outputVideoPacket(firstVideoFrame.getPacket(), this.videoCodecMeta);
                        } else {
                            PixelStore.LoanerPicture frame = this.filterFrame(firstVideoFrame);
                            this.sink.outputVideoFrame(new VideoFrameWithPacket(firstVideoFrame.getPacket(), frame));
                            this.pixelStore.putBack(frame);
                        }
                    }
                }
            }
        }

        private PixelStore.LoanerPicture filterFrame(VideoFrameWithPacket firstVideoFrame) {
            PixelStore.LoanerPicture frame = firstVideoFrame.getFrame();
            for (Filter filter : this.filters) {
                PixelStore.LoanerPicture old = frame;
                frame = filter.filter(frame.getPicture(), this.pixelStore);
                if (frame == null) {
                    frame = old;
                } else {
                    this.pixelStore.putBack(old);
                }
            }
            return frame;
        }

        public void finalFlushQueues() throws IOException {
            VideoFrameWithPacket lastVideoFrame = null;
            for (VideoFrameWithPacket videoFrame : this.videoQueue) {
                if (lastVideoFrame == null || videoFrame.getPacket().getPtsD() >= lastVideoFrame.getPacket().getPtsD()) {
                    lastVideoFrame = videoFrame;
                }
            }
            if (lastVideoFrame != null) {
                for (AudioFrameWithPacket audioFrame : this.audioQueue) {
                    if (audioFrame.getPacket().getPtsD() > lastVideoFrame.getPacket().getPtsD()) {
                        break;
                    }
                    if (this.audioCopy && this.sink instanceof PacketSink) {
                        ((PacketSink) this.sink).outputAudioPacket(audioFrame.getPacket(), this.audioCodecMeta);
                    } else {
                        this.sink.outputAudioFrame(audioFrame);
                    }
                }
                for (VideoFrameWithPacket videoFramex : this.videoQueue) {
                    if (videoFramex != null) {
                        if (this.videoCopy && this.sink instanceof PacketSink) {
                            ((PacketSink) this.sink).outputVideoPacket(videoFramex.getPacket(), this.videoCodecMeta);
                        } else {
                            PixelStore.LoanerPicture frame = this.filterFrame(videoFramex);
                            this.sink.outputVideoFrame(new VideoFrameWithPacket(videoFramex.getPacket(), frame));
                            this.pixelStore.putBack(frame);
                        }
                    }
                }
            } else {
                for (AudioFrameWithPacket audioFrame : this.audioQueue) {
                    if (this.audioCopy && this.sink instanceof PacketSink) {
                        ((PacketSink) this.sink).outputAudioPacket(audioFrame.getPacket(), this.audioCodecMeta);
                    } else {
                        this.sink.outputAudioFrame(audioFrame);
                    }
                }
            }
        }

        public void addVideoPacket(VideoFrameWithPacket videoFrame, VideoCodecMeta meta) {
            if (videoFrame.getFrame() != null) {
                this.pixelStore.retake(videoFrame.getFrame());
            }
            this.videoQueue.add(videoFrame);
            this.videoCodecMeta = meta;
            if (this.filters == null) {
                this.filters = this.initColorTransform(this.videoCodecMeta.getColor(), this.extraFilters, this.sink);
            }
        }

        public void addAudioPacket(AudioFrameWithPacket videoFrame, AudioCodecMeta meta) {
            this.audioQueue.add(videoFrame);
            this.audioCodecMeta = meta;
        }

        public boolean needsVideoFrame() {
            return this.videoQueue.size() <= 0 ? true : this.videoCopy && this.videoQueue.size() < 5;
        }

        public boolean hasLeadingAudio() {
            VideoFrameWithPacket firstVideoFrame = (VideoFrameWithPacket) this.videoQueue.get(0);
            for (AudioFrameWithPacket audioFrame : this.audioQueue) {
                if (audioFrame.getPacket().getPtsD() >= firstVideoFrame.getPacket().getPtsD() + 0.2) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class TranscoderBuilder {

        private List<Source> source = new ArrayList();

        private List<Sink> sink = new ArrayList();

        private List<List<Filter>> filters = new ArrayList();

        private IntArrayList seekFrames = new IntArrayList(20);

        private IntArrayList maxFrames = new IntArrayList(20);

        private List<Transcoder.Mapping> videoMappings = new ArrayList();

        private List<Transcoder.Mapping> audioMappings = new ArrayList();

        public Transcoder.TranscoderBuilder addFilter(int sink, Filter filter) {
            ((List) this.filters.get(sink)).add(filter);
            return this;
        }

        public Transcoder.TranscoderBuilder setSeekFrames(int source, int seekFrames) {
            this.seekFrames.set(source, seekFrames);
            return this;
        }

        public Transcoder.TranscoderBuilder setMaxFrames(int source, int maxFrames) {
            this.maxFrames.set(source, maxFrames);
            return this;
        }

        public Transcoder.TranscoderBuilder addSource(Source source) {
            this.source.add(source);
            this.seekFrames.add(0);
            this.maxFrames.add(Integer.MAX_VALUE);
            return this;
        }

        public Transcoder.TranscoderBuilder addSink(Sink sink) {
            this.sink.add(sink);
            this.videoMappings.add(new Transcoder.Mapping(0, false));
            this.audioMappings.add(new Transcoder.Mapping(0, false));
            this.filters.add(new ArrayList());
            return this;
        }

        public Transcoder.TranscoderBuilder setVideoMapping(int src, int sink, boolean copy) {
            this.videoMappings.set(sink, new Transcoder.Mapping(src, copy));
            return this;
        }

        public Transcoder.TranscoderBuilder setAudioMapping(int src, int sink, boolean copy) {
            this.audioMappings.set(sink, new Transcoder.Mapping(src, copy));
            return this;
        }

        public Transcoder create() {
            return new Transcoder((Source[]) this.source.toArray(new Source[0]), (Sink[]) this.sink.toArray(new Sink[0]), (Transcoder.Mapping[]) this.videoMappings.toArray(new Transcoder.Mapping[0]), (Transcoder.Mapping[]) this.audioMappings.toArray(new Transcoder.Mapping[0]), (List[]) this.filters.toArray(new List[0]), this.seekFrames.toArray(), this.maxFrames.toArray());
        }
    }
}