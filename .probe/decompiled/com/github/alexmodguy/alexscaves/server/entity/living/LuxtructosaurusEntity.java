package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.FissurePrimalMagmaBlock;
import com.github.alexmodguy.alexscaves.server.entity.ai.LookForwardsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.LuxtructosaurusMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.ACBossEvent;
import com.github.alexmodguy.alexscaves.server.entity.util.KaijuMob;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.message.UpdateBossEruptionStatus;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.misc.VoronoiGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class LuxtructosaurusEntity extends SauropodBaseEntity implements Enemy {

    private static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(LuxtructosaurusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final VoronoiGenerator VORONOI_GENERATOR = new VoronoiGenerator(42L);

    private float prevEnragedProgress = 0.0F;

    private float enragedProgress = 0.0F;

    public Vec3 jumpTarget = null;

    public int enragedFor = 0;

    private BlockPos lastStompPos;

    private int postStopTicks;

    private boolean stompMakesFissures;

    private boolean prevOnGround;

    private final ACBossEvent bossEvent = (ACBossEvent) new ACBossEvent(this.m_5446_(), 0).m_7005_(true);

    private int reducedDamageTicks;

    private boolean collectedLoot = false;

    private List<ItemStack> deathItems = new ArrayList();

    private int lastScareTimestamp;

    public LuxtructosaurusEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
        VORONOI_GENERATOR.setOffsetAmount(1.0);
        VORONOI_GENERATOR.setDistanceType(VoronoiGenerator.DistanceType.euclidean);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new LuxtructosaurusMeleeGoal(this));
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 10) {

            @Override
            protected Vec3 getPosition() {
                return DefaultRandomPos.getPos(this.f_25725_, 30, 7);
            }
        });
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 10.0F));
        this.f_21345_.addGoal(6, new LookForwardsGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, LuxtructosaurusEntity.class));
        this.f_21346_.addGoal(2, new MobTarget3DGoal(this, Player.class, false));
        this.f_21346_.addGoal(3, new MobTarget3DGoal(this, DinosaurEntity.class, false, 200, dinosaur -> !(dinosaur instanceof LuxtructosaurusEntity)));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ENRAGED, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.325).add(Attributes.MAX_HEALTH, 600.0).add(Attributes.ARMOR, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_DAMAGE, 12.0).add(Attributes.FOLLOW_RANGE, 256.0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.m_7378_(compoundTag);
        if (this.m_8077_()) {
            this.bossEvent.m_6456_(this.m_5446_());
        }
    }

    @Override
    public int getMaxFallDistance() {
        return super.m_6056_() + 10;
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.m_6593_(name);
        this.bossEvent.m_6456_(this.m_5446_());
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public BlockState createEggBlockState() {
        return Blocks.AIR.defaultBlockState();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevEnragedProgress = this.enragedProgress;
        if (this.isEnraged() && this.enragedProgress < 20.0F) {
            this.enragedProgress++;
        }
        if (!this.isEnraged() && this.enragedProgress > 0.0F) {
            this.enragedProgress--;
        }
        if (this.m_9236_().isClientSide) {
            if (this.m_6084_()) {
                if (this.isEnraged()) {
                    if (this.f_19796_.nextInt(8) == 0) {
                        this.m_9236_().addParticle(ACParticleRegistry.LUXTRUCTOSAURUS_SPIT.get(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
                    }
                    if (this.getAnimation() == ANIMATION_RIGHT_WHIP || this.getAnimation() == ANIMATION_LEFT_WHIP) {
                        float tailPitch = this.tailPart1.calculateAnimationAngle(1.0F, true) + this.tailPart2.calculateAnimationAngle(1.0F, true) + this.tailPart3.calculateAnimationAngle(1.0F, true);
                        float tailYaw = this.f_20883_ + this.tailPart1.calculateAnimationAngle(1.0F, false) + this.tailPart2.calculateAnimationAngle(1.0F, false) + this.tailPart3.calculateAnimationAngle(1.0F, false);
                        Vec3 tailOffset = this.rotateOffsetVec(new Vec3((double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (0.5F + (this.f_19796_.nextFloat() - 0.5F) * 0.2F), (double) (-2.0F + (this.f_19796_.nextFloat() - 0.5F) * 2.0F)), tailPitch, tailYaw);
                        Vec3 tailCenter = this.tailPart3.centeredPosition().add(tailOffset);
                        this.m_9236_().addParticle(ACParticleRegistry.TEPHRA_FLAME.get(), tailCenter.x, tailCenter.y, tailCenter.z, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.1F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
                    }
                }
                if (this.getAnimation() == ANIMATION_SPEW_FLAMES && (float) this.getAnimationTick() > 10.0F && (float) this.getAnimationTick() < 70.0F) {
                    Vec3 headPos = this.headPart.centeredPosition().add((double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
                    float flameRot = this.f_20883_ + (this.neckPart1.calculateAnimationAngle(1.0F, false) + this.neckPart2.calculateAnimationAngle(1.0F, false) + this.neckPart3.calculateAnimationAngle(1.0F, false)) / 3.0F;
                    for (int i = -3; i <= 3; i++) {
                        Vec3 flameDelta = this.rotateOffsetVec(new Vec3(0.0, (double) (this.f_19796_.nextFloat() * 0.2F - 0.1F), (double) (this.f_19796_.nextFloat() * 0.5F + 0.5F)), (this.f_19796_.nextFloat() - 0.5F) * 5.0F, 180.0F + flameRot + (float) (i * 10));
                        this.m_9236_().addParticle(ACParticleRegistry.TEPHRA_FLAME.get(), headPos.x, headPos.y, headPos.z, flameDelta.x, flameDelta.y, flameDelta.z);
                    }
                }
                if (this.getAnimation() == ANIMATION_JUMP && this.getAnimationTick() > 25 && this.m_20096_() && this.getAnimationTick() > 25 && this.screenShakeAmount < 3.0F) {
                    this.screenShakeAmount = 3.0F;
                }
            }
        } else {
            if (this.getAnimation() == ANIMATION_JUMP) {
                if (this.getAnimationTick() >= 15 && this.getAnimationTick() <= 25 && this.jumpTarget != null) {
                    Vec3 vec3 = this.m_20184_();
                    Vec3 vec31 = new Vec3(this.jumpTarget.x - this.m_20185_(), 0.0, this.jumpTarget.z - this.m_20189_());
                    if (vec31.length() > 250.0) {
                        vec31 = vec3.normalize().scale(250.0);
                    }
                    if (vec31.lengthSqr() > 1.0E-7) {
                        vec31 = vec31.scale(0.155F).add(vec3.scale(0.2));
                    }
                    this.m_20334_(vec31.x, (double) ((float) (10 - Math.min(this.getAnimationTick() - 10, 10)) * 0.2F) + vec31.length() * 0.3F, vec31.z);
                } else {
                    if (this.m_20096_() && !this.prevOnGround) {
                        this.hurtEntitiesAround(this.m_20182_(), 10.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 0.5F, 2.0F, false, false);
                    }
                    this.m_20256_(this.m_20184_().subtract(0.0, 0.2, 0.0));
                }
            }
            if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() >= 10 && this.getAnimationTick() < 55 && this.getAnimationTick() % 5 == 0 && this.m_6084_()) {
                BlockPos tephraSpawnAt = this.m_20183_().offset(this.f_19796_.nextInt(20) - 10, 2, this.f_19796_.nextInt(20) - 10);
                while (tephraSpawnAt.m_123342_() < Math.min(this.m_9236_().m_151558_(), this.m_146904_() + 100) && !this.m_9236_().getBlockState(tephraSpawnAt).m_280296_()) {
                    tephraSpawnAt = tephraSpawnAt.above();
                }
                tephraSpawnAt = tephraSpawnAt.below();
                TephraEntity tephra = new TephraEntity(this.m_9236_(), this);
                tephra.m_146884_(tephraSpawnAt.getCenter());
                tephra.setMaxScale(1.0F + 2.0F * this.m_9236_().random.nextFloat());
                Vec3 targetVec = new Vec3((double) (this.m_9236_().random.nextFloat() - 0.5F), -1.0, (double) (this.m_9236_().random.nextFloat() - 0.5F)).normalize().scale((double) (this.m_9236_().random.nextInt(20) + 20));
                tephra.m_6686_(targetVec.x, targetVec.y, targetVec.z, 5.0F + this.m_9236_().random.nextFloat() * 2.0F, 1.0F + this.m_9236_().random.nextFloat() * 0.5F);
                this.m_9236_().m_7967_(tephra);
            }
            if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 30 && this.postStopTicks <= 0) {
                this.stompMakesFissures = this.isEnraged();
                this.postStopTicks = this.stompMakesFissures ? 15 : 50;
                this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_STOMP.get(), 3.0F, 1.0F);
                this.lastStompPos = this.m_20183_();
                this.hurtEntitiesAround(this.m_20182_(), 10.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 1.2F, 2.0F, false, false);
            }
            if (this.getAnimation() == ANIMATION_RIGHT_KICK && this.getAnimationTick() == 8) {
                Vec3 armPos = this.m_20182_().add(this.rotateOffsetVec(new Vec3(-2.0, 0.0, 2.5), 0.0F, this.f_20883_));
                this.hurtEntitiesAround(armPos, 5.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 0.8F, 2.0F, false, false);
            }
            if (this.getAnimation() == ANIMATION_LEFT_KICK && this.getAnimationTick() == 8) {
                Vec3 armPos = this.m_20182_().add(this.rotateOffsetVec(new Vec3(2.0, 0.0, 2.5), 0.0F, this.f_20883_));
                this.hurtEntitiesAround(armPos, 5.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 0.8F, 2.0F, false, false);
            }
            if ((this.getAnimation() == ANIMATION_LEFT_WHIP || this.getAnimation() == ANIMATION_RIGHT_WHIP) && this.getAnimationTick() > 20 && this.getAnimationTick() < 30) {
                this.hurtEntitiesAround(this.tailPart2.m_20182_(), 12.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE), 2.0F, this.isEnraged(), true);
            }
            if (this.getAnimation() == ANIMATION_SPEW_FLAMES && this.getAnimationTick() > 10 && this.getAnimationTick() < 70) {
                this.burnWithBreath(13.0F);
            }
            if (this.postStopTicks > 0) {
                this.postStopTicks--;
                if (this.screenShakeAmount < 3.0F) {
                    this.screenShakeAmount = 3.0F;
                }
                this.tickStompAttack();
            }
            if (this.reducedDamageTicks > 0) {
                this.reducedDamageTicks--;
            }
            if (!this.m_21525_() && this.f_19797_ % 10 == 0) {
                LivingEntity target = this.m_5448_();
                Player closestPlayer = this.m_9236_().m_45930_(this, 150.0);
                if (target != null && target.isAlive() && !(target instanceof Player) && closestPlayer == null) {
                    this.m_5634_(6.0F);
                }
            }
        }
        if (!this.m_21525_()) {
            if (this.f_19797_ % 60 == 0 && this.getAnimation() != ANIMATION_ROAR && this.m_6084_()) {
                if (this.m_9236_().isClientSide) {
                    Vec3 headCenter = this.headPart.centeredPosition();
                    Vec3 nostilRightDelta = this.rotateOffsetVec(new Vec3(-0.25, 0.5, 0.75), this.m_146909_(), this.m_6080_());
                    Vec3 nostilRight = headCenter.add(nostilRightDelta);
                    Vec3 nostilLeftDelta = this.rotateOffsetVec(new Vec3(0.25, 0.5, 0.75), this.m_146909_(), this.m_6080_());
                    Vec3 nostilLeft = headCenter.add(nostilLeftDelta);
                    nostilRightDelta = nostilRightDelta.scale(0.1F);
                    nostilLeftDelta = nostilLeftDelta.scale(0.1F);
                    ParticleOptions types = ACParticleRegistry.TEPHRA_SMALL.get();
                    this.m_9236_().addParticle(types, nostilRight.x, nostilRight.y, nostilRight.z, nostilRightDelta.x, nostilRightDelta.y, nostilRightDelta.z);
                    this.m_9236_().addParticle(types, nostilLeft.x, nostilLeft.y, nostilLeft.z, nostilLeftDelta.x, nostilLeftDelta.y, nostilLeftDelta.z);
                }
                this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_SNORT.get(), this.getSoundVolume(), this.m_6100_());
            }
            if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() > 10 && this.getAnimationTick() < 50 && !this.m_9236_().isClientSide) {
                this.scareMobs();
            }
        }
        if (this.getAnimation() == ANIMATION_SUMMON && this.getAnimationTick() > 5 && !this.m_21023_(MobEffects.INVISIBILITY)) {
            this.m_6842_(false);
            this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_SUMMON.get(), 3.0F, 1.0F);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 2 && this.m_6084_()) {
            this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_ROAR.get(), 5.0F, this.m_6100_());
        }
        if (this.getAnimation() == ANIMATION_SPEW_FLAMES && this.getAnimationTick() == 10) {
            this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_BREATH.get(), 5.0F, this.m_6100_());
        }
        this.prevOnGround = this.m_20096_();
    }

    @Override
    protected void tickDeath() {
        if (this.f_20919_ <= 0) {
            this.toggleServerEruptionStatus(false);
        }
        this.f_20919_++;
        this.setAnimation(ANIMATION_EPIC_DEATH);
        this.setEnraged(true);
        this.screenShakeAmount = 0.0F;
        this.m_146926_(0.0F);
        this.m_5616_(this.m_146908_());
        if (!this.m_9236_().isClientSide && !this.collectedLoot) {
            this.populateDeathLootForLuxtructosaurus();
        }
        if (this.getAnimation() == ANIMATION_EPIC_DEATH) {
            if (this.getAnimationTick() >= 100 && this.getAnimationTick() <= 110 && this.m_9236_().isClientSide) {
                for (int i = 0; i < 50; i++) {
                    this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.LUXTRUCTOSAURUS_ASH.get(), true, this.m_20185_(), this.m_20186_(), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
                }
            }
            if (this.getAnimationTick() > 110 && !this.m_9236_().isClientSide() & !this.m_213877_()) {
                if (!this.deathItems.isEmpty()) {
                    ItemStack currentStack = ItemStack.EMPTY;
                    while (!this.deathItems.isEmpty() || !currentStack.isEmpty()) {
                        if (currentStack.isEmpty()) {
                            currentStack = (ItemStack) this.deathItems.remove(0);
                        }
                        if (currentStack.getCount() > 0) {
                            ItemStack one = currentStack.copy();
                            one.setCount(1);
                            currentStack.shrink(1);
                            this.spawnAtLocation(one);
                        }
                    }
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 60);
                this.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    private void populateDeathLootForLuxtructosaurus() {
        ResourceLocation resourcelocation = this.m_5743_();
        DamageSource damageSource = this.m_21225_();
        if (damageSource != null) {
            LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(resourcelocation);
            LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.m_20182_()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());
            if (this.f_20888_ != null) {
                lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.f_20888_).withLuck(this.f_20888_.getLuck());
            }
            LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
            loottable.getRandomItems(lootparams, this.m_287233_(), this.deathItems::add);
        }
        this.collectedLoot = true;
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itementity = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), stack);
        if (itementity != null) {
            Vec3 centerOfMob = this.m_20182_().add((double) (3.0F - 6.0F * this.f_19796_.nextFloat()), 4.0, (double) (3.0F - 6.0F * this.f_19796_.nextFloat()));
            Vec3 randomDelta = new Vec3((double) (1.0F - this.f_19796_.nextFloat()), 0.0, (double) (1.0F - this.f_19796_.nextFloat())).normalize().scale((double) (this.f_19796_.nextFloat() * 0.4F + 0.4F)).add(0.0, 0.2, 0.0);
            itementity.m_146884_(centerOfMob);
            itementity.m_20256_(randomDelta);
            itementity.m_146915_(true);
            itementity.setDefaultPickUpDelay();
            itementity.setUnlimitedLifetime();
        }
        this.m_9236_().m_7967_(itementity);
        return itementity;
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean b) {
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            this.bossEvent.m_142711_(this.m_21223_() / this.m_21233_());
            if (this.enragedFor > 0) {
                this.enragedFor--;
            } else if (this.isEnraged() && (double) this.m_21223_() >= Math.ceil((double) (this.m_21233_() * 0.25F))) {
                this.setEnraged(false);
            }
            if (this.f_19862_ || this.m_20069_() || ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                AABB aabb = this.m_20191_().inflate(0.2);
                if (this.getAnimation() == ANIMATION_JUMP && this.getAnimationTick() > 24 && this.m_20096_()) {
                    return;
                }
                for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX - 1.0), Mth.floor(aabb.minY - 1.0), Mth.floor(aabb.minZ - 1.0), Mth.ceil(aabb.maxX + 1.0), Mth.ceil(aabb.maxY + 2.0), Mth.ceil(aabb.maxZ + 1.0))) {
                    BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                    if (blockstate.m_204336_(ACTagRegistry.LUXTRUCTOSAURUS_BREAKS)) {
                        this.m_9236_().m_46953_(blockpos, true, this);
                    }
                    if (blockstate.m_60819_().is(FluidTags.WATER)) {
                        this.m_9236_().setBlock(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent(this.m_9236_(), blockpos, blockpos, Blocks.STONE.defaultBlockState()), 3);
                        this.m_9236_().m_46796_(1501, blockpos, 0);
                    }
                }
            }
        }
    }

    @Override
    protected void onStep() {
        if (this.screenShakeAmount <= 1.0F) {
            this.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_STEP.get(), 4.0F, 1.0F);
        }
        if (this.screenShakeAmount <= 3.0F) {
            this.screenShakeAmount = 3.0F;
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.m_6457_(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
        this.toggleServerEruptionStatus(true);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.m_6452_(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
        if (this.bossEvent.m_8324_().isEmpty()) {
            this.toggleServerEruptionStatus(false);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
        if (!this.m_20365_(entity) && !entity.noPhysics && !this.f_19794_) {
            double d0 = entity.getX() - this.m_20185_();
            double d1 = entity.getZ() - this.m_20189_();
            double d2 = Mth.absMax(d0, d1);
            if (d2 >= 0.01F) {
                d2 = Math.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0 / d2;
                if (d3 > 1.0) {
                    d3 = 1.0;
                }
                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05F;
                d1 *= 0.05F;
                if (!entity.isVehicle() && (entity.isPushable() || entity instanceof KaijuMob)) {
                    entity.push(d0, 0.0, d1);
                }
            }
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean canFeelShake(Entity player) {
        return player.onGround() || this.getAnimation() == ANIMATION_ROAR && this.m_6084_();
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        super.m_142687_(removalReason);
        this.toggleServerEruptionStatus(false);
    }

    private void toggleServerEruptionStatus(boolean erupting) {
        if (!this.m_9236_().isClientSide) {
            ACWorldData worldData = ACWorldData.get(this.m_9236_());
            if (worldData != null) {
                worldData.trackPrimordialBoss(this.m_19879_(), erupting);
                AlexsCaves.sendMSGToAll(new UpdateBossEruptionStatus(this.m_19879_(), worldData.isPrimordialBossActive(this.m_9236_())));
            }
        }
    }

    public boolean isLoadedInWorld() {
        return this.m_9236_().m_7232_(SectionPos.blockToSectionCoord(this.m_20185_()), SectionPos.blockToSectionCoord(this.m_20189_()));
    }

    public boolean isEnraged() {
        return this.f_19804_.get(ENRAGED);
    }

    public void setEnraged(boolean enraged) {
        this.f_19804_.set(ENRAGED, enraged);
    }

    public float getEnragedProgress(float partialTicks) {
        return (this.prevEnragedProgress + (this.enragedProgress - this.prevEnragedProgress) * partialTicks) * 0.05F;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isDancing() {
        return false;
    }

    private void tickStompAttack() {
        BlockPos pos = this.lastStompPos == null ? this.m_20183_() : this.lastStompPos;
        if (this.stompMakesFissures) {
            int fissureProgress = Math.max(0, (int) Math.ceil((double) ((float) (20 - this.postStopTicks) * 2.3F)));
            this.placeFissureRing(1 + fissureProgress, pos.m_123341_(), pos.m_123343_());
        } else if (this.postStopTicks >= 15) {
            this.crushBlocksInRing(12, pos.m_123341_(), pos.m_123343_(), 0.2F);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageAmount) {
        if (this.reducedDamageTicks > 0) {
            damageAmount *= 0.35F;
        }
        if (damageSource.getDirectEntity() instanceof DinosaurEntity && !(damageSource.getDirectEntity() instanceof TremorzillaEntity)) {
            damageAmount = (float) ((double) damageAmount * 0.65);
        }
        if (damageSource.getEntity() instanceof AbstractGolem) {
            damageAmount *= 0.5F;
        }
        if (damageSource.getEntity() instanceof Warden) {
            damageAmount *= 0.25F;
        }
        boolean prev = super.hurt(damageSource, damageAmount);
        if (prev && this.reducedDamageTicks == 0) {
            this.reducedDamageTicks = 10;
        }
        return prev;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource.is(DamageTypes.IN_WALL) | super.isInvulnerableTo(damageSource);
    }

    private void scareMobs() {
        if (this.f_19797_ - this.lastScareTimestamp > 5) {
            this.lastScareTimestamp = this.f_19797_;
        }
        for (LivingEntity e : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(64.0, 20.0, 64.0))) {
            if (!e.m_6095_().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR) && !this.m_7307_(e)) {
                if (e instanceof PathfinderMob) {
                    PathfinderMob mob = (PathfinderMob) e;
                    if (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose()) {
                        mob.m_6710_(null);
                        mob.m_6703_(null);
                        if (mob.m_20096_()) {
                            Vec3 randomShake = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), 0.0, (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.1F);
                            mob.m_20256_(mob.m_20184_().multiply(0.7F, 1.0, 0.7F).add(randomShake));
                        }
                        if (this.lastScareTimestamp == this.f_19797_) {
                            mob.m_21573_().stop();
                        }
                        if (mob.m_21573_().isDone()) {
                            Vec3 vec = LandRandomPos.getPosAway(mob, 30, 7, this.m_20182_());
                            if (vec != null) {
                                mob.m_21573_().moveTo(vec.x, vec.y, vec.z, 2.0);
                            }
                        }
                    }
                }
                if (this.m_21824_()) {
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0, true, true));
                }
            }
        }
    }

    private void placeFissureRing(int width, int fissureStartX, int fissureStartZ) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int eyeBlockY = (int) this.m_20227_(0.5);
        BlockPos center = new BlockPos(fissureStartX, eyeBlockY, fissureStartZ);
        for (int i = -width - 1; i <= width + 1; i++) {
            for (int j = -width - 1; j <= width + 1; j++) {
                mutableBlockPos.set(this.m_146903_() + i, eyeBlockY, this.m_146907_() + j);
                double dist = Math.sqrt(mutableBlockPos.m_123331_(center));
                if (dist <= (double) (width + 2) && dist > (double) (width - 2) && this.m_9236_().isLoaded(mutableBlockPos)) {
                    while (this.canFissureMoveThrough(mutableBlockPos)) {
                        mutableBlockPos.move(0, -1, 0);
                    }
                    mutableBlockPos.move(0, 1, 0);
                    if (this.placeFissureBlock(mutableBlockPos)) {
                        ((ServerLevel) this.m_9236_()).sendParticles(ACParticleRegistry.MUSHROOM_CLOUD_EXPLOSION.get(), (double) ((float) mutableBlockPos.m_123341_() + this.f_19796_.nextFloat()), (double) ((float) mutableBlockPos.m_123342_() + 0.5F + this.f_19796_.nextFloat()), (double) ((float) mutableBlockPos.m_123343_() + this.f_19796_.nextFloat()), 0, 0.0, 0.0, 0.0, 1.0);
                    }
                }
            }
        }
    }

    private boolean placeFissureBlock(BlockPos.MutableBlockPos blockPos) {
        float sampleScale = 0.08F;
        int depth = 4;
        VoronoiGenerator.VoronoiInfo info = VORONOI_GENERATOR.get2((double) ((float) blockPos.m_123341_() * sampleScale), (double) ((float) blockPos.m_123343_() * sampleScale));
        boolean flag = false;
        if (info.distance1() - (double) (sampleScale * 4.0F) < info.distance()) {
            if (!ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                return true;
            }
            int y = blockPos.m_123342_();
            for (int i = 0; i <= depth; i++) {
                BlockState state = this.m_9236_().getBlockState(blockPos);
                if (blockPos.m_123342_() <= this.m_9236_().m_141937_() || state.m_204336_(ACTagRegistry.UNMOVEABLE) || state.m_60713_(ACBlockRegistry.FISSURE_PRIMAL_MAGMA.get())) {
                    break;
                }
                if (i < depth && !state.m_204336_(ACTagRegistry.REGENERATES_AFTER_PRIMORDIAL_BOSS_FIGHT)) {
                    this.m_9236_().m_46961_(blockPos, true);
                } else {
                    this.m_9236_().setBlockAndUpdate(blockPos, i == depth ? (BlockState) ACBlockRegistry.FISSURE_PRIMAL_MAGMA.get().defaultBlockState().m_61124_(FissurePrimalMagmaBlock.REGEN_HEIGHT, Mth.clamp(i - 1, 0, 4)) : Blocks.AIR.defaultBlockState());
                }
                flag = true;
                blockPos.move(0, -1, 0);
            }
            blockPos.setY(y);
        }
        return flag;
    }

    private boolean canFissureMoveThrough(BlockPos.MutableBlockPos blockPos) {
        if (blockPos.m_123342_() <= this.m_9236_().m_141937_()) {
            return false;
        } else {
            BlockState state = this.m_9236_().getBlockState(blockPos);
            return !state.m_280296_() || state.m_204336_(BlockTags.LEAVES) || state.m_204336_(BlockTags.LOGS) || !state.m_60838_(this.m_9236_(), blockPos);
        }
    }

    @Override
    public float getProjectileDamageReduction() {
        return 0.55F;
    }

    @Override
    public int getAltSkinForItem(ItemStack stack) {
        return 0;
    }

    private void burnWithBreath(float maxDistance) {
        float distanceBurned = 0.0F;
        float burnWidth = 1.0F;
        Vec3 headPos = this.headPart.centeredPosition();
        for (float burnAngle = this.f_20883_ + this.neckYRot; distanceBurned < maxDistance; distanceBurned += burnWidth) {
            burnWidth++;
            Vec3 burnPos = headPos.add(this.rotateOffsetVec(new Vec3(0.0, 0.0, (double) distanceBurned), 0.0F, burnAngle));
            if (this.f_19796_.nextFloat() < 0.5F * (1.0F - (maxDistance - distanceBurned) / maxDistance)) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
                pos.set(burnPos.x + (double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * distanceBurned), burnPos.y, burnPos.z + (double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * distanceBurned));
                while (this.canFissureMoveThrough(pos)) {
                    pos.move(0, -1, 0);
                }
                pos.move(0, 1, 0);
                if (this.m_9236_().getBlockState(pos).m_247087_()) {
                    this.m_9236_().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                }
            }
            this.hurtEntitiesAround(burnPos, burnWidth, 3.0F, 0.3F, true, false);
        }
    }

    @Override
    protected void createWitherRose(@Nullable LivingEntity living) {
        if (living != null) {
            ACWorldData worldData = ACWorldData.get(this.m_9236_());
            if (worldData != null) {
                boolean prev = worldData.isPrimordialBossDefeatedOnce();
                worldData.setPrimordialBossDefeatedOnce(true);
                if (!prev) {
                    worldData.setFirstPrimordialBossDefeatTimestamp(this.m_9236_().getGameTime());
                    if (this.m_9236_() instanceof ServerLevel serverLevel) {
                        serverLevel.getPlayers(EntitySelector.NO_SPECTATORS).forEach(serverPlayer -> serverPlayer.displayClientMessage(Component.translatable("entity.alexscaves.luxtructosaurus.slain_message").withStyle(ChatFormatting.GOLD), true));
                    }
                }
            }
        }
        super.m_21268_(living);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.LUXTRUCTOSAURUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.LUXTRUCTOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.LUXTRUCTOSAURUS_DEATH.get();
    }

    @Override
    public int getExperienceReward() {
        return 100;
    }
}