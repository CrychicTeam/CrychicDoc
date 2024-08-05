package de.keksuccino.melody.resources.audio;

import de.keksuccino.melody.resources.audio.openal.ALException;
import java.io.Closeable;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public interface AudioClip extends Closeable {

    void play() throws ALException;

    boolean isPlaying() throws ALException;

    void pause() throws ALException;

    boolean isPaused() throws ALException;

    void resume() throws ALException;

    void stop() throws ALException;

    void setVolume(float var1) throws ALException;

    float getVolume();

    void setSoundChannel(@NotNull SoundSource var1) throws ALException;

    @NotNull
    SoundSource getSoundChannel();

    boolean isClosed();
}