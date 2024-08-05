package com.mna.entities.sorcery.targeting;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.entities.ISpellInteractibleEntity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;

public class Smite extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<CompoundTag> SPELL_RECIPE = SynchedEntityData.defineId(Smite.class, EntityDataSerializers.COMPOUND_TAG);

    private static final Vec3 DOWN = new Vec3(0.0, -1.0, 0.0);

    private List<Entity> hitEntities;

    public UUID shootingEntity;

    private int ticksInAir;

    private ArrayList<Affinity> affinities;

    private SpellRecipe _cachedSpellRecipe = null;

    private float bonusDamagePctFromHeight = 0.0F;

    private HashMap<Affinity, BiConsumer<Integer, Float>> particleFunctions = new HashMap();

    public Smite(EntityType<? extends Smite> type, Level world) {
        super(EntityInit.SMITE_PROJECTILE.get(), world);
        this.m_20242_(true);
        this.particleFunctions.put(Affinity.ARCANE, this::spawnArcaneParticles);
        this.particleFunctions.put(Affinity.ENDER, this::spawnEnderParticles);
        this.particleFunctions.put(Affinity.FIRE, this::spawnFireParticles);
        this.particleFunctions.put(Affinity.HELLFIRE, this::spawnHellfireParticles);
        this.particleFunctions.put(Affinity.LIGHTNING, this::spawnLightningParticles);
        this.particleFunctions.put(Affinity.WATER, this::spawnWaterParticles);
        this.particleFunctions.put(Affinity.ICE, this::spawnFrostParticles);
        this.particleFunctions.put(Affinity.WIND, this::spawnWindParticles);
        this.particleFunctions.put(Affinity.EARTH, this::spawnEarthParticles);
    }

    public Smite(Level worldIn, Vec3 position, CompoundTag recipe, LivingEntity shooter) {
        super(EntityInit.SMITE_PROJECTILE.get(), worldIn);
        this.m_6034_(position.x(), position.y(), position.z());
        this.m_20242_(true);
        this.setOwner(shooter);
        this.setSpellRecipe(recipe);
    }

    public ArrayList<Affinity> getAffinity() {
        if (this.affinities == null) {
            SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
            this.affinities = new ArrayList(recipe.getAffinity().keySet());
        }
        return this.affinities;
    }

    public void setBonusDamagePctFromHeight(float bonus) {
        this.bonusDamagePctFromHeight = bonus;
    }

    public void spawnParticles(int particleCount, float partialTick) {
        if (this.affinities == null) {
            SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
            this.affinities = new ArrayList(recipe.getAffinity().keySet());
        }
        if (this.affinities != null) {
            Affinity aff = (Affinity) this.affinities.get((int) (Math.random() * (double) this.affinities.size()));
            if (this.particleFunctions.containsKey(aff)) {
                ((BiConsumer) this.particleFunctions.get(aff)).accept(particleCount, partialTick);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(SPELL_RECIPE, new CompoundTag());
    }

    public void setSpellRecipe(CompoundTag recipe) {
        this.f_19804_.set(SPELL_RECIPE, recipe);
    }

    protected void entityHit(Entity entity) {
        this.applyEffect(this.m_20183_(), this.m_6350_(), entity);
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected SpellRecipe getRecipe() {
        if (this._cachedSpellRecipe == null) {
            this._cachedSpellRecipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
        }
        return this._cachedSpellRecipe;
    }

    protected void blockHit(BlockPos pos, net.minecraft.core.Direction face) {
        this.applyEffect(pos, net.minecraft.core.Direction.DOWN, null);
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    private void applyEffect(BlockPos impactPoint, net.minecraft.core.Direction face, @Nullable Entity hitEntity) {
        if (this.getShooterAsLiving() == null) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            SpellSource source = new SpellSource(this.getShooterAsLiving(), InteractionHand.MAIN_HAND);
            SpellRecipe recipe = this.getRecipe();
            if (recipe.isValid() && !this.m_9236_().isClientSide()) {
                BlockState state = this.m_9236_().getBlockState(impactPoint);
                if (state.m_60734_() instanceof ISpellInteractibleBlock && ((ISpellInteractibleBlock) state.m_60734_()).onHitBySpell(this.m_9236_(), impactPoint, recipe)) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
                if (hitEntity != null && hitEntity instanceof ISpellInteractibleEntity && ((ISpellInteractibleEntity) hitEntity).onShapeTarget(recipe, source)) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
                float spellRadius = recipe.getShape().getValue(Attribute.RADIUS);
                float spellVerticalRadius = recipe.getShape().getValue(Attribute.HEIGHT);
                SpellContext context = new SpellContext(this.m_9236_(), recipe, this);
                int fallTicks = Math.min(Math.max(this.ticksInAir - 2, 0), 20);
                float dmgBoost = (float) fallTicks * 0.03F * (1.0F + this.bonusDamagePctFromHeight);
                recipe.iterateComponents(c -> {
                    if (((SpellEffect) c.getPart()).getModifiableAttributes().stream().anyMatch(a -> a.getAttribute() == Attribute.DAMAGE)) {
                        float base = c.getValue(Attribute.DAMAGE);
                        c.setValue(Attribute.DAMAGE, base + base * dmgBoost);
                    }
                });
                int radius = (int) Math.floor((double) spellRadius);
                if (radius < 0) {
                    radius = 0;
                }
                int vRadius = (int) Math.floor((double) spellVerticalRadius) - 1;
                if (vRadius < 0) {
                    vRadius = 0;
                }
                if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
                    int delay = 1;
                    if (radius > 0 || hitEntity == null) {
                        Smite.Spiral s = new Smite.Spiral(radius * 2, radius * 2);
                        for (Point p : s.spiral()) {
                            for (int j = vRadius; j >= -vRadius; j--) {
                                BlockPos adjusted = impactPoint.offset(p.x, j, p.y);
                                int loopDelay = delay;
                                recipe.iterateComponents(c -> {
                                    if (((SpellEffect) c.getPart()).targetsBlocks() && !context.hasBlockBeenAffected((SpellEffect) c.getPart(), adjusted)) {
                                        DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedSpellEffect("smiteLoop", (int) ((float) loopDelay + c.getValue(Attribute.DELAY) * 20.0F), source, new SpellTarget(adjusted, face), c, context, false));
                                    }
                                });
                            }
                            delay++;
                        }
                    }
                }
                if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
                    List<Entity> targets = this.m_9236_().getEntities(this, new AABB(impactPoint).expandTowards((double) spellRadius, (double) spellVerticalRadius, (double) spellRadius).expandTowards((double) (-spellRadius), 0.0, (double) (-spellRadius)), e -> !e.isInvulnerable());
                    if (hitEntity != null && !targets.contains(hitEntity)) {
                        targets.add(hitEntity);
                    }
                    for (Entity target : targets) {
                        if (target != null && target != this.getShooterAsLiving()) {
                            SpellCaster.ApplyComponents(recipe, source, new SpellTarget(target).doNotOffsetFace(), context);
                        }
                    }
                }
                switch(recipe.getHighestAffinity()) {
                    case ARCANE:
                        this.m_9236_().playSound(null, impactPoint, SFX.Spell.Impact.AoE.ARCANE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
                        break;
                    case EARTH:
                        this.m_9236_().playSound(null, impactPoint, SFX.Spell.Impact.AoE.EARTH, SoundSource.PLAYERS, 1.0F, 1.0F);
                        break;
                    case ENDER:
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
                        break;
                    case FIRE:
                    case HELLFIRE:
                        this.m_9236_().playSound(null, impactPoint, SFX.Spell.Impact.AoE.EARTH, SoundSource.PLAYERS, 0.5F, 1.0F);
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    case LIGHTNING:
                    default:
                        break;
                    case WATER:
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.PLAYER_SPLASH_HIGH_SPEED, SoundSource.PLAYERS, 1.0F, 1.0F);
                        break;
                    case ICE:
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        this.m_9236_().playSound(null, impactPoint, SoundEvents.SNOW_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        break;
                    case WIND:
                        this.m_9236_().playSound(null, impactPoint, SFX.Spell.Impact.AoE.WIND, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult raytraceResultIn) {
        HitResult.Type raytraceresult$type = raytraceResultIn.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) raytraceResultIn);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockraytraceresult = (BlockHitResult) raytraceResultIn;
            this.blockHit(blockraytraceresult.getBlockPos(), blockraytraceresult.getDirection());
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
            if (recipe.isValid()) {
                this.spawnParticleBurst("", recipe.getShape().getValue(Attribute.RADIUS));
            }
        }
    }

    private LivingEntity getShooterAsLiving() {
        Entity shooter = this.getShooter();
        return shooter != null && shooter instanceof LivingEntity ? (LivingEntity) shooter : null;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Nullable
    public Entity getShooter() {
        return this.shootingEntity != null && this.m_9236_() instanceof ServerLevel ? ((ServerLevel) this.m_9236_()).getEntity(this.shootingEntity) : null;
    }

    @Override
    public void setOwner(@Nullable Entity entityIn) {
        this.shootingEntity = entityIn == null ? null : entityIn.getUUID();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (this.shootingEntity != null) {
            compound.putUUID("OwnerUUID", this.shootingEntity);
        }
        compound.putFloat("bonusFallDamagePct", this.bonusDamagePctFromHeight);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("OwnerUUID")) {
            this.shootingEntity = compound.getUUID("OwnerUUID");
        }
        if (compound.contains("bonusFallDamagePct")) {
            this.bonusDamagePctFromHeight = compound.getFloat("bonusFallDamagePct");
        }
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.m_9236_(), this, startVec, endVec, this.m_20191_().expandTowards(DOWN).inflate(1.0), p_213871_1_ -> !p_213871_1_.isSpectator() && p_213871_1_.isAlive() && p_213871_1_.isPickable() && (p_213871_1_ != this.getShooter() || this.ticksInAir >= 5));
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        this.entityHit(entity);
        if (!entity.isAlive() && this.hitEntities != null) {
            this.hitEntities.add(entity);
        }
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 3 && !this.m_9236_().isClientSide()) {
            SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
            if (recipe != null && recipe.isValid() && recipe.getHighestAffinity() == Affinity.LIGHTNING) {
                this.m_9236_().playSound(null, this.m_20183_(), SFX.Spell.Impact.AoE.LIGHTNING, SoundSource.PLAYERS, 0.5F, (float) (0.6 + Math.random() * 0.8));
            }
        }
        if (this.m_20070_()) {
            this.m_20095_();
        }
        this.ticksInAir++;
        Vec3 Vector3d2 = this.m_20182_();
        Vec3 Vector3d3 = Vector3d2.add(DOWN);
        HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(Vector3d2, Vector3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            Vector3d3 = raytraceresult.getLocation();
        }
        while (this.m_6084_()) {
            EntityHitResult entityraytraceresult = this.rayTraceEntities(Vector3d2, Vector3d3);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }
            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) raytraceresult).getEntity();
                Entity entity1 = this.getShooter();
                if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.f_19812_ = true;
            }
            if (entityraytraceresult == null) {
                break;
            }
            raytraceresult = null;
        }
        float speedFactor = 0.99F;
        if (this.m_20069_()) {
            if (this.affinities.contains(Affinity.FIRE)) {
                if (!this.m_9236_().isClientSide()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
                return;
            }
            for (int j = 0; j < 4; j++) {
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
            if (!this.affinities.contains(Affinity.WATER)) {
                speedFactor = 0.6F;
            }
        }
        Vec3 newPos = this.m_20182_().add(DOWN.scale((double) speedFactor));
        this.f_19854_ = this.m_20185_();
        this.f_19855_ = this.m_20186_();
        this.f_19856_ = this.m_20189_();
        this.m_6034_(newPos.x, newPos.y, newPos.z);
        this.m_20101_();
        if (this.f_19797_ > 200) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.m_6034_(x, y, z);
        this.m_19915_(yaw, pitch);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.m_20191_().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 64.0 * m_20150_();
        return distance < d0 * d0;
    }

    public final int getOverrideColor() {
        Entity caster = this.getShooter();
        SpellRecipe recipe = this.getRecipe();
        if (caster != null && recipe != null) {
            MutableInt color = new MutableInt(-1);
            caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> color.setValue(m.getParticleColorOverride()));
            if (color.getValue() == -1) {
                color.setValue(recipe.getParticleColorOverride());
            }
            return color.getValue();
        } else {
            return -1;
        }
    }

    public void spawnParticleBurst(String identifier, Float radius) {
        if (this.affinities != null && this.affinities.size() != 0) {
            MAParticleType particle = null;
            int count = 180;
            double angleRads = 0.0;
            double step = (Math.PI * 2) / (double) count;
            for (int i = 0; i < count; i++) {
                angleRads += step;
                Affinity affinity = (Affinity) this.affinities.get((int) Math.random() * this.affinities.size());
                switch(affinity) {
                    case ARCANE:
                        particle = ParticleInit.ARCANE.get();
                        break;
                    case EARTH:
                        particle = ParticleInit.DUST.get();
                        break;
                    case ENDER:
                        {
                            Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize().scale((double) (radius * 5.0F)).add(this.m_20182_());
                            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ENDER.get()), this.getShooterAsLiving()), dir.x, dir.y, dir.z, this.m_20185_(), this.m_20186_() + 0.25, this.m_20189_());
                            continue;
                        }
                    case FIRE:
                        particle = ParticleInit.FLAME.get();
                        break;
                    case HELLFIRE:
                        particle = ParticleInit.HELLFIRE.get();
                        break;
                    case LIGHTNING:
                        {
                            Vec3 dir = new Vec3(Math.cos(angleRads), 0.1F, Math.sin(angleRads)).normalize().scale((double) (radius * 5.0F)).add(this.m_20182_());
                            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getShooterAsLiving()), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), dir.x, dir.y + 1.0, dir.z);
                            i += count / 10;
                            angleRads += step * (double) (count / 10);
                            continue;
                        }
                    case WATER:
                        particle = ParticleInit.WATER.get();
                        break;
                    case ICE:
                        particle = ParticleInit.FROST.get();
                        break;
                    case WIND:
                        {
                            Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize();
                            Vec3 vel = dir.scale((double) (0.3F * radius));
                            dir = dir.scale(0.2);
                            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), this.getShooterAsLiving()), this.m_20185_() + dir.x, this.m_20186_() + Math.random(), this.m_20189_() + dir.z, vel.x, vel.y, vel.z);
                            break;
                        }
                    case UNKNOWN:
                    default:
                        particle = ParticleInit.SPARKLE_GRAVITY.get();
                }
                if (particle != null) {
                    Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize();
                    Vec3 vel = dir.scale((double) (0.3F * radius));
                    dir = dir.scale(0.2);
                    this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(particle), this.getShooterAsLiving()), this.m_20185_() + dir.x, this.m_20186_() + Math.random(), this.m_20189_() + dir.z, vel.x, vel.y, vel.z);
                }
            }
        }
    }

    public void spawnArcaneParticles(int particleCount, float partialTick) {
        for (int j = 0; j < particleCount; j++) {
            Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), this.getShooterAsLiving()), pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnEnderParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
        float particle_spread = 0.5F;
        for (int j = 0; j < particleCount; j++) {
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ENDER.get()), this.getShooterAsLiving()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + -2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.x, pos.y, pos.z);
        }
    }

    public void spawnEarthParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.5, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.DUST.get()), this.getShooterAsLiving()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnWaterParticles(int particleCount, float partialTick) {
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 pos = this.m_20182_().add(DOWN.scale(Math.random()));
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.05F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.WATER.get()), this.getShooterAsLiving()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnFireParticles(int particleCount, float partialTick) {
        for (int j = 0; j < particleCount; j++) {
            Vec3 pos = this.m_20182_().add(0.0, -Math.random(), 0.0);
            Vec3 velocity = new Vec3(0.0, 0.0, 0.0);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FLAME.get()), this.getShooterAsLiving()), pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnHellfireParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), this.getShooterAsLiving()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnWindParticles(int particleCount, float partialTick) {
        for (int i = 0; i < particleCount; i++) {
            Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), this.getShooterAsLiving()), pos.x, pos.y, pos.z, 0.6F * Math.random() + 0.1F, -0.2F, 0.25);
        }
    }

    public void spawnFrostParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FROST.get()), this.getShooterAsLiving()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    public void spawnLightningParticles(int particleCount, float partialTick) {
        BlockPos projected_impact = BlockPos.containing(this.m_20182_());
        for (int count = 0; this.m_9236_().m_46859_(projected_impact) && count < 30; count++) {
            projected_impact = projected_impact.below();
        }
        this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getShooterAsLiving()), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_20185_(), (double) projected_impact.m_123342_(), this.m_20189_());
    }

    private static class Spiral {

        private static final Point ORIGIN = new Point(0, 0);

        private final int width;

        private final int height;

        private Point point;

        private Smite.Spiral.Direction direction = Smite.Spiral.Direction.E;

        private List<Point> list = new ArrayList();

        public Spiral(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public List<Point> spiral() {
            this.point = ORIGIN;
            for (int steps = 1; this.list.size() < this.width * this.height; steps++) {
                this.advance(steps);
                this.advance(steps);
            }
            return this.list;
        }

        private void advance(int n) {
            for (int i = 0; i < n; i++) {
                if (this.inBounds(this.point)) {
                    this.list.add(this.point);
                }
                this.point = this.direction.advance(this.point);
            }
            this.direction = this.direction.next();
        }

        private boolean inBounds(Point p) {
            return between(-this.width / 2, this.width / 2, p.x) && between(-this.height / 2, this.height / 2, p.y);
        }

        private static boolean between(int low, int high, int n) {
            return low <= n && n <= high;
        }

        private static enum Direction {

            E(1, 0) {

                @Override
                Smite.Spiral.Direction next() {
                    return N;
                }
            }
            , N(0, 1) {

                @Override
                Smite.Spiral.Direction next() {
                    return W;
                }
            }
            , W(-1, 0) {

                @Override
                Smite.Spiral.Direction next() {
                    return S;
                }
            }
            , S(0, -1) {

                @Override
                Smite.Spiral.Direction next() {
                    return E;
                }
            }
            ;

            private int dx;

            private int dy;

            Point advance(Point point) {
                return new Point(point.x + this.dx, point.y + this.dy);
            }

            abstract Smite.Spiral.Direction next();

            private Direction(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }
    }
}