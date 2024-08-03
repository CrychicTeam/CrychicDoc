package com.github.alexthe666.citadel.repack.jcodec.codecs.raw;

import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.nio.ByteBuffer;

public class RAWVideoEncoder extends VideoEncoder {

    @Override
    public VideoEncoder.EncodedFrame encodeFrame(Picture pic, ByteBuffer _out) {
        ByteBuffer dup = _out.duplicate();
        ColorSpace color = pic.getColor();
        if (color.planar) {
            for (int plane = 0; plane < color.nComp; plane++) {
                int width = pic.getWidth() >> color.compWidth[plane];
                int startX = pic.getStartX();
                int startY = pic.getStartY();
                int cropW = pic.getCroppedWidth() >> color.compWidth[plane];
                int cropH = pic.getCroppedHeight() >> color.compHeight[plane];
                byte[] planeData = pic.getPlaneData(plane);
                int pos = width * startY + startX;
                for (int y = 0; y < cropH; y++) {
                    for (int x = 0; x < cropW; x++) {
                        dup.put((byte) (planeData[pos + x] + 128));
                    }
                    pos += width;
                }
            }
        } else {
            int bytesPerPixel = color.bitsPerPixel + 7 >> 3;
            int stride = pic.getWidth() * bytesPerPixel;
            int startX = pic.getStartX();
            int startY = pic.getStartY();
            int cropW = pic.getCroppedWidth();
            int cropH = pic.getCroppedHeight();
            byte[] planeData = pic.getPlaneData(0);
            int pos = stride * startY + startX * bytesPerPixel;
            for (int y = 0; y < cropH; y++) {
                int x = 0;
                for (int off = 0; x < cropW; off += bytesPerPixel) {
                    for (int b = 0; b < bytesPerPixel; b++) {
                        dup.put((byte) (planeData[pos + off + b] + 128));
                    }
                    x++;
                }
                pos += stride;
            }
        }
        dup.flip();
        return new VideoEncoder.EncodedFrame(dup, true);
    }

    @Override
    public ColorSpace[] getSupportedColorSpaces() {
        return null;
    }

    @Override
    public int estimateBufferSize(Picture frame) {
        int fullPlaneSize = frame.getWidth() * frame.getCroppedHeight();
        ColorSpace color = frame.getColor();
        int totalSize = 0;
        for (int i = 0; i < color.nComp; i++) {
            totalSize += fullPlaneSize >> color.compWidth[i] >> color.compHeight[i];
        }
        return totalSize;
    }
}