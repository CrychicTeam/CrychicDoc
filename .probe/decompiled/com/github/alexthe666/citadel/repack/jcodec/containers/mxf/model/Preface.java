package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Preface extends MXFInterchangeObject {

    private Date lastModifiedDate;

    private int objectModelVersion;

    private UL op;

    private UL[] essenceContainers;

    private UL[] dmSchemes;

    public Preface(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 15106:
                    this.lastModifiedDate = readDate(_bb);
                    break;
                case 15107:
                case 15108:
                case 15109:
                case 15110:
                case 15112:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 15111:
                    this.objectModelVersion = _bb.getInt();
                    break;
                case 15113:
                    this.op = UL.read(_bb);
                    break;
                case 15114:
                    this.essenceContainers = readULBatch(_bb);
                    break;
                case 15115:
                    this.dmSchemes = readULBatch(_bb);
            }
            it.remove();
        }
    }

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public int getObjectModelVersion() {
        return this.objectModelVersion;
    }

    public UL getOp() {
        return this.op;
    }

    public UL[] getEssenceContainers() {
        return this.essenceContainers;
    }

    public UL[] getDmSchemes() {
        return this.dmSchemes;
    }
}