package com.frikinjay.lmd;

import com.frikinjay.lmd.config.LetMeDespawnConfig;
import java.io.File;
import java.util.logging.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("letmedespawn")
public class LetMeDespawn {

    public static final String MOD_ID = "letmedespawn";

    public static Logger logger = Logger.getLogger("letmedespawn");

    public static final File CONFIG_FILE = new File("config/letmedespawn.json");

    public static LetMeDespawnConfig config;

    public LetMeDespawn() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (!CONFIG_FILE.exists()) {
                LetMeDespawnConfig.createDefaultConfig();
            }
            config = LetMeDespawnConfig.load();
        });
    }
}