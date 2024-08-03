package fuzs.puzzleslib.api.core.v1.context;

import fuzs.puzzleslib.api.biome.v1.BiomeLoadingContext;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.BiomeModificationContext;
import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface BiomeModificationsContext {

    void register(BiomeLoadingPhase var1, Predicate<BiomeLoadingContext> var2, Consumer<BiomeModificationContext> var3);
}