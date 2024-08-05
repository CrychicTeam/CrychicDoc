package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RGBAEssenceDescriptor extends GenericPictureEssenceDescriptor {

    private int componentMaxRef;

    private int componentMinRef;

    private int alphaMaxRef;

    private int alphaMinRef;

    private byte scanningDirection;

    private ByteBuffer pixelLayout;

    private ByteBuffer palette;

    private ByteBuffer paletteLayout;

    public RGBAEssenceDescriptor(UL ul) {
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
                case 13313:
                    this.pixelLayout = _bb;
                    break;
                case 13314:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 13315:
                    this.palette = _bb;
                    break;
                case 13316:
                    this.paletteLayout = _bb;
                    break;
                case 13317:
                    this.scanningDirection = _bb.get();
                    break;
                case 13318:
                    this.componentMaxRef = _bb.getInt();
                    break;
                case 13319:
                    this.componentMinRef = _bb.getInt();
                    break;
                case 13320:
                    this.alphaMaxRef = _bb.getInt();
                    break;
                case 13321:
                    this.alphaMinRef = _bb.getInt();
            }
            it.remove();
        }
    }

    public int getComponentMaxRef() {
        return this.componentMaxRef;
    }

    public int getComponentMinRef() {
        return this.componentMinRef;
    }

    public int getAlphaMaxRef() {
        return this.alphaMaxRef;
    }

    public int getAlphaMinRef() {
        return this.alphaMinRef;
    }

    public byte getScanningDirection() {
        return this.scanningDirection;
    }

    public ByteBuffer getPixelLayout() {
        return this.pixelLayout;
    }

    public ByteBuffer getPalette() {
        return this.palette;
    }

    public ByteBuffer getPaletteLayout() {
        return this.paletteLayout;
    }
}