package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class UrlBox extends FullBox {

    private String url;

    public static String fourcc() {
        return "url ";
    }

    public static UrlBox createUrlBox(String url) {
        UrlBox urlBox = new UrlBox(new Header(fourcc()));
        urlBox.url = url;
        return urlBox;
    }

    public UrlBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if ((this.flags & 1) == 0) {
            this.url = NIOUtils.readNullTermStringCharset(input, "UTF-8");
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        if (this.url != null) {
            NIOUtils.write(out, ByteBuffer.wrap(Platform.getBytesForCharset(this.url, "UTF-8")));
            out.put((byte) 0);
        }
    }

    @Override
    public int estimateSize() {
        int sz = 13;
        if (this.url != null) {
            sz += Platform.getBytesForCharset(this.url, "UTF-8").length;
        }
        return sz;
    }

    public String getUrl() {
        return this.url;
    }
}