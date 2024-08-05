package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.liopyu.entityjs.entities.living.vanilla.PandaEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class PandaJSBuilder extends AnimalEntityBuilder<PandaEntityJS> {

    public transient Boolean defaultGoals = true;

    public PandaJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public PandaJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<PandaEntityJS> factory() {
        return (type, level) -> new PandaEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return PandaEntityJS.m_29157_();
    }
}