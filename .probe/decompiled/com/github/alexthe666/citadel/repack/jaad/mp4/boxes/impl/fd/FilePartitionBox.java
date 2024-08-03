package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.fd;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class FilePartitionBox extends FullBox {

    private int itemID;

    private int packetPayloadSize;

    private int fecEncodingID;

    private int fecInstanceID;

    private int maxSourceBlockLength;

    private int encodingSymbolLength;

    private int maxNumberOfEncodingSymbols;

    private String schemeSpecificInfo;

    private int[] blockCounts;

    private long[] blockSizes;

    public FilePartitionBox() {
        super("File Partition Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.itemID = (int) in.readBytes(2);
        this.packetPayloadSize = (int) in.readBytes(2);
        in.skipBytes(1L);
        this.fecEncodingID = in.read();
        this.fecInstanceID = (int) in.readBytes(2);
        this.maxSourceBlockLength = (int) in.readBytes(2);
        this.encodingSymbolLength = (int) in.readBytes(2);
        this.maxNumberOfEncodingSymbols = (int) in.readBytes(2);
        this.schemeSpecificInfo = new String(Base64Decoder.decode(in.readTerminated((int) this.getLeft(in), 0)));
        int entryCount = (int) in.readBytes(2);
        this.blockCounts = new int[entryCount];
        this.blockSizes = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.blockCounts[i] = (int) in.readBytes(2);
            this.blockSizes[i] = (long) ((int) in.readBytes(4));
        }
    }

    public int getItemID() {
        return this.itemID;
    }

    public int getPacketPayloadSize() {
        return this.packetPayloadSize;
    }

    public int getFECEncodingID() {
        return this.fecEncodingID;
    }

    public int getFECInstanceID() {
        return this.fecInstanceID;
    }

    public int getMaxSourceBlockLength() {
        return this.maxSourceBlockLength;
    }

    public int getEncodingSymbolLength() {
        return this.encodingSymbolLength;
    }

    public int getMaxNumberOfEncodingSymbols() {
        return this.maxNumberOfEncodingSymbols;
    }

    public String getSchemeSpecificInfo() {
        return this.schemeSpecificInfo;
    }

    public int[] getBlockCounts() {
        return this.blockCounts;
    }

    public long[] getBlockSizes() {
        return this.blockSizes;
    }
}