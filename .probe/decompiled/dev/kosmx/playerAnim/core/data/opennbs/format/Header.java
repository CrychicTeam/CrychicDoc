package dev.kosmx.playerAnim.core.data.opennbs.format;

public class Header {

    public byte NBS_version;

    public byte Vanilla_instrument_count;

    @Deprecated
    public short Song_length;

    public short Layer_count;

    public String Song_name;

    public String Song_author;

    public String Song_original_author;

    public String Song_description;

    public short Song_tempo;

    @Deprecated
    public byte Auto_saving;

    @Deprecated
    public byte Auto_saving_duration;

    public byte Time_signature;

    public int Minutes_spent;

    public int Left_clicks;

    public int Right_clicks;

    public int Note_blocks_added;

    public int Note_blocks_removed;

    public String MIDI_Schematic_file_name;

    public byte Loop_on_off;

    public byte Max_loop_count;

    public short Loop_start_tick;

    public boolean Auto_saving() {
        return this.Auto_saving != 0;
    }

    public boolean Loop_on_off() {
        return this.Loop_on_off != 0;
    }
}