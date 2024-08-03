package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.ZombieEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class ZombieJSBuilder extends PathfinderMobBuilder<ZombieEntityJS> {

    public transient Boolean defaultBehaviourGoals = true;

    public transient Boolean defaultGoals = true;

    public transient boolean isSunSensitive = true;

    public transient boolean convertsInWater = true;

    public ZombieJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param isSunSensitive Sets whether the mob should convert in water to another mob\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.convertsInWater(false);\n```\n")
    public ZombieJSBuilder convertsInWater(boolean convertsInWater) {
        this.convertsInWater = convertsInWater;
        return this;
    }

    @Info("@param isSunSensitive Sets whether the mob should burn in daylight\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.isSunSensitive(false);\n```\n")
    public ZombieJSBuilder isSunSensitive(boolean isSunSensitive) {
        this.isSunSensitive = isSunSensitive;
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public ZombieJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Info("@param defaultBehaviourGoals Sets whether the mob should inherit it's goal behavior from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultBehaviourGoals(false);\n```\n")
    public ZombieJSBuilder defaultBehaviourGoals(boolean defaultBehaviourGoals) {
        this.defaultBehaviourGoals = defaultBehaviourGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<ZombieEntityJS> factory() {
        return (type, level) -> new ZombieEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return ZombieEntityJS.m_34328_();
    }
}