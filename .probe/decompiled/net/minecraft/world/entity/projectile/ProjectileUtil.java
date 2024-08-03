package net.minecraft.world.entity.projectile;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class ProjectileUtil {

    public static HitResult getHitResultOnMoveVector(Entity entity0, Predicate<Entity> predicateEntity1) {
        Vec3 $$2 = entity0.getDeltaMovement();
        Level $$3 = entity0.level();
        Vec3 $$4 = entity0.position();
        return getHitResult($$4, entity0, predicateEntity1, $$2, $$3);
    }

    public static HitResult getHitResultOnViewVector(Entity entity0, Predicate<Entity> predicateEntity1, double double2) {
        Vec3 $$3 = entity0.getViewVector(0.0F).scale(double2);
        Level $$4 = entity0.level();
        Vec3 $$5 = entity0.getEyePosition();
        return getHitResult($$5, entity0, predicateEntity1, $$3, $$4);
    }

    private static HitResult getHitResult(Vec3 vec0, Entity entity1, Predicate<Entity> predicateEntity2, Vec3 vec3, Level level4) {
        Vec3 $$5 = vec0.add(vec3);
        HitResult $$6 = level4.m_45547_(new ClipContext(vec0, $$5, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity1));
        if ($$6.getType() != HitResult.Type.MISS) {
            $$5 = $$6.getLocation();
        }
        HitResult $$7 = getEntityHitResult(level4, entity1, vec0, $$5, entity1.getBoundingBox().expandTowards(vec3).inflate(1.0), predicateEntity2);
        if ($$7 != null) {
            $$6 = $$7;
        }
        return $$6;
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Entity entity0, Vec3 vec1, Vec3 vec2, AABB aABB3, Predicate<Entity> predicateEntity4, double double5) {
        Level $$6 = entity0.level();
        double $$7 = double5;
        Entity $$8 = null;
        Vec3 $$9 = null;
        for (Entity $$10 : $$6.getEntities(entity0, aABB3, predicateEntity4)) {
            AABB $$11 = $$10.getBoundingBox().inflate((double) $$10.getPickRadius());
            Optional<Vec3> $$12 = $$11.clip(vec1, vec2);
            if ($$11.contains(vec1)) {
                if ($$7 >= 0.0) {
                    $$8 = $$10;
                    $$9 = (Vec3) $$12.orElse(vec1);
                    $$7 = 0.0;
                }
            } else if ($$12.isPresent()) {
                Vec3 $$13 = (Vec3) $$12.get();
                double $$14 = vec1.distanceToSqr($$13);
                if ($$14 < $$7 || $$7 == 0.0) {
                    if ($$10.getRootVehicle() == entity0.getRootVehicle()) {
                        if ($$7 == 0.0) {
                            $$8 = $$10;
                            $$9 = $$13;
                        }
                    } else {
                        $$8 = $$10;
                        $$9 = $$13;
                        $$7 = $$14;
                    }
                }
            }
        }
        return $$8 == null ? null : new EntityHitResult($$8, $$9);
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Level level0, Entity entity1, Vec3 vec2, Vec3 vec3, AABB aABB4, Predicate<Entity> predicateEntity5) {
        return getEntityHitResult(level0, entity1, vec2, vec3, aABB4, predicateEntity5, 0.3F);
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Level level0, Entity entity1, Vec3 vec2, Vec3 vec3, AABB aABB4, Predicate<Entity> predicateEntity5, float float6) {
        double $$7 = Double.MAX_VALUE;
        Entity $$8 = null;
        for (Entity $$9 : level0.getEntities(entity1, aABB4, predicateEntity5)) {
            AABB $$10 = $$9.getBoundingBox().inflate((double) float6);
            Optional<Vec3> $$11 = $$10.clip(vec2, vec3);
            if ($$11.isPresent()) {
                double $$12 = vec2.distanceToSqr((Vec3) $$11.get());
                if ($$12 < $$7) {
                    $$8 = $$9;
                    $$7 = $$12;
                }
            }
        }
        return $$8 == null ? null : new EntityHitResult($$8);
    }

    public static void rotateTowardsMovement(Entity entity0, float float1) {
        Vec3 $$2 = entity0.getDeltaMovement();
        if ($$2.lengthSqr() != 0.0) {
            double $$3 = $$2.horizontalDistance();
            entity0.setYRot((float) (Mth.atan2($$2.z, $$2.x) * 180.0F / (float) Math.PI) + 90.0F);
            entity0.setXRot((float) (Mth.atan2($$3, $$2.y) * 180.0F / (float) Math.PI) - 90.0F);
            while (entity0.getXRot() - entity0.xRotO < -180.0F) {
                entity0.xRotO -= 360.0F;
            }
            while (entity0.getXRot() - entity0.xRotO >= 180.0F) {
                entity0.xRotO += 360.0F;
            }
            while (entity0.getYRot() - entity0.yRotO < -180.0F) {
                entity0.yRotO -= 360.0F;
            }
            while (entity0.getYRot() - entity0.yRotO >= 180.0F) {
                entity0.yRotO += 360.0F;
            }
            entity0.setXRot(Mth.lerp(float1, entity0.xRotO, entity0.getXRot()));
            entity0.setYRot(Mth.lerp(float1, entity0.yRotO, entity0.getYRot()));
        }
    }

    public static InteractionHand getWeaponHoldingHand(LivingEntity livingEntity0, Item item1) {
        return livingEntity0.getMainHandItem().is(item1) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static AbstractArrow getMobArrow(LivingEntity livingEntity0, ItemStack itemStack1, float float2) {
        ArrowItem $$3 = (ArrowItem) (itemStack1.getItem() instanceof ArrowItem ? itemStack1.getItem() : Items.ARROW);
        AbstractArrow $$4 = $$3.createArrow(livingEntity0.m_9236_(), itemStack1, livingEntity0);
        $$4.setEnchantmentEffectsFromEntity(livingEntity0, float2);
        if (itemStack1.is(Items.TIPPED_ARROW) && $$4 instanceof Arrow) {
            ((Arrow) $$4).setEffectsFromItem(itemStack1);
        }
        return $$4;
    }
}