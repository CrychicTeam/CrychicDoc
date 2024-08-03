package com.mna.items.renderers;

import com.mna.items.armor.FeyArmorItem;
import com.mna.items.armor.models.FeyArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class FeyArmorRenderer extends GeoArmorRenderer<FeyArmorItem> {

    public FeyArmorRenderer() {
        super(new FeyArmorModel());
    }

    @Override
    public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel) {
        if (entity instanceof LivingEntity) {
            FeyArmorItem.renderEntity = (LivingEntity) entity;
        }
        super.prepForRender(entity, stack, slot, baseModel);
    }
}