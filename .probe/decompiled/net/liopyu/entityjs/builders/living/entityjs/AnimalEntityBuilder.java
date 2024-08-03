package net.liopyu.entityjs.builders.living.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class AnimalEntityBuilder<T extends Animal & IAnimatableJS> extends PathfinderMobBuilder<T> {

    public transient Function<ContextUtils.BreedableEntityContext, Object> setBreedOffspring;

    public transient Ingredient isFood;

    public transient Function<ContextUtils.EntityItemStackContext, Object> isFoodPredicate;

    public transient Function<LivingEntity, Object> canBreed;

    public transient Function<ContextUtils.EntityAnimalContext, Object> canMate;

    public transient Consumer<ContextUtils.LevelAnimalContext> onSpawnChildFromBreeding;

    public AnimalEntityBuilder(ResourceLocation i) {
        super(i);
        this.followLeashSpeed = 1.0;
    }

    @Info("Sets the offspring for the Animal Entity.\n\n@param breedOffspring Function returning a resource location for the breed offspring.\n\nExample usage:\n```javascript\nanimalBuilder.setBreedOffspring(context => {\n    const { entity, mate, level } = context\n    // Use the context to return a ResourceLocation of an entity to spawn when the entity mates\n    return 'minecraft:cow' //Some Resource location representing the entity to spawn.\n})\n```\n")
    public AnimalEntityBuilder<T> setBreedOffspring(Function<ContextUtils.BreedableEntityContext, Object> breedOffspring) {
        this.setBreedOffspring = breedOffspring;
        return this;
    }

    @Info("Sets a predicate to determine if the animal entity can breed.\n\n@param canBreed A Function that defines the conditions for breeding.\n\nExample usage:\n```javascript\nanimalBuilder.canBreed(entity => {\n    // Custom logic to determine if the entity can breed\n    // Return true if the entity can breed, false otherwise.\n});\n```\n")
    public AnimalEntityBuilder<T> canBreed(Function<LivingEntity, Object> canBreed) {
        this.canBreed = canBreed;
        return this;
    }

    @Info("Sets the ingredient representing the list of items that the animal entity can eat.\n\n@param isFood An {@link Ingredient} specifying the items that the entity can eat.\n\nExample usage:\n```javascript\nanimalBuilder.isFood([\n    \"#minecraft:apple\",\n    \"minecraft:golden_apple\",\n    \"minecraft:diamond\"\n]);\n```\n")
    public AnimalEntityBuilder<T> isFood(Ingredient isFood) {
        this.isFood = isFood;
        return this;
    }

    @Info("Sets the predicate to determine if an entity item stack is considered as food for the animal entity.\n\n@param isFoodPredicate A predicate accepting a {@link ContextUtils.EntityItemStackContext} parameter,\n                       defining the conditions for an entity item stack to be considered as food.\n\nExample usage:\n```javascript\nanimalBuilder.isFoodPredicate(context => {\n    // Custom logic to determine if the entity item stack is considered as food.\n    // Access information about the item stack using the provided context.\n    return true // Some Boolean value;\n});\n```\n")
    public AnimalEntityBuilder<T> isFoodPredicate(Function<ContextUtils.EntityItemStackContext, Object> isFoodPredicate) {
        this.isFoodPredicate = isFoodPredicate;
        return this;
    }

    @Info("Sets a predicate to determine if the entity can mate.\n\n@param predicate A Function accepting a ContextUtils.EntityAnimalContext parameter,\n                 defining the condition for the entity to be able to mate.\n\nExample usage:\n```javascript\nanimalBuilder.canMate(context => {\n    // Custom logic to determine if the entity can mate\n    // Return true if mating is allowed based on the provided context.\n});\n```\n")
    public AnimalEntityBuilder<T> canMate(Function<ContextUtils.EntityAnimalContext, Object> predicate) {
        this.canMate = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when a child is spawned from breeding.\n\n@param consumer A Consumer accepting a ContextUtils.LevelAnimalContext parameter,\n                 defining the behavior to be executed when a child is spawned from breeding.\n\nExample usage:\n```javascript\nanimalBuilder.onSpawnChildFromBreeding(context => {\n    // Custom logic to handle the spawning of a child from breeding\n    // Access information about the breeding event using the provided context.\n});\n```\n")
    public AnimalEntityBuilder<T> onSpawnChildFromBreeding(Consumer<ContextUtils.LevelAnimalContext> consumer) {
        this.onSpawnChildFromBreeding = consumer;
        return this;
    }
}