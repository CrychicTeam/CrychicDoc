package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class VideoMediaHeaderBox extends FullBox {

    int graphicsMode;

    int rOpColor;

    int gOpColor;

    int bOpColor;

    public static String fourcc() {
        return "vmhd";
    }

    public static VideoMediaHeaderBox createVideoMediaHeaderBox(int graphicsMode, int rOpColor, int gOpColor, int bOpColor) {
        VideoMediaHeaderBox vmhd = new VideoMediaHeaderBox(new Header(fourcc()));
        vmhd.graphicsMode = graphicsMode;
        vmhd.rOpColor = rOpColor;
        vmhd.gOpColor = gOpColor;
        vmhd.bOpColor = bOpColor;
        return vmhd;
    }

    public VideoMediaHeaderBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.graphicsMode = input.getShort();
        this.rOpColor = input.getShort();
        this.gOpColor = input.getShort();
        this.bOpColor = input.getShort();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort((short) this.graphicsMode);
        out.putShort((short) this.rOpColor);
        out.putShort((short) this.gOpColor);
        out.putShort((short) this.bOpColor);
    }

    @Override
    public int estimateSize() {
        return 20;
    }

    public int getGraphicsMode() {
        return this.graphicsMode;
    }

    public int getrOpColor() {
        return this.rOpColor;
    }

    public int getgOpColor() {
        return this.gOpColor;
    }

    public int getbOpColor() {
        return this.bOpColor;
    }
}