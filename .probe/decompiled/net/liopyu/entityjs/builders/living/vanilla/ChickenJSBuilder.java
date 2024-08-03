package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Function;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.liopyu.entityjs.entities.living.vanilla.ChickenEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class ChickenJSBuilder extends AnimalEntityBuilder<ChickenEntityJS> {

    public transient Boolean defaultGoals = true;

    public transient Function<LivingEntity, Object> eggTime;

    public ChickenJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public ChickenJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Info("@param eggTime Sets a function to determine the laying egg time of the entity\n\nExample usage:\n```javascript\nmobBuilder.eggTime(entity => {\n    return 100 // returning 100 here will result in the entity laying an egg every 100 ticks\n});\n```\n")
    public ChickenJSBuilder eggTime(Function<LivingEntity, Object> eggTime) {
        this.eggTime = eggTime;
        return this;
    }

    @Override
    public EntityType.EntityFactory<ChickenEntityJS> factory() {
        return (type, level) -> new ChickenEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return ChickenEntityJS.m_28263_();
    }
}