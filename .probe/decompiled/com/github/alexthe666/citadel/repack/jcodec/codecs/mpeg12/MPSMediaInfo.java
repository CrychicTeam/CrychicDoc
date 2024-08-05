package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ChannelLabel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPSUtils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPSMediaInfo extends MPSUtils.PESReader {

    private Map<Integer, MPSMediaInfo.MPEGTrackMetadata> infos = new HashMap();

    private int pesTried;

    private MPSMediaInfo.PSM psm;

    public List<MPSMediaInfo.MPEGTrackMetadata> getMediaInfo(File f) throws IOException {
        try {
            (new NIOUtils.FileReader() {

                @Override
                protected void data(ByteBuffer data, long filePos) {
                    MPSMediaInfo.this.analyseBuffer(data, filePos);
                }

                @Override
                protected void done() {
                }
            }).readFile(f, 65536, null);
        } catch (MPSMediaInfo.MediaInfoDone var3) {
            Logger.info("Media info done");
        }
        return this.getInfos();
    }

    @Override
    protected void pes(ByteBuffer pesBuffer, long start, int pesLen, int stream) {
        if (MPSUtils.mediaStream(stream)) {
            MPSMediaInfo.MPEGTrackMetadata info = (MPSMediaInfo.MPEGTrackMetadata) this.infos.get(stream);
            if (info == null) {
                info = new MPSMediaInfo.MPEGTrackMetadata(stream);
                this.infos.put(stream, info);
            }
            if (info.probeData == null) {
                info.probeData = NIOUtils.cloneBuffer(pesBuffer);
            }
            if (++this.pesTried >= 100) {
                this.deriveMediaInfo();
                throw new MPSMediaInfo.MediaInfoDone();
            }
        }
    }

    private void deriveMediaInfo() {
        for (MPSMediaInfo.MPEGTrackMetadata stream : this.infos.values()) {
            int streamId = 256 | stream.streamId;
            if (streamId >= 448 && streamId <= 479) {
                stream.codec = Codec.MP2;
            } else if (streamId == 445) {
                ByteBuffer dup = stream.probeData.duplicate();
                MPSUtils.readPESHeader(dup, 0L);
                int type = dup.get() & 255;
                if (type >= 128 && type <= 135) {
                    stream.codec = Codec.AC3;
                } else if ((type < 136 || type > 143) && (type < 152 || type > 159)) {
                    if (type >= 160 && type <= 175) {
                        stream.codec = Codec.PCM_DVD;
                    } else if (type >= 176 && type <= 191) {
                        stream.codec = Codec.TRUEHD;
                    } else if (type >= 192 && type <= 207) {
                        stream.codec = Codec.AC3;
                    }
                } else {
                    stream.codec = Codec.DTS;
                }
            } else if (streamId >= 480 && streamId <= 495) {
                stream.codec = Codec.MPEG2;
            }
        }
    }

    private int[] parseSystem(ByteBuffer pesBuffer) {
        NIOUtils.skip(pesBuffer, 12);
        IntArrayList result = IntArrayList.createIntArrayList();
        while (pesBuffer.remaining() >= 3 && (pesBuffer.get(pesBuffer.position()) & 128) == 128) {
            result.add(pesBuffer.get() & 255);
            pesBuffer.getShort();
        }
        return result.toArray();
    }

    private MPSMediaInfo.PSM parsePSM(ByteBuffer pesBuffer) {
        pesBuffer.getInt();
        short psmLen = pesBuffer.getShort();
        if (psmLen > 1018) {
            throw new RuntimeException("Invalid PSM");
        } else {
            byte b0 = pesBuffer.get();
            byte b1 = pesBuffer.get();
            if ((b1 & 1) != 1) {
                throw new RuntimeException("Invalid PSM");
            } else {
                short psiLen = pesBuffer.getShort();
                ByteBuffer psi = NIOUtils.read(pesBuffer, psiLen & '\uffff');
                short elStreamLen = pesBuffer.getShort();
                this.parseElStreams(NIOUtils.read(pesBuffer, elStreamLen & '\uffff'));
                int crc = pesBuffer.getInt();
                return new MPSMediaInfo.PSM();
            }
        }
    }

    private void parseElStreams(ByteBuffer buf) {
        while (buf.hasRemaining()) {
            byte streamType = buf.get();
            byte streamId = buf.get();
            short strInfoLen = buf.getShort();
            ByteBuffer var5 = NIOUtils.read(buf, strInfoLen & '\uffff');
        }
    }

    public List<MPSMediaInfo.MPEGTrackMetadata> getInfos() {
        return new ArrayList(this.infos.values());
    }

    public static void main1(String[] args) throws IOException {
        new MPSMediaInfo().getMediaInfo(new File(args[0]));
    }

    public static MPSMediaInfo extract(SeekableByteChannel input) {
        return null;
    }

    public List<MPSMediaInfo.MPEGTrackMetadata> getAudioTracks() {
        return null;
    }

    public MPSMediaInfo.MPEGTrackMetadata getVideoTrack() {
        return null;
    }

    public static class MPEGTimecodeMetadata {

        public String getNumFrames() {
            return null;
        }

        public String isDropFrame() {
            return null;
        }

        public String getStartCounter() {
            return null;
        }
    }

    public static class MPEGTrackMetadata {

        int streamId;

        Codec codec;

        ByteBuffer probeData;

        public MPEGTrackMetadata(int streamId) {
            this.streamId = streamId;
        }

        public AudioFormat getAudioFormat() {
            return null;
        }

        public ChannelLabel[] getChannelLables() {
            return null;
        }

        public Size getDisplaySize() {
            return null;
        }

        public Size getCodedSize() {
            return null;
        }

        public float getFps() {
            return 0.0F;
        }

        public float getDuration() {
            return 0.0F;
        }

        public String getFourcc() {
            return null;
        }

        public Rational getFpsR() {
            return null;
        }

        public int getNumFrames() {
            return 0;
        }

        public MPSMediaInfo.MPEGTimecodeMetadata getTimecode() {
            return null;
        }
    }

    public static class MediaInfoDone extends RuntimeException {
    }

    public static class PSM {
    }
}