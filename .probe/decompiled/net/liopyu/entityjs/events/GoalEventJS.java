package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.RangedAttackMob;

public abstract class GoalEventJS<T extends Mob> extends EventJS {

    protected final T mob;

    protected final GoalSelector selector;

    protected final boolean isPathFinder;

    protected final boolean isAnimal;

    protected final boolean isTamable;

    protected final boolean isRangedAttack;

    protected final boolean isHorse;

    protected final boolean isNeutral;

    public GoalEventJS(T mob, GoalSelector selector) {
        this.mob = mob;
        this.selector = selector;
        this.isPathFinder = mob instanceof PathfinderMob;
        this.isAnimal = mob instanceof Animal;
        this.isTamable = mob instanceof TamableAnimal;
        this.isRangedAttack = mob instanceof RangedAttackMob;
        this.isHorse = mob instanceof AbstractHorse;
        this.isNeutral = mob instanceof NeutralMob;
    }

    public Mob getEntity() {
        return this.mob;
    }
}