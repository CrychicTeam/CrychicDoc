package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.Descriptor;
import java.io.IOException;

public class IPMPControlBox extends FullBox {

    private Descriptor toolList;

    private Descriptor[] ipmpDescriptors;

    public IPMPControlBox() {
        super("IPMP Control Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.toolList = Descriptor.createDescriptor(in);
        int count = in.read();
        this.ipmpDescriptors = new Descriptor[count];
        for (int i = 0; i < count; i++) {
            this.ipmpDescriptors[i] = Descriptor.createDescriptor(in);
        }
    }

    public Descriptor getToolList() {
        return this.toolList;
    }

    public Descriptor[] getIPMPDescriptors() {
        return this.ipmpDescriptors;
    }
}