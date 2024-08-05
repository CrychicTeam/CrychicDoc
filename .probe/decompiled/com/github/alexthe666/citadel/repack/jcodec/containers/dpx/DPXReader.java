package com.github.alexthe666.citadel.repack.jcodec.containers.dpx;

import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DPXReader {

    private static final int READ_BUFFER_SIZE = 3072;

    static final int IMAGEINFO_OFFSET = 768;

    static final int IMAGESOURCE_OFFSET = 1408;

    static final int FILM_OFFSET = 1664;

    static final int TVINFO_OFFSET = 1920;

    public static final int SDPX = 1396985944;

    private final ByteBuffer readBuf = ByteBuffer.allocate(3072);

    private final int magic;

    private boolean eof;

    public DPXReader(SeekableByteChannel ch) throws IOException {
        this.initialRead(ch);
        this.magic = this.readBuf.getInt();
        if (this.magic == 1396985944) {
            this.readBuf.order(ByteOrder.BIG_ENDIAN);
        } else {
            this.readBuf.order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    public DPXMetadata parseMetadata() {
        DPXMetadata dpx = new DPXMetadata();
        dpx.file = readFileInfo(this.readBuf);
        dpx.file.magic = this.magic;
        this.readBuf.position(768);
        dpx.image = readImageInfoHeader(this.readBuf);
        this.readBuf.position(1408);
        dpx.imageSource = readImageSourceHeader(this.readBuf);
        this.readBuf.position(1664);
        dpx.film = readFilmInformationHeader(this.readBuf);
        this.readBuf.position(1920);
        dpx.television = readTelevisionInfoHeader(this.readBuf);
        dpx.userId = readNullTermString(this.readBuf, 32);
        return dpx;
    }

    private void initialRead(ReadableByteChannel ch) throws IOException {
        this.readBuf.clear();
        if (ch.read(this.readBuf) == -1) {
            this.eof = true;
        }
        this.readBuf.flip();
    }

    private static FileHeader readFileInfo(ByteBuffer bb) {
        FileHeader h = new FileHeader();
        h.imageOffset = bb.getInt();
        h.version = readNullTermString(bb, 8);
        h.filesize = bb.getInt();
        h.ditto = bb.getInt();
        h.genericHeaderLength = bb.getInt();
        h.industryHeaderLength = bb.getInt();
        h.userHeaderLength = bb.getInt();
        h.filename = readNullTermString(bb, 100);
        h.created = tryParseISO8601Date(readNullTermString(bb, 24));
        h.creator = readNullTermString(bb, 100);
        h.projectName = readNullTermString(bb, 200);
        h.copyright = readNullTermString(bb, 200);
        h.encKey = bb.getInt();
        return h;
    }

    static Date tryParseISO8601Date(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        } else {
            String noTZ = "yyyy:MM:dd:HH:mm:ss";
            if (dateString.length() == noTZ.length()) {
                return date(dateString, noTZ);
            } else {
                if (dateString.length() == noTZ.length() + 4) {
                    dateString = dateString + "00";
                }
                return date(dateString, "yyyy:MM:dd:HH:mm:ss:Z");
            }
        }
    }

    private static Date date(String dateString, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            return format.parse(dateString);
        } catch (ParseException var4) {
            return null;
        }
    }

    private static String readNullTermString(ByteBuffer bb, int length) {
        ByteBuffer b = ByteBuffer.allocate(length);
        bb.get(b.array(), 0, length);
        return NIOUtils.readNullTermString(b);
    }

    public static DPXReader readFile(File file) throws IOException {
        SeekableByteChannel _in = NIOUtils.readableChannel(file);
        DPXReader var2;
        try {
            var2 = new DPXReader(_in);
        } finally {
            IOUtils.closeQuietly(_in);
        }
        return var2;
    }

    private static TelevisionHeader readTelevisionInfoHeader(ByteBuffer r) {
        TelevisionHeader h = new TelevisionHeader();
        h.timecode = r.getInt();
        h.userBits = r.getInt();
        h.interlace = r.get();
        h.filedNumber = r.get();
        h.videoSignalStarted = r.get();
        h.zero = r.get();
        h.horSamplingRateHz = r.getInt();
        h.vertSampleRateHz = r.getInt();
        h.frameRate = r.getInt();
        h.timeOffset = r.getInt();
        h.gamma = r.getInt();
        h.blackLevel = r.getInt();
        h.blackGain = r.getInt();
        h.breakpoint = r.getInt();
        h.referenceWhiteLevel = r.getInt();
        h.integrationTime = r.getInt();
        return h;
    }

    private static FilmHeader readFilmInformationHeader(ByteBuffer r) {
        FilmHeader h = new FilmHeader();
        h.idCode = readNullTermString(r, 2);
        h.type = readNullTermString(r, 2);
        h.offset = readNullTermString(r, 2);
        h.prefix = readNullTermString(r, 6);
        h.count = readNullTermString(r, 4);
        h.format = readNullTermString(r, 32);
        return h;
    }

    private static ImageSourceHeader readImageSourceHeader(ByteBuffer r) {
        ImageSourceHeader h = new ImageSourceHeader();
        h.xOffset = r.getInt();
        h.yOffset = r.getInt();
        h.xCenter = r.getFloat();
        h.yCenter = r.getFloat();
        h.xOriginal = r.getInt();
        h.yOriginal = r.getInt();
        h.sourceImageFilename = readNullTermString(r, 100);
        h.sourceImageDate = tryParseISO8601Date(readNullTermString(r, 24));
        h.deviceName = readNullTermString(r, 32);
        h.deviceSerial = readNullTermString(r, 32);
        h.borderValidity = new short[] { r.getShort(), r.getShort(), r.getShort(), r.getShort() };
        h.aspectRatio = new int[] { r.getInt(), r.getInt() };
        return h;
    }

    private static ImageHeader readImageInfoHeader(ByteBuffer r) {
        ImageHeader h = new ImageHeader();
        h.orientation = r.getShort();
        h.numberOfImageElements = r.getShort();
        h.pixelsPerLine = r.getInt();
        h.linesPerImageElement = r.getInt();
        h.imageElement1 = new ImageElement();
        h.imageElement1.dataSign = r.getInt();
        return h;
    }
}