package com.squoshi.irons_spells_js.compat.entityjs.entity.builder;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public abstract class SpellCastingMobBuilder<T extends PathfinderMob & IAnimatableJS> extends PathfinderMobBuilder<T> {

    public transient Consumer<LivingEntity> onCancelledCast;

    public transient Function<LivingEntity, Object> isCasting;

    public SpellCastingMobBuilder(ResourceLocation i) {
        super(i);
    }

    public SpellCastingMobBuilder<T> isCasting(Function<LivingEntity, Object> isCasting) {
        this.isCasting = isCasting;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops casting a spell.\n\nExample usage:\n```javascript\nspellEntityBuilder.onCancelledCast(entity => {\n    // Custom logic to handle the entity cancelling their spell casts\n});\n```\n")
    public SpellCastingMobBuilder<T> onCancelledCast(Consumer<LivingEntity> onCancelledCast) {
        this.onCancelledCast = onCancelledCast;
        return this;
    }
}