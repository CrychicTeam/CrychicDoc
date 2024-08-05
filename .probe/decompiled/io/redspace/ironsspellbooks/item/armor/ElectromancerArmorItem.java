package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.ElectromancerArmorModel;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ElectromancerArmorItem extends ImbuableChestplateArmorItem implements ArmorCapeProvider {

    public ElectromancerArmorItem(ArmorItem.Type slot, Item.Properties settings) {
        super(ExtendedArmorMaterials.ELECTROMANCER, slot, settings);
    }

    @Override
    public ResourceLocation getCapeResourceLocation() {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/electromancer_cape.png");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new ElectromancerArmorModel());
    }
}