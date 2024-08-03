package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.EndianBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FormatBox;

public class WaveExtBoxes extends Boxes {

    public WaveExtBoxes() {
        this.mappings.put(FormatBox.fourcc(), FormatBox.class);
        this.mappings.put(EndianBox.fourcc(), EndianBox.class);
    }
}