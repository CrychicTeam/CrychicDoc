package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityVineLasso extends Entity {

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    public EntityVineLasso(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityVineLasso(Level worldIn, LivingEntity entity) {
        this(AMEntityRegistry.VINE_LASSO.get(), worldIn);
        this.setShooter(entity);
        this.m_6034_(entity.m_20185_(), entity.m_20188_() + 0.15F, entity.m_20189_());
    }

    @OnlyIn(Dist.CLIENT)
    public EntityVineLasso(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this(AMEntityRegistry.VINE_LASSO.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntityVineLasso(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.VINE_LASSO.get(), world);
    }

    protected static float lerpRotation(float p_234614_0_, float p_234614_1_) {
        while (p_234614_1_ - p_234614_0_ < -180.0F) {
            p_234614_0_ -= 360.0F;
        }
        while (p_234614_1_ - p_234614_0_ >= 180.0F) {
            p_234614_0_ += 360.0F;
        }
        return Mth.lerp(0.2F, p_234614_0_, p_234614_1_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        this.updateRotation();
        if (this.getOwner() != null && this.m_20270_(this.getOwner()) > 15.0F) {
            this.removeAndAddToInventory();
        }
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_) && !this.m_20069_() && !this.m_20077_()) {
            this.removeAndAddToInventory();
        } else {
            double d0 = this.m_20185_() + vector3d.x;
            double d1 = this.m_20186_() + vector3d.y;
            double d2 = this.m_20189_() + vector3d.z;
            this.m_20256_(vector3d.scale(0.99F));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.02F, 0.0));
            }
            this.m_6034_(d0, d1, d2);
        }
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity && p_213868_1_.getEntity() != this.getOwner() && p_213868_1_.getEntity() instanceof LivingEntity && !VineLassoUtil.hasLassoData((LivingEntity) p_213868_1_.getEntity())) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            VineLassoUtil.lassoTo((LivingEntity) entity, (LivingEntity) p_213868_1_.getEntity());
        }
    }

    private void removeAndAddToInventory() {
        Entity entity = this.getOwner();
        ItemStack item = new ItemStack(AMItemRegistry.VINE_LASSO.get());
        if (!this.m_213877_() && (!(entity instanceof Player) || !((Player) entity).addItem(item))) {
            this.m_19983_(item);
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().getBlockState(p_230299_1_.getBlockPos());
        if (!this.m_9236_().isClientSide) {
            this.removeAndAddToInventory();
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.getUUID();
            this.ownerNetworkId = entityIn.getId();
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
        } else {
            return this.ownerNetworkId != 0 ? this.m_9236_().getEntity(this.ownerNetworkId) : null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compound.putBoolean("LeftOwner", true);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.leftOwner = compound.getBoolean("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for (Entity entity1 : this.m_9236_().getEntities(this, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), p_234613_0_ -> !p_234613_0_.isSpectator() && p_234613_0_.isPickable())) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) (vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -Mth.sin(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((p_234612_2_ + p_234612_4_) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        this.shoot((double) f, (double) f1, (double) f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(vector3d.x, p_234612_1_.onGround() ? 0.0 : vector3d.y, vector3d.z));
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            float f = Mth.sqrt((float) (x * x + z * z));
            this.m_146926_((float) (Mth.atan2(y, (double) f) * 180.0F / (float) Math.PI));
            this.m_146922_((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
            Entity entity = this.getOwner();
            return entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(p_230298_1_);
        } else {
            return false;
        }
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) (vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(this.m_146908_() + 20.0F);
    }
}