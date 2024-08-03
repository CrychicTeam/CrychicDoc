package com.github.alexthe666.citadel.repack.jaad;

import com.github.alexthe666.citadel.repack.jaad.aac.Decoder;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleBuffer;
import com.github.alexthe666.citadel.repack.jaad.adts.ADTSDemultiplexer;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Radio {

    private static final String USAGE = "usage:\nnet.sourceforge.jaad.Radio <url>";

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                printUsage();
            } else {
                decode(args[0]);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.err.println("error while decoding: " + var2.toString());
        }
    }

    private static void printUsage() {
        System.out.println("usage:\nnet.sourceforge.jaad.Radio <url>");
        System.exit(1);
    }

    private static void decode(String arg) throws Exception {
        SampleBuffer buf = new SampleBuffer();
        SourceDataLine line = null;
        try {
            URI uri = new URI(arg);
            Socket sock = new Socket(uri.getHost(), uri.getPort() > 0 ? uri.getPort() : 80);
            PrintStream out = new PrintStream(sock.getOutputStream());
            String path = uri.getPath();
            if (path == null || path.equals("")) {
                path = "/";
            }
            if (uri.getQuery() != null) {
                path = path + "?" + uri.getQuery();
            }
            out.println("GET " + path + " HTTP/1.1");
            out.println("Host: " + uri.getHost());
            out.println();
            DataInputStream in = new DataInputStream(sock.getInputStream());
            String x;
            do {
                x = in.readLine();
            } while (x != null && !x.trim().equals(""));
            ADTSDemultiplexer adts = new ADTSDemultiplexer(in);
            AudioFormat aufmt = new AudioFormat((float) adts.getSampleFrequency(), 16, adts.getChannelCount(), true, true);
            Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            while (true) {
                byte[] b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (line != null && formatChanged(line.getFormat(), buf)) {
                    line.stop();
                    line.close();
                    line = null;
                    aufmt = new AudioFormat((float) buf.getSampleRate(), buf.getBitsPerSample(), buf.getChannels(), true, true);
                }
                if (line == null) {
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

    private static boolean formatChanged(AudioFormat af, SampleBuffer buf) {
        return af.getSampleRate() != (float) buf.getSampleRate() || af.getChannels() != buf.getChannels() || af.getSampleSizeInBits() != buf.getBitsPerSample() || af.isBigEndian() != buf.isBigEndian();
    }
}