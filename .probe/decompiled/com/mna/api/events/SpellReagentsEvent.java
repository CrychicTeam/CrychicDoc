package com.mna.api.events;

import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.ArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class SpellReagentsEvent extends Event {

    final ISpellDefinition spell;

    final Player caster;

    ArrayList<SpellReagent> requiredReagents;

    public SpellReagentsEvent(ISpellDefinition spell, Player caster, ArrayList<SpellReagent> requiredReagents) {
        this.spell = spell;
        this.caster = caster;
        this.requiredReagents = requiredReagents;
    }

    public ISpellDefinition getSpell() {
        return this.spell;
    }

    public Player getCaster() {
        return this.caster;
    }

    public ArrayList<SpellReagent> getRequiredReagents() {
        return this.requiredReagents;
    }
}