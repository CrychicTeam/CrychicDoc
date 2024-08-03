package com.squoshi.irons_spells_js.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.world.entity.player.Player;

public class SpellOnCastEventJS extends PlayerEventJS {

    private final SpellOnCastEvent event;

    public SpellOnCastEventJS(SpellOnCastEvent event) {
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

    @Info("    Returns the spell ID of the spell that was cast.\n")
    public String getSpellId() {
        return this.event.getSpellId();
    }

    @Info("    Returns the school type of the spell that was cast.\n")
    public SchoolType getSchoolType() {
        return this.event.getSchoolType();
    }

    @Info("    Returns the new spell level of the spell that was cast.\n")
    public int getSpellLevel() {
        return this.event.getSpellLevel();
    }

    @Info("    Returns the original spell level of the spell that was cast.\n")
    public int getOriginalSpellLevel() {
        return this.event.getOriginalSpellLevel();
    }

    @Info("    Sets the new spell level of the spell that was cast.\n")
    public void setSpellLevel(int spellLevel) {
        this.event.setSpellLevel(spellLevel);
    }

    @Info("    Returns the cast source.\n")
    public CastSource getCastSource() {
        return this.event.getCastSource();
    }

    @Info("    Returns the original mana cost.\n")
    public int getOriginalManaCost() {
        return this.event.getOriginalManaCost();
    }

    @Info("    Returns the new mana cost.\n")
    public int getManaCost() {
        return this.event.getManaCost();
    }

    @Info("    Sets the new mana cost.\n")
    public void setManaCost(int manaCost) {
        this.event.setManaCost(manaCost);
    }
}