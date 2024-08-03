package me.jellysquid.mods.lithium.mixin.chunk.palette;

import me.jellysquid.mods.lithium.common.world.chunk.LithiumHashPalette;
import net.minecraft.core.IdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ PalettedContainer.Strategy.class })
public abstract class PalettedContainerMixin {

    @Mutable
    @Shadow
    @Final
    public static PalettedContainer.Strategy SECTION_STATES;

    @Unique
    private static final PalettedContainer.Configuration<?>[] BLOCKSTATE_DATA_PROVIDERS;

    @Unique
    private static final PalettedContainer.Configuration<?>[] BIOME_DATA_PROVIDERS = new PalettedContainer.Configuration[] { new PalettedContainer.Configuration(PalettedContainer.Strategy.SINGLE_VALUE_PALETTE_FACTORY, 0), new PalettedContainer.Configuration(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 1), new PalettedContainer.Configuration(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 2), new PalettedContainer.Configuration(PalettedContainerMixin.HASH, 3) };

    @Unique
    private static final Palette.Factory HASH = LithiumHashPalette::create;

    @Mutable
    @Shadow
    @Final
    public static PalettedContainer.Strategy SECTION_BIOMES;

    @Shadow
    @Final
    static Palette.Factory GLOBAL_PALETTE_FACTORY;

    static {
        final Palette.Factory idListFactory = GLOBAL_PALETTE_FACTORY;
        PalettedContainer.Configuration<?> arrayDataProvider4bit = new PalettedContainer.Configuration(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 4);
        PalettedContainer.Configuration<?> hashDataProvider4bit = new PalettedContainer.Configuration(HASH, 4);
        BLOCKSTATE_DATA_PROVIDERS = new PalettedContainer.Configuration[] { new PalettedContainer.Configuration(PalettedContainer.Strategy.SINGLE_VALUE_PALETTE_FACTORY, 0), arrayDataProvider4bit, arrayDataProvider4bit, hashDataProvider4bit, hashDataProvider4bit, new PalettedContainer.Configuration(HASH, 5), new PalettedContainer.Configuration(HASH, 6), new PalettedContainer.Configuration(HASH, 7), new PalettedContainer.Configuration(HASH, 8) };
        SECTION_STATES = new PalettedContainer.Strategy(4) {

            @Override
            public <A> PalettedContainer.Configuration<A> getConfiguration(IdMap<A> idList, int bits) {
                return (PalettedContainer.Configuration<A>) (bits >= 0 && bits < PalettedContainerMixin.BLOCKSTATE_DATA_PROVIDERS.length ? PalettedContainerMixin.BLOCKSTATE_DATA_PROVIDERS[bits] : new PalettedContainer.Configuration<>(idListFactory, Mth.ceillog2(idList.size())));
            }
        };
        SECTION_BIOMES = new PalettedContainer.Strategy(2) {

            @Override
            public <A> PalettedContainer.Configuration<A> getConfiguration(IdMap<A> idList, int bits) {
                return (PalettedContainer.Configuration<A>) (bits >= 0 && bits < PalettedContainerMixin.BIOME_DATA_PROVIDERS.length ? PalettedContainerMixin.BIOME_DATA_PROVIDERS[bits] : new PalettedContainer.Configuration<>(idListFactory, Mth.ceillog2(idList.size())));
            }
        };
    }
}