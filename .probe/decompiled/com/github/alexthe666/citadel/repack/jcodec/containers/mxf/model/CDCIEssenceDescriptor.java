package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CDCIEssenceDescriptor extends GenericPictureEssenceDescriptor {

    private int componentDepth;

    private int horizontalSubsampling;

    private int verticalSubsampling;

    private byte colorSiting;

    private byte reversedByteOrder;

    private short paddingBits;

    private int alphaSampleDepth;

    private int blackRefLevel;

    private int whiteReflevel;

    private int colorRange;

    public CDCIEssenceDescriptor(UL ul) {
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
                case 13057:
                    this.componentDepth = _bb.getInt();
                    break;
                case 13058:
                    this.horizontalSubsampling = _bb.getInt();
                    break;
                case 13059:
                    this.colorSiting = _bb.get();
                    break;
                case 13060:
                    this.blackRefLevel = _bb.getInt();
                    break;
                case 13061:
                    this.whiteReflevel = _bb.getInt();
                    break;
                case 13062:
                    this.colorRange = _bb.getInt();
                    break;
                case 13063:
                    this.paddingBits = _bb.getShort();
                    break;
                case 13064:
                    this.verticalSubsampling = _bb.getInt();
                    break;
                case 13065:
                    this.alphaSampleDepth = _bb.getInt();
                    break;
                case 13066:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 13067:
                    this.reversedByteOrder = _bb.get();
            }
            it.remove();
        }
    }

    public int getComponentDepth() {
        return this.componentDepth;
    }

    public int getHorizontalSubsampling() {
        return this.horizontalSubsampling;
    }

    public int getVerticalSubsampling() {
        return this.verticalSubsampling;
    }

    public byte getColorSiting() {
        return this.colorSiting;
    }

    public byte getReversedByteOrder() {
        return this.reversedByteOrder;
    }

    public short getPaddingBits() {
        return this.paddingBits;
    }

    public int getAlphaSampleDepth() {
        return this.alphaSampleDepth;
    }

    public int getBlackRefLevel() {
        return this.blackRefLevel;
    }

    public int getWhiteReflevel() {
        return this.whiteReflevel;
    }

    public int getColorRange() {
        return this.colorRange;
    }
}