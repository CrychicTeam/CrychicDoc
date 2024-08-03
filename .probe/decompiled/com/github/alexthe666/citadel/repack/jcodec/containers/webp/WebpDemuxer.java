package com.github.alexthe666.citadel.repack.jcodec.containers.webp;

import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.DataReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class WebpDemuxer implements Demuxer, DemuxerTrack {

    public static final int FOURCC_RIFF = 1179011410;

    public static final int FOURCC_WEBP = 1346520407;

    public static final int FOURCC_VP8 = 540561494;

    public static final int FOURCC_ICCP = 1346585417;

    public static final int FOURCC_ANIM = 1296649793;

    public static final int FOURCC_ANMF = 1179471425;

    public static final int FOURCC_XMP = 542133592;

    public static final int FOURCC_EXIF = 1179211845;

    public static final int FOURCC_ALPH = 1213221953;

    public static final int FOURCC_VP8L = 1278758998;

    public static final int FOURCC_VP8X = 1480085590;

    private ArrayList<DemuxerTrack> vt;

    private boolean headerRead;

    private DataReader raf;

    private boolean done;

    public WebpDemuxer(SeekableByteChannel channel) {
        this.raf = DataReader.createDataReader(channel, ByteOrder.LITTLE_ENDIAN);
        this.vt = new ArrayList();
        this.vt.add(this);
    }

    public void close() throws IOException {
        this.raf.close();
    }

    @Override
    public Packet nextFrame() throws IOException {
        if (this.done) {
            return null;
        } else {
            if (!this.headerRead) {
                this.readHeader();
                this.headerRead = true;
            }
            int fourCC = this.raf.readInt();
            int size = this.raf.readInt();
            this.done = true;
            switch(fourCC) {
                case 540561494:
                    byte[] b = new byte[size];
                    this.raf.readFully(b);
                    return new Packet(ByteBuffer.wrap(b), 0L, 25, 1L, 0L, Packet.FrameType.KEY, null, 0);
                case 542133592:
                case 1179211845:
                case 1179471425:
                case 1213221953:
                case 1278758998:
                case 1296649793:
                case 1346585417:
                case 1480085590:
                default:
                    Logger.warn("Skipping unsupported chunk: " + dwToFourCC(fourCC) + ".");
                    byte[] b1 = new byte[size];
                    this.raf.readFully(b1);
                    return null;
            }
        }
    }

    private void readHeader() throws IOException {
        if (this.raf.readInt() != 1179011410) {
            throw new IOException("Invalid RIFF file.");
        } else {
            int size = this.raf.readInt();
            if (this.raf.readInt() != 1346520407) {
                throw new IOException("Not a WEBP file.");
            }
        }
    }

    @Override
    public DemuxerTrackMeta getMeta() {
        return null;
    }

    @Override
    public List<? extends DemuxerTrack> getTracks() {
        return this.vt;
    }

    @Override
    public List<? extends DemuxerTrack> getVideoTracks() {
        return this.vt;
    }

    @Override
    public List<? extends DemuxerTrack> getAudioTracks() {
        return new ArrayList();
    }

    @UsedViaReflection
    public static int probe(ByteBuffer b_) {
        ByteBuffer b = b_.duplicate();
        if (b.remaining() < 12) {
            return 0;
        } else {
            b.order(ByteOrder.LITTLE_ENDIAN);
            if (b.getInt() != 1179011410) {
                return 0;
            } else {
                int size = b.getInt();
                return b.getInt() != 1346520407 ? 0 : 100;
            }
        }
    }

    public static String dwToFourCC(int fourCC) {
        char[] ch = new char[] { (char) (fourCC >> 24 & 0xFF), (char) (fourCC >> 16 & 0xFF), (char) (fourCC >> 8 & 0xFF), (char) (fourCC >> 0 & 0xFF) };
        return Platform.stringFromChars(ch);
    }
}