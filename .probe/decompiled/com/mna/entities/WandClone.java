package com.mna.entities;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.items.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class WandClone extends Entity {

    protected static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<Byte> STATE = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<String> CURRENT_ANIMATION = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.STRING);

    private int age = 0;

    public static final Byte GUIDING_REAGENT_PLACEMENT = (byte) 0;

    public static final Byte POWERING_UP = (byte) 1;

    public static final Byte COLLECTING_REAGENTS = (byte) 2;

    public static final Byte COLLECTING_PATTERNS = (byte) 3;

    public static final Byte PROCESSING_RITUAL = (byte) 4;

    public static final Byte COMPLETING = (byte) 5;

    public static final Byte DEAD = (byte) 99;

    private Vec3 centerPoint = new Vec3(0.0, 0.0, 0.0);

    public WandClone(EntityType<? extends WandClone> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public byte getState() {
        return this.f_19804_.get(STATE);
    }

    @Override
    public void tick() {
        this.setAge(this.getAge() + 1);
        if (this.m_9236_().isClientSide()) {
            this.spawnParticles();
        }
        if (this.getAge() >= 81 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            Vec3 up = new Vec3(0.0, 1.0, 0.0);
            Vec3 perpendicular = up.cross(this.m_20154_());
            Vec3 pointOne = new Vec3(this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y, this.m_20189_() + perpendicular.z);
            Vec3 pointTwo = new Vec3(this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y + 1.0, this.m_20189_() + perpendicular.z);
            Vec3 pointThree = new Vec3(this.m_20185_(), this.m_20186_() + perpendicular.y, this.m_20189_());
            Vec3 pointFour = new Vec3(this.m_20185_(), this.m_20186_() + perpendicular.y + 1.0, this.m_20189_());
            double CenterPointX = (pointOne.x + pointTwo.x + pointThree.x + pointFour.x) / 4.0;
            double CenterPointZ = (pointOne.z + pointTwo.z + pointThree.z + pointFour.z) / 4.0;
            double CenterPointY = this.m_20186_() + perpendicular.y + 0.5;
            Vec3 endPoint = new Vec3(CenterPointX, CenterPointY, CenterPointZ);
            this.setCenterPoint(endPoint);
            ItemStack itemstack = new ItemStack(ItemInit.WAND_CLONE.get());
            ItemEntity ie = new ItemEntity(this.m_9236_(), this.getCenterPoint().x, this.getCenterPoint().y, this.getCenterPoint().z, itemstack);
            ie.setItem(new ItemStack(ItemInit.WAND_CLONE.get()));
            this.m_9236_().m_7967_(ie);
        }
    }

    @Override
    public void baseTick() {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    public boolean canUpdate() {
        return true;
    }

    public void spawnParticles() {
        double ySpeed = 0.006F;
        MAParticleType bpt = new MAParticleType(ParticleInit.SPARKLE_GRAVITY.get());
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        Vec3 perpendicular = up.cross(this.m_20154_());
        if (this.getAge() % 5 == 0) {
            this.m_9236_().addParticle(bpt, this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y, this.m_20189_() + perpendicular.z, 0.0, ySpeed, 0.0);
            this.m_9236_().addParticle(bpt, this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y + 1.0, this.m_20189_() + perpendicular.z, 0.0, ySpeed, 0.0);
            this.m_9236_().addParticle(bpt, this.m_20185_(), this.m_20186_() + perpendicular.y, this.m_20189_(), 0.0, ySpeed, 0.0);
            this.m_9236_().addParticle(bpt, this.m_20185_(), this.m_20186_() + perpendicular.y + 1.0, this.m_20189_(), 0.0, ySpeed, 0.0);
        }
        if (this.getAge() > 60) {
            Vec3 pointOne = new Vec3(this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y, this.m_20189_() + perpendicular.z);
            Vec3 pointTwo = new Vec3(this.m_20185_() + perpendicular.x, this.m_20186_() + perpendicular.y + 1.0, this.m_20189_() + perpendicular.z);
            Vec3 pointThree = new Vec3(this.m_20185_(), this.m_20186_() + perpendicular.y, this.m_20189_());
            Vec3 pointFour = new Vec3(this.m_20185_(), this.m_20186_() + perpendicular.y + 1.0, this.m_20189_());
            double CenterPointX = (pointOne.x + pointTwo.x + pointThree.x + pointFour.x) / 4.0;
            double CenterPointZ = (pointOne.z + pointTwo.z + pointThree.z + pointFour.z) / 4.0;
            double CenterPointY = this.m_20186_() + perpendicular.y + 0.5;
            Vec3 endPoint = new Vec3(CenterPointX, CenterPointY, CenterPointZ);
            this.setCenterPoint(endPoint);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, 0.02, 0.0, 0.02);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, 0.02, 0.0, 0.0);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, 0.0, 0.0, 0.02);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, -0.02, 0.0, 0.0);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, 0.0, 0.0, -0.02);
            this.m_9236_().addParticle(ParticleInit.ARCANE.get(), CenterPointX, CenterPointY, CenterPointZ, -0.02, 0.0, -0.02);
        }
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Vec3 getCenterPoint() {
        return this.centerPoint;
    }

    public void setCenterPoint(Vec3 centerPoint) {
        this.centerPoint = centerPoint;
    }
}