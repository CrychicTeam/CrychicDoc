package net.minecraft.world.item;

public class BookItem extends Item {

    public BookItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack0) {
        return itemStack0.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}