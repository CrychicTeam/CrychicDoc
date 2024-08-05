package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.EvokerEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EvokerJSBuilder extends PathfinderMobBuilder<EvokerEntityJS> {

    public transient Boolean defaultGoals = true;

    public EvokerJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public EvokerJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<EvokerEntityJS> factory() {
        return (type, level) -> new EvokerEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return EvokerEntityJS.m_32657_();
    }
}