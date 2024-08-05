package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class J2KPictureDescriptor extends MXFInterchangeObject {

    private short rsiz;

    private int xsiz;

    private int ysiz;

    private int xOsiz;

    private int yOsiz;

    private int xTsiz;

    private int yTsiz;

    private int xTOsiz;

    private int yTOsiz;

    private short csiz;

    public J2KPictureDescriptor(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 24836:
                    this.rsiz = _bb.getShort();
                    break;
                case 24837:
                    this.xsiz = _bb.getInt();
                    break;
                case 24838:
                    this.ysiz = _bb.getInt();
                    break;
                case 24839:
                    this.xOsiz = _bb.getInt();
                    break;
                case 24840:
                    this.yOsiz = _bb.getInt();
                    break;
                case 24841:
                    this.xTsiz = _bb.getInt();
                    break;
                case 24842:
                    this.yTsiz = _bb.getInt();
                    break;
                case 24843:
                    this.xTOsiz = _bb.getInt();
                    break;
                case 24844:
                    this.yTOsiz = _bb.getInt();
                    break;
                case 24845:
                    this.csiz = _bb.getShort();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public short getRsiz() {
        return this.rsiz;
    }

    public int getXsiz() {
        return this.xsiz;
    }

    public int getYsiz() {
        return this.ysiz;
    }

    public int getxOsiz() {
        return this.xOsiz;
    }

    public int getyOsiz() {
        return this.yOsiz;
    }

    public int getxTsiz() {
        return this.xTsiz;
    }

    public int getyTsiz() {
        return this.yTsiz;
    }

    public int getxTOsiz() {
        return this.xTOsiz;
    }

    public int getyTOsiz() {
        return this.yTOsiz;
    }

    public short getCsiz() {
        return this.csiz;
    }
}