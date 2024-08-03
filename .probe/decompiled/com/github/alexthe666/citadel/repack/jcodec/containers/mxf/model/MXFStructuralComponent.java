package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MXFStructuralComponent extends MXFInterchangeObject {

    private long duration;

    private UL dataDefinitionUL;

    public MXFStructuralComponent(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            switch(entry.getKey()) {
                case 513:
                    this.dataDefinitionUL = UL.read((ByteBuffer) entry.getValue());
                    break;
                case 514:
                    this.duration = ((ByteBuffer) entry.getValue()).getLong();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public long getDuration() {
        return this.duration;
    }

    public UL getDataDefinitionUL() {
        return this.dataDefinitionUL;
    }
}