package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PATSection;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HLSRelocatePMT {

    private static final int TS_START_CODE = 71;

    private static final int CHUNK_SIZE_PKT = 1024;

    private static final int TS_PKT_SIZE = 188;

    public static void main1(String[] args) throws IOException {
        MainUtils.Cmd cmd = MainUtils.parseArguments(args, new MainUtils.Flag[0]);
        if (cmd.args.length < 2) {
            MainUtils.printHelpNoFlags("file _in", "file out");
        } else {
            ReadableByteChannel _in = null;
            WritableByteChannel out = null;
            try {
                _in = NIOUtils.readableChannel(new File(cmd.args[0]));
                out = NIOUtils.writableChannel(new File(cmd.args[1]));
                System.err.println("Processed: " + replocatePMT(_in, out) + " packets.");
            } finally {
                NIOUtils.closeQuietly(_in);
                NIOUtils.closeQuietly(out);
            }
        }
    }

    private static int replocatePMT(ReadableByteChannel _in, WritableByteChannel out) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(192512);
        Set<Integer> pmtPids = new HashSet();
        List<ByteBuffer> held = new ArrayList();
        ByteBuffer patPkt = null;
        ByteBuffer pmtPkt = null;
        int totalPkt;
        for (totalPkt = 0; _in.read(buf) != -1; buf.clear()) {
            buf.flip();
            buf.limit(buf.limit() / 188 * 188);
            while (buf.hasRemaining()) {
                ByteBuffer pkt = NIOUtils.read(buf, 188);
                ByteBuffer pktRead = pkt.duplicate();
                Preconditions.checkState(71 == (pktRead.get() & 255));
                totalPkt++;
                int guidFlags = (pktRead.get() & 255) << 8 | pktRead.get() & 255;
                int guid = guidFlags & 8191;
                int payloadStart = guidFlags >> 14 & 1;
                int b0 = pktRead.get() & 255;
                int counter = b0 & 15;
                if ((b0 & 32) != 0) {
                    NIOUtils.skip(pktRead, pktRead.get() & 255);
                }
                if (guid != 0 && !pmtPids.contains(guid)) {
                    if (pmtPkt == null) {
                        held.add(pkt);
                    } else {
                        out.write(pkt);
                    }
                } else {
                    if (payloadStart == 1) {
                        NIOUtils.skip(pktRead, pktRead.get() & 255);
                    }
                    if (guid == 0) {
                        patPkt = pkt;
                        PATSection pat = PATSection.parsePAT(pktRead);
                        int[] values = pat.getPrograms().values();
                        for (int i = 0; i < values.length; i++) {
                            int pmtPid = values[i];
                            pmtPids.add(pmtPid);
                        }
                    } else if (pmtPids.contains(guid)) {
                        pmtPkt = pkt;
                        out.write(patPkt);
                        out.write(pkt);
                        for (ByteBuffer heldPkt : held) {
                            out.write(heldPkt);
                        }
                        held.clear();
                    }
                }
            }
        }
        return totalPkt;
    }
}