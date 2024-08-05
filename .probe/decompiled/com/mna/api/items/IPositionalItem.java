package com.mna.api.items;

import com.mna.api.blocks.DirectionalPoint;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IPositionalItem<T extends Item> {

    void setLocation(ItemStack var1, DirectionalPoint var2);

    void setLocation(ItemStack var1, BlockPos var2, Direction var3);

    void setLocation(ItemStack var1, BlockPos var2, Direction var3, Level var4);

    void copyPositionFrom(ItemStack var1, ItemStack var2);

    @Nullable
    BlockPos getLocation(ItemStack var1);

    Direction getFace(ItemStack var1);

    @Nullable
    Component getBlockName(ItemStack var1);

    @Nullable
    DirectionalPoint getDirectionalPoint(ItemStack var1);

    @Nullable
    String getBlockTranslateKey(ItemStack var1);
}