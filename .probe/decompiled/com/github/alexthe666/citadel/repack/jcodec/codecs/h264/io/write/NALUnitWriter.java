package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class NALUnitWriter {

    private final WritableByteChannel to;

    private static ByteBuffer _MARKER = ByteBuffer.allocate(4);

    public NALUnitWriter(WritableByteChannel to) {
        this.to = to;
    }

    public void writeUnit(NALUnit nal, ByteBuffer data) throws IOException {
        ByteBuffer emprev = ByteBuffer.allocate(data.remaining() + 1024);
        NIOUtils.write(emprev, _MARKER);
        nal.write(emprev);
        this.emprev(emprev, data);
        emprev.flip();
        this.to.write(emprev);
    }

    private void emprev(ByteBuffer emprev, ByteBuffer data) {
        ByteBuffer dd = data.duplicate();
        int prev1 = 1;
        int prev2 = 1;
        while (dd.hasRemaining()) {
            byte b = dd.get();
            if (prev1 == 0 && prev2 == 0 && (b & 3) == b) {
                prev1 = 3;
                emprev.put((byte) 3);
            }
            prev2 = prev1;
            prev1 = b;
            emprev.put(b);
        }
    }

    static {
        _MARKER.putInt(1);
        _MARKER.flip();
    }
}