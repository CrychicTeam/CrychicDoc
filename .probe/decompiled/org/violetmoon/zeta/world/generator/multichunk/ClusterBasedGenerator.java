package org.violetmoon.zeta.world.generator.multichunk;

import java.util.Random;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.violetmoon.zeta.config.type.ClusterSizeConfig;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BooleanSuppliers;

public abstract class ClusterBasedGenerator extends MultiChunkFeatureGenerator {

    public final ClusterShape.Provider shapeProvider;

    public ClusterBasedGenerator(DimensionConfig dimConfig, ClusterSizeConfig sizeConfig, long seedXor) {
        this(dimConfig, BooleanSuppliers.TRUE, sizeConfig, seedXor);
    }

    public ClusterBasedGenerator(DimensionConfig dimConfig, BooleanSupplier condition, ClusterSizeConfig sizeConfig, long seedXor) {
        super(dimConfig, condition, seedXor);
        this.shapeProvider = new ClusterShape.Provider(sizeConfig, seedXor);
    }

    @Override
    public int getFeatureRadius() {
        return this.shapeProvider.getRadius();
    }

    @Override
    public void generateChunkPart(BlockPos src, ChunkGenerator generator, Random random, BlockPos chunkCorner, WorldGenRegion world) {
        ClusterShape shape = this.shapeProvider.around(src);
        ClusterBasedGenerator.IGenerationContext context = this.createContext(src, generator, random, chunkCorner, world);
        this.forEachChunkBlock(world, chunkCorner, shape.getLowerBound(), shape.getUpperBound(), pos -> {
            if (context.canPlaceAt(pos) && shape.isInside(pos)) {
                context.consume(pos);
            }
        });
    }

    public abstract ClusterBasedGenerator.IGenerationContext createContext(BlockPos var1, ChunkGenerator var2, Random var3, BlockPos var4, WorldGenRegion var5);

    public interface IGenerationContext {

        boolean canPlaceAt(BlockPos var1);

        void consume(BlockPos var1);
    }
}