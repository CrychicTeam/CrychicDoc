package com.squoshi.irons_spells_js.compat.entityjs;

import com.squoshi.irons_spells_js.compat.entityjs.entity.builder.SpellCastingMobJSBuilder;
import com.squoshi.irons_spells_js.compat.entityjs.entity.builder.SpellProjectileJSBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;

public class EntityJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryInfo.ENTITY_TYPE.addType("irons_spells_js:spellcasting", SpellCastingMobJSBuilder.class, SpellCastingMobJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("irons_spells_js:spell_projectile", SpellProjectileJSBuilder.class, SpellProjectileJSBuilder::new);
    }
}