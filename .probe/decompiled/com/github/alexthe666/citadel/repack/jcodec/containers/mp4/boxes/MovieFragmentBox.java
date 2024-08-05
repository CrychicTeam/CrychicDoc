package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class MovieFragmentBox extends NodeBox {

    private MovieBox moov;

    public MovieFragmentBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "moof";
    }

    public MovieBox getMovie() {
        return this.moov;
    }

    public void setMovie(MovieBox moov) {
        this.moov = moov;
    }

    public TrackFragmentBox[] getTracks() {
        return NodeBox.findAll(this, TrackFragmentBox.class, TrackFragmentBox.fourcc());
    }

    public int getSequenceNumber() {
        MovieFragmentHeaderBox mfhd = NodeBox.findFirst(this, MovieFragmentHeaderBox.class, MovieFragmentHeaderBox.fourcc());
        if (mfhd == null) {
            throw new RuntimeException("Corrupt movie fragment, no header atom found");
        } else {
            return mfhd.getSequenceNumber();
        }
    }

    public static MovieFragmentBox createMovieFragmentBox() {
        return new MovieFragmentBox(new Header(fourcc()));
    }
}