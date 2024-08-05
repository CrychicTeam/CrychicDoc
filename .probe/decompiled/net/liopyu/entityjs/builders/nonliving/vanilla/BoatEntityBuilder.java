package net.liopyu.entityjs.builders.nonliving.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;

public abstract class BoatEntityBuilder<T extends Entity & IAnimatableJSNL> extends BaseEntityBuilder<T> {

    public transient Function<Boat, Object> getDropItem;

    public transient float setShadowRadius;

    public static final List<BoatEntityBuilder<?>> thisList = new ArrayList();

    public transient Function<Boat, Object> turningBoatSpeed;

    public transient Function<Boat, Object> forwardBoatSpeed;

    public transient Function<Boat, Object> backwardsBoatSpeed;

    public BoatEntityBuilder(ResourceLocation i) {
        super(i);
        thisList.add(this);
        this.setShadowRadius = 0.3F;
    }

    @Info("Sets a function to determine the speed of the boat when it turns.\nExample usage:\n```javascript\nbuilder.turningBoatSpeed(entity => {\n    // Use information about the entity provided by the context.\n    return 1 // Some Float\n});\n```\n")
    public BoatEntityBuilder<T> turningBoatSpeed(Function<Boat, Object> function) {
        this.turningBoatSpeed = function;
        return this;
    }

    @Info("Sets a function to determine the speed of the boat when going forward.\nExample usage:\n```javascript\nbuilder.forwardBoatSpeed(entity => {\n    // Use information about the entity provided by the context.\n    return 1 // Some Float\n});\n```\n")
    public BoatEntityBuilder<T> forwardBoatSpeed(Function<Boat, Object> function) {
        this.forwardBoatSpeed = function;
        return this;
    }

    @Info("Sets a function to determine the speed of the boat when in reverse.\nExample usage:\n```javascript\nbuilder.backwardsBoatSpeed(entity => {\n    // Use information about the entity provided by the context.\n    return 1 // Some Float\n});\n```\n")
    public BoatEntityBuilder<T> backwardsBoatSpeed(Function<Boat, Object> function) {
        this.backwardsBoatSpeed = function;
        return this;
    }

    @Info("Sets the shadow radius of the entity.\nDefaults to 0.3.\nExample usage:\n```javascript\nbuilder.setShadowRadius(0.8);\n```\n")
    public BoatEntityBuilder<T> setShadowRadius(float f) {
        this.setShadowRadius = f;
        return this;
    }

    @Info("Sets a function to determine the Item the entity drops when it\nturns back into an item.\nDefaults to Boat super method.\nExample usage:\n```javascript\nbuilder.getDropItem(entity => {\n    // Use information about the entity provided by the context.\n    return Item.of('amethyst_block').item // Some Item\n});\n```\n")
    public BoatEntityBuilder<T> getDropItem(Function<Boat, Object> function) {
        this.getDropItem = function;
        return this;
    }
}