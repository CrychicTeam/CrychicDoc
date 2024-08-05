package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.moonlight.api.item.WoodBasedItem;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SignPostBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FramedBlocksCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class SignPostItem extends WoodBasedItem {

    public SignPostItem(Item.Properties properties, WoodType wood) {
        super(properties, wood, 100);
    }

    private SignPostItem.AttachType getAttachType(BlockState state) {
        Block b = state.m_60734_();
        if (b instanceof SignPostBlock) {
            return SignPostItem.AttachType.SIGN_POST;
        } else if ((!(b instanceof StickBlock) || (Boolean) state.m_61143_(StickBlock.AXIS_X) || (Boolean) state.m_61143_(StickBlock.AXIS_Z)) && (!(state.m_60734_() instanceof EndRodBlock) || ((Direction) state.m_61143_(EndRodBlock.f_52588_)).getAxis() != Direction.Axis.Y)) {
            ResourceLocation res = Utils.getID(b);
            return state.m_204336_(ModTags.POSTS) && !res.getNamespace().equals("blockcarpentry") ? SignPostItem.AttachType.FENCE : SignPostItem.AttachType.NONE;
        } else {
            return SignPostItem.AttachType.STICK;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        } else {
            BlockPos blockpos = context.getClickedPos();
            Level world = context.getLevel();
            ItemStack itemstack = context.getItemInHand();
            BlockState state = world.getBlockState(blockpos);
            Block targetBlock = state.m_60734_();
            boolean framed = false;
            SignPostItem.AttachType attachType = this.getAttachType(state);
            if (attachType != SignPostItem.AttachType.NONE) {
                if (CompatHandler.FRAMEDBLOCKS) {
                    Block f = FramedBlocksCompat.tryGettingFramedBlock(targetBlock, world, blockpos);
                    if (f != null) {
                        framed = true;
                        if (f != Blocks.AIR) {
                            targetBlock = f;
                        }
                    }
                }
                boolean waterlogged = world.getFluidState(blockpos).getType() == Fluids.WATER;
                if (attachType != SignPostItem.AttachType.SIGN_POST) {
                    world.setBlock(blockpos, (BlockState) ((Block) ModRegistry.SIGN_POST.get()).getStateForPlacement(new BlockPlaceContext(context)).m_61124_(SignPostBlock.WATERLOGGED, waterlogged), 3);
                }
                boolean flag = false;
                if (world.getBlockEntity(blockpos) instanceof SignPostBlockTile tile) {
                    int r = Mth.floor((double) ((180.0F + context.getRotation()) * 16.0F / 360.0F) + 0.5) & 15;
                    double y = context.getClickLocation().y - (double) blockpos.m_123342_();
                    boolean up = y > 0.5;
                    flag = tile.initializeSignAfterConversion(this.getBlockType(), r, up, attachType == SignPostItem.AttachType.STICK, framed);
                    if (flag) {
                        if (attachType != SignPostItem.AttachType.SIGN_POST) {
                            tile.setHeldBlock(targetBlock.defaultBlockState());
                            tile.m_6596_();
                        } else {
                            BlockUtil.addOptionalOwnership(player, tile);
                        }
                    }
                }
                if (flag) {
                    world.sendBlockUpdated(blockpos, state, state, 3);
                    SoundType soundtype = this.getBlockType().getSound();
                    world.playSound(null, blockpos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (!context.getPlayer().isCreative()) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }

    private static enum AttachType {

        FENCE, SIGN_POST, STICK, NONE
    }
}