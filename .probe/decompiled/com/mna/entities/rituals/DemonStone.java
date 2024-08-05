package com.mna.entities.rituals;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.entities.boss.DemonLord;
import com.mna.sound.EntityAliveLoopingSound;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class DemonStone extends Entity {

    protected int age = 0;

    private static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(DemonStone.class, EntityDataSerializers.STRING);

    private boolean summonAsHostile = false;

    public DemonStone(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public DemonStone setSummonAsHostile() {
        this.summonAsHostile = true;
        return this;
    }

    @Override
    public void tick() {
        this.age++;
        if (this.m_9236_().isClientSide()) {
            this.playSounds();
            this.spawnParticles();
        }
        if (this.age > 200 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            DemonLord edl = new DemonLord(this.m_9236_());
            edl.m_146884_(this.m_20182_());
            this.m_9236_().m_7967_(edl);
            if (!this.summonAsHostile) {
                UUID casterUUID = this.getCasterUUID();
                Player player = null;
                if (casterUUID != null) {
                    player = this.m_9236_().m_46003_(casterUUID);
                }
                edl.setSummoner(player);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playSounds() {
        if (this.age == 1) {
            Minecraft.getInstance().getSoundManager().play(new EntityAliveLoopingSound(SFX.Loops.DEMON_SUMMON, this));
        } else if (this.age == 120) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.DEMON_SUMMON_END, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    private void spawnParticles() {
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(ParticleTypes.LANDING_LAVA, this.m_20185_() - 1.5 + Math.random() * 3.0, this.m_20186_(), this.m_20189_() - 1.5 + Math.random() * 3.0, 0.0, 0.05F, 0.0);
        }
        if (this.age < 150) {
            for (int i = 0; i < 5; i++) {
                boolean xAxis = Math.random() > 0.5;
                boolean positive = Math.random() > 0.5;
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHER_BRICKS.defaultBlockState()), xAxis ? (positive ? this.m_20185_() + 2.5 - Math.random() * 0.5 : this.m_20185_() - 2.5 + Math.random() * 0.5) : this.m_20185_() + 2.5 - Math.random() * 5.0, this.m_20186_(), !xAxis ? (positive ? this.m_20189_() + 2.5 - Math.random() * 0.5 : this.m_20189_() - 2.5 + Math.random() * 0.5) : this.m_20189_() + 2.5 - Math.random() * 5.0, 0.0, 0.05F, 0.0);
            }
        }
        this.m_9236_().addParticle(ParticleTypes.LAVA, this.m_20185_() - 0.5 + Math.random() * 1.0, this.m_20186_(), this.m_20189_() - 0.5 + Math.random() * 1.0, 0.0, 0.05F, 0.0);
        if (this.age > 160) {
            for (int i = 0; i < 25; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), this.m_20185_() - 0.25 + Math.random() * 0.5, this.m_20186_(), this.m_20189_() - 0.25 + Math.random() * 0.5, 0.0, 0.15F, 0.0);
            }
        }
        if (this.age == 199) {
            for (int i = 0; i < 100; i++) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHER_BRICKS.defaultBlockState()), this.m_20185_() - 1.5 + Math.random() * 3.0, this.m_20186_() + 1.5, this.m_20189_() - 1.5 + Math.random() * 3.0, -0.5 + Math.random(), 0.0, -0.5 + Math.random());
            }
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(CASTER_UUID, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("age")) {
            this.age = compound.getInt("age");
        }
        if (compound.contains("caster")) {
            this.f_19804_.set(CASTER_UUID, compound.getString("caster"));
        }
        if (compound.contains("summonHostile")) {
            this.summonAsHostile = compound.getBoolean("summonHostile");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("age", this.age);
        compound.putString("caster", this.f_19804_.get(CASTER_UUID));
        compound.putBoolean("summonHostile", this.summonAsHostile);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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
}