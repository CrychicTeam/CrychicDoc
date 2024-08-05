package com.github.alexthe666.citadel.repack.jcodec.containers.mps.psi;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPSUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MTSStreamType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PMTSection extends PSISection {

    private int pcrPid;

    private PMTSection.Tag[] tags;

    private PMTSection.PMTStream[] streams;

    public PMTSection(PSISection psi, int pcrPid, PMTSection.Tag[] tags, PMTSection.PMTStream[] streams) {
        super(psi.tableId, psi.specificId, psi.versionNumber, psi.currentNextIndicator, psi.sectionNumber, psi.lastSectionNumber);
        this.pcrPid = pcrPid;
        this.tags = tags;
        this.streams = streams;
    }

    public int getPcrPid() {
        return this.pcrPid;
    }

    public PMTSection.Tag[] getTags() {
        return this.tags;
    }

    public PMTSection.PMTStream[] getStreams() {
        return this.streams;
    }

    public static PMTSection parsePMT(ByteBuffer data) {
        PSISection psi = PSISection.parsePSI(data);
        int w1 = data.getShort() & '\uffff';
        int pcrPid = w1 & 8191;
        int w2 = data.getShort() & '\uffff';
        int programInfoLength = w2 & 4095;
        List<PMTSection.Tag> tags = parseTags(NIOUtils.read(data, programInfoLength));
        List<PMTSection.PMTStream> streams = new ArrayList();
        while (data.remaining() > 4) {
            int streamType = data.get() & 255;
            int wn = data.getShort() & '\uffff';
            int elementaryPid = wn & 8191;
            int wn1 = data.getShort() & '\uffff';
            int esInfoLength = wn1 & 4095;
            ByteBuffer read = NIOUtils.read(data, esInfoLength);
            streams.add(new PMTSection.PMTStream(streamType, elementaryPid, MPSUtils.parseDescriptors(read)));
        }
        return new PMTSection(psi, pcrPid, (PMTSection.Tag[]) tags.toArray(new PMTSection.Tag[0]), (PMTSection.PMTStream[]) streams.toArray(new PMTSection.PMTStream[0]));
    }

    static List<PMTSection.Tag> parseTags(ByteBuffer bb) {
        List<PMTSection.Tag> tags = new ArrayList();
        while (bb.hasRemaining()) {
            int tag = bb.get();
            int tagLen = bb.get();
            tags.add(new PMTSection.Tag(tag, NIOUtils.read(bb, tagLen)));
        }
        return tags;
    }

    public static class PMTStream {

        private int streamTypeTag;

        private int pid;

        private List<MPSUtils.MPEGMediaDescriptor> descriptors;

        private MTSStreamType streamType;

        public PMTStream(int streamTypeTag, int pid, List<MPSUtils.MPEGMediaDescriptor> descriptors) {
            this.streamTypeTag = streamTypeTag;
            this.pid = pid;
            this.descriptors = descriptors;
            this.streamType = MTSStreamType.fromTag(streamTypeTag);
        }

        public int getStreamTypeTag() {
            return this.streamTypeTag;
        }

        public MTSStreamType getStreamType() {
            return this.streamType;
        }

        public int getPid() {
            return this.pid;
        }

        public List<MPSUtils.MPEGMediaDescriptor> getDesctiptors() {
            return this.descriptors;
        }
    }

    public static class Tag {

        private int tag;

        private ByteBuffer content;

        public Tag(int tag, ByteBuffer content) {
            this.tag = tag;
            this.content = content;
        }

        public int getTag() {
            return this.tag;
        }

        public ByteBuffer getContent() {
            return this.content;
        }
    }
}