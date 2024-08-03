package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.Chunk;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.ChunkReader;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.ChunkWriter;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AliasBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataRefBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FileTypeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.UrlBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Flatten {

    public List<Flatten.ProgressListener> listeners = new ArrayList();

    public static void main1(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Syntax: self <ref movie> <out movie>");
            System.exit(-1);
        }
        File outFile = new File(args[1]);
        Platform.deleteFile(outFile);
        SeekableByteChannel input = null;
        try {
            input = NIOUtils.readableChannel(new File(args[0]));
            MP4Util.Movie movie = MP4Util.parseFullMovieChannel(input);
            new Flatten().flatten(movie, outFile);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public void addProgressListener(Flatten.ProgressListener listener) {
        this.listeners.add(listener);
    }

    public void flattenChannel(MP4Util.Movie movie, SeekableByteChannel out) throws IOException {
        FileTypeBox ftyp = movie.getFtyp();
        MovieBox moov = movie.getMoov();
        if (!moov.isPureRefMovie()) {
            throw new IllegalArgumentException("movie should be reference");
        } else {
            out.setPosition(0L);
            MP4Util.writeFullMovie(out, movie);
            int extraSpace = this.calcSpaceReq(moov);
            ByteBuffer buf = ByteBuffer.allocate(extraSpace);
            out.write(buf);
            long mdatOff = out.position();
            this.writeHeader(Header.createHeader("mdat", 4294967297L), out);
            SeekableByteChannel[][] inputs = this.getInputs(moov);
            TrakBox[] tracks = moov.getTracks();
            ChunkReader[] readers = new ChunkReader[tracks.length];
            ChunkWriter[] writers = new ChunkWriter[tracks.length];
            Chunk[] head = new Chunk[tracks.length];
            int totalChunks = 0;
            int writtenChunks = 0;
            int lastProgress = 0;
            long[] off = new long[tracks.length];
            for (int i = 0; i < tracks.length; i++) {
                readers[i] = new ChunkReader(tracks[i]);
                totalChunks += readers[i].size();
                writers[i] = new ChunkWriter(tracks[i], inputs[i], out);
                head[i] = readers[i].next();
                if (tracks[i].isVideo()) {
                    off[i] = (long) (2 * moov.getTimescale());
                }
            }
            while (true) {
                int min = -1;
                for (int ix = 0; ix < readers.length; ix++) {
                    if (head[ix] != null) {
                        if (min == -1) {
                            min = ix;
                        } else {
                            long iTv = moov.rescale(head[ix].getStartTv(), (long) tracks[ix].getTimescale()) + off[ix];
                            long minTv = moov.rescale(head[min].getStartTv(), (long) tracks[min].getTimescale()) + off[min];
                            if (iTv < minTv) {
                                min = ix;
                            }
                        }
                    }
                }
                if (min == -1) {
                    for (int ixx = 0; ixx < tracks.length; ixx++) {
                        writers[ixx].apply();
                    }
                    long mdatSize = out.position() - mdatOff;
                    out.setPosition(0L);
                    MP4Util.writeFullMovie(out, movie);
                    long extra = mdatOff - out.position();
                    if (extra < 0L) {
                        throw new RuntimeException("Not enough space to write the header");
                    }
                    this.writeHeader(Header.createHeader("free", extra), out);
                    out.setPosition(mdatOff);
                    this.writeHeader(Header.createHeader("mdat", mdatSize), out);
                    return;
                }
                writers[min].write(head[min]);
                head[min] = readers[min].next();
                lastProgress = this.calcProgress(totalChunks, ++writtenChunks, lastProgress);
            }
        }
    }

    private void writeHeader(Header header, SeekableByteChannel out) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(16);
        header.write(bb);
        bb.flip();
        out.write(bb);
    }

    private int calcProgress(int totalChunks, int writtenChunks, int lastProgress) {
        int curProgress = 100 * writtenChunks / totalChunks;
        if (lastProgress < curProgress) {
            lastProgress = curProgress;
            for (Flatten.ProgressListener pl : this.listeners) {
                pl.trigger(lastProgress);
            }
        }
        return lastProgress;
    }

    protected SeekableByteChannel[][] getInputs(MovieBox movie) throws IOException {
        TrakBox[] tracks = movie.getTracks();
        SeekableByteChannel[][] result = new SeekableByteChannel[tracks.length][];
        for (int i = 0; i < tracks.length; i++) {
            DataRefBox drefs = NodeBox.findFirstPath(tracks[i], DataRefBox.class, Box.path("mdia.minf.dinf.dref"));
            if (drefs == null) {
                throw new RuntimeException("No data references");
            }
            List<Box> entries = drefs.getBoxes();
            SeekableByteChannel[] e = new SeekableByteChannel[entries.size()];
            SeekableByteChannel[] inputs = new SeekableByteChannel[entries.size()];
            for (int j = 0; j < e.length; j++) {
                inputs[j] = this.resolveDataRef((Box) entries.get(j));
            }
            result[i] = inputs;
        }
        return result;
    }

    private int calcSpaceReq(MovieBox movie) {
        int sum = 0;
        TrakBox[] tracks = movie.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            ChunkOffsetsBox stco = trakBox.getStco();
            if (stco != null) {
                sum += stco.getChunkOffsets().length * 4;
            }
        }
        return sum;
    }

    public SeekableByteChannel resolveDataRef(Box box) throws IOException {
        if (box instanceof UrlBox) {
            String url = ((UrlBox) box).getUrl();
            if (!url.startsWith("file://")) {
                throw new RuntimeException("Only file:// urls are supported in data reference");
            } else {
                return NIOUtils.readableChannel(new File(url.substring(7)));
            }
        } else if (box instanceof AliasBox) {
            String uxPath = ((AliasBox) box).getUnixPath();
            if (uxPath == null) {
                throw new RuntimeException("Could not resolve alias");
            } else {
                return NIOUtils.readableChannel(new File(uxPath));
            }
        } else {
            throw new RuntimeException(box.getHeader().getFourcc() + " dataref type is not supported");
        }
    }

    public void flatten(MP4Util.Movie movie, File video) throws IOException {
        Platform.deleteFile(video);
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableChannel(video);
            this.flattenChannel(movie, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public interface ProgressListener {

        void trigger(int var1);
    }
}