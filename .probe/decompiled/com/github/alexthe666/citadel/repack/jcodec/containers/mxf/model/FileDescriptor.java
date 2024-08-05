package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FileDescriptor extends GenericDescriptor {

    private int linkedTrackId;

    private Rational sampleRate;

    private long containerDuration;

    private UL essenceContainer;

    private UL codec;

    public FileDescriptor(UL ul) {
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
                case 12289:
                    this.sampleRate = new Rational(_bb.getInt(), _bb.getInt());
                    break;
                case 12290:
                    this.containerDuration = _bb.getLong();
                    break;
                case 12291:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 12292:
                    this.essenceContainer = UL.read(_bb);
                    break;
                case 12293:
                    this.codec = UL.read(_bb);
                    break;
                case 12294:
                    this.linkedTrackId = _bb.getInt();
            }
            it.remove();
        }
    }

    public int getLinkedTrackId() {
        return this.linkedTrackId;
    }

    public Rational getSampleRate() {
        return this.sampleRate;
    }

    public long getContainerDuration() {
        return this.containerDuration;
    }

    public UL getEssenceContainer() {
        return this.essenceContainer;
    }

    public UL getCodec() {
        return this.codec;
    }
}