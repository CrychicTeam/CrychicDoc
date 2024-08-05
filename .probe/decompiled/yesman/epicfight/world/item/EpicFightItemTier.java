package yesman.epicfight.world.item;

import java.util.function.Supplier;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum EpicFightItemTier implements Tier {

    UCHIGATANA(4, 1625, 9.0F, 6.0F, 22, () -> Ingredient.of(Items.NETHERITE_INGOT)), GLOVE(4, 255, 9.0F, 0.0F, 16, () -> Ingredient.of(Items.IRON_INGOT));

    private final int harvestLevel;

    private final int maxUses;

    private final float efficiency;

    private final float attackDamage;

    private final int enchantability;

    private final LazyLoadedValue<Ingredient> repairMaterial;

    private EpicFightItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}