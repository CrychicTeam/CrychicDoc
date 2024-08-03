package dev.kosmx.playerAnim.core.data.opennbs;

import dev.kosmx.playerAnim.core.data.opennbs.format.CustomInstrument;
import dev.kosmx.playerAnim.core.data.opennbs.format.Header;
import dev.kosmx.playerAnim.core.data.opennbs.format.Layer;
import java.util.ArrayList;
import java.util.List;

public class NBS {

    public final Header header;

    final ArrayList<Layer> layers;

    int length;

    byte customInstrumentCount;

    final ArrayList<CustomInstrument> customInstruments;

    public NBS(Header header, ArrayList<Layer> layers, ArrayList<CustomInstrument> customInstruments) {
        if (header.Layer_count != layers.size()) {
            if (layers.size() != 0) {
                throw new IllegalArgumentException("Layer count have to be same in the header with the layers size");
            }
            for (int i = 0; i < header.Layer_count; i++) {
                layers.add(new Layer());
            }
        }
        this.header = header;
        this.layers = layers;
        this.customInstruments = customInstruments;
    }

    public ArrayList<Layer> getLayers() {
        return this.layers;
    }

    List<Layer.Note> getNotesUntilTick(int tickFrom, int tickTo) {
        ArrayList<Layer.Note> notes = new ArrayList();
        for (Layer layer : this.layers) {
            if (tickFrom > tickTo) {
                notes.addAll(layer.getNotesFrom(tickFrom, this.length));
                notes.addAll(layer.getNotesFrom(this.header.Loop_on_off() ? this.header.Loop_start_tick - 1 : -1, tickTo));
            } else {
                notes.addAll(layer.getNotesFrom(tickFrom, tickTo));
            }
        }
        return notes;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = (length / this.header.Time_signature + 1) * this.header.Time_signature;
    }

    public static class Builder {

        public Header header = new Header();

        public ArrayList<Layer> layers = new ArrayList();

        public ArrayList<CustomInstrument> customInstruments;

        public NBS build() {
            return new NBS(this.header, this.layers, this.customInstruments);
        }
    }
}