package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.liopyu.entityjs.builders.living.entityjs.TameableMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.ParrotEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class ParrotJSBuilder extends TameableMobBuilder<ParrotEntityJS> {

    public transient Consumer<ContextUtils.PlayerEntityContext> onTamed;

    public transient Consumer<ContextUtils.PlayerEntityContext> tameOverride;

    public transient Boolean defaultGoals = true;

    public ParrotJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets a Consumer invoked after the entity is tamed\nand replaces the logic used to set the UUID of the owner\nwith the parameter of ContextUtils.PlayerEntityContext callback\n\n@param tameOverride A Consumer responsible for determining the uuid to set when the entity is tamed.\n\nExample usage:\n```javascript\nbuilder.tameOverride(context => {\n    const {entity,player} = context\n    // Mimic the vanilla way of setting the uuid when the entity is tamed.\n    entity.setOwnerUUID(player.getUUID());\n});\n```\n")
    public ParrotJSBuilder tameOverride(Consumer<ContextUtils.PlayerEntityContext> tameOverride) {
        this.tameOverride = tameOverride;
        return this;
    }

    @Info("Sets a Consumer with the parameter of ContextUtils.PlayerEntityContext callback\nThis is fired after the entity is tamed and all tame logic has already taken place.\nUseful if you don't want to mess with the UUID logic in the tameOverride method.\n\n@param onTamed A Consumer that fires when the entity is tamed.\n\nExample usage:\n```javascript\nbuilder.onTamed(entity => {\n    // Do stuff when the entity is tamed.\n});\n```\n")
    public ParrotJSBuilder onTamed(Consumer<ContextUtils.PlayerEntityContext> onTamed) {
        this.onTamed = onTamed;
        return this;
    }

    @Info("@param defaultGoals Sets whether the mob should inherit it's goals from it's superclass\nDefaults to true.\n\nExample usage:\n```javascript\nbuilder.defaultGoals(false);\n```\n")
    public ParrotJSBuilder defaultGoals(boolean defaultGoals) {
        this.defaultGoals = defaultGoals;
        return this;
    }

    @Override
    public EntityType.EntityFactory<ParrotEntityJS> factory() {
        return (type, level) -> new ParrotEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return ParrotEntityJS.m_29438_();
    }
}