package com.mna.api.spells.targeting;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellTargetHelper {

    @Nullable
    public static EntityHitResult rayTraceEntities(Level world, Entity caster, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
        return rayTraceEntities(world, caster, startVec, endVec, boundingBox, filter, Double.MAX_VALUE);
    }

    @Nullable
    public static EntityHitResult rayTraceEntities(Level worldIn, @Nullable Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double max_distance) {
        double cur_distance = max_distance;
        Entity target_candidate = null;
        Vec3 target_pos = null;
        for (Entity entity : worldIn.getEntities(projectile, boundingBox, filter)) {
            AABB entityBoundingBox = entity.getBoundingBox().inflate(0.3F);
            Optional<Vec3> optional = entityBoundingBox.clip(startVec, endVec);
            if (optional.isPresent()) {
                double entityDistance = startVec.distanceToSqr((Vec3) optional.get());
                if (entityDistance < cur_distance) {
                    target_candidate = entity;
                    cur_distance = entityDistance;
                    target_pos = (Vec3) optional.get();
                }
            }
        }
        return target_candidate == null ? null : new EntityHitResult(target_candidate, target_pos);
    }

    @Nullable
    public static HitResult rayTrace(@Nullable Entity caster, Level world, Vec3 casterPosition, Vec3 casterLook, boolean checkEntityCollision, boolean includeCaster, ClipContext.Block blockModeIn, Predicate<Entity> filter, AABB boundingBox, double range) {
        Vec3 look_projected = casterPosition.add(casterLook.multiply(range, range, range));
        HitResult raytraceresult = world.m_45547_(new ClipContext(casterPosition, look_projected, blockModeIn, ClipContext.Fluid.NONE, caster));
        if (checkEntityCollision) {
            if (raytraceresult.getType() != HitResult.Type.MISS) {
                look_projected = raytraceresult.getLocation();
            }
            HitResult entity_trace_result = rayTraceEntities(world, caster, casterPosition, look_projected, boundingBox, filter);
            if (entity_trace_result != null) {
                raytraceresult = entity_trace_result;
            }
        }
        return raytraceresult;
    }
}