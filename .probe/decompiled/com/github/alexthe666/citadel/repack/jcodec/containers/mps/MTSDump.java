package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.IntIntMap;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PATSection;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PMTSection;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MTSDump extends MPSDump {

    private static final MainUtils.Flag DUMP_FROM = MainUtils.Flag.flag("dump-from", null, "Stop reading at timestamp");

    private static final MainUtils.Flag STOP_AT = MainUtils.Flag.flag("stop-at", null, "Start dumping from timestamp");

    private static final MainUtils.Flag[] ALL_FLAGS = new MainUtils.Flag[] { DUMP_FROM, STOP_AT };

    private int guid;

    private ByteBuffer buf = ByteBuffer.allocate(192512);

    private ByteBuffer tsBuf = ByteBuffer.allocate(188);

    private int tsNo;

    private int globalPayload;

    private int[] payloads;

    private int[] nums;

    private int[] prevPayloads;

    private int[] prevNums;

    public MTSDump(ReadableByteChannel ch, int targetGuid) {
        super(ch);
        this.guid = targetGuid;
        this.buf.position(this.buf.limit());
        this.tsBuf.position(this.tsBuf.limit());
    }

    public static void main2(String[] args) throws IOException {
        ReadableByteChannel ch = null;
        try {
            MainUtils.Cmd cmd = MainUtils.parseArguments(args, ALL_FLAGS);
            if (cmd.args.length < 1) {
                MainUtils.printHelp(ALL_FLAGS, Arrays.asList("file name", "guid"));
                return;
            }
            if (cmd.args.length != 1) {
                ch = NIOUtils.readableChannel(new File(cmd.args[0]));
                Long dumpAfterPts = cmd.getLongFlag(DUMP_FROM);
                Long stopPts = cmd.getLongFlag(STOP_AT);
                new MTSDump(ch, Integer.parseInt(cmd.args[1])).dump(dumpAfterPts, stopPts);
                return;
            }
            System.out.println("MTS programs:");
            dumpProgramPids(NIOUtils.readableChannel(new File(cmd.args[0])));
        } finally {
            NIOUtils.closeQuietly(ch);
        }
    }

    private static void dumpProgramPids(ReadableByteChannel readableFileChannel) throws IOException {
        Set<Integer> pids = new HashSet();
        ByteBuffer buf = ByteBuffer.allocate(1925120);
        readableFileChannel.read(buf);
        buf.flip();
        buf.limit(buf.limit() - buf.limit() % 188);
        int pmtPid = -1;
        while (buf.hasRemaining()) {
            ByteBuffer tsBuf = NIOUtils.read(buf, 188);
            Preconditions.checkState(71 == (tsBuf.get() & 255));
            int guidFlags = (tsBuf.get() & 255) << 8 | tsBuf.get() & 255;
            int guid = guidFlags & 8191;
            System.out.println(guid);
            if (guid != 0) {
                pids.add(guid);
            }
            if (guid == 0 || guid == pmtPid) {
                int payloadStart = guidFlags >> 14 & 1;
                int b0 = tsBuf.get() & 255;
                int counter = b0 & 15;
                int payloadOff = 0;
                if ((b0 & 32) != 0) {
                    NIOUtils.skip(tsBuf, tsBuf.get() & 255);
                }
                if (payloadStart == 1) {
                    NIOUtils.skip(tsBuf, tsBuf.get() & 255);
                }
                if (guid == 0) {
                    PATSection pat = PATSection.parsePAT(tsBuf);
                    IntIntMap programs = pat.getPrograms();
                    pmtPid = programs.values()[0];
                    printPat(pat);
                } else if (guid == pmtPid) {
                    PMTSection pmt = PMTSection.parsePMT(tsBuf);
                    printPmt(pmt);
                    return;
                }
            }
        }
        for (Integer pid : pids) {
            System.out.println(pid);
        }
    }

    private static void printPat(PATSection pat) {
        IntIntMap programs = pat.getPrograms();
        System.out.print("PAT: ");
        int[] keys = programs.keys();
        for (int i : keys) {
            System.out.print(i + ":" + programs.get(i) + ", ");
        }
        System.out.println();
    }

    private static void printPmt(PMTSection pmt) {
        System.out.print("PMT: ");
        for (PMTSection.PMTStream pmtStream : pmt.getStreams()) {
            System.out.print(pmtStream.getPid() + ":" + pmtStream.getStreamTypeTag() + ", ");
            for (MPSUtils.MPEGMediaDescriptor descriptor : pmtStream.getDesctiptors()) {
                System.out.println(Platform.toJSON(descriptor));
            }
        }
        System.out.println();
    }

    @Override
    protected void logPes(PESPacket pkt, int hdrSize, ByteBuffer payload) {
        System.out.println(pkt.streamId + "(" + (pkt.streamId >= 224 ? "video" : "audio") + ") [ts#" + this.mapPos(pkt.pos) + ", " + (payload.remaining() + hdrSize) + "b], pts: " + pkt.pts + ", dts: " + pkt.dts);
    }

    private int mapPos(long pos) {
        int left = this.globalPayload;
        for (int i = this.payloads.length - 1; i >= 0; i--) {
            left -= this.payloads[i];
            if ((long) left <= pos) {
                return this.nums[i];
            }
        }
        if (this.prevPayloads != null) {
            for (int ix = this.prevPayloads.length - 1; ix >= 0; ix--) {
                left -= this.prevPayloads[ix];
                if ((long) left <= pos) {
                    return this.prevNums[ix];
                }
            }
        }
        return -1;
    }

    @Override
    public int fillBuffer(ByteBuffer dst) throws IOException {
        IntArrayList payloads = IntArrayList.createIntArrayList();
        IntArrayList nums = IntArrayList.createIntArrayList();
        int remaining = dst.remaining();
        try {
            dst.put(NIOUtils.read(this.tsBuf, Math.min(dst.remaining(), this.tsBuf.remaining())));
            while (dst.hasRemaining()) {
                if (!this.buf.hasRemaining()) {
                    ByteBuffer dub = this.buf.duplicate();
                    dub.clear();
                    int read = this.ch.read(dub);
                    if (read == -1) {
                        return dst.remaining() != remaining ? remaining - dst.remaining() : -1;
                    }
                    dub.flip();
                    dub.limit(dub.limit() - dub.limit() % 188);
                    this.buf = dub;
                }
                this.tsBuf = NIOUtils.read(this.buf, 188);
                Preconditions.checkState(71 == (this.tsBuf.get() & 255));
                this.tsNo++;
                int guidFlags = (this.tsBuf.get() & 255) << 8 | this.tsBuf.get() & 255;
                int guid = guidFlags & 8191;
                if (guid == this.guid) {
                    int payloadStart = guidFlags >> 14 & 1;
                    int b0 = this.tsBuf.get() & 255;
                    int counter = b0 & 15;
                    if ((b0 & 32) != 0) {
                        NIOUtils.skip(this.tsBuf, this.tsBuf.get() & 255);
                    }
                    this.globalPayload = this.globalPayload + this.tsBuf.remaining();
                    payloads.add(this.tsBuf.remaining());
                    nums.add(this.tsNo - 1);
                    dst.put(NIOUtils.read(this.tsBuf, Math.min(dst.remaining(), this.tsBuf.remaining())));
                }
            }
            return remaining - dst.remaining();
        } finally {
            this.prevPayloads = this.payloads;
            this.payloads = payloads.toArray();
            this.prevNums = this.nums;
            this.nums = nums.toArray();
        }
    }
}