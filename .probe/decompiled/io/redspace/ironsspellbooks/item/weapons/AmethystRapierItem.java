package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.Map;
import net.minecraft.world.item.Rarity;

public class AmethystRapierItem extends MagicSwordItem {

    public AmethystRapierItem(SpellDataRegistryHolder[] imbuedSpells) {
        super(ExtendedWeaponTiers.AMETHYST, 7.0, -1.5, imbuedSpells, Map.of(), ItemPropertiesHelper.hidden(1).rarity(Rarity.EPIC));
    }
}