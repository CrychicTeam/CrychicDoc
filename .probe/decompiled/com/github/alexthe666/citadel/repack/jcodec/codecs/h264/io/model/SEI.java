package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SEI {

    public SEI.SEIMessage[] messages;

    public SEI(SEI.SEIMessage[] messages) {
        this.messages = messages;
    }

    public static SEI read(ByteBuffer is) {
        List<SEI.SEIMessage> messages = new ArrayList();
        SEI.SEIMessage msg;
        do {
            msg = sei_message(is);
            if (msg != null) {
                messages.add(msg);
            }
        } while (msg != null);
        return new SEI((SEI.SEIMessage[]) messages.toArray(new SEI.SEIMessage[0]));
    }

    private static SEI.SEIMessage sei_message(ByteBuffer is) {
        int payloadType = 0;
        int b = 0;
        while (is.hasRemaining() && (b = is.get() & 255) == 255) {
            payloadType += 255;
        }
        if (!is.hasRemaining()) {
            return null;
        } else {
            payloadType += b;
            int payloadSize = 0;
            while (is.hasRemaining() && (b = is.get() & 255) == 255) {
                payloadSize += 255;
            }
            if (!is.hasRemaining()) {
                return null;
            } else {
                payloadSize += b;
                byte[] payload = sei_payload(payloadType, payloadSize, is);
                return payload.length != payloadSize ? null : new SEI.SEIMessage(payloadType, payloadSize, payload);
            }
        }
    }

    private static byte[] sei_payload(int payloadType, int payloadSize, ByteBuffer is) {
        byte[] res = new byte[payloadSize];
        is.get(res);
        return res;
    }

    public void write(ByteBuffer out) {
        BitWriter writer = new BitWriter(out);
        CAVLCWriter.writeTrailingBits(writer);
    }

    public static class SEIMessage {

        public int payloadType;

        public int payloadSize;

        public byte[] payload;

        public SEIMessage(int payloadType2, int payloadSize2, byte[] payload2) {
            this.payload = payload2;
            this.payloadType = payloadType2;
            this.payloadSize = payloadSize2;
        }
    }
}