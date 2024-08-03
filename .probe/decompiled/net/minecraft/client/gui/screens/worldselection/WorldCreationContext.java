package net.minecraft.client.gui.screens.worldselection;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;

public record WorldCreationContext(WorldOptions f_244272_, Registry<LevelStem> f_244375_, WorldDimensions f_243796_, LayeredRegistryAccess<RegistryLayer> f_243708_, ReloadableServerResources f_232990_, WorldDataConfiguration f_243842_) {

    private final WorldOptions options;

    private final Registry<LevelStem> datapackDimensions;

    private final WorldDimensions selectedDimensions;

    private final LayeredRegistryAccess<RegistryLayer> worldgenRegistries;

    private final ReloadableServerResources dataPackResources;

    private final WorldDataConfiguration dataConfiguration;

    public WorldCreationContext(WorldGenSettings p_249130_, LayeredRegistryAccess<RegistryLayer> p_248513_, ReloadableServerResources p_251786_, WorldDataConfiguration p_248593_) {
        this(p_249130_.options(), p_249130_.dimensions(), p_248513_, p_251786_, p_248593_);
    }

    public WorldCreationContext(WorldOptions p_249836_, WorldDimensions p_250641_, LayeredRegistryAccess<RegistryLayer> p_251794_, ReloadableServerResources p_250560_, WorldDataConfiguration p_248539_) {
        this(p_249836_, p_251794_.getLayer(RegistryLayer.DIMENSIONS).m_175515_(Registries.LEVEL_STEM), p_250641_, p_251794_.replaceFrom(RegistryLayer.DIMENSIONS), p_250560_, p_248539_);
    }

    public WorldCreationContext(WorldOptions f_244272_, Registry<LevelStem> f_244375_, WorldDimensions f_243796_, LayeredRegistryAccess<RegistryLayer> f_243708_, ReloadableServerResources f_232990_, WorldDataConfiguration f_243842_) {
        this.options = f_244272_;
        this.datapackDimensions = f_244375_;
        this.selectedDimensions = f_243796_;
        this.worldgenRegistries = f_243708_;
        this.dataPackResources = f_232990_;
        this.dataConfiguration = f_243842_;
    }

    public WorldCreationContext withSettings(WorldOptions p_249492_, WorldDimensions p_250298_) {
        return new WorldCreationContext(p_249492_, this.datapackDimensions, p_250298_, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration);
    }

    public WorldCreationContext withOptions(WorldCreationContext.OptionsModifier p_252288_) {
        return new WorldCreationContext((WorldOptions) p_252288_.apply(this.options), this.datapackDimensions, this.selectedDimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration);
    }

    public WorldCreationContext withDimensions(WorldCreationContext.DimensionsUpdater p_250676_) {
        return new WorldCreationContext(this.options, this.datapackDimensions, (WorldDimensions) p_250676_.apply(this.worldgenLoadContext(), this.selectedDimensions), this.worldgenRegistries, this.dataPackResources, this.dataConfiguration);
    }

    public RegistryAccess.Frozen worldgenLoadContext() {
        return this.worldgenRegistries.compositeAccess();
    }

    @FunctionalInterface
    public interface DimensionsUpdater extends BiFunction<RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {
    }

    public interface OptionsModifier extends UnaryOperator<WorldOptions> {
    }
}