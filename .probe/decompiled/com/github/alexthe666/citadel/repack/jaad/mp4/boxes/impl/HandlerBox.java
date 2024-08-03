package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class HandlerBox extends FullBox {

    public static final int TYPE_VIDEO = 1986618469;

    public static final int TYPE_SOUND = 1936684398;

    public static final int TYPE_HINT = 1751740020;

    public static final int TYPE_META = 1835365473;

    public static final int TYPE_NULL = 1853189228;

    public static final int TYPE_ODSM = 1868854125;

    public static final int TYPE_CRSM = 1668445037;

    public static final int TYPE_SDSM = 1935962989;

    public static final int TYPE_M7SM = 1832350573;

    public static final int TYPE_OCSM = 1868788589;

    public static final int TYPE_IPSM = 1768977261;

    public static final int TYPE_MJSM = 1835692909;

    private long handlerType;

    private String handlerName;

    public HandlerBox() {
        super("Handler Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        in.skipBytes(4L);
        this.handlerType = in.readBytes(4);
        in.readBytes(4);
        in.readBytes(4);
        in.readBytes(4);
        this.handlerName = in.readUTFString((int) this.getLeft(in), "UTF-8");
    }

    public long getHandlerType() {
        return this.handlerType;
    }

    public String getHandlerName() {
        return this.handlerName;
    }
}