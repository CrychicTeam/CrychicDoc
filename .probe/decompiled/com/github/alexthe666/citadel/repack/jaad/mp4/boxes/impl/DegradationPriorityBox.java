package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class DegradationPriorityBox extends FullBox {

    private int[] priorities;

    public DegradationPriorityBox() {
        super("Degradation Priority Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int sampleCount = ((SampleSizeBox) this.parent.getChild(1937011578L)).getSampleCount();
        this.priorities = new int[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            this.priorities[i] = (int) in.readBytes(2);
        }
    }

    public int[] getPriorities() {
        return this.priorities;
    }
}