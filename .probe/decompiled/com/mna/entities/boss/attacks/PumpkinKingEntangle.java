package com.mna.entities.boss.attacks;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class PumpkinKingEntangle extends Entity {

    private Vec3 origin;

    private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(PumpkinKingEntangle.class, EntityDataSerializers.OPTIONAL_UUID);

    public PumpkinKingEntangle(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.m_20242_(true);
        this.f_19794_ = true;
    }

    public PumpkinKingEntangle(Level p_i48580_2_) {
        this(EntityInit.PUMPKIN_KING_ENTANGLE.get(), p_i48580_2_);
    }

    public void setTarget(Player player) {
        this.f_19804_.set(TARGET, Optional.of(player.m_20148_()));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.origin == null) {
            this.origin = this.m_20182_();
        }
        if (this.m_9236_().isClientSide()) {
            BlockPos cur = BlockPos.containing(this.origin.x, this.origin.y, this.origin.z);
            BlockState state = this.m_9236_().getBlockState(cur);
            this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.origin.x - 0.25 + Math.random() * 0.5, this.origin.y + 0.25, this.origin.z - 0.25 + Math.random() * 0.5, 0.0, 0.05 * Math.random(), 0.0);
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.EARTH.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.105882354F, 0.3137255F, 0.05490196F);
        }
        if (this.f_19797_ == 20) {
            this.resolveTarget();
        } else if (this.f_19797_ > 20 && this.f_19797_ < 79) {
            this.f_19794_ = true;
            this.m_6478_(MoverType.SELF, this.m_20184_());
        }
        if (this.f_19797_ > 80) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    private void resolveTarget() {
        this.f_19804_.get(TARGET).ifPresent(u -> {
            Player player = this.m_9236_().m_46003_(u);
            Vec3 movement = player.m_20182_().subtract(this.origin).normalize();
            movement = movement.scale(0.25);
            this.m_20256_(movement);
        });
    }

    public Vec3 getOrigin() {
        return this.origin;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.f_19797_ > 20) {
            if (!this.m_9236_().isClientSide()) {
                if (!player.m_20096_()) {
                    player.m_7292_(new MobEffectInstance(EffectInit.GRAVITY_WELL.get(), 20, 1));
                    player.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
                } else {
                    player.m_7292_(new MobEffectInstance(EffectInit.ENTANGLE.get(), 60, 1));
                }
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TARGET, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(128.0);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}