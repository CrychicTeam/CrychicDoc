package com.github.alexthe666.citadel.repack.jaad;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.Decoder;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleBuffer;
import com.github.alexthe666.citadel.repack.jaad.adts.ADTSDemultiplexer;
import com.github.alexthe666.citadel.repack.jaad.mp4.MP4Container;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.AudioTrack;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Frame;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Movie;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Track;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Play {

    private static final String USAGE = "usage:\nnet.sourceforge.jaad.Play [-mp4] <infile>\n\n\t-mp4\tinput file is in MP4 container format";

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                printUsage();
            }
            if (args[0].equals("-mp4")) {
                if (args.length < 2) {
                    printUsage();
                } else {
                    decodeMP4(args[1]);
                }
            } else {
                decodeAAC(args[0]);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.err.println("error while decoding: " + var2.toString());
        }
    }

    private static void printUsage() {
        System.out.println("usage:\nnet.sourceforge.jaad.Play [-mp4] <infile>\n\n\t-mp4\tinput file is in MP4 container format");
        System.exit(1);
    }

    private static void decodeMP4(String in) throws Exception {
        SourceDataLine line = null;
        try {
            MP4Container cont = new MP4Container(new RandomAccessFile(in, "r"));
            Movie movie = cont.getMovie();
            List<Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
            if (tracks.isEmpty()) {
                throw new Exception("movie does not contain any AAC track");
            }
            AudioTrack track = (AudioTrack) tracks.get(0);
            AudioFormat aufmt = new AudioFormat((float) track.getSampleRate(), track.getSampleSize(), track.getChannelCount(), true, true);
            line = AudioSystem.getSourceDataLine(aufmt);
            line.open();
            line.start();
            Decoder dec = new Decoder(track.getDecoderSpecificInfo());
            SampleBuffer buf = new SampleBuffer();
            while (track.hasMoreFrames()) {
                Frame frame = track.readNextFrame();
                try {
                    dec.decodeFrame(frame.getData(), buf);
                    byte[] b = buf.getData();
                    line.write(b, 0, b.length);
                } catch (AACException var15) {
                    var15.printStackTrace();
                }
            }
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }

    private static void decodeAAC(String in) throws Exception {
        if (in.startsWith("http:")) {
            decodeAAC(new URL(in).openStream());
        } else {
            decodeAAC(new FileInputStream(in));
        }
    }

    private static void decodeAAC(InputStream in) throws Exception {
        SourceDataLine line = null;
        try {
            ADTSDemultiplexer adts = new ADTSDemultiplexer(in);
            Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            SampleBuffer buf = new SampleBuffer();
            while (true) {
                byte[] b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (line == null) {
                    AudioFormat aufmt = new AudioFormat((float) buf.getSampleRate(), buf.getBitsPerSample(), buf.getChannels(), true, true);
                    line = AudioSystem.getSourceDataLine(aufmt);
                    line.open();
                    line.start();
                }
                b = buf.getData();
                line.write(b, 0, b.length);
            }
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }
}