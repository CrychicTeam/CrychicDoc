package com.github.alexthe666.citadel.repack.jaad.spi.javasound;

import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.adts.ADTSDemultiplexer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.AudioFileReader;

public class AACAudioFileReader extends AudioFileReader {

    public static final Type AAC = new Type("AAC", "aac");

    public static final Type MP4 = new Type("MP4", "mp4");

    private static final Encoding AAC_ENCODING = new Encoding("AAC");

    public AudioFileFormat getAudioFileFormat(InputStream in) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat var2;
        try {
            if (!in.markSupported()) {
                in = new BufferedInputStream(in);
            }
            in.mark(4);
            var2 = this.getAudioFileFormat(in, -1);
        } finally {
            in.reset();
        }
        return var2;
    }

    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream in = url.openStream();
        AudioFileFormat var3;
        try {
            var3 = this.getAudioFileFormat(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return var3;
    }

    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        InputStream in = null;
        AudioFileFormat var4;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            in.mark(1000);
            AudioFileFormat aff = this.getAudioFileFormat(in, (int) file.length());
            in.reset();
            var4 = aff;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return var4;
    }

    private AudioFileFormat getAudioFileFormat(InputStream in, int mediaLength) throws UnsupportedAudioFileException, IOException {
        byte[] head = new byte[12];
        in.read(head);
        boolean canHandle = false;
        if (new String(head, 4, 4).equals("ftyp")) {
            canHandle = true;
        } else if (head[0] == 82 && head[1] == 73 && head[2] == 70 && head[3] == 70 && head[8] == 87 && head[9] == 65 && head[10] == 86 && head[11] == 69) {
            canHandle = false;
        } else if (head[0] == 46 && head[1] == 115 && head[2] == 110 && head[3] == 100) {
            canHandle = false;
        } else if (head[0] == 70 && head[1] == 79 && head[2] == 82 && head[3] == 77 && head[8] == 65 && head[9] == 73 && head[10] == 70 && head[11] == 70) {
            canHandle = false;
        } else if (head[0] == 77 | head[0] == 109 && head[1] == 65 | head[1] == 97 && head[2] == 67 | head[2] == 99) {
            canHandle = false;
        } else if (head[0] == 70 | head[0] == 102 && head[1] == 76 | head[1] == 108 && head[2] == 65 | head[2] == 97 && head[3] == 67 | head[3] == 99) {
            canHandle = false;
        } else if (head[0] == 73 | head[0] == 105 && head[1] == 67 | head[1] == 99 && head[2] == 89 | head[2] == 121) {
            canHandle = false;
        } else if (head[0] == 79 | head[0] == 111 && head[1] == 71 | head[1] == 103 && head[2] == 71 | head[2] == 103) {
            canHandle = false;
        } else {
            new BitStream(head);
            try {
                new ADTSDemultiplexer(in);
                canHandle = true;
            } catch (Exception var7) {
                canHandle = false;
            }
        }
        if (canHandle) {
            AudioFormat format = new AudioFormat(AAC_ENCODING, -1.0F, -1, mediaLength, -1, -1.0F, true);
            return new AudioFileFormat(AAC, format, -1);
        } else {
            throw new UnsupportedAudioFileException();
        }
    }

    public AudioInputStream getAudioInputStream(InputStream in) throws UnsupportedAudioFileException, IOException {
        try {
            if (!in.markSupported()) {
                in = new BufferedInputStream(in);
            }
            in.mark(1000);
            AudioFileFormat aff = this.getAudioFileFormat(in, -1);
            in.reset();
            return new MP4AudioInputStream(in, aff.getFormat(), (long) aff.getFrameLength());
        } catch (UnsupportedAudioFileException var3) {
            in.reset();
            throw var3;
        } catch (IOException var4) {
            if (var4.getMessage().equals("movie does not contain any AAC track")) {
                throw new UnsupportedAudioFileException("movie does not contain any AAC track");
            } else {
                in.reset();
                throw var4;
            }
        }
    }

    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream in = url.openStream();
        try {
            return this.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException var4) {
            if (in != null) {
                in.close();
            }
            throw var4;
        } catch (IOException var5) {
            if (in != null) {
                in.close();
            }
            throw var5;
        }
    }

    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        InputStream in = new FileInputStream(file);
        try {
            return this.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException var4) {
            if (in != null) {
                in.close();
            }
            throw var4;
        } catch (IOException var5) {
            if (in != null) {
                in.close();
            }
            throw var5;
        }
    }
}