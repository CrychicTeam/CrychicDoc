package com.mna.api.events;

import com.mna.api.cantrips.ICantrip;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CantripCastEvent extends Event {

    ICantrip cantrip;

    Player caster;

    public CantripCastEvent(ICantrip spell, Player caster) {
        this.cantrip = spell;
        this.caster = caster;
    }

    public ICantrip getCantrip() {
        return this.cantrip;
    }

    public Player getCrafter() {
        return this.caster;
    }

    public boolean isCancelable() {
        return false;
    }
}