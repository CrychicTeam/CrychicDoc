package com.github.alexthe666.citadel.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CitadelLecternBlock extends LecternBlock {

    public CitadelLecternBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new CitadelLecternBlockEntity(pos, blockState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide && level.getBlockEntity(pos) instanceof CitadelLecternBlockEntity lecternBlockEntity && lecternBlockEntity.hasBook()) {
            ItemStack book = lecternBlockEntity.getBook();
            if (!book.isEmpty() && !player.getCooldowns().isOnCooldown(book.getItem())) {
                book.use(level, player, hand);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if ((Boolean) state.m_61143_(f_54467_)) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof CitadelLecternBlockEntity) {
                return ((CitadelLecternBlockEntity) blockentity).getRedstoneSignal();
            }
        }
        return 0;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState replaceState, boolean b) {
        if (!state.m_60713_(replaceState.m_60734_())) {
            if ((Boolean) state.m_61143_(f_54467_)) {
                this.popCitadelBook(state, level, pos);
            }
            if ((Boolean) state.m_61143_(f_54466_)) {
                level.updateNeighborsAt(pos.below(), this);
            }
            super.onRemove(state, level, pos, replaceState, b);
        }
    }

    private void popCitadelBook(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CitadelLecternBlockEntity lecternblockentity) {
            Direction direction = (Direction) state.m_61143_(f_54465_);
            ItemStack itemstack = lecternblockentity.getBook().copy();
            float f = 0.25F * (float) direction.getStepX();
            float f1 = 0.25F * (float) direction.getStepZ();
            ItemEntity itementity = new ItemEntity(level, (double) pos.m_123341_() + 0.5 + (double) f, (double) (pos.m_123342_() + 1), (double) pos.m_123343_() + 0.5 + (double) f1, itemstack);
            itementity.setDefaultPickUpDelay();
            level.m_7967_(itementity);
            lecternblockentity.clearContent();
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.LECTERN);
    }
}