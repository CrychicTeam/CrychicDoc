package dev.xkmc.modulargolems.content.item.equipments;

import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeMod;

public class MetalGolemWeaponItem extends GolemEquipmentItem {

    public MetalGolemWeaponItem(Item.Properties properties, int attackDamage, double percentAttack, float range, float sweep) {
        super(properties, EquipmentSlot.MAINHAND, GolemTypes.ENTITY_GOLEM::get, builder -> {
            UUID uuid = (UUID) UUID.get(EquipmentSlot.MAINHAND);
            if (attackDamage > 0) {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", (double) attackDamage, AttributeModifier.Operation.ADDITION));
            }
            if (percentAttack > 0.0) {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", percentAttack, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (range > 0.0F) {
                builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(uuid, "spear_range", (double) range, AttributeModifier.Operation.ADDITION));
            }
            if (sweep > 0.0F) {
                builder.put((Attribute) GolemTypes.GOLEM_SWEEP.get(), new AttributeModifier(uuid, "spear_sweep", (double) sweep, AttributeModifier.Operation.ADDITION));
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategory.WEAPON ? true : super.canApplyAtEnchantingTable(stack, enchantment);
    }
}