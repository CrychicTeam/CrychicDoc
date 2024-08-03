package com.mna.entities.boss.effects;

import com.mna.ManaAndArtifice;
import com.mna.api.sound.SFX;
import com.mna.entities.UtilityEntityBase;
import com.mna.entities.boss.DemonLord;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class DemonReturnPortal extends UtilityEntityBase implements IEntityAdditionalSpawnData {

    private DemonLord focus;

    public DemonReturnPortal(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= 180 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.m_9236_().isClientSide()) {
            float bbWidth = this.m_20205_() / 2.0F;
            float bbWidthHalf = -bbWidth / 2.0F;
            Vec3 pos = this.m_20182_().add((double) bbWidthHalf, (double) bbWidthHalf, (double) bbWidthHalf).add((double) bbWidth * Math.random(), (double) bbWidth * Math.random(), (double) bbWidth * Math.random());
            this.m_9236_().addParticle(ParticleTypes.LAVA, pos.x, pos.y, pos.z, -0.5 + Math.random(), Math.random(), -0.5 + Math.random());
        }
    }

    public void setFocus(DemonLord focus) {
        this.focus = focus;
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.m_9236_().isClientSide()) {
            ManaAndArtifice.instance.proxy.playEntityAliveLoopingSound(SFX.Loops.DEMON_SUMMON, this);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            ManaAndArtifice.instance.proxy.playEntityAliveLoopingSound(SFX.Loops.DEMON_SUMMON, this);
        }
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
    public void writeSpawnData(FriendlyByteBuf buffer) {
        if (this.focus != null) {
            buffer.writeBoolean(true);
            buffer.writeInt(this.focus.m_19879_());
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        if (additionalData.readBoolean()) {
            int entityID = additionalData.readInt();
            Entity e = this.m_9236_().getEntity(entityID);
            if (e != null && e instanceof DemonLord) {
                this.focus = (DemonLord) e;
            }
        }
    }
}