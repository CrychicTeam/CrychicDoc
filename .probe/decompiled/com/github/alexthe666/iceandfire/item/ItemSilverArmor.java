package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.model.armor.ModelSilverArmor;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ItemSilverArmor extends ArmorItem {

    public ItemSilverArmor(ArmorMaterial material, ArmorItem.Type slot) {
        super(material, slot, new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return new ModelSilverArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
            }
        });
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "armor_silver_metal_layer_2" : "armor_silver_metal_layer_1") + ".png";
    }
}