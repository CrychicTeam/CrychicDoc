package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GenericPictureEssenceDescriptor extends FileDescriptor {

    private byte signalStandard;

    private GenericPictureEssenceDescriptor.LayoutType frameLayout;

    private int storedWidth;

    private int storedHeight;

    private int storedF2Offset;

    private int sampledWidth;

    private int sampledHeight;

    private int sampledXOffset;

    private int sampledYOffset;

    private int displayHeight;

    private int displayWidth;

    private int displayXOffset;

    private int displayYOffset;

    private int displayF2Offset;

    private Rational aspectRatio;

    private byte activeFormatDescriptor;

    private int[] videoLineMap;

    private byte alphaTransparency;

    private UL transferCharacteristic;

    private int imageAlignmentOffset;

    private int imageStartOffset;

    private int imageEndOffset;

    private byte fieldDominance;

    private UL pictureEssenceCoding;

    private UL codingEquations;

    private UL colorPrimaries;

    public GenericPictureEssenceDescriptor(UL ul) {
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
                case 12801:
                    this.pictureEssenceCoding = UL.read(_bb);
                    break;
                case 12802:
                    this.storedHeight = _bb.getInt();
                    break;
                case 12803:
                    this.storedWidth = _bb.getInt();
                    break;
                case 12804:
                    this.sampledHeight = _bb.getInt();
                    break;
                case 12805:
                    this.sampledWidth = _bb.getInt();
                    break;
                case 12806:
                    this.sampledXOffset = _bb.getInt();
                    break;
                case 12807:
                    this.sampledYOffset = _bb.getInt();
                    break;
                case 12808:
                    this.displayHeight = _bb.getInt();
                    break;
                case 12809:
                    this.displayWidth = _bb.getInt();
                    break;
                case 12810:
                    this.displayXOffset = _bb.getInt();
                    break;
                case 12811:
                    this.displayYOffset = _bb.getInt();
                    break;
                case 12812:
                    this.frameLayout = GenericPictureEssenceDescriptor.LayoutType.values()[_bb.get()];
                    break;
                case 12813:
                    this.videoLineMap = readInt32Batch(_bb);
                    break;
                case 12814:
                    this.aspectRatio = new Rational(_bb.getInt(), _bb.getInt());
                    break;
                case 12815:
                    this.alphaTransparency = _bb.get();
                    break;
                case 12816:
                    this.transferCharacteristic = UL.read(_bb);
                    break;
                case 12817:
                    this.imageAlignmentOffset = _bb.getInt();
                    break;
                case 12818:
                    this.fieldDominance = _bb.get();
                    break;
                case 12819:
                    this.imageStartOffset = _bb.getInt();
                    break;
                case 12820:
                    this.imageEndOffset = _bb.getInt();
                    break;
                case 12821:
                    this.signalStandard = _bb.get();
                    break;
                case 12822:
                    this.storedF2Offset = _bb.getInt();
                    break;
                case 12823:
                    this.displayF2Offset = _bb.getInt();
                    break;
                case 12824:
                    this.activeFormatDescriptor = _bb.get();
                    break;
                case 12825:
                    this.colorPrimaries = UL.read(_bb);
                    break;
                case 12826:
                    this.codingEquations = UL.read(_bb);
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public byte getSignalStandard() {
        return this.signalStandard;
    }

    public GenericPictureEssenceDescriptor.LayoutType getFrameLayout() {
        return this.frameLayout;
    }

    public int getStoredWidth() {
        return this.storedWidth;
    }

    public int getStoredHeight() {
        return this.storedHeight;
    }

    public int getStoredF2Offset() {
        return this.storedF2Offset;
    }

    public int getSampledWidth() {
        return this.sampledWidth;
    }

    public int getSampledHeight() {
        return this.sampledHeight;
    }

    public int getSampledXOffset() {
        return this.sampledXOffset;
    }

    public int getSampledYOffset() {
        return this.sampledYOffset;
    }

    public int getDisplayHeight() {
        return this.displayHeight;
    }

    public int getDisplayWidth() {
        return this.displayWidth;
    }

    public int getDisplayXOffset() {
        return this.displayXOffset;
    }

    public int getDisplayYOffset() {
        return this.displayYOffset;
    }

    public int getDisplayF2Offset() {
        return this.displayF2Offset;
    }

    public Rational getAspectRatio() {
        return this.aspectRatio;
    }

    public byte getActiveFormatDescriptor() {
        return this.activeFormatDescriptor;
    }

    public int[] getVideoLineMap() {
        return this.videoLineMap;
    }

    public byte getAlphaTransparency() {
        return this.alphaTransparency;
    }

    public UL getTransferCharacteristic() {
        return this.transferCharacteristic;
    }

    public int getImageAlignmentOffset() {
        return this.imageAlignmentOffset;
    }

    public int getImageStartOffset() {
        return this.imageStartOffset;
    }

    public int getImageEndOffset() {
        return this.imageEndOffset;
    }

    public byte getFieldDominance() {
        return this.fieldDominance;
    }

    public UL getPictureEssenceCoding() {
        return this.pictureEssenceCoding;
    }

    public UL getCodingEquations() {
        return this.codingEquations;
    }

    public UL getColorPrimaries() {
        return this.colorPrimaries;
    }

    public static enum LayoutType {

        FullFrame, SeparateFields, OneField, MixedFields, SegmentedFrame
    }
}