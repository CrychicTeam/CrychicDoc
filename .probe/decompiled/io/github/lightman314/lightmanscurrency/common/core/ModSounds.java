package io.github.lightman314.lightmanscurrency.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final RegistryObject<SoundEvent> COINS_CLINKING = ModRegistries.SOUND_EVENTS.register("coins_clinking", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("lightmanscurrency", "coins_clinking")));

    public static void init() {
    }
}