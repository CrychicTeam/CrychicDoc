package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.IBoxFactory;
import java.nio.ByteBuffer;

public class UdtaBox extends NodeBox {

    private static final String FOURCC = "udta";

    public static UdtaBox createUdtaBox() {
        return new UdtaBox(Header.createHeader(fourcc(), 0L));
    }

    @Override
    public void setFactory(final IBoxFactory _factory) {
        this.factory = new IBoxFactory() {

            @Override
            public Box newBox(Header header) {
                if (header.getFourcc().equals(UdtaMetaBox.fourcc())) {
                    UdtaMetaBox box = new UdtaMetaBox(header);
                    box.setFactory(_factory);
                    return box;
                } else {
                    return _factory.newBox(header);
                }
            }
        };
    }

    public UdtaBox(Header atom) {
        super(atom);
    }

    public MetaBox meta() {
        return NodeBox.findFirst(this, MetaBox.class, MetaBox.fourcc());
    }

    public static String fourcc() {
        return "udta";
    }

    public String latlng() {
        Box gps = findGps(this);
        if (gps == null) {
            return null;
        } else {
            ByteBuffer data = getData(gps);
            if (data == null) {
                return null;
            } else if (data.remaining() < 4) {
                return null;
            } else {
                data.getInt();
                byte[] coordsBytes = new byte[data.remaining()];
                data.get(coordsBytes);
                return new String(coordsBytes);
            }
        }
    }

    static Box findGps(UdtaBox udta) {
        for (Box box : udta.getBoxes()) {
            if (box.getFourcc().endsWith("xyz")) {
                return box;
            }
        }
        return null;
    }

    static ByteBuffer getData(Box box) {
        if (box instanceof Box.LeafBox) {
            Box.LeafBox leaf = (Box.LeafBox) box;
            return leaf.getData();
        } else {
            return null;
        }
    }
}