package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MXFPartitionPack extends MXFMetadata {

    private int kagSize;

    private long thisPartition;

    private long prevPartition;

    private long footerPartition;

    private long headerByteCount;

    private long indexByteCount;

    private int indexSid;

    private int bodySid;

    private UL op;

    private int nbEssenceContainers;

    public MXFPartitionPack(UL ul) {
        super(ul);
    }

    @Override
    public void readBuf(ByteBuffer bb) {
        bb.order(ByteOrder.BIG_ENDIAN);
        NIOUtils.skip(bb, 4);
        this.kagSize = bb.getInt();
        this.thisPartition = bb.getLong();
        this.prevPartition = bb.getLong();
        this.footerPartition = bb.getLong();
        this.headerByteCount = bb.getLong();
        this.indexByteCount = bb.getLong();
        this.indexSid = bb.getInt();
        NIOUtils.skip(bb, 8);
        this.bodySid = bb.getInt();
        this.op = UL.read(bb);
        this.nbEssenceContainers = bb.getInt();
    }

    public int getKagSize() {
        return this.kagSize;
    }

    public long getThisPartition() {
        return this.thisPartition;
    }

    public long getPrevPartition() {
        return this.prevPartition;
    }

    public long getFooterPartition() {
        return this.footerPartition;
    }

    public long getHeaderByteCount() {
        return this.headerByteCount;
    }

    public long getIndexByteCount() {
        return this.indexByteCount;
    }

    public int getIndexSid() {
        return this.indexSid;
    }

    public int getBodySid() {
        return this.bodySid;
    }

    public UL getOp() {
        return this.op;
    }

    public int getNbEssenceContainers() {
        return this.nbEssenceContainers;
    }
}