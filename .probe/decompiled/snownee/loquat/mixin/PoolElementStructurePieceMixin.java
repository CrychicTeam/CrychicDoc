package snownee.loquat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.loquat.duck.LoquatStructurePiece;

@Mixin({ PoolElementStructurePiece.class })
public class PoolElementStructurePieceMixin implements LoquatStructurePiece {

    @Unique
    CompoundTag loquat$attachedData;

    @Override
    public void loquat$setAttachedData(CompoundTag tag) {
        this.loquat$attachedData = tag;
    }

    @Override
    public CompoundTag loquat$getAttachedData() {
        return this.loquat$attachedData;
    }

    @Inject(method = { "place" }, at = { @At("HEAD") })
    private void loquat$beforePlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, BlockPos pos, boolean keepJigsaws, CallbackInfo ci) {
        LoquatStructurePiece.CURRENT.set(Pair.of(this, level.m_9598_()));
    }

    @Inject(method = { "place" }, at = { @At("RETURN") })
    private void loquat$afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, BlockPos pos, boolean keepJigsaws, CallbackInfo ci) {
        LoquatStructurePiece.CURRENT.remove();
    }
}