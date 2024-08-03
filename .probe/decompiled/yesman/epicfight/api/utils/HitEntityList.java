package yesman.epicfight.api.utils;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class HitEntityList {

    private final List<Entity> hitEntites;

    private int index = -1;

    public HitEntityList(LivingEntityPatch<?> attacker, List<Entity> entities, HitEntityList.Priority priority) {
        this.hitEntites = priority.sort(attacker, entities);
    }

    public Entity getEntity() {
        return (Entity) this.hitEntites.get(this.index);
    }

    public boolean next() {
        this.index++;
        return this.hitEntites.size() > this.index;
    }

    public static enum Priority {

        DISTANCE((attacker, list) -> {
            List<Double> distanceToAttacker = Lists.newArrayList();
            List<Entity> hitEntites = Lists.newArrayList();
            label24: for (Entity entity : list) {
                double distance = ((LivingEntity) attacker.getOriginal()).m_20280_(entity);
                int index;
                for (index = 0; index < hitEntites.size(); index++) {
                    if (distance < (Double) distanceToAttacker.get(index)) {
                        hitEntites.add(index, entity);
                        distanceToAttacker.add(index, distance);
                        continue label24;
                    }
                }
                hitEntites.add(index, entity);
                distanceToAttacker.add(index, distance);
            }
            return hitEntites;
        }), TARGET((attacker, list) -> {
            List<Entity> hitEntites = Lists.newArrayList();
            for (Entity entity : list) {
                if (entity.is(attacker.getTarget())) {
                    hitEntites.add(entity);
                }
            }
            return hitEntites;
        }), HOSTILITY((attacker, list) -> {
            List<Entity> firstTargets = Lists.newArrayList();
            List<Entity> secondTargets = Lists.newArrayList();
            List<Entity> lastTargets = Lists.newArrayList();
            label54: for (Entity e : list) {
                if (!attacker.isTeammate(e)) {
                    if (((LivingEntity) attacker.getOriginal()).getLastHurtByMob() != e && attacker.getTarget() != e) {
                        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(e, LivingEntityPatch.class);
                        if (entitypatch != null && ((LivingEntity) attacker.getOriginal()).m_7306_(entitypatch.getTarget())) {
                            firstTargets.add(e);
                        } else {
                            if (e instanceof Mob mob) {
                                if (((LivingEntity) attacker.getOriginal()).m_7306_(mob.getTarget())) {
                                    firstTargets.add(mob);
                                    continue;
                                }
                                GoalSelector targetingAi = mob.targetSelector;
                                for (WrappedGoal goal : targetingAi.getAvailableGoals()) {
                                    if (goal.getGoal() instanceof NearestAttackableTargetGoal<?> targetGoal && targetGoal.targetConditions.test(mob, (LivingEntity) attacker.getOriginal())) {
                                        secondTargets.add(mob);
                                        continue label54;
                                    }
                                }
                            }
                            lastTargets.add(e);
                        }
                    } else {
                        firstTargets.add(e);
                    }
                }
            }
            secondTargets.addAll(lastTargets);
            firstTargets.addAll(secondTargets);
            return firstTargets;
        });

        BiFunction<LivingEntityPatch<?>, List<Entity>, List<Entity>> sortingFunction;

        private Priority(BiFunction<LivingEntityPatch<?>, List<Entity>, List<Entity>> sortingFunction) {
            this.sortingFunction = sortingFunction;
        }

        public List<Entity> sort(LivingEntityPatch<?> attacker, List<Entity> entities) {
            return (List<Entity>) this.sortingFunction.apply(attacker, entities);
        }
    }
}