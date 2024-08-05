package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import java.nio.ByteBuffer;
import java.util.Date;

public class EbmlDate extends EbmlSint {

    private static final int NANOSECONDS_IN_A_SECOND = 1000000000;

    private static final int MILISECONDS_IN_A_SECOND = 1000;

    private static final int NANOSECONDS_IN_A_MILISECOND = 1000000;

    public static long MILISECONDS_SINCE_UNIX_EPOCH_START = 978307200L;

    public EbmlDate(byte[] id) {
        super(id);
    }

    public void setDate(Date value) {
        this.setMiliseconds(value.getTime());
    }

    public Date getDate() {
        long val = this.getLong();
        val = val / 1000000L + MILISECONDS_SINCE_UNIX_EPOCH_START;
        return new Date(val);
    }

    private void setMiliseconds(long milliseconds) {
        this.setLong((milliseconds - MILISECONDS_SINCE_UNIX_EPOCH_START) * 1000000L);
    }

    @Override
    public void setLong(long value) {
        this.data = ByteBuffer.allocate(8);
        this.data.putLong(value);
        this.data.flip();
    }
}