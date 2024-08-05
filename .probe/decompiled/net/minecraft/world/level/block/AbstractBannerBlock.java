package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBannerBlock extends BaseEntityBlock {

    private final DyeColor color;

    protected AbstractBannerBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new BannerBlockEntity(blockPos0, blockState1, this.color);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        if (level0.isClientSide) {
            level0.m_141902_(blockPos1, BlockEntityType.BANNER).ifPresent(p_187404_ -> p_187404_.fromItem(itemStack4));
        } else if (itemStack4.hasCustomHoverName()) {
            level0.m_141902_(blockPos1, BlockEntityType.BANNER).ifPresent(p_187401_ -> p_187401_.setCustomName(itemStack4.getHoverName()));
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        BlockEntity $$3 = blockGetter0.getBlockEntity(blockPos1);
        return $$3 instanceof BannerBlockEntity ? ((BannerBlockEntity) $$3).getItem() : super.m_7397_(blockGetter0, blockPos1, blockState2);
    }

    public DyeColor getColor() {
        return this.color;
    }
}