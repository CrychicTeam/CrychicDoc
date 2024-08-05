package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class ItemProtectionBox extends FullBox {

    public ItemProtectionBox() {
        super("Item Protection Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int protectionCount = (int) in.readBytes(2);
        this.readChildren(in, protectionCount);
    }
}