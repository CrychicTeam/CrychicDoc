package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.ESDescriptor;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.ObjectDescriptor;
import java.io.IOException;

public class ESDBox extends FullBox {

    private ESDescriptor esd;

    public ESDBox() {
        super("ESD Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.esd = (ESDescriptor) ObjectDescriptor.createDescriptor(in);
    }

    public ESDescriptor getEntryDescriptor() {
        return this.esd;
    }
}