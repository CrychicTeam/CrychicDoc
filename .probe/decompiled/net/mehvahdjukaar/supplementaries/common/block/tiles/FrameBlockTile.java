package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FeatherBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.IFrameBlock;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FrameBlockTile extends MimicBlockTile {

    public FrameBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.TIMBER_FRAME_TILE.get(), pos, state);
    }

    @Override
    public boolean setHeldBlock(BlockState state) {
        this.mimic = state;
        if (this.f_58857_ instanceof ServerLevel) {
            this.m_6596_();
            int newLight = ForgeHelper.getLightEmission(this.getHeldBlock(), this.f_58857_, this.f_58858_);
            this.f_58857_.setBlock(this.f_58858_, (BlockState) ((BlockState) ((BlockState) this.m_58900_().m_61124_(FrameBlock.HAS_BLOCK, true)).m_61124_(FrameBlock.WATERLOGGED, false)).m_61124_(FrameBlock.LIGHT_LEVEL, newLight), 3);
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
        return true;
    }

    public BlockState acceptBlock(BlockState state) {
        Block b = state.m_60734_();
        if (b == ModRegistry.DAUB.get() && (Boolean) CommonConfigs.Building.REPLACE_DAUB.get() && this.f_58857_ != null && !this.f_58857_.isClientSide && this.m_58900_().m_60734_() instanceof IFrameBlock fb) {
            Block s = fb.getFilledBlock(state.m_60734_());
            if (s != null) {
                state = s.withPropertiesOf(this.m_58900_());
                this.f_58857_.setBlock(this.f_58858_, state, 3);
                return state;
            }
        }
        this.setHeldBlock(state);
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.requestModelReload();
        }
        return state;
    }

    public InteractionResult handleInteraction(Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace, boolean canStrip) {
        ItemStack stack = player.m_21120_(hand);
        Item item = stack.getItem();
        if (Utils.mayBuild(player, pos) && !trace.isInside()) {
            if (item instanceof BlockItem blockItem && this.getHeldBlock().m_60795_()) {
                BlockPlaceContext context = new FrameBlockTile.SelfPlacementContext(player, hand, stack, trace);
                if (context.getClickedPos().equals(pos)) {
                    BlockState toPlace = blockItem.getBlock().getStateForPlacement(context);
                    if (isValidBlock(toPlace, pos, level)) {
                        if (((BlockItem) item).getBlock() instanceof SlabBlock) {
                            boolean newState = true;
                        }
                        BlockState newState = this.acceptBlock(toPlace);
                        SoundType s = newState.m_60827_();
                        level.m_142346_(player, GameEvent.BLOCK_CHANGE, pos);
                        level.playSound(player, pos, s.getPlaceSound(), SoundSource.BLOCKS, (s.getVolume() + 1.0F) / 2.0F, s.getPitch() * 0.8F);
                        if (!player.getAbilities().instabuild && !level.isClientSide()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
                return InteractionResult.FAIL;
            }
            if (canStrip && item instanceof AxeItem && !this.getHeldBlock().m_60795_() && (Boolean) CommonConfigs.Building.AXE_TIMBER_FRAME_STRIP.get()) {
                BlockState held = this.getHeldBlock();
                if (!level.isClientSide) {
                    Block.popResourceFromFace(level, pos, trace.getDirection(), new ItemStack(this.m_58900_().m_60734_()));
                }
                level.playSound(player, pos, this.m_58900_().m_60827_().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                stack.hurtAndBreak(1, player, l -> l.m_21190_(hand));
                level.setBlockAndUpdate(pos, held);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    public static boolean isValidBlock(@Nullable BlockState state, BlockPos pos, Level world) {
        if (state == null) {
            return false;
        } else {
            Block b = state.m_60734_();
            if (b.builtInRegistryHolder().is(ModTags.FRAME_BLOCK_BLACKLIST) || b instanceof EntityBlock) {
                return false;
            } else {
                return !(b instanceof FeatherBlock) && !(b instanceof SoulSandBlock) ? state.m_60804_(world, pos) && Block.isShapeFullBlock(state.m_60812_(world, pos)) : true;
            }
        }
    }

    public static class SelfPlacementContext extends BlockPlaceContext {

        public SelfPlacementContext(Player player, InteractionHand interactionHand, ItemStack itemStack, BlockHitResult blockHitResult) {
            super(player, interactionHand, itemStack, blockHitResult);
            this.f_43628_ = true;
        }
    }
}