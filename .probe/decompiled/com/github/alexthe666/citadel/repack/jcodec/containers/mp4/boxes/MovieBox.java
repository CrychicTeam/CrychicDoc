package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MovieBox extends NodeBox {

    public MovieBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "moov";
    }

    public static MovieBox createMovieBox() {
        return new MovieBox(new Header(fourcc()));
    }

    public TrakBox[] getTracks() {
        return NodeBox.findAll(this, TrakBox.class, "trak");
    }

    public TrakBox getVideoTrack() {
        TrakBox[] tracks = this.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isVideo()) {
                return trakBox;
            }
        }
        return null;
    }

    public TrakBox getTimecodeTrack() {
        TrakBox[] tracks = this.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isTimecode()) {
                return trakBox;
            }
        }
        return null;
    }

    public int getTimescale() {
        return this.getMovieHeader().getTimescale();
    }

    public long rescale(long tv, long ts) {
        return tv * (long) this.getTimescale() / ts;
    }

    public void fixTimescale(int newTs) {
        int oldTs = this.getTimescale();
        this.setTimescale(newTs);
        TrakBox[] tracks = this.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDuration(this.rescale(trakBox.getDuration(), (long) oldTs));
            List<Edit> edits = trakBox.getEdits();
            if (edits != null) {
                ListIterator<Edit> lit = edits.listIterator();
                while (lit.hasNext()) {
                    Edit edit = (Edit) lit.next();
                    lit.set(new Edit(this.rescale(edit.getDuration(), (long) oldTs), edit.getMediaTime(), edit.getRate()));
                }
            }
        }
        this.setDuration(this.rescale(this.getDuration(), (long) oldTs));
    }

    private void setTimescale(int newTs) {
        NodeBox.<MovieHeaderBox>findFirst(this, MovieHeaderBox.class, "mvhd").setTimescale(newTs);
    }

    public void setDuration(long movDuration) {
        this.getMovieHeader().setDuration(movDuration);
    }

    private MovieHeaderBox getMovieHeader() {
        return NodeBox.findFirst(this, MovieHeaderBox.class, "mvhd");
    }

    public List<TrakBox> getAudioTracks() {
        ArrayList<TrakBox> result = new ArrayList();
        TrakBox[] tracks = this.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.isAudio()) {
                result.add(trakBox);
            }
        }
        return result;
    }

    public long getDuration() {
        return this.getMovieHeader().getDuration();
    }

    public TrakBox importTrack(MovieBox movie, TrakBox track) {
        TrakBox newTrack = (TrakBox) NodeBox.cloneBox(track, 1048576, this.factory);
        List<Edit> edits = newTrack.getEdits();
        ArrayList<Edit> result = new ArrayList();
        if (edits != null) {
            for (Edit edit : edits) {
                result.add(new Edit(this.rescale(edit.getDuration(), (long) movie.getTimescale()), edit.getMediaTime(), edit.getRate()));
            }
        }
        newTrack.setEdits(result);
        return newTrack;
    }

    public void appendTrack(TrakBox newTrack) {
        newTrack.getTrackHeader().setNo(this.getMovieHeader().getNextTrackId());
        this.getMovieHeader().setNextTrackId(this.getMovieHeader().getNextTrackId() + 1);
        this.boxes.add(newTrack);
    }

    public boolean isPureRefMovie() {
        boolean pureRef = true;
        TrakBox[] tracks = this.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            pureRef &= trakBox.isPureRef();
        }
        return pureRef;
    }

    public void updateDuration() {
        TrakBox[] tracks = this.getTracks();
        long min = 2147483647L;
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            if (trakBox.getDuration() < min) {
                min = trakBox.getDuration();
            }
        }
        this.getMovieHeader().setDuration(min);
    }

    public Size getDisplaySize() {
        TrakBox videoTrack = this.getVideoTrack();
        if (videoTrack == null) {
            return null;
        } else {
            ClearApertureBox clef = NodeBox.findFirstPath(videoTrack, ClearApertureBox.class, Box.path("tapt.clef"));
            if (clef != null) {
                return this.applyMatrix(videoTrack, new Size((int) clef.getWidth(), (int) clef.getHeight()));
            } else {
                Box box = (Box) NodeBox.<SampleDescriptionBox>findFirstPath(videoTrack, SampleDescriptionBox.class, Box.path("mdia.minf.stbl.stsd")).getBoxes().get(0);
                if (box != null && box instanceof VideoSampleEntry) {
                    VideoSampleEntry vs = (VideoSampleEntry) box;
                    Rational par = videoTrack.getPAR();
                    return this.applyMatrix(videoTrack, new Size(vs.getWidth() * par.getNum() / par.getDen(), vs.getHeight()));
                } else {
                    return null;
                }
            }
        }
    }

    private Size applyMatrix(TrakBox videoTrack, Size size) {
        int[] matrix = videoTrack.getTrackHeader().getMatrix();
        return new Size((int) ((double) size.getWidth() * (double) matrix[0] / 65536.0), (int) ((double) size.getHeight() * (double) matrix[4] / 65536.0));
    }

    public Size getStoredSize() {
        TrakBox videoTrack = this.getVideoTrack();
        if (videoTrack == null) {
            return null;
        } else {
            EncodedPixelBox enof = NodeBox.findFirstPath(videoTrack, EncodedPixelBox.class, Box.path("tapt.enof"));
            if (enof != null) {
                return new Size((int) enof.getWidth(), (int) enof.getHeight());
            } else {
                Box box = (Box) NodeBox.<SampleDescriptionBox>findFirstPath(videoTrack, SampleDescriptionBox.class, Box.path("mdia.minf.stbl.stsd")).getBoxes().get(0);
                if (box != null && box instanceof VideoSampleEntry) {
                    VideoSampleEntry vs = (VideoSampleEntry) box;
                    return new Size(vs.getWidth(), vs.getHeight());
                } else {
                    return null;
                }
            }
        }
    }
}