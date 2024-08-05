package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LockBlock extends Block implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public LockBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if ((Boolean) state.m_61143_(POWERED)) {
            return InteractionResult.PASS;
        } else {
            if (worldIn.getBlockEntity(pos) instanceof KeyLockableTile tile && tile.handleAction(player, handIn, "lock_block")) {
                this.activate(state, worldIn, pos, player);
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
    }

    public void activate(BlockState state, Level worldIn, BlockPos pos, Player player) {
        if (!worldIn.isClientSide) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(POWERED, true), 7);
            worldIn.m_186460_(pos, this, 20);
        }
        this.playSound(player, worldIn, pos, true);
    }

    protected void playSound(@Nullable Player player, Level level, BlockPos pos, boolean isOpened) {
        level.playSound(player, pos, isOpened ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        super.m_213897_(state, worldIn, pos, rand);
        if (!worldIn.f_46443_ && (Boolean) state.m_61143_(POWERED)) {
            worldIn.m_7731_(pos, (BlockState) state.m_61124_(POWERED, false), 7);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(POWERED) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new KeyLockableTile(pPos, pState);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (MiscUtils.showsHints(worldIn, flagIn)) {
            tooltip.add(Component.translatable("message.supplementaries.key.lockable").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
    }
}