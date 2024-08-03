package com.mna.api.events;

import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class SpellCraftedEvent extends Event {

    ISpellDefinition spell;

    Player caster;

    public SpellCraftedEvent(ISpellDefinition spell, Player caster) {
        this.spell = spell;
        this.caster = caster;
    }

    public ISpellDefinition getSpellRecipe() {
        return this.spell;
    }

    public Player getCrafter() {
        return this.caster;
    }

    public boolean isCancelable() {
        return false;
    }
}