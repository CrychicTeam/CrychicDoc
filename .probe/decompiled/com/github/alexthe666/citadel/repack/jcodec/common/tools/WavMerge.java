package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.codecs.wav.WavHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class WavMerge {

    public static void main1(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("wavmerge <output wav> <input wav> .... <input wav>");
            System.exit(-1);
        }
        File out = new File(args[0]);
        File[] ins = new File[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            ins[i - 1] = new File(args[i]);
        }
        merge(out, ins);
    }

    // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public static void merge(File result, File[] src) throws IOException {
        WritableByteChannel out = null;
        ReadableByteChannel[] inputs = new ReadableByteChannel[src.length];
        WavHeader[] headers = new WavHeader[src.length];
        ByteBuffer[] ins = new ByteBuffer[src.length];
        boolean var17 = false;
        try {
            var17 = true;
            int sampleSize = -1;
            for (int outb = 0; outb < src.length; outb++) {
                inputs[outb] = NIOUtils.readableChannel(src[outb]);
                WavHeader hdr = WavHeader.readChannel(inputs[outb]);
                if (sampleSize != -1 && sampleSize != hdr.fmt.bitsPerSample) {
                    throw new RuntimeException("Input files have different sample sizes");
                }
                sampleSize = hdr.fmt.bitsPerSample;
                headers[outb] = hdr;
                ins[outb] = ByteBuffer.allocate(hdr.getFormat().framesToBytes(4096));
            }
            ByteBuffer outb = ByteBuffer.allocate(headers[0].getFormat().framesToBytes(4096) * src.length);
            WavHeader newHeader = WavHeader.multiChannelWav(headers);
            out = NIOUtils.writableChannel(result);
            newHeader.write(out);
            boolean readOnce = true;
            while (true) {
                readOnce = false;
                for (int i = 0; i < ins.length; i++) {
                    if (inputs[i] != null) {
                        ins[i].clear();
                        if (inputs[i].read(ins[i]) == -1) {
                            NIOUtils.closeQuietly(inputs[i]);
                            inputs[i] = null;
                        } else {
                            readOnce = true;
                        }
                        ins[i].flip();
                    }
                }
                if (!readOnce) {
                    var17 = false;
                    break;
                }
                outb.clear();
                AudioUtil.interleave(headers[0].getFormat(), ins, outb);
                outb.flip();
                out.write(outb);
            }
        } finally {
            if (var17) {
                IOUtils.closeQuietly(out);
                for (ReadableByteChannel inputStream : inputs) {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }
        IOUtils.closeQuietly(out);
        for (ReadableByteChannel inputStream : inputs) {
            IOUtils.closeQuietly(inputStream);
        }
    }
}