package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.TameableMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.CatEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class CatJSBuilder extends TameableMobBuilder<CatEntityJS> {

    public transient Boolean defaultGoals = true;

    public CatJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public CatJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<CatEntityJS> factory() {
        return (type, level) -> new CatEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return CatEntityJS.m_28168_();
    }
}