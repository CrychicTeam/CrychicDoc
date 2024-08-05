package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntIntMap;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PATSection;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PSISection;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class MTSReplacePid extends MTSUtils.TSReader {

    private Set<Integer> pmtPids = new HashSet();

    private IntIntMap replaceSpec;

    public MTSReplacePid(IntIntMap replaceSpec) {
        super(true);
        this.replaceSpec = replaceSpec;
    }

    @Override
    public boolean onPkt(int guid, boolean payloadStart, ByteBuffer tsBuf, long filePos, boolean sectionSyntax, ByteBuffer fullPkt) {
        if (sectionSyntax) {
            this.replaceRefs(this.replaceSpec, guid, tsBuf, this.pmtPids);
        } else {
            System.out.print("TS ");
            ByteBuffer buf = fullPkt.duplicate();
            short tsFlags = buf.getShort(buf.position() + 1);
            buf.putShort(buf.position() + 1, (short) (this.replacePid(this.replaceSpec, tsFlags & 8191) | tsFlags & -8192));
        }
        return true;
    }

    private static IntIntMap parseReplaceSpec(String spec) {
        IntIntMap map = new IntIntMap();
        for (String pidPair : spec.split(",")) {
            String[] pidPairParsed = pidPair.split(":");
            map.put(Integer.parseInt(pidPairParsed[0]), Integer.parseInt(pidPairParsed[1]));
        }
        return map;
    }

    private void replaceRefs(IntIntMap replaceSpec, int guid, ByteBuffer buf, Set<Integer> pmtPids) {
        if (guid == 0) {
            PATSection pat = PATSection.parsePAT(buf);
            for (int pids : pat.getPrograms().values()) {
                pmtPids.add(pids);
            }
        } else if (pmtPids.contains(guid)) {
            System.out.println(MainUtils.bold("PMT"));
            PSISection.parsePSI(buf);
            buf.getShort();
            NIOUtils.skip(buf, buf.getShort() & 4095);
            while (buf.remaining() > 4) {
                byte streamType = buf.get();
                MTSStreamType fromTag = MTSStreamType.fromTag(streamType);
                System.out.print((fromTag == null ? "UNKNOWN" : fromTag) + "(" + String.format("0x%02x", streamType) + "):\t");
                int wn = buf.getShort() & '\uffff';
                int wasPid = wn & 8191;
                int elementaryPid = this.replacePid(replaceSpec, wasPid);
                buf.putShort(buf.position() - 2, (short) (elementaryPid & 8191 | wn & -8192));
                NIOUtils.skip(buf, buf.getShort() & 4095);
            }
        }
    }

    private int replacePid(IntIntMap replaceSpec, int pid) {
        int newPid = pid;
        if (replaceSpec.contains(pid)) {
            newPid = replaceSpec.get(pid);
        }
        System.out.println("[" + pid + "->" + newPid + "]");
        return newPid;
    }

    public static void main1(String[] args) throws IOException {
        MainUtils.Cmd cmd = MainUtils.parseArguments(args, new MainUtils.Flag[0]);
        if (cmd.args.length < 2) {
            MainUtils.printHelpNoFlags("pid_from:pid_to,[pid_from:pid_to...]", "file");
        } else {
            IntIntMap replaceSpec = parseReplaceSpec(cmd.getArg(0));
            SeekableByteChannel ch = null;
            try {
                ch = NIOUtils.rwChannel(new File(cmd.getArg(1)));
                new MTSReplacePid(replaceSpec).readTsFile(ch);
            } finally {
                NIOUtils.closeQuietly(ch);
            }
        }
    }
}