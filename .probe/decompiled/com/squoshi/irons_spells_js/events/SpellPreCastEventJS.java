package com.squoshi.irons_spells_js.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.world.entity.player.Player;

public class SpellPreCastEventJS extends PlayerEventJS {

    private final SpellPreCastEvent event;

    public SpellPreCastEventJS(SpellPreCastEvent event) {
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

    @Info("    Returns the cast source.\n")
    public CastSource getCastSource() {
        return this.event.getCastSource();
    }
}