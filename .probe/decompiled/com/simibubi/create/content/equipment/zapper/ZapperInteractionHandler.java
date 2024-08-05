package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ZapperInteractionHandler {

    @SubscribeEvent
    public static void leftClickingBlocksWithTheZapperSelectsTheBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide) {
            ItemStack heldItem = event.getEntity().m_21205_();
            if (heldItem.getItem() instanceof ZapperItem && trySelect(heldItem, event.getEntity())) {
                event.setCancellationResult(InteractionResult.FAIL);
                event.setCanceled(true);
            }
        }
    }

    public static boolean trySelect(ItemStack stack, Player player) {
        if (player.m_6144_()) {
            return false;
        } else {
            Vec3 start = player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0);
            Vec3 range = player.m_20154_().scale((double) getRange(stack));
            BlockHitResult raytrace = player.m_9236_().m_45547_(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            BlockPos pos = raytrace.getBlockPos();
            if (pos == null) {
                return false;
            } else {
                player.m_9236_().destroyBlockProgress(player.m_19879_(), pos, -1);
                BlockState newState = player.m_9236_().getBlockState(pos);
                if (BlockHelper.getRequiredItem(newState).isEmpty()) {
                    return false;
                } else if (newState.m_155947_() && !AllTags.AllBlockTags.SAFE_NBT.matches(newState)) {
                    return false;
                } else if (newState.m_61138_(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                    return false;
                } else if (newState.m_61138_(BlockStateProperties.ATTACHED)) {
                    return false;
                } else if (newState.m_61138_(BlockStateProperties.HANGING)) {
                    return false;
                } else if (newState.m_61138_(BlockStateProperties.BED_PART)) {
                    return false;
                } else {
                    if (newState.m_61138_(BlockStateProperties.STAIRS_SHAPE)) {
                        newState = (BlockState) newState.m_61124_(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
                    }
                    if (newState.m_61138_(BlockStateProperties.PERSISTENT)) {
                        newState = (BlockState) newState.m_61124_(BlockStateProperties.PERSISTENT, true);
                    }
                    if (newState.m_61138_(BlockStateProperties.WATERLOGGED)) {
                        newState = (BlockState) newState.m_61124_(BlockStateProperties.WATERLOGGED, false);
                    }
                    CompoundTag data = null;
                    BlockEntity blockEntity = player.m_9236_().getBlockEntity(pos);
                    if (blockEntity != null) {
                        data = blockEntity.saveWithFullMetadata();
                        data.remove("x");
                        data.remove("y");
                        data.remove("z");
                        data.remove("id");
                    }
                    CompoundTag tag = stack.getOrCreateTag();
                    if (tag.contains("BlockUsed") && NbtUtils.readBlockState(player.m_9236_().m_246945_(Registries.BLOCK), stack.getTag().getCompound("BlockUsed")) == newState && Objects.equals(data, tag.get("BlockData"))) {
                        return false;
                    } else {
                        tag.put("BlockUsed", NbtUtils.writeBlockState(newState));
                        if (data == null) {
                            tag.remove("BlockData");
                        } else {
                            tag.put("BlockData", data);
                        }
                        AllSoundEvents.CONFIRM.playOnServer(player.m_9236_(), player.m_20183_());
                        return true;
                    }
                }
            }
        }
    }

    public static int getRange(ItemStack stack) {
        return stack.getItem() instanceof ZapperItem ? ((ZapperItem) stack.getItem()).getZappingRange(stack) : 0;
    }
}