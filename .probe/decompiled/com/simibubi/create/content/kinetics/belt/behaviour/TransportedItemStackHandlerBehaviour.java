package com.simibubi.create.content.kinetics.belt.behaviour;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TransportedItemStackHandlerBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<TransportedItemStackHandlerBehaviour> TYPE = new BehaviourType<>();

    private TransportedItemStackHandlerBehaviour.ProcessingCallback processingCallback;

    private TransportedItemStackHandlerBehaviour.PositionGetter positionGetter;

    public TransportedItemStackHandlerBehaviour(SmartBlockEntity be, TransportedItemStackHandlerBehaviour.ProcessingCallback processingCallback) {
        super(be);
        this.processingCallback = processingCallback;
        this.positionGetter = t -> VecHelper.getCenterOf(be.m_58899_());
    }

    public TransportedItemStackHandlerBehaviour withStackPlacement(TransportedItemStackHandlerBehaviour.PositionGetter function) {
        this.positionGetter = function;
        return this;
    }

    public void handleProcessingOnAllItems(Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        this.handleCenteredProcessingOnAllItems(0.51F, processFunction);
    }

    public void handleProcessingOnItem(TransportedItemStack item, TransportedItemStackHandlerBehaviour.TransportedResult processOutput) {
        this.handleCenteredProcessingOnAllItems(0.51F, t -> t == item ? processOutput : null);
    }

    public void handleCenteredProcessingOnAllItems(float maxDistanceFromCenter, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        this.processingCallback.applyToAllItems(maxDistanceFromCenter, processFunction);
    }

    public Vec3 getWorldPositionOf(TransportedItemStack transported) {
        return this.positionGetter.getWorldPositionVector(transported);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @FunctionalInterface
    public interface PositionGetter {

        Vec3 getWorldPositionVector(TransportedItemStack var1);
    }

    @FunctionalInterface
    public interface ProcessingCallback {

        void applyToAllItems(float var1, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> var2);
    }

    public static class TransportedResult {

        List<TransportedItemStack> outputs;

        TransportedItemStack heldOutput;

        private static final TransportedItemStackHandlerBehaviour.TransportedResult DO_NOTHING = new TransportedItemStackHandlerBehaviour.TransportedResult(null, null);

        private static final TransportedItemStackHandlerBehaviour.TransportedResult REMOVE_ITEM = new TransportedItemStackHandlerBehaviour.TransportedResult(ImmutableList.of(), null);

        public static TransportedItemStackHandlerBehaviour.TransportedResult doNothing() {
            return DO_NOTHING;
        }

        public static TransportedItemStackHandlerBehaviour.TransportedResult removeItem() {
            return REMOVE_ITEM;
        }

        public static TransportedItemStackHandlerBehaviour.TransportedResult convertTo(TransportedItemStack output) {
            return new TransportedItemStackHandlerBehaviour.TransportedResult(ImmutableList.of(output), null);
        }

        public static TransportedItemStackHandlerBehaviour.TransportedResult convertTo(List<TransportedItemStack> outputs) {
            return new TransportedItemStackHandlerBehaviour.TransportedResult(outputs, null);
        }

        public static TransportedItemStackHandlerBehaviour.TransportedResult convertToAndLeaveHeld(List<TransportedItemStack> outputs, TransportedItemStack heldOutput) {
            return new TransportedItemStackHandlerBehaviour.TransportedResult(outputs, heldOutput);
        }

        private TransportedResult(List<TransportedItemStack> outputs, TransportedItemStack heldOutput) {
            this.outputs = outputs;
            this.heldOutput = heldOutput;
        }

        public boolean doesNothing() {
            return this.outputs == null;
        }

        public boolean didntChangeFrom(ItemStack stackBefore) {
            return this.doesNothing() || this.outputs.size() == 1 && ((TransportedItemStack) this.outputs.get(0)).stack.equals(stackBefore, false) && !this.hasHeldOutput();
        }

        public List<TransportedItemStack> getOutputs() {
            if (this.outputs == null) {
                throw new IllegalStateException("Do not call getOutputs() on a Result that doesNothing().");
            } else {
                return this.outputs;
            }
        }

        public boolean hasHeldOutput() {
            return this.heldOutput != null;
        }

        @Nullable
        public TransportedItemStack getHeldOutput() {
            if (this.heldOutput == null) {
                throw new IllegalStateException("Do not call getHeldOutput() on a Result with hasHeldOutput() == false.");
            } else {
                return this.heldOutput;
            }
        }
    }
}