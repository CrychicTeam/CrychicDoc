package brightspark.asynclocator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("asynclocator")
public class AsyncLocatorModForge {

    public AsyncLocatorModForge() {
        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (serverVersion, networkBool) -> true));
        ctx.registerConfig(Type.SERVER, AsyncLocatorConfigForge.SPEC);
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(event -> AsyncLocator.setupExecutorService());
        forgeEventBus.addListener(event -> AsyncLocator.shutdownExecutorService());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> AsyncLocatorModCommon.printConfigs());
    }
}