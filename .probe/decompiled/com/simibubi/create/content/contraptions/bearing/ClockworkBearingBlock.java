package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClockworkBearingBlock extends BearingBlock implements IBE<ClockworkBearingBlockEntity> {

    public ClockworkBearingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.m_6144_()) {
            return InteractionResult.FAIL;
        } else if (player.m_21120_(handIn).isEmpty()) {
            if (!worldIn.isClientSide) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    if (be.running) {
                        be.disassemble();
                    } else {
                        be.assembleNextTick = true;
                    }
                });
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public Class<ClockworkBearingBlockEntity> getBlockEntityClass() {
        return ClockworkBearingBlockEntity.class;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult resultType = super.onWrenched(state, context);
        if (!context.getLevel().isClientSide && resultType.consumesAction()) {
            this.withBlockEntityDo(context.getLevel(), context.getClickedPos(), ClockworkBearingBlockEntity::disassemble);
        }
        return resultType;
    }

    @Override
    public BlockEntityType<? extends ClockworkBearingBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ClockworkBearingBlockEntity>) AllBlockEntityTypes.CLOCKWORK_BEARING.get();
    }
}