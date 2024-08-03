package net.minecraft.world.level.chunk;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.VisibleForDebug;

public class DataLayer {

    public static final int LAYER_COUNT = 16;

    public static final int LAYER_SIZE = 128;

    public static final int SIZE = 2048;

    private static final int NIBBLE_SIZE = 4;

    @Nullable
    protected byte[] data;

    private int defaultValue;

    public DataLayer() {
        this(0);
    }

    public DataLayer(int int0) {
        this.defaultValue = int0;
    }

    public DataLayer(byte[] byte0) {
        this.data = byte0;
        this.defaultValue = 0;
        if (byte0.length != 2048) {
            throw (IllegalArgumentException) Util.pauseInIde(new IllegalArgumentException("DataLayer should be 2048 bytes not: " + byte0.length));
        }
    }

    public int get(int int0, int int1, int int2) {
        return this.get(getIndex(int0, int1, int2));
    }

    public void set(int int0, int int1, int int2, int int3) {
        this.set(getIndex(int0, int1, int2), int3);
    }

    private static int getIndex(int int0, int int1, int int2) {
        return int1 << 8 | int2 << 4 | int0;
    }

    private int get(int int0) {
        if (this.data == null) {
            return this.defaultValue;
        } else {
            int $$1 = getByteIndex(int0);
            int $$2 = getNibbleIndex(int0);
            return this.data[$$1] >> 4 * $$2 & 15;
        }
    }

    private void set(int int0, int int1) {
        byte[] $$2 = this.getData();
        int $$3 = getByteIndex(int0);
        int $$4 = getNibbleIndex(int0);
        int $$5 = ~(15 << 4 * $$4);
        int $$6 = (int1 & 15) << 4 * $$4;
        $$2[$$3] = (byte) ($$2[$$3] & $$5 | $$6);
    }

    private static int getNibbleIndex(int int0) {
        return int0 & 1;
    }

    private static int getByteIndex(int int0) {
        return int0 >> 1;
    }

    public void fill(int int0) {
        this.defaultValue = int0;
        this.data = null;
    }

    private static byte packFilled(int int0) {
        byte $$1 = (byte) int0;
        for (int $$2 = 4; $$2 < 8; $$2 += 4) {
            $$1 = (byte) ($$1 | int0 << $$2);
        }
        return $$1;
    }

    public byte[] getData() {
        if (this.data == null) {
            this.data = new byte[2048];
            if (this.defaultValue != 0) {
                Arrays.fill(this.data, packFilled(this.defaultValue));
            }
        }
        return this.data;
    }

    public DataLayer copy() {
        return this.data == null ? new DataLayer(this.defaultValue) : new DataLayer((byte[]) this.data.clone());
    }

    public String toString() {
        StringBuilder $$0 = new StringBuilder();
        for (int $$1 = 0; $$1 < 4096; $$1++) {
            $$0.append(Integer.toHexString(this.get($$1)));
            if (($$1 & 15) == 15) {
                $$0.append("\n");
            }
            if (($$1 & 0xFF) == 255) {
                $$0.append("\n");
            }
        }
        return $$0.toString();
    }

    @VisibleForDebug
    public String layerToString(int int0) {
        StringBuilder $$1 = new StringBuilder();
        for (int $$2 = 0; $$2 < 256; $$2++) {
            $$1.append(Integer.toHexString(this.get($$2)));
            if (($$2 & 15) == 15) {
                $$1.append("\n");
            }
        }
        return $$1.toString();
    }

    public boolean isDefinitelyHomogenous() {
        return this.data == null;
    }

    public boolean isDefinitelyFilledWith(int int0) {
        return this.data == null && this.defaultValue == int0;
    }

    public boolean isEmpty() {
        return this.data == null && this.defaultValue == 0;
    }
}