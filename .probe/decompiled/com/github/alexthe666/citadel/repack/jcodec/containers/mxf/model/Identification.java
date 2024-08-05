package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Identification extends MXFInterchangeObject {

    private UL thisGenerationUID;

    private String companyName;

    private String productName;

    private short versionString;

    private UL productUID;

    private Date modificationDate;

    private String platform;

    public Identification(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 15361:
                    this.companyName = this.readUtf16String(_bb);
                    break;
                case 15362:
                    this.productName = this.readUtf16String(_bb);
                    break;
                case 15363:
                case 15367:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 15364:
                    this.versionString = _bb.getShort();
                    break;
                case 15365:
                    this.productUID = UL.read(_bb);
                    break;
                case 15366:
                    this.modificationDate = readDate(_bb);
                    break;
                case 15368:
                    this.platform = this.readUtf16String(_bb);
                    break;
                case 15369:
                    this.thisGenerationUID = UL.read(_bb);
            }
            it.remove();
        }
    }

    public UL getThisGenerationUID() {
        return this.thisGenerationUID;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getProductName() {
        return this.productName;
    }

    public short getVersionString() {
        return this.versionString;
    }

    public UL getProductUID() {
        return this.productUID;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public String getPlatform() {
        return this.platform;
    }
}