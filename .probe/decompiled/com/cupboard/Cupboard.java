package com.cupboard;

import com.cupboard.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import com.cupboard.event.EventHandler;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;
import java.util.Random;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("cupboard")
public class Cupboard {

    public static final String MOD_ID = "cupboard";

    public static final Logger LOGGER = LogManager.getLogger();

    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>("cupboard", new CommonConfiguration());

    public static Random rand = new Random();

    public Cupboard() {
        if (config.getCommonConfig().forceHeapDumpOnOOM) {
            try {
                HotSpotDiagnosticMXBean bean = (HotSpotDiagnosticMXBean) ManagementFactory.newPlatformMXBeanProxy(ManagementFactory.getPlatformMBeanServer(), "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
                bean.setVMOption("HeapDumpOnOutOfMemoryError", "true");
                bean.setVMOption("HeapDumpPath", FMLPaths.GAMEDIR.get().toString());
            } catch (Exception var2) {
                LOGGER.error("Failed to enable heapdump option: ", var2);
            }
        }
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "", (a, b) -> true));
        ((IEventBus) Bus.FORGE.bus().get()).register(EventHandler.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        CupboardClient.onInitializeClient(event);
    }
}