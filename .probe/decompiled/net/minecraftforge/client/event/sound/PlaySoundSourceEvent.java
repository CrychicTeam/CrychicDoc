package net.minecraftforge.client.event.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.jetbrains.annotations.ApiStatus.Internal;

public class PlaySoundSourceEvent extends SoundEvent.SoundSourceEvent {

    @Internal
    public PlaySoundSourceEvent(SoundEngine engine, SoundInstance sound, Channel channel) {
        super(engine, sound, channel);
    }
}