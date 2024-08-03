package com.mna.api.events;

import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SpellCastEvent extends Event {

    ISpellDefinition spell;

    Player caster;

    public SpellCastEvent(ISpellDefinition spell, Player caster) {
        this.spell = spell;
        this.caster = caster;
    }

    public ISpellDefinition getSpell() {
        return this.spell;
    }

    public Player getCaster() {
        return this.caster;
    }
}