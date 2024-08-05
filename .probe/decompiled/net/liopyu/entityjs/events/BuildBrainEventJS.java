package net.liopyu.entityjs.events;

import com.google.common.collect.ImmutableList;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.List;
import java.util.Set;
import net.liopyu.entityjs.util.ai.Behaviors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.schedule.Activity;

public class BuildBrainEventJS<T extends LivingEntity> extends EventJS {

    private final Brain<T> base;

    public final Behaviors behaviors = Behaviors.INSTANCE;

    public BuildBrainEventJS(Brain<T> base) {
        this.base = base;
    }

    public void coreActivity(int i, List<Behavior<? super LivingEntity>> behaviors) {
        this.base.addActivity(Activity.CORE, i, ImmutableList.copyOf(behaviors));
        this.base.setCoreActivities(Set.of(Activity.CORE));
    }

    public void idleActivity(int i, List<Behavior<? super LivingEntity>> behaviors) {
        this.base.addActivity(Activity.IDLE, i, ImmutableList.copyOf(behaviors));
        this.base.setDefaultActivity(Activity.IDLE);
    }

    public void addActivity(Activity activity, int i, List<Behavior<? super LivingEntity>> behaviors) {
        this.base.addActivity(activity, i, ImmutableList.copyOf(behaviors));
    }
}