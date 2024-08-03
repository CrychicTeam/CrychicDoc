package com.mna.entities.rituals;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import com.mna.entities.UtilityEntityBase;
import com.mna.entities.boss.FaerieQueen;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FeyLight extends UtilityEntityBase {

    private static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(FeyLight.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<BlockPos> TARGET_POSITION = SynchedEntityData.defineId(FeyLight.class, EntityDataSerializers.BLOCK_POS);

    private float angle = 0.0F;

    private int growTicks = 0;

    private static int maxGrowTicks = 40;

    public FeyLight(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age == 1 && this.m_9236_().isClientSide()) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() - 9.0, this.m_20189_(), SFX.Event.Ritual.FAERIE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
        if (this.updateMovement()) {
            this.growTicks++;
            if (this.growTicks > maxGrowTicks && !this.m_9236_().isClientSide()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                FaerieQueen efq = new FaerieQueen(EntityInit.FAERIE_QUEEN.get(), this.m_9236_());
                efq.m_146884_(this.m_20182_().subtract(0.0, 1.0, 0.0));
                UUID casterUUID = this.getCasterUUID();
                Player player = null;
                if (casterUUID != null) {
                    player = this.m_9236_().m_46003_(casterUUID);
                }
                efq.setSummoner(player);
                this.m_9236_().m_7967_(efq);
            }
        } else if (this.m_9236_().isClientSide()) {
            float r = 0.25F;
            float r2 = r * 2.0F;
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_STATIONARY.get()), this.m_20185_() - (double) r + Math.random() * (double) r2, this.m_20186_() - (double) r + Math.random() * (double) r2, this.m_20189_() - (double) r + Math.random() * (double) r2, 0.0, 0.0, 0.0);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        double radius = 1.0;
        double velocity = 0.5;
        if (this.m_9236_().isClientSide()) {
            RandomSource random = this.m_9236_().getRandom();
            for (int i = 0; i < 100; i++) {
                Vec3 pos = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();
                Vec3 vel = pos.scale(velocity);
                pos = pos.scale(radius);
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), this.m_20185_() + pos.x, this.m_20186_() + pos.y, this.m_20189_() + pos.z, vel.x, vel.y, vel.z);
            }
        }
    }

    private boolean updateMovement() {
        Vec3 target = new Vec3((double) this.getTargetPos().m_123341_(), (double) this.getTargetPos().m_123342_(), (double) this.getTargetPos().m_123343_()).add(0.5, 0.0, 0.5);
        Vec3 me = this.m_20182_();
        if (!(target.distanceTo(me) < 0.2F) && !(me.y < target.y)) {
            Vec3 diff = me.subtract(target);
            float radius = (float) diff.y / 2.0F;
            this.angle = (float) ((double) this.angle + 0.08726646324990228);
            Vec3 newPos = new Vec3(target.x + (double) radius * Math.cos((double) this.angle), target.y + diff.y - 0.09F, target.z + (double) radius * Math.sin((double) this.angle));
            this.m_6034_(newPos.x, newPos.y, newPos.z);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(CASTER_UUID, "");
        this.f_19804_.define(TARGET_POSITION, this.m_20183_());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("caster")) {
            this.f_19804_.set(CASTER_UUID, compound.getString("caster"));
        }
        if (compound.contains("target_position")) {
            CompoundTag targetPos = compound.getCompound("target_position");
            this.f_19804_.set(TARGET_POSITION, BlockPos.containing((double) targetPos.getFloat("x"), (double) targetPos.getFloat("y"), (double) targetPos.getFloat("z")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("caster", this.f_19804_.get(CASTER_UUID));
        CompoundTag targetPos = new CompoundTag();
        targetPos.putFloat("x", (float) this.f_19804_.get(TARGET_POSITION).m_123341_());
        targetPos.putFloat("y", (float) this.f_19804_.get(TARGET_POSITION).m_123342_());
        targetPos.putFloat("z", (float) this.f_19804_.get(TARGET_POSITION).m_123343_());
        compound.put("target_position", targetPos);
    }

    public UUID getCasterUUID() {
        try {
            return UUID.fromString(this.f_19804_.get(CASTER_UUID));
        } catch (Exception var5) {
            return null;
        } finally {
            ;
        }
    }

    public void setCasterUUID(UUID casterUUID) {
        if (casterUUID != null) {
            this.f_19804_.set(CASTER_UUID, casterUUID.toString());
        } else {
            ManaAndArtifice.LOGGER.error("Received null UUID for ritual caster.  Some effects may not apply!");
        }
    }

    public BlockPos getTargetPos() {
        return this.f_19804_.get(TARGET_POSITION);
    }

    public void setTargetPos(BlockPos target) {
        this.f_19804_.set(TARGET_POSITION, target);
    }

    public float getGrowthAmount() {
        return (float) this.growTicks / (float) maxGrowTicks;
    }
}