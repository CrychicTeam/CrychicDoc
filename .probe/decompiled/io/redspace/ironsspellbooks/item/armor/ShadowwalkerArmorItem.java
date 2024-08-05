package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.ShadowwalkerArmorModel;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ShadowwalkerArmorItem extends ImbuableChestplateArmorItem {

    public ShadowwalkerArmorItem(ArmorItem.Type slot, Item.Properties settings) {
        super(ExtendedArmorMaterials.SHADOWWALKER, slot, settings);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new ShadowwalkerArmorModel());
    }
}