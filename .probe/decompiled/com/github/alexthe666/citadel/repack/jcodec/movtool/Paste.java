package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ClipRegionBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.LoadSettingsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SoundMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrackHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.util.Arrays;

public class Paste {

    long[] tv;

    public static void main1(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Syntax: paste <to movie> <from movie> [second]");
            System.exit(-1);
        }
        File toFile = new File(args[0]);
        SeekableByteChannel to = null;
        SeekableByteChannel from = null;
        SeekableByteChannel out = null;
        try {
            File outFile = new File(toFile.getParentFile(), toFile.getName().replaceAll("\\.mov$", "") + ".paste.mov");
            Platform.deleteFile(outFile);
            out = NIOUtils.writableChannel(outFile);
            to = NIOUtils.writableChannel(toFile);
            File fromFile = new File(args[1]);
            from = NIOUtils.readableChannel(fromFile);
            MP4Util.Movie toMov = MP4Util.createRefFullMovie(to, "file://" + toFile.getCanonicalPath());
            MP4Util.Movie fromMov = MP4Util.createRefFullMovie(from, "file://" + fromFile.getCanonicalPath());
            new Strip().strip(fromMov.getMoov());
            if (args.length > 2) {
                new Paste().paste(toMov.getMoov(), fromMov.getMoov(), Double.parseDouble(args[2]));
            } else {
                new Paste().addToMovie(toMov.getMoov(), fromMov.getMoov());
            }
            MP4Util.writeFullMovie(out, toMov);
        } finally {
            if (to != null) {
                to.close();
            }
            if (from != null) {
                from.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public void paste(MovieBox to, MovieBox from, double sec) {
        TrakBox videoTrack = to.getVideoTrack();
        if (videoTrack != null && videoTrack.getTimescale() != to.getTimescale()) {
            to.fixTimescale(videoTrack.getTimescale());
        }
        long displayTv = (long) ((double) to.getTimescale() * sec);
        Util.forceEditListMov(to);
        Util.forceEditListMov(from);
        TrakBox[] fromTracks = from.getTracks();
        TrakBox[] toTracks = to.getTracks();
        int[][] matches = this.findMatches(fromTracks, toTracks);
        for (int i = 0; i < matches[0].length; i++) {
            TrakBox localTrack = to.importTrack(from, fromTracks[i]);
            if (matches[0][i] != -1) {
                Util.insertTo(to, toTracks[matches[0][i]], localTrack, displayTv);
            } else {
                to.appendTrack(localTrack);
                Util.shift(to, localTrack, displayTv);
            }
        }
        for (int ix = 0; ix < matches[1].length; ix++) {
            if (matches[1][ix] == -1) {
                Util.spread(to, toTracks[ix], displayTv, to.rescale(from.getDuration(), (long) from.getTimescale()));
            }
        }
        to.updateDuration();
    }

    public void addToMovie(MovieBox to, MovieBox from) {
        TrakBox[] tracks = from.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox track = tracks[i];
            to.appendTrack(to.importTrack(from, track));
        }
    }

    private long getFrameTv(TrakBox videoTrack, int frame) {
        if (this.tv == null) {
            this.tv = Util.getTimevalues(videoTrack);
        }
        return this.tv[frame];
    }

    private int[][] findMatches(TrakBox[] fromTracks, TrakBox[] toTracks) {
        int[] f2t = new int[fromTracks.length];
        int[] t2f = new int[toTracks.length];
        Arrays.fill(f2t, -1);
        Arrays.fill(t2f, -1);
        for (int i = 0; i < fromTracks.length; i++) {
            if (f2t[i] == -1) {
                for (int j = 0; j < toTracks.length; j++) {
                    if (t2f[j] == -1 && this.matches(fromTracks[i], toTracks[j])) {
                        f2t[i] = j;
                        t2f[j] = i;
                        break;
                    }
                }
            }
        }
        return new int[][] { f2t, t2f };
    }

    private boolean matches(TrakBox trakBox1, TrakBox trakBox2) {
        return trakBox1.getHandlerType().equals(trakBox2.getHandlerType()) && this.matchHeaders(trakBox1, trakBox2) && this.matchSampleSizes(trakBox1, trakBox2) && this.matchMediaHeader(trakBox1, trakBox2) && this.matchClip(trakBox1, trakBox2) && this.matchLoad(trakBox1, trakBox2);
    }

    private boolean matchSampleSizes(TrakBox trakBox1, TrakBox trakBox2) {
        SampleSizesBox stsz1 = NodeBox.findFirstPath(trakBox1, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        SampleSizesBox stsz2 = NodeBox.findFirstPath(trakBox1, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        return stsz1.getDefaultSize() == stsz2.getDefaultSize();
    }

    private boolean matchMediaHeader(TrakBox trakBox1, TrakBox trakBox2) {
        VideoMediaHeaderBox vmhd1 = NodeBox.findFirstPath(trakBox1, VideoMediaHeaderBox.class, Box.path("mdia.minf.vmhd"));
        VideoMediaHeaderBox vmhd2 = NodeBox.findFirstPath(trakBox2, VideoMediaHeaderBox.class, Box.path("mdia.minf.vmhd"));
        if ((vmhd1 == null || vmhd2 != null) && (vmhd1 != null || vmhd2 == null)) {
            if (vmhd1 != null && vmhd2 != null) {
                return vmhd1.getGraphicsMode() == vmhd2.getGraphicsMode() && vmhd1.getbOpColor() == vmhd2.getbOpColor() && vmhd1.getgOpColor() == vmhd2.getgOpColor() && vmhd1.getrOpColor() == vmhd2.getrOpColor();
            } else {
                SoundMediaHeaderBox smhd1 = NodeBox.findFirstPath(trakBox1, SoundMediaHeaderBox.class, Box.path("mdia.minf.smhd"));
                SoundMediaHeaderBox smhd2 = NodeBox.findFirstPath(trakBox2, SoundMediaHeaderBox.class, Box.path("mdia.minf.smhd"));
                if ((smhd1 != null || smhd2 == null) && (smhd1 == null || smhd2 != null)) {
                    return smhd1 != null && smhd2 != null ? smhd1.getBalance() == smhd1.getBalance() : true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private boolean matchHeaders(TrakBox trakBox1, TrakBox trakBox2) {
        TrackHeaderBox th1 = trakBox1.getTrackHeader();
        TrackHeaderBox th2 = trakBox2.getTrackHeader();
        return "vide".equals(trakBox1.getHandlerType()) && Platform.arrayEqualsInt(th1.getMatrix(), th2.getMatrix()) && th1.getLayer() == th2.getLayer() && th1.getWidth() == th2.getWidth() && th1.getHeight() == th2.getHeight() || "soun".equals(trakBox1.getHandlerType()) && th1.getVolume() == th2.getVolume() || "tmcd".equals(trakBox1.getHandlerType());
    }

    private boolean matchLoad(TrakBox trakBox1, TrakBox trakBox2) {
        LoadSettingsBox load1 = NodeBox.findFirst(trakBox1, LoadSettingsBox.class, "load");
        LoadSettingsBox load2 = NodeBox.findFirst(trakBox2, LoadSettingsBox.class, "load");
        return load1 != null && load2 != null ? load1.getPreloadStartTime() == load2.getPreloadStartTime() && load1.getPreloadDuration() == load2.getPreloadDuration() && load1.getPreloadFlags() == load2.getPreloadFlags() && load1.getDefaultHints() == load2.getDefaultHints() : load1 == null && load2 == null;
    }

    private boolean matchClip(TrakBox trakBox1, TrakBox trakBox2) {
        ClipRegionBox crgn1 = NodeBox.findFirstPath(trakBox1, ClipRegionBox.class, Box.path("clip.crgn"));
        ClipRegionBox crgn2 = NodeBox.findFirstPath(trakBox2, ClipRegionBox.class, Box.path("clip.crgn"));
        return crgn1 != null && crgn2 != null ? crgn1.getRgnSize() == crgn2.getRgnSize() && crgn1.getX() == crgn2.getX() && crgn1.getY() == crgn2.getY() && crgn1.getWidth() == crgn2.getWidth() && crgn1.getHeight() == crgn2.getHeight() : crgn1 == null && crgn2 == null;
    }
}