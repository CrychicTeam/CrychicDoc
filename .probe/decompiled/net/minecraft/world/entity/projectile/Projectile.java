package net.minecraft.world.entity.projectile;

import com.google.common.base.MoreObjects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class Projectile extends Entity implements TraceableEntity {

    @Nullable
    private UUID ownerUUID;

    @Nullable
    private Entity cachedOwner;

    private boolean leftOwner;

    private boolean hasBeenShot;

    Projectile(EntityType<? extends Projectile> entityTypeExtendsProjectile0, Level level1) {
        super(entityTypeExtendsProjectile0, level1);
    }

    public void setOwner(@Nullable Entity entity0) {
        if (entity0 != null) {
            this.ownerUUID = entity0.getUUID();
            this.cachedOwner = entity0;
        }
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public Entity getEffectSource() {
        return (Entity) MoreObjects.firstNonNull(this.getOwner(), this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        if (this.ownerUUID != null) {
            compoundTag0.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compoundTag0.putBoolean("LeftOwner", true);
        }
        compoundTag0.putBoolean("HasBeenShot", this.hasBeenShot);
    }

    protected boolean ownedBy(Entity entity0) {
        return entity0.getUUID().equals(this.ownerUUID);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.hasUUID("Owner")) {
            this.ownerUUID = compoundTag0.getUUID("Owner");
            this.cachedOwner = null;
        }
        this.leftOwner = compoundTag0.getBoolean("LeftOwner");
        this.hasBeenShot = compoundTag0.getBoolean("HasBeenShot");
    }

    @Override
    public void tick() {
        if (!this.hasBeenShot) {
            this.m_146852_(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        super.tick();
    }

    private boolean checkLeftOwner() {
        Entity $$0 = this.getOwner();
        if ($$0 != null) {
            for (Entity $$1 : this.m_9236_().getEntities(this, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), p_37272_ -> !p_37272_.isSpectator() && p_37272_.isPickable())) {
                if ($$1.getRootVehicle() == $$0.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shoot(double double0, double double1, double double2, float float3, float float4) {
        Vec3 $$5 = new Vec3(double0, double1, double2).normalize().add(this.f_19796_.triangle(0.0, 0.0172275 * (double) float4), this.f_19796_.triangle(0.0, 0.0172275 * (double) float4), this.f_19796_.triangle(0.0, 0.0172275 * (double) float4)).scale((double) float3);
        this.m_20256_($$5);
        double $$6 = $$5.horizontalDistance();
        this.m_146922_((float) (Mth.atan2($$5.x, $$5.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2($$5.y, $$6) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity entity0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = -Mth.sin(float2 * (float) (Math.PI / 180.0)) * Mth.cos(float1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin((float1 + float3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos(float2 * (float) (Math.PI / 180.0)) * Mth.cos(float1 * (float) (Math.PI / 180.0));
        this.shoot((double) $$6, (double) $$7, (double) $$8, float4, float5);
        Vec3 $$9 = entity0.getDeltaMovement();
        this.m_20256_(this.m_20184_().add($$9.x, entity0.onGround() ? 0.0 : $$9.y, $$9.z));
    }

    protected void onHit(HitResult hitResult0) {
        HitResult.Type $$1 = hitResult0.getType();
        if ($$1 == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) hitResult0);
            this.m_9236_().m_214171_(GameEvent.PROJECTILE_LAND, hitResult0.getLocation(), GameEvent.Context.of(this, null));
        } else if ($$1 == HitResult.Type.BLOCK) {
            BlockHitResult $$2 = (BlockHitResult) hitResult0;
            this.onHitBlock($$2);
            BlockPos $$3 = $$2.getBlockPos();
            this.m_9236_().m_220407_(GameEvent.PROJECTILE_LAND, $$3, GameEvent.Context.of(this, this.m_9236_().getBlockState($$3)));
        }
    }

    protected void onHitEntity(EntityHitResult entityHitResult0) {
    }

    protected void onHitBlock(BlockHitResult blockHitResult0) {
        BlockState $$1 = this.m_9236_().getBlockState(blockHitResult0.getBlockPos());
        $$1.m_60669_(this.m_9236_(), $$1, blockHitResult0, this);
    }

    @Override
    public void lerpMotion(double double0, double double1, double double2) {
        this.m_20334_(double0, double1, double2);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            double $$3 = Math.sqrt(double0 * double0 + double2 * double2);
            this.m_146926_((float) (Mth.atan2(double1, $$3) * 180.0F / (float) Math.PI));
            this.m_146922_((float) (Mth.atan2(double0, double2) * 180.0F / (float) Math.PI));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    protected boolean canHitEntity(Entity entity0) {
        if (!entity0.canBeHitByProjectile()) {
            return false;
        } else {
            Entity $$1 = this.getOwner();
            return $$1 == null || this.leftOwner || !$$1.isPassengerOfSameVehicle(entity0);
        }
    }

    protected void updateRotation() {
        Vec3 $$0 = this.m_20184_();
        double $$1 = $$0.horizontalDistance();
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2($$0.y, $$1) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2($$0.x, $$0.z) * 180.0F / (float) Math.PI)));
    }

    protected static float lerpRotation(float float0, float float1) {
        while (float1 - float0 < -180.0F) {
            float0 -= 360.0F;
        }
        while (float1 - float0 >= 180.0F) {
            float0 += 360.0F;
        }
        return Mth.lerp(0.2F, float0, float1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity $$0 = this.getOwner();
        return new ClientboundAddEntityPacket(this, $$0 == null ? 0 : $$0.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.recreateFromPacket(clientboundAddEntityPacket0);
        Entity $$1 = this.m_9236_().getEntity(clientboundAddEntityPacket0.getData());
        if ($$1 != null) {
            this.setOwner($$1);
        }
    }

    @Override
    public boolean mayInteract(Level level0, BlockPos blockPos1) {
        Entity $$2 = this.getOwner();
        return $$2 instanceof Player ? $$2.mayInteract(level0, blockPos1) : $$2 == null || level0.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }
}