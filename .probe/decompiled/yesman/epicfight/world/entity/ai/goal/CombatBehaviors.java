package yesman.epicfight.world.entity.ai.goal;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

public class CombatBehaviors<T extends MobPatch<?>> {

    private final List<CombatBehaviors.BehaviorSeries<T>> behaviorSeriesList = Lists.newArrayList();

    private final T mobpatch;

    private int currentBehaviorPointer;

    protected CombatBehaviors(CombatBehaviors.Builder<T> builder, T mobpatch) {
        builder.behaviorSeriesList.stream().map(behaviorSeriesBuilder -> behaviorSeriesBuilder.build()).forEach(this.behaviorSeriesList::add);
        this.mobpatch = mobpatch;
        this.currentBehaviorPointer = -1;
    }

    private int getRandomCombatBehaviorSeries() {
        List<Integer> candidates = Lists.newArrayList();
        List<Float> rescaledWeight = Lists.newArrayList();
        float weightSum = 0.0F;
        for (int i = 0; i < this.behaviorSeriesList.size(); i++) {
            if (this.currentBehaviorPointer != i) {
                CombatBehaviors.BehaviorSeries<T> move = (CombatBehaviors.BehaviorSeries<T>) this.behaviorSeriesList.get(i);
                boolean result = move.test(this.mobpatch);
                if (result) {
                    weightSum += move.weight;
                    candidates.add(i);
                }
            }
        }
        for (int ix : candidates) {
            rescaledWeight.add(((CombatBehaviors.BehaviorSeries) this.behaviorSeriesList.get(ix)).weight / weightSum);
        }
        float random = this.mobpatch.getOriginal().m_217043_().nextFloat();
        float delta = 0.0F;
        for (int ix = 0; ix < candidates.size(); ix++) {
            int index = (Integer) candidates.get(ix);
            delta += rescaledWeight.get(ix);
            if (random < delta) {
                ((CombatBehaviors.BehaviorSeries) this.behaviorSeriesList.get(index)).resetCooldown(this, true);
                return index;
            }
        }
        return -1;
    }

    public void execute(int seriesPointer) {
        this.currentBehaviorPointer = seriesPointer;
        CombatBehaviors.BehaviorSeries<T> behaviorSeries = (CombatBehaviors.BehaviorSeries<T>) this.behaviorSeriesList.get(seriesPointer);
        CombatBehaviors.Behavior<T> behavior = (CombatBehaviors.Behavior<T>) behaviorSeries.behaviors.get(behaviorSeries.nextBehaviorPointer);
        behaviorSeries.upCounter();
        behavior.execute(this.mobpatch);
    }

    public void resetCooldown(int seriesPointer, boolean resetSharingCooldown) {
        ((CombatBehaviors.BehaviorSeries) this.behaviorSeriesList.get(seriesPointer)).resetCooldown(this, resetSharingCooldown);
    }

    public CombatBehaviors.Behavior<T> selectRandomBehaviorSeries() {
        int seriesPointer = this.getRandomCombatBehaviorSeries();
        if (seriesPointer >= 0) {
            this.currentBehaviorPointer = seriesPointer;
            CombatBehaviors.BehaviorSeries<T> behaviorSeries = (CombatBehaviors.BehaviorSeries<T>) this.behaviorSeriesList.get(seriesPointer);
            CombatBehaviors.Behavior<T> behavior = (CombatBehaviors.Behavior<T>) behaviorSeries.behaviors.get(behaviorSeries.nextBehaviorPointer);
            behaviorSeries.upCounter();
            if (behaviorSeries.loopFinished && !behaviorSeries.looping) {
                behaviorSeries.loopFinished = false;
                this.currentBehaviorPointer = -1;
            }
            return behavior;
        } else {
            return null;
        }
    }

    public CombatBehaviors.Behavior<T> tryProceed() {
        CombatBehaviors.BehaviorSeries<T> currentBehaviorSeries = (CombatBehaviors.BehaviorSeries<T>) this.behaviorSeriesList.get(this.currentBehaviorPointer);
        if (currentBehaviorSeries.canBeInterrupted) {
            int seriesPointer = this.getRandomCombatBehaviorSeries();
            if (seriesPointer >= 0 && this.currentBehaviorPointer != seriesPointer) {
                this.currentBehaviorPointer = seriesPointer;
                CombatBehaviors.BehaviorSeries<T> newCombatBehaviorSeries = (CombatBehaviors.BehaviorSeries<T>) this.behaviorSeriesList.get(seriesPointer);
                return (CombatBehaviors.Behavior<T>) newCombatBehaviorSeries.behaviors.get(newCombatBehaviorSeries.nextBehaviorPointer);
            }
        }
        if (currentBehaviorSeries.loopFinished && !currentBehaviorSeries.looping) {
            currentBehaviorSeries.loopFinished = false;
            this.currentBehaviorPointer = -1;
            return null;
        } else {
            CombatBehaviors.Behavior<T> nextBehavior = (CombatBehaviors.Behavior<T>) currentBehaviorSeries.behaviors.get(currentBehaviorSeries.nextBehaviorPointer);
            if (nextBehavior.checkPredicates(this.mobpatch)) {
                currentBehaviorSeries.upCounter();
                return nextBehavior;
            } else {
                this.currentBehaviorPointer = -1;
                if (!currentBehaviorSeries.looping) {
                    currentBehaviorSeries.nextBehaviorPointer = 0;
                }
                return null;
            }
        }
    }

    public boolean hasActivatedMove() {
        return this.currentBehaviorPointer >= 0;
    }

    public void tick() {
        for (CombatBehaviors.BehaviorSeries<T> behaviorSeries : this.behaviorSeriesList) {
            behaviorSeries.tick();
        }
    }

    public static <T extends MobPatch<?>> CombatBehaviors.Builder<T> builder() {
        return new CombatBehaviors.Builder<>();
    }

    public static class Behavior<T extends MobPatch<?>> {

        private final Consumer<T> behavior;

        private final List<CombatBehaviors.BehaviorPredicate<T>> predicates;

        private Behavior(CombatBehaviors.Behavior.Builder<T> builder) {
            this.behavior = builder.behavior;
            this.predicates = builder.predicate;
        }

        private boolean checkPredicates(T mobpatch) {
            for (CombatBehaviors.BehaviorPredicate<T> predicate : this.predicates) {
                if (!predicate.test(mobpatch)) {
                    return false;
                }
            }
            return true;
        }

        public void execute(T mobpatch) {
            this.behavior.accept(mobpatch);
            mobpatch.updateEntityState();
        }

        public static <T extends MobPatch<?>> CombatBehaviors.Behavior.Builder<T> builder() {
            return new CombatBehaviors.Behavior.Builder<>();
        }

        public static class Builder<T extends MobPatch<?>> {

            private Consumer<T> behavior;

            private final List<CombatBehaviors.BehaviorPredicate<T>> predicate = Lists.newArrayList();

            private LivingEntityPatch.AnimationPacketProvider packetProvider = SPPlayAnimation::new;

            public CombatBehaviors.Behavior.Builder<T> behavior(Consumer<T> behavior) {
                this.behavior = behavior;
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> emptyBehavior() {
                this.behavior = mobpatch -> {
                };
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> animationBehavior(StaticAnimation motion) {
                this.behavior = mobpatch -> mobpatch.playAnimationSynchronized(motion, 0.0F, this.packetProvider);
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> withinEyeHeight() {
                this.predicate(new CombatBehaviors.TargetWithinEyeHeight<>());
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> randomChance(float chance) {
                this.predicate(new CombatBehaviors.RandomChance<>(chance));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> withinDistance(double minDistance, double maxDistance) {
                this.predicate(new CombatBehaviors.TargetWithinDistance<>(minDistance, maxDistance));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> withinAngle(double minDegree, double maxDegree) {
                this.predicate(new CombatBehaviors.TargetWithinAngle<>(minDegree, maxDegree));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> withinAngleHorizontal(double minDegree, double maxDegree) {
                this.predicate(new CombatBehaviors.TargetWithinAngle.Horizontal<>(minDegree, maxDegree));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> health(float health, CombatBehaviors.Health.Comparator comparator) {
                this.predicate(new CombatBehaviors.Health<>(health, comparator));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> custom(Function<T, Boolean> customPredicate) {
                this.predicate(new CombatBehaviors.CustomPredicate<>(customPredicate));
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> predicate(CombatBehaviors.BehaviorPredicate<T> predicate) {
                this.predicate.add(predicate);
                return this;
            }

            public CombatBehaviors.Behavior.Builder<T> packetProvider(LivingEntityPatch.AnimationPacketProvider packetProvider) {
                this.packetProvider = packetProvider;
                return this;
            }

            public CombatBehaviors.Behavior<T> build() {
                return new CombatBehaviors.Behavior<>(this);
            }
        }
    }

    public abstract static class BehaviorPredicate<T extends MobPatch<?>> {

        public abstract boolean test(T var1);
    }

    public static class BehaviorSeries<T extends MobPatch<?>> {

        private final List<CombatBehaviors.Behavior<T>> behaviors = Lists.newArrayList();

        private final boolean looping;

        private final boolean canBeInterrupted;

        private final float weight;

        private final int maxCooldown;

        private final List<Integer> cooldownSharingPointer;

        private int cooldown;

        private int nextBehaviorPointer;

        private boolean loopFinished;

        private BehaviorSeries(CombatBehaviors.BehaviorSeries.Builder<T> builder) {
            builder.behaviors.stream().map(motionBuilder -> motionBuilder.build()).forEach(this.behaviors::add);
            this.looping = builder.looping;
            this.canBeInterrupted = builder.canBeInterrupted;
            this.weight = builder.weight;
            this.cooldownSharingPointer = builder.cooldownSharingPointers;
            this.maxCooldown = builder.cooldown;
        }

        public boolean test(T mobpatch) {
            return this.cooldown > 0 ? false : ((CombatBehaviors.Behavior) this.behaviors.get(this.nextBehaviorPointer)).checkPredicates(mobpatch);
        }

        public void upCounter() {
            this.nextBehaviorPointer++;
            this.loopFinished = false;
            int behaviorsNum = this.behaviors.size();
            if (this.nextBehaviorPointer >= behaviorsNum) {
                this.nextBehaviorPointer %= behaviorsNum;
                this.loopFinished = true;
            }
        }

        public void tick() {
            this.cooldown--;
        }

        public void resetCooldown(CombatBehaviors<T> mobBehavior, boolean resetSharingCooldown) {
            this.cooldown = this.maxCooldown;
            if (resetSharingCooldown) {
                for (int i : this.cooldownSharingPointer) {
                    CombatBehaviors.BehaviorSeries<T> behaviorSeries = (CombatBehaviors.BehaviorSeries<T>) mobBehavior.behaviorSeriesList.get(i);
                    behaviorSeries.cooldown = behaviorSeries.maxCooldown;
                }
            }
        }

        public static <T extends MobPatch<?>> CombatBehaviors.BehaviorSeries.Builder<T> builder() {
            return new CombatBehaviors.BehaviorSeries.Builder<>();
        }

        public static class Builder<T extends MobPatch<?>> {

            private final List<CombatBehaviors.Behavior.Builder<T>> behaviors = Lists.newArrayList();

            private boolean looping = false;

            private boolean canBeInterrupted = true;

            private float weight;

            private int cooldown;

            private final List<Integer> cooldownSharingPointers = Lists.newArrayList();

            public CombatBehaviors.BehaviorSeries.Builder<T> weight(float weight) {
                this.weight = weight;
                return this;
            }

            public CombatBehaviors.BehaviorSeries.Builder<T> cooldown(int cooldown) {
                this.cooldown = cooldown;
                return this;
            }

            public CombatBehaviors.BehaviorSeries.Builder<T> simultaneousCooldown(int... cooldownSharingPointers) {
                for (int pointer : cooldownSharingPointers) {
                    this.cooldownSharingPointers.add(pointer);
                }
                return this;
            }

            public CombatBehaviors.BehaviorSeries.Builder<T> nextBehavior(CombatBehaviors.Behavior.Builder<T> motion) {
                this.behaviors.add(motion);
                return this;
            }

            public CombatBehaviors.BehaviorSeries.Builder<T> looping(boolean looping) {
                this.looping = looping;
                return this;
            }

            public CombatBehaviors.BehaviorSeries.Builder<T> canBeInterrupted(boolean canBeInterrupted) {
                this.canBeInterrupted = canBeInterrupted;
                return this;
            }

            public CombatBehaviors.BehaviorSeries<T> build() {
                return new CombatBehaviors.BehaviorSeries<>(this);
            }
        }
    }

    public static class Builder<T extends MobPatch<?>> {

        private final List<CombatBehaviors.BehaviorSeries.Builder<T>> behaviorSeriesList = Lists.newArrayList();

        public CombatBehaviors.Builder<T> newBehaviorSeries(CombatBehaviors.BehaviorSeries.Builder<T> builder) {
            this.behaviorSeriesList.add(builder);
            return this;
        }

        public CombatBehaviors<T> build(T mobpatch) {
            return new CombatBehaviors<>(this, mobpatch);
        }
    }

    public static class CustomPredicate<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        Function<T, Boolean> test;

        public CustomPredicate(Function<T, Boolean> test) {
            this.test = test;
        }

        @Override
        public boolean test(T mobpatch) {
            return (Boolean) this.test.apply(mobpatch);
        }
    }

    public static class Health<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final float value;

        private final CombatBehaviors.Health.Comparator comparator;

        public Health(float value, CombatBehaviors.Health.Comparator comparator) {
            this.value = value;
            this.comparator = comparator;
        }

        @Override
        public boolean test(T mobpatch) {
            switch(this.comparator) {
                case LESS_ABSOLUTE:
                    return this.value > mobpatch.getOriginal().m_21223_();
                case GREATER_ABSOLUTE:
                    return this.value < mobpatch.getOriginal().m_21223_();
                case LESS_RATIO:
                    return this.value > mobpatch.getOriginal().m_21223_() / mobpatch.getOriginal().m_21233_();
                case GREATER_RATIO:
                    return this.value < mobpatch.getOriginal().m_21223_() / mobpatch.getOriginal().m_21233_();
                default:
                    return true;
            }
        }

        public static enum Comparator {

            GREATER_ABSOLUTE, LESS_ABSOLUTE, GREATER_RATIO, LESS_RATIO
        }
    }

    public static class RandomChance<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final float chance;

        public RandomChance(float chance) {
            this.chance = chance;
        }

        @Override
        public boolean test(T mobpatch) {
            return mobpatch.getOriginal().m_217043_().nextFloat() < this.chance;
        }
    }

    public static class TargetWithinAngle<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        protected final double minDegree;

        protected final double maxDegree;

        public TargetWithinAngle(double minDegree, double maxDegree) {
            this.minDegree = minDegree;
            this.maxDegree = maxDegree;
        }

        @Override
        public boolean test(T mobpatch) {
            Entity target = mobpatch.getTarget();
            double degree = mobpatch.getAngleTo(target);
            return this.minDegree < degree && degree < this.maxDegree;
        }

        public static class Horizontal<T extends MobPatch<?>> extends CombatBehaviors.TargetWithinAngle<T> {

            public Horizontal(double minDegree, double maxDegree) {
                super(minDegree, maxDegree);
            }

            @Override
            public boolean test(T mobpatch) {
                Entity target = mobpatch.getTarget();
                double degree = mobpatch.getAngleToHorizontal(target);
                return this.minDegree < degree && degree < this.maxDegree;
            }
        }
    }

    public static class TargetWithinDistance<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final double minDistance;

        private final double maxDistance;

        public TargetWithinDistance(double minDistance, double maxDistance) {
            this.minDistance = minDistance * minDistance;
            this.maxDistance = maxDistance * maxDistance;
        }

        @Override
        public boolean test(T mobpatch) {
            double distanceSqr = mobpatch.getOriginal().m_20280_(mobpatch.getTarget());
            return this.minDistance < distanceSqr && distanceSqr < this.maxDistance;
        }
    }

    public static class TargetWithinEyeHeight<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        @Override
        public boolean test(T mobpatch) {
            double veticalDistance = Math.abs(mobpatch.getOriginal().m_20186_() - mobpatch.getTarget().m_20186_());
            return veticalDistance < (double) mobpatch.getOriginal().m_20192_();
        }
    }
}