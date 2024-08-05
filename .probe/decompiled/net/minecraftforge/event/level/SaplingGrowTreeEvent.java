package net.minecraftforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.Nullable;

@HasResult
public class SaplingGrowTreeEvent extends LevelEvent {

    private final RandomSource randomSource;

    private final BlockPos pos;

    @Nullable
    private Holder<ConfiguredFeature<?, ?>> feature;

    public SaplingGrowTreeEvent(LevelAccessor level, RandomSource randomSource, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> feature) {
        super(level);
        this.randomSource = randomSource;
        this.pos = pos;
        this.feature = feature;
    }

    public RandomSource getRandomSource() {
        return this.randomSource;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    @Nullable
    public Holder<ConfiguredFeature<?, ?>> getFeature() {
        return this.feature;
    }

    public void setFeature(@Nullable Holder<ConfiguredFeature<?, ?>> feature) {
        this.feature = feature;
    }

    public void setFeature(ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
        this.feature = (Holder<ConfiguredFeature<?, ?>>) this.getLevel().m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(featureKey).orElse(null);
    }
}