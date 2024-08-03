package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class ItemLocationBox extends FullBox {

    private int[] itemID;

    private int[] dataReferenceIndex;

    private long[] baseOffset;

    private long[][] extentOffset;

    private long[][] extentLength;

    public ItemLocationBox() {
        super("Item Location Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        long l = in.readBytes(2);
        int offsetSize = (int) (l >> 12) & 15;
        int lengthSize = (int) (l >> 8) & 15;
        int baseOffsetSize = (int) (l >> 4) & 15;
        int itemCount = (int) in.readBytes(2);
        this.dataReferenceIndex = new int[itemCount];
        this.baseOffset = new long[itemCount];
        this.extentOffset = new long[itemCount][];
        this.extentLength = new long[itemCount][];
        for (int i = 0; i < itemCount; i++) {
            this.itemID[i] = (int) in.readBytes(2);
            this.dataReferenceIndex[i] = (int) in.readBytes(2);
            this.baseOffset[i] = in.readBytes(baseOffsetSize);
            int extentCount = (int) in.readBytes(2);
            this.extentOffset[i] = new long[extentCount];
            this.extentLength[i] = new long[extentCount];
            for (int j = 0; j < extentCount; j++) {
                this.extentOffset[i][j] = in.readBytes(offsetSize);
                this.extentLength[i][j] = in.readBytes(lengthSize);
            }
        }
    }

    public int[] getItemID() {
        return this.itemID;
    }

    public int[] getDataReferenceIndex() {
        return this.dataReferenceIndex;
    }

    public long[] getBaseOffset() {
        return this.baseOffset;
    }

    public long[][] getExtentOffset() {
        return this.extentOffset;
    }

    public long[][] getExtentLength() {
        return this.extentLength;
    }
}