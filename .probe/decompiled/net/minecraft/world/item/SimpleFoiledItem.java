package net.minecraft.world.item;

public class SimpleFoiledItem extends Item {

    public SimpleFoiledItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }
}