package fuzs.puzzleslib.api.data.v2.core;

import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.impl.data.ExistingFileHelperHolder;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public final class DataProviderHelper {

    private DataProviderHelper() {
    }

    public static void registerDataProviders(String modId, ForgeDataProviderContext.Factory... factories) {
        if (ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) {
            Objects.checkIndex(0, factories.length);
            registerDataProviders(modId, (ForgeDataProviderContext.LegacyFactory[]) Stream.of(factories).map(factory -> (evt, $) -> (DataProvider) factory.apply(ForgeDataProviderContext.fromEvent(modId, evt))).toArray(ForgeDataProviderContext.LegacyFactory[]::new));
        }
    }

    public static void registerDataProviders(String modId, ForgeDataProviderContext.LegacyFactory... factories) {
        if (ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) {
            Objects.checkIndex(0, factories.length);
            ModContainerHelper.getOptionalModEventBus(modId).ifPresent(eventBus -> eventBus.addListener(evt -> onGatherData(evt, modId, factories)));
        }
    }

    static void onGatherData(GatherDataEvent evt, String modId, ForgeDataProviderContext.LegacyFactory... factories) {
        Objects.checkIndex(0, factories.length);
        DataGenerator dataGenerator = evt.getGenerator();
        for (ForgeDataProviderContext.LegacyFactory factory : factories) {
            DataProvider dataProvider = (DataProvider) factory.apply(evt, modId);
            if (dataProvider instanceof ExistingFileHelperHolder holder) {
                holder.puzzleslib$setExistingFileHelper(evt.getExistingFileHelper());
            }
            dataGenerator.addProvider(true, dataProvider);
        }
    }
}