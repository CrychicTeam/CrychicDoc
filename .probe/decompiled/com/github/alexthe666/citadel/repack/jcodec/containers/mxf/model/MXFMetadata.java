package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

public abstract class MXFMetadata {

    protected UL ul;

    protected UL uid;

    public MXFMetadata(UL ul) {
        this.ul = ul;
    }

    public abstract void readBuf(ByteBuffer var1);

    protected static UL[] readULBatch(ByteBuffer _bb) {
        int count = _bb.getInt();
        _bb.getInt();
        UL[] result = new UL[count];
        for (int i = 0; i < count; i++) {
            result[i] = UL.read(_bb);
        }
        return result;
    }

    protected static int[] readInt32Batch(ByteBuffer _bb) {
        int count = _bb.getInt();
        _bb.getInt();
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = _bb.getInt();
        }
        return result;
    }

    protected static Date readDate(ByteBuffer _bb) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, _bb.getShort());
        calendar.set(2, _bb.get());
        calendar.set(5, _bb.get());
        calendar.set(10, _bb.get());
        calendar.set(12, _bb.get());
        calendar.set(13, _bb.get());
        calendar.set(14, (_bb.get() & 255) << 2);
        return calendar.getTime();
    }

    protected String readUtf16String(ByteBuffer _bb) {
        byte[] array;
        if (_bb.getShort(_bb.limit() - 2) != 0) {
            array = NIOUtils.toArray(_bb);
        } else {
            array = NIOUtils.toArray((ByteBuffer) _bb.limit(_bb.limit() - 2));
        }
        return Platform.stringFromCharset(array, "UTF-16");
    }

    public UL getUl() {
        return this.ul;
    }

    public UL getUid() {
        return this.uid;
    }
}