package com.simibubi.create.content.kinetics.belt.behaviour;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BeltProcessingBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<BeltProcessingBehaviour> TYPE = new BehaviourType<>();

    private BeltProcessingBehaviour.ProcessingCallback onItemEnter = (s, i) -> BeltProcessingBehaviour.ProcessingResult.PASS;

    private BeltProcessingBehaviour.ProcessingCallback continueProcessing = (s, i) -> BeltProcessingBehaviour.ProcessingResult.PASS;

    public BeltProcessingBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public BeltProcessingBehaviour whenItemEnters(BeltProcessingBehaviour.ProcessingCallback callback) {
        this.onItemEnter = callback;
        return this;
    }

    public BeltProcessingBehaviour whileItemHeld(BeltProcessingBehaviour.ProcessingCallback callback) {
        this.continueProcessing = callback;
        return this;
    }

    public static boolean isBlocked(BlockGetter world, BlockPos processingSpace) {
        BlockState blockState = world.getBlockState(processingSpace.above());
        return AbstractFunnelBlock.isFunnel(blockState) ? false : !blockState.m_60812_(world, processingSpace.above()).isEmpty();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public BeltProcessingBehaviour.ProcessingResult handleReceivedItem(TransportedItemStack stack, TransportedItemStackHandlerBehaviour inventory) {
        return this.onItemEnter.apply(stack, inventory);
    }

    public BeltProcessingBehaviour.ProcessingResult handleHeldItem(TransportedItemStack stack, TransportedItemStackHandlerBehaviour inventory) {
        return this.continueProcessing.apply(stack, inventory);
    }

    @FunctionalInterface
    public interface ProcessingCallback {

        BeltProcessingBehaviour.ProcessingResult apply(TransportedItemStack var1, TransportedItemStackHandlerBehaviour var2);
    }

    public static enum ProcessingResult {

        PASS, HOLD, REMOVE
    }
}