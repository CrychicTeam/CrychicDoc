package com.github.alexthe666.citadel.repack.jcodec.codecs.png;

import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class PNGEncoder extends VideoEncoder {

    private static int crc32(ByteBuffer from, ByteBuffer to) {
        from.limit(to.position());
        CRC32 crc32 = new CRC32();
        crc32.update(NIOUtils.toArray(from));
        return (int) crc32.getValue();
    }

    @Override
    public VideoEncoder.EncodedFrame encodeFrame(Picture pic, ByteBuffer out) {
        ByteBuffer _out = out.duplicate();
        _out.putInt(-1991225785);
        _out.putInt(218765834);
        IHDR ihdr = new IHDR();
        ihdr.width = pic.getCroppedWidth();
        ihdr.height = pic.getCroppedHeight();
        ihdr.bitDepth = 8;
        ihdr.colorType = 2;
        _out.putInt(13);
        ByteBuffer crcFrom = _out.duplicate();
        _out.putInt(1229472850);
        ihdr.write(_out);
        _out.putInt(crc32(crcFrom, _out));
        Deflater deflater = new Deflater();
        byte[] rowData = new byte[pic.getCroppedWidth() * 3 + 1];
        byte[] pix = pic.getPlaneData(0);
        byte[] buffer = new byte[32768];
        int ptr = 0;
        int len = buffer.length;
        int lineStep = (pic.getWidth() - pic.getCroppedWidth()) * 3;
        int row = 0;
        for (int bptr = 0; row < pic.getCroppedHeight() + 1; row++) {
            int count;
            while ((count = deflater.deflate(buffer, ptr, len)) > 0) {
                ptr += count;
                len -= count;
                if (len == 0) {
                    _out.putInt(ptr);
                    crcFrom = _out.duplicate();
                    _out.putInt(1229209940);
                    _out.put(buffer, 0, ptr);
                    _out.putInt(crc32(crcFrom, _out));
                    ptr = 0;
                    len = buffer.length;
                }
            }
            if (row >= pic.getCroppedHeight()) {
                break;
            }
            rowData[0] = 0;
            for (int i = 1; i <= pic.getCroppedWidth() * 3; bptr += 3) {
                rowData[i] = (byte) (pix[bptr] + 128);
                rowData[i + 1] = (byte) (pix[bptr + 1] + 128);
                rowData[i + 2] = (byte) (pix[bptr + 2] + 128);
                i += 3;
            }
            bptr += lineStep;
            deflater.setInput(rowData);
            if (row >= pic.getCroppedHeight() - 1) {
                deflater.finish();
            }
        }
        if (ptr > 0) {
            _out.putInt(ptr);
            crcFrom = _out.duplicate();
            _out.putInt(1229209940);
            _out.put(buffer, 0, ptr);
            _out.putInt(crc32(crcFrom, _out));
        }
        _out.putInt(0);
        _out.putInt(1229278788);
        _out.putInt(-1371381630);
        _out.flip();
        return new VideoEncoder.EncodedFrame(_out, true);
    }

    @Override
    public ColorSpace[] getSupportedColorSpaces() {
        return new ColorSpace[] { ColorSpace.RGB };
    }

    @Override
    public int estimateBufferSize(Picture frame) {
        return frame.getCroppedWidth() * frame.getCroppedHeight() * 4;
    }
}