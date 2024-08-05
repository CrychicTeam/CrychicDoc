package org.embeddedt.embeddium.bootstrap;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.modlauncher.api.IModuleLayerManager.Layer;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.IntSupplier;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import net.minecraftforge.fml.loading.ImmediateWindowProvider;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddiumEarlyWindowHacks {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-FMLEarlyWindow");

    public static void createEarlyLaunchWindow(IntSupplier width, IntSupplier height) {
        Optional<ImmediateWindowProvider> newProviderOpt = ServiceLoader.load(ImmediateWindowProvider.class).stream().map(Provider::get).filter(p -> p.name().equals("fmlearlywindow")).findFirst();
        if (newProviderOpt.isEmpty()) {
            LOGGER.warn("Failed to find FML early window implementation, aborting");
        } else {
            ImmediateWindowProvider newProvider = (ImmediateWindowProvider) newProviderOpt.get();
            try {
                Field f = ImmediateWindowHandler.class.getDeclaredField("provider");
                f.setAccessible(true);
                ImmediateWindowProvider oldProvider = (ImmediateWindowProvider) f.get(null);
                if (!oldProvider.name().equals("dummyprovider")) {
                    LOGGER.error("Did not find dummy provider as we expected, found {}. Aborting.", oldProvider.getClass().getName());
                    return;
                }
                f.set(null, newProviderOpt.get());
            } catch (ReflectiveOperationException var10) {
                throw new RuntimeException("Exception setting new provider", var10);
            }
            FMLLoader.progressWindowTick = newProvider.initialize(new String[] { "--fml.mcVersion", FMLLoader.versionInfo().mcVersion(), "--fml.forgeVersion", FMLLoader.versionInfo().forgeVersion(), "--width", String.valueOf(width.getAsInt()), "--height", String.valueOf(height.getAsInt()) });
            ModFileInfo modInfo = LoadingModList.get().getModFileById("embeddium");
            String ourVersion = modInfo != null ? modInfo.versionString() : "unknown";
            StartupMessageManager.modLoaderConsumer().ifPresent(c -> c.accept("Embeddium " + ourVersion));
            ImmediateWindowHandler.acceptGameLayer((ModuleLayer) ((IModuleLayerManager) Launcher.INSTANCE.findLayerManager().orElseThrow()).getLayer(Layer.GAME).orElseThrow());
            try {
                Field windowTickField = newProvider.getClass().getDeclaredField("windowTick");
                windowTickField.setAccessible(true);
                while (windowTickField.get(newProvider) == null) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var8) {
                    }
                }
            } catch (ReflectiveOperationException var9) {
                LOGGER.error("Exception thrown while waiting for window tick to be present", var9);
            }
            LOGGER.info("Successfully initialized our own early loading screen");
        }
    }
}