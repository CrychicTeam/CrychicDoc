package fuzs.puzzleslib.api.data.v2.core;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public class ForgeDataProviderContext extends DataProviderContext {

    private final ExistingFileHelper fileHelper;

    public ForgeDataProviderContext(String modId, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(modId, packOutput, () -> lookupProvider);
        this.fileHelper = fileHelper;
    }

    public static ForgeDataProviderContext fromEvent(String modId, GatherDataEvent evt) {
        return new ForgeDataProviderContext(modId, evt.getGenerator().getPackOutput(), evt.getLookupProvider(), evt.getExistingFileHelper());
    }

    public ExistingFileHelper getFileHelper() {
        return this.fileHelper;
    }

    @FunctionalInterface
    public interface Factory extends Function<ForgeDataProviderContext, DataProvider> {
    }

    @FunctionalInterface
    public interface LegacyFactory extends BiFunction<GatherDataEvent, String, DataProvider> {
    }
}