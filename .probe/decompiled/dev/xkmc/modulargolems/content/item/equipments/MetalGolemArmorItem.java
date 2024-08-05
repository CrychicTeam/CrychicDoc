package dev.xkmc.modulargolems.content.item.equipments;

import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.ForgeRegistries;

public class MetalGolemArmorItem extends GolemEquipmentItem implements GolemModelItem {

    private final ResourceLocation model;

    public MetalGolemArmorItem(Item.Properties properties, ArmorItem.Type type, int defense, float toughness, ResourceLocation model) {
        super(properties, type.getSlot(), GolemTypes.ENTITY_GOLEM::get, builder -> {
            UUID uuid = (UUID) UUID.get(type.getSlot());
            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double) defense, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double) toughness, AttributeModifier.Operation.ADDITION));
        });
        this.model = model;
    }

    @Override
    public ResourceLocation getModelTexture() {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(this);
        assert rl != null;
        return new ResourceLocation(rl.getNamespace(), "textures/equipments/" + rl.getPath() + ".png");
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
        if (enchantment.category == EnchantmentCategory.ARMOR) {
            return true;
        } else if (enchantment.category == EnchantmentCategory.ARMOR_HEAD && this.getSlot() == EquipmentSlot.HEAD) {
            return true;
        } else if (enchantment.category == EnchantmentCategory.ARMOR_CHEST && this.getSlot() == EquipmentSlot.CHEST) {
            return true;
        } else if (enchantment.category == EnchantmentCategory.ARMOR_LEGS && this.getSlot() == EquipmentSlot.LEGS) {
            return true;
        } else {
            return enchantment.category == EnchantmentCategory.ARMOR_FEET && this.getSlot() == EquipmentSlot.FEET ? true : super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public ResourceLocation getModelPath() {
        return this.model;
    }
}