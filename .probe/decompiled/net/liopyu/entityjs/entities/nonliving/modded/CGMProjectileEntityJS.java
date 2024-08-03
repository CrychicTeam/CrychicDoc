package net.liopyu.entityjs.entities.nonliving.modded;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.entity.MissileEntity;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.interfaces.IExplosionDamageable;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.modded.CGMProjectileEntityJSBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CGMProjectileEntityJS extends MissileEntity implements IAnimatableJSNL {

    private final AnimatableInstanceCache getAnimatableInstanceCache;

    public CGMProjectileEntityJSBuilder builder;

    public CGMProjectileEntityJS(CGMProjectileEntityJSBuilder builder, EntityType<? extends MissileEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.builder = builder;
        this.getAnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    }

    public CGMProjectileEntityJS(CGMProjectileEntityJSBuilder builder, EntityType<? extends MissileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
        this.builder = builder;
        this.getAnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public BaseEntityBuilder<?> getBuilder() {
        return this.builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.getAnimatableInstanceCache;
    }

    public String entityName() {
        return this.m_6095_().toString();
    }

    protected void onProjectileTick() {
        if (this.builder.onProjectileTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onProjectileTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onProjectileTick.");
        } else {
            super.onProjectileTick();
        }
    }

    public static void createExplosion(Entity entity, float radius, boolean breakTerrain) {
        Level world = entity.level();
        if (!world.isClientSide()) {
            DamageSource source = entity instanceof ProjectileEntity projectile ? entity.damageSources().explosion(entity, projectile.getShooter()) : null;
            Explosion.BlockInteraction mode = breakTerrain ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
            Explosion explosion = new ProjectileExplosion(world, entity, source, null, entity.getX(), entity.getY(), entity.getZ(), radius, false, mode);
            if (!ForgeEventFactory.onExplosionStart(world, explosion)) {
                explosion.explode();
                explosion.finalizeExplosion(true);
                explosion.getToBlow().forEach(pos -> {
                    if (world.getBlockState(pos).m_60734_() instanceof IExplosionDamageable) {
                        ((IExplosionDamageable) world.getBlockState(pos).m_60734_()).onProjectileExploded(world, world.getBlockState(pos), pos, entity);
                    }
                });
                if (!explosion.interactsWithBlocks()) {
                    explosion.clearToBlow();
                }
                for (ServerPlayer player : ((ServerLevel) world).players()) {
                    if (player.m_20275_(entity.getX(), entity.getY(), entity.getZ()) < 4096.0) {
                        player.connection.send(new ClientboundExplodePacket(entity.getX(), entity.getY(), entity.getZ(), radius, explosion.getToBlow(), (Vec3) explosion.getHitPlayers().get(player)));
                    }
                }
            }
        }
    }

    public void onExpired() {
        if (this.builder.explosionEnabled) {
            createExplosion(this, 5.0F, true);
        }
    }

    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        if (this.builder.explosionEnabled) {
            createExplosion(this, 5.0F, true);
        }
        if (this.builder.onHitEntity != null) {
            CGMProjectileEntityJS.ShotContext context = new CGMProjectileEntityJS.ShotContext(this, entity, hitVec, startVec, endVec, headshot);
            EntityJSHelperClass.consumerCallback(this.builder.onHitEntity, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitBlock.");
        }
    }

    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        if (this.builder.explosionEnabled) {
            createExplosion(this, 5.0F, true);
        }
        if (this.builder.onHitBlock != null) {
            CGMProjectileEntityJS.HitBlockContext context = new CGMProjectileEntityJS.HitBlockContext(this, state, pos, face, x, y, z);
            EntityJSHelperClass.consumerCallback(this.builder.onHitBlock, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitBlock.");
        }
    }

    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = new Vec3(pX, pY, pZ).normalize().add(this.f_19796_.triangle(0.0, 0.0172275 * (double) pInaccuracy), this.f_19796_.triangle(0.0, 0.0172275 * (double) pInaccuracy), this.f_19796_.triangle(0.0, 0.0172275 * (double) pInaccuracy)).scale((double) pVelocity);
        this.m_20256_(vec3);
        double d0 = vec3.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vec3.y, d0) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((pX + pZ) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        this.shoot((double) f, (double) f1, (double) f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(vec3.x, pShooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.builder != null && this.builder.onHurt != null) {
            ContextUtils.EntityHurtContext context = new ContextUtils.EntityHurtContext(this, pSource, pAmount);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        return super.m_6469_(pSource, pAmount);
    }

    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.m_6453_(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        if (this.builder != null && this.builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(x, y, z, yaw, pitch, posRotationIncrements, teleport, this);
            EntityJSHelperClass.consumerCallback(this.builder.lerpTo, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: lerpTo.");
        }
    }

    public void tick() {
        super.m_8119_();
        if (this.builder != null && this.builder.tick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
    }

    public void move(MoverType pType, Vec3 pPos) {
        super.m_6478_(pType, pPos);
        if (this.builder != null && this.builder.move != null) {
            ContextUtils.MovementContext context = new ContextUtils.MovementContext(pType, pPos, this);
            EntityJSHelperClass.consumerCallback(this.builder.move, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: move.");
        }
    }

    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.builder.positionRider != null) {
            ContextUtils.PositionRiderContext context = new ContextUtils.PositionRiderContext(this, pPassenger, pCallback);
            EntityJSHelperClass.consumerCallback(this.builder.positionRider, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: positionRider.");
        } else {
            super.m_19956_(pPassenger, pCallback);
        }
    }

    protected boolean canAddPassenger(@NotNull Entity entity) {
        if (this.builder.canAddPassenger == null) {
            return super.m_7310_(entity);
        } else {
            ContextUtils.EPassengerEntityContext context = new ContextUtils.EPassengerEntityContext(entity, this);
            Object obj = this.builder.canAddPassenger.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAddPassenger from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.m_7310_(entity));
                return super.m_7310_(entity);
            }
        }
    }

    public void playerTouch(Player player) {
        if (this.builder != null && this.builder.playerTouch != null) {
            ContextUtils.EntityPlayerContext context = new ContextUtils.EntityPlayerContext(player, this);
            EntityJSHelperClass.consumerCallback(this.builder.playerTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: playerTouch.");
        } else {
            super.m_6123_(player);
        }
    }

    public void onRemovedFromWorld() {
        if (this.builder != null && this.builder.onRemovedFromWorld != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onRemovedFromWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onRemovedFromWorld.");
        }
        super.onRemovedFromWorld();
    }

    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (this.builder != null && this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.EThunderHitContext context = new ContextUtils.EThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        if (this.builder != null && this.builder.onFall != null) {
            ContextUtils.EEntityFallDamageContext context = new ContextUtils.EEntityFallDamageContext(this, pMultiplier, pFallDistance, pSource);
            EntityJSHelperClass.consumerCallback(this.builder.onFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.m_142535_(pFallDistance, pMultiplier, pSource);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder != null && this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    public void setSprinting(boolean sprinting) {
        if (this.builder != null && this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.m_6858_(sprinting);
    }

    public void stopRiding() {
        super.m_8127_();
        if (this.builder != null && this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    public void rideTick() {
        super.m_6083_();
        if (this.builder != null && this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    public void onClientRemoval() {
        if (this.builder != null && this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    public void lavaHurt() {
        if (this.builder != null && this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    protected void onFlap() {
        if (this.builder != null && this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.builder != null && this.builder.shouldRenderAtSqrDistance != null) {
            ContextUtils.EntitySqrDistanceContext context = new ContextUtils.EntitySqrDistanceContext(distance, this);
            Object obj = this.builder.shouldRenderAtSqrDistance.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid shouldRenderAtSqrDistance for arrow builder: " + obj + ". Must be a boolean. Defaulting to super method: " + super.m_6783_(distance));
        }
        return super.m_6783_(distance);
    }

    public boolean isAttackable() {
        return this.builder.isAttackable != null ? this.builder.isAttackable : super.m_6097_();
    }

    public boolean isPushable() {
        return this.builder.isPushable;
    }

    public static class HitBlockContext {

        @Info("The projectile entity")
        public final Entity entity;

        @Info("The block state")
        public final BlockState state;

        @Info("The block position")
        public final BlockPos pos;

        @Info("The direction of the hit")
        public final Direction face;

        @Info("The x coordinate of the hit")
        public final double x;

        @Info("The y coordinate of the hit")
        public final double y;

        @Info("The z coordinate of the hit")
        public final double z;

        public HitBlockContext(Entity entity, BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
            this.entity = entity;
            this.state = state;
            this.pos = pos;
            this.face = face;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class ShotContext {

        @Info("The projectile entity")
        public final Entity entity;

        @Info("The target")
        public final Entity target;

        @Info("The hit vector")
        public final Vec3 hitVec;

        @Info("The start vector")
        public final Vec3 startVec;

        @Info("The end vector")
        public final Vec3 endVec;

        @Info("Whether it's a headshot")
        public final boolean headshot;

        public ShotContext(Entity entity, Entity target, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
            this.entity = entity;
            this.target = target;
            this.hitVec = hitVec;
            this.startVec = startVec;
            this.endVec = endVec;
            this.headshot = headshot;
        }
    }
}