package com.mna.api.entities.possession;

import com.mna.api.spells.targeting.SpellTargetHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PossessionActions {

    private static HashMap<EntityType<?>, PossessionActions.PosessionActionCollection<?>> _registeredActions = new HashMap();

    public static <T extends Mob> void RegisterLeftClickAction(EntityType<T> entityType, Consumer<T> callback) {
        PossessionActions.PosessionActionCollection<T> registry = (PossessionActions.PosessionActionCollection<T>) _registeredActions.getOrDefault(entityType, new PossessionActions.PosessionActionCollection());
        registry.AddAction(callback);
        _registeredActions.put(entityType, registry);
    }

    public static <T extends Mob> boolean Invoke(T reference) {
        PossessionActions.PosessionActionCollection<T> registry = (PossessionActions.PosessionActionCollection<T>) _registeredActions.getOrDefault(reference.m_6095_(), null);
        if (registry == null) {
            return false;
        } else {
            registry.Invoke(reference);
            return true;
        }
    }

    public static <T extends Mob> boolean InvokeDefault(T reference) {
        boolean isSkeleton = reference instanceof AbstractSkeleton;
        boolean isCrossbowUser = reference instanceof CrossbowAttackMob;
        if (isSkeleton || isCrossbowUser) {
            ItemStack itemstack = reference.m_6298_(reference.m_21120_(ProjectileUtil.getWeaponHoldingHand(reference, item -> item == Items.BOW)));
            AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(reference, itemstack, 1.0F);
            if (reference.m_21205_().getItem() instanceof BowItem) {
                abstractarrowentity = ((BowItem) reference.m_21205_().getItem()).customArrow(abstractarrowentity);
            }
            Vec3 fwd = Vec3.directionFromRotation(reference.m_20155_());
            double d0 = fwd.x;
            double d1 = fwd.y;
            double d2 = fwd.z;
            double d3 = Mth.square(d0 * d0 + d2 * d2);
            abstractarrowentity.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - reference.m_9236_().m_46791_().getId() * 4));
            reference.m_5496_(isCrossbowUser ? SoundEvents.CROSSBOW_SHOOT : SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (reference.m_217043_().nextFloat() * 0.4F + 0.8F));
            reference.m_9236_().m_7967_(abstractarrowentity);
            return true;
        } else if (reference instanceof RangedAttackMob) {
            ((RangedAttackMob) reference).performRangedAttack(reference, 1.0F);
            return true;
        } else {
            float range = 8.0F;
            double damage = 1.0;
            if (reference.m_21204_().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                damage = Math.max(reference.m_21051_(Attributes.ATTACK_DAMAGE).getValue(), 1.0);
            }
            HitResult targetResult = SpellTargetHelper.rayTrace(null, reference.m_9236_(), reference.m_20182_().add(0.0, (double) reference.m_20192_(), 0.0), Vec3.directionFromRotation(reference.m_20155_()), true, false, ClipContext.Block.COLLIDER, entity -> entity.isPickable() && entity.isAlive() && entity != reference, reference.m_20191_().inflate((double) range, (double) range, (double) range), (double) range);
            reference.m_21011_(InteractionHand.MAIN_HAND, true);
            if (targetResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult ertr = (EntityHitResult) targetResult;
                ertr.getEntity().hurt(reference.m_269291_().mobAttack(reference), (float) damage);
                return true;
            } else {
                return false;
            }
        }
    }

    static class PosessionActionCollection<T extends Entity> {

        ArrayList<Consumer<T>> actions = new ArrayList();

        public PosessionActionCollection() {
        }

        public void AddAction(Consumer<T> callback) {
            this.actions.add(callback);
        }

        public void Invoke(T e) {
            this.actions.forEach(a -> a.accept(e));
        }
    }
}