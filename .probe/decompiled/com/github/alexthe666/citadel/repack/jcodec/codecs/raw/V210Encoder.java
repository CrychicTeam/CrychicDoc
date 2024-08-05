package com.github.alexthe666.citadel.repack.jcodec.codecs.raw;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class V210Encoder {

    public ByteBuffer encodeFrame(ByteBuffer _out, Picture frame) throws IOException {
        ByteBuffer out = _out.duplicate();
        out.order(ByteOrder.LITTLE_ENDIAN);
        int tgtStride = (frame.getPlaneWidth(0) + 47) / 48 * 48;
        byte[][] data = frame.getData();
        byte[] tmpY = new byte[tgtStride];
        byte[] tmpCb = new byte[tgtStride >> 1];
        byte[] tmpCr = new byte[tgtStride >> 1];
        int yOff = 0;
        int cbOff = 0;
        int crOff = 0;
        for (int yy = 0; yy < frame.getHeight(); yy++) {
            System.arraycopy(data[0], yOff, tmpY, 0, frame.getPlaneWidth(0));
            System.arraycopy(data[1], cbOff, tmpCb, 0, frame.getPlaneWidth(1));
            System.arraycopy(data[2], crOff, tmpCr, 0, frame.getPlaneWidth(2));
            int yi = 0;
            int cbi = 0;
            int cri = 0;
            while (yi < tgtStride) {
                int i = 0;
                i |= clip(tmpCr[cri++]) << 20;
                i |= clip(tmpY[yi++]) << 10;
                i |= clip(tmpCb[cbi++]);
                out.putInt(i);
                int var29 = 0;
                var29 |= clip(tmpY[yi++]);
                var29 |= clip(tmpY[yi++]) << 20;
                var29 |= clip(tmpCb[cbi++]) << 10;
                out.putInt(var29);
                int var33 = 0;
                var33 |= clip(tmpCb[cbi++]) << 20;
                var33 |= clip(tmpY[yi++]) << 10;
                var33 |= clip(tmpCr[cri++]);
                out.putInt(var33);
                int var37 = 0;
                var37 |= clip(tmpY[yi++]);
                var37 |= clip(tmpY[yi++]) << 20;
                var37 |= clip(tmpCr[cri++]) << 10;
                out.putInt(var37);
            }
            yOff += frame.getPlaneWidth(0);
            cbOff += frame.getPlaneWidth(1);
            crOff += frame.getPlaneWidth(2);
        }
        out.flip();
        return out;
    }

    static final int clip(byte val) {
        return MathUtil.clip(val + 128 << 2, 8, 1019);
    }
}