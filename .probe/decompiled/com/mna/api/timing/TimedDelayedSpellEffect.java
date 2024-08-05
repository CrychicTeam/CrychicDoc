package com.mna.api.timing;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraft.world.phys.Vec3;

public class TimedDelayedSpellEffect implements IDelayedEvent {

    private int delay;

    private SpellSource source;

    private SpellTarget target;

    private IModifiedSpellPart<SpellEffect> component;

    private SpellContext context;

    private String identifier;

    private final boolean spawnFX;

    public TimedDelayedSpellEffect(String identifier, int delay, SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> c, SpellContext context, boolean spawnFX) {
        this.delay = delay;
        this.source = source;
        this.target = target;
        this.component = c;
        this.context = context;
        this.spawnFX = spawnFX;
        this.identifier = identifier;
    }

    public TimedDelayedSpellEffect(String identifier, int delay, SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> c, SpellContext context) {
        this(identifier, delay, source, target, c, context, true);
    }

    @Override
    public boolean tick() {
        this.delay--;
        if (this.delay != 0) {
            return false;
        } else {
            if (this.source != null && this.target != null && this.context != null && this.component != null) {
                if (this.target.isBlock() && this.component.getPart().targetsBlocks() && !this.context.hasBlockBeenAffected(this.component.getPart(), this.target.getBlock())) {
                    this.component.getPart().ApplyEffect(this.source, this.target, this.component, this.context);
                    this.context.addAffectedBlock(this.component.getPart(), this.target.getBlock());
                } else if (this.target.isEntity() && this.component.getPart().targetsEntities() && !this.context.hasEntityBeenAffected(this.component.getPart(), this.target.getEntity())) {
                    this.component.getPart().ApplyEffect(this.source, this.target, this.component, this.context);
                    this.context.addAffectedEntity(this.component.getPart(), this.target.getEntity());
                }
                if (this.spawnFX) {
                    ManaAndArtificeMod.getSpellHelper().spawnSpellVFX(this.context.getLevel(), this.target.getPosition(), Vec3.ZERO, this.source, this.component.getPart());
                }
            }
            return true;
        }
    }

    @Override
    public String getID() {
        return this.identifier;
    }
}