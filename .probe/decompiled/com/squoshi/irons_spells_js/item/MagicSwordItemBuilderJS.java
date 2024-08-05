package com.squoshi.irons_spells_js.item;

import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class MagicSwordItemBuilderJS extends HandheldItemBuilder {

    public transient List<MagicSwordItemBuilderJS.AttributeHolder> additionalAttributes = new ArrayList();

    public transient List<MagicSwordItemBuilderJS.SpellHolder> spellHolders = new ArrayList();

    public MagicSwordItemBuilderJS(ResourceLocation i) {
        super(i, 3.0F, -2.4F);
    }

    public MagicSwordItemBuilderJS addDefaultSpell(ISSKJSUtils.SpellHolder spell, int spellLevel) {
        this.spellHolders.add(new MagicSwordItemBuilderJS.SpellHolder(spell.getLocation(), spellLevel));
        return this;
    }

    public MagicSwordItemBuilderJS addAdditionalAttribute(ISSKJSUtils.AttributeHolder attribute, String modifierName, double modifierAmount, AttributeModifier.Operation modifierOperation) {
        this.additionalAttributes.add(new MagicSwordItemBuilderJS.AttributeHolder(attribute.getLocation(), new AttributeModifier(modifierName, modifierAmount, modifierOperation)));
        return this;
    }

    public MagicSwordItem createObject() {
        Map<Attribute, AttributeModifier> map = new HashMap(Map.of());
        for (MagicSwordItemBuilderJS.AttributeHolder holder : this.additionalAttributes) {
            Attribute attribute = (Attribute) Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(holder.attribute()));
            map.put(attribute, holder.modifier());
        }
        SpellDataRegistryHolder[] spellDataHolders = new SpellDataRegistryHolder[this.spellHolders.size()];
        Iterator<MagicSwordItemBuilderJS.SpellHolder> iterator = this.spellHolders.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            MagicSwordItemBuilderJS.SpellHolder spells = (MagicSwordItemBuilderJS.SpellHolder) iterator.next();
            spellDataHolders[i] = new SpellDataRegistryHolder(RegistryObject.create(spells.spell, (IForgeRegistry) SpellRegistry.REGISTRY.get()), spells.spellLevel);
        }
        return new MagicSwordItem(this.toolTier, (double) this.attackDamageBaseline, (double) this.speedBaseline, spellDataHolders, map, this.createItemProperties());
    }

    public static record AttributeHolder(ResourceLocation attribute, AttributeModifier modifier) {
    }

    public static record SpellHolder(ResourceLocation spell, int spellLevel) {
    }
}