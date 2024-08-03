package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WindmillBearingBlock extends BearingBlock implements IBE<WindmillBearingBlockEntity> {

    public WindmillBearingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.m_6144_()) {
            return InteractionResult.FAIL;
        } else if (player.m_21120_(handIn).isEmpty()) {
            if (worldIn.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    if (be.running) {
                        be.disassemble();
                    } else {
                        be.assembleNextTick = true;
                    }
                });
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public Class<WindmillBearingBlockEntity> getBlockEntityClass() {
        return WindmillBearingBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WindmillBearingBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends WindmillBearingBlockEntity>) AllBlockEntityTypes.WINDMILL_BEARING.get();
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(1, 16);
    }
}