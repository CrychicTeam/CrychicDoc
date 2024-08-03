package com.mna.entities.boss.attacks;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import com.mna.entities.UtilityEntityBase;
import com.mna.sound.EntityAliveLoopingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PumpkinKingIncinerate extends UtilityEntityBase {

    public PumpkinKingIncinerate(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PumpkinKingIncinerate(Level p_i48580_2_) {
        super(EntityInit.PUMPKIN_KING_INCINERATE.get(), p_i48580_2_);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide()) {
            this.spawnParticles();
        }
        if (this.age >= 25) {
            this.m_5496_(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            this.m_9236_().getEntities(this, this.m_20191_().inflate(2.0), e -> e instanceof Player && e.isAlive() && e.isAttackable()).stream().forEach(player -> {
                player.setSecondsOnFire(4);
                player.hurt(this.m_269291_().inFire(), 8.0F);
            });
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playSounds() {
        if (this.age == 1) {
            Minecraft.getInstance().getSoundManager().play(new EntityAliveLoopingSound(SFX.Loops.DEMON_SUMMON, this));
        } else if (this.age == 20) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.DEMON_SUMMON_END, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    private void spawnParticles() {
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(ParticleTypes.LANDING_LAVA, this.m_20185_() - 1.5 + Math.random() * 3.0, this.m_20186_(), this.m_20189_() - 1.5 + Math.random() * 3.0, 0.0, 0.05F, 0.0);
        }
        this.m_9236_().addParticle(ParticleTypes.LAVA, this.m_20185_() - 0.5 + Math.random() * 1.0, this.m_20186_(), this.m_20189_() - 0.5 + Math.random() * 1.0, 0.0, 0.05F, 0.0);
        for (int i = 0; i < 25; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.FLAME.get()), this.m_20185_() - 0.25 + Math.random() * 0.5, this.m_20186_(), this.m_20189_() - 0.25 + Math.random() * 0.5, 0.0, 0.15F, 0.0);
        }
        if (this.age == 20) {
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
    }
}