package snownee.loquat.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface LoquatPlacer {

    boolean accept(ResourceLocation var1);

    Structure.GenerationStub place(ResourceLocation var1, Structure.GenerationContext var2, BlockPos var3, VoxelShape var4, int var5, Registry<StructureTemplatePool> var6, PoolElementStructurePiece var7);
}