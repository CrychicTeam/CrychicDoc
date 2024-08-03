package noppes.npcs.mixin;

import java.util.EnumSet;
import java.util.Map;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ GoalSelector.class })
public interface GoalSelectorMixin {

    @Accessor("lockedFlags")
    Map<Goal.Flag, WrappedGoal> lockedFlags();

    @Accessor("disabledFlags")
    EnumSet<Goal.Flag> disabledFlags();
}