package de.keksuccino.fancymenu.util.resource.resources.audio;

import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public interface IAudio extends PlayableResourceWithAudio {

    @Override
    void play();

    void setSoundChannel(@NotNull SoundSource var1);

    @NotNull
    SoundSource getSoundChannel();
}