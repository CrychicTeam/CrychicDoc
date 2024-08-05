package dev.kosmx.playerAnim.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class MathHelper {

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static int colorHelper(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static float clampToRadian(float f) {
        double a = Math.PI * 2;
        double b = ((double) f + Math.PI) % (Math.PI * 2);
        if (b < 0.0) {
            b += Math.PI * 2;
        }
        return (float) (b - Math.PI);
    }

    public static ByteBuffer readFromIStream(InputStream stream) throws IOException {
        List<Pair<Integer, byte[]>> listOfBites = new LinkedList();
        int totalSize = 0;
        while (true) {
            int estimatedSize = stream.available();
            byte[] bytes = new byte[Math.max(1, estimatedSize)];
            int i = stream.read(bytes);
            if (i < 1) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
                for (Pair<Integer, byte[]> ix : listOfBites) {
                    byteBuffer.put(ix.getRight(), 0, ix.getLeft());
                }
                byteBuffer.position(0);
                return byteBuffer;
            }
            totalSize += i;
            listOfBites.add(new Pair<>(i, bytes));
        }
    }
}