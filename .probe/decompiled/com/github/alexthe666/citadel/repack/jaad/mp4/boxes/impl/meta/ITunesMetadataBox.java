package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ITunesMetadataBox extends FullBox {

    private static final String[] TIMESTAMPS = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd" };

    private ITunesMetadataBox.DataType dataType;

    private byte[] data;

    public ITunesMetadataBox() {
        super("iTunes Metadata Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.dataType = ITunesMetadataBox.DataType.forInt(this.flags);
        in.skipBytes(4L);
        this.data = new byte[(int) this.getLeft(in)];
        in.readBytes(this.data);
    }

    public ITunesMetadataBox.DataType getDataType() {
        return this.dataType;
    }

    public byte[] getData() {
        return Arrays.copyOf(this.data, this.data.length);
    }

    public String getText() {
        return new String(this.data, 0, this.data.length, Charset.forName("UTF-8"));
    }

    public long getNumber() {
        long l = 0L;
        for (int i = 0; i < this.data.length; i++) {
            l <<= 8;
            l |= (long) (this.data[i] & 255);
        }
        return l;
    }

    public int getInteger() {
        return (int) this.getNumber();
    }

    public boolean getBoolean() {
        return this.getNumber() != 0L;
    }

    public Date getDate() {
        int i = (int) Math.floor((double) (this.data.length / 3)) - 1;
        Date date;
        if (i >= 0 && i < TIMESTAMPS.length) {
            SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMPS[i]);
            date = sdf.parse(new String(this.data), new ParsePosition(0));
        } else {
            date = null;
        }
        return date;
    }

    public static enum DataType {

        IMPLICIT,
        UTF8,
        UTF16,
        HTML,
        XML,
        UUID,
        ISRC,
        MI3P,
        GIF,
        JPEG,
        PNG,
        URL,
        DURATION,
        DATETIME,
        GENRE,
        INTEGER,
        RIAA,
        UPC,
        BMP,
        UNDEFINED;

        private static final ITunesMetadataBox.DataType[] TYPES = new ITunesMetadataBox.DataType[] { IMPLICIT, UTF8, UTF16, null, null, null, HTML, XML, UUID, ISRC, MI3P, null, GIF, JPEG, PNG, URL, DURATION, DATETIME, GENRE, null, null, INTEGER, null, null, RIAA, UPC, null, BMP };

        private static ITunesMetadataBox.DataType forInt(int i) {
            ITunesMetadataBox.DataType type = null;
            if (i >= 0 && i < TYPES.length) {
                type = TYPES[i];
            }
            if (type == null) {
                type = UNDEFINED;
            }
            return type;
        }
    }
}