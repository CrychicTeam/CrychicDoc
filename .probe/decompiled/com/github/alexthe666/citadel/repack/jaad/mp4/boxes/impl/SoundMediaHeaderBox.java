package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SoundMediaHeaderBox extends FullBox {

    private double balance;

    public SoundMediaHeaderBox() {
        super("Sound Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.balance = in.readFixedPoint(8, 8);
        in.skipBytes(2L);
    }

    public double getBalance() {
        return this.balance;
    }
}