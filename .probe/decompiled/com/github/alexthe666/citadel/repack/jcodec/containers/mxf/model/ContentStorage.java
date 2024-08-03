package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ContentStorage extends MXFInterchangeObject {

    private UL[] packageRefs;

    private UL[] essenceContainerData;

    public ContentStorage(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 6401:
                    this.packageRefs = readULBatch(_bb);
                    break;
                case 6402:
                    this.essenceContainerData = readULBatch(_bb);
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ ContentStorage: " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public UL[] getPackageRefs() {
        return this.packageRefs;
    }

    public UL[] getEssenceContainerData() {
        return this.essenceContainerData;
    }
}