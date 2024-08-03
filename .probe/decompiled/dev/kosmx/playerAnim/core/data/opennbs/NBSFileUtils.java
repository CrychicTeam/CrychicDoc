package dev.kosmx.playerAnim.core.data.opennbs;

import dev.kosmx.playerAnim.core.data.opennbs.format.Header;
import dev.kosmx.playerAnim.core.data.opennbs.format.Layer;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NBSFileUtils {

    static final int maxWorkingVersion = 5;

    public static NBS read(DataInputStream stream) throws IOException {
        if (readShort(stream) != 0) {
            throw new IOException("Can't read old NBS format.");
        } else {
            NBS.Builder songBuilder = new NBS.Builder();
            Header header = songBuilder.header;
            header.NBS_version = stream.readByte();
            int version = header.NBS_version;
            if (version > 5) {
                throw new IOException("Can't read newer NBS format than 5.");
            } else {
                header.Vanilla_instrument_count = stream.readByte();
                if (version >= 3) {
                    header.Song_length = readShort(stream);
                }
                header.Layer_count = readShort(stream);
                header.Song_name = readString(stream);
                header.Song_author = readString(stream);
                header.Song_original_author = readString(stream);
                header.Song_description = readString(stream);
                header.Song_tempo = readShort(stream);
                header.Auto_saving = stream.readByte();
                header.Auto_saving_duration = stream.readByte();
                header.Time_signature = stream.readByte();
                header.Minutes_spent = readInt(stream);
                header.Left_clicks = readInt(stream);
                header.Right_clicks = readInt(stream);
                header.Note_blocks_added = readInt(stream);
                header.Note_blocks_removed = readInt(stream);
                header.MIDI_Schematic_file_name = readString(stream);
                if (version >= 4) {
                    header.Loop_on_off = stream.readByte();
                    header.Max_loop_count = stream.readByte();
                    header.Loop_start_tick = readShort(stream);
                }
                for (int i = 0; i < header.Layer_count; i++) {
                    songBuilder.layers.add(new Layer());
                }
                int maxLength = 0;
                int tick = -1;
                for (short jumpToTheNextTick = readShort(stream); jumpToTheNextTick != 0; jumpToTheNextTick = readShort(stream)) {
                    tick += jumpToTheNextTick;
                    int layer = -1;
                    for (int jumpToTheNextLayer = readShort(stream); jumpToTheNextLayer != 0; jumpToTheNextLayer = readShort(stream)) {
                        layer += jumpToTheNextLayer;
                        Layer.Note note = ((Layer) songBuilder.layers.get(layer)).addNote(tick);
                        if (note == null) {
                            throw new IOException("Creeper, Aww man");
                        }
                        note.instrument = stream.readByte();
                        note.key = stream.readByte();
                        if (version >= 4) {
                            note.velocity = stream.readByte();
                            note.panning = stream.readByte();
                            note.pitch = readShort(stream);
                        }
                        maxLength = Math.max(maxLength, tick);
                    }
                }
                for (Layer layer : songBuilder.layers) {
                    layer.name = readString(stream);
                    if (version >= 4) {
                        layer.lock = stream.readByte();
                    }
                    layer.volume = stream.readByte();
                    layer.stereo = stream.readByte();
                }
                if (stream.readByte() != 0) {
                    throw new IOException("NBSUtils can not handle custom instruments (yet)");
                } else {
                    NBS song = songBuilder.build();
                    song.setLength(maxLength);
                    return song;
                }
            }
        }
    }

    static String readString(DataInputStream stream) throws IOException {
        int len = readInt(stream);
        if (len < 0) {
            throw new IOException("The string's length is less than zero. You wanna me to read it backwards???");
        } else {
            byte[] bytes = new byte[len];
            if (stream.read(bytes) != len) {
                throw new IOException("Invalid string");
            } else {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
    }

    static int readInt(DataInputStream stream) throws IOException {
        int i = 0;
        for (int n = 0; n < 4; n++) {
            i |= stream.read() << 8 * n;
        }
        return i;
    }

    static short readShort(DataInputStream stream) throws IOException {
        short i = 0;
        for (int n = 0; n < 2; n++) {
            i = (short) (i | stream.read() << 8 * n);
        }
        return i;
    }
}