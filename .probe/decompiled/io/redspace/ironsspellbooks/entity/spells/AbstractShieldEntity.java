package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;

public abstract class AbstractShieldEntity extends Entity implements AntiMagicSusceptible {

    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = SynchedEntityData.defineId(AbstractShieldEntity.class, EntityDataSerializers.FLOAT);

    public boolean hurtThisTick;

    public AbstractShieldEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractShieldEntity(Level level, float health) {
        this(EntityRegistry.SHIELD_ENTITY.get(), level);
        this.setHealth(health);
    }

    protected abstract void createShield();

    public abstract void takeDamage(DamageSource var1, float var2, @Nullable Vec3 var3);

    @Override
    public void tick() {
        this.hurtThisTick = false;
        for (PartEntity<?> subEntity : this.getParts()) {
            Vec3 pos = subEntity.m_20182_();
            subEntity.m_146884_(pos);
            subEntity.f_19854_ = pos.x;
            subEntity.f_19855_ = pos.y;
            subEntity.f_19856_ = pos.z;
            subEntity.f_19790_ = pos.x;
            subEntity.f_19791_ = pos.y;
            subEntity.f_19792_ = pos.z;
        }
    }

    protected void destroy() {
        this.m_6074_();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public abstract PartEntity<?>[] getParts();

    @Override
    public void setId(int id) {
        super.setId(id);
        PartEntity<?>[] subEntities = this.getParts();
        for (int i = 0; i < subEntities.length; i++) {
            subEntities[i].m_20234_(id + i + 1);
        }
    }

    public float getHealth() {
        return this.f_19804_.get(DATA_HEALTH_ID);
    }

    public void setHealth(float pHealth) {
        this.f_19804_.set(DATA_HEALTH_ID, pHealth);
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_HEALTH_ID, 1.0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Health", 99)) {
            this.setHealth(pCompound.getFloat("Health"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Health", this.getHealth());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public List<VoxelShape> getVoxels() {
        List<VoxelShape> voxels = new ArrayList();
        for (PartEntity<?> shieldPart : this.getParts()) {
            voxels.add(Shapes.create(shieldPart.m_20191_()));
        }
        return voxels;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.m_146870_();
    }
}