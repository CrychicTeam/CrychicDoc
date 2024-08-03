package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {

    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadWoodLock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava().strength(-1.0F, 1000000.0F).sound(SoundType.WOOD));
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(PLAYER_PLACED, Boolean.FALSE));
    }

    @Override
    public float getDestroyProgress(BlockState state, @NotNull Player player, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        if ((Boolean) state.m_61143_(PLAYER_PLACED)) {
            float f = 8.0F;
            return player.getDigSpeed(state, pos) / f / 30.0F;
        } else {
            return super.m_5880_(state, player, worldIn, pos);
        }
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult resultIn) {
        ItemStack stack = player.m_21120_(handIn);
        if (stack.getItem() == IafItemRegistry.DREAD_KEY.get()) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.deleteNearbyWood(worldIn, pos, pos);
            worldIn.playLocalSound((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            worldIn.playLocalSound((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1.0F, 2.0F, false);
        }
        return InteractionResult.SUCCESS;
    }

    private void deleteNearbyWood(Level worldIn, BlockPos pos, BlockPos startPos) {
        if (pos.m_123331_(startPos) < 32.0 && (worldIn.getBlockState(pos).m_60734_() == IafBlockRegistry.DREADWOOD_PLANKS.get() || worldIn.getBlockState(pos).m_60734_() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK.get())) {
            worldIn.m_46961_(pos, false);
            for (Direction facing : Direction.values()) {
                this.deleteNearbyWood(worldIn, pos.relative(facing), startPos);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(Level worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return (BlockState) this.m_49966_().m_61124_(PLAYER_PLACED, true);
    }
}