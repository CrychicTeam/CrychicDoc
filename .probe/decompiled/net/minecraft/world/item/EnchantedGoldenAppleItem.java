package net.minecraft.world.item;

public class EnchantedGoldenAppleItem extends Item {

    public EnchantedGoldenAppleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }
}