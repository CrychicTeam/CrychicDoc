package net.liopyu.entityjs.builders.nonliving.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.BaseNonAnimatableEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.NonAnimatableEntityTypeBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IProjectileEntityJS;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public abstract class EyeOfEnderEntityBuilder<T extends Entity & IProjectileEntityJS> extends BaseNonAnimatableEntityBuilder<T> {

    public transient Function<T, Object> textureLocation = t -> ((IProjectileEntityJS) t).getProjectileBuilder().newID("textures/entity/projectiles/", ".png");

    public static final List<EyeOfEnderEntityBuilder<?>> thisList = new ArrayList();

    public transient Float pX;

    public transient Float pY;

    public transient Float pZ;

    public transient Float vX;

    public transient Float vY;

    public transient Float vZ;

    public EyeOfEnderEntityBuilder(ResourceLocation i) {
        super(i);
        thisList.add(this);
    }

    @Override
    public EntityType<T> createObject() {
        return new NonAnimatableEntityTypeBuilder<T>(this).get();
    }

    @Info("Sets the scale for rendering the projectile entity.\n\n@param pX The X-axis scale.\n\n@param pY The Y-axis scale.\n\n@param pZ The Z-axis scale.\n\nExample usage:\n```javascript\nprojectileEntityBuilder.renderScale(1.5, 1.5, 1.5);\n```\n")
    public EyeOfEnderEntityBuilder<T> renderScale(Float pX, Float pY, Float pZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
        return this;
    }

    @Info("Sets the offset for rendering the projectile entity.\n\n@param vX The X-axis offset.\n\n@param vY The Y-axis offset.\n\n@param vZ The Z-axis offset.\n\nExample usage:\n```javascript\nprojectileEntityBuilder.renderOffset(0.5, 1.0, -0.5);\n```\n")
    public EyeOfEnderEntityBuilder<T> renderOffset(Float vX, Float vY, Float vZ) {
        this.vX = vX;
        this.vY = vY;
        this.vZ = vZ;
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/projectiles/<path>.png.\n\nExample usage:\n```javascript\nprojectileBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public EyeOfEnderEntityBuilder<T> textureLocation(Function<T, Object> function) {
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
}