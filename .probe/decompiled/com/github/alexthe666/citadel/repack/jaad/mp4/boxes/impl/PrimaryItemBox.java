package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class PrimaryItemBox extends FullBox {

    private int itemID;

    public PrimaryItemBox() {
        super("Primary Item Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.itemID = (int) in.readBytes(2);
    }

    public int getItemID() {
        return this.itemID;
    }
}