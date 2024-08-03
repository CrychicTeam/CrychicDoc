package dev.xkmc.l2backpack.content.bag;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BookBag extends AbstractBag {

    public BookBag(Item.Properties props) {
        super(props);
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return stack.getItem() == Items.ENCHANTED_BOOK;
    }
}