package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntIntMap;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PATSection;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PMTSection;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class MTSPktDump {

    public static void main1(String[] args) throws IOException {
        MainUtils.Cmd cmd = MainUtils.parseArguments(args, new MainUtils.Flag[0]);
        if (cmd.args.length < 1) {
            MainUtils.printHelpNoFlags("file name");
        } else {
            ReadableByteChannel ch = null;
            try {
                ch = NIOUtils.readableChannel(new File(cmd.args[0]));
                dumpTSPackets(ch);
            } finally {
                NIOUtils.closeQuietly(ch);
            }
        }
    }

    private static void dumpTSPackets(ReadableByteChannel _in) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(192512);
        while (_in.read(buf) != -1) {
            buf.flip();
            buf.limit(buf.limit() / 188 * 188);
            int pmtPid = -1;
            for (int pkt = 0; buf.hasRemaining(); pkt++) {
                ByteBuffer tsBuf = NIOUtils.read(buf, 188);
                Preconditions.checkState(71 == (tsBuf.get() & 255));
                int guidFlags = (tsBuf.get() & 255) << 8 | tsBuf.get() & 255;
                int guid = guidFlags & 8191;
                int payloadStart = guidFlags >> 14 & 1;
                int b0 = tsBuf.get() & 255;
                int counter = b0 & 15;
                if ((b0 & 32) != 0) {
                    NIOUtils.skip(tsBuf, tsBuf.get() & 255);
                }
                System.out.print("#" + pkt + "[guid: " + guid + ", cnt: " + counter + ", start: " + (payloadStart == 1 ? "y" : "-"));
                if (guid != 0 && guid != pmtPid) {
                    System.out.print("]: " + tsBuf.remaining());
                } else {
                    System.out.print(", PSI]: ");
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
                    }
                }
                System.out.println();
            }
            buf.clear();
        }
    }

    private static void printPat(PATSection pat) {
        IntIntMap programs = pat.getPrograms();
        System.out.print("PAT: ");
        int[] keys = programs.keys();
        for (int i : keys) {
            System.out.print(i + ":" + programs.get(i) + ", ");
        }
    }

    private static void printPmt(PMTSection pmt) {
        System.out.print("PMT: ");
        for (PMTSection.PMTStream pmtStream : pmt.getStreams()) {
            System.out.print(pmtStream.getPid() + ":" + pmtStream.getStreamTypeTag() + ", ");
        }
    }
}