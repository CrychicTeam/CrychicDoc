package net.minecraft.world.level.levelgen.structure.pieces;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@FunctionalInterface
public interface PieceGenerator<C extends FeatureConfiguration> {

    void generatePieces(StructurePiecesBuilder var1, PieceGenerator.Context<C> var2);

    public static record Context<C extends FeatureConfiguration>(C f_197328_, ChunkGenerator f_192703_, StructureTemplateManager f_226931_, ChunkPos f_192705_, LevelHeightAccessor f_192707_, WorldgenRandom f_192708_, long f_192709_) {

        private final C config;

        private final ChunkGenerator chunkGenerator;

        private final StructureTemplateManager structureTemplateManager;

        private final ChunkPos chunkPos;

        private final LevelHeightAccessor heightAccessor;

        private final WorldgenRandom random;

        private final long seed;

        public Context(C f_197328_, ChunkGenerator f_192703_, StructureTemplateManager f_226931_, ChunkPos f_192705_, LevelHeightAccessor f_192707_, WorldgenRandom f_192708_, long f_192709_) {
            this.config = f_197328_;
            this.chunkGenerator = f_192703_;
            this.structureTemplateManager = f_226931_;
            this.chunkPos = f_192705_;
            this.heightAccessor = f_192707_;
            this.random = f_192708_;
            this.seed = f_192709_;
        }
    }
}