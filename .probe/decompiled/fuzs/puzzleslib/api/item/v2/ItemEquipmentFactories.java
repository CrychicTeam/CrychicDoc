package fuzs.puzzleslib.api.item.v2;

import com.google.common.base.Suppliers;
import fuzs.puzzleslib.impl.item.ArmorMaterialImpl;
import fuzs.puzzleslib.impl.item.TierImpl;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public final class ItemEquipmentFactories {

    private ItemEquipmentFactories() {
    }

    public static Tier registerTier(int miningLevel, int itemDurability, float miningSpeed, float attackDamageBonus, int enchantability, Supplier<Ingredient> repairIngredient) {
        return new TierImpl(miningLevel, itemDurability, miningSpeed, attackDamageBonus, enchantability, Suppliers.memoize(repairIngredient::get));
    }

    public static ArmorMaterial registerArmorMaterial(ResourceLocation name, Supplier<Ingredient> repairIngredient) {
        return registerArmorMaterial(name, 0, new int[] { 1, 1, 1, 1 }, 0, repairIngredient);
    }

    public static ArmorMaterial registerArmorMaterial(ResourceLocation name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, Supplier<Ingredient> repairIngredient) {
        return registerArmorMaterial(name, durabilityMultiplier, protectionAmounts, enchantability, () -> SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, repairIngredient);
    }

    public static ArmorMaterial registerArmorMaterial(ResourceLocation name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, Supplier<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        return new ArmorMaterialImpl(name.toString(), durabilityMultiplier, protectionAmounts, enchantability, Suppliers.memoize(equipSound::get), toughness, knockbackResistance, Suppliers.memoize(repairIngredient::get));
    }
}