package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACLootTableRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class AbyssalRuinsStructurePiece extends TemplateStructurePiece {

    public AbyssalRuinsStructurePiece(StructureTemplateManager manager, ResourceLocation resourceLocation, BlockPos pos, Rotation rotation) {
        super(ACStructurePieceRegistry.ABYSSAL_RUINS.get(), 0, manager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), pos);
    }

    public AbyssalRuinsStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(ACStructurePieceRegistry.ABYSSAL_RUINS.get(), tag, manager, x -> makeSettings(Rotation.valueOf(tag.getString("Rotation"))));
    }

    public AbyssalRuinsStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        this(context.structureTemplateManager(), tag);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).setKeepLiquids(false);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString("Rotation", this.f_73657_.getRotation().name());
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        int i = worldGenLevel.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, this.f_73658_.m_123341_(), this.f_73658_.m_123343_());
        this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), i, this.f_73658_.m_123343_());
        BlockPos blockpos = StructureTemplate.transform(new BlockPos(this.f_73656_.getSize().getX() - 1, 0, this.f_73656_.getSize().getZ() - 1), Mirror.NONE, this.f_73657_.getRotation(), BlockPos.ZERO).offset(this.f_73658_);
        this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), this.getHeight(this.f_73658_, worldGenLevel, blockpos), this.f_73658_.m_123343_());
        if (this.f_73658_.m_123342_() > chunkGenerator.getSeaLevel() - 40) {
            this.f_73658_ = this.f_73658_.atY(-128);
        }
        super.postProcess(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, pos);
    }

    private int getHeight(BlockPos blockPos, BlockGetter level, BlockPos pos) {
        int i = blockPos.m_123342_();
        int j = 512;
        int k = i - 1;
        int l = 0;
        for (BlockPos blockpos : BlockPos.betweenClosed(blockPos, pos)) {
            int i1 = blockpos.m_123341_();
            int j1 = blockpos.m_123343_();
            int k1 = blockPos.m_123342_() - 1;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(i1, k1, j1);
            BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
            for (FluidState fluidstate = level.getFluidState(blockpos$mutableblockpos); (blockstate.m_60795_() || fluidstate.is(FluidTags.WATER) || blockstate.m_204336_(BlockTags.ICE)) && k1 > level.m_141937_() + 1; fluidstate = level.getFluidState(blockpos$mutableblockpos)) {
                blockpos$mutableblockpos.set(i1, --k1, j1);
                blockstate = level.getBlockState(blockpos$mutableblockpos);
            }
            j = Math.min(j, k1);
            if (k1 < k - 2) {
                l++;
            }
        }
        int l1 = Math.abs(blockPos.m_123341_() - pos.m_123341_());
        if (k - j > 2 && l > l1 - 2) {
            i = j + 1;
        }
        return i;
    }

    @Override
    protected void handleDataMarker(String string, BlockPos pos, ServerLevelAccessor accessor, RandomSource random, BoundingBox box) {
        accessor.m_7731_(pos, Blocks.CAVE_AIR.defaultBlockState(), 0);
        switch(string) {
            case "loot_chest":
                RandomizableContainerBlockEntity.setLootTable(accessor, random, pos.below(), ACLootTableRegistry.ABYSSAL_RUINS_CHEST);
                break;
            case "submarine":
                this.spawnSubmarine(accessor, pos, false);
                break;
            case "submarine_damaged":
                if (random.nextFloat() > 0.5F) {
                    this.spawnSubmarine(accessor, pos, true);
                }
        }
    }

    private void spawnSubmarine(ServerLevelAccessor level, BlockPos pos, boolean totaled) {
        SubmarineEntity submarine = ACEntityRegistry.SUBMARINE.get().create(level.getLevel());
        while (level.m_8055_(pos).m_60819_().isEmpty() && !level.m_46859_(pos) && pos.m_123342_() < level.m_151558_()) {
            pos = pos.above();
        }
        Vec3 vec31 = Vec3.atCenterOf(pos);
        submarine.m_146922_(level.m_213780_().nextFloat() * 360.0F);
        submarine.m_6034_(vec31.x, vec31.y, vec31.z);
        while (!level.m_45784_(submarine)) {
            submarine.m_146884_(submarine.m_20182_().add(new Vec3(0.0, 1.0, 0.0)));
        }
        if (totaled) {
            submarine.setDamageLevel(4);
            submarine.setOxidizationLevel(3);
        } else {
            submarine.setOxidizationLevel(2);
        }
        level.m_7967_(submarine);
    }
}