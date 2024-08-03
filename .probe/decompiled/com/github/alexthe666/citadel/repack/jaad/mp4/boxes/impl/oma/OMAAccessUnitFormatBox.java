package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.oma;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class OMAAccessUnitFormatBox extends FullBox {

    private boolean selectiveEncrypted;

    private int keyIndicatorLength;

    private int initialVectorLength;

    public OMAAccessUnitFormatBox() {
        super("OMA DRM Access Unit Format Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.selectiveEncrypted = (in.read() >> 7 & 1) == 1;
        this.keyIndicatorLength = in.read();
        this.initialVectorLength = in.read();
    }

    public boolean isSelectiveEncrypted() {
        return this.selectiveEncrypted;
    }

    public int getKeyIndicatorLength() {
        return this.keyIndicatorLength;
    }

    public int getInitialVectorLength() {
        return this.initialVectorLength;
    }
}