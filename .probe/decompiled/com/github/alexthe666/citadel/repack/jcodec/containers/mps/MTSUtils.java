package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PATSection;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PMTSection;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PSISection;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MTSUtils {

    @Deprecated
    public static int parsePAT(ByteBuffer data) {
        PATSection pat = PATSection.parsePAT(data);
        return pat.getPrograms().size() > 0 ? pat.getPrograms().values()[0] : -1;
    }

    @Deprecated
    public static PMTSection parsePMT(ByteBuffer data) {
        return PMTSection.parsePMT(data);
    }

    @Deprecated
    public static PSISection parseSection(ByteBuffer data) {
        return PSISection.parsePSI(data);
    }

    private static void parseEsInfo(ByteBuffer read) {
    }

    public static PMTSection.PMTStream[] getProgramGuids(File src) throws IOException {
        SeekableByteChannel ch = null;
        PMTSection.PMTStream[] var2;
        try {
            ch = NIOUtils.readableChannel(src);
            var2 = getProgramGuidsFromChannel(ch);
        } finally {
            NIOUtils.closeQuietly(ch);
        }
        return var2;
    }

    public static PMTSection.PMTStream[] getProgramGuidsFromChannel(SeekableByteChannel _in) throws IOException {
        MTSUtils.PMTExtractor ex = new MTSUtils.PMTExtractor();
        ex.readTsFile(_in);
        PMTSection pmt = ex.getPmt();
        return pmt.getStreams();
    }

    public static int getVideoPid(File src) throws IOException {
        for (PMTSection.PMTStream stream : getProgramGuids(src)) {
            if (stream.getStreamType().isVideo()) {
                return stream.getPid();
            }
        }
        throw new RuntimeException("No video stream");
    }

    public static int getAudioPid(File src) throws IOException {
        for (PMTSection.PMTStream stream : getProgramGuids(src)) {
            if (stream.getStreamType().isAudio()) {
                return stream.getPid();
            }
        }
        throw new RuntimeException("No audio stream");
    }

    public static int[] getMediaPidsFromChannel(SeekableByteChannel src) throws IOException {
        return filterMediaPids(getProgramGuidsFromChannel(src));
    }

    public static int[] getMediaPids(File src) throws IOException {
        return filterMediaPids(getProgramGuids(src));
    }

    private static int[] filterMediaPids(PMTSection.PMTStream[] programs) {
        IntArrayList result = IntArrayList.createIntArrayList();
        for (PMTSection.PMTStream stream : programs) {
            if (stream.getStreamType().isVideo() || stream.getStreamType().isAudio()) {
                result.add(stream.getPid());
            }
        }
        return result.toArray();
    }

    private static class PMTExtractor extends MTSUtils.TSReader {

        private int pmtGuid = -1;

        private PMTSection pmt;

        public PMTExtractor() {
            super(false);
        }

        @Override
        public boolean onPkt(int guid, boolean payloadStart, ByteBuffer tsBuf, long filePos, boolean sectionSyntax, ByteBuffer fullPkt) {
            if (guid == 0) {
                this.pmtGuid = MTSUtils.parsePAT(tsBuf);
            } else if (this.pmtGuid != -1 && guid == this.pmtGuid) {
                this.pmt = MTSUtils.parsePMT(tsBuf);
                return false;
            }
            return true;
        }

        public PMTSection getPmt() {
            return this.pmt;
        }
    }

    public abstract static class TSReader {

        private static final int TS_SYNC_MARKER = 71;

        private static final int TS_PKT_SIZE = 188;

        public static final int BUFFER_SIZE = 96256;

        private boolean flush;

        public TSReader(boolean flush) {
            this.flush = flush;
        }

        public void readTsFile(SeekableByteChannel ch) throws IOException {
            ch.setPosition(0L);
            ByteBuffer buf = ByteBuffer.allocate(96256);
            for (long pos = ch.position(); ch.read(buf) >= 188; pos = ch.position()) {
                long posRem = pos;
                buf.flip();
                while (buf.remaining() >= 188) {
                    ByteBuffer tsBuf = NIOUtils.read(buf, 188);
                    ByteBuffer fullPkt = tsBuf.duplicate();
                    pos += 188L;
                    Preconditions.checkState(71 == (tsBuf.get() & 255));
                    int guidFlags = (tsBuf.get() & 255) << 8 | tsBuf.get() & 255;
                    int guid = guidFlags & 8191;
                    int payloadStart = guidFlags >> 14 & 1;
                    int b0 = tsBuf.get() & 255;
                    int counter = b0 & 15;
                    if ((b0 & 32) != 0) {
                        NIOUtils.skip(tsBuf, tsBuf.get() & 255);
                    }
                    boolean sectionSyntax = payloadStart == 1 && (NIOUtils.getRel(tsBuf, NIOUtils.getRel(tsBuf, 0) + 2) & 128) == 128;
                    if (sectionSyntax) {
                        NIOUtils.skip(tsBuf, tsBuf.get() & 255);
                    }
                    if (!this.onPkt(guid, payloadStart == 1, tsBuf, pos - (long) tsBuf.remaining(), sectionSyntax, fullPkt)) {
                        return;
                    }
                }
                if (this.flush) {
                    buf.flip();
                    ch.setPosition(posRem);
                    ch.write(buf);
                }
                buf.clear();
            }
        }

        protected boolean onPkt(int guid, boolean payloadStart, ByteBuffer tsBuf, long filePos, boolean sectionSyntax, ByteBuffer fullPkt) {
            return true;
        }
    }
}