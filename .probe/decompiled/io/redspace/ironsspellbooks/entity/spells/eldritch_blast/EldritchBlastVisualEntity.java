package io.redspace.ironsspellbooks.entity.spells.eldritch_blast;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class EldritchBlastVisualEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final int lifetime = 8;

    public float distance;

    public EldritchBlastVisualEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EldritchBlastVisualEntity(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super(EntityRegistry.ELDRITCH_BLAST_VISUAL_ENTITY.get(), level);
        this.m_146884_(start);
        this.distance = (float) start.distanceTo(end);
        this.m_19915_(owner.m_146908_(), owner.m_146909_());
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 1) {
            if (this.f_19853_.isClientSide) {
                Vec3 forward = this.m_20156_();
                for (float i = 1.0F; i < this.distance; i += 0.5F) {
                    Vec3 pos = this.m_20182_().add(forward.scale((double) i));
                    this.f_19853_.addParticle(ParticleTypes.SMOKE, false, pos.x, pos.y + 0.5, pos.z, 0.0, 0.0, 0.0);
                }
            }
        } else if (this.f_19797_ > 8) {
            this.m_146870_();
        }
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt((int) (this.distance * 10.0F));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.distance = (float) additionalData.readInt() / 10.0F;
    }
}