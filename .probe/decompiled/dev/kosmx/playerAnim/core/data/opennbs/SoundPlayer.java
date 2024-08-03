package dev.kosmx.playerAnim.core.data.opennbs;

import dev.kosmx.playerAnim.core.data.opennbs.format.Layer;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class SoundPlayer {

    final NBS song;

    final float songPerMCTick;

    int mcTick = 0;

    int soundTick = -1;

    boolean isPlaying = true;

    final Consumer<Layer.Note> playSound;

    public SoundPlayer(NBS song, Consumer<Layer.Note> soundPlayer, int tickToBegin) {
        this.song = song;
        this.songPerMCTick = (float) song.header.Song_tempo / 2000.0F;
        this.playSound = soundPlayer;
        this.mcTick = tickToBegin;
    }

    public void tick() {
        int newSongTick = (int) ((float) (this.mcTick++) * this.songPerMCTick);
        if (newSongTick > this.song.header.Loop_start_tick && this.song.header.Loop_on_off()) {
            if (this.song.header.Max_loop_count != 0) {
                int loop = this.song.header.Max_loop_count & 255;
                if ((newSongTick - this.song.header.Loop_start_tick) / (this.song.getLength() - this.song.header.Loop_start_tick) > loop) {
                    this.stop();
                    return;
                }
            }
            newSongTick = (newSongTick - this.song.header.Loop_start_tick) % (this.song.getLength() - this.song.header.Loop_start_tick) + this.song.header.Loop_start_tick;
        } else if (newSongTick > this.song.getLength()) {
            this.stop();
            return;
        }
        if (newSongTick != this.soundTick) {
            List<Layer.Note> notesToPlay = this.song.getNotesUntilTick(this.soundTick, newSongTick);
            notesToPlay.forEach(this.playSound);
            this.soundTick = newSongTick;
        }
    }

    public void stop() {
        this.isPlaying = false;
    }

    public static boolean isPlayingSong(@Nullable SoundPlayer player) {
        return player != null && player.isPlaying;
    }
}