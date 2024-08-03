package me.lucko.spark.forge;

import java.nio.file.Path;
import me.lucko.spark.forge.plugin.ForgeClientSparkPlugin;
import me.lucko.spark.forge.plugin.ForgeServerSparkPlugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("spark")
public class ForgeSparkMod {

    private ModContainer container;

    private Path configDirectory;

    public ForgeSparkMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
    }

    public String getVersion() {
        return this.container.getModInfo().getVersion().toString();
    }

    public void setup(FMLCommonSetupEvent e) {
        this.container = ModLoadingContext.get().getActiveContainer();
        this.configDirectory = FMLPaths.CONFIGDIR.get().resolve(this.container.getModId());
    }

    public void clientInit(FMLClientSetupEvent e) {
        ForgeClientSparkPlugin.register(this, e);
    }

    @SubscribeEvent
    public void serverInit(ServerAboutToStartEvent e) {
        ForgeServerSparkPlugin.register(this, e);
    }

    public Path getConfigDirectory() {
        if (this.configDirectory == null) {
            throw new IllegalStateException("Config directory not set");
        } else {
            return this.configDirectory;
        }
    }
}