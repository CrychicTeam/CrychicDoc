package me.steinborn.krypton.mod.shared;

import com.velocitypowered.natives.util.Natives;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("krypton")
public class Krypton {

    private static final Logger LOGGER = LogManager.getLogger(Krypton.class);

    public Krypton() {
        if (System.getProperty("io.netty.allocator.maxOrder") == null) {
            System.setProperty("io.netty.allocator.maxOrder", "9");
        }
        if (System.getProperty("io.netty.leakDetection.level") == null) {
            ResourceLeakDetector.setLevel(Level.DISABLED);
        }
        IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_BUS.addListener(this::commonSetup);
        MOD_BUS.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Compression will use " + Natives.compress.getLoadedVariant() + ", encryption will use " + Natives.cipher.getLoadedVariant());
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }
}