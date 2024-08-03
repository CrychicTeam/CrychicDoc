package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class EssenceContainerData extends MXFInterchangeObject {

    private UL linkedPackageUID;

    private int indexSID;

    private int bodySID;

    public EssenceContainerData(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 9985:
                    this.linkedPackageUID = UL.read(_bb);
                    break;
                case 16134:
                    this.indexSID = _bb.getInt();
                    break;
                case 16135:
                    this.bodySID = _bb.getInt();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ EssenceContainerData: " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public UL getLinkedPackageUID() {
        return this.linkedPackageUID;
    }

    public int getIndexSID() {
        return this.indexSID;
    }

    public int getBodySID() {
        return this.bodySID;
    }
}