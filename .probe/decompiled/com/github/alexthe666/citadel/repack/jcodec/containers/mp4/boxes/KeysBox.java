package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.Boxes;
import java.nio.ByteBuffer;

public class KeysBox extends NodeBox {

    private static final String FOURCC = "keys";

    public KeysBox(Header atom) {
        super(atom);
        this.factory = new SimpleBoxFactory(new KeysBox.LocalBoxes());
    }

    public static KeysBox createKeysBox() {
        return new KeysBox(Header.createHeader("keys", 0L));
    }

    @Override
    public void parse(ByteBuffer input) {
        int vf = input.getInt();
        int cnt = input.getInt();
        super.parse(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putInt(0);
        out.putInt(this.boxes.size());
        super.doWrite(out);
    }

    public static String fourcc() {
        return "keys";
    }

    @Override
    public int estimateSize() {
        return 8 + super.estimateSize();
    }

    private static class LocalBoxes extends Boxes {

        LocalBoxes() {
            this.mappings.put(MdtaBox.fourcc(), MdtaBox.class);
        }
    }
}