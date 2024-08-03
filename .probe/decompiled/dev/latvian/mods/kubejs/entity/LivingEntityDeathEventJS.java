package dev.latvian.mods.kubejs.entity;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Info("Invoked before a living entity dies.\n\n**NOTE**: You need to set hp to > 0 besides cancelling the event to prevent the entity from dying.\n")
public class LivingEntityDeathEventJS extends LivingEntityEventJS {

    private final LivingEntity entity;

    private final DamageSource source;

    public LivingEntityDeathEventJS(LivingEntity entity, DamageSource source) {
        this.entity = entity;
        this.source = source;
    }

    @Info("The entity that dies.")
    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Info("The damage source that triggers the death.")
    public DamageSource getSource() {
        return this.source;
    }
}