package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AudioSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataRefBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimecodeSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.WaveExtension;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;

public class BoxFactory implements IBoxFactory {

    private static IBoxFactory instance = new BoxFactory(new DefaultBoxes());

    private static IBoxFactory audio = new BoxFactory(new AudioBoxes());

    private static IBoxFactory data = new BoxFactory(new DataBoxes());

    private static IBoxFactory sample = new BoxFactory(new SampleBoxes());

    private static IBoxFactory timecode = new BoxFactory(new TimecodeBoxes());

    private static IBoxFactory video = new BoxFactory(new VideoBoxes());

    private static IBoxFactory waveext = new BoxFactory(new WaveExtBoxes());

    private Boxes boxes;

    public static IBoxFactory getDefault() {
        return instance;
    }

    public BoxFactory(Boxes boxes) {
        this.boxes = boxes;
    }

    @Override
    public Box newBox(Header header) {
        Class<? extends Box> claz = this.boxes.toClass(header.getFourcc());
        if (claz == null) {
            return new Box.LeafBox(header);
        } else {
            Box box = Platform.newInstance(claz, new Object[] { header });
            if (box instanceof NodeBox) {
                NodeBox nodebox = (NodeBox) box;
                if (nodebox instanceof SampleDescriptionBox) {
                    nodebox.setFactory(sample);
                } else if (nodebox instanceof VideoSampleEntry) {
                    nodebox.setFactory(video);
                } else if (nodebox instanceof AudioSampleEntry) {
                    nodebox.setFactory(audio);
                } else if (nodebox instanceof TimecodeSampleEntry) {
                    nodebox.setFactory(timecode);
                } else if (nodebox instanceof DataRefBox) {
                    nodebox.setFactory(data);
                } else if (nodebox instanceof WaveExtension) {
                    nodebox.setFactory(waveext);
                } else {
                    nodebox.setFactory(this);
                }
            }
            return box;
        }
    }
}