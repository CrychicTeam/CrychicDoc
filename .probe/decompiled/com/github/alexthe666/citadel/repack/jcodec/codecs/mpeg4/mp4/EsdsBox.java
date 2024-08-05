package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.mp4;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.ADTSParser;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.DecoderConfig;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.DecoderSpecific;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.Descriptor;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.DescriptorParser;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.ES;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.NodeDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es.SL;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class EsdsBox extends FullBox {

    private ByteBuffer streamInfo;

    private int objectType;

    private int bufSize;

    private int maxBitrate;

    private int avgBitrate;

    private int trackId;

    public static String fourcc() {
        return "esds";
    }

    public EsdsBox(Header atom) {
        super(atom);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        if (this.streamInfo != null && this.streamInfo.remaining() > 0) {
            ArrayList<Descriptor> l = new ArrayList();
            ArrayList<Descriptor> l1 = new ArrayList();
            l1.add(new DecoderSpecific(this.streamInfo));
            l.add(new DecoderConfig(this.objectType, this.bufSize, this.maxBitrate, this.avgBitrate, l1));
            l.add(new SL());
            new ES(this.trackId, l).write(out);
        } else {
            ArrayList<Descriptor> l = new ArrayList();
            l.add(new DecoderConfig(this.objectType, this.bufSize, this.maxBitrate, this.avgBitrate, new ArrayList()));
            l.add(new SL());
            new ES(this.trackId, l).write(out);
        }
    }

    @Override
    public int estimateSize() {
        return 64;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        ES es = (ES) DescriptorParser.read(input);
        this.trackId = es.getTrackId();
        DecoderConfig decoderConfig = NodeDescriptor.findByTag(es, DecoderConfig.tag());
        this.objectType = decoderConfig.getObjectType();
        this.bufSize = decoderConfig.getBufSize();
        this.maxBitrate = decoderConfig.getMaxBitrate();
        this.avgBitrate = decoderConfig.getAvgBitrate();
        DecoderSpecific decoderSpecific = NodeDescriptor.findByTag(decoderConfig, DecoderSpecific.tag());
        this.streamInfo = decoderSpecific.getData();
    }

    public ByteBuffer getStreamInfo() {
        return this.streamInfo;
    }

    public int getObjectType() {
        return this.objectType;
    }

    public int getBufSize() {
        return this.bufSize;
    }

    public int getMaxBitrate() {
        return this.maxBitrate;
    }

    public int getAvgBitrate() {
        return this.avgBitrate;
    }

    public int getTrackId() {
        return this.trackId;
    }

    public static EsdsBox fromADTS(ADTSParser.Header hdr) {
        return createEsdsBox(ADTSParser.adtsToStreamInfo(hdr), hdr.getObjectType() << 5, 0, 210750, 133350, 2);
    }

    public static EsdsBox createEsdsBox(ByteBuffer streamInfo, int objectType, int bufSize, int maxBitrate, int avgBitrate, int trackId) {
        EsdsBox esds = new EsdsBox(new Header(fourcc()));
        esds.objectType = objectType;
        esds.bufSize = bufSize;
        esds.maxBitrate = maxBitrate;
        esds.avgBitrate = avgBitrate;
        esds.trackId = trackId;
        esds.streamInfo = streamInfo;
        return esds;
    }

    public static EsdsBox newEsdsBox() {
        return new EsdsBox(new Header(fourcc()));
    }
}