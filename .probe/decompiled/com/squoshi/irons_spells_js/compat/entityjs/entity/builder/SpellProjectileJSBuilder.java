package com.squoshi.irons_spells_js.compat.entityjs.entity.builder;

import com.squoshi.irons_spells_js.compat.entityjs.entity.SpellProjectileJS;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.liopyu.entityjs.builders.nonliving.entityjs.ProjectileEntityBuilder;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class SpellProjectileJSBuilder extends ProjectileEntityBuilder<SpellProjectileJS> {

    public transient Consumer<SpellProjectileJS.OnAntiMagicContext> onAntiMagic;

    public transient Consumer<SpellProjectileJS> trailParticles;

    public transient Consumer<SpellProjectileJS.ImpactParticleContext> impactParticles;

    public transient Object setImpactSound;

    public SpellProjectileJSBuilder(ResourceLocation i) {
        super(i);
    }

    public SpellProjectileJSBuilder onAntiMagic(Consumer<SpellProjectileJS.OnAntiMagicContext> onAntiMagic) {
        this.onAntiMagic = onAntiMagic;
        return this;
    }

    @Info("A consumer determining the impact particles for the spell.\n\nExample usage:\n```javascript\nbuilder.impactParticles(context => {\n    const {x, y, z, entity} = context\n    // Logic for spawning impact particles\n});\n```\n")
    public SpellProjectileJSBuilder impactParticles(Consumer<SpellProjectileJS.ImpactParticleContext> impactParticles) {
        this.impactParticles = impactParticles;
        return this;
    }

    @Info("A consumer determining the trailing particles behind the spell.\n\nExample usage:\n```javascript\nbuilder.trailParticles(entity => {\n    // Logic for spawning trailing particles\n});\n```\n")
    public SpellProjectileJSBuilder trailParticles(Consumer<SpellProjectileJS> trailParticles) {
        this.trailParticles = trailParticles;
        return this;
    }

    @Info("Sets the impact sound for the entity using a string representation.\n\nExample usage:\n```javascript\nbuilder.setImpactSound(\"minecraft:entity.generic.swim\");\n```\n")
    public SpellProjectileJSBuilder setImpactSound(Object sound) {
        if (sound instanceof String) {
            this.setImpactSound = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.setImpactSound = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[SpellJS]: Invalid value for setImpactSound. Value: " + sound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.generic.swim\"");
            this.setImpactSound = null;
        }
        return this;
    }

    @Override
    public EntityType.EntityFactory<SpellProjectileJS> factory() {
        return (type, level) -> new SpellProjectileJS(this, type, level);
    }
}