package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.MineExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class DepthChargeEntity extends ThrowableItemProjectile {

    private boolean hitGround = false;

    private int groundTime = 0;

    public DepthChargeEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public DepthChargeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.DEPTH_CHARGE.get(), level);
    }

    public DepthChargeEntity(Level level, LivingEntity thrower) {
        super(ACEntityRegistry.DEPTH_CHARGE.get(), thrower, level);
    }

    public DepthChargeEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.DEPTH_CHARGE.get(), x, y, z, level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleEntityEvent(byte message) {
        if (message == 3) {
            double d0 = 0.08;
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.m_5790_(hitResult);
        hitResult.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 2.0F + (float) this.f_19796_.nextInt(2));
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.m_6532_(hitResult);
        this.hitGround = true;
    }

    @Override
    public void tick() {
        this.m_6075_();
        this.m_20101_();
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.05F, 0.0));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_37283_();
        this.m_20256_(this.m_20184_().scale(0.99F));
        if (this.m_20096_() || this.f_19862_ || this.f_19863_) {
            this.hitGround = true;
        }
        if (this.m_20069_()) {
            for (int i = 0; i < 4; i++) {
                float f1 = 0.25F;
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
        if (this.hitGround) {
            this.m_20256_(this.m_20184_().multiply(0.0, 0.0, 0.0));
            if (this.groundTime++ > 30) {
                this.explode();
            }
        }
    }

    private void explode() {
        this.m_142687_(Entity.RemovalReason.KILLED);
        Explosion.BlockInteraction blockinteraction = ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) ? (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY) : Explosion.BlockInteraction.KEEP;
        boolean inWater = this.m_146900_() != null && this.m_146900_().m_60819_().is(FluidTags.WATER);
        MineExplosion explosion = new MineExplosion(this.m_9236_(), this, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 2.0F, inWater, blockinteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ACItemRegistry.DEPTH_CHARGE.get();
    }
}