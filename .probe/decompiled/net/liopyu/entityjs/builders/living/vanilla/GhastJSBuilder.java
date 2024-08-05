package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.MobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.GhastEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class GhastJSBuilder extends MobBuilder<GhastEntityJS> {

    public transient Boolean defaultGoals = true;

    public GhastJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public GhastJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<GhastEntityJS> factory() {
        return (type, level) -> new GhastEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return GhastEntityJS.m_32752_();
    }
}