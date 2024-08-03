package org.violetmoon.zeta.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public abstract class RaytracingUtil {

    public abstract double getEntityRange(LivingEntity var1);

    public HitResult rayTrace(Entity entity, Level world, Player player, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        return this.rayTrace(entity, world, player, blockMode, fluidMode, this.getEntityRange(player));
    }

    public HitResult rayTrace(Entity entity, Level world, Entity player, ClipContext.Block blockMode, ClipContext.Fluid fluidMode, double range) {
        Pair<Vec3, Vec3> params = this.getEntityParams(player);
        return this.rayTrace(entity, world, (Vec3) params.getLeft(), (Vec3) params.getRight(), blockMode, fluidMode, range);
    }

    public HitResult rayTrace(Entity entity, Level world, Vec3 startPos, Vec3 ray, ClipContext.Block blockMode, ClipContext.Fluid fluidMode, double range) {
        return this.rayTrace(entity, world, startPos, ray.scale(range), blockMode, fluidMode);
    }

    public HitResult rayTrace(Entity entity, Level world, Vec3 startPos, Vec3 ray, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        Vec3 end = startPos.add(ray);
        ClipContext context = new ClipContext(startPos, end, blockMode, fluidMode, entity);
        return world.m_45547_(context);
    }

    public Pair<Vec3, Vec3> getEntityParams(Entity player) {
        float scale = 1.0F;
        float pitch = player.xRotO + (player.getXRot() - player.xRotO) * scale;
        float yaw = player.yRotO + (player.getYRot() - player.yRotO) * scale;
        Vec3 pos = player.position();
        double posX = player.xo + (pos.x - player.xo) * (double) scale;
        double posY = player.yo + (pos.y - player.yo) * (double) scale;
        if (player instanceof Player) {
            posY += (double) player.getEyeHeight();
        }
        double posZ = player.zo + (pos.z - player.zo) * (double) scale;
        Vec3 rayPos = new Vec3(posX, posY, posZ);
        float zYaw = -Mth.cos(yaw * (float) Math.PI / 180.0F);
        float xYaw = Mth.sin(yaw * (float) Math.PI / 180.0F);
        float pitchMod = -Mth.cos(pitch * (float) Math.PI / 180.0F);
        float azimuth = -Mth.sin(pitch * (float) Math.PI / 180.0F);
        float xLen = xYaw * pitchMod;
        float yLen = zYaw * pitchMod;
        Vec3 ray = new Vec3((double) xLen, (double) azimuth, (double) yLen);
        return Pair.of(rayPos, ray);
    }
}