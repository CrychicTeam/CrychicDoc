package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SmartChuteBlockEntity extends ChuteBlockEntity {

    FilteringBehaviour filtering;

    public SmartChuteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean canAcceptItem(ItemStack stack) {
        return super.canAcceptItem(stack) && this.canCollectItemsFromBelow() && this.filtering.test(stack);
    }

    @Override
    protected int getExtractionAmount() {
        return this.filtering.isCountVisible() && !this.filtering.anyAmount() ? this.filtering.getAmount() : 64;
    }

    @Override
    protected ItemHelper.ExtractionCountMode getExtractionMode() {
        return this.filtering.isCountVisible() && !this.filtering.anyAmount() && !this.filtering.upTo ? ItemHelper.ExtractionCountMode.EXACTLY : ItemHelper.ExtractionCountMode.UPTO;
    }

    @Override
    protected boolean canCollectItemsFromBelow() {
        BlockState blockState = this.m_58900_();
        return blockState.m_61138_(SmartChuteBlock.POWERED) && !(Boolean) blockState.m_61143_(SmartChuteBlock.POWERED);
    }

    @Override
    protected boolean canOutputItems() {
        BlockState blockState = this.m_58900_();
        return blockState.m_61138_(SmartChuteBlock.POWERED) && !(Boolean) blockState.m_61143_(SmartChuteBlock.POWERED);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.filtering = new FilteringBehaviour(this, new SmartChuteFilterSlotPositioning()).showCountWhen(this::isExtracting).withCallback($ -> this.invVersionTracker.reset()));
        super.addBehaviours(behaviours);
    }

    private boolean isExtracting() {
        boolean up = this.getItemMotion() < 0.0F;
        BlockPos chutePos = this.f_58858_.relative(up ? Direction.UP : Direction.DOWN);
        BlockState blockState = this.f_58857_.getBlockState(chutePos);
        return !AbstractChuteBlock.isChute(blockState) && !blockState.m_247087_();
    }
}