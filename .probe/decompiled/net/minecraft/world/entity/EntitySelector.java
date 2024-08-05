package net.minecraft.world.entity;

import com.google.common.base.Predicates;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;

public final class EntitySelector {

    public static final Predicate<Entity> ENTITY_STILL_ALIVE = Entity::m_6084_;

    public static final Predicate<Entity> LIVING_ENTITY_STILL_ALIVE = p_20442_ -> p_20442_.isAlive() && p_20442_ instanceof LivingEntity;

    public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN = p_20440_ -> p_20440_.isAlive() && !p_20440_.isVehicle() && !p_20440_.isPassenger();

    public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR = p_20438_ -> p_20438_ instanceof Container && p_20438_.isAlive();

    public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR = p_20436_ -> !(p_20436_ instanceof Player) || !p_20436_.isSpectator() && !((Player) p_20436_).isCreative();

    public static final Predicate<Entity> NO_SPECTATORS = p_20434_ -> !p_20434_.isSpectator();

    public static final Predicate<Entity> CAN_BE_COLLIDED_WITH = NO_SPECTATORS.and(Entity::m_5829_);

    private EntitySelector() {
    }

    public static Predicate<Entity> withinDistance(double double0, double double1, double double2, double double3) {
        double $$4 = double3 * double3;
        return p_20420_ -> p_20420_ != null && p_20420_.distanceToSqr(double0, double1, double2) <= $$4;
    }

    public static Predicate<Entity> pushableBy(Entity entity0) {
        Team $$1 = entity0.getTeam();
        Team.CollisionRule $$2 = $$1 == null ? Team.CollisionRule.ALWAYS : $$1.getCollisionRule();
        return (Predicate<Entity>) ($$2 == Team.CollisionRule.NEVER ? Predicates.alwaysFalse() : NO_SPECTATORS.and(p_20430_ -> {
            if (!p_20430_.isPushable()) {
                return false;
            } else if (!entity0.level().isClientSide || p_20430_ instanceof Player && ((Player) p_20430_).isLocalPlayer()) {
                Team $$4 = p_20430_.getTeam();
                Team.CollisionRule $$5 = $$4 == null ? Team.CollisionRule.ALWAYS : $$4.getCollisionRule();
                if ($$5 == Team.CollisionRule.NEVER) {
                    return false;
                } else {
                    boolean $$6 = $$1 != null && $$1.isAlliedTo($$4);
                    return ($$2 == Team.CollisionRule.PUSH_OWN_TEAM || $$5 == Team.CollisionRule.PUSH_OWN_TEAM) && $$6 ? false : $$2 != Team.CollisionRule.PUSH_OTHER_TEAMS && $$5 != Team.CollisionRule.PUSH_OTHER_TEAMS || $$6;
                }
            } else {
                return false;
            }
        }));
    }

    public static Predicate<Entity> notRiding(Entity entity0) {
        return p_20425_ -> {
            while (p_20425_.isPassenger()) {
                p_20425_ = p_20425_.getVehicle();
                if (p_20425_ == entity0) {
                    return false;
                }
            }
            return true;
        };
    }

    public static class MobCanWearArmorEntitySelector implements Predicate<Entity> {

        private final ItemStack itemStack;

        public MobCanWearArmorEntitySelector(ItemStack itemStack0) {
            this.itemStack = itemStack0;
        }

        public boolean test(@Nullable Entity entity0) {
            if (!entity0.isAlive()) {
                return false;
            } else {
                return !(entity0 instanceof LivingEntity $$1) ? false : $$1.canTakeItem(this.itemStack);
            }
        }
    }
}