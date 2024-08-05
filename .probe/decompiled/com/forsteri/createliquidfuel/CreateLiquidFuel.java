package com.forsteri.createliquidfuel;

import com.forsteri.createliquidfuel.eventhandlers.ForgeEventsHandler;
import com.forsteri.createliquidfuel.eventhandlers.ModEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("createliquidfuel")
public class CreateLiquidFuel {

    public static final String MOD_ID = "createliquidfuel";

    public CreateLiquidFuel() {
        MinecraftForge.EVENT_BUS.register(ForgeEventsHandler.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(ModEventHandler.class);
    }
}