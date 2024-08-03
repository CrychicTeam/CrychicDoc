package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

public class DescriptorParser {

    private static final int ES_TAG = 3;

    private static final int DC_TAG = 4;

    private static final int DS_TAG = 5;

    private static final int SL_TAG = 6;

    public static Descriptor read(ByteBuffer input) {
        if (input.remaining() < 2) {
            return null;
        } else {
            int tag = input.get() & 255;
            int size = JCodecUtil2.readBER32(input);
            ByteBuffer byteBuffer = NIOUtils.read(input, size);
            switch(tag) {
                case 3:
                    return parseES(byteBuffer);
                case 4:
                    return parseDecoderConfig(byteBuffer);
                case 5:
                    return parseDecoderSpecific(byteBuffer);
                case 6:
                    return parseSL(byteBuffer);
                default:
                    throw new RuntimeException("unknown tag " + tag);
            }
        }
    }

    @UsedViaReflection
    private static NodeDescriptor parseNodeDesc(ByteBuffer input) {
        Collection<Descriptor> children = new ArrayList();
        Descriptor d;
        do {
            d = read(input);
            if (d != null) {
                children.add(d);
            }
        } while (d != null);
        return new NodeDescriptor(0, children);
    }

    private static ES parseES(ByteBuffer input) {
        int trackId = input.getShort();
        input.get();
        NodeDescriptor node = parseNodeDesc(input);
        return new ES(trackId, node.getChildren());
    }

    private static SL parseSL(ByteBuffer input) {
        Preconditions.checkState(2 == (input.get() & 255));
        return new SL();
    }

    private static DecoderSpecific parseDecoderSpecific(ByteBuffer input) {
        ByteBuffer data = NIOUtils.readBuf(input);
        return new DecoderSpecific(data);
    }

    private static DecoderConfig parseDecoderConfig(ByteBuffer input) {
        int objectType = input.get() & 255;
        input.get();
        int bufSize = (input.get() & 255) << 16 | input.getShort() & '\uffff';
        int maxBitrate = input.getInt();
        int avgBitrate = input.getInt();
        NodeDescriptor node = parseNodeDesc(input);
        return new DecoderConfig(objectType, bufSize, maxBitrate, avgBitrate, node.getChildren());
    }
}