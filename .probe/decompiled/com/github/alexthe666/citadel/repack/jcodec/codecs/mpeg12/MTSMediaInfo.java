package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MTSUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi.PMTSection;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MTSMediaInfo {

    public List<MPSMediaInfo.MPEGTrackMetadata> getMediaInfo(File f) throws IOException {
        FileChannelWrapper ch = null;
        final List<PMTSection> pmtSections = new ArrayList();
        final Map<Integer, MPSMediaInfo> pids = new HashMap();
        final List<MPSMediaInfo.MPEGTrackMetadata> result = new ArrayList();
        try {
            ch = NIOUtils.readableChannel(f);
            (new MTSUtils.TSReader(false) {

                private ByteBuffer pmtBuffer;

                private int pmtPid = -1;

                private boolean pmtDone;

                @Override
                protected boolean onPkt(int guid, boolean payloadStart, ByteBuffer tsBuf, long filePos, boolean sectionSyntax, ByteBuffer fullPkt) {
                    if (guid == 0) {
                        this.pmtPid = MTSUtils.parsePAT(tsBuf);
                    } else if (guid == this.pmtPid && !this.pmtDone) {
                        if (this.pmtBuffer == null) {
                            this.pmtBuffer = ByteBuffer.allocate((tsBuf.duplicate().getInt() >> 8 & 1023) + 3);
                        } else if (this.pmtBuffer.hasRemaining()) {
                            NIOUtils.writeL(this.pmtBuffer, tsBuf, Math.min(this.pmtBuffer.remaining(), tsBuf.remaining()));
                        }
                        if (!this.pmtBuffer.hasRemaining()) {
                            this.pmtBuffer.flip();
                            PMTSection pmt = MTSUtils.parsePMT(this.pmtBuffer);
                            pmtSections.add(pmt);
                            PMTSection.PMTStream[] streams = pmt.getStreams();
                            for (int i = 0; i < streams.length; i++) {
                                PMTSection.PMTStream stream = streams[i];
                                if (!pids.containsKey(stream.getPid())) {
                                    pids.put(stream.getPid(), new MPSMediaInfo());
                                }
                            }
                            this.pmtDone = pmt.getSectionNumber() == pmt.getLastSectionNumber();
                            this.pmtBuffer = null;
                        }
                    } else if (pids.containsKey(guid)) {
                        try {
                            ((MPSMediaInfo) pids.get(guid)).analyseBuffer(tsBuf, filePos);
                        } catch (MPSMediaInfo.MediaInfoDone var12) {
                            result.addAll(((MPSMediaInfo) pids.get(guid)).getInfos());
                            pids.remove(guid);
                            if (pids.size() == 0) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }).readTsFile(ch);
        } finally {
            NIOUtils.closeQuietly(ch);
        }
        return result;
    }

    public static void main1(String[] args) throws IOException {
        for (MPSMediaInfo.MPEGTrackMetadata stream : new MTSMediaInfo().getMediaInfo(new File(args[0]))) {
            System.out.println(stream.codec);
        }
    }

    public static MTSMediaInfo extract(SeekableByteChannel input) {
        return null;
    }

    public MPSMediaInfo.MPEGTrackMetadata getVideoTrack() {
        return null;
    }

    public List<MPSMediaInfo.MPEGTrackMetadata> getAudioTracks() {
        return null;
    }
}