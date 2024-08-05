package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;

public class AutoRegisterSoundEvent extends AutoRegisterEntry<SoundEvent> {

    public static AutoRegisterSoundEvent create() {
        return new AutoRegisterSoundEvent(null);
    }

    private AutoRegisterSoundEvent(Supplier<SoundEvent> soundEventSupplier) {
        super(soundEventSupplier);
    }
}