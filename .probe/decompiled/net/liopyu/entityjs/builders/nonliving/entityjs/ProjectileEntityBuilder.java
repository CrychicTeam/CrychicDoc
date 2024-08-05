package net.liopyu.entityjs.builders.nonliving.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.BaseNonAnimatableEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.NonAnimatableEntityTypeBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IProjectileEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public abstract class ProjectileEntityBuilder<T extends Entity & IProjectileEntityJS> extends BaseNonAnimatableEntityBuilder<T> {

    public transient Function<T, Object> textureLocation = t -> ((IProjectileEntityJS) t).getProjectileBuilder().newID("textures/entity/projectiles/", ".png");

    public static final List<ProjectileEntityBuilder<?>> thisList = new ArrayList();

    public transient Consumer<ContextUtils.ProjectileEntityHitContext> onHitEntity;

    public transient Consumer<ContextUtils.ProjectileBlockHitContext> onHitBlock;

    public transient Function<Entity, Object> canHitEntity;

    public transient Float pX;

    public transient Float pY;

    public transient Float pZ;

    public transient Float vX;

    public transient Float vY;

    public transient Float vZ;

    public transient Consumer<ContextUtils.CollidingProjectileEntityContext> onEntityCollision;

    public ProjectileEntityBuilder(ResourceLocation i) {
        super(i);
        thisList.add(this);
    }

    @Override
    public EntityType<T> createObject() {
        return new NonAnimatableEntityTypeBuilder<T>(this).get();
    }

    @Info("Sets the scale for rendering the projectile entity.\n\n@param pX The X-axis scale.\n\n@param pY The Y-axis scale.\n\n@param pZ The Z-axis scale.\n\nExample usage:\n```javascript\nprojectileEntityBuilder.renderScale(1.5, 1.5, 1.5);\n```\n")
    public ProjectileEntityBuilder<T> renderScale(Float pX, Float pY, Float pZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
        return this;
    }

    @Info("Sets the offset for rendering the projectile entity.\n\n@param vX The X-axis offset.\n\n@param vY The Y-axis offset.\n\n@param vZ The Z-axis offset.\n\nExample usage:\n```javascript\nprojectileEntityBuilder.renderOffset(0.5, 1.0, -0.5);\n```\n")
    public ProjectileEntityBuilder<T> renderOffset(Float vX, Float vY, Float vZ) {
        this.vX = vX;
        this.vY = vY;
        this.vZ = vZ;
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/projectiles/<path>.png.\n\nExample usage:\n```javascript\nprojectileBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public ProjectileEntityBuilder<T> textureLocation(Function<T, Object> function) {
        this.textureLocation = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("Invalid texture resource in projectile builder: " + obj + "Defaulting to " + ((IProjectileEntityJS) entity).getProjectileBuilder().newID("textures/entity/projectiles/", ".png"));
                return ((IProjectileEntityJS) entity).getProjectileBuilder().newID("textures/entity/projectiles/", ".png");
            }
        };
        return this;
    }

    @Info("Sets a callback function to be executed when the projectile\ncollides with an entity.\n\nExample usage:\n```javascript\narrowEntityBuilder.onEntityCollision(context => {\n    const { entity, target } = context\n    console.log(entity)\n});\n```\n")
    public ProjectileEntityBuilder<T> onEntityCollision(Consumer<ContextUtils.CollidingProjectileEntityContext> consumer) {
        this.onEntityCollision = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the projectile hits an entity.\nThe provided Consumer accepts a {@link ContextUtils.ProjectileEntityHitContext} parameter,\nrepresenting the context of the projectile's interaction with a specific entity.\n\nExample usage:\n```javascript\nprojectileBuilder.onHitEntity(context -> {\n    // Custom logic to handle the projectile hitting an entity.\n    // Access information about the entity and projectile using the provided context.\n});\n```\n")
    public ProjectileEntityBuilder<T> onHitEntity(Consumer<ContextUtils.ProjectileEntityHitContext> consumer) {
        this.onHitEntity = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the projectile hits a block.\nThe provided Consumer accepts a {@link ContextUtils.ProjectileBlockHitContext} parameter,\nrepresenting the context of the projectile's interaction with a specific block.\n\nExample usage:\n```javascript\nprojectileBuilder.onHitBlock(context -> {\n    // Custom logic to handle the projectile hitting a block.\n    // Access information about the block and projectile using the provided context.\n});\n```\n")
    public ProjectileEntityBuilder<T> onHitBlock(Consumer<ContextUtils.ProjectileBlockHitContext> consumer) {
        this.onHitBlock = consumer;
        return this;
    }

    @Info("Sets a function to determine if the projectile entity can hit a specific entity.\n\n@param canHitEntity The predicate to check if the arrow can hit the entity.\n\nExample usage:\n```javascript\nprojectileEntityBuilder.canHitEntity(entity -> {\n    // Custom logic to determine if the projectile can hit the specified entity\n    // Return true if the arrow can hit, false otherwise.\n});\n```\n")
    public ProjectileEntityBuilder<T> canHitEntity(Function<Entity, Object> function) {
        this.canHitEntity = function;
        return this;
    }
}