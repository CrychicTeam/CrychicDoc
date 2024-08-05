package snownee.loquat.mixin;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.placement.GenerationContextExtension;
import snownee.loquat.placement.LoquatPlacements;
import snownee.loquat.placement.LoquatPlacer;

@Mixin({ JigsawPlacement.class })
public class JigsawPlacementMixin {

    @Inject(method = { "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;<init>(Lnet/minecraft/core/BlockPos;Ljava/util/function/Consumer;)V") }, cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void loquat$addPieces(Structure.GenerationContext context, Holder<StructureTemplatePool> holder, Optional<ResourceLocation> optional, int size, BlockPos blockPos, boolean bl, Optional<Heightmap.Types> optional2, int range, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir, RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, LevelHeightAccessor levelHeightAccessor, WorldgenRandom worldgenRandom, Registry<StructureTemplatePool> poolRegistry, Rotation rotation, StructureTemplatePool structureTemplatePool, StructurePoolElement structurePoolElement, BlockPos blockPos2, Vec3i vec3i, BlockPos blockPos3, PoolElementStructurePiece defaultStartPiece, BoundingBox boundingBox, int k, int l, int m, int n, int o) {
        GenerationContextExtension extension = (GenerationContextExtension) GenerationContextExtension.CACHE.getIfPresent(context);
        if (extension != null) {
            LoquatPlacer placer = LoquatPlacements.getPlacerFor(extension.structureId());
            if (placer != null) {
                BlockPos defaultStartPos = new BlockPos(k, o, l);
                AABB aabb = new AABB((double) (k - range), (double) (o - range), (double) (l - range), (double) (k + range + 1), (double) (o + range + 1), (double) (l + range + 1));
                VoxelShape defaultValidSpace = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingBox)), BooleanOp.ONLY_FIRST);
                cir.setReturnValue(Optional.of(placer.place(extension.structureId(), context, defaultStartPos, defaultValidSpace, range, poolRegistry, defaultStartPiece)));
            }
        }
    }
}