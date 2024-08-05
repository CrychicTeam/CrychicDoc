package net.minecraft.server.rcon;

import java.nio.charset.StandardCharsets;

public class PktUtils {

    public static final int MAX_PACKET_SIZE = 1460;

    public static final char[] HEX_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String stringFromByteArray(byte[] byte0, int int1, int int2) {
        int $$3 = int2 - 1;
        int $$4 = int1 > $$3 ? $$3 : int1;
        while (0 != byte0[$$4] && $$4 < $$3) {
            $$4++;
        }
        return new String(byte0, int1, $$4 - int1, StandardCharsets.UTF_8);
    }

    public static int intFromByteArray(byte[] byte0, int int1) {
        return intFromByteArray(byte0, int1, byte0.length);
    }

    public static int intFromByteArray(byte[] byte0, int int1, int int2) {
        return 0 > int2 - int1 - 4 ? 0 : byte0[int1 + 3] << 24 | (byte0[int1 + 2] & 0xFF) << 16 | (byte0[int1 + 1] & 0xFF) << 8 | byte0[int1] & 0xFF;
    }

    public static int intFromNetworkByteArray(byte[] byte0, int int1, int int2) {
        return 0 > int2 - int1 - 4 ? 0 : byte0[int1] << 24 | (byte0[int1 + 1] & 0xFF) << 16 | (byte0[int1 + 2] & 0xFF) << 8 | byte0[int1 + 3] & 0xFF;
    }

    public static String toHexString(byte byte0) {
        return "" + HEX_CHAR[(byte0 & 240) >>> 4] + HEX_CHAR[byte0 & 15];
    }
}