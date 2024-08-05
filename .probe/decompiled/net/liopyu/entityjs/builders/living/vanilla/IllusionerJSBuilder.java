package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.IllusionerEntityJS;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class IllusionerJSBuilder extends PathfinderMobBuilder<IllusionerEntityJS> {

    public transient Boolean defaultGoals = true;

    public transient Object setCelebrateSound;

    public IllusionerJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets the sound to play when the entity is celebrating using either a string representation or a ResourceLocation object.\n\nExample usage:\n```javascript\nmobBuilder.setCelebrateSound(\"minecraft:entity.zombie.ambient\");\n```\n")
    public IllusionerJSBuilder setCelebrateSound(Object ambientSound) {
        if (ambientSound instanceof String) {
            this.setCelebrateSound = new ResourceLocation((String) ambientSound);
        } else if (ambientSound instanceof ResourceLocation resourceLocation) {
            this.setCelebrateSound = resourceLocation;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for setCelebrateSound. Value: " + ambientSound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.zombie.ambient\"");
            this.setCelebrateSound = null;
        }
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public IllusionerJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<IllusionerEntityJS> factory() {
        return (type, level) -> new IllusionerEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return IllusionerEntityJS.m_32931_();
    }
}