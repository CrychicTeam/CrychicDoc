package net.minecraft.world.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LlamaSpit extends Projectile {

    public LlamaSpit(EntityType<? extends LlamaSpit> entityTypeExtendsLlamaSpit0, Level level1) {
        super(entityTypeExtendsLlamaSpit0, level1);
    }

    public LlamaSpit(Level level0, Llama llama1) {
        this(EntityType.LLAMA_SPIT, level0);
        this.m_5602_(llama1);
        this.m_6034_(llama1.m_20185_() - (double) (llama1.m_20205_() + 1.0F) * 0.5 * (double) Mth.sin(llama1.f_20883_ * (float) (Math.PI / 180.0)), llama1.m_20188_() - 0.1F, llama1.m_20189_() + (double) (llama1.m_20205_() + 1.0F) * 0.5 * (double) Mth.cos(llama1.f_20883_ * (float) (Math.PI / 180.0)));
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 $$0 = this.m_20184_();
        HitResult $$1 = ProjectileUtil.getHitResultOnMoveVector(this, this::m_5603_);
        this.m_6532_($$1);
        double $$2 = this.m_20185_() + $$0.x;
        double $$3 = this.m_20186_() + $$0.y;
        double $$4 = this.m_20189_() + $$0.z;
        this.m_37283_();
        float $$5 = 0.99F;
        float $$6 = 0.06F;
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_)) {
            this.m_146870_();
        } else if (this.m_20072_()) {
            this.m_146870_();
        } else {
            this.m_20256_($$0.scale(0.99F));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.06F, 0.0));
            }
            this.m_6034_($$2, $$3, $$4);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.onHitEntity(entityHitResult0);
        if (this.m_19749_() instanceof LivingEntity $$1) {
            entityHitResult0.getEntity().hurt(this.m_269291_().mobProjectile(this, $$1), 1.0F);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult0) {
        super.onHitBlock(blockHitResult0);
        if (!this.m_9236_().isClientSide) {
            this.m_146870_();
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.recreateFromPacket(clientboundAddEntityPacket0);
        double $$1 = clientboundAddEntityPacket0.getXa();
        double $$2 = clientboundAddEntityPacket0.getYa();
        double $$3 = clientboundAddEntityPacket0.getZa();
        for (int $$4 = 0; $$4 < 7; $$4++) {
            double $$5 = 0.4 + 0.1 * (double) $$4;
            this.m_9236_().addParticle(ParticleTypes.SPIT, this.m_20185_(), this.m_20186_(), this.m_20189_(), $$1 * $$5, $$2, $$3 * $$5);
        }
        this.m_20334_($$1, $$2, $$3);
    }
}