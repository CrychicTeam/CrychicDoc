package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SourceClip extends MXFStructuralComponent {

    private long startPosition;

    private int sourceTrackId;

    private UL sourcePackageUid;

    public SourceClip(UL ul) {
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
                case 4353:
                    this.sourcePackageUid = UL.read(_bb);
                    break;
                case 4354:
                    this.sourceTrackId = _bb.getInt();
                    break;
                case 4609:
                    this.startPosition = _bb.getLong();
                    break;
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
            }
            it.remove();
        }
    }

    public UL getSourcePackageUid() {
        return this.sourcePackageUid;
    }

    public long getStartPosition() {
        return this.startPosition;
    }

    public int getSourceTrackId() {
        return this.sourceTrackId;
    }
}