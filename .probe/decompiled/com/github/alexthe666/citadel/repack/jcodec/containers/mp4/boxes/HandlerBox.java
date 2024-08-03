package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class HandlerBox extends FullBox {

    private String componentType;

    private String componentSubType;

    private String componentManufacturer;

    private int componentFlags;

    private int componentFlagsMask;

    private String componentName;

    public HandlerBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "hdlr";
    }

    public static HandlerBox createHandlerBox(String componentType, String componentSubType, String componentManufacturer, int componentFlags, int componentFlagsMask) {
        HandlerBox hdlr = new HandlerBox(new Header(fourcc()));
        hdlr.componentType = componentType;
        hdlr.componentSubType = componentSubType;
        hdlr.componentManufacturer = componentManufacturer;
        hdlr.componentFlags = componentFlags;
        hdlr.componentFlagsMask = componentFlagsMask;
        hdlr.componentName = "";
        return hdlr;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.componentType = NIOUtils.readString(input, 4);
        this.componentSubType = NIOUtils.readString(input, 4);
        this.componentManufacturer = NIOUtils.readString(input, 4);
        this.componentFlags = input.getInt();
        this.componentFlagsMask = input.getInt();
        this.componentName = NIOUtils.readString(input, input.remaining());
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.put(JCodecUtil2.asciiString(this.componentType));
        out.put(JCodecUtil2.asciiString(this.componentSubType));
        out.put(JCodecUtil2.asciiString(this.componentManufacturer));
        out.putInt(this.componentFlags);
        out.putInt(this.componentFlagsMask);
        if (this.componentName != null) {
            out.put(JCodecUtil2.asciiString(this.componentName));
        }
    }

    @Override
    public int estimateSize() {
        return 12 + JCodecUtil2.asciiString(this.componentType).length + JCodecUtil2.asciiString(this.componentSubType).length + JCodecUtil2.asciiString(this.componentManufacturer).length + 9;
    }

    public String getComponentType() {
        return this.componentType;
    }

    public String getComponentSubType() {
        return this.componentSubType;
    }

    public String getComponentManufacturer() {
        return this.componentManufacturer;
    }

    public int getComponentFlags() {
        return this.componentFlags;
    }

    public int getComponentFlagsMask() {
        return this.componentFlagsMask;
    }
}