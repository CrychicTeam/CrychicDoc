package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.MPEGUtil;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.CopyrightExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.GOPHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureCodingExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureDisplayExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureSpatialScalableExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureTemporalScalableExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.QuantMatrixExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceDisplayExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceExtension;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceScalableExtension;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public class MPSDump {

    private static final MainUtils.Flag DUMP_FROM = MainUtils.Flag.flag("dump-from", null, "Stop reading at timestamp");

    private static final MainUtils.Flag STOP_AT = MainUtils.Flag.flag("stop-at", null, "Start dumping from timestamp");

    private static final MainUtils.Flag[] ALL_FLAGS = new MainUtils.Flag[] { DUMP_FROM, STOP_AT };

    protected ReadableByteChannel ch;

    public MPSDump(ReadableByteChannel ch) {
        this.ch = ch;
    }

    public static void main1(String[] args) throws IOException {
        FileChannelWrapper ch = null;
        try {
            MainUtils.Cmd cmd = MainUtils.parseArguments(args, ALL_FLAGS);
            if (cmd.args.length >= 1) {
                ch = NIOUtils.readableChannel(new File(cmd.args[0]));
                Long dumpAfterPts = cmd.getLongFlag(DUMP_FROM);
                Long stopPts = cmd.getLongFlag(STOP_AT);
                new MPSDump(ch).dump(dumpAfterPts, stopPts);
                return;
            }
            MainUtils.printHelp(ALL_FLAGS, Arrays.asList("file name"));
        } finally {
            NIOUtils.closeQuietly(ch);
        }
    }

    public void dump(Long dumpAfterPts, Long stopPts) throws IOException {
        MPSDump.MPEGVideoAnalyzer analyzer = null;
        ByteBuffer buffer = ByteBuffer.allocate(1048576);
        PESPacket pkt = null;
        int hdrSize = 0;
        long position = 0L;
        while (true) {
            position -= (long) buffer.position();
            if (this.fillBuffer(buffer) == -1) {
                break;
            }
            buffer.flip();
            if (buffer.remaining() < 4) {
                break;
            }
            position += (long) buffer.remaining();
            while (true) {
                ByteBuffer payload = null;
                if (pkt != null && pkt.length > 0) {
                    int pesLen = pkt.length - hdrSize + 6;
                    if (pesLen <= buffer.remaining()) {
                        payload = NIOUtils.read(buffer, pesLen);
                    }
                } else {
                    payload = getPesPayload(buffer);
                }
                if (payload != null) {
                    if (pkt != null) {
                        this.logPes(pkt, hdrSize, payload);
                    }
                    if (analyzer != null && pkt != null && pkt.streamId >= 224 && pkt.streamId <= 239) {
                        analyzer.analyzeMpegVideoPacket(payload);
                    }
                    if (buffer.remaining() < 32) {
                        pkt = null;
                    } else {
                        skipToNextPES(buffer);
                        if (buffer.remaining() >= 32) {
                            hdrSize = buffer.position();
                            pkt = MPSUtils.readPESHeader(buffer, position - (long) buffer.remaining());
                            hdrSize = buffer.position() - hdrSize;
                            if (dumpAfterPts != null && pkt.pts >= dumpAfterPts) {
                                analyzer = new MPSDump.MPEGVideoAnalyzer();
                            }
                            if (stopPts != null && pkt.pts >= stopPts) {
                                return;
                            }
                            continue;
                        }
                        pkt = null;
                    }
                }
                buffer = this.transferRemainder(buffer);
                break;
            }
        }
    }

    protected int fillBuffer(ByteBuffer buffer) throws IOException {
        return this.ch.read(buffer);
    }

    protected void logPes(PESPacket pkt, int hdrSize, ByteBuffer payload) {
        System.out.println(pkt.streamId + "(" + (pkt.streamId >= 224 ? "video" : "audio") + ") [" + pkt.pos + ", " + (payload.remaining() + hdrSize) + "], pts: " + pkt.pts + ", dts: " + pkt.dts);
    }

    private ByteBuffer transferRemainder(ByteBuffer buffer) {
        ByteBuffer dup = buffer.duplicate();
        dup.clear();
        while (buffer.hasRemaining()) {
            dup.put(buffer.get());
        }
        return dup;
    }

    private static void skipToNextPES(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            int marker = buffer.duplicate().getInt();
            if (marker < 445 || marker > 511 || marker == 446) {
                buffer.getInt();
                MPEGUtil.gotoNextMarker(buffer);
                continue;
            }
            break;
        }
    }

    private static ByteBuffer getPesPayload(ByteBuffer buffer) {
        ByteBuffer copy = buffer.duplicate();
        ByteBuffer result = buffer.duplicate();
        while (copy.hasRemaining()) {
            int marker = copy.duplicate().getInt();
            if (marker >= 441) {
                result.limit(copy.position());
                buffer.position(copy.position());
                return result;
            }
            copy.getInt();
            MPEGUtil.gotoNextMarker(copy);
        }
        return null;
    }

    private static class MPEGVideoAnalyzer {

        private int nextStartCode = -1;

        private ByteBuffer bselPayload = ByteBuffer.allocate(1048576);

        private int bselStartCode;

        private int bselOffset;

        private int bselBufInd;

        private int prevBufSize;

        private int curBufInd;

        private PictureHeader picHeader;

        private SequenceHeader sequenceHeader;

        private PictureCodingExtension pictureCodingExtension;

        private SequenceExtension sequenceExtension;

        public MPEGVideoAnalyzer() {
        }

        private void analyzeMpegVideoPacket(ByteBuffer buffer) {
            int pos = buffer.position();
            int bufSize = buffer.remaining();
            while (buffer.hasRemaining()) {
                this.bselPayload.put((byte) (this.nextStartCode >> 24));
                this.nextStartCode = this.nextStartCode << 8 | buffer.get() & 255;
                if (this.nextStartCode >= 256 && this.nextStartCode <= 440) {
                    this.bselPayload.flip();
                    this.bselPayload.getInt();
                    if (this.bselStartCode != 0) {
                        if (this.bselBufInd != this.curBufInd) {
                            this.bselOffset = this.bselOffset - this.prevBufSize;
                        }
                        this.dumpBSEl(this.bselStartCode, this.bselOffset, this.bselPayload);
                    }
                    this.bselPayload.clear();
                    this.bselStartCode = this.nextStartCode;
                    this.bselOffset = buffer.position() - 4 - pos;
                    this.bselBufInd = this.curBufInd;
                }
            }
            this.curBufInd++;
            this.prevBufSize = bufSize;
        }

        private void dumpBSEl(int mark, int offset, ByteBuffer b) {
            System.out.print(String.format("marker: 0x%02x [@%d] ( ", mark, offset));
            if (mark == 256) {
                this.dumpPictureHeader(b);
            } else if (mark <= 431) {
                System.out.print(MainUtils.colorBright(String.format("slice @0x%02x", mark - 257), MainUtils.ANSIColor.BLACK, true));
            } else if (mark == 435) {
                this.dumpSequenceHeader(b);
            } else if (mark == 437) {
                this.dumpExtension(b);
            } else if (mark == 440) {
                this.dumpGroupHeader(b);
            } else {
                System.out.print("--");
            }
            System.out.println(" )");
        }

        private void dumpExtension(ByteBuffer b) {
            BitReader _in = BitReader.createBitReader(b);
            int extType = _in.readNBit(4);
            if (this.picHeader == null) {
                if (this.sequenceHeader != null) {
                    switch(extType) {
                        case 1:
                            this.sequenceExtension = SequenceExtension.read(_in);
                            this.dumpSequenceExtension(this.sequenceExtension);
                            break;
                        case 2:
                            this.dumpSequenceDisplayExtension(SequenceDisplayExtension.read(_in));
                            break;
                        case 3:
                        case 4:
                        default:
                            System.out.print(MainUtils.colorBright("extension " + extType, MainUtils.ANSIColor.GREEN, true));
                            break;
                        case 5:
                            this.dumpSequenceScalableExtension(SequenceScalableExtension.read(_in));
                    }
                } else {
                    System.out.print(MainUtils.colorBright("dangling extension " + extType, MainUtils.ANSIColor.GREEN, true));
                }
            } else {
                switch(extType) {
                    case 3:
                        this.dumpQuantMatrixExtension(QuantMatrixExtension.read(_in));
                        break;
                    case 4:
                        this.dumpCopyrightExtension(CopyrightExtension.read(_in));
                        break;
                    case 5:
                    case 6:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        System.out.print(MainUtils.colorBright("extension " + extType, MainUtils.ANSIColor.GREEN, true));
                        break;
                    case 7:
                        if (this.sequenceHeader != null && this.pictureCodingExtension != null) {
                            this.dumpPictureDisplayExtension(PictureDisplayExtension.read(_in, this.sequenceExtension, this.pictureCodingExtension));
                        }
                        break;
                    case 8:
                        this.pictureCodingExtension = PictureCodingExtension.read(_in);
                        this.dumpPictureCodingExtension(this.pictureCodingExtension);
                        break;
                    case 9:
                        this.dumpPictureSpatialScalableExtension(PictureSpatialScalableExtension.read(_in));
                        break;
                    case 16:
                        this.dumpPictureTemporalScalableExtension(PictureTemporalScalableExtension.read(_in));
                }
            }
        }

        private void dumpSequenceDisplayExtension(SequenceDisplayExtension read) {
            System.out.print(MainUtils.colorBright("sequence display extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpSequenceScalableExtension(SequenceScalableExtension read) {
            System.out.print(MainUtils.colorBright("sequence scalable extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpSequenceExtension(SequenceExtension read) {
            System.out.print(MainUtils.colorBright("sequence extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpPictureTemporalScalableExtension(PictureTemporalScalableExtension read) {
            System.out.print(MainUtils.colorBright("picture temporal scalable extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpPictureSpatialScalableExtension(PictureSpatialScalableExtension read) {
            System.out.print(MainUtils.colorBright("picture spatial scalable extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpPictureCodingExtension(PictureCodingExtension read) {
            System.out.print(MainUtils.colorBright("picture coding extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpPictureDisplayExtension(PictureDisplayExtension read) {
            System.out.print(MainUtils.colorBright("picture display extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpCopyrightExtension(CopyrightExtension read) {
            System.out.print(MainUtils.colorBright("copyright extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private void dumpQuantMatrixExtension(QuantMatrixExtension read) {
            System.out.print(MainUtils.colorBright("quant matrix extension " + this.dumpBin(read), MainUtils.ANSIColor.GREEN, true));
        }

        private String dumpBin(Object read) {
            StringBuilder bldr = new StringBuilder();
            bldr.append("<");
            Field[] fields = Platform.getFields(read.getClass());
            for (int i = 0; i < fields.length; i++) {
                if (Modifier.isPublic(fields[i].getModifiers()) && !Modifier.isStatic(fields[i].getModifiers())) {
                    bldr.append(this.convertName(fields[i].getName()) + ": ");
                    if (fields[i].getType().isPrimitive()) {
                        try {
                            bldr.append(fields[i].get(read));
                        } catch (Exception var6) {
                        }
                    } else {
                        try {
                            Object val = fields[i].get(read);
                            if (val != null) {
                                bldr.append(this.dumpBin(val));
                            } else {
                                bldr.append("N/A");
                            }
                        } catch (Exception var7) {
                        }
                    }
                    if (i < fields.length - 1) {
                        bldr.append(",");
                    }
                }
            }
            bldr.append(">");
            return bldr.toString();
        }

        private String convertName(String name) {
            return name.replaceAll("([A-Z])", " $1").replaceFirst("^ ", "").toLowerCase();
        }

        private void dumpGroupHeader(ByteBuffer b) {
            GOPHeader gopHeader = GOPHeader.read(b);
            System.out.print(MainUtils.colorBright("group header <closed:" + gopHeader.isClosedGop() + ",broken link:" + gopHeader.isBrokenLink() + (gopHeader.getTimeCode() != null ? ",timecode:" + gopHeader.getTimeCode().toString() : "") + ">", MainUtils.ANSIColor.MAGENTA, true));
        }

        private void dumpSequenceHeader(ByteBuffer b) {
            this.picHeader = null;
            this.pictureCodingExtension = null;
            this.sequenceExtension = null;
            this.sequenceHeader = SequenceHeader.read(b);
            System.out.print(MainUtils.colorBright("sequence header", MainUtils.ANSIColor.BLUE, true));
        }

        private void dumpPictureHeader(ByteBuffer b) {
            this.picHeader = PictureHeader.read(b);
            this.pictureCodingExtension = null;
            System.out.print(MainUtils.colorBright("picture header <type:" + (this.picHeader.picture_coding_type == 1 ? "I" : (this.picHeader.picture_coding_type == 2 ? "P" : "B")) + ", temp_ref:" + this.picHeader.temporal_reference + ">", MainUtils.ANSIColor.BROWN, true));
        }
    }
}