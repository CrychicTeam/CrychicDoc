package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TimecodeComponent extends MXFStructuralComponent {

    private long start;

    private int base;

    private int dropFrame;

    public TimecodeComponent(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        super.read(tags);
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 5377:
                    this.start = _bb.getLong();
                    break;
                case 5378:
                    this.base = _bb.getShort();
                    break;
                case 5379:
                    this.dropFrame = _bb.get();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public long getStart() {
        return this.start;
    }

    public int getBase() {
        return this.base;
    }

    public int getDropFrame() {
        return this.dropFrame;
    }
}