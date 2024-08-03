package io.github.lightman314.lightmanscurrency.common.items;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.ITraderBlock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class CashRegisterItem extends BlockItem {

    private static final SoundEvent soundEffect = SoundEvents.EXPERIENCE_ORB_PICKUP;

    public CashRegisterItem(Block block, Item.Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos lookPos = context.getClickedPos();
        Level level = context.getLevel();
        if (level.getBlockState(lookPos).m_60734_() instanceof ITraderBlock block) {
            BlockEntity blockEntity = block.getBlockEntity(level.getBlockState(lookPos), level, lookPos);
            if (!this.HasEntity(context.getItemInHand(), blockEntity) && blockEntity instanceof TraderBlockEntity) {
                this.AddEntity(context.getItemInHand(), blockEntity);
                if (level.isClientSide) {
                    level.playSound(context.getPlayer(), blockEntity.getBlockPos(), soundEffect, SoundSource.NEUTRAL, 1.0F, 0.0F);
                }
                return InteractionResult.SUCCESS;
            }
            if (blockEntity instanceof TraderBlockEntity) {
                if (level.isClientSide) {
                    level.playSound(context.getPlayer(), blockEntity.getBlockPos(), soundEffect, SoundSource.NEUTRAL, 1.0F, 1.35F);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    private boolean HasEntity(ItemStack stack, BlockEntity blockEntity) {
        if (!stack.hasTag()) {
            return false;
        } else {
            CompoundTag tag = stack.getTag();
            assert tag != null;
            if (!tag.contains("TraderPos")) {
                return false;
            } else {
                ListTag storageList = tag.getList("TraderPos", 10);
                for (int i = 0; i < storageList.size(); i++) {
                    CompoundTag thisEntry = storageList.getCompound(i);
                    if (thisEntry.contains("x") && thisEntry.contains("y") && thisEntry.contains("z") && thisEntry.getInt("x") == blockEntity.getBlockPos().m_123341_() && thisEntry.getInt("y") == blockEntity.getBlockPos().m_123342_() && thisEntry.getInt("z") == blockEntity.getBlockPos().m_123343_()) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private void AddEntity(ItemStack stack, BlockEntity blockEntity) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag storageList;
        if (tag.contains("TraderPos")) {
            storageList = tag.getList("TraderPos", 10);
        } else {
            storageList = new ListTag();
        }
        CompoundTag newEntry = new CompoundTag();
        newEntry.putInt("x", blockEntity.getBlockPos().m_123341_());
        newEntry.putInt("y", blockEntity.getBlockPos().m_123342_());
        newEntry.putInt("z", blockEntity.getBlockPos().m_123343_());
        storageList.add(newEntry);
        tag.put("TraderPos", storageList);
    }

    private List<BlockPos> readNBT(ItemStack stack) {
        List<BlockPos> positions = new ArrayList();
        if (!stack.hasTag()) {
            return positions;
        } else {
            CompoundTag tag = stack.getTag();
            assert tag != null;
            if (tag.contains("TraderPos")) {
                ListTag list = tag.getList("TraderPos", 10);
                for (int i = 0; i < list.size(); i++) {
                    CompoundTag thisPos = list.getCompound(i);
                    if (thisPos.contains("x") && thisPos.contains("y") && thisPos.contains("z")) {
                        positions.add(new BlockPos(thisPos.getInt("x"), thisPos.getInt("y"), thisPos.getInt("z")));
                    }
                }
            }
            return positions;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        List<BlockPos> data = this.readNBT(stack);
        tooltip.addAll(LCText.TOOLTIP_CASH_REGISTER.get());
        tooltip.add(LCText.TOOLTIP_CASH_REGISTER_INFO.get(data.size()));
        if (!Screen.hasShiftDown() || data.isEmpty()) {
            tooltip.add(LCText.TOOLTIP_CASH_REGISTER_INSTRUCTIONS.get());
        }
        if (Screen.hasShiftDown()) {
            for (int i = 0; i < data.size(); i++) {
                BlockPos pos = (BlockPos) data.get(i);
                tooltip.add(LCText.TOOLTIP_CASH_REGISTER_DETAILS.get(i + 1, pos.m_123341_(), pos.m_123342_(), pos.m_123343_()));
            }
        } else if (!data.isEmpty()) {
            tooltip.add(LCText.TOOLTIP_CASH_REGISTER_HOLD_SHIFT.get().withStyle(ChatFormatting.YELLOW));
        }
    }
}