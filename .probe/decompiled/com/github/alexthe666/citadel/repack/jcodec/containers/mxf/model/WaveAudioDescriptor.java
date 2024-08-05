package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WaveAudioDescriptor extends GenericSoundEssenceDescriptor {

    private short blockAlign;

    private byte sequenceOffset;

    private int avgBps;

    private UL channelAssignment;

    private int peakEnvelopeVersion;

    private int peakEnvelopeFormat;

    private int pointsPerPeakValue;

    private int peakEnvelopeBlockSize;

    private int peakChannels;

    private int peakFrames;

    private ByteBuffer peakOfPeaksPosition;

    private ByteBuffer peakEnvelopeTimestamp;

    private ByteBuffer peakEnvelopeData;

    public WaveAudioDescriptor(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        super.read(tags);
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 15625:
                    this.avgBps = _bb.getInt();
                    break;
                case 15626:
                    this.blockAlign = _bb.getShort();
                    break;
                case 15627:
                    this.sequenceOffset = _bb.get();
                    break;
                case 15628:
                case 15629:
                case 15630:
                case 15631:
                case 15632:
                case 15633:
                case 15634:
                case 15635:
                case 15636:
                case 15637:
                case 15638:
                case 15639:
                case 15640:
                case 15641:
                case 15642:
                case 15643:
                case 15644:
                case 15645:
                case 15646:
                case 15647:
                case 15648:
                case 15649:
                case 15650:
                case 15651:
                case 15652:
                case 15653:
                case 15654:
                case 15655:
                case 15656:
                default:
                    System.out.println(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 15657:
                    this.peakEnvelopeVersion = _bb.getInt();
                    break;
                case 15658:
                    this.peakEnvelopeFormat = _bb.getInt();
                    break;
                case 15659:
                    this.pointsPerPeakValue = _bb.getInt();
                    break;
                case 15660:
                    this.peakEnvelopeBlockSize = _bb.getInt();
                    break;
                case 15661:
                    this.peakChannels = _bb.getInt();
                    break;
                case 15662:
                    this.peakFrames = _bb.getInt();
                    break;
                case 15663:
                    this.peakOfPeaksPosition = _bb;
                    break;
                case 15664:
                    this.peakEnvelopeTimestamp = _bb;
                    break;
                case 15665:
                    this.peakEnvelopeData = _bb;
                    break;
                case 15666:
                    this.channelAssignment = UL.read(_bb);
            }
            it.remove();
        }
    }

    public short getBlockAlign() {
        return this.blockAlign;
    }

    public byte getSequenceOffset() {
        return this.sequenceOffset;
    }

    public int getAvgBps() {
        return this.avgBps;
    }

    public UL getChannelAssignment() {
        return this.channelAssignment;
    }

    public int getPeakEnvelopeVersion() {
        return this.peakEnvelopeVersion;
    }

    public int getPeakEnvelopeFormat() {
        return this.peakEnvelopeFormat;
    }

    public int getPointsPerPeakValue() {
        return this.pointsPerPeakValue;
    }

    public int getPeakEnvelopeBlockSize() {
        return this.peakEnvelopeBlockSize;
    }

    public int getPeakChannels() {
        return this.peakChannels;
    }

    public int getPeakFrames() {
        return this.peakFrames;
    }

    public ByteBuffer getPeakOfPeaksPosition() {
        return this.peakOfPeaksPosition;
    }

    public ByteBuffer getPeakEnvelopeTimestamp() {
        return this.peakEnvelopeTimestamp;
    }

    public ByteBuffer getPeakEnvelopeData() {
        return this.peakEnvelopeData;
    }
}