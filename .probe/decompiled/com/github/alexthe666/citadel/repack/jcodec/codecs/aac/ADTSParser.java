package com.github.alexthe666.citadel.repack.jcodec.codecs.aac;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class ADTSParser {

    public static ByteBuffer adtsToStreamInfo(ADTSParser.Header hdr) {
        ByteBuffer si = ByteBuffer.allocate(2);
        BitWriter wr = new BitWriter(si);
        wr.writeNBit(hdr.getObjectType(), 5);
        wr.writeNBit(hdr.getSamplingIndex(), 4);
        wr.writeNBit(hdr.getChanConfig(), 4);
        wr.flush();
        si.clear();
        return si;
    }

    public static ADTSParser.Header read(ByteBuffer data) {
        ByteBuffer dup = data.duplicate();
        BitReader br = BitReader.createBitReader(dup);
        if (br.readNBit(12) != 4095) {
            return null;
        } else {
            int id = br.read1Bit();
            int layer = br.readNBit(2);
            int crc_abs = br.read1Bit();
            int aot = br.readNBit(2);
            int sr = br.readNBit(4);
            int pb = br.read1Bit();
            int ch = br.readNBit(3);
            int origCopy = br.read1Bit();
            int home = br.read1Bit();
            int copy = br.read1Bit();
            int copyStart = br.read1Bit();
            int size = br.readNBit(13);
            if (size < 7) {
                return null;
            } else {
                int buffer = br.readNBit(11);
                int rdb = br.readNBit(2);
                br.stop();
                data.position(dup.position());
                return new ADTSParser.Header(aot + 1, ch, crc_abs, rdb + 1, sr, size);
            }
        }
    }

    public static ByteBuffer write(ADTSParser.Header header, ByteBuffer buf) {
        ByteBuffer data = buf.duplicate();
        BitWriter br = new BitWriter(data);
        br.writeNBit(4095, 12);
        br.write1Bit(1);
        br.writeNBit(0, 2);
        br.write1Bit(header.getCrcAbsent());
        br.writeNBit(header.getObjectType() - 1, 2);
        br.writeNBit(header.getSamplingIndex(), 4);
        br.write1Bit(0);
        br.writeNBit(header.getChanConfig(), 3);
        br.write1Bit(0);
        br.write1Bit(0);
        br.write1Bit(0);
        br.write1Bit(0);
        br.writeNBit(header.getSize(), 13);
        br.writeNBit(0, 11);
        br.writeNBit(header.getNumAACFrames() - 1, 2);
        br.flush();
        data.flip();
        return data;
    }

    public static class Header {

        private int objectType;

        private int chanConfig;

        private int crcAbsent;

        private int numAACFrames;

        private int samplingIndex;

        private int samples;

        private int size;

        public Header(int object_type, int chanConfig, int crcAbsent, int numAACFrames, int samplingIndex, int size) {
            this.objectType = object_type;
            this.chanConfig = chanConfig;
            this.crcAbsent = crcAbsent;
            this.numAACFrames = numAACFrames;
            this.samplingIndex = samplingIndex;
            this.size = size;
        }

        public int getObjectType() {
            return this.objectType;
        }

        public int getChanConfig() {
            return this.chanConfig;
        }

        public int getCrcAbsent() {
            return this.crcAbsent;
        }

        public int getNumAACFrames() {
            return this.numAACFrames;
        }

        public int getSamplingIndex() {
            return this.samplingIndex;
        }

        public int getSamples() {
            return this.samples;
        }

        public int getSize() {
            return this.size;
        }

        public int getSampleRate() {
            return AACConts.AAC_SAMPLE_RATES[this.samplingIndex];
        }
    }
}