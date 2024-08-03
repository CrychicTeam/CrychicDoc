package com.rekindled.embers.api.projectile;

import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageEmberRayFX;
import com.rekindled.embers.util.Misc;
import java.awt.Color;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class ProjectileRay implements IProjectilePreset {

    Vec3 pos;

    Vec3 velocity;

    IProjectileEffect effect;

    Entity shooter;

    boolean pierceEntities;

    Color color = new Color(255, 64, 16);

    public ProjectileRay(Entity shooter, Vec3 start, Vec3 end, boolean pierceEntities, IProjectileEffect effect) {
        this.pos = start;
        this.velocity = end.subtract(start);
        this.effect = effect;
        this.shooter = shooter;
        this.pierceEntities = pierceEntities;
    }

    @Override
    public Vec3 getPos() {
        return this.pos;
    }

    @Override
    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Vec3 getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public IProjectileEffect getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public boolean canPierceEntities() {
        return this.pierceEntities;
    }

    public void setPierceEntities(boolean pierceEntities) {
        this.pierceEntities = pierceEntities;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return null;
    }

    @Nullable
    @Override
    public Entity getShooter() {
        return this.shooter;
    }

    @Override
    public void shoot(Level world) {
        double startX = this.getPos().x;
        double startY = this.getPos().y;
        double startZ = this.getPos().z;
        double dX = this.getVelocity().x;
        double dY = this.getVelocity().y;
        double dZ = this.getVelocity().z;
        double impactDist = Double.POSITIVE_INFINITY;
        boolean doContinue = true;
        Vec3 currPosVec = this.getPos();
        Vec3 newPosVector = this.getPos().add(this.getVelocity());
        BlockHitResult blockTrace = world.m_45547_(new ClipContext(currPosVec, newPosVector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.shooter));
        List<EntityHitResult> entityTraces = Misc.getEntityHitResults(world, null, this.shooter, currPosVec, newPosVector, new AABB(startX - 0.3, startY - 0.3, startZ - 0.3, startX + 0.3, startY + 0.3, startZ + 0.3), EntitySelector.NO_SPECTATORS, 0.3F);
        double distBlock = blockTrace != null ? this.getPos().distanceToSqr(blockTrace.m_82450_()) : Double.POSITIVE_INFINITY;
        for (HitResult entityTraceFirst : entityTraces) {
            if (doContinue && entityTraceFirst != null && this.getPos().distanceToSqr(entityTraceFirst.getLocation()) < distBlock) {
                this.effect.onHit(world, entityTraceFirst, this);
                if (!this.pierceEntities) {
                    impactDist = this.getPos().distanceTo(entityTraceFirst.getLocation());
                    doContinue = false;
                }
            }
        }
        if (doContinue && blockTrace != null) {
            this.effect.onHit(world, blockTrace, this);
            impactDist = this.getPos().distanceTo(blockTrace.m_82450_());
            doContinue = false;
        }
        if (doContinue) {
            this.effect.onFizzle(world, newPosVector, this);
            impactDist = this.getPos().distanceTo(newPosVector);
        }
        if (!world.isClientSide) {
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.shooter), new MessageEmberRayFX(startX, startY, startZ, dX, dY, dZ, impactDist, Misc.intColor(this.color.getRed(), this.color.getGreen(), this.color.getBlue())));
        }
    }
}