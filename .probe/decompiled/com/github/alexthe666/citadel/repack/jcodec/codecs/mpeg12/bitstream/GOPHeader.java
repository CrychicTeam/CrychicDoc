package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import java.nio.ByteBuffer;

public class GOPHeader implements MPEGHeader {

    private TapeTimecode timeCode;

    private boolean closedGop;

    private boolean brokenLink;

    public GOPHeader(TapeTimecode timeCode, boolean closedGop, boolean brokenLink) {
        this.timeCode = timeCode;
        this.closedGop = closedGop;
        this.brokenLink = brokenLink;
    }

    public static GOPHeader read(ByteBuffer bb) {
        BitReader _in = BitReader.createBitReader(bb);
        boolean dropFrame = _in.read1Bit() == 1;
        short hours = (short) _in.readNBit(5);
        byte minutes = (byte) _in.readNBit(6);
        _in.skip(1);
        byte seconds = (byte) _in.readNBit(6);
        byte frames = (byte) _in.readNBit(6);
        boolean closedGop = _in.read1Bit() == 1;
        boolean brokenLink = _in.read1Bit() == 1;
        return new GOPHeader(new TapeTimecode(hours, minutes, seconds, frames, dropFrame, 0), closedGop, brokenLink);
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        if (this.timeCode == null) {
            bw.writeNBit(0, 25);
        } else {
            bw.write1Bit(this.timeCode.isDropFrame() ? 1 : 0);
            bw.writeNBit(this.timeCode.getHour(), 5);
            bw.writeNBit(this.timeCode.getMinute(), 6);
            bw.write1Bit(1);
            bw.writeNBit(this.timeCode.getSecond(), 6);
            bw.writeNBit(this.timeCode.getFrame(), 6);
        }
        bw.write1Bit(this.closedGop ? 1 : 0);
        bw.write1Bit(this.brokenLink ? 1 : 0);
        bw.flush();
    }

    public TapeTimecode getTimeCode() {
        return this.timeCode;
    }

    public boolean isClosedGop() {
        return this.closedGop;
    }

    public boolean isBrokenLink() {
        return this.brokenLink;
    }
}