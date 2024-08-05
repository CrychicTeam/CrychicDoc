package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemDragonEgg extends Item {

    public EnumDragonEgg type;

    public ItemDragonEgg(EnumDragonEgg type) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return "item.iceandfire.dragonegg";
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dragon." + this.type.toString().toLowerCase()).withStyle(this.type.color));
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getPlayer().m_21120_(context.getHand());
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
        EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), context.getLevel());
        egg.setEggType(this.type);
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
}