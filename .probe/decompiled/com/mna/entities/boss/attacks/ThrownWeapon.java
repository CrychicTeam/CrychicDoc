package com.mna.entities.boss.attacks;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class ThrownWeapon extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {

    private ItemStack renderStack = ItemStack.EMPTY;

    private ParticleOptions trailParticle;

    private float renderScale = 1.0F;

    public ThrownWeapon(EntityType<? extends AbstractHurtingProjectile> type, Level world) {
        super(type, world);
        this.trailParticle = new MAParticleType(ParticleInit.FLAME.get()).setMaxAge(10);
    }

    public ThrownWeapon(EntityType<? extends AbstractHurtingProjectile> type, LivingEntity shooter, Level worldIn, ItemStack renderStack) {
        this(type, worldIn, shooter.m_20185_(), shooter.m_20188_() - 0.1F, shooter.m_20189_(), renderStack);
        this.m_5602_(shooter);
    }

    public ThrownWeapon(EntityType<? extends AbstractHurtingProjectile> type, Level worldIn, double x, double y, double z, ItemStack renderStack) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
        this.setRenderStack(renderStack);
    }

    public ThrownWeapon setRenderStack(ItemStack stack) {
        this.renderStack = stack;
        return this;
    }

    public ThrownWeapon setTrailParticle(ParticleOptions particle) {
        this.trailParticle = particle;
        return this;
    }

    public ThrownWeapon setScale(float scale) {
        this.renderScale = scale;
        return this;
    }

    public ItemStack getRenderStack() {
        return this.renderStack;
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 Vector3d = new Vec3(x, y, z).normalize().scale((double) velocity);
        this.m_20256_(Vector3d);
        double f = Vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(Vector3d.x, Vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(Vector3d.y, f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        if (ray.getEntity() instanceof LivingEntity livingHit) {
            Entity eOwner = this.m_19749_();
            if (eOwner instanceof LivingEntity) {
                livingHit.hurt(this.m_269291_().mobAttack((LivingEntity) eOwner), 15.0F);
            }
        }
        if (!this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult ray) {
        if (!this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return this.trailParticle;
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            for (int i = 0; i < 10; i++) {
                this.m_9236_().addParticle(this.trailParticle, this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.5 + Math.random(), -0.5 + Math.random(), -0.5 + Math.random());
            }
        }
    }

    @Override
    protected float getInertia() {
        return 0.95F;
    }

    @Override
    public void tick() {
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.05F, 0.0));
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItemStack(this.renderStack, true);
        buffer.writeUtf(this.trailParticle.writeToString());
        buffer.writeFloat(this.renderScale);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.renderStack = additionalData.readItem();
        String trailParticle = additionalData.readUtf();
        try {
            this.trailParticle = ParticleArgument.readParticle(new StringReader(trailParticle), this.m_9236_().m_246945_(Registries.PARTICLE_TYPE));
        } catch (CommandSyntaxException var4) {
            ManaAndArtifice.LOGGER.warn("Couldn't load custom particle {}", trailParticle, var4);
        }
        this.renderScale = additionalData.readFloat();
    }
}