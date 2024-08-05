package com.mna.entities.sorcery.targeting;

import com.google.common.collect.Lists;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.entities.ISpellInteractibleEntity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.Odin;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;

public class SpellProjectile extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<CompoundTag> SPELL_RECIPE = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> SPECIAL_RENDER = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> SPECIAL_RENDER_PARAM = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> FORCED_AFFINITY = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> FORCED_DAMAGE = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> DISABLE_WATER_CHECK = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> HOMING_STRENGTH = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> VELOCITY = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);

    private static ItemStack[] halloween_stacks = new ItemStack[] { new ItemStack(Blocks.ZOMBIE_HEAD), new ItemStack(Blocks.CREEPER_HEAD), new ItemStack(Blocks.SKELETON_SKULL), new ItemStack(Blocks.WITHER_SKELETON_SKULL), new ItemStack(Blocks.CARVED_PUMPKIN), new ItemStack(Blocks.JACK_O_LANTERN) };

    private IntOpenHashSet piercedEntities;

    private List<Entity> hitEntities;

    public UUID shootingEntity;

    private int ticksInAir;

    private ArrayList<Affinity> affinities;

    private Optional<Integer> forcedTargetID = Optional.empty();

    private boolean trackForcedTarget = false;

    private float trackingSpeed = 0.0F;

    private SpellRecipe _cachedSpellRecipe = null;

    private LivingEntity target;

    private HashMap<Affinity, BiConsumer<Integer, Float>> particleFunctions = new HashMap();

    private HashMap<Affinity, BiConsumer<Integer, Float>> impactFunctions = new HashMap();

    public SpellProjectile(EntityType<? extends SpellProjectile> type, Level world) {
        super(EntityInit.SPELL_PROJECTILE.get(), world);
        this.m_20242_(true);
        this.forcedTargetID = Optional.empty();
        this.particleFunctions.put(Affinity.ARCANE, this::spawnArcaneParticles);
        this.particleFunctions.put(Affinity.ENDER, this::spawnEnderParticles);
        this.particleFunctions.put(Affinity.FIRE, this::spawnFireParticles);
        this.particleFunctions.put(Affinity.HELLFIRE, this::spawnHellfireParticles);
        this.particleFunctions.put(Affinity.LIGHTNING, this::spawnLightningParticles);
        this.particleFunctions.put(Affinity.WATER, this::spawnWaterParticles);
        this.particleFunctions.put(Affinity.ICE, this::spawnFrostParticles);
        this.particleFunctions.put(Affinity.WIND, this::spawnWindParticles);
        this.particleFunctions.put(Affinity.EARTH, this::spawnEarthParticles);
        this.impactFunctions.put(Affinity.WATER, this::spawnWaterImpactParticles);
        this.impactFunctions.put(Affinity.FIRE, this::spawnFireImpactParticles);
        this.impactFunctions.put(Affinity.HELLFIRE, this::spawnFireImpactParticles);
        this.impactFunctions.put(Affinity.EARTH, this::spawnEarthImpactParticles);
        this.impactFunctions.put(Affinity.WIND, this::spawnWindImpactParticles);
        this.impactFunctions.put(Affinity.ARCANE, this::spawnArcaneImpactParticles);
        this.impactFunctions.put(Affinity.ENDER, this::spawnEnderImpactParticles);
        this.impactFunctions.put(Affinity.ICE, this::spawnFrostImpactParticles);
    }

    public SpellProjectile(LivingEntity shooter, Level worldIn) {
        this(worldIn, shooter.m_20185_(), shooter.m_20188_() - 0.1F, shooter.m_20189_());
        this.setOwner(shooter);
    }

    public SpellProjectile(Level worldIn, double x, double y, double z) {
        super(EntityInit.SPELL_PROJECTILE.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20242_(true);
    }

    public ArrayList<Affinity> getAffinity() {
        if (this.affinities == null) {
            if (this.f_19804_.get(FORCED_AFFINITY) > 0) {
                this.affinities = new ArrayList();
                this.affinities.add(Affinity.values()[this.f_19804_.get(FORCED_AFFINITY)]);
            } else {
                SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
                this.affinities = new ArrayList(recipe.getAffinity().keySet());
            }
        }
        return this.affinities;
    }

    public SpellProjectile.SpecialRenderType getSpecialRenderType() {
        return SpellProjectile.SpecialRenderType.values()[this.f_19804_.get(SPECIAL_RENDER)];
    }

    public void spawnParticles(int particleCount, float partialTick) {
        if (this.getAffinity() != null) {
            Affinity aff = (Affinity) this.affinities.get((int) (Math.random() * (double) this.affinities.size()));
            if (this.particleFunctions.containsKey(aff)) {
                ((BiConsumer) this.particleFunctions.get(aff)).accept(particleCount, partialTick);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(SPELL_RECIPE, new CompoundTag());
        this.f_19804_.define(PIERCE_LEVEL, (byte) 0);
        this.f_19804_.define(FORCED_AFFINITY, (byte) 0);
        this.f_19804_.define(FORCED_DAMAGE, (byte) 0);
        this.f_19804_.define(SPECIAL_RENDER, (byte) 0);
        this.f_19804_.define(SPECIAL_RENDER_PARAM, (byte) 0);
        this.f_19804_.define(DISABLE_WATER_CHECK, false);
        this.f_19804_.define(VELOCITY, 0.2F);
        this.f_19804_.define(HOMING_STRENGTH, 0.0F);
        this.f_19804_.define(TARGET_ID, -1);
        this.f_19804_.define(OWNER_ID, -1);
    }

    public void setForcedDamageAffinityAndTarget(Affinity affinity, float damage, @Nullable Entity target) {
        this.f_19804_.set(FORCED_DAMAGE, (byte) ((int) damage));
        this.f_19804_.set(FORCED_AFFINITY, (byte) affinity.ordinal());
        this.forcedTargetID = target != null ? Optional.of(target.getId()) : Optional.empty();
    }

    public void setHellball(Entity target, float speed) {
        this.forcedTargetID = Optional.of(target.getId());
        this.trackForcedTarget = true;
        this.trackingSpeed = speed;
        this.setSpecialRender(SpellProjectile.SpecialRenderType.HELLBALL);
    }

    public void setHomingStrength(float strength) {
        this.f_19804_.set(HOMING_STRENGTH, MathUtils.clamp01(strength));
    }

    public void setSpecialRender(SpellProjectile.SpecialRenderType type) {
        this.f_19804_.set(SPECIAL_RENDER, (byte) type.ordinal());
        switch(type) {
            case HALLOWEEN:
                this.f_19804_.set(SPECIAL_RENDER_PARAM, (byte) ((int) (Math.random() * (double) halloween_stacks.length)));
            case NONE:
        }
    }

    public void setNoWaterFizzle() {
        this.f_19804_.set(DISABLE_WATER_CHECK, true);
    }

    public ItemStack getSpecialRenderStack() {
        return halloween_stacks[this.f_19804_.get(SPECIAL_RENDER_PARAM)];
    }

    public void setSpellRecipe(CompoundTag recipe) {
        this.f_19804_.set(SPELL_RECIPE, recipe);
    }

    protected SpellRecipe getRecipe() {
        if (this._cachedSpellRecipe == null) {
            this._cachedSpellRecipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
        }
        return this._cachedSpellRecipe;
    }

    public void shoot(@Nullable Entity shooter, Vec3 look, float velocity, float inaccuracy) {
        this.shoot(look.x(), look.y(), look.z(), velocity, inaccuracy);
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
        this.f_19804_.set(VELOCITY, velocity);
    }

    protected void entityHit(Entity entity) {
        if (!(entity instanceof SpellProjectile)) {
            if (!this.m_9236_().isClientSide()) {
                if (this.f_19804_.get(FORCED_DAMAGE) > 0) {
                    Entity shooter = this.getShooter();
                    if (shooter != null && shooter instanceof LivingEntity) {
                        entity.hurt(this.m_269291_().mobAttack((LivingEntity) shooter), (float) this.f_19804_.get(FORCED_DAMAGE).byteValue());
                    } else {
                        entity.hurt(entity.damageSources().magic(), (float) this.f_19804_.get(FORCED_DAMAGE).byteValue());
                    }
                } else {
                    this.applyEffect(this.m_20183_(), this.m_6350_(), entity);
                }
            }
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    protected void blockHit(BlockPos pos, Direction face) {
        if (this.f_19804_.get(FORCED_DAMAGE) <= 0 && !this.m_9236_().isClientSide()) {
            this.applyEffect(pos, face, null);
        }
        this.m_142687_(Entity.RemovalReason.KILLED);
    }

    private void applyEffect(BlockPos impactPoint, Direction face, @Nullable Entity hitEntity) {
        if (this.m_6084_()) {
            this.m_142687_(Entity.RemovalReason.KILLED);
            if (this.getShooter() != null && this.getShooter() instanceof LivingEntity) {
                SpellSource source = new SpellSource((LivingEntity) this.getShooter(), InteractionHand.MAIN_HAND);
                SpellRecipe recipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
                if (recipe.isValid() && !this.m_9236_().isClientSide()) {
                    HashMap<SpellEffect, ComponentApplicationResult> results = new HashMap();
                    BlockState state = this.m_9236_().getBlockState(impactPoint);
                    if (state.m_60734_() instanceof ISpellInteractibleBlock && ((ISpellInteractibleBlock) state.m_60734_()).onHitBySpell(this.m_9236_(), impactPoint, recipe)) {
                        return;
                    }
                    if (hitEntity != null && hitEntity instanceof ISpellInteractibleEntity && ((ISpellInteractibleEntity) hitEntity).onShapeTarget(recipe, source)) {
                        return;
                    }
                    float spellRadius = recipe.getShape().getValue(Attribute.RADIUS) * 2.0F;
                    SpellContext context = new SpellContext(this.m_9236_(), recipe, this);
                    boolean sphere = recipe.getShape().getValue(Attribute.PRECISION) == 1.0F;
                    if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
                        int radius = (int) ((Math.floor((double) spellRadius) - 1.0) / 2.0);
                        if (radius < 0) {
                            radius = 0;
                        }
                        int rSquared = sphere ? radius * radius : 0;
                        if (radius > 0 || hitEntity == null) {
                            for (int i = -radius; i <= radius; i++) {
                                int tx_squared = sphere ? i * i : 0;
                                for (int j = -radius; j <= radius; j++) {
                                    int ty_squared = sphere ? j * j : 0;
                                    for (int k = -radius; k <= radius; k++) {
                                        int tz_squared = sphere ? k * k : 0;
                                        if (!sphere || tx_squared + ty_squared + tz_squared <= rSquared) {
                                            BlockPos adjusted = impactPoint.offset(i, j, k);
                                            HashMap<SpellEffect, ComponentApplicationResult> loopRes = SpellCaster.ApplyComponents(recipe, source, new SpellTarget(adjusted, face).doNotOffsetFace(), context);
                                            SpellCaster.mergeComponentResults(results, loopRes);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
                        float radiusx = recipe.getShape().getValue(Attribute.RADIUS);
                        List<Entity> targets = this.m_9236_().getEntities(this, this.m_20191_().inflate((double) radiusx), e -> !e.isInvulnerable() && (!sphere || e.distanceTo(this) <= radius) && !(e instanceof SpellProjectile) && e.isAlive() && e != this);
                        if (hitEntity != null && !targets.contains(hitEntity)) {
                            targets.add(hitEntity);
                        }
                        for (Entity target : targets) {
                            if (target != null && target != this.getShooter()) {
                                HashMap<SpellEffect, ComponentApplicationResult> loopRes = SpellCaster.ApplyComponents(recipe, source, new SpellTarget(target), context);
                                SpellCaster.mergeComponentResults(results, loopRes);
                            }
                        }
                    }
                    if (results.size() > 0) {
                        List<SpellEffect> appliedEffects = (List<SpellEffect>) results.entrySet().stream().map(e -> e.getValue() == ComponentApplicationResult.SUCCESS ? (SpellEffect) e.getKey() : null).filter(e -> e != null).collect(Collectors.toList());
                        SpellCaster.spawnClientFX(this.m_9236_(), this.m_20182_(), this.m_20184_(), source, appliedEffects);
                    }
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult raytraceResultIn) {
        if (raytraceResultIn != null) {
            HitResult.Type raytraceresult$type = raytraceResultIn.getType();
            if (raytraceresult$type == HitResult.Type.ENTITY) {
                this.onHitEntity((EntityHitResult) raytraceResultIn);
            } else if (raytraceresult$type == HitResult.Type.BLOCK) {
                BlockHitResult blockraytraceresult = (BlockHitResult) raytraceResultIn;
                this.blockHit(blockraytraceresult.getBlockPos(), blockraytraceresult.getDirection());
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected float getWaterDrag() {
        return this.affinities.contains(Affinity.WATER) ? 1.0F : 0.6F;
    }

    public byte getPierceLevel() {
        return this.f_19804_.get(PIERCE_LEVEL);
    }

    public void setPierceLevel(byte level) {
        this.f_19804_.set(PIERCE_LEVEL, level);
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
        if (this.m_9236_() instanceof ServerLevel) {
            return this.shootingEntity != null ? ((ServerLevel) this.m_9236_()).getEntity(this.shootingEntity) : null;
        } else {
            int shooterID = this.f_19804_.get(OWNER_ID);
            return this.m_9236_().getEntity(shooterID);
        }
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

    @Override
    public void setOwner(@Nullable Entity entityIn) {
        this.shootingEntity = entityIn == null ? null : entityIn.getUUID();
        if (entityIn != null) {
            this.f_19804_.set(OWNER_ID, entityIn.getId());
        } else {
            this.f_19804_.set(OWNER_ID, -1);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("PierceLevel", this.getPierceLevel());
        if (this.shootingEntity != null) {
            compound.putUUID("OwnerUUID", this.shootingEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setPierceLevel(compound.getByte("PierceLevel"));
        if (compound.hasUUID("OwnerUUID")) {
            this.shootingEntity = compound.getUUID("OwnerUUID");
        }
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return this.getShooter() instanceof Odin ? ProjectileUtil.getEntityHitResult(this.m_9236_(), this, startVec, endVec, this.m_20191_().inflate(3.0).expandTowards(this.m_20184_()), candidate -> !candidate.isSpectator() && candidate.isAlive() && candidate.isPickable() && (candidate != this.getShooter() || this.ticksInAir >= 5) && (this.piercedEntities == null || !this.piercedEntities.contains(candidate.getId()))) : ProjectileUtil.getEntityHitResult(this.m_9236_(), this, startVec, endVec, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), candidate -> !candidate.isSpectator() && candidate.isAlive() && candidate.isPickable() && (candidate != this.getShooter() || this.ticksInAir >= 5) && (this.piercedEntities == null || !this.piercedEntities.contains(candidate.getId())));
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        if (this.getShooter() != null && entity != this.getShooter()) {
            if (this.getPierceLevel() > 0) {
                if (this.piercedEntities == null) {
                    this.piercedEntities = new IntOpenHashSet(5);
                }
                if (this.hitEntities == null) {
                    this.hitEntities = Lists.newArrayListWithCapacity(5);
                }
                if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                    this.m_142687_(Entity.RemovalReason.KILLED);
                    return;
                }
                this.piercedEntities.add(entity.getId());
            }
            if (this.forcedTargetID == null || !this.forcedTargetID.isPresent() || (Integer) this.forcedTargetID.get() == entity.getId()) {
                this.entityHit(entity);
                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(entity);
                }
                if (this.getPierceLevel() <= 0) {
                    this.m_142687_(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    public void tick() {
        Vec3 motion = this.m_20184_();
        Entity target = this.getTarget();
        if (this.forcedTargetID.isPresent()) {
            target = this.m_9236_().getEntity((Integer) this.forcedTargetID.get());
        }
        if (this.trackForcedTarget && this.forcedTargetID.isPresent()) {
            if (target == null) {
                this.applyEffect(this.m_20183_(), this.m_6350_(), null);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return;
            }
            Vec3 direction = target.position().subtract(this.m_20182_()).normalize().scale((double) this.trackingSpeed);
            this.m_20256_(direction);
            float closeEnough = 2.0F;
            if (this.m_20182_().distanceTo(target.position()) <= (double) closeEnough) {
                EntityHitResult ehr = new EntityHitResult(target);
                this.onHitEntity(ehr);
            }
        }
        float homingStrength = this.f_19804_.get(HOMING_STRENGTH);
        if (homingStrength > 0.0F) {
            if (target == null) {
                if (this.m_9236_().getGameTime() % 5L == 0L) {
                    this.target();
                }
            } else {
                Vec3 myPos = this.m_20182_();
                Vec3 theirPos = target.position().add(0.0, (double) (target.getBbHeight() / 2.0F), 0.0);
                float tickTheta = 15.0F * MathUtils.clamp01(homingStrength);
                if (tickTheta > 0.0F) {
                    Vec3 desiredHeading = theirPos.subtract(myPos).normalize();
                    Vec3 calculatedHeading = MathUtils.rotateTowards(this.m_20184_().normalize(), desiredHeading, tickTheta).normalize().scale((double) this.f_19804_.get(VELOCITY).floatValue());
                    this.m_20256_(calculatedHeading);
                }
            }
        }
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            double f = motion.horizontalDistance();
            this.m_146922_((float) (Mth.atan2(motion.x, motion.z) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(motion.y, f) * 180.0F / (float) Math.PI));
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
        }
        this.f_19798_ = this.m_9236_().m_46801_(this.m_20183_());
        if (this.m_20070_()) {
            this.m_20095_();
        }
        if (this.m_20072_() && this.getAffinity().contains(Affinity.FIRE) && !this.f_19804_.get(DISABLE_WATER_CHECK)) {
            if (!this.m_9236_().isClientSide()) {
                this.m_9236_().playSound(null, this.m_20183_(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else {
            this.ticksInAir++;
            Vec3 Vector3d2 = this.m_20182_();
            Vec3 Vector3d3 = Vector3d2.add(motion);
            HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(Vector3d2, Vector3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
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
                    if (!(entity instanceof LivingEntity)) {
                        break;
                    }
                    Entity entity1 = this.getShooter();
                    if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }
                if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.f_19812_ = true;
                    break;
                }
                if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                    break;
                }
                raytraceresult = null;
            }
            motion = this.m_20184_();
            double d3 = motion.x;
            double d4 = motion.y;
            double d0 = motion.z;
            double d5 = this.m_20185_() + d3;
            double d1 = this.m_20186_() + d4;
            double d2 = this.m_20189_() + d0;
            double f1 = motion.horizontalDistance();
            this.m_146922_((float) (Mth.atan2(d3, d0) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(d4, f1) * 180.0F / (float) Math.PI));
            while (this.m_146909_() - this.f_19860_ < -180.0F) {
                this.f_19860_ -= 360.0F;
            }
            while (this.m_146909_() - this.f_19860_ >= 180.0F) {
                this.f_19860_ += 360.0F;
            }
            while (this.m_146908_() - this.f_19859_ < -180.0F) {
                this.f_19859_ -= 360.0F;
            }
            while (this.m_146908_() - this.f_19859_ >= 180.0F) {
                this.f_19859_ += 360.0F;
            }
            this.m_146926_(Mth.lerp(0.2F, this.f_19860_, this.m_146909_()));
            this.m_146922_(Mth.lerp(0.2F, this.f_19859_, this.m_146908_()));
            float f2 = 0.99F;
            if (this.m_20069_()) {
                for (int j = 0; j < 4; j++) {
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, d5 - d3 * 0.25, d1 - d4 * 0.25, d2 - d0 * 0.25, d3, d4, d0);
                }
            }
            this.m_20256_(motion.scale((double) f2));
            if (!this.m_20068_()) {
                Vec3 Vector3d4 = this.m_20184_();
                this.m_20334_(Vector3d4.x, Vector3d4.y - 0.05F, Vector3d4.z);
            }
            this.m_6034_(d5, d1, d2);
            this.m_20101_();
            if (this.f_19797_ > 200 || this.ticksInAir > 200) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public void lookAt(Entity pEntity, float pMaxYRotIncrease, float pMaxXRotIncrease) {
        double d0 = pEntity.getX() - this.m_20185_();
        double d2 = pEntity.getZ() - this.m_20189_();
        double d1;
        if (pEntity instanceof LivingEntity livingentity) {
            d1 = livingentity.m_20188_() - this.m_20188_();
        } else {
            d1 = (pEntity.getBoundingBox().minY + pEntity.getBoundingBox().maxY) / 2.0 - this.m_20188_();
        }
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
        float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
        this.m_146926_(this.rotlerp(this.m_146909_(), f1, pMaxXRotIncrease));
        this.m_146922_(this.rotlerp(this.m_146908_(), f, pMaxYRotIncrease));
    }

    private float rotlerp(float pAngle, float pTargetAngle, float pMaxIncrease) {
        float f = Mth.wrapDegrees(pTargetAngle - pAngle);
        if (f > pMaxIncrease) {
            f = pMaxIncrease;
        }
        if (f < -pMaxIncrease) {
            f = -pMaxIncrease;
        }
        return pAngle + f;
    }

    private void target() {
        if (this.getShooter() != null && this.getShooter() instanceof LivingEntity) {
            this.target = (LivingEntity) this.m_9236_().getEntities(this, this.m_20191_().inflate(16.0), e -> {
                ClipContext ctx = new ClipContext(this.m_20182_(), e.position().add(0.0, (double) (e.getBbHeight() / 2.0F), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
                return this.m_9236_().m_45547_(ctx).getType() == HitResult.Type.BLOCK ? false : e.isAlive() && e instanceof LivingEntity && !SummonUtils.isTargetFriendly(e, (LivingEntity) this.getShooter());
            }).stream().map(e -> (LivingEntity) e).findFirst().orElse(null);
            if (this.target != null) {
                this.f_19804_.set(TARGET_ID, this.target.m_19879_());
                this.f_19797_ = 0;
            }
        }
    }

    @Nullable
    private LivingEntity getTarget() {
        if (this.target == null) {
            Entity e = this.m_9236_().getEntity(this.f_19804_.get(TARGET_ID));
            if (e instanceof LivingEntity) {
                this.target = (LivingEntity) e;
            }
        }
        return this.target;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            float f = Mth.sqrt((float) (x * x + z * z));
            this.m_146926_((float) (Mth.atan2(y, (double) f) * 180.0F / (float) Math.PI));
            this.m_146922_((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
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

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            Entity.RemovalReason reason = this.m_146911_();
            if (reason == Entity.RemovalReason.DISCARDED) {
                this.spawnImpactParticles();
            }
        }
    }

    private void spawnImpactParticles() {
        if (this.getRecipe().isValid() && this.affinities != null) {
            float radius = this.getRecipe().getShape().getValue(Attribute.RADIUS) + 1.0F;
            for (Affinity aff : this.affinities) {
                if (this.impactFunctions.containsKey(aff)) {
                    ((BiConsumer) this.impactFunctions.get(aff)).accept((int) (75.0F * radius), radius);
                }
            }
        }
    }

    private void spawnWaterImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.2F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale((Math.random() * 0.5 + 0.5) * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.DRIP.get()), this.getShooter()).setGravity(0.05F).setPhysics(true).setScale(0.125F).setMaxAge(40), particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnFireImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.1F;
        for (int j = 0; j < particleCount / 8; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)).scale(0.25));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale(0.1F).scale((double) (spread * velocityModifier));
            this.m_9236_().addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
        for (int j = 0; j < particleCount / 4; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)).scale((double) (velocityModifier / 2.0F)));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale(Math.random() * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(ParticleTypes.LAVA, particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnWindImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.2F;
        for (int j = 0; j < particleCount / 6; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale((Math.random() * 0.5 + 0.5) * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()), this.getShooter()).setScale(0.2F).setColor(10, 10, 10).setScale(0.125F).setMaxAge(4 + (int) (Math.random() * 4.0 * (double) velocityModifier)), particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
        for (int j = 0; j < particleCount / 2; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)));
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()), this.getShooter()).setScale(0.2F).setColor(10, 10, 10).setScale(0.125F).setMaxAge(4 + (int) (Math.random() * 4.0 * (double) velocityModifier)), particlePos.x, particlePos.y, particlePos.z, (double) (0.05F * velocityModifier), (double) (0.05F * velocityModifier), (double) (1.0F * velocityModifier));
        }
    }

    private void spawnArcaneImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.2F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale((Math.random() * 0.5 + 0.5) * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()), this.getShooter()).setGravity(0.05F).setPhysics(true).setScale(0.125F).setMaxAge(40), particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnEnderImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        for (int j = 0; j < particleCount; j++) {
            Vec3 particlePos = pos.add(new Vec3(1.0, 1.0, 1.0).scale(1.0 + Math.random() * 3.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)));
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ENDER.get()), this.getShooter()).setMaxAge(10 + (int) (Math.random() * 10.0)), particlePos.x, particlePos.y, particlePos.z, pos.x, pos.y, pos.z);
        }
    }

    private void spawnEarthImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.1F;
        for (int j = 0; j < particleCount / 4; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)).scale(Math.random() * (double) velocityModifier));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale(Math.random() * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.EARTH.get()), this.getShooter()).setScale(0.125F).setMaxAge(40).setGravity(0.01F).setPhysics(true), particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnFrostImpactParticles(int particleCount, float velocityModifier) {
        Vec3 pos = this.m_20182_();
        float spread = 0.1F;
        for (int j = 0; j < particleCount / 4; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)).scale(Math.random() * (double) velocityModifier));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale(Math.random() * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FROST.get()), this.getShooter()).setScale(0.05F).setMaxAge(60).setGravity(0.01F).setPhysics(true), particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
        for (int j = 0; j < particleCount / 4; j++) {
            Vec3 particlePos = pos.add(this.m_20156_().multiply(-1.0, -1.0, -1.0).xRot((float) (Math.random() * 360.0)).yRot((float) (Math.random() * 360.0)).zRot((float) (Math.random() * 360.0)).scale(Math.random() * (double) velocityModifier));
            Vec3 velocity = particlePos.subtract(pos).normalize().scale(Math.random() * (double) spread * (double) velocityModifier);
            this.m_9236_().addParticle(ParticleTypes.SNOWFLAKE, particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }
        for (int j = 0; j < particleCount / 16; j++) {
            Vec3 particlePos = pos.add(new Vec3(-0.5 + Math.random(), 0.0, -0.5 + Math.random()).scale((double) velocityModifier));
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FROST.get()), this.getShooter()).setScale(0.1F * velocityModifier).setColor(140, 150, 160, 64).setMaxAge(60), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
        }
    }

    private void spawnArcaneParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float spread = 0.05F;
        float spread_2 = 0.1F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3((double) (-spread) + Math.random() * (double) spread_2, (double) (-spread) + Math.random() * (double) spread, (double) (-spread) + Math.random() * (double) spread_2);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), this.getShooter()).setGravity(0.005F).setPhysics(true).setScale(0.025F), pos.x + (double) (-spread) + Math.random() * (double) spread * 2.0, pos.y + (double) (-spread) + Math.random() * (double) spread * 2.0, pos.z + (double) (-spread) + Math.random() * (double) spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnEnderParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount; j++) {
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.ENDER.get()).setMaxAge(15), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0);
        }
    }

    private void spawnEarthParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        this.m_9236_().addParticle(new MAParticleType(ParticleInit.EARTH.get()).setColor(0.12156863F, 0.44313726F, 0.12156863F), pos.x + (double) (-particle_spread + this.m_9236_().getRandom().nextFloat() * particle_spread * 2.0F), pos.y + (double) (-particle_spread + this.m_9236_().getRandom().nextFloat() * particle_spread * 2.0F), pos.z + (double) (-particle_spread + this.m_9236_().getRandom().nextFloat() * particle_spread * 2.0F), 0.0, 0.0, 0.0);
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.DUST.get()), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnWaterParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        float velocity_spread = 0.025F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3((double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, Math.random() * (double) velocity_spread, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.DRIP.get()).setGravity(0.025F).setPhysics(true).setScale(0.05F), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnFireParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        float velocity_spread = 0.025F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3((double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FLAME.get()).setScale(0.01F), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnHellfireParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        float velocity_spread = 0.025F;
        float scale = this.getSpecialRenderType() == SpellProjectile.SpecialRenderType.HELLBALL ? 0.05F : 0.01F;
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3((double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()).setScale(scale), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnLightningParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        double radius = 0.25;
        Vec3 motion = this.m_20184_();
        int particleLife = (int) (5.0 + Math.random() * 5.0);
        this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()).setMaxAge(particleLife), this.getShooter()), pos.x - radius + Math.random() * radius * 2.0, pos.y - radius + Math.random() * radius * 2.0, pos.z - radius + Math.random() * radius * 2.0, pos.x + motion.x * (double) particleLife / 2.0, pos.y + motion.y * (double) particleLife / 2.0, pos.z + motion.z * (double) particleLife / 2.0);
    }

    private void spawnWindParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        float velocity_spread = 0.025F;
        for (int i = 0; i < particleCount; i++) {
            Vec3 vSpread = new Vec3((double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0, (double) (-velocity_spread) + Math.random() * (double) velocity_spread * 2.0);
            Vec3 velocity = this.m_20184_().scale(-0.2F).add(vSpread);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setMaxAge(10).setScale(0.2F).setColor(3, 3, 3), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0 + this.m_20184_().x * Math.random(), pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0 + this.m_20184_().y * Math.random(), pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0 + this.m_20184_().z * Math.random(), velocity.x, velocity.y, velocity.z);
        }
    }

    private void spawnFrostParticles(int particleCount, float partialTick) {
        Vec3 pos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        for (int j = 0; j < particleCount / 2; j++) {
            Vec3 velocity = new Vec3(-0.01F + Math.random() * 0.02F, -0.05F + Math.random() * 0.05F, -0.01F + Math.random() * 0.02F);
            this.m_9236_().addParticle(this.getRecipe().colorParticle(new MAParticleType(ParticleInit.FROST.get()).setScale(0.01F).setGravity(0.005F).setPhysics(true), this.getShooter()), pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    public static enum SpecialRenderType {

        NONE, HALLOWEEN, HELLBALL
    }
}