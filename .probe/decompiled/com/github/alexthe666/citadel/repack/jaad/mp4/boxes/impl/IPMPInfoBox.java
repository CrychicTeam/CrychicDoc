package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.Descriptor;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.ObjectDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IPMPInfoBox extends FullBox {

    private List<Descriptor> ipmpDescriptors;

    public IPMPInfoBox() {
        super("IPMP Info Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.ipmpDescriptors = new ArrayList();
        while (this.getLeft(in) > 0L) {
            Descriptor desc = ObjectDescriptor.createDescriptor(in);
            this.ipmpDescriptors.add(desc);
        }
    }

    public List<Descriptor> getIPMPDescriptors() {
        return Collections.unmodifiableList(this.ipmpDescriptors);
    }
}