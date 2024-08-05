package com.mna.interop.lucent;

import com.legacy.lucent.api.plugin.ILucentPlugin;
import com.legacy.lucent.api.plugin.LucentPlugin;
import com.legacy.lucent.api.registry.EntityLightSourcePosRegistry;
import com.legacy.lucent.api.registry.EntityLightingRegistry;
import com.legacy.lucent.api.registry.ItemLightingRegistry;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;

@LucentPlugin
public class MnALucentPlugin implements ILucentPlugin {

    public String ownerModID() {
        return "mna";
    }

    public void registerEntityLightings(EntityLightingRegistry registry) {
        registry.register(EntityInit.SPELL_PROJECTILE.get(), 13);
        registry.register(EntityInit.SPELL_BEAM.get(), 13);
        registry.register(EntityInit.SPELL_CONE.get(), 13);
        registry.register(EntityInit.SPELL_EMANATION.get(), 13);
        registry.register(EntityInit.SPELL_EMBER.get(), 13);
        registry.register(EntityInit.SPELL_FISSURE.get(), 13);
        registry.register(EntityInit.SPELL_FOCUS.get(), 13);
        registry.register(EntityInit.SPELL_WALL.get(), 13);
        registry.register(EntityInit.SPELL_WAVE.get(), 13);
        registry.register(EntityInit.SMITE_PROJECTILE.get(), 13);
        registry.register(EntityInit.SENTRY_PROJECTILE.get(), 13);
        registry.register(EntityInit.PORTAL_ENTITY.get(), 8);
        registry.register(EntityInit.RIFT.get(), 8);
        registry.register(EntityInit.DEMON_RETURN_PORTAL.get(), 8);
        registry.register(EntityInit.BIFROST.get(), 15);
        registry.register(EntityInit.FAERIE_QUEEN.get(), 10);
        registry.register(EntityInit.FEY_LIGHT.get(), 15);
        registry.register(EntityInit.LANTERN_WRAITH.get(), 10);
        registry.register(EntityInit.LIVING_WARD.get(), 6);
        registry.register(EntityInit.MANAWEAVE_ENTITY.get(), 8);
        registry.register(EntityInit.PIXIE.get(), 6);
        registry.register(EntityInit.PRESENTATION_ENTITY.get(), 12);
        registry.register(EntityInit.STARBALL_ENTITY.get(), 15);
    }

    public void registerEntityLightSourcePositionGetter(EntityLightSourcePosRegistry registry) {
        registry.register(EntityInit.BIFROST.get(), e -> e.m_20182_().subtract(0.0, 10.0, 0.0));
        registry.register(EntityInit.SPELL_BEAM.get(), e -> e.getLastTickImpact());
    }

    public void registerItemLightings(ItemLightingRegistry registry) {
        registry.register(ItemInit.SPELL.get(), 11);
        registry.register(ItemInit.ROTE_BOOK.get(), 11);
        registry.register(ItemInit.BOUND_AXE.get(), 11);
        registry.register(ItemInit.BOUND_SWORD.get(), 11);
        registry.register(ItemInit.BOUND_BOW.get(), 11);
        registry.register(ItemInit.BOUND_SHIELD.get(), 11);
        registry.register(ItemInit.HELLFIRE_STAFF.get(), 11);
        registry.register(ItemInit.PUNKIN_STAFF.get(), 11);
        registry.register(ItemInit.SPELL_BOOK.get(), 11);
        registry.register(ItemInit.GRIMOIRE.get(), 11);
        registry.register(ItemInit.STAFF_AMETHYST.get(), 11);
        registry.register(ItemInit.STAFF_AUM.get(), 11);
        registry.register(ItemInit.STAFF_CERUBLOSSOM.get(), 11);
        registry.register(ItemInit.STAFF_CHIMERITE.get(), 11);
        registry.register(ItemInit.STAFF_DESERTNOVA.get(), 11);
        registry.register(ItemInit.STAFF_EMERALD.get(), 11);
        registry.register(ItemInit.STAFF_GLASS.get(), 11);
        registry.register(ItemInit.STAFF_GOLD.get(), 11);
        registry.register(ItemInit.STAFF_IRON.get(), 11);
        registry.register(ItemInit.STAFF_LAPIS.get(), 11);
        registry.register(ItemInit.STAFF_NETHERQUARTZ.get(), 11);
        registry.register(ItemInit.STAFF_PHYLACTERY.get(), 11);
        registry.register(ItemInit.STAFF_PRISMARINECRYSTAL.get(), 11);
        registry.register(ItemInit.STAFF_PRISMARINESHARD.get(), 11);
        registry.register(ItemInit.STAFF_REDSTONE.get(), 11);
        registry.register(ItemInit.STAFF_SKULL.get(), 11);
        registry.register(ItemInit.STAFF_SKULL_ALT.get(), 11);
        registry.register(ItemInit.STAFF_TARMA.get(), 11);
        registry.register(ItemInit.STAFF_WAKEBLOOM.get(), 11);
        registry.register(ItemInit.STAFF_VINTEUM.get(), 11);
        registry.register(ItemInit.WAND_AMETHYST.get(), 11);
        registry.register(ItemInit.WAND_AUM.get(), 11);
        registry.register(ItemInit.WAND_CERUBLOSSOM.get(), 11);
        registry.register(ItemInit.WAND_CHIMERITE.get(), 11);
        registry.register(ItemInit.WAND_DESERTNOVA.get(), 11);
        registry.register(ItemInit.WAND_EMERALD.get(), 11);
        registry.register(ItemInit.WAND_GLASS.get(), 11);
        registry.register(ItemInit.WAND_GOLD.get(), 11);
        registry.register(ItemInit.WAND_IRON.get(), 11);
        registry.register(ItemInit.WAND_LAPIS.get(), 11);
        registry.register(ItemInit.WAND_NETHERQUARTZ.get(), 11);
        registry.register(ItemInit.WAND_PRISMARINECRYSTAL.get(), 11);
        registry.register(ItemInit.WAND_PRISMARINESHARD.get(), 11);
        registry.register(ItemInit.WAND_REDSTONE.get(), 11);
        registry.register(ItemInit.WAND_SKULL.get(), 11);
        registry.register(ItemInit.WAND_SKULL_ALT.get(), 11);
        registry.register(ItemInit.WAND_TARMA.get(), 11);
        registry.register(ItemInit.WAND_WAKEBLOOM.get(), 11);
        registry.register(ItemInit.WAND_VINTEUM.get(), 11);
        registry.register(ItemInit.CHIMERITE_GEM.get(), 8);
        registry.register(ItemInit.VINTEUM_INGOT_SUPERHEATED.get(), 8);
        registry.register(ItemInit.VINTEUM_INGOT_PURIFIED_SUPERHEATED.get(), 10);
        registry.register(ItemInit.MOTE_AIR.get(), 8);
        registry.register(ItemInit.MOTE_WATER.get(), 8);
        registry.register(ItemInit.MOTE_EARTH.get(), 8);
        registry.register(ItemInit.MOTE_ENDER.get(), 8);
        registry.register(ItemInit.MOTE_ARCANE.get(), 8);
        registry.register(ItemInit.MOTE_FIRE.get(), 8);
        registry.register(ItemInit.GREATER_MOTE_AIR.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_WATER.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_EARTH.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_ENDER.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_ARCANE.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_FIRE.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_HELLFIRE.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_LIGHTNING.get(), 12);
        registry.register(ItemInit.GREATER_MOTE_ICE.get(), 12);
        registry.register(ItemInit.CRYSTAL_PHYLACTERY.get(), 8);
        registry.register(ItemInit.CRYSTAL_OF_MEMORIES.get(), 8);
        registry.register(ItemInit.ELDRITCH_ORB.get(), 10);
        registry.register(ItemInit.EMBERGLOW_BRACELET.get(), 8);
        registry.register(ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get(), 8);
        registry.register(ItemInit.LIVING_FLAME.get(), 10);
        registry.register(ItemInit.MAJOR_MANA_GEM.get(), 14);
        registry.register(ItemInit.MINOR_MANA_GEM.get(), 10);
        registry.register(ItemInit.MANA_CRYSTAL_FRAGMENT.get(), 8);
        registry.register(ItemInit.RESONATING_LUMP.get(), 8);
        registry.register(ItemInit.RESONATING_DUST.get(), 8);
        registry.register(ItemInit.DEMON_LORD_AXE.get(), 12);
        registry.register(ItemInit.DEMON_LORD_SWORD.get(), 12);
    }
}