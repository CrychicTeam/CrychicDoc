package dev.latvian.mods.kubejs.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.MutableArmorTier;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;

public class ArmorItemBuilder extends ItemBuilder {

    public final ArmorItem.Type armorType;

    public MutableArmorTier armorTier;

    protected ArmorItemBuilder(ResourceLocation i, ArmorItem.Type t) {
        super(i);
        this.armorType = t;
        this.armorTier = new MutableArmorTier(this.id.toString(), ArmorMaterials.IRON);
        this.unstackable();
    }

    public Item createObject() {
        return new ArmorItem(this.armorTier, this.armorType, this.createItemProperties()) {

            private boolean modified = false;

            {
                this.f_40383_ = ArrayListMultimap.create(this.f_40383_);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!this.modified) {
                    this.modified = true;
                    ArmorItemBuilder.this.attributes.forEach((r, m) -> this.f_40383_.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                }
                return super.getDefaultAttributeModifiers(equipmentSlot);
            }
        };
    }

    public ArmorItemBuilder tier(ArmorMaterial t) {
        this.armorTier = new MutableArmorTier(t.getName(), t);
        return this;
    }

    public ArmorItemBuilder modifyTier(Consumer<MutableArmorTier> callback) {
        callback.accept(this.armorTier);
        return this;
    }

    public static class Boots extends ArmorItemBuilder {

        public Boots(ResourceLocation i) {
            super(i, ArmorItem.Type.BOOTS);
        }
    }

    public static class Chestplate extends ArmorItemBuilder {

        public Chestplate(ResourceLocation i) {
            super(i, ArmorItem.Type.CHESTPLATE);
        }
    }

    public static class Helmet extends ArmorItemBuilder {

        public Helmet(ResourceLocation i) {
            super(i, ArmorItem.Type.HELMET);
        }
    }

    public static class Leggings extends ArmorItemBuilder {

        public Leggings(ResourceLocation i) {
            super(i, ArmorItem.Type.LEGGINGS);
        }
    }
}