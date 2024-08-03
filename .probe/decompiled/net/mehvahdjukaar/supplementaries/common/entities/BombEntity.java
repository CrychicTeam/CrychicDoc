package net.mehvahdjukaar.supplementaries.common.entities;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.entity.IExtraClientSpawnData;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.misc.explosion.BombExplosion;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BombEntity extends ImprovedProjectileEntity implements IExtraClientSpawnData {

    private final boolean hasFuse = (Integer) CommonConfigs.Tools.BOMB_FUSE.get() != 0;

    private BombEntity.BombType type = BombEntity.BombType.NORMAL;

    private boolean active = true;

    private int changeTimer = -1;

    private boolean superCharged = false;

    public BombEntity(EntityType<? extends BombEntity> type, Level world) {
        super(type, world);
        this.maxAge = this.hasFuse ? (Integer) CommonConfigs.Tools.BOMB_FUSE.get() : 200;
    }

    public BombEntity(Level worldIn, LivingEntity throwerIn, BombEntity.BombType type) {
        super((EntityType<? extends ThrowableItemProjectile>) ModEntities.BOMB.get(), throwerIn, worldIn);
        this.type = type;
        this.maxAge = this.hasFuse ? (Integer) CommonConfigs.Tools.BOMB_FUSE.get() : 200;
    }

    public BombEntity(Level worldIn, double x, double y, double z, BombEntity.BombType type) {
        super((EntityType<? extends ThrowableItemProjectile>) ModEntities.BOMB.get(), x, y, z, worldIn);
        this.type = type;
        this.maxAge = this.hasFuse ? (Integer) CommonConfigs.Tools.BOMB_FUSE.get() : 200;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Active", this.active);
        compound.putInt("Type", this.type.ordinal());
        compound.putInt("Timer", this.changeTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.active = compound.getBoolean("Active");
        this.type = BombEntity.BombType.values()[compound.getInt("Type")];
        this.changeTimer = compound.getInt("Timer");
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.type = buffer.readEnum(BombEntity.BombType.class);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.type);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return (Item) ModRegistry.BOMB_ITEM_ON.get();
    }

    @Override
    public ItemStack getItem() {
        return this.type.getDisplayStack(this.active);
    }

    private void spawnBreakParticles() {
        for (int i = 0; i < 8; i++) {
            this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack((ItemLike) ModRegistry.BOMB_ITEM.get())), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch(id) {
            case 3:
                this.spawnBreakParticles();
                this.m_146870_();
                break;
            case 10:
                this.spawnBreakParticles();
                if (MiscUtils.FESTIVITY.isBirthday()) {
                    this.spawnParticleInASphere((ParticleOptions) ModParticles.CONFETTI_PARTICLE.get(), 55, 0.3F);
                } else {
                    this.m_9236_().addParticle((ParticleOptions) ModParticles.BOMB_EXPLOSION_PARTICLE_EMITTER.get(), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), this.type.getRadius(), 0.0, 0.0);
                }
                this.type.spawnExtraParticles(this);
                this.m_146870_();
                break;
            case 67:
                RandomSource random = this.m_9236_().getRandom();
                for (int i = 0; i < 10; i++) {
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + 0.25 - (double) (random.nextFloat() * 0.5F), this.m_20186_() + 0.45F - (double) (random.nextFloat() * 0.5F), this.m_20189_() + 0.25 - (double) (random.nextFloat() * 0.5F), 0.0, 0.005, 0.0);
                }
                this.active = false;
                break;
            case 68:
                this.m_9236_().addParticle(ParticleTypes.SONIC_BOOM, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                break;
            default:
                super.m_7822_(id);
        }
    }

    private void spawnParticleInASphere(ParticleOptions type, int amount, float speed) {
        double inclinationIncrement = Math.PI / (double) amount;
        double azimuthIncrement = Math.PI * (3.0 - Math.sqrt(5.0));
        for (int i = 0; i < amount; i++) {
            double inclination = Math.acos(1.0 - 2.0 * ((double) i + 0.5) / (double) amount);
            double azimuth = azimuthIncrement * (double) i;
            double x = (double) speed * Math.sin(inclination) * Math.cos(azimuth);
            double y = (double) speed * Math.sin(inclination) * Math.sin(azimuth);
            double z = (double) speed * Math.cos(inclination);
            this.m_9236_().addParticle(type, this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_() + z, x, y, z);
        }
    }

    @Override
    public boolean hasReachedEndOfLife() {
        return super.hasReachedEndOfLife() || this.changeTimer == 0;
    }

    @Override
    public void tick() {
        if (this.active && this.m_20069_() && this.type != BombEntity.BombType.BLUE) {
            this.turnOff();
        }
        super.tick();
    }

    @Override
    public void spawnTrailParticles() {
        Vec3 newPos = this.m_20182_();
        Vec3 currentPos = new Vec3(this.f_19854_, this.f_19855_, this.f_19856_);
        if (this.active && this.f_19797_ > 1) {
            double x = currentPos.x;
            double y = currentPos.y;
            double z = currentPos.z;
            double dx = newPos.x - x;
            double dy = newPos.y - y;
            double dz = newPos.z - z;
            int s = 4;
            for (int i = 0; i < s; i++) {
                double j = (double) i / (double) s;
                this.m_9236_().addParticle(ParticleTypes.SMOKE, x - dx * j, 0.25 + y - dy * j, z - dz * j, 0.0, 0.02, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.m_5790_(hit);
        hit.getEntity().hurt(this.m_9236_().damageSources().thrown(this, this.m_19749_()), 1.0F);
        if (hit.getEntity() instanceof LargeFireball) {
            this.superCharged = true;
            hit.getEntity().remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public void turnOff() {
        Level level = this.m_9236_();
        if (!level.isClientSide()) {
            level.broadcastEntityEvent(this, (byte) 67);
            level.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F, 1.5F);
        }
        this.active = false;
    }

    @Override
    public void playerTouch(Player entityIn) {
        if (!this.m_9236_().isClientSide && !this.active && entityIn.getInventory().add(this.getItem())) {
            entityIn.m_7938_(this, 1);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.m_8060_(hit);
        Vec3 vector3d = hit.m_82450_().subtract(this.m_20185_(), this.m_20186_(), this.m_20189_());
        this.m_20256_(vector3d);
        Vec3 vector3d1 = vector3d.normalize().scale((double) this.getGravity());
        this.m_20343_(this.m_20185_() - vector3d1.x, this.m_20186_() - vector3d1.y, this.m_20189_() - vector3d1.z);
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        Level level = this.m_9236_();
        if (!level.isClientSide && !this.hasFuse) {
            boolean isInstantlyActivated = this.type.isInstantlyActivated();
            if (!isInstantlyActivated && this.changeTimer == -1) {
                this.changeTimer = 10;
                level.broadcastEntityEvent(this, (byte) 68);
                level.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL, 1.5F, 1.3F);
            }
            if (!this.m_213877_() && (isInstantlyActivated || this.superCharged)) {
                this.reachedEndOfLife();
            }
        }
    }

    @Override
    protected void updateRotation() {
    }

    @Override
    public void reachedEndOfLife() {
        Level level = this.m_9236_();
        level.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.NETHERITE_BLOCK_BREAK, SoundSource.NEUTRAL, 1.5F, 1.5F);
        if (!level.isClientSide) {
            if (this.active) {
                this.createExplosion();
                level.broadcastEntityEvent(this, (byte) 10);
            } else {
                level.broadcastEntityEvent(this, (byte) 3);
            }
            this.m_146870_();
        }
    }

    private void createExplosion() {
        boolean breaks = this.m_19749_() instanceof Player || PlatHelper.isMobGriefingOn(this.m_9236_(), this.m_19749_());
        if (CompatHandler.FLAN && this.m_19749_() instanceof Player p && !FlanCompat.canBreak(p, BlockPos.containing(this.m_20182_()))) {
            breaks = false;
        }
        if (this.superCharged) {
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 6.0F, breaks, this.m_19749_() instanceof Player ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.MOB);
        }
        BombExplosion explosion = new BombExplosion(this.m_9236_(), this, new BombEntity.BombExplosionDamageCalculator(this.type), this.m_20185_(), this.m_20186_() + 0.25, this.m_20189_(), (float) this.type.getRadius(), this.type, breaks ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP);
        explosion.explode();
        explosion.doFinalizeExplosion();
    }

    private static class BombExplosionDamageCalculator extends ExplosionDamageCalculator {

        private final BombEntity.BombType type;

        public BombExplosionDamageCalculator(BombEntity.BombType type) {
            this.type = type;
        }

        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
            return switch(this.type.breakMode()) {
                case ALL ->
                    true;
                case WEAK ->
                    state.m_60722_(Fluids.WATER) || state.m_204336_(ModTags.BOMB_BREAKABLE) || state.m_60734_() instanceof TntBlock;
                default ->
                    false;
            };
        }
    }

    public static enum BombType {

        NORMAL(ModRegistry.BOMB_ITEM, ModRegistry.BOMB_ITEM_ON), BLUE(ModRegistry.BOMB_BLUE_ITEM, ModRegistry.BOMB_BLUE_ITEM_ON), SPIKY(ModRegistry.BOMB_SPIKY_ITEM, ModRegistry.BOMB_SPIKY_ITEM_ON);

        public final Supplier<Item> item;

        public final Supplier<Item> itemOn;

        private BombType(Supplier<Item> item, Supplier<Item> itemOn) {
            this.item = item;
            this.itemOn = itemOn;
        }

        public ItemStack getDisplayStack(boolean active) {
            return ((Item) (active ? this.itemOn : this.item).get()).getDefaultInstance();
        }

        public double getRadius() {
            return this == BLUE ? (Double) CommonConfigs.Tools.BOMB_BLUE_RADIUS.get() : (Double) CommonConfigs.Tools.BOMB_RADIUS.get();
        }

        public BombEntity.BreakingMode breakMode() {
            return this == BLUE ? (BombEntity.BreakingMode) CommonConfigs.Tools.BOMB_BLUE_BREAKS.get() : (BombEntity.BreakingMode) CommonConfigs.Tools.BOMB_BREAKS.get();
        }

        public float volume() {
            return this == BLUE ? 5.0F : 3.0F;
        }

        public void applyStatusEffects(LivingEntity entity, double distSq) {
            switch(this) {
                case BLUE:
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
                    entity.m_20254_(10);
                case SPIKY:
            }
        }

        public boolean isInstantlyActivated() {
            return this != BLUE;
        }

        public void spawnExtraParticles(BombEntity bomb) {
            switch(this) {
                case BLUE:
                    bomb.spawnParticleInASphere(ParticleTypes.FLAME, 40, 0.4F);
                    break;
                case SPIKY:
                    ParticleType<?> particle = (ParticleType<?>) CompatObjects.SHARPNEL.get();
                    if (particle instanceof ParticleOptions p) {
                        for (int i = 0; i < 80; i++) {
                            float dx = (float) (bomb.f_19796_.nextGaussian() * 2.0);
                            float dy = (float) (bomb.f_19796_.nextGaussian() * 2.0);
                            float dz = (float) (bomb.f_19796_.nextGaussian() * 2.0);
                            bomb.m_9236_().addParticle(p, bomb.m_20185_(), bomb.m_20186_() + 1.0, bomb.m_20189_(), (double) dx, (double) dy, (double) dz);
                        }
                    } else {
                        bomb.spawnParticleInASphere(ParticleTypes.CRIT, 100, 5.0F);
                    }
            }
        }

        public void afterExploded(BombExplosion exp, Level level) {
            if (this == SPIKY) {
                Vec3 pos = exp.m_46077_().getSourcePosition();
                Entity e = exp.m_252906_();
                if (e == null) {
                    return;
                }
                for (Entity entity : level.m_45933_(e, new AABB(pos.x - 30.0, pos.y - 4.0, pos.z - 30.0, pos.x + 30.0, pos.y + 4.0, pos.z + 30.0))) {
                    int random = level.random.nextInt() * 100;
                    boolean shouldPoison = false;
                    if (entity.distanceToSqr(e) <= 16.0) {
                        shouldPoison = true;
                    } else if (entity.distanceToSqr(e) <= 64.0) {
                        if (random < 60) {
                            shouldPoison = true;
                        }
                    } else if (entity.distanceToSqr(e) <= 225.0) {
                        if (random < 30) {
                            shouldPoison = true;
                        }
                    } else if (entity.distanceToSqr(e) <= 900.0 && random < 5) {
                        shouldPoison = true;
                    }
                    if (shouldPoison && entity instanceof LivingEntity livingEntity) {
                        livingEntity.hurt(level.damageSources().magic(), 2.0F);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 130));
                        MobEffect effect = (MobEffect) CompatObjects.STUNNED_EFFECT.get();
                        if (effect != null) {
                            livingEntity.addEffect(new MobEffectInstance(effect, 400));
                        }
                    }
                }
            }
        }
    }

    public static enum BreakingMode {

        ALL, WEAK, NONE
    }
}