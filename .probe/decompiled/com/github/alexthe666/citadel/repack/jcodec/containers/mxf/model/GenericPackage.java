package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GenericPackage extends MXFInterchangeObject {

    private UL[] tracks;

    private UL packageUID;

    private String name;

    private Date packageModifiedDate;

    private Date packageCreationDate;

    public GenericPackage(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 17409:
                    this.packageUID = UL.read(_bb);
                    break;
                case 17410:
                    this.name = this.readUtf16String(_bb);
                    break;
                case 17411:
                    this.tracks = readULBatch(_bb);
                    break;
                case 17412:
                    this.packageModifiedDate = readDate(_bb);
                    break;
                case 17413:
                    this.packageCreationDate = readDate(_bb);
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public UL[] getTracks() {
        return this.tracks;
    }

    public UL getPackageUID() {
        return this.packageUID;
    }

    public String getName() {
        return this.name;
    }

    public Date getPackageModifiedDate() {
        return this.packageModifiedDate;
    }

    public Date getPackageCreationDate() {
        return this.packageCreationDate;
    }
}