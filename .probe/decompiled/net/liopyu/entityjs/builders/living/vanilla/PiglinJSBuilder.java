package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.PiglinEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class PiglinJSBuilder extends PathfinderMobBuilder<PiglinEntityJS> {

    public transient Boolean defaultGoals = true;

    public transient Function<LivingEntity, Object> isConverting;

    public transient Consumer<ContextUtils.EntityServerLevelContext> finishConversion;

    public PiglinJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets a consumer responsible for spawning an entity after the mob has converted.\n\n@param finishConversion A Function accepting an entity parameter\n\nExample usage:\n```javascript\nmobBuilder.finishConversion(entity => {\n    //Convert to a ghast instead of a zombified piglin when in the overworld\n    let EntityType = Java.loadClass(\"net.minecraft.world.entity.EntityType\");\n    entity.convertTo(EntityType.GHAST, true);\n});\n```\n")
    public PiglinJSBuilder finishConversion(Consumer<ContextUtils.EntityServerLevelContext> finishConversion) {
        this.finishConversion = finishConversion;
        return this;
    }

    @Info("Sets a function to determine if the entity is converting.\n\n@param isConverting A Function accepting an entity parameter\n\nExample usage:\n```javascript\nmobBuilder.isConverting(entity => {\n    return entity.age > 500;\n});\n```\n")
    public PiglinJSBuilder isConverting(Function<LivingEntity, Object> isConverting) {
        this.isConverting = isConverting;
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public PiglinJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<PiglinEntityJS> factory() {
        return (type, level) -> new PiglinEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return PiglinEntityJS.m_34770_();
    }
}