package com.squoshi.irons_spells_js.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.entity.player.Player;

public class ChangeManaEventJS extends PlayerEventJS {

    private final ChangeManaEvent event;

    public ChangeManaEventJS(ChangeManaEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return this.event.getEntity();
    }

    @Info("    Returns the float mana value that the value was before it was changed.\n")
    public float getOldMana() {
        return this.event.getOldMana();
    }

    @Info("    Returns the float mana value that the value changed to after it was changed.\n")
    public float getNewMana() {
        return this.event.getNewMana();
    }

    @Info("    Changes the value that the mana will change to during the event.\n")
    public void setNewMana(float newMana) {
        this.event.setNewMana(newMana);
    }

    @Info("    Returns the player's current MagicData.\n")
    public MagicData getMagicData() {
        return this.event.getMagicData();
    }
}