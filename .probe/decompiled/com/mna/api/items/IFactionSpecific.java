package com.mna.api.items;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.faction.IFaction;
import net.minecraft.world.entity.player.Player;

public interface IFactionSpecific {

    default IFaction getFaction() {
        return null;
    }

    default float getMinIre() {
        return 0.0F;
    }

    default float getMaxIre() {
        return 0.005F;
    }

    default void usedByPlayer(Player player) {
        if (!player.m_9236_().isClientSide() && this.getFaction() != null) {
            player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> {
                IFaction playerFaction = p.getAlliedFaction();
                if (playerFaction != null && playerFaction != this.getFaction() && !this.getFaction().getAlliedFactions().contains(playerFaction)) {
                    p.incrementFactionAggro(this.getFaction(), this.getMinIre(), this.getMaxIre());
                }
            });
        }
    }
}