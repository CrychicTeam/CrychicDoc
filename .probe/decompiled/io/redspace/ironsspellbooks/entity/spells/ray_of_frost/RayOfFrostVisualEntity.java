package io.redspace.ironsspellbooks.entity.spells.ray_of_frost;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
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

public class RayOfFrostVisualEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final int lifetime = 15;

    public float distance;

    public RayOfFrostVisualEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RayOfFrostVisualEntity(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super(EntityRegistry.RAY_OF_FROST_VISUAL_ENTITY.get(), level);
        this.m_146884_(start.subtract(0.0, 0.75, 0.0));
        this.distance = (float) start.distanceTo(end);
        this.m_19915_(owner.m_146908_(), owner.m_146909_());
    }

    @Override
    public void tick() {
        if (++this.f_19797_ > 15) {
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