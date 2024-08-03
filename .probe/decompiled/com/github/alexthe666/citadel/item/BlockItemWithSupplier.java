package com.github.alexthe666.citadel.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraftforge.registries.RegistryObject;

public class BlockItemWithSupplier extends BlockItem {

    private final RegistryObject<Block> blockSupplier;

    public BlockItemWithSupplier(RegistryObject<Block> blockSupplier, Item.Properties props) {
        super(null, props);
        this.blockSupplier = blockSupplier;
    }

    @Override
    public Block getBlock() {
        return this.blockSupplier.get();
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return !(this.blockSupplier.get() instanceof ShulkerBoxBlock);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity0) {
        if (this.blockSupplier.get() instanceof ShulkerBoxBlock) {
            ItemStack itemstack = itemEntity0.getItem();
            CompoundTag compoundtag = m_186336_(itemstack);
            if (compoundtag != null && compoundtag.contains("Items", 9)) {
                ListTag listtag = compoundtag.getList("Items", 10);
                ItemUtils.onContainerDestroyed(itemEntity0, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::m_41712_));
            }
        }
    }
}