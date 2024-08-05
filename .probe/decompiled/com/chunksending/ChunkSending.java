package com.chunksending;

import com.chunksending.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import java.util.Random;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("chunksending")
public class ChunkSending {

    public static final String MODID = "chunksending";

    public static final Logger LOGGER = LogManager.getLogger();

    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>("chunksending", new CommonConfiguration());

    public static Random rand = new Random();

    public ChunkSending() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        LOGGER.info("chunksending mod initialized");
    }
}