package net.minecraftforge.client.event.sound;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class SoundEngineLoadEvent extends SoundEvent implements IModBusEvent {

    @Internal
    public SoundEngineLoadEvent(SoundEngine manager) {
        super(manager);
    }
}