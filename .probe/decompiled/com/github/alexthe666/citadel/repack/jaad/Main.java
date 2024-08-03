package com.github.alexthe666.citadel.repack.jaad;

import com.github.alexthe666.citadel.repack.jaad.aac.Decoder;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleBuffer;
import com.github.alexthe666.citadel.repack.jaad.adts.ADTSDemultiplexer;
import com.github.alexthe666.citadel.repack.jaad.mp4.MP4Container;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.AudioTrack;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Frame;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Movie;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Track;
import com.github.alexthe666.citadel.repack.jaad.util.wav.WaveFileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class Main {

    private static final String USAGE = "usage:\nnet.sourceforge.jaad.Main [-mp4] <infile> <outfile>\n\n\t-mp4\tinput file is in MP4 container format";

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                printUsage();
            }
            if (args[0].equals("-mp4")) {
                if (args.length < 3) {
                    printUsage();
                } else {
                    decodeMP4(args[1], args[2]);
                }
            } else {
                decodeAAC(args[0], args[1]);
            }
        } catch (Exception var2) {
            System.err.println("error while decoding: " + var2.toString());
        }
    }

    private static void printUsage() {
        System.out.println("usage:\nnet.sourceforge.jaad.Main [-mp4] <infile> <outfile>\n\n\t-mp4\tinput file is in MP4 container format");
        System.exit(1);
    }

    private static void decodeMP4(String in, String out) throws Exception {
        WaveFileWriter wav = null;
        try {
            MP4Container cont = new MP4Container(new RandomAccessFile(in, "r"));
            Movie movie = cont.getMovie();
            List<Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
            if (tracks.isEmpty()) {
                throw new Exception("movie does not contain any AAC track");
            }
            AudioTrack track = (AudioTrack) tracks.get(0);
            wav = new WaveFileWriter(new File(out), track.getSampleRate(), track.getChannelCount(), track.getSampleSize());
            Decoder dec = new Decoder(track.getDecoderSpecificInfo());
            SampleBuffer buf = new SampleBuffer();
            while (track.hasMoreFrames()) {
                Frame frame = track.readNextFrame();
                dec.decodeFrame(frame.getData(), buf);
                wav.write(buf.getData());
            }
        } finally {
            if (wav != null) {
                wav.close();
            }
        }
    }

    private static void decodeAAC(String in, String out) throws IOException {
        WaveFileWriter wav = null;
        try {
            ADTSDemultiplexer adts = new ADTSDemultiplexer(new FileInputStream(in));
            Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            SampleBuffer buf = new SampleBuffer();
            while (true) {
                byte[] b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (wav == null) {
                    wav = new WaveFileWriter(new File(out), buf.getSampleRate(), buf.getChannels(), buf.getBitsPerSample());
                }
                wav.write(buf.getData());
            }
        } finally {
            if (wav != null) {
                wav.close();
            }
        }
    }
}