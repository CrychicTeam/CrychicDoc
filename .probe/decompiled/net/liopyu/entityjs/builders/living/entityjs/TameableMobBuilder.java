package net.liopyu.entityjs.builders.living.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class TameableMobBuilder<T extends TamableAnimal & IAnimatableJS> extends AnimalEntityBuilder<T> {

    public transient Ingredient tamableFood;

    public transient Function<ContextUtils.EntityItemStackContext, Object> tamableFoodPredicate;

    public transient Consumer<ContextUtils.PlayerEntityContext> onTamed;

    public transient Consumer<ContextUtils.PlayerEntityContext> tameOverride;

    public TameableMobBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets a Consumer invoked after the entity is tamed\nand replaces the logic used to set the UUID of the owner\nwith the parameter of ContextUtils.PlayerEntityContext callback\n\n@param tameOverride A Consumer responsible for determining the uuid to set when the entity is tamed.\n\nExample usage:\n```javascript\nmobBuilder.tameOverride(context => {\n    const {entity,player} = context\n    // Mimic the vanilla way of setting the uuid when the entity is tamed.\n    entity.setOwnerUUID(player.getUUID());\n});\n```\n")
    public MobBuilder<T> tameOverride(Consumer<ContextUtils.PlayerEntityContext> tameOverride) {
        this.tameOverride = tameOverride;
        return this;
    }

    @Info("Sets a Consumer with the parameter of ContextUtils.PlayerEntityContext callback\nThis is fired after the entity is tamed and all tame logic has already taken place.\nUseful if you don't want to mess with the UUID logic in the tameOverride method.\n\n@param onTamed A Consumer that fires when the entity is tamed.\n\nExample usage:\n```javascript\nmobBuilder.onTamed(entity => {\n    // Do stuff when the entity is tamed.\n});\n```\n")
    public MobBuilder<T> onTamed(Consumer<ContextUtils.PlayerEntityContext> onTamed) {
        this.onTamed = onTamed;
        return this;
    }

    @Info("Sets a function to determine if the player's current itemstack will tame the mob.\n\n@param tamableFoodPredicate A Function accepting a ContextUtils.EntityItemStackContext parameter\n\nExample usage:\n```javascript\nmobBuilder.tamableFood([\n    'minecraft:diamond',\n    'minecraft:wheat'\n]);\n```\n")
    public MobBuilder<T> tamableFood(Ingredient tamableFood) {
        this.tamableFood = tamableFood;
        return this;
    }

    @Info("Sets a function to determine if the player's current itemstack will tame the mob.\n\n@param tamableFoodPredicate A Function accepting a ContextUtils.EntityItemStackContext parameter\n\nExample usage:\n```javascript\nmobBuilder.tamableFoodPredicate(context => {\n    const { entity, item } = context\n    return item.id == 'minecraft:diamond' // Return true if the player's current itemstack will tame the mob.\n});\n```\n")
    public MobBuilder<T> tamableFoodPredicate(Function<ContextUtils.EntityItemStackContext, Object> tamableFoodPredicate) {
        this.tamableFoodPredicate = tamableFoodPredicate;
        return this;
    }
}