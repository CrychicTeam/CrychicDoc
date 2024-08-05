package com.github.alexthe666.citadel.repack.jcodec.codecs.ppm;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.nio.ByteBuffer;

public class PPMEncoder {

    public ByteBuffer encodeFrame(Picture picture) {
        if (picture.getColor() != ColorSpace.RGB) {
            throw new IllegalArgumentException("Only RGB image can be stored in PPM");
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(picture.getWidth() * picture.getHeight() * 3 + 200);
            buffer.put(JCodecUtil2.asciiString("P6 " + picture.getWidth() + " " + picture.getHeight() + " 255\n"));
            byte[][] data = picture.getData();
            for (int i = 0; i < picture.getWidth() * picture.getHeight() * 3; i += 3) {
                buffer.put((byte) (data[0][i + 2] + 128));
                buffer.put((byte) (data[0][i + 1] + 128));
                buffer.put((byte) (data[0][i] + 128));
            }
            buffer.flip();
            return buffer;
        }
    }
}