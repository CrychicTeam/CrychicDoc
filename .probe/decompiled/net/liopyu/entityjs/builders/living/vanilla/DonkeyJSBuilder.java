package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.liopyu.entityjs.entities.living.vanilla.DonkeyEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class DonkeyJSBuilder extends AnimalEntityBuilder<DonkeyEntityJS> {

    public transient boolean defaultGoals = true;

    public transient Boolean defaultBehaviourGoals = true;

    public DonkeyJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultBehaviourGoals Sets whether the mob should inherit it's goal behavior from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultBehaviourGoals(false);\n```\n")
    public DonkeyJSBuilder defaultBehaviourGoals(boolean defaultBehaviourGoals) {
        this.defaultBehaviourGoals = defaultBehaviourGoals;
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public DonkeyJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<DonkeyEntityJS> factory() {
        return (type, level) -> new DonkeyEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return DonkeyEntityJS.m_30627_();
    }
}