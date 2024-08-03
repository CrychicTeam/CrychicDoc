package noppes.npcs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityChairMount extends Entity {

    public EntityChairMount(EntityType type, Level world) {
        super(type, world);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.5;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.m_9236_() != null && !this.m_9236_().isClientSide && this.m_20197_().isEmpty()) {
            this.m_146870_();
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public void move(MoverType type, Vec3 vec) {
    }

    @Override
    public void load(CompoundTag tagCompound) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public CompoundTag saveWithoutId(CompoundTag tagCompound) {
        return tagCompound;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_, boolean bo) {
        this.m_6034_(p_70056_1_, p_70056_3_, p_70056_5_);
        this.m_19915_(p_70056_7_, p_70056_8_);
    }
}