package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.Chunk;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.ChunkReader;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimeToSampleBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Strip {

    public static void main1(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Syntax: strip <ref movie> <out movie>");
            System.exit(-1);
        }
        SeekableByteChannel input = null;
        SeekableByteChannel out = null;
        try {
            input = NIOUtils.readableChannel(new File(args[0]));
            File file = new File(args[1]);
            Platform.deleteFile(file);
            out = NIOUtils.writableChannel(file);
            MP4Util.Movie movie = MP4Util.createRefFullMovie(input, "file://" + new File(args[0]).getAbsolutePath());
            new Strip().strip(movie.getMoov());
            MP4Util.writeFullMovie(out, movie);
        } finally {
            if (input != null) {
                input.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public void strip(MovieBox movie) throws IOException {
        TrakBox[] tracks = movie.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox track = tracks[i];
            this.stripTrack(movie, track);
        }
    }

    public void stripTrack(MovieBox movie, TrakBox track) {
        ChunkReader chunks = new ChunkReader(track);
        List<Edit> edits = track.getEdits();
        List<Edit> oldEdits = this.deepCopy(edits);
        List<Chunk> result = new ArrayList();
        Chunk chunk;
        while ((chunk = chunks.next()) != null) {
            boolean intersects = false;
            for (Edit edit : oldEdits) {
                if (edit.getMediaTime() != -1L) {
                    long editS = edit.getMediaTime();
                    long editE = edit.getMediaTime() + track.rescale(edit.getDuration(), (long) movie.getTimescale());
                    long chunkS = chunk.getStartTv();
                    long chunkE = chunk.getStartTv() + (long) chunk.getDuration();
                    intersects = this.intersects(editS, editE, chunkS, chunkE);
                    if (intersects) {
                        break;
                    }
                }
            }
            if (!intersects) {
                for (int i = 0; i < oldEdits.size(); i++) {
                    if (((Edit) oldEdits.get(i)).getMediaTime() >= chunk.getStartTv() + (long) chunk.getDuration()) {
                        ((Edit) edits.get(i)).shift((long) (-chunk.getDuration()));
                    }
                }
            } else {
                result.add(chunk);
            }
        }
        NodeBox stbl = NodeBox.findFirstPath(track, NodeBox.class, Box.path("mdia.minf.stbl"));
        stbl.replace("stts", this.getTimeToSamples(result));
        stbl.replace("stsz", this.getSampleSizes(result));
        stbl.replace("stsc", this.getSamplesToChunk(result));
        stbl.removeChildren(new String[] { "stco", "co64" });
        stbl.add(this.getChunkOffsets(result));
        NodeBox.<MediaHeaderBox>findFirstPath(track, MediaHeaderBox.class, Box.path("mdia.mdhd")).setDuration(this.totalDuration(result));
    }

    private long totalDuration(List<Chunk> result) {
        long duration = 0L;
        for (Chunk chunk : result) {
            duration += (long) chunk.getDuration();
        }
        return duration;
    }

    private List<Edit> deepCopy(List<Edit> edits) {
        ArrayList<Edit> newList = new ArrayList();
        for (Edit edit : edits) {
            newList.add(Edit.createEdit(edit));
        }
        return newList;
    }

    public Box getChunkOffsets(List<Chunk> chunks) {
        long[] result = new long[chunks.size()];
        boolean longBox = false;
        int i = 0;
        for (Chunk chunk : chunks) {
            if (chunk.getOffset() >= 4294967296L) {
                longBox = true;
            }
            result[i++] = chunk.getOffset();
        }
        return (Box) (longBox ? ChunkOffsets64Box.createChunkOffsets64Box(result) : ChunkOffsetsBox.createChunkOffsetsBox(result));
    }

    public TimeToSampleBox getTimeToSamples(List<Chunk> chunks) {
        ArrayList<TimeToSampleBox.TimeToSampleEntry> tts = new ArrayList();
        int curTts = -1;
        int cnt = 0;
        for (Chunk chunk : chunks) {
            if (chunk.getSampleDur() > 0) {
                if (curTts == -1 || curTts != chunk.getSampleDur()) {
                    if (curTts != -1) {
                        tts.add(new TimeToSampleBox.TimeToSampleEntry(cnt, curTts));
                    }
                    cnt = 0;
                    curTts = chunk.getSampleDur();
                }
                cnt += chunk.getSampleCount();
            } else {
                for (int dur : chunk.getSampleDurs()) {
                    if (curTts == -1 || curTts != dur) {
                        if (curTts != -1) {
                            tts.add(new TimeToSampleBox.TimeToSampleEntry(cnt, curTts));
                        }
                        cnt = 0;
                        curTts = dur;
                    }
                    cnt++;
                }
            }
        }
        if (cnt > 0) {
            tts.add(new TimeToSampleBox.TimeToSampleEntry(cnt, curTts));
        }
        return TimeToSampleBox.createTimeToSampleBox((TimeToSampleBox.TimeToSampleEntry[]) tts.toArray(new TimeToSampleBox.TimeToSampleEntry[0]));
    }

    public SampleSizesBox getSampleSizes(List<Chunk> chunks) {
        int nSamples = 0;
        int prevSize = ((Chunk) chunks.get(0)).getSampleSize();
        for (Chunk chunk : chunks) {
            nSamples += chunk.getSampleCount();
            if (prevSize == 0 && chunk.getSampleSize() != 0) {
                throw new RuntimeException("Mixed sample sizes not supported");
            }
        }
        if (prevSize > 0) {
            return SampleSizesBox.createSampleSizesBox(prevSize, nSamples);
        } else {
            int[] sizes = new int[nSamples];
            int startSample = 0;
            for (Chunk chunkx : chunks) {
                System.arraycopy(chunkx.getSampleSizes(), 0, sizes, startSample, chunkx.getSampleCount());
                startSample += chunkx.getSampleCount();
            }
            return SampleSizesBox.createSampleSizesBox2(sizes);
        }
    }

    public SampleToChunkBox getSamplesToChunk(List<Chunk> chunks) {
        ArrayList<SampleToChunkBox.SampleToChunkEntry> result = new ArrayList();
        Iterator<Chunk> it = chunks.iterator();
        Chunk chunk = (Chunk) it.next();
        int curSz = chunk.getSampleCount();
        int curEntry = chunk.getEntry();
        int first = 1;
        int cnt;
        for (cnt = 1; it.hasNext(); cnt++) {
            chunk = (Chunk) it.next();
            int newSz = chunk.getSampleCount();
            int newEntry = chunk.getEntry();
            if (curSz != newSz || curEntry != newEntry) {
                result.add(new SampleToChunkBox.SampleToChunkEntry((long) first, curSz, curEntry));
                curSz = newSz;
                curEntry = newEntry;
                first += cnt;
                cnt = 0;
            }
        }
        if (cnt > 0) {
            result.add(new SampleToChunkBox.SampleToChunkEntry((long) first, curSz, curEntry));
        }
        return SampleToChunkBox.createSampleToChunkBox((SampleToChunkBox.SampleToChunkEntry[]) result.toArray(new SampleToChunkBox.SampleToChunkEntry[0]));
    }

    private boolean intersects(long a, long b, long c, long d) {
        return a >= c && a < d || b >= c && b < d || c >= a && c < b || d >= a && d < b;
    }
}