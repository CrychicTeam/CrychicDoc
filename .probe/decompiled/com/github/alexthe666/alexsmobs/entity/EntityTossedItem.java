package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityTossedItem extends ThrowableItemProjectile {

    protected static final EntityDataAccessor<Boolean> DART = SynchedEntityData.defineId(EntityTossedItem.class, EntityDataSerializers.BOOLEAN);

    public EntityTossedItem(EntityType p_i50154_1_, Level p_i50154_2_) {
        super(p_i50154_1_, p_i50154_2_);
    }

    public EntityTossedItem(Level worldIn, LivingEntity throwerIn) {
        super(AMEntityRegistry.TOSSED_ITEM.get(), throwerIn, worldIn);
    }

    public EntityTossedItem(Level worldIn, double x, double y, double z) {
        super(AMEntityRegistry.TOSSED_ITEM.get(), x, y, z, worldIn);
    }

    public EntityTossedItem(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.TOSSED_ITEM.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DART, false);
    }

    public boolean isDart() {
        return this.f_19804_.get(DART);
    }

    public void setDart(boolean dart) {
        this.f_19804_.set(DART, dart);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            double d0 = 0.08;
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
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

    @Override
    public void tick() {
        super.m_8119_();
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
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
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.m_5790_(p_213868_1_);
        if (this.m_19749_() instanceof EntityCapuchinMonkey) {
            EntityCapuchinMonkey boss = (EntityCapuchinMonkey) this.m_19749_();
            if (!boss.isAlliedTo(p_213868_1_.getEntity()) || !boss.m_21824_() && !(p_213868_1_.getEntity() instanceof EntityCapuchinMonkey)) {
                p_213868_1_.getEntity().hurt(this.m_269291_().thrown(this, boss), this.isDart() ? 8.0F : 4.0F);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("Dart", this.isDart());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setDart(compound.getBoolean("Dart"));
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide && (!this.isDart() || result.getType() == HitResult.Type.BLOCK)) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return this.isDart() ? AMItemRegistry.ANCIENT_DART.get() : Items.COBBLESTONE;
    }
}