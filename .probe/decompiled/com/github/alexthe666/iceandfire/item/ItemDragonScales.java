package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemDragonScales extends Item {

    EnumDragonEgg type;

    public ItemDragonScales(EnumDragonEgg type) {
        super(new Item.Properties());
        this.type = type;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return "item.iceandfire.dragonscales";
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dragon." + this.type.toString().toLowerCase()).withStyle(this.type.color));
    }
}