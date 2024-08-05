package com.mna.items.runes;

import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.IPositionalItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemRuneMarking extends ItemRune implements IPositionalItem<ItemRuneMarking> {

    public int getMaxStackSize(ItemStack stack) {
        return this.getLocation(stack) == null ? 64 : 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        IPlayerMagic magic = (IPlayerMagic) context.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (!magic.isMagicUnlocked()) {
            if (context.getLevel().isClientSide()) {
                context.getPlayer().m_213846_(Component.translatable("item.mna.rune_marking.no_magic").withStyle(ChatFormatting.GOLD));
            }
            return InteractionResult.FAIL;
        } else if (context.getHand() == InteractionHand.OFF_HAND) {
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = context.getItemInHand();
            boolean didSplit = false;
            if (stack.getCount() > 1) {
                stack.shrink(1);
                stack = new ItemStack(ItemInit.RUNE_MARKING.get());
                didSplit = true;
            }
            this.setLocation(stack, context.getClickedPos(), context.getClickedFace(), context.getLevel());
            if (didSplit && !context.getPlayer().addItem(stack)) {
                context.getPlayer().drop(stack, true);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        BlockPos pos = this.getLocation(stack);
        if (pos != null) {
            tooltip.add(Component.translatable("item.mna.rune_marking.setPosition", pos.m_123341_(), pos.m_123342_(), pos.m_123343_()));
            MutableComponent block = (MutableComponent) this.getBlockName(stack);
            if (block != null) {
                tooltip.add(Component.translatable("item.mna.rune_marking.blockWhenSet", block.getString()).withStyle(ChatFormatting.GOLD));
            }
            Direction face = this.getFace(stack);
            tooltip.add(Component.translatable("item.mna.rune_marking.face", face.toString()).withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("item.mna.rune_marking.noPosition"));
        }
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.getLocation(stack) != null;
    }

    @Override
    public void setLocation(ItemStack stack, BlockPos pos, Direction face, Level world) {
        CompoundTag nbt = stack.getOrCreateTagElement("mark");
        nbt.putInt("x", pos.m_123341_());
        nbt.putInt("y", pos.m_123342_());
        nbt.putInt("z", pos.m_123343_());
        nbt.putInt("face", face.get3DDataValue());
        if (world.isLoaded(pos)) {
            nbt.putString("block", new ItemStack(world.getBlockState(pos).m_60734_()).getDescriptionId());
        }
    }

    @Override
    public void setLocation(ItemStack stack, BlockPos pos, Direction face) {
        CompoundTag nbt = stack.getOrCreateTagElement("mark");
        nbt.putInt("x", pos.m_123341_());
        nbt.putInt("y", pos.m_123342_());
        nbt.putInt("z", pos.m_123343_());
        nbt.putInt("face", face.get3DDataValue());
    }

    @Override
    public void setLocation(ItemStack stack, DirectionalPoint point) {
        CompoundTag nbt = stack.getOrCreateTagElement("mark");
        nbt.putInt("x", point.getPosition().m_123341_());
        nbt.putInt("y", point.getPosition().m_123342_());
        nbt.putInt("z", point.getPosition().m_123343_());
        nbt.putInt("face", point.getDirection().get3DDataValue());
        nbt.putString("block", point.getBlock());
    }

    @Nullable
    @Override
    public DirectionalPoint getDirectionalPoint(ItemStack stack) {
        return new DirectionalPoint(this.getLocation(stack), this.getFace(stack), this.getBlockTranslateKey(stack));
    }

    @Nullable
    @Override
    public BlockPos getLocation(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        } else {
            CompoundTag nbt = stack.getOrCreateTagElement("mark");
            return nbt.contains("x") && nbt.contains("y") && nbt.contains("z") ? new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")) : null;
        }
    }

    @Override
    public Direction getFace(ItemStack stack) {
        if (!stack.hasTag()) {
            return Direction.DOWN;
        } else {
            CompoundTag nbt = stack.getOrCreateTagElement("mark");
            return nbt.contains("face") ? Direction.from3DDataValue(nbt.getInt("face")) : Direction.DOWN;
        }
    }

    @Nullable
    @Override
    public String getBlockTranslateKey(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        } else {
            CompoundTag nbt = stack.getOrCreateTagElement("mark");
            return nbt.contains("block") ? nbt.getString("block") : null;
        }
    }

    @Nullable
    @Override
    public Component getBlockName(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        } else {
            CompoundTag nbt = stack.getOrCreateTagElement("mark");
            return nbt.contains("block") ? Component.translatable(nbt.getString("block")) : null;
        }
    }

    @Override
    public void copyPositionFrom(ItemStack source, ItemStack destination) {
        if (source.getItem() instanceof IPositionalItem && destination.getItem() instanceof IPositionalItem) {
            CompoundTag srcNbt = source.getOrCreateTagElement("mark");
            CompoundTag destNbt = destination.getOrCreateTagElement("mark");
            destNbt.putInt("x", srcNbt.getInt("x"));
            destNbt.putInt("y", srcNbt.getInt("y"));
            destNbt.putInt("z", srcNbt.getInt("z"));
            destNbt.putInt("face", srcNbt.getInt("face"));
            destNbt.putString("block", srcNbt.getString("block"));
        }
    }
}