package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.netherite.NetheriteMageArmorModel;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NetheriteMageArmorItem extends ImbuableChestplateArmorItem {

    public NetheriteMageArmorItem(ArmorItem.Type type, Item.Properties settings) {
        super(ExtendedArmorMaterials.NETHERITE_BATTLEMAGE, type, settings);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GeoArmorRenderer<>(new NetheriteMageArmorModel());
    }
}