package com.mna.items.artifice;

import com.mna.Registries;
import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.api.items.IFactionSpecific;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;

public class FactionSpecificSpellModifierRing extends ChargeableItem implements IPreEnchantedItem<FactionSpecificSpellModifierRing>, IFactionSpecific {

    final ResourceLocation faction;

    IFaction _cachedFaction = null;

    public FactionSpecificSpellModifierRing(Item.Properties properties, float maxMana, ResourceLocation faction) {
        super(properties, maxMana);
        this.faction = faction;
    }

    @Override
    public IFaction getFaction() {
        if (this._cachedFaction == null) {
            this._cachedFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(this.faction);
        }
        return this._cachedFaction;
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    @Override
    protected boolean tickCurio() {
        return false;
    }
}