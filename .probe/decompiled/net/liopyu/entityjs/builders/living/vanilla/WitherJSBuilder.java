package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.WitherEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class WitherJSBuilder extends PathfinderMobBuilder<WitherEntityJS> {

    public transient Boolean defaultGoals = true;

    public transient String attackProjectile;

    public transient boolean customServerAiStep = true;

    public WitherJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param attackProjectile Sets the projectile shot by the wither.\nDefaults to a wither skull.\n\nExample usage:\n```javascript\nbuilder.attackProjectile(\"minecraft:arrow\");\n```\n")
    public WitherJSBuilder attackProjectile(String attackProjectile) {
        this.attackProjectile = attackProjectile;
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public WitherJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Info("@param customServerAiStep Sets whether the mob has its default custom server ai step behavior\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.customServerAiStep(false);\n```\n")
    public WitherJSBuilder customServerAiStep(boolean customServerAiStep) {
        this.customServerAiStep = customServerAiStep;
        return this;
    }

    @Override
    public EntityType.EntityFactory<WitherEntityJS> factory() {
        return (type, level) -> new WitherEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return WitherEntityJS.m_31501_();
    }
}