package yesman.epicfight.compat;

import java.lang.reflect.Constructor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.main.EpicFightMod;

public interface ICompatModule {

    static void loadCompatModule(Class<? extends ICompatModule> compatModule) {
        try {
            Constructor<? extends ICompatModule> constructor = compatModule.getConstructor();
            ICompatModule compatModuleInstance = (ICompatModule) constructor.newInstance();
            compatModuleInstance.onModEventBus(FMLJavaModLoadingContext.get().getModEventBus());
            compatModuleInstance.onForgeEventBus(MinecraftForge.EVENT_BUS);
            EpicFightMod.LOGGER.info("Loaded mod compat: " + compatModule.getSimpleName());
        } catch (Exception var3) {
            EpicFightMod.LOGGER.error("Failed to load mod compat: " + var3.getMessage());
            var3.printStackTrace();
        }
    }

    static void loadCompatModuleClient(Class<? extends ICompatModule> compatModule) {
        try {
            Constructor<? extends ICompatModule> constructor = compatModule.getConstructor();
            ICompatModule compatModuleInstance = (ICompatModule) constructor.newInstance();
            compatModuleInstance.onModEventBusClient(FMLJavaModLoadingContext.get().getModEventBus());
            compatModuleInstance.onForgeEventBusClient(MinecraftForge.EVENT_BUS);
            EpicFightMod.LOGGER.info("Loaded mod compat: " + compatModule.getSimpleName());
        } catch (Exception var3) {
            EpicFightMod.LOGGER.error("Failed to load mod compat: " + var3.getMessage());
            var3.printStackTrace();
        }
    }

    void onModEventBus(IEventBus var1);

    void onForgeEventBus(IEventBus var1);

    void onModEventBusClient(IEventBus var1);

    void onForgeEventBusClient(IEventBus var1);
}