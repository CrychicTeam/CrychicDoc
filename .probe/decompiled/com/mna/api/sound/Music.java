package com.mna.api.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public final class Music {

    @SubscribeEvent
    public static void onRegisterSounds(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> registerSound(helper, "music_big_car_theft"));
    }

    private static void registerSound(RegisterEvent.RegisterHelper<SoundEvent> registry, String name) {
        registry.register(new ResourceLocation("mna", name), SoundEvent.createVariableRangeEvent(new ResourceLocation("mna", name)));
    }

    public static final class Construct {

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:music_big_car_theft")
        public static final SoundEvent DANCE_MIX = null;
    }
}