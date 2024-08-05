package net.minecraftforge.logging;

import io.netty.buffer.ByteBuf;

public class PacketDump {

    public static String getContentDump(ByteBuf buffer) {
        int currentLength = buffer.readableBytes();
        StringBuffer returnString = new StringBuffer(currentLength * 3 + currentLength + currentLength / 4 + 30);
        int i;
        for (i = 0; i < currentLength; i++) {
            if (i != 0 && i % 16 == 0) {
                returnString.append('\t');
                for (int j = i - 16; j < i; j++) {
                    if (buffer.getByte(j) >= 32 && buffer.getByte(j) <= 127) {
                        returnString.append((char) buffer.getByte(j));
                    } else {
                        returnString.append('.');
                    }
                }
                returnString.append("\n");
            }
            returnString.append(Integer.toString((buffer.getByte(i) & 240) >> 4, 16) + Integer.toString((buffer.getByte(i) & 15) >> 0, 16));
            returnString.append(' ');
        }
        if (i != 0 && i % 16 != 0) {
            for (int jx = 0; jx < (16 - i % 16) * 3; jx++) {
                returnString.append(' ');
            }
        }
        returnString.append('\t');
        int jx;
        if (i > 0 && i % 16 == 0) {
            jx = i - 16;
        } else {
            jx = i - i % 16;
        }
        for (; i >= 0 && jx < i; jx++) {
            if (buffer.getByte(jx) >= 32 && buffer.getByte(jx) <= 127) {
                returnString.append((char) buffer.getByte(jx));
            } else {
                returnString.append('.');
            }
        }
        returnString.append('\n');
        returnString.append("Length: " + currentLength);
        return returnString.toString();
    }
}