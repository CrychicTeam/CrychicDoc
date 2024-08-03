package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.codecs.wav.WavHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public class WavSplit {

    public static final MainUtils.Flag FLAG_PATTERN = MainUtils.Flag.flag("pattern", "p", "Output file name pattern, i.e. out%02d.wav");

    private static final MainUtils.Flag[] ALL_FLAGS = new MainUtils.Flag[] { FLAG_PATTERN };

    public static void main1(String[] args) throws Exception {
        MainUtils.Cmd cmd = MainUtils.parseArguments(args, ALL_FLAGS);
        if (cmd.argsLength() < 1) {
            MainUtils.printHelp(ALL_FLAGS, Arrays.asList("filename.wav"));
            System.exit(-1);
        }
        File s = new File(args[0]);
        String pattern = cmd.getStringFlagD(FLAG_PATTERN, "c%02d.wav");
        WavHeader wavHeader = WavHeader.read(s);
        System.out.println("WAV: " + wavHeader.getFormat());
        Preconditions.checkState(2 == wavHeader.fmt.numChannels);
        int dataOffset = wavHeader.dataOffset;
        FileChannelWrapper is = NIOUtils.readableChannel(s);
        is.setPosition((long) dataOffset);
        int channels = wavHeader.getFormat().getChannels();
        SeekableByteChannel[] out = new SeekableByteChannel[channels];
        for (int i = 0; i < channels; i++) {
            out[i] = NIOUtils.writableChannel(new File(s.getParentFile(), String.format(pattern, i)));
            WavHeader.copyWithChannels(wavHeader, 1).write(out[i]);
        }
        copy(wavHeader.getFormat(), is, out);
        for (int i = 0; i < channels; i++) {
            out[i].close();
        }
    }

    private static void copy(AudioFormat format, ReadableByteChannel is, SeekableByteChannel[] out) throws IOException {
        ByteBuffer[] outs = new ByteBuffer[out.length];
        for (int i = 0; i < out.length; i++) {
            outs[i] = ByteBuffer.allocate(format.framesToBytes(4096));
        }
        ByteBuffer inb = ByteBuffer.allocate(format.framesToBytes(4096) * out.length);
        while (is.read(inb) != -1) {
            inb.flip();
            AudioUtil.deinterleave(format, inb, outs);
            inb.clear();
            for (int i = 0; i < out.length; i++) {
                outs[i].flip();
                out[i].write(outs[i]);
                outs[i].clear();
            }
        }
    }
}