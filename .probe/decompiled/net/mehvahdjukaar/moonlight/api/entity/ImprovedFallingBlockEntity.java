package net.mehvahdjukaar.moonlight.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class ImprovedFallingBlockEntity extends FallingBlockEntity {

    protected boolean saveTileDataToItem;

    public ImprovedFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);
        this.saveTileDataToItem = false;
    }

    public ImprovedFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level, BlockPos pos, BlockState blockState, boolean saveDataToItem) {
        super(type, level);
        this.f_19850_ = true;
        this.f_19854_ = (double) pos.m_123341_() + 0.5;
        this.f_19855_ = (double) pos.m_123342_();
        this.f_19856_ = (double) pos.m_123343_() + 0.5;
        this.m_6034_(this.f_19854_, this.f_19855_ + (double) ((1.0F - this.m_20206_()) / 2.0F), this.f_19856_);
        this.m_20256_(Vec3.ZERO);
        this.m_31959_(this.m_20183_());
        this.setBlockState(blockState);
        this.saveTileDataToItem = saveDataToItem;
    }

    public static ImprovedFallingBlockEntity fall(EntityType<? extends FallingBlockEntity> type, Level level, BlockPos pos, BlockState state, boolean saveDataToItem) {
        ImprovedFallingBlockEntity entity = new ImprovedFallingBlockEntity(type, level, pos, state, saveDataToItem);
        level.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
        level.m_7967_(entity);
        return entity;
    }

    public void setSaveTileDataToItem(boolean b) {
        this.saveTileDataToItem = b;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("saveToItem", this.saveTileDataToItem);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.saveTileDataToItem = tag.getBoolean("saveToItem");
    }

    public void setBlockState(BlockState state) {
        if (state.m_61138_(BlockStateProperties.WATERLOGGED)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, false);
        }
        CompoundTag tag = new CompoundTag();
        tag.put("BlockState", NbtUtils.writeBlockState(state));
        tag.putInt("Time", this.f_31942_);
        this.readAdditionalSaveData(tag);
    }

    @Override
    public ItemEntity spawnAtLocation(ItemLike itemIn, int offset) {
        ItemStack stack = new ItemStack(itemIn);
        if (itemIn instanceof Block && this.saveTileDataToItem && this.f_31944_ != null) {
            stack.addTagElement("BlockEntityTag", this.f_31944_);
        }
        return this.m_5552_(stack, (float) offset);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return super.causeFallDamage(pFallDistance, pMultiplier, pSource);
    }

    public void setCancelDrop(boolean cancelDrop) {
        this.f_31947_ = cancelDrop;
    }
}