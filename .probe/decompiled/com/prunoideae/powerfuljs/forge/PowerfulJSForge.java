package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJS;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("powerfuljs")
public class PowerfulJSForge {

    public PowerfulJSForge() {
        EventBuses.registerModEventBus("powerfuljs", FMLJavaModLoadingContext.get().getModEventBus());
        PowerfulJS.init();
    }
}