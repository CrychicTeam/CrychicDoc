package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChannelBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.WaveExtension;

public class AudioBoxes extends Boxes {

    public AudioBoxes() {
        this.mappings.put(WaveExtension.fourcc(), WaveExtension.class);
        this.mappings.put(ChannelBox.fourcc(), ChannelBox.class);
        this.mappings.put("esds", Box.LeafBox.class);
    }
}