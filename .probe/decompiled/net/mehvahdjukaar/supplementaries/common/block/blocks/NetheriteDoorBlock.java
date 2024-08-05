package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NetheriteDoorBlock extends DoorBlock implements EntityBlock {

    public NetheriteDoorBlock(BlockBehaviour.Properties builder) {
        super(builder, BlockSetType.IRON);
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return SoundType.NETHERITE_BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockPos p = this.hasTileEntity(state) ? pos : pos.below();
        if (level.getBlockEntity(p) instanceof KeyLockableTile keyLockableTile && keyLockableTile.handleAction(player, handIn, "door")) {
            GoldDoorBlock.tryOpenDoubleDoorKey(level, state, pos, player, handIn);
            state = (BlockState) state.m_61122_(f_52727_);
            level.setBlock(pos, state, 10);
            boolean open = (Boolean) state.m_61143_(f_52727_);
            this.m_245755_(player, level, pos, (Boolean) state.m_61143_(f_52727_));
            level.m_142346_(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : (BlockState) ((BlockState) state.m_61124_(f_52727_, false)).m_61124_(f_52729_, false);
    }

    public boolean hasTileEntity(BlockState state) {
        return state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return this.hasTileEntity(pState) ? new KeyLockableTile(pPos, pState) : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_5871_(stack, worldIn, tooltip, flagIn);
        if (MiscUtils.showsHints(worldIn, flagIn)) {
            tooltip.add(Component.translatable("message.supplementaries.key.lockable").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
    }
}