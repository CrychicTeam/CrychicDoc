package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GenericTrack extends MXFInterchangeObject {

    private int trackId;

    private String name;

    private UL sequenceRef;

    private int trackNumber;

    public GenericTrack(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 18433:
                    this.trackId = _bb.getInt();
                    break;
                case 18434:
                    this.name = this.readUtf16String(_bb);
                    break;
                case 18435:
                    this.sequenceRef = UL.read(_bb);
                    break;
                case 18436:
                    this.trackNumber = _bb.getInt();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public int getTrackId() {
        return this.trackId;
    }

    public String getName() {
        return this.name;
    }

    public UL getSequenceRef() {
        return this.sequenceRef;
    }

    public int getTrackNumber() {
        return this.trackNumber;
    }
}