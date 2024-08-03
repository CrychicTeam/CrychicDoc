package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class ThreeGPPRecordingYearBox extends FullBox {

    private int year;

    public ThreeGPPRecordingYearBox() {
        super("3GPP Recording Year Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.year = (int) in.readBytes(2);
    }

    public int getYear() {
        return this.year;
    }
}