package net.minecraft.world.item;

public class TieredItem extends Item {

    private final Tier tier;

    public TieredItem(Tier tier0, Item.Properties itemProperties1) {
        super(itemProperties1.defaultDurability(tier0.getUses()));
        this.tier = tier0;
    }

    public Tier getTier() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack0, ItemStack itemStack1) {
        return this.tier.getRepairIngredient().test(itemStack1) || super.isValidRepairItem(itemStack0, itemStack1);
    }
}