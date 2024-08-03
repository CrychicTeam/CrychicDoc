package net.liopyu.entityjs.builders.modification;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Function;
import net.liopyu.entityjs.util.ContextUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class ModifyPathfinderMobBuilder extends ModifyMobBuilder {

    public transient Function<Mob, Object> shouldStayCloseToLeashHolder;

    public transient Double followLeashSpeed;

    public transient Function<ContextUtils.EntityBlockPosLevelContext, Object> walkTargetValue;

    public ModifyPathfinderMobBuilder(EntityType<?> entity) {
        super(entity);
    }

    @Info("Sets the function to determine whether the entity should stay close to its leash holder.\n\n@param predicate A Function accepting a {@link Mob} parameter,\n                 defining the condition for the entity to stay close to its leash holder.\n\nExample usage:\n```javascript\nmobBuilder.shouldStayCloseToLeashHolder(entity => {\n    // Custom logic to determine whether the entity should stay close to its leash holder.\n    return true;\n});\n```\n")
    public ModifyPathfinderMobBuilder shouldStayCloseToLeashHolder(Function<Mob, Object> predicate) {
        this.shouldStayCloseToLeashHolder = predicate;
        return this;
    }

    @Info("Sets the follow leash speed for the entity.\n\n@param speed The follow leash speed.\n\nExample usage:\n```javascript\nmobBuilder.followLeashSpeed(1.5);\n```\n")
    public ModifyPathfinderMobBuilder followLeashSpeed(double speed) {
        this.followLeashSpeed = speed;
        return this;
    }

    @Info("Sets the walk target value function for the entity.\n\n@param function A Function accepting a {@link ContextUtils.EntityBlockPosLevelContext} parameter,\n                defining the walk target value based on the entity's interaction with a specific block.\n\nExample usage:\n```javascript\nmobBuilder.walkTargetValue(context => {\n    // Custom logic to calculate the walk target value based on the provided context.\n    // Access information about the block position and level using the provided context.\n    return 10;\n});\n```\n")
    public ModifyPathfinderMobBuilder walkTargetValue(Function<ContextUtils.EntityBlockPosLevelContext, Object> function) {
        this.walkTargetValue = function;
        return this;
    }
}