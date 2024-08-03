package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFleeLight;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;

public class EntityCockroach extends Animal implements Shearable, IForgeShearable, ITargetsDroppedItems {

    public static final ResourceLocation MARACA_LOOT = new ResourceLocation("alexsmobs", "entities/cockroach_maracas");

    public static final ResourceLocation MARACA_HEADLESS_LOOT = new ResourceLocation("alexsmobs", "entities/cockroach_maracas_headless");

    protected static final EntityDimensions STAND_SIZE = EntityDimensions.fixed(0.7F, 0.9F);

    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(EntityCockroach.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HEADLESS = SynchedEntityData.defineId(EntityCockroach.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> MARACAS = SynchedEntityData.defineId(EntityCockroach.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<UUID>> NEAREST_MUSICIAN = SynchedEntityData.defineId(EntityCockroach.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> BREADED = SynchedEntityData.defineId(EntityCockroach.class, EntityDataSerializers.BOOLEAN);

    public int randomWingFlapTick = 0;

    public float prevDanceProgress;

    public float danceProgress;

    private boolean prevStand = false;

    private boolean isJukeboxing;

    private BlockPos jukeboxPosition;

    private int laCucarachaTimer = 0;

    public int timeUntilNextEgg = this.f_19796_.nextInt(24000) + 24000;

    public EntityCockroach(EntityType type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

    public static boolean isValidLightLevel(ServerLevelAccessor p_223323_0_, BlockPos p_223323_1_, RandomSource p_223323_2_) {
        if (p_223323_0_.m_45517_(LightLayer.SKY, p_223323_1_) > p_223323_2_.nextInt(32)) {
            return false;
        } else {
            int lvt_3_1_ = p_223323_0_.getLevel().m_46470_() ? p_223323_0_.m_46849_(p_223323_1_, 10) : p_223323_0_.m_46803_(p_223323_1_);
            return lvt_3_1_ <= p_223323_2_.nextInt(8);
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cockroachSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends EntityCockroach> p_223325_0_, ServerLevelAccessor p_223325_1_, MobSpawnType p_223325_2_, BlockPos p_223325_3_, RandomSource p_223325_4_) {
        return isValidLightLevel(p_223325_1_, p_223325_3_, p_223325_4_) && m_217057_(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
    }

    public static <T extends Mob> boolean canCockroachSpawn(EntityType<EntityCockroach> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || !iServerWorld.m_45527_(pos) && pos.m_123342_() <= 64 && canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.isBreaded() || this.isDancing() || this.hasMaracas() || this.isHeadless();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.COCKROACH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.COCKROACH_HURT.get();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.1));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(AMItemRegistry.MARACA.get(), Items.SUGAR), false));
        this.f_21345_.addGoal(4, new AvoidEntityGoal(this, EntityCentipedeHead.class, 16.0F, 1.3, 1.0));
        this.f_21345_.addGoal(4, new AvoidEntityGoal(this, Player.class, 8.0F, 1.3, 1.0) {

            @Override
            public boolean canUse() {
                return !EntityCockroach.this.isBreaded() && super.canUse();
            }
        });
        this.f_21345_.addGoal(5, new AnimalAIFleeLight(this, 1.0) {

            @Override
            public boolean canUse() {
                return !EntityCockroach.this.isBreaded() && super.canUse();
            }
        });
        this.f_21345_.addGoal(6, new RandomStrollGoal(this, 1.0, 80));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            this.randomWingFlapTick = 5 + this.f_19796_.nextInt(15);
            if (this.m_21223_() <= 1.0F && amount > 0.0F && !this.isHeadless() && this.m_217043_().nextInt(3) == 0) {
                this.setHeadless(true);
                if (!this.m_9236_().isClientSide) {
                    ServerLevel serverLevel = (ServerLevel) this.m_9236_();
                    for (int i = 0; i < 3; i++) {
                        serverLevel.sendParticles(ParticleTypes.SNEEZE, this.m_20208_(0.52F), this.m_20227_(1.0), this.m_20262_(0.52F), 1, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
        return prev;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.SUGAR;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Maracas", this.hasMaracas());
        compound.putBoolean("Dancing", this.isDancing());
        compound.putBoolean("Breaded", this.isBreaded());
        compound.putInt("EggTime", this.timeUntilNextEgg);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setMaracas(compound.getBoolean("Maracas"));
        this.setDancing(compound.getBoolean("Dancing"));
        this.setBreaded(compound.getBoolean("Breaded"));
        if (compound.contains("EggTime")) {
            this.timeUntilNextEgg = compound.getInt("EggTime");
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.hasMaracas() ? (this.isHeadless() ? MARACA_HEADLESS_LOOT : MARACA_LOOT) : super.m_7582_();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return 0.5F - (float) Math.max(worldIn.m_45517_(LightLayer.BLOCK, pos), worldIn.m_45517_(LightLayer.SKY, pos));
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isDancing() ? STAND_SIZE.scale(this.m_6134_()) : super.m_6972_(poseIn);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypeTags.IS_EXPLOSION) || source.getMsgId().equals("anvil") || super.m_6673_(source);
    }

    @Override
    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack lvt_3_1_ = p_230254_1_.m_21120_(p_230254_2_);
        if (lvt_3_1_.getItem() == AMItemRegistry.MARACA.get() && this.m_6084_() && !this.hasMaracas()) {
            this.setMaracas(true);
            lvt_3_1_.shrink(1);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if (lvt_3_1_.getItem() != AMItemRegistry.MARACA.get() && this.m_6084_() && this.hasMaracas()) {
            this.setMaracas(false);
            this.setDancing(false);
            this.m_19983_(new ItemStack(AMItemRegistry.MARACA.get()));
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DANCING, false);
        this.f_19804_.define(HEADLESS, false);
        this.f_19804_.define(MARACAS, false);
        this.f_19804_.define(NEAREST_MUSICIAN, Optional.empty());
        this.f_19804_.define(BREADED, false);
    }

    public boolean isDancing() {
        return this.f_19804_.get(DANCING);
    }

    public void setDancing(boolean dancing) {
        this.f_19804_.set(DANCING, dancing);
    }

    public boolean isHeadless() {
        return this.f_19804_.get(HEADLESS);
    }

    public void setHeadless(boolean head) {
        this.f_19804_.set(HEADLESS, head);
    }

    public boolean hasMaracas() {
        return this.f_19804_.get(MARACAS);
    }

    public void setMaracas(boolean head) {
        this.f_19804_.set(MARACAS, head);
    }

    public boolean isBreaded() {
        return this.f_19804_.get(BREADED);
    }

    public void setBreaded(boolean breaded) {
        this.f_19804_.set(BREADED, breaded);
    }

    @Nullable
    public UUID getNearestMusicianId() {
        return (UUID) this.f_19804_.get(NEAREST_MUSICIAN).orElse(null);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevDanceProgress = this.danceProgress;
        boolean dance = this.isJukeboxing || this.isDancing();
        if (this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_(this.m_20182_(), 3.46) || !this.m_9236_().getBlockState(this.jukeboxPosition).m_60713_(Blocks.JUKEBOX)) {
            this.isJukeboxing = false;
            this.jukeboxPosition = null;
        }
        if (this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (dance) {
            if (this.danceProgress < 5.0F) {
                this.danceProgress++;
            }
        } else if (this.danceProgress > 0.0F) {
            this.danceProgress--;
        }
        if (!this.m_20096_() || this.f_19796_.nextInt(200) == 0) {
            this.randomWingFlapTick = 5 + this.f_19796_.nextInt(15);
        }
        if (this.randomWingFlapTick > 0) {
            this.randomWingFlapTick--;
        }
        if (this.prevStand != dance) {
            if (this.hasMaracas()) {
                this.tellOthersImPlayingLaCucaracha();
            }
            this.m_6210_();
        }
        if (!this.hasMaracas()) {
            Entity musician = this.getNearestMusician();
            if (musician != null) {
                if (musician.isAlive() && !(this.m_20270_(musician) > 10.0F) && (!(musician instanceof EntityCockroach) || ((EntityCockroach) musician).hasMaracas())) {
                    this.setDancing(true);
                } else {
                    this.setNearestMusician(null);
                    this.setDancing(false);
                }
            }
        }
        if (this.hasMaracas()) {
            this.laCucarachaTimer++;
            if (this.laCucarachaTimer % 20 == 0 && this.f_19796_.nextFloat() < 0.3F) {
                this.tellOthersImPlayingLaCucaracha();
            }
            this.setDancing(true);
            if (!this.m_20067_()) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 67);
            }
        } else {
            this.laCucarachaTimer = 0;
        }
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_6162_() && --this.timeUntilNextEgg <= 0) {
            ItemEntity dropped = this.m_19998_(AMItemRegistry.COCKROACH_OOTHECA.get());
            if (dropped != null) {
                dropped.setDefaultPickUpDelay();
            }
            this.timeUntilNextEgg = this.f_19796_.nextInt(24000) + 24000;
        }
        this.prevStand = dance;
    }

    private void tellOthersImPlayingLaCucaracha() {
        for (EntityCockroach roach : this.m_9236_().m_6443_(EntityCockroach.class, this.getMusicianDistance(), EntitySelector.NO_SPECTATORS)) {
            if (!roach.hasMaracas()) {
                roach.setNearestMusician(this.m_20148_());
            }
        }
    }

    private AABB getMusicianDistance() {
        return this.m_20191_().inflate(10.0, 10.0, 10.0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 67) {
            AlexsMobs.PROXY.onEntityStatus(this, id);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public Entity getNearestMusician() {
        UUID id = this.getNearestMusicianId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setNearestMusician(@Nullable UUID uniqueId) {
        this.f_19804_.set(NEAREST_MUSICIAN, Optional.ofNullable(uniqueId));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.jukeboxPosition = pos;
        this.isJukeboxing = isPartying;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityCockroach roach = AMEntityRegistry.COCKROACH.get().create(serverWorld);
        roach.setBreaded(true);
        return roach;
    }

    @Override
    public boolean readyForShearing() {
        return this.m_6084_() && !this.m_6162_() && !this.isHeadless();
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.readyForShearing();
    }

    @Override
    public void shear(SoundSource category) {
        this.hurt(this.m_269291_().generic(), 0.0F);
        this.m_9236_().playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        this.setHeadless(true);
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        this.hurt(this.m_269291_().generic(), 0.0F);
        if (!world.isClientSide) {
            for (int i = 0; i < 3; i++) {
                ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.SNEEZE, this.m_20208_(0.52F), this.m_20227_(1.0), this.m_20262_(0.52F), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        this.setHeadless(true);
        return Collections.emptyList();
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() || stack.getItem() == Items.SUGAR;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isDancing() || this.danceProgress > 0.0F) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        if (e.getItem().getItem() == AMItemRegistry.MARACA.get()) {
            this.setMaracas(true);
        } else {
            if (e.getItem().hasCraftingRemainingItem()) {
                this.m_19983_(e.getItem().getCraftingRemainingItem().copy());
            }
            this.m_5634_(5.0F);
            if (e.getItem().getItem() == Items.BREAD || e.getItem().getItem() == Items.SUGAR) {
                this.setBreaded(true);
            }
        }
    }
}