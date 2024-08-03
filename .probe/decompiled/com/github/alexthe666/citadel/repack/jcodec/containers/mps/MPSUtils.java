package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MPSUtils {

    public static final int VIDEO_MIN = 480;

    public static final int VIDEO_MAX = 495;

    public static final int AUDIO_MIN = 448;

    public static final int AUDIO_MAX = 479;

    public static final int PACK = 442;

    public static final int SYSTEM = 443;

    public static final int PSM = 444;

    public static final int PRIVATE_1 = 445;

    public static final int PRIVATE_2 = 447;

    public static Class<? extends MPSUtils.MPEGMediaDescriptor>[] dMapping = new Class[256];

    public static final boolean mediaStream(int streamId) {
        return streamId >= $(448) && streamId <= $(495) || streamId == $(445) || streamId == $(447);
    }

    public static final boolean mediaMarker(int marker) {
        return marker >= 448 && marker <= 495 || marker == 445 || marker == 447;
    }

    public static final boolean psMarker(int marker) {
        return marker >= 445 && marker <= 495;
    }

    public static boolean videoMarker(int marker) {
        return marker >= 480 && marker <= 495;
    }

    public static final boolean videoStream(int streamId) {
        return streamId >= $(480) && streamId <= $(495);
    }

    public static boolean audioStream(int streamId) {
        return streamId >= $(448) && streamId <= $(479) || streamId == $(445) || streamId == $(447);
    }

    static int $(int marker) {
        return marker & 0xFF;
    }

    public static PESPacket readPESHeader(ByteBuffer iss, long pos) {
        int streamId = iss.getInt() & 0xFF;
        int len = iss.getShort() & '\uffff';
        if (streamId != 191) {
            int b0 = iss.get() & 255;
            return (b0 & 192) == 128 ? mpeg2Pes(b0, len, streamId, iss, pos) : mpeg1Pes(b0, len, streamId, iss, pos);
        } else {
            return new PESPacket(null, -1L, streamId, len, pos, -1L);
        }
    }

    public static PESPacket mpeg1Pes(int b0, int len, int streamId, ByteBuffer is, long pos) {
        int c = b0;
        while (c == 255) {
            c = is.get() & 255;
        }
        if ((c & 192) == 64) {
            is.get();
            c = is.get() & 255;
        }
        long pts = -1L;
        long dts = -1L;
        if ((c & 240) == 32) {
            pts = _readTs(is, c);
        } else if ((c & 240) == 48) {
            pts = _readTs(is, c);
            dts = readTs(is);
        } else if (c != 15) {
            throw new RuntimeException("Invalid data");
        }
        return new PESPacket(null, pts, streamId, len, pos, dts);
    }

    public static long _readTs(ByteBuffer is, int c) {
        return ((long) c & 14L) << 29 | (long) ((is.get() & 255) << 22) | (long) ((is.get() & 255) >> 1 << 15) | (long) ((is.get() & 255) << 7) | (long) ((is.get() & 255) >> 1);
    }

    public static PESPacket mpeg2Pes(int b0, int len, int streamId, ByteBuffer is, long pos) {
        int flags2 = is.get() & 255;
        int header_len = is.get() & 255;
        long pts = -1L;
        long dts = -1L;
        if ((flags2 & 192) == 128) {
            pts = readTs(is);
            NIOUtils.skip(is, header_len - 5);
        } else if ((flags2 & 192) == 192) {
            pts = readTs(is);
            dts = readTs(is);
            NIOUtils.skip(is, header_len - 10);
        } else {
            NIOUtils.skip(is, header_len);
        }
        return new PESPacket(null, pts, streamId, len, pos, dts);
    }

    public static long readTs(ByteBuffer is) {
        return ((long) is.get() & 14L) << 29 | (long) ((is.get() & 255) << 22) | (long) ((is.get() & 255) >> 1 << 15) | (long) ((is.get() & 255) << 7) | (long) ((is.get() & 255) >> 1);
    }

    public static void writeTs(ByteBuffer is, long ts) {
        is.put((byte) ((int) (ts >> 29 << 1)));
        is.put((byte) ((int) (ts >> 22)));
        is.put((byte) ((int) (ts >> 15 << 1)));
        is.put((byte) ((int) (ts >> 7)));
        is.put((byte) ((int) (ts >> 1)));
    }

    public static List<MPSUtils.MPEGMediaDescriptor> parseDescriptors(ByteBuffer bb) {
        List<MPSUtils.MPEGMediaDescriptor> result = new ArrayList();
        while (bb.remaining() >= 2) {
            ByteBuffer dup = bb.duplicate();
            int tag = dup.get() & 255;
            int len = dup.get() & 255;
            ByteBuffer descriptorBuffer = NIOUtils.read(bb, len + 2);
            if (dMapping[tag] != null) {
                try {
                    MPSUtils.MPEGMediaDescriptor descriptor = (MPSUtils.MPEGMediaDescriptor) dMapping[tag].newInstance();
                    descriptor.parse(descriptorBuffer);
                    result.add(descriptor);
                } catch (Exception var7) {
                    throw new RuntimeException(var7);
                }
            }
        }
        return result;
    }

    static {
        dMapping[2] = MPSUtils.VideoStreamDescriptor.class;
        dMapping[3] = MPSUtils.AudioStreamDescriptor.class;
        dMapping[6] = MPSUtils.DataStreamAlignmentDescriptor.class;
        dMapping[5] = MPSUtils.RegistrationDescriptor.class;
        dMapping[10] = MPSUtils.ISO639LanguageDescriptor.class;
        dMapping[27] = MPSUtils.Mpeg4VideoDescriptor.class;
        dMapping[28] = MPSUtils.Mpeg4AudioDescriptor.class;
        dMapping[40] = MPSUtils.AVCVideoDescriptor.class;
        dMapping[43] = MPSUtils.AACAudioDescriptor.class;
    }

    public static class AACAudioDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int profile;

        private int channel;

        private int flags;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.profile = buf.get() & 255;
            this.channel = buf.get() & 255;
            this.flags = buf.get() & 255;
        }

        public int getProfile() {
            return this.profile;
        }

        public int getChannel() {
            return this.channel;
        }

        public int getFlags() {
            return this.flags;
        }
    }

    public static class AVCVideoDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int profileIdc;

        private int flags;

        private int level;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.profileIdc = buf.get() & 255;
            this.flags = buf.get() & 255;
            this.level = buf.get() & 255;
        }

        public int getProfileIdc() {
            return this.profileIdc;
        }

        public int getFlags() {
            return this.flags;
        }

        public int getLevel() {
            return this.level;
        }
    }

    public static class AudioStreamDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int variableRateAudioIndicator;

        private int freeFormatFlag;

        private int id;

        private int layer;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            int b0 = buf.get() & 255;
            this.freeFormatFlag = b0 >> 7 & 1;
            this.id = b0 >> 6 & 1;
            this.layer = b0 >> 5 & 3;
            this.variableRateAudioIndicator = b0 >> 3 & 1;
        }

        public int getVariableRateAudioIndicator() {
            return this.variableRateAudioIndicator;
        }

        public int getFreeFormatFlag() {
            return this.freeFormatFlag;
        }

        public int getId() {
            return this.id;
        }

        public int getLayer() {
            return this.layer;
        }
    }

    public static class DataStreamAlignmentDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int alignmentType;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.alignmentType = buf.get() & 255;
        }

        public int getAlignmentType() {
            return this.alignmentType;
        }
    }

    public static class ISO639LanguageDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private IntArrayList languageCodes = IntArrayList.createIntArrayList();

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            while (buf.remaining() >= 4) {
                this.languageCodes.add(buf.getInt());
            }
        }

        public IntArrayList getLanguageCodes() {
            return this.languageCodes;
        }
    }

    public static class MPEGMediaDescriptor {

        private int tag;

        private int len;

        public void parse(ByteBuffer buf) {
            this.tag = buf.get() & 255;
            this.len = buf.get() & 255;
        }

        public int getTag() {
            return this.tag;
        }

        public int getLen() {
            return this.len;
        }
    }

    public static class Mpeg4AudioDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int profileLevel;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.profileLevel = buf.get() & 255;
        }

        public int getProfileLevel() {
            return this.profileLevel;
        }
    }

    public static class Mpeg4VideoDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int profileLevel;

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.profileLevel = buf.get() & 255;
        }
    }

    public abstract static class PESReader {

        private int marker = -1;

        private int lenFieldLeft;

        private int pesLen;

        private long pesFileStart = -1L;

        private int stream;

        private boolean _pes;

        private int pesLeft;

        private ByteBuffer pesBuffer = ByteBuffer.allocate(2097152);

        protected abstract void pes(ByteBuffer var1, long var2, int var4, int var5);

        public void analyseBuffer(ByteBuffer buf, long pos) {
            int init = buf.position();
            while (buf.hasRemaining()) {
                if (this.pesLeft > 0) {
                    int toRead = Math.min(buf.remaining(), this.pesLeft);
                    this.pesBuffer.put(NIOUtils.read(buf, toRead));
                    this.pesLeft -= toRead;
                    if (this.pesLeft == 0) {
                        long filePos = pos + (long) buf.position() - (long) init;
                        this.pes1(this.pesBuffer, this.pesFileStart, (int) (filePos - this.pesFileStart), this.stream);
                        this.pesFileStart = -1L;
                        this._pes = false;
                        this.stream = -1;
                    }
                } else {
                    int bt = buf.get() & 255;
                    if (this._pes) {
                        this.pesBuffer.put((byte) (this.marker >>> 24));
                    }
                    this.marker = this.marker << 8 | bt;
                    if (this.marker >= 443 && this.marker <= 495) {
                        long filePos = pos + (long) buf.position() - (long) init - 4L;
                        if (this._pes) {
                            this.pes1(this.pesBuffer, this.pesFileStart, (int) (filePos - this.pesFileStart), this.stream);
                        }
                        this.pesFileStart = filePos;
                        this._pes = true;
                        this.stream = this.marker & 0xFF;
                        this.lenFieldLeft = 2;
                        this.pesLen = 0;
                    } else if (this.marker >= 441 && this.marker <= 511) {
                        if (this._pes) {
                            long filePos = pos + (long) buf.position() - (long) init - 4L;
                            this.pes1(this.pesBuffer, this.pesFileStart, (int) (filePos - this.pesFileStart), this.stream);
                        }
                        this.pesFileStart = -1L;
                        this._pes = false;
                        this.stream = -1;
                    } else if (this.lenFieldLeft > 0) {
                        this.pesLen = this.pesLen << 8 | bt;
                        this.lenFieldLeft--;
                        if (this.lenFieldLeft == 0) {
                            this.pesLeft = this.pesLen;
                            if (this.pesLen != 0) {
                                this.flushMarker();
                                this.marker = -1;
                            }
                        }
                    }
                }
            }
        }

        private void flushMarker() {
            this.pesBuffer.put((byte) (this.marker >>> 24));
            this.pesBuffer.put((byte) (this.marker >>> 16 & 0xFF));
            this.pesBuffer.put((byte) (this.marker >>> 8 & 0xFF));
            this.pesBuffer.put((byte) (this.marker & 0xFF));
        }

        private void pes1(ByteBuffer pesBuffer, long start, int pesLen, int stream) {
            pesBuffer.flip();
            this.pes(pesBuffer, start, pesLen, stream);
            pesBuffer.clear();
        }

        public void finishRead() {
            if (this.pesLeft <= 4) {
                this.flushMarker();
                this.pes1(this.pesBuffer, this.pesFileStart, this.pesBuffer.position(), this.stream);
            }
        }
    }

    public static class RegistrationDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int formatIdentifier;

        private IntArrayList additionalFormatIdentifiers = IntArrayList.createIntArrayList();

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            this.formatIdentifier = buf.getInt();
            while (buf.hasRemaining()) {
                this.additionalFormatIdentifiers.add(buf.get() & 255);
            }
        }

        public int getFormatIdentifier() {
            return this.formatIdentifier;
        }

        public IntArrayList getAdditionalFormatIdentifiers() {
            return this.additionalFormatIdentifiers;
        }
    }

    public static class VideoStreamDescriptor extends MPSUtils.MPEGMediaDescriptor {

        private int multipleFrameRate;

        private int frameRateCode;

        private boolean mpeg1Only;

        private int constrainedParameter;

        private int stillPicture;

        private int profileAndLevel;

        private int chromaFormat;

        private int frameRateExtension;

        Rational[] frameRates = new Rational[] { null, new Rational(24000, 1001), new Rational(24, 1), new Rational(25, 1), new Rational(30000, 1001), new Rational(30, 1), new Rational(50, 1), new Rational(60000, 1001), new Rational(60, 1), null, null, null, null, null, null, null };

        @Override
        public void parse(ByteBuffer buf) {
            super.parse(buf);
            int b0 = buf.get() & 255;
            this.multipleFrameRate = b0 >> 7 & 1;
            this.frameRateCode = b0 >> 3 & 15;
            this.mpeg1Only = (b0 >> 2 & 1) == 0;
            this.constrainedParameter = b0 >> 1 & 1;
            this.stillPicture = b0 & 1;
            if (!this.mpeg1Only) {
                this.profileAndLevel = buf.get() & 255;
                int b1 = buf.get() & 255;
                this.chromaFormat = b1 >> 6;
                this.frameRateExtension = b1 >> 5 & 1;
            }
        }

        public Rational getFrameRate() {
            return this.frameRates[this.frameRateCode];
        }

        public int getMultipleFrameRate() {
            return this.multipleFrameRate;
        }

        public int getFrameRateCode() {
            return this.frameRateCode;
        }

        public boolean isMpeg1Only() {
            return this.mpeg1Only;
        }

        public int getConstrainedParameter() {
            return this.constrainedParameter;
        }

        public int getStillPicture() {
            return this.stillPicture;
        }

        public int getProfileAndLevel() {
            return this.profileAndLevel;
        }

        public int getChromaFormat() {
            return this.chromaFormat;
        }

        public int getFrameRateExtension() {
            return this.frameRateExtension;
        }
    }
}