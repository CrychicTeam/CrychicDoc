package com.simibubi.create.content.fluids.pipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class FluidPipeBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    public FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new FluidPipeBlockEntity.StandardPipeFluidTransportBehaviour(this));
        behaviours.add(new BracketedBlockEntityBehaviour(this, this::canHaveBracket));
        this.registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
    }

    @Override
    public void transform(StructureTransform transform) {
        BracketedBlockEntityBehaviour bracketBehaviour = this.getBehaviour(BracketedBlockEntityBehaviour.TYPE);
        if (bracketBehaviour != null) {
            bracketBehaviour.transformBracket(transform);
        }
    }

    private boolean canHaveBracket(BlockState state) {
        return !(state.m_60734_() instanceof EncasedPipeBlock);
    }

    class StandardPipeFluidTransportBehaviour extends FluidTransportBehaviour {

        public StandardPipeFluidTransportBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return (FluidPipeBlock.isPipe(state) || state.m_60734_() instanceof EncasedPipeBlock) && (Boolean) state.m_61143_((Property) FluidPipeBlock.f_55154_.get(direction));
        }

        @Override
        public FluidTransportBehaviour.AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
            FluidTransportBehaviour.AttachmentTypes attachment = super.getRenderedRimAttachment(world, pos, state, direction);
            BlockPos offsetPos = pos.relative(direction);
            BlockState otherState = world.m_8055_(offsetPos);
            if (state.m_60734_() instanceof EncasedPipeBlock && attachment != FluidTransportBehaviour.AttachmentTypes.DRAIN) {
                return FluidTransportBehaviour.AttachmentTypes.NONE;
            } else {
                if (attachment == FluidTransportBehaviour.AttachmentTypes.RIM && !FluidPipeBlock.isPipe(otherState) && !AllBlocks.MECHANICAL_PUMP.has(otherState) && !AllBlocks.ENCASED_FLUID_PIPE.has(otherState)) {
                    FluidTransportBehaviour pipeBehaviour = BlockEntityBehaviour.get(world, offsetPos, FluidTransportBehaviour.TYPE);
                    if (pipeBehaviour != null && pipeBehaviour.canHaveFlowToward(otherState, direction.getOpposite())) {
                        return FluidTransportBehaviour.AttachmentTypes.CONNECTION;
                    }
                }
                if (attachment == FluidTransportBehaviour.AttachmentTypes.RIM && !FluidPipeBlock.shouldDrawRim(world, pos, state, direction)) {
                    return FluidTransportBehaviour.AttachmentTypes.CONNECTION;
                } else {
                    return attachment == FluidTransportBehaviour.AttachmentTypes.NONE && state.m_61143_((Property) FluidPipeBlock.f_55154_.get(direction)) ? FluidTransportBehaviour.AttachmentTypes.CONNECTION : attachment;
                }
            }
        }
    }
}