package net.mehvahdjukaar.moonlight.api.entity;

import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ImprovedProjectileEntity extends ThrowableItemProjectile {

    protected boolean isInBlock = false;

    protected int inBlockTime = 0;

    protected int maxAge = 300;

    protected int maxInBlockTime = 20;

    @Deprecated(forRemoval = true)
    public boolean touchedGround;

    @Deprecated(forRemoval = true)
    public int groundTime = 0;

    protected ImprovedProjectileEntity(EntityType<? extends ThrowableItemProjectile> type, Level world) {
        super(type, world);
        this.m_274367_(0.0F);
    }

    protected ImprovedProjectileEntity(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level world) {
        this(type, world);
        this.m_6034_(x, y, z);
    }

    protected ImprovedProjectileEntity(EntityType<? extends ThrowableItemProjectile> type, LivingEntity thrower, Level world) {
        this(type, thrower.m_20185_(), thrower.m_20188_() - 0.1F, thrower.m_20189_(), world);
        this.m_5602_(thrower);
    }

    @Override
    public float maxUpStep() {
        return super.m_274421_();
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    public boolean collidesWithBlocks() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.f_150164_) {
            this.m_146852_(GameEvent.PROJECTILE_SHOOT, this.m_19749_());
            this.f_150164_ = true;
        }
        this.m_6075_();
        this.f_146810_ = this.m_6060_();
        Level level = this.m_9236_();
        Vec3 pos = this.m_20182_();
        BlockPos blockpos = this.m_20183_();
        Vec3 movement = this.m_20184_();
        boolean client = level.isClientSide;
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.m_60795_()) {
            VoxelShape voxelshape = blockstate.m_60742_(level, blockpos, CollisionContext.of(this));
            if (!voxelshape.isEmpty()) {
                Vec3 centerPos = this.m_146892_();
                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(centerPos)) {
                        this.isInBlock = true;
                        break;
                    }
                }
            }
        }
        if (this.m_20070_() || blockstate.m_60713_(Blocks.POWDER_SNOW)) {
            this.m_20095_();
        }
        if (this.isInBlock && !this.f_19794_) {
            this.inBlockTime++;
        } else {
            this.inBlockTime = 0;
            this.m_37283_();
            HitResult hitResult;
            if (this.getColliderType() == ImprovedProjectileEntity.ColliderType.RAY) {
                hitResult = level.m_45547_(new ClipContext(pos, pos.add(movement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            } else {
                hitResult = MthUtils.collideWithSweptAABB(this, movement, 2.0);
            }
            Vec3 newPos = hitResult.getLocation();
            this.m_6034_(newPos.x, newPos.y, newPos.z);
            float deceleration = this.m_20069_() ? this.getWaterInertia() : this.getInertia();
            if (client) {
                this.spawnTrailParticles();
            }
            this.m_20256_(this.m_20184_().scale((double) deceleration));
            if (!this.m_20068_() && !this.f_19794_) {
                this.m_20256_(this.m_20184_().subtract(0.0, (double) this.m_7139_(), 0.0));
            }
            this.m_20101_();
            if (this.hasReachedEndOfLife() && !this.m_213877_()) {
                this.reachedEndOfLife();
            }
            if (!this.m_213877_()) {
                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, this, pos, newPos, this.m_20191_().expandTowards(newPos.subtract(pos)).inflate(1.0), x$0 -> this.m_5603_(x$0));
                if (entityHitResult != null) {
                    hitResult = entityHitResult;
                }
                if (hitResult.getType() != HitResult.Type.MISS) {
                    boolean portalHit = false;
                    if (hitResult instanceof EntityHitResult ei) {
                        Entity hitEntity = ei.getEntity();
                        if (hitEntity == this.m_19749_()) {
                            if (!this.canHarmOwner()) {
                                hitResult = null;
                            }
                        } else if (hitEntity instanceof Player p1 && this.m_19749_() instanceof Player p2 && !p2.canHarmPlayer(p1)) {
                            hitResult = null;
                        }
                    } else if (hitResult instanceof BlockHitResult bi) {
                        BlockPos hitPos = bi.getBlockPos();
                        BlockState hitState = level.getBlockState(hitPos);
                        if (hitState.m_60713_(Blocks.NETHER_PORTAL)) {
                            this.m_20221_(hitPos);
                            portalHit = true;
                        } else if (hitState.m_60713_(Blocks.END_GATEWAY)) {
                            if (level.getBlockEntity(hitPos) instanceof TheEndGatewayBlockEntity tile && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                                TheEndGatewayBlockEntity.teleportEntity(level, hitPos, hitState, this, tile);
                            }
                            portalHit = true;
                        }
                    }
                    if (!portalHit && hitResult != null && hitResult.getType() != HitResult.Type.MISS && !this.f_19794_ && !ForgeHelper.onProjectileImpact(this, hitResult)) {
                        this.m_6532_(hitResult);
                    }
                }
            }
        }
    }

    public boolean canHarmOwner() {
        return this.m_19749_() instanceof Player ? this.m_9236_().m_46791_().getId() >= 1 : false;
    }

    protected float getInertia() {
        return 0.99F;
    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    public boolean hasReachedEndOfLife() {
        return this.f_19797_ > this.maxAge || this.inBlockTime > this.maxInBlockTime;
    }

    public void reachedEndOfLife() {
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @Deprecated(forRemoval = true)
    public void spawnTrailParticles(Vec3 oldPos, Vec3 newPos) {
    }

    public void spawnTrailParticles() {
        this.spawnTrailParticles(new Vec3(this.f_19854_, this.f_19855_, this.f_19856_), this.m_20182_());
        if (this.m_20069_()) {
            Vec3 movement = this.m_20184_();
            double velX = movement.x;
            double velY = movement.y;
            double velZ = movement.z;
            for (int j = 0; j < 4; j++) {
                double pY = this.m_20188_();
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() - velX * 0.25, pY - velY * 0.25, this.m_20189_() - velZ * 0.25, velX, velY, velZ);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("inBlock", this.isInBlock);
        tag.putInt("inBlockTime", this.inBlockTime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isInBlock = tag.getBoolean("inBlock");
        this.inBlockTime = tag.getInt("inBlockTime");
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        super.m_37251_(shooter, x, y, z, velocity, inaccuracy);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.m_6686_(x, y, z, velocity, inaccuracy);
    }

    public float getDefaultShootVelocity() {
        return 1.5F;
    }

    @Deprecated(forRemoval = true)
    public void setNoPhysics(boolean noGravity) {
        super.m_20242_(noGravity);
    }

    @Deprecated(forRemoval = true)
    public boolean isNoPhysics() {
        return super.m_20068_();
    }

    @Deprecated(forRemoval = true)
    protected float getDeceleration() {
        return 0.99F;
    }

    @Deprecated(forRemoval = true)
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 oPos, Vec3 pos) {
        return ProjectileUtil.getEntityHitResult(this.m_9236_(), this, oPos, pos, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), x$0 -> this.m_5603_(x$0));
    }

    protected ImprovedProjectileEntity.ColliderType getColliderType() {
        return ImprovedProjectileEntity.ColliderType.AABB;
    }

    protected static enum ColliderType {

        RAY, AABB
    }
}