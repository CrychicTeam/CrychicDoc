package net.minecraft.client.sounds;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MusicManager {

    private static final int STARTING_DELAY = 100;

    private final RandomSource random = RandomSource.create();

    private final Minecraft minecraft;

    @Nullable
    private SoundInstance currentMusic;

    private int nextSongDelay = 100;

    public MusicManager(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void tick() {
        Music $$0 = this.minecraft.getSituationalMusic();
        if (this.currentMusic != null) {
            if (!$$0.getEvent().value().getLocation().equals(this.currentMusic.getLocation()) && $$0.replaceCurrentMusic()) {
                this.minecraft.getSoundManager().stop(this.currentMusic);
                this.nextSongDelay = Mth.nextInt(this.random, 0, $$0.getMinDelay() / 2);
            }
            if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
                this.currentMusic = null;
                this.nextSongDelay = Math.min(this.nextSongDelay, Mth.nextInt(this.random, $$0.getMinDelay(), $$0.getMaxDelay()));
            }
        }
        this.nextSongDelay = Math.min(this.nextSongDelay, $$0.getMaxDelay());
        if (this.currentMusic == null && this.nextSongDelay-- <= 0) {
            this.startPlaying($$0);
        }
    }

    public void startPlaying(Music music0) {
        this.currentMusic = SimpleSoundInstance.forMusic(music0.getEvent().value());
        if (this.currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
            this.minecraft.getSoundManager().play(this.currentMusic);
        }
        this.nextSongDelay = Integer.MAX_VALUE;
    }

    public void stopPlaying(Music music0) {
        if (this.isPlayingMusic(music0)) {
            this.stopPlaying();
        }
    }

    public void stopPlaying() {
        if (this.currentMusic != null) {
            this.minecraft.getSoundManager().stop(this.currentMusic);
            this.currentMusic = null;
        }
        this.nextSongDelay += 100;
    }

    public boolean isPlayingMusic(Music music0) {
        return this.currentMusic == null ? false : music0.getEvent().value().getLocation().equals(this.currentMusic.getLocation());
    }
}