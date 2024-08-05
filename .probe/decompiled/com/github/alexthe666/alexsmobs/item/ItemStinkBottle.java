package com.github.alexthe666.alexsmobs.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ItemStinkBottle extends AMBlockItem {

    public ItemStinkBottle(RegistryObject<Block> blockSupplier, Item.Properties props) {
        super(blockSupplier, props);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.m_40576_(context);
        if (result.consumesAction()) {
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (context.m_43723_() == null) {
                context.m_43725_().m_7967_(new ItemEntity(context.m_43725_(), (double) ((float) context.getClickedPos().m_123341_() + 0.5F), (double) ((float) context.getClickedPos().m_123342_() + 0.5F), (double) ((float) context.getClickedPos().m_123343_() + 0.5F), bottle));
            } else if (!context.m_43723_().addItem(bottle)) {
                context.m_43723_().drop(bottle, false);
            }
        }
        return result;
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }
}