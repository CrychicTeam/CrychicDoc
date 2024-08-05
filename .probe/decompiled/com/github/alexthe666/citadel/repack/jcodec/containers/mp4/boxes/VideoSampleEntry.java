package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.nio.ByteBuffer;

public class VideoSampleEntry extends SampleEntry {

    private short version;

    private short revision;

    private String vendor;

    private int temporalQual;

    private int spacialQual;

    private short width;

    private short height;

    private float hRes;

    private float vRes;

    private short frameCount;

    private String compressorName;

    private short depth;

    private short clrTbl;

    public static VideoSampleEntry videoSampleEntry(String fourcc, Size size, String encoderName) {
        return createVideoSampleEntry(new Header(fourcc), (short) 0, (short) 0, "jcod", 0, 768, (short) size.getWidth(), (short) size.getHeight(), 72L, 72L, (short) 1, encoderName != null ? encoderName : "jcodec", (short) 24, (short) 1, (short) -1);
    }

    public static VideoSampleEntry createVideoSampleEntry(Header atom, short version, short revision, String vendor, int temporalQual, int spacialQual, short width, short height, long hRes, long vRes, short frameCount, String compressorName, short depth, short drefInd, short clrTbl) {
        VideoSampleEntry e = new VideoSampleEntry(atom);
        e.drefInd = drefInd;
        e.version = version;
        e.revision = revision;
        e.vendor = vendor;
        e.temporalQual = temporalQual;
        e.spacialQual = spacialQual;
        e.width = width;
        e.height = height;
        e.hRes = (float) hRes;
        e.vRes = (float) vRes;
        e.frameCount = frameCount;
        e.compressorName = compressorName;
        e.depth = depth;
        e.clrTbl = clrTbl;
        return e;
    }

    public VideoSampleEntry(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.version = input.getShort();
        this.revision = input.getShort();
        this.vendor = NIOUtils.readString(input, 4);
        this.temporalQual = input.getInt();
        this.spacialQual = input.getInt();
        this.width = input.getShort();
        this.height = input.getShort();
        this.hRes = (float) input.getInt() / 65536.0F;
        this.vRes = (float) input.getInt() / 65536.0F;
        input.getInt();
        this.frameCount = input.getShort();
        this.compressorName = NIOUtils.readPascalStringL(input, 31);
        this.depth = input.getShort();
        this.clrTbl = input.getShort();
        this.parseExtensions(input);
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(this.version);
        out.putShort(this.revision);
        out.put(JCodecUtil2.asciiString(this.vendor), 0, 4);
        out.putInt(this.temporalQual);
        out.putInt(this.spacialQual);
        out.putShort(this.width);
        out.putShort(this.height);
        out.putInt((int) (this.hRes * 65536.0F));
        out.putInt((int) (this.vRes * 65536.0F));
        out.putInt(0);
        out.putShort(this.frameCount);
        NIOUtils.writePascalStringL(out, this.compressorName, 31);
        out.putShort(this.depth);
        out.putShort(this.clrTbl);
        this.writeExtensions(out);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float gethRes() {
        return this.hRes;
    }

    public float getvRes() {
        return this.vRes;
    }

    public long getFrameCount() {
        return (long) this.frameCount;
    }

    public String getCompressorName() {
        return this.compressorName;
    }

    public long getDepth() {
        return (long) this.depth;
    }

    public String getVendor() {
        return this.vendor;
    }

    public short getVersion() {
        return this.version;
    }

    public short getRevision() {
        return this.revision;
    }

    public int getTemporalQual() {
        return this.temporalQual;
    }

    public int getSpacialQual() {
        return this.spacialQual;
    }

    public short getClrTbl() {
        return this.clrTbl;
    }
}