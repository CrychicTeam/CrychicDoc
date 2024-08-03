package dev.kosmx.playerAnim.core.data.opennbs.network;

import dev.kosmx.playerAnim.core.data.opennbs.NBS;
import dev.kosmx.playerAnim.core.data.opennbs.format.Header;
import dev.kosmx.playerAnim.core.data.opennbs.format.Layer;
import dev.kosmx.playerAnim.core.util.NetworkHelper;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NBSPacket {

    NBS song;

    boolean sendExtraData = false;

    int version = 1;

    final int packetVersion = 1;

    boolean valid = true;

    public NBSPacket(NBS song) {
        this.song = song;
    }

    public NBSPacket() {
    }

    public NBS getSong() {
        return this.song;
    }

    public void write(ByteBuffer buf) {
        buf.putInt(1);
        buf.put((byte) (this.sendExtraData ? 1 : 0));
        buf.put(this.song.header.Vanilla_instrument_count);
        if (this.sendExtraData) {
            buf.putShort(this.song.header.Song_length);
            NetworkHelper.writeString(buf, this.song.header.Song_name);
            NetworkHelper.writeString(buf, this.song.header.Song_author);
            NetworkHelper.writeString(buf, this.song.header.Song_original_author);
            NetworkHelper.writeString(buf, this.song.header.Song_description);
        }
        buf.putShort(this.song.header.Song_tempo);
        if (this.sendExtraData) {
            buf.put(this.song.header.Auto_saving);
            buf.put(this.song.header.Auto_saving_duration);
        }
        buf.put(this.song.header.Time_signature);
        if (this.sendExtraData) {
            buf.putInt(this.song.header.Minutes_spent);
            buf.putInt(this.song.header.Left_clicks);
            buf.putInt(this.song.header.Right_clicks);
            buf.putInt(this.song.header.Note_blocks_added);
            buf.putInt(this.song.header.Note_blocks_removed);
            NetworkHelper.writeString(buf, this.song.header.MIDI_Schematic_file_name);
        }
        buf.put(this.song.header.Loop_on_off);
        buf.put(this.song.header.Max_loop_count);
        buf.putShort(this.song.header.Loop_start_tick);
        buf.putShort((short) this.song.getLayers().size());
        this.writeLayersAndNotes(buf);
    }

    public void writeLayersAndNotes(ByteBuffer buf) {
        for (Layer layer : this.song.getLayers()) {
            if (this.sendExtraData) {
                NetworkHelper.writeString(buf, layer.name);
                buf.put(layer.lock);
            }
            buf.put(layer.volume);
            buf.put(layer.stereo);
            int tick = -1;
            for (Layer.Note note : layer.notes) {
                buf.putShort((short) (note.tick - tick));
                tick = note.tick;
                buf.put(note.instrument);
                buf.put(note.key);
                buf.put(note.velocity);
                buf.put(note.panning);
                buf.putShort(note.pitch);
            }
            buf.putShort((short) 0);
        }
    }

    public boolean read(ByteBuffer buf) throws IOException {
        this.version = buf.getInt();
        this.sendExtraData = buf.get() != 0;
        NBS.Builder builder = new NBS.Builder();
        Header header = builder.header;
        header.Vanilla_instrument_count = buf.get();
        if (this.sendExtraData) {
            header.Song_length = buf.getShort();
            header.Song_name = NetworkHelper.readString(buf);
            header.Song_author = NetworkHelper.readString(buf);
            header.Song_original_author = NetworkHelper.readString(buf);
            header.Song_description = NetworkHelper.readString(buf);
        }
        header.Song_tempo = buf.getShort();
        if (this.sendExtraData) {
            header.Auto_saving = buf.get();
            header.Auto_saving_duration = buf.get();
        }
        header.Time_signature = buf.get();
        if (this.sendExtraData) {
            header.Minutes_spent = buf.getInt();
            header.Left_clicks = buf.getInt();
            header.Right_clicks = buf.getInt();
            header.Note_blocks_added = buf.getInt();
            header.Note_blocks_removed = buf.getInt();
            header.MIDI_Schematic_file_name = NetworkHelper.readString(buf);
        }
        header.Loop_on_off = buf.get();
        header.Max_loop_count = buf.get();
        header.Loop_start_tick = buf.getShort();
        header.Layer_count = buf.getShort();
        this.song = builder.build();
        this.readLayersAndNotes(buf);
        return this.valid;
    }

    void readLayersAndNotes(ByteBuffer buf) throws IOException {
        int length = 0;
        for (Layer layer : this.song.getLayers()) {
            boolean locked = false;
            if (this.sendExtraData) {
                layer.name = NetworkHelper.readString(buf);
                locked = buf.get() != 0;
            }
            layer.volume = buf.get();
            layer.stereo = buf.get();
            int tick = -1;
            for (int step = buf.getShort(); step != 0; step = buf.getShort()) {
                tick += step;
                Layer.Note note = layer.addNote(tick);
                if (note == null) {
                    this.valid = false;
                    return;
                }
                note.instrument = buf.get();
                note.key = buf.get();
                note.velocity = buf.get();
                note.panning = buf.get();
                note.pitch = buf.getShort();
                length = Math.max(length, tick);
            }
            layer.setLock(locked);
        }
        this.song.setLength(length);
    }

    public static int calculateMessageSize(NBS song) {
        int size = 15;
        for (Layer layer : song.getLayers()) {
            size += getLayerMessageSize(layer);
        }
        return size;
    }

    public static int getLayerMessageSize(Layer layer) {
        return 4 + layer.notes.size() * 8;
    }
}