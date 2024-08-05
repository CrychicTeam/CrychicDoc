package com.rekindled.embers.item;

import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.api.tile.ITargetable;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TinkerHammerItem extends Item {

    public TinkerHammerItem(Item.Properties pProperties) {
        super(pProperties);
        EmbersAPI.registerLinkingHammer(this);
        EmbersAPI.registerHammerTargetGetter(this);
    }

    public final ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.isEmpty() ? new ItemStack(this, stack.getCount(), stack.getTag()) : stack.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        CompoundTag nbt = stack.getOrCreateTag();
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockEntity tile = world.getBlockEntity(pos);
        if (world != null && nbt.contains("targetWorld") && world.dimension().location().toString().equals(nbt.getString("targetWorld"))) {
            BlockPos targetPos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
            BlockEntity targetTile = world.getBlockEntity(targetPos);
            if (targetTile instanceof IEmberPacketProducer) {
                if (tile instanceof IEmberPacketReceiver) {
                    ((IEmberPacketProducer) targetTile).setTargetPosition(pos, Direction.byName(nbt.getString("targetFace")));
                    world.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.5F + world.random.nextFloat() * 0.1F, false);
                    nbt.remove("targetWorld");
                    return InteractionResult.SUCCESS;
                }
            } else if (targetTile instanceof ITargetable) {
                ((ITargetable) targetTile).setTarget(pos);
                world.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.5F + world.random.nextFloat() * 0.1F, false);
                nbt.remove("targetWorld");
                return InteractionResult.SUCCESS;
            }
        }
        if (world != null && (tile instanceof IEmberPacketProducer || tile instanceof ITargetable)) {
            Direction face = context.getClickedFace();
            if (tile instanceof IEmberPacketProducer) {
                face = ((IEmberPacketProducer) tile).getEmittingDirection(face);
                if (face == null) {
                    return InteractionResult.PASS;
                }
            }
            nbt.putString("targetWorld", world.dimension().location().toString());
            nbt.putString("targetFace", face.getName());
            nbt.putInt("targetX", pos.m_123341_());
            nbt.putInt("targetY", pos.m_123342_());
            nbt.putInt("targetZ", pos.m_123343_());
            world.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.95F + world.random.nextFloat() * 0.2F, false);
            if (world.isClientSide) {
                EmbersClientEvents.lastTarget = null;
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (level != null && stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt.contains("targetWorld")) {
                String dimension = nbt.getString("targetWorld");
                if (level.dimension().location().toString().equals(dimension)) {
                    BlockPos pos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
                    BlockState blockState = level.getBlockState(pos);
                    tooltip.add(Component.translatable("embers.tooltip.aiming_block", blockState.m_60734_().getName()).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.translatable(" X=" + pos.m_123341_()).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.translatable(" Y=" + pos.m_123342_()).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.translatable(" Z=" + pos.m_123343_()).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}