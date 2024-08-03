package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.AutoFileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FileTypeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieFragmentBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MP4Util {

    private static Map<Codec, String> codecMapping = new HashMap();

    public static MovieBox createRefMovie(SeekableByteChannel input, String url) throws IOException {
        MovieBox movie = parseMovieChannel(input);
        TrakBox[] tracks = movie.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDataRef(url);
        }
        return movie;
    }

    public static MovieBox parseMovieChannel(SeekableByteChannel input) throws IOException {
        for (MP4Util.Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                return (MovieBox) atom.parseBox(input);
            }
        }
        return null;
    }

    public static MP4Util.Movie createRefFullMovie(SeekableByteChannel input, String url) throws IOException {
        MP4Util.Movie movie = parseFullMovieChannel(input);
        TrakBox[] tracks = movie.moov.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            TrakBox trakBox = tracks[i];
            trakBox.setDataRef(url);
        }
        return movie;
    }

    public static MP4Util.Movie parseFullMovieChannel(SeekableByteChannel input) throws IOException {
        FileTypeBox ftyp = null;
        for (MP4Util.Atom atom : getRootAtoms(input)) {
            if ("ftyp".equals(atom.getHeader().getFourcc())) {
                ftyp = (FileTypeBox) atom.parseBox(input);
            } else if ("moov".equals(atom.getHeader().getFourcc())) {
                return new MP4Util.Movie(ftyp, (MovieBox) atom.parseBox(input));
            }
        }
        return null;
    }

    public static List<MovieFragmentBox> parseMovieFragments(SeekableByteChannel input) throws IOException {
        MovieBox moov = null;
        LinkedList<MovieFragmentBox> fragments = new LinkedList();
        for (MP4Util.Atom atom : getRootAtoms(input)) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                moov = (MovieBox) atom.parseBox(input);
            } else if ("moof".equalsIgnoreCase(atom.getHeader().getFourcc())) {
                fragments.add((MovieFragmentBox) atom.parseBox(input));
            }
        }
        for (MovieFragmentBox fragment : fragments) {
            fragment.setMovie(moov);
        }
        return fragments;
    }

    public static List<MP4Util.Atom> getRootAtoms(SeekableByteChannel input) throws IOException {
        input.setPosition(0L);
        List<MP4Util.Atom> result = new ArrayList();
        long off = 0L;
        while (off < input.size()) {
            input.setPosition(off);
            Header atom = Header.read(NIOUtils.fetchFromChannel(input, 16));
            if (atom == null) {
                break;
            }
            result.add(new MP4Util.Atom(atom, off));
            off += atom.getSize();
        }
        return result;
    }

    public static MP4Util.Atom findFirstAtomInFile(String fourcc, File input) throws IOException {
        SeekableByteChannel c = new AutoFileChannelWrapper(input);
        MP4Util.Atom var3;
        try {
            var3 = findFirstAtom(fourcc, c);
        } finally {
            IOUtils.closeQuietly(c);
        }
        return var3;
    }

    public static MP4Util.Atom findFirstAtom(String fourcc, SeekableByteChannel input) throws IOException {
        for (MP4Util.Atom atom : getRootAtoms(input)) {
            if (fourcc.equals(atom.getHeader().getFourcc())) {
                return atom;
            }
        }
        return null;
    }

    public static MP4Util.Atom atom(SeekableByteChannel input) throws IOException {
        long off = input.position();
        Header atom = Header.read(NIOUtils.fetchFromChannel(input, 16));
        return atom == null ? null : new MP4Util.Atom(atom, off);
    }

    public static MovieBox parseMovie(File source) throws IOException {
        SeekableByteChannel input = null;
        MovieBox var2;
        try {
            input = NIOUtils.readableChannel(source);
            var2 = parseMovieChannel(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return var2;
    }

    public static MovieBox createRefMovieFromFile(File source) throws IOException {
        SeekableByteChannel input = null;
        MovieBox var2;
        try {
            input = NIOUtils.readableChannel(source);
            var2 = createRefMovie(input, "file://" + source.getCanonicalPath());
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return var2;
    }

    public static void writeMovieToFile(File f, MovieBox movie) throws IOException {
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableChannel(f);
            writeMovie(out, movie);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void writeMovie(SeekableByteChannel out, MovieBox movie) throws IOException {
        doWriteMovieToChannel(out, movie, 0);
    }

    public static void doWriteMovieToChannel(SeekableByteChannel out, MovieBox movie, int additionalSize) throws IOException {
        int sizeHint = estimateMoovBoxSize(movie) + additionalSize;
        Logger.debug("Using " + sizeHint + " bytes for MOOV box");
        ByteBuffer buf = ByteBuffer.allocate(sizeHint * 4);
        movie.write(buf);
        buf.flip();
        out.write(buf);
    }

    public static MP4Util.Movie parseFullMovie(File source) throws IOException {
        SeekableByteChannel input = null;
        MP4Util.Movie var2;
        try {
            input = NIOUtils.readableChannel(source);
            var2 = parseFullMovieChannel(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return var2;
    }

    public static MP4Util.Movie createRefFullMovieFromFile(File source) throws IOException {
        SeekableByteChannel input = null;
        MP4Util.Movie var2;
        try {
            input = NIOUtils.readableChannel(source);
            var2 = createRefFullMovie(input, "file://" + source.getCanonicalPath());
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return var2;
    }

    public static void writeFullMovieToFile(File f, MP4Util.Movie movie) throws IOException {
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableChannel(f);
            writeFullMovie(out, movie);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void writeFullMovie(SeekableByteChannel out, MP4Util.Movie movie) throws IOException {
        doWriteFullMovieToChannel(out, movie, 0);
    }

    public static void doWriteFullMovieToChannel(SeekableByteChannel out, MP4Util.Movie movie, int additionalSize) throws IOException {
        int sizeHint = estimateMoovBoxSize(movie.getMoov()) + additionalSize;
        Logger.debug("Using " + sizeHint + " bytes for MOOV box");
        ByteBuffer buf = ByteBuffer.allocate(sizeHint + 128);
        movie.getFtyp().write(buf);
        movie.getMoov().write(buf);
        buf.flip();
        out.write(buf);
    }

    public static int estimateMoovBoxSize(MovieBox movie) {
        return movie.estimateSize() + 4096;
    }

    public static String getFourcc(Codec codec) {
        return (String) codecMapping.get(codec);
    }

    public static ByteBuffer writeBox(Box box, int approxSize) {
        ByteBuffer buf = ByteBuffer.allocate(approxSize);
        box.write(buf);
        buf.flip();
        return buf;
    }

    static {
        codecMapping.put(Codec.MPEG2, "m2v1");
        codecMapping.put(Codec.H264, "avc1");
        codecMapping.put(Codec.J2K, "mjp2");
    }

    public static class Atom {

        private long offset;

        private Header header;

        public Atom(Header header, long offset) {
            this.header = header;
            this.offset = offset;
        }

        public long getOffset() {
            return this.offset;
        }

        public Header getHeader() {
            return this.header;
        }

        public Box parseBox(SeekableByteChannel input) throws IOException {
            input.setPosition(this.offset + this.header.headerSize());
            return BoxUtil.parseBox(NIOUtils.fetchFromChannel(input, (int) this.header.getBodySize()), this.header, BoxFactory.getDefault());
        }

        public void copy(SeekableByteChannel input, WritableByteChannel out) throws IOException {
            input.setPosition(this.offset);
            NIOUtils.copy(input, out, this.header.getSize());
        }
    }

    public static class Movie {

        private FileTypeBox ftyp;

        private MovieBox moov;

        public Movie(FileTypeBox ftyp, MovieBox moov) {
            this.ftyp = ftyp;
            this.moov = moov;
        }

        public FileTypeBox getFtyp() {
            return this.ftyp;
        }

        public MovieBox getMoov() {
            return this.moov;
        }
    }
}