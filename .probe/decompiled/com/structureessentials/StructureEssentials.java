package com.structureessentials;

import com.cupboard.config.CupboardConfig;
import com.structureessentials.command.Command;
import com.structureessentials.config.CommonConfiguration;
import java.util.Random;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("structureessentials")
public class StructureEssentials {

    public static final String MODID = "structureessentials";

    public static final Logger LOGGER = LogManager.getLogger();

    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>("structureessentials", new CommonConfiguration());

    public static Random rand = new Random();

    public StructureEssentials() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ((IEventBus) Bus.FORGE.bus().get()).addListener(this::commandRegister);
    }

    @SubscribeEvent
    public void commandRegister(RegisterCommandsEvent event) {
        event.getDispatcher().register(new Command().build(event.getBuildContext()));
    }

    private void setup(FMLCommonSetupEvent event) {
        LOGGER.info("structureessentials mod initialized");
    }
}