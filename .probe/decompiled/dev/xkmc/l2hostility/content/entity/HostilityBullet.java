package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2hostility.init.registrate.LHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class HostilityBullet extends ShulkerBullet {

    private BulletType type;

    private int lv;

    public HostilityBullet(EntityType<HostilityBullet> type, Level level) {
        super(type, level);
    }

    public HostilityBullet(Level level, LivingEntity owner, Entity target, Direction.Axis direction, BulletType type, int lv) {
        this((EntityType<HostilityBullet>) LHEntities.BULLET.get(), level);
        this.m_5602_(owner);
        BlockPos blockpos = owner.m_20183_();
        double d0 = (double) blockpos.m_123341_() + 0.5;
        double d1 = (double) blockpos.m_123342_() + 0.5;
        double d2 = (double) blockpos.m_123343_() + 0.5;
        this.m_7678_(d0, d1, d2, this.m_146908_(), this.m_146909_());
        this.f_37312_ = target;
        this.f_37313_ = Direction.UP;
        this.m_37348_(direction);
        this.type = type;
        this.lv = lv;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.type != null) {
            Entity target = result.getEntity();
            Entity owner = this.m_19749_();
            LivingEntity leowner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
            float damage = this.type.getDamage(this.lv);
            if (damage > 0.0F) {
                target.hurt(this.m_269291_().mobProjectile(this, leowner), damage);
            }
            this.type.onHit(this, result, this.lv);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.type.onHit(this, result, this.lv);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("BulletType", this.type.name());
        tag.putInt("BulletLevel", this.lv);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.type = BulletType.valueOf(tag.getString("BulletType"));
        this.lv = tag.getInt("BulletLevel");
    }

    public boolean isTarget(Entity e) {
        if (e instanceof Player) {
            return true;
        } else if (e == this.f_37312_) {
            return true;
        } else {
            if (e instanceof Mob target) {
                Entity owner = this.m_19749_();
                if (owner != null) {
                    if (target.getTarget() == owner) {
                        return true;
                    }
                    if (owner instanceof Mob mob) {
                        return mob.getTarget() == e;
                    }
                }
            }
            return false;
        }
    }
}