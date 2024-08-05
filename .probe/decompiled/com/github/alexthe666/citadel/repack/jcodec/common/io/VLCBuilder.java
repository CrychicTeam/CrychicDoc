package com.github.alexthe666.citadel.repack.jcodec.common.io;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.IntIntMap;

public class VLCBuilder {

    private IntIntMap forward = new IntIntMap();

    private IntIntMap inverse = new IntIntMap();

    private IntArrayList codes = IntArrayList.createIntArrayList();

    private IntArrayList codesSizes = IntArrayList.createIntArrayList();

    public static VLCBuilder createVLCBuilder(int[] codes, int[] lens, int[] vals) {
        VLCBuilder b = new VLCBuilder();
        for (int i = 0; i < codes.length; i++) {
            b.setInt(codes[i], lens[i], vals[i]);
        }
        return b;
    }

    public VLCBuilder set(int val, String code) {
        this.setInt(Integer.parseInt(code, 2), code.length(), val);
        return this;
    }

    public VLCBuilder setInt(int code, int len, int val) {
        this.codes.add(code << 32 - len);
        this.codesSizes.add(len);
        this.forward.put(val, this.codes.size() - 1);
        this.inverse.put(this.codes.size() - 1, val);
        return this;
    }

    public VLC getVLC() {
        final VLCBuilder self = this;
        return new VLC(this.codes.toArray(), this.codesSizes.toArray()) {

            @Override
            public int readVLC(BitReader _in) {
                return self.inverse.get(super.readVLC(_in));
            }

            @Override
            public int readVLC16(BitReader _in) {
                return self.inverse.get(super.readVLC16(_in));
            }

            @Override
            public void writeVLC(BitWriter out, int code) {
                super.writeVLC(out, self.forward.get(code));
            }
        };
    }
}