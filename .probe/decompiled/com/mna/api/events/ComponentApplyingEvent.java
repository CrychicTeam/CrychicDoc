package com.mna.api.events;

import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ComponentApplyingEvent extends Event {

    SpellSource source;

    SpellContext context;

    SpellTarget target;

    SpellEffect component;

    public ComponentApplyingEvent(SpellSource source, SpellContext context, SpellTarget target, SpellEffect component) {
        this.source = source;
        this.context = context;
        this.target = target;
        this.component = component;
    }

    public SpellSource getSource() {
        return this.source;
    }

    public SpellContext getContext() {
        return this.context;
    }

    public SpellTarget getTarget() {
        return this.target;
    }

    public SpellEffect getComponent() {
        return this.component;
    }

    public boolean isCancelable() {
        return true;
    }
}