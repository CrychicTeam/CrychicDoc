package net.mehvahdjukaar.supplementaries.common.entities;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.mehvahdjukaar.supplementaries.common.block.blocks.UrnBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.UrnBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FallingUrnEntity extends ImprovedFallingBlockEntity {

    public FallingUrnEntity(EntityType<FallingUrnEntity> type, Level level) {
        super(type, level);
    }

    public FallingUrnEntity(Level level, BlockPos pos, BlockState blockState) {
        super((EntityType<? extends FallingBlockEntity>) ModEntities.FALLING_URN.get(), level, pos, blockState, false);
        this.m_149656_(1.0F, 20);
    }

    public static FallingUrnEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingUrnEntity entity = new FallingUrnEntity(level, pos, state);
        level.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
        level.m_7967_(entity);
        return entity;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike pItem) {
        this.shatter();
        return null;
    }

    @Override
    public boolean causeFallDamage(float height, float amount, DamageSource source) {
        boolean r = super.causeFallDamage(height, amount, source);
        if (this.m_20184_().lengthSqr() > 0.25) {
            this.shatter();
            this.setCancelDrop(true);
            this.m_146870_();
        } else if (!this.m_20067_()) {
            this.m_9236_().m_46796_(1045, this.m_20183_(), 0);
        }
        return r;
    }

    private void shatter() {
        BlockState state = this.m_31980_();
        CompoundTag tag = this.f_31944_;
        BlockEntity tile = null;
        BlockPos pos = this.m_20183_();
        if (tag != null && !tag.isEmpty()) {
            tile = new UrnBlockTile(pos, state);
            tile.load(tag);
        }
        Level level = this.m_9236_();
        Block.dropResources(state, level, pos, tile, null, ItemStack.EMPTY);
        level.m_5898_(null, 2001, pos, Block.getId(state));
        UrnBlock.spawnExtraBrokenParticles(state, pos, level);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.shatter();
        this.m_146870_();
        return true;
    }
}