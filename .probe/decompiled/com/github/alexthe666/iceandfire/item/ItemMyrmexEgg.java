package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemMyrmexEgg extends Item {

    boolean isJungle;

    public ItemMyrmexEgg(boolean isJungle) {
        super(new Item.Properties().stacksTo(1));
        this.isJungle = isJungle;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        CompoundTag tag = stack.getTag();
        int eggOrdinal = 0;
        if (tag != null) {
            eggOrdinal = tag.getInt("EggOrdinal");
        }
        String caste = switch(eggOrdinal) {
            case 1 ->
                "soldier";
            case 2 ->
                "royal";
            case 3 ->
                "sentinel";
            case 4 ->
                "queen";
            default ->
                "worker";
        };
        if (eggOrdinal == 4) {
            tooltip.add(Component.translatable("myrmex.caste_" + caste + ".name").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            tooltip.add(Component.translatable("myrmex.caste_" + caste + ".name").withStyle(ChatFormatting.GRAY));
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getPlayer().m_21120_(context.getHand());
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
        EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), context.getLevel());
        CompoundTag tag = itemstack.getTag();
        int eggOrdinal = 0;
        if (tag != null) {
            eggOrdinal = tag.getInt("EggOrdinal");
        }
        egg.setJungle(this.isJungle);
        egg.setMyrmexCaste(eggOrdinal);
        egg.m_7678_((double) offset.m_123341_() + 0.5, (double) offset.m_123342_(), (double) offset.m_123343_() + 0.5, 0.0F, 0.0F);
        egg.onPlayerPlace(context.getPlayer());
        if (itemstack.hasCustomHoverName()) {
            egg.m_6593_(itemstack.getHoverName());
        }
        if (!context.getLevel().isClientSide) {
            context.getLevel().m_7967_(egg);
        }
        itemstack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        int eggOrdinal = 0;
        if (tag != null) {
            eggOrdinal = tag.getInt("EggOrdinal");
        }
        return super.isFoil(stack) || eggOrdinal == 4;
    }
}