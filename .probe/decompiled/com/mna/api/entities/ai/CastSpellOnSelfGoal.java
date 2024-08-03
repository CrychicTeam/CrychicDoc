package com.mna.api.entities.ai;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.targeting.SpellSource;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class CastSpellOnSelfGoal<T extends Mob> extends Goal {

    private final T user;

    private final ItemStack stack;

    private final Predicate<? super T> shouldCast;

    private final Consumer<? super T> onStartHandler;

    private final Consumer<? super T> onStopHandler;

    private final int warmup;

    private int counter;

    public CastSpellOnSelfGoal(T user, ItemStack stack, Predicate<? super T> shouldCast) {
        this(user, stack, shouldCast, null, null, 0);
    }

    public CastSpellOnSelfGoal(T user, ItemStack stack, Predicate<? super T> shouldCast, @Nullable Consumer<? super T> onStartHandler, @Nullable Consumer<? super T> onStopHandler, int warmup) {
        this.user = user;
        this.stack = stack;
        this.shouldCast = shouldCast;
        this.warmup = warmup;
        this.onStartHandler = onStartHandler;
        this.onStopHandler = onStopHandler;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canContinueToUse() {
        return this.counter < this.warmup;
    }

    @Override
    public boolean canUse() {
        return this.shouldCast.test(this.user);
    }

    @Override
    public void start() {
        if (this.onStartHandler != null) {
            this.onStartHandler.accept(this.user);
        }
        this.counter = 0;
    }

    @Override
    public void stop() {
        if (this.onStopHandler != null) {
            this.onStopHandler.accept(this.user);
        }
        this.counter = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        if (++this.counter >= this.warmup) {
            ManaAndArtificeMod.getSpellHelper().affect(this.stack, ManaAndArtificeMod.getSpellHelper().parseSpellDefinition(this.stack), this.user.m_9236_(), new SpellSource(this.user, InteractionHand.MAIN_HAND));
            if (this.onStopHandler != null) {
                this.onStopHandler.accept(this.user);
            }
        }
    }
}