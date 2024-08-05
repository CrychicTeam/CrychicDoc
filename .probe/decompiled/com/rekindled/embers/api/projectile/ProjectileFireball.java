package com.rekindled.embers.api.projectile;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.entity.EmberProjectileEntity;
import java.awt.Color;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ProjectileFireball implements IProjectilePreset {

    Vec3 pos;

    Vec3 velocity;

    IProjectileEffect effect;

    double size;

    int lifetime;

    Entity shooter;

    EmberProjectileEntity entity;

    Color color = new Color(255, 64, 16);

    double gravity;

    int homingTime;

    double homingRange;

    int homingIndex;

    int homingModulo;

    Predicate<Entity> homingPredicate;

    public ProjectileFireball(Entity shooter, Vec3 pos, Vec3 velocity, double size, int lifetime, IProjectileEffect effect) {
        this.pos = pos;
        this.velocity = velocity;
        this.effect = effect;
        this.size = size;
        this.lifetime = lifetime;
        this.shooter = shooter;
    }

    public double getSize() {
        return this.size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
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

    public double getGravity() {
        return this.gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @Override
    public IProjectileEffect getEffect() {
        return this.effect;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Nullable
    @Override
    public Entity getShooter() {
        return this.shooter;
    }

    @Override
    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public void setHoming(int time, double range, int index, int modulo, Predicate<Entity> predicate) {
        this.homingTime = time;
        this.homingRange = range;
        this.homingIndex = index;
        this.homingModulo = modulo;
        this.homingPredicate = predicate;
    }

    @Override
    public void shoot(Level world) {
        this.entity = new EmberProjectileEntity(RegistryManager.EMBER_PROJECTILE.get(), world);
        this.entity.shootFromRotation(this.shooter, (float) this.velocity.x, (float) this.velocity.y, (float) this.velocity.z, (float) this.velocity.length(), 0.0F, this.size);
        this.entity.m_146884_(this.pos);
        this.entity.setGravity(this.gravity);
        this.entity.setEffect(this.effect);
        this.entity.setPreset(this);
        this.entity.setLifetime(this.lifetime);
        this.entity.setColor(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha());
        this.entity.setHoming(this.homingTime, this.homingRange, this.homingIndex, this.homingModulo, this.homingPredicate);
        world.m_7967_(this.entity);
    }
}