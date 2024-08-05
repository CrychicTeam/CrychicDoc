package com.simibubi.create.content.trains.track;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class TrackBlockItem extends BlockItem {

    public TrackBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = pContext.getPlayer();
        if (player == null) {
            return super.useOn(pContext);
        } else if (pContext.getHand() == InteractionHand.OFF_HAND) {
            return super.useOn(pContext);
        } else {
            Vec3 lookAngle = player.m_20154_();
            if (!this.isFoil(stack)) {
                if (state.m_60734_() instanceof TrackBlock track && track.getTrackAxes(level, pos, state).size() > 1) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("track.junction_start").withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (level.getBlockEntity(pos) instanceof TrackBlockEntity tbe && tbe.isTilted()) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("track.turn_start").withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (select(level, pos, lookAngle, stack)) {
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 1.0F);
                    return InteractionResult.SUCCESS;
                } else {
                    return super.useOn(pContext);
                }
            } else if (player.m_6144_()) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("track.selection_cleared"), true);
                    stack.setTag(null);
                } else {
                    level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            } else {
                boolean placing = !(state.m_60734_() instanceof ITrackBlock);
                CompoundTag tag = stack.getTag();
                boolean extend = tag.getBoolean("ExtendCurve");
                tag.remove("ExtendCurve");
                if (placing) {
                    if (!state.m_247087_()) {
                        pos = pos.relative(pContext.getClickedFace());
                    }
                    state = this.getPlacementState(pContext);
                    if (state == null) {
                        return InteractionResult.FAIL;
                    }
                }
                ItemStack offhandItem = player.m_21206_();
                boolean hasGirder = AllBlocks.METAL_GIRDER.isIn(offhandItem);
                TrackPlacement.PlacementInfo info = TrackPlacement.tryConnect(level, player, pos, state, stack, hasGirder, extend);
                if (info.message != null && !level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect(info.message), true);
                }
                if (!info.valid) {
                    AllSoundEvents.DENY.playFrom(player, 1.0F, 1.0F);
                    return InteractionResult.FAIL;
                } else if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    stack = player.m_21205_();
                    if (AllTags.AllBlockTags.TRACKS.matches(stack)) {
                        stack.setTag(null);
                        player.m_21008_(pContext.getHand(), stack);
                    }
                    SoundType soundtype = state.m_60827_();
                    if (soundtype != null) {
                        level.playSound(null, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    public BlockState getPlacementState(UseOnContext pContext) {
        return this.m_5965_(this.m_7732_(new BlockPlaceContext(pContext)));
    }

    public static boolean select(LevelAccessor world, BlockPos pos, Vec3 lookVec, ItemStack heldItem) {
        BlockState blockState = world.m_8055_(pos);
        if (!(blockState.m_60734_() instanceof ITrackBlock track)) {
            return false;
        } else {
            Pair<Vec3, Direction.AxisDirection> nearestTrackAxis = track.getNearestTrackAxis(world, pos, blockState, lookVec);
            Vec3 axis = nearestTrackAxis.getFirst().scale(nearestTrackAxis.getSecond() == Direction.AxisDirection.POSITIVE ? -1.0 : 1.0);
            Vec3 end = track.getCurveStart(world, pos, blockState, axis);
            Vec3 normal = track.getUpNormal(world, pos, blockState).normalize();
            CompoundTag compoundTag = heldItem.getOrCreateTagElement("ConnectingFrom");
            compoundTag.put("Pos", NbtUtils.writeBlockPos(pos));
            compoundTag.put("Axis", VecHelper.writeNBT(axis));
            compoundTag.put("Normal", VecHelper.writeNBT(normal));
            compoundTag.put("End", VecHelper.writeNBT(end));
            return true;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void sendExtenderPacket(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (AllTags.AllBlockTags.TRACKS.matches(stack) && stack.hasTag()) {
            if (Minecraft.getInstance().options.keySprint.isDown()) {
                AllPackets.getChannel().sendToServer(new PlaceExtendedCurvePacket(event.getHand() == InteractionHand.MAIN_HAND, true));
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("ConnectingFrom");
    }
}