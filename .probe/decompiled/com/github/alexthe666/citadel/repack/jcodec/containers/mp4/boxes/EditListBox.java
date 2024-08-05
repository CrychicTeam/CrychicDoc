package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class EditListBox extends FullBox {

    private List<Edit> edits;

    public static String fourcc() {
        return "elst";
    }

    public static EditListBox createEditListBox(List<Edit> edits) {
        EditListBox elst = new EditListBox(new Header(fourcc()));
        elst.edits = edits;
        return elst;
    }

    public EditListBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.edits = new ArrayList();
        long num = (long) input.getInt();
        for (int i = 0; (long) i < num; i++) {
            int duration = input.getInt();
            int mediaTime = input.getInt();
            float rate = (float) input.getInt() / 65536.0F;
            this.edits.add(new Edit((long) duration, (long) mediaTime, rate));
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.edits.size());
        for (Edit edit : this.edits) {
            out.putInt((int) edit.getDuration());
            out.putInt((int) edit.getMediaTime());
            out.putInt((int) (edit.getRate() * 65536.0F));
        }
    }

    @Override
    public int estimateSize() {
        return 16 + this.edits.size() * 12;
    }

    public List<Edit> getEdits() {
        return this.edits;
    }
}