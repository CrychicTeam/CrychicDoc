package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.BoxFactory;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Cut {

    public static void main1(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Syntax: cut [-command arg]...[-command arg] [-self] <movie file>\n\tCreates a reference movie out of the file and applies a set of changes specified by the commands to it.");
            System.exit(-1);
        }
        List<Cut.Slice> slices = new ArrayList();
        List<String> sliceNames = new ArrayList();
        boolean selfContained = false;
        int shift = 0;
        while (true) {
            while (!"-cut".equals(args[shift])) {
                if (!"-self".equals(args[shift])) {
                    File source = new File(args[shift]);
                    SeekableByteChannel input = null;
                    SeekableByteChannel out = null;
                    List<SeekableByteChannel> outs = new ArrayList();
                    try {
                        input = NIOUtils.readableChannel(source);
                        MP4Util.Movie movie = MP4Util.createRefFullMovie(input, "file://" + source.getCanonicalPath());
                        List<MP4Util.Movie> slicesMovs;
                        if (!selfContained) {
                            out = NIOUtils.writableChannel(new File(source.getParentFile(), JCodecUtil2.removeExtension(source.getName()) + ".ref.mov"));
                            slicesMovs = new Cut().cut(movie, slices);
                            MP4Util.writeFullMovie(out, movie);
                        } else {
                            out = NIOUtils.writableChannel(new File(source.getParentFile(), JCodecUtil2.removeExtension(source.getName()) + ".self.mov"));
                            slicesMovs = new Cut().cut(movie, slices);
                            new Strip().strip(movie.getMoov());
                            new Flatten().flattenChannel(movie, out);
                        }
                        saveSlices(slicesMovs, sliceNames, source.getParentFile());
                    } finally {
                        if (input != null) {
                            input.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        for (SeekableByteChannel o : outs) {
                            o.close();
                        }
                    }
                    return;
                }
                shift++;
                selfContained = true;
            }
            String[] pt = StringUtils.splitS(args[shift + 1], ":");
            slices.add(new Cut.Slice((double) Integer.parseInt(pt[0]), (double) Integer.parseInt(pt[1])));
            if (pt.length > 2) {
                sliceNames.add(pt[2]);
            } else {
                sliceNames.add(null);
            }
            shift += 2;
        }
    }

    private static void saveSlices(List<MP4Util.Movie> slices, List<String> names, File parentFile) throws IOException {
        for (int i = 0; i < slices.size(); i++) {
            if (names.get(i) != null) {
                SeekableByteChannel out = null;
                try {
                    out = NIOUtils.writableChannel(new File(parentFile, (String) names.get(i)));
                    MP4Util.writeFullMovie(out, (MP4Util.Movie) slices.get(i));
                } finally {
                    NIOUtils.closeQuietly(out);
                }
            }
        }
    }

    public List<MP4Util.Movie> cut(MP4Util.Movie movie, List<Cut.Slice> commands) {
        MovieBox moov = movie.getMoov();
        TrakBox videoTrack = moov.getVideoTrack();
        if (videoTrack != null && videoTrack.getTimescale() != moov.getTimescale()) {
            moov.fixTimescale(videoTrack.getTimescale());
        }
        TrakBox[] tracks = moov.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            Util.forceEditList(moov, trakBox);
            List<Edit> edits = trakBox.getEdits();
            for (Cut.Slice cut : commands) {
                this.split(edits, cut.inSec, moov, trakBox);
                this.split(edits, cut.outSec, moov, trakBox);
            }
        }
        ArrayList<MP4Util.Movie> result = new ArrayList();
        for (Cut.Slice cut : commands) {
            MovieBox clone = (MovieBox) NodeBox.cloneBox(moov, 16777216, BoxFactory.getDefault());
            for (TrakBox trakBox : clone.getTracks()) {
                this.selectInner(trakBox.getEdits(), cut, moov, trakBox);
            }
            result.add(new MP4Util.Movie(movie.getFtyp(), clone));
        }
        long movDuration = 0L;
        for (TrakBox trakBox : moov.getTracks()) {
            this.selectOuter(trakBox.getEdits(), commands, moov, trakBox);
            trakBox.setEdits(trakBox.getEdits());
            movDuration = Math.max(movDuration, trakBox.getDuration());
        }
        moov.setDuration(movDuration);
        return result;
    }

    private void selectOuter(List<Edit> edits, List<Cut.Slice> commands, MovieBox movie, TrakBox trakBox) {
        long[] inMv = new long[commands.size()];
        long[] outMv = new long[commands.size()];
        for (int i = 0; i < commands.size(); i++) {
            inMv[i] = (long) (((Cut.Slice) commands.get(i)).inSec * (double) movie.getTimescale());
            outMv[i] = (long) (((Cut.Slice) commands.get(i)).outSec * (double) movie.getTimescale());
        }
        long editStartMv = 0L;
        ListIterator<Edit> lit = edits.listIterator();
        while (lit.hasNext()) {
            Edit edit = (Edit) lit.next();
            for (int i = 0; i < inMv.length; i++) {
                if (editStartMv + edit.getDuration() > inMv[i] && editStartMv < outMv[i]) {
                    lit.remove();
                }
            }
            editStartMv += edit.getDuration();
        }
    }

    private void selectInner(List<Edit> edits, Cut.Slice cut, MovieBox movie, TrakBox trakBox) {
        long inMv = (long) ((double) movie.getTimescale() * cut.inSec);
        long outMv = (long) ((double) movie.getTimescale() * cut.outSec);
        long editStart = 0L;
        ListIterator<Edit> lit = edits.listIterator();
        while (lit.hasNext()) {
            Edit edit = (Edit) lit.next();
            if (editStart + edit.getDuration() <= inMv || editStart >= outMv) {
                lit.remove();
            }
            editStart += edit.getDuration();
        }
    }

    private void split(List<Edit> edits, double sec, MovieBox movie, TrakBox trakBox) {
        Util.split(movie, trakBox, (long) (sec * (double) movie.getTimescale()));
    }

    public static class Slice {

        private double inSec;

        private double outSec;

        public Slice(double _in, double out) {
            this.inSec = _in;
            this.outSec = out;
        }
    }
}