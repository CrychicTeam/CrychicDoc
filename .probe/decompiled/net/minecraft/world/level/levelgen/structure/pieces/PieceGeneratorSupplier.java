package net.minecraft.world.level.levelgen.structure.pieces;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@FunctionalInterface
public interface PieceGeneratorSupplier<C extends FeatureConfiguration> {

    Optional<PieceGenerator<C>> createGenerator(PieceGeneratorSupplier.Context<C> var1);

    static <C extends FeatureConfiguration> PieceGeneratorSupplier<C> simple(Predicate<PieceGeneratorSupplier.Context<C>> predicatePieceGeneratorSupplierContextC0, PieceGenerator<C> pieceGeneratorC1) {
        Optional<PieceGenerator<C>> $$2 = Optional.of(pieceGeneratorC1);
        return p_197344_ -> predicatePieceGeneratorSupplierContextC0.test(p_197344_) ? $$2 : Optional.empty();
    }

    static <C extends FeatureConfiguration> Predicate<PieceGeneratorSupplier.Context<C>> checkForBiomeOnTop(Heightmap.Types heightmapTypes0) {
        return p_197340_ -> p_197340_.validBiomeOnTop(heightmapTypes0);
    }

    public static record Context<C extends FeatureConfiguration>(ChunkGenerator f_197352_, BiomeSource f_197353_, RandomState f_226941_, long f_197354_, ChunkPos f_197355_, C f_197356_, LevelHeightAccessor f_197357_, Predicate<Holder<Biome>> f_197358_, StructureTemplateManager f_226942_, RegistryAccess f_197360_) {

        private final ChunkGenerator chunkGenerator;

        private final BiomeSource biomeSource;

        private final RandomState randomState;

        private final long seed;

        private final ChunkPos chunkPos;

        private final C config;

        private final LevelHeightAccessor heightAccessor;

        private final Predicate<Holder<Biome>> validBiome;

        private final StructureTemplateManager structureTemplateManager;

        private final RegistryAccess registryAccess;

        public Context(ChunkGenerator f_197352_, BiomeSource f_197353_, RandomState f_226941_, long f_197354_, ChunkPos f_197355_, C f_197356_, LevelHeightAccessor f_197357_, Predicate<Holder<Biome>> f_197358_, StructureTemplateManager f_226942_, RegistryAccess f_197360_) {
            this.chunkGenerator = f_197352_;
            this.biomeSource = f_197353_;
            this.randomState = f_226941_;
            this.seed = f_197354_;
            this.chunkPos = f_197355_;
            this.config = f_197356_;
            this.heightAccessor = f_197357_;
            this.validBiome = f_197358_;
            this.structureTemplateManager = f_226942_;
            this.registryAccess = f_197360_;
        }

        public boolean validBiomeOnTop(Heightmap.Types p_197381_) {
            int $$1 = this.chunkPos.getMiddleBlockX();
            int $$2 = this.chunkPos.getMiddleBlockZ();
            int $$3 = this.chunkGenerator.getFirstOccupiedHeight($$1, $$2, p_197381_, this.heightAccessor, this.randomState);
            Holder<Biome> $$4 = this.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock($$1), QuartPos.fromBlock($$3), QuartPos.fromBlock($$2), this.randomState.sampler());
            return this.validBiome.test($$4);
        }
    }
}