package com.craisinlord.idas;

import com.craisinlord.idas.config.ConfigModule;
import com.craisinlord.idas.config.ConfigModuleForge;
import com.craisinlord.idas.item.IDASCreativeModeTab;
import com.craisinlord.idas.item.IDASItems;
import com.craisinlord.idas.sound.IDASSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("idas")
public class IDAS {

    public static final String MODID = "idas";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ConfigModule CONFIG = new ConfigModule();

    public IDAS() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IDASCreativeModeTab.register(eventBus);
        IDASSounds.register(eventBus);
        IDASItems.register(eventBus);
        IDASTags.initTags();
        ConfigModuleForge.init();
        IDASProcessors.IDAS_STRUCTURE_PROCESSOR.init();
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}