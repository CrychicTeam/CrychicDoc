package dev.kosmx.playerAnim.core.data.opennbs.format;

import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;

public class Layer {

    public String name;

    public byte lock;

    public byte volume = 100;

    public byte stereo = 100;

    private int lastUsedTickPos = 0;

    public final ArrayList<Layer.Note> notes = new ArrayList();

    public boolean getLock() {
        return this.lock != 0;
    }

    public void setLock(boolean newValue) {
        this.lock = (byte) (newValue ? 1 : 0);
    }

    public int findAtTick(int tick) {
        int i = -1;
        if (this.notes.size() > this.lastUsedTickPos + 1 && ((Layer.Note) this.notes.get(this.lastUsedTickPos + 1)).tick <= tick) {
            i = this.lastUsedTickPos;
        }
        while (this.notes.size() > i + 1 && ((Layer.Note) this.notes.get(i + 1)).tick <= tick) {
            i++;
        }
        this.lastUsedTickPos = i;
        return i;
    }

    @Nullable
    public Layer.Note addNote(int tick) {
        if (this.getLock()) {
            return null;
        } else {
            int i = this.findAtTick(tick);
            if (i > 0 && ((Layer.Note) this.notes.get(i)).tick == tick) {
                return null;
            } else {
                Layer.Note note = new Layer.Note(tick);
                this.notes.add(i + 1, note);
                return note;
            }
        }
    }

    public ArrayList<Layer.Note> getNotesFrom(int fromTick, int toTick) {
        ArrayList<Layer.Note> returnNotes = new ArrayList();
        int posAtBegin = this.findAtTick(fromTick);
        if (this.notes.size() >= posAtBegin) {
            posAtBegin++;
        }
        int posAtEnd = this.findAtTick(toTick);
        if (this.notes.size() >= posAtEnd) {
            posAtEnd++;
        }
        if (posAtBegin < 0) {
            posAtBegin = 0;
        }
        while (posAtBegin < posAtEnd) {
            returnNotes.add((Layer.Note) this.notes.get(posAtBegin));
            posAtBegin++;
        }
        return returnNotes;
    }

    public ArrayList<Layer.Note> getNotesFrom(int toTick) {
        return this.getNotesFrom(this.lastUsedTickPos, toTick);
    }

    public class Note {

        public byte instrument;

        public byte key;

        public byte velocity = 100;

        public byte panning = 100;

        public short pitch = 0;

        public final int tick;

        public Note(int tick) {
            this.tick = tick;
        }

        public float getPitch() {
            return (float) Math.pow(2.0, (double) (this.key + this.pitch / 100 - 45) / 12.0);
        }

        public float getVolume() {
            return (float) this.velocity / 10000.0F * (float) Layer.this.volume;
        }
    }
}