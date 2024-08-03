package com.squoshi.irons_spells_js.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.player.Player;

public class SpellSelectionEventJS extends PlayerEventJS {

    private final SpellSelectionManager.SpellSelectionEvent event;

    public SpellSelectionEventJS(SpellSelectionManager.SpellSelectionEvent event) {
        this.event = event;
    }

    @Info("    Returns the player that cast the spell.\n")
    @Override
    public Player getEntity() {
        return this.event.getEntity();
    }

    @Info("    Returns if the event is cancelable.\n")
    public boolean isCancelable() {
        return this.event.isCancelable();
    }

    @Info("    Adds spell option to the end of a player's spell bar.\n")
    public void addSelectionOption(SpellData spellData, String slotId, int localSlotIndex, int globalIndex) {
        this.event.addSelectionOption(spellData, slotId, localSlotIndex, globalIndex);
    }

    @Info("    Adds spell option to the end of a player's spell bar.\n")
    public void addSelectionOption(SpellData spellData, String slotId, int localSlotIndex) {
        this.event.addSelectionOption(spellData, slotId, localSlotIndex);
    }

    public SpellSelectionManager getManager() {
        return this.event.getManager();
    }
}