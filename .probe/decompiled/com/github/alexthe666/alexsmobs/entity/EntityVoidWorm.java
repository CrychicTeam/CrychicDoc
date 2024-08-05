package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockEnderResidue;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityVoidWorm extends Monster {

    public static final ResourceLocation SPLITTER_LOOT = new ResourceLocation("alexsmobs", "entities/void_worm_splitter");

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> SPLIT_FROM_UUID = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> SEGMENT_COUNT = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> JAW_TICKS = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> WORM_ANGLE = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SPEEDMOD = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> SPLITTER = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> PORTAL_TICKS = SynchedEntityData.defineId(EntityVoidWorm.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    public float prevWormAngle;

    public float prevJawProgress;

    public float jawProgress;

    public Vec3 teleportPos = null;

    public EntityVoidPortal portalTarget = null;

    public boolean fullyThrough = true;

    public boolean updatePostSummon = false;

    private int makePortalCooldown = 0;

    private int stillTicks = 0;

    private int blockBreakCounter;

    private int makeIdlePortalCooldown = 200 + this.f_19796_.nextInt(800);

    protected EntityVoidWorm(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        this.f_21364_ = 10;
        this.f_21342_ = new FlightMoveController(this, 1.0F, false, true);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.VOID_WORM_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.VOID_WORM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.VOID_WORM_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return this.m_20067_() ? 0.0F : 5.0F;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.voidWormSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canVoidWormSpawn(EntityType animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return true;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, AMConfig.voidWormMaxHealth).add(Attributes.ARMOR, 4.0).add(Attributes.FOLLOW_RANGE, 256.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isSplitter() ? SPLITTER_LOOT : super.m_7582_();
    }

    @Override
    public void kill() {
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void die(DamageSource cause) {
        super.m_6667_(cause);
        if (!this.m_9236_().isClientSide && !this.isSplitter() && cause != null && cause.getEntity() instanceof ServerPlayer) {
            AMAdvancementTriggerRegistry.VOID_WORM_SLAY_HEAD.trigger((ServerPlayer) cause.getEntity());
        }
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itementity = this.m_5552_(stack, 0.0F);
        if (itementity != null) {
            itementity.m_20242_(true);
            itementity.m_146915_(true);
            itementity.setExtendedLifetime();
        }
        return itementity;
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
    }

    private void placeDropsSafely(Collection<ItemEntity> drops) {
        BlockPos pos = this.m_20183_();
        while (!this.m_9236_().getBlockState(pos).m_247087_() && pos.m_123342_() < this.m_9236_().m_151558_() - 2) {
            pos = pos.above();
        }
        int radius = 2;
        BlockState residue = (BlockState) AMBlockRegistry.ENDER_RESIDUE.get().defaultBlockState().m_61124_(BlockEnderResidue.SLOW_DECAY, true);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double sq = (double) (x * x + y * y + z * z);
                    BlockPos pos1 = pos.offset(x, y, z);
                    BlockState state = this.m_9236_().getBlockState(pos1);
                    if (sq <= (double) (radius * radius) && sq >= (double) ((float) (radius * radius) - 2.0F) && (state.m_247087_() || state.m_60713_(AMBlockRegistry.ENDER_RESIDUE.get()))) {
                        this.m_9236_().setBlockAndUpdate(pos1, residue);
                    }
                }
            }
        }
        this.m_9236_().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        for (ItemEntity drop : drops) {
            drop.m_146884_(Vec3.atBottomCenterOf(pos));
            drop.m_146915_(true);
            drop.m_20242_(true);
            drop.setDefaultPickUpDelay();
            drop.setUnlimitedLifetime();
            drop.m_20256_(Vec3.ZERO);
            this.m_9236_().m_7967_(drop);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypeTags.IS_FIRE) || super.m_6673_(source);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new EntityVoidWorm.AIEnterPortal());
        this.f_21345_.addGoal(2, new EntityVoidWorm.AIAttack());
        this.f_21345_.addGoal(3, new EntityVoidWorm.AIFlyIdle());
        this.f_21346_.addGoal(1, new EntityAINearestTarget3D(this, Player.class, 10, false, true, null));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, EnderDragon.class, 10, false, true, null));
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new DirectPathNavigator(this, this.m_9236_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
        this.setWormSpeed(compound.getFloat("WormSpeed"));
        this.setSplitter(compound.getBoolean("Splitter"));
        this.setPortalTicks(compound.getInt("PortalTicks"));
        this.makeIdlePortalCooldown = compound.getInt("MakePortalTime");
        this.makePortalCooldown = compound.getInt("MakePortalCooldown");
        if (this.m_8077_()) {
            this.bossInfo.setName(this.m_5446_());
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.m_6593_(name);
        this.bossInfo.setName(this.m_5446_());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
        compound.putInt("PortalTicks", this.getPortalTicks());
        compound.putInt("MakePortalTime", this.makeIdlePortalCooldown);
        compound.putInt("MakePortalCooldown", this.makePortalCooldown);
        compound.putFloat("WormSpeed", this.getWormSpeed());
        compound.putBoolean("Splitter", this.isSplitter());
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public int getExperienceReward() {
        return this.isSplitter() ? 8 : 50;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevWormAngle = this.getWormAngle();
        this.prevJawProgress = this.jawProgress;
        float threshold = 0.05F;
        if (this.isSplitter()) {
            this.f_21364_ = 10;
        } else {
            this.f_21364_ = 70;
        }
        if (this.f_19859_ - this.m_146908_() > threshold) {
            this.setWormAngle(this.getWormAngle() + 15.0F);
        } else if (this.f_19859_ - this.m_146908_() < -threshold) {
            this.setWormAngle(this.getWormAngle() - 15.0F);
        } else if (this.getWormAngle() > 0.0F) {
            this.setWormAngle(Math.max(this.getWormAngle() - 20.0F, 0.0F));
        } else if (this.getWormAngle() < 0.0F) {
            this.setWormAngle(Math.min(this.getWormAngle() + 20.0F, 0.0F));
        }
        if (!this.m_9236_().isClientSide) {
            if (!this.fullyThrough) {
                this.m_20256_(this.m_20184_().multiply(0.9F, 0.9F, 0.9F).add(0.0, -0.01, 0.0));
            } else {
                this.m_20256_(this.m_20184_().add(0.0, 0.01, 0.0));
            }
        }
        if (Math.abs(this.f_19854_ - this.m_20185_()) < 0.01F && Math.abs(this.f_19855_ - this.m_20186_()) < 0.01F && Math.abs(this.f_19856_ - this.m_20189_()) < 0.01F) {
            this.stillTicks++;
        } else {
            this.stillTicks = 0;
        }
        if (this.stillTicks > 40 && this.makePortalCooldown == 0) {
            this.createStuckPortal();
        }
        if (this.makePortalCooldown > 0) {
            this.makePortalCooldown--;
        }
        if (this.makeIdlePortalCooldown > 0) {
            this.makeIdlePortalCooldown--;
        }
        if (this.makeIdlePortalCooldown == 0 && this.f_19796_.nextInt(100) == 0) {
            this.createPortalRandomDestination();
            this.makeIdlePortalCooldown = 200 + this.f_19796_.nextInt(1000);
        }
        if (this.f_19804_.get(JAW_TICKS) > 0) {
            if (this.jawProgress < 5.0F) {
                this.jawProgress++;
            }
            this.f_19804_.set(JAW_TICKS, this.f_19804_.get(JAW_TICKS) - 1);
        } else if (this.jawProgress > 0.0F) {
            this.jawProgress--;
        }
        if (this.m_6084_()) {
            for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(2.0))) {
                if (!entity.is(this) && !(entity instanceof EntityVoidWormPart) && !entity.isAlliedTo(this) && entity != this) {
                    this.launch(entity, false);
                }
            }
            this.m_274367_(2.0F);
        } else {
            this.m_20256_(new Vec3(0.0, 0.03F, 0.0));
        }
        this.f_20883_ = this.m_146908_();
        float f2 = (float) (-((double) ((float) this.m_20184_().y) * 180.0F / (float) Math.PI));
        this.m_146926_(f2);
        this.m_274367_(2.0F);
        if (!this.m_9236_().isClientSide) {
            Entity child = this.getChild();
            if (child == null) {
                LivingEntity partParent = this;
                int tailstart = Math.min(3 + this.f_19796_.nextInt(2), this.getSegmentCount());
                int segments = this.getSegmentCount();
                for (int i = 0; i < segments; i++) {
                    float scale = 1.0F + (float) i / (float) segments * 0.5F;
                    boolean tail = false;
                    if (i >= segments - tailstart) {
                        tail = true;
                        scale *= 0.85F;
                    }
                    EntityVoidWormPart part = new EntityVoidWormPart(AMEntityRegistry.VOID_WORM_PART.get(), partParent, 1.0F + scale * (tail ? 0.65F : 0.3F) + (i == 0 ? 0.8F : 0.0F), 180.0F, i == 0 ? -0.0F : (i == segments - tailstart ? -0.3F : 0.0F));
                    part.setParent(partParent);
                    if (this.updatePostSummon) {
                        part.setPortalTicks(i * 2);
                    }
                    part.setBodyIndex(i);
                    part.setTail(tail);
                    part.setWormScale(scale);
                    if (partParent == this) {
                        this.setChildId(part.m_20148_());
                    } else if (partParent instanceof EntityVoidWormPart) {
                        ((EntityVoidWormPart) partParent).setChildId(part.m_20148_());
                    }
                    part.setInitialPartPos(this);
                    partParent = part;
                    this.m_9236_().m_7967_(part);
                }
            }
        }
        if (this.getPortalTicks() > 0) {
            this.setPortalTicks(this.getPortalTicks() - 1);
            if (this.getPortalTicks() == 2 && this.teleportPos != null) {
                this.m_6034_(this.teleportPos.x, this.teleportPos.y, this.teleportPos.z);
                this.teleportPos = null;
            }
        }
        if (this.portalTarget != null && this.portalTarget.getLifespan() < 5) {
            this.portalTarget = null;
        }
        this.bossInfo.setProgress(this.m_21223_() / this.m_21233_());
        this.breakBlock();
        if (this.updatePostSummon) {
            this.updatePostSummon = false;
        }
        if (!this.m_20067_() && !this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 67);
        }
    }

    public void setMaxHealth(double maxHealth, boolean heal) {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
        if (heal) {
            this.m_5634_((float) maxHealth);
        }
    }

    @Override
    protected void tickDeath() {
        this.f_20919_++;
        if (this.f_20919_ == (this.isSplitter() ? 20 : 80) && !this.m_9236_().isClientSide()) {
            DamageSource source = this.m_21225_() == null ? this.m_269291_().generic() : this.m_21225_();
            Entity entity = source.getEntity();
            int i = ForgeHooks.getLootingLevel(this, entity, source);
            this.captureDrops(new ArrayList());
            boolean flag = this.f_20889_ > 0;
            if (this.m_6125_() && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.m_7625_(source, flag);
                this.m_7472_(source, i, flag);
            }
            this.m_5907_();
            this.m_21226_();
            Collection<ItemEntity> drops = this.captureDrops(null);
            if (!ForgeHooks.onLivingDrops(this, source, drops, i, this.f_20889_ > 0) && !drops.isEmpty()) {
                this.placeDropsSafely(drops);
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 60);
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.m_6457_(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.m_6452_(player);
        this.bossInfo.removePlayer(player);
    }

    public void teleportTo(Vec3 vec) {
        this.setPortalTicks(10);
        this.teleportPos = vec;
        this.fullyThrough = false;
        if (this.getChild() instanceof EntityVoidWormPart) {
            ((EntityVoidWormPart) this.getChild()).teleportTo(this.m_20182_(), this.teleportPos);
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.onGround()) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 2.0F : 0.5F;
            e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
        }
    }

    public void resetWormScales() {
        if (!this.m_9236_().isClientSide) {
            Entity child = this.getChild();
            if (child == null) {
                LivingEntity nextPart = this;
                int tailstart = Math.min(3 + this.f_19796_.nextInt(2), this.getSegmentCount());
                int segments = this.getSegmentCount();
                int i = 0;
                while (nextPart instanceof EntityVoidWormPart) {
                    EntityVoidWormPart part = (EntityVoidWormPart) ((EntityVoidWormPart) nextPart).getChild();
                    i++;
                    float scale = 1.0F + (float) i / (float) segments * 0.5F;
                    boolean tail = i >= segments - tailstart;
                    part.setTail(tail);
                    part.setWormScale(scale);
                    part.radius = 1.0F + scale * (tail ? 0.65F : 0.3F) + (i == 0 ? 0.8F : 0.0F);
                    part.offsetY = i == 0 ? -0.0F : (i == segments - tailstart ? -0.3F : 0.0F);
                    nextPart = part;
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setSegmentCount(25 + this.f_19796_.nextInt(15));
        this.m_146926_(0.0F);
        this.setMaxHealth(AMConfig.voidWormMaxHealth, true);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SPLIT_FROM_UUID, Optional.empty());
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(SEGMENT_COUNT, 10);
        this.f_19804_.define(JAW_TICKS, 0);
        this.f_19804_.define(WORM_ANGLE, 0.0F);
        this.f_19804_.define(SPEEDMOD, 1.0F);
        this.f_19804_.define(SPLITTER, false);
        this.f_19804_.define(PORTAL_TICKS, 0);
    }

    public float getWormAngle() {
        return this.f_19804_.get(WORM_ANGLE);
    }

    public void setWormAngle(float progress) {
        this.f_19804_.set(WORM_ANGLE, progress);
    }

    public float getWormSpeed() {
        return this.f_19804_.get(SPEEDMOD);
    }

    public void setWormSpeed(float progress) {
        if (this.getWormSpeed() != progress) {
            this.f_21342_ = new FlightMoveController(this, progress, false, true);
        }
        this.f_19804_.set(SPEEDMOD, progress);
    }

    public boolean isSplitter() {
        return this.f_19804_.get(SPLITTER);
    }

    public void setSplitter(boolean splitter) {
        this.f_19804_.set(SPLITTER, splitter);
    }

    public void openMouth(int time) {
        this.f_19804_.set(JAW_TICKS, time);
    }

    public boolean isMouthOpen() {
        return (float) this.f_19804_.get(JAW_TICKS).intValue() >= 5.0F;
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    @Nullable
    public UUID getSplitFromUUID() {
        return (UUID) this.f_19804_.get(SPLIT_FROM_UUID).orElse(null);
    }

    public void setSplitFromUuid(@Nullable UUID uniqueId) {
        this.f_19804_.set(SPLIT_FROM_UUID, Optional.ofNullable(uniqueId));
    }

    public int getPortalTicks() {
        return this.f_19804_.get(PORTAL_TICKS);
    }

    public void setPortalTicks(int ticks) {
        this.f_19804_.set(PORTAL_TICKS, ticks);
    }

    public int getSegmentCount() {
        return this.f_19804_.get(SEGMENT_COUNT);
    }

    public void setSegmentCount(int command) {
        this.f_19804_.set(SEGMENT_COUNT, command);
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2F, 0.0, 0.2F));
        entities.stream().filter(entity -> !(entity instanceof EntityVoidWormPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    @Override
    public void push(Entity entityIn) {
    }

    public void createStuckPortal() {
        if (this.m_5448_() != null) {
            this.createPortal(this.m_5448_().m_20182_().add((double) (this.f_19796_.nextInt(8) - 4), (double) (2 + this.f_19796_.nextInt(3)), (double) (this.f_19796_.nextInt(8) - 4)));
        } else {
            Vec3 vec = Vec3.atCenterOf(this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, this.m_20183_().above(this.f_19796_.nextInt(10) + 10)));
            this.createPortal(vec);
        }
    }

    public void createPortal(Vec3 to) {
        this.createPortal(this.m_20182_().add(this.m_20154_().scale(20.0)), to, null);
    }

    public void createPortalRandomDestination() {
        Vec3 vec = null;
        for (int i = 0; i < 15; i++) {
            BlockPos pos = AMBlockPos.fromCoords(this.m_20185_() + (double) this.f_19796_.nextInt(60) - 30.0, 0.0, this.m_20189_() + (double) this.f_19796_.nextInt(60) - 30.0);
            BlockPos height = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, pos);
            if (height.m_123342_() < 10) {
                height = height.above(50 + this.f_19796_.nextInt(50));
            } else {
                height = height.above(this.f_19796_.nextInt(30));
            }
            if (this.m_9236_().m_46859_(height)) {
                vec = Vec3.atBottomCenterOf(height);
            }
        }
        if (vec != null) {
            this.createPortal(this.m_20182_().add(this.m_20154_().scale(20.0)), vec, null);
        }
    }

    public void createPortal(Vec3 from, Vec3 to, @Nullable Direction outDir) {
        if (!this.m_9236_().isClientSide && this.portalTarget == null) {
            Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
            HitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, from, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            Vec3 vec = result.getLocation() != null ? result.getLocation() : this.m_20182_();
            if (result instanceof BlockHitResult result1) {
                vec = vec.add(Vec3.atLowerCornerOf(result1.getDirection().getNormal()));
            }
            EntityVoidPortal portal = AMEntityRegistry.VOID_PORTAL.get().create(this.m_9236_());
            portal.m_6034_(vec.x, vec.y, vec.z);
            Vec3 dirVec = vec.subtract(this.m_20182_());
            Direction dir = Direction.getNearest(dirVec.x, dirVec.y, dirVec.z);
            portal.setAttachmentFacing(dir);
            portal.setLifespan(10000);
            if (!this.m_9236_().isClientSide) {
                this.m_9236_().m_7967_(portal);
            }
            this.portalTarget = portal;
            portal.setDestination(AMBlockPos.fromCoords(to.x, to.y, to.z), outDir);
            this.makePortalCooldown = 300;
        }
    }

    public void resetPortalLogic() {
        this.portalTarget = null;
        this.stillTicks = 0;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
        } else {
            boolean flag = false;
            if (!this.m_9236_().isClientSide && this.blockBreakCounter == 0 && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                for (int a = (int) Math.round(this.m_20191_().minX); a <= (int) Math.round(this.m_20191_().maxX); a++) {
                    for (int b = (int) Math.round(this.m_20191_().minY) - 1; b <= (int) Math.round(this.m_20191_().maxY) + 1 && b <= 127; b++) {
                        for (int c = (int) Math.round(this.m_20191_().minZ); c <= (int) Math.round(this.m_20191_().maxZ); c++) {
                            BlockPos pos = new BlockPos(a, b, c);
                            BlockState state = this.m_9236_().getBlockState(pos);
                            FluidState fluidState = this.m_9236_().getFluidState(pos);
                            Block block = state.m_60734_();
                            if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && state.m_204336_(AMTagRegistry.VOID_WORM_BREAKABLES) && fluidState.isEmpty() && block != Blocks.AIR) {
                                this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                flag = true;
                                this.m_9236_().m_46961_(pos, true);
                                if (state.m_204336_(BlockTags.ICE)) {
                                    this.m_9236_().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCounter = 10;
            }
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = (-9.45F - (float) this.m_217043_().nextInt(24)) * radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.getGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 10 + this.m_217043_().nextInt(20);
        BlockPos newPos = ground.above(distFromGround > 8 ? flightHeight : this.m_217043_().nextInt(10) + 15);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public Vec3 getBlockInViewAwaySlam(Vec3 fleePos, int slamHeight) {
        float radius = (float) (3 + this.f_19796_.nextInt(3));
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.getHeighestAirAbove(radialPos, slamHeight);
        return !this.isTargetBlocked(Vec3.atCenterOf(ground)) && this.m_20238_(Vec3.atCenterOf(ground)) > 1.0 ? Vec3.atCenterOf(ground) : null;
    }

    private BlockPos getHeighestAirAbove(BlockPos radialPos, int limit) {
        BlockPos position = AMBlockPos.fromCoords((double) radialPos.m_123341_(), this.m_20186_(), (double) radialPos.m_123343_());
        while (position.m_123342_() < 256 && (double) position.m_123342_() < this.m_20186_() + (double) limit && this.m_9236_().m_46859_(position)) {
            position = position.above();
        }
        return position;
    }

    private BlockPos getGround(BlockPos in) {
        BlockPos position = AMBlockPos.fromCoords((double) in.m_123341_(), this.m_20186_(), (double) in.m_123343_());
        while (position.m_123342_() > -63 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return position.m_123342_() < -62 ? position.above(120 + this.f_19796_.nextInt(5)) : position;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        return super.m_7307_(entityIn) || this.getSplitFromUUID() != null && this.getSplitFromUUID().equals(entityIn.getUUID()) || entityIn instanceof EntityVoidWorm && ((EntityVoidWorm) entityIn).getSplitFromUUID() != null && ((EntityVoidWorm) entityIn).getSplitFromUUID().equals(entityIn.getUUID());
    }

    private void spit(Vec3 shotAt, boolean portal) {
        shotAt = shotAt.yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
        EntityVoidWormShot shot = new EntityVoidWormShot(this.m_9236_(), this);
        double d0 = shotAt.x;
        double d1 = shotAt.y;
        double d2 = shotAt.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.35F;
        shot.shoot(d0, d1 + (double) f, d2, 0.5F, 3.0F);
        if (!this.m_20067_()) {
            this.m_146850_(GameEvent.PROJECTILE_SHOOT);
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.DROWNED_SHOOT, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
        }
        this.openMouth(5);
        this.m_9236_().m_7967_(shot);
    }

    private boolean wormAttack(Entity entity, DamageSource source, float dmg) {
        dmg = (float) ((double) dmg * AMConfig.voidWormDamageModifier);
        return entity instanceof EnderDragon ? ((EnderDragon) entity).reallyHurt(source, dmg * 0.5F) : entity.hurt(source, dmg);
    }

    public void playHurtSoundWorm(DamageSource source) {
        this.m_6677_(source);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 67) {
            AlexsMobs.PROXY.onEntityStatus(this, id);
        } else {
            super.m_7822_(id);
        }
    }

    public class AIAttack extends Goal {

        private EntityVoidWorm.AttackMode mode = EntityVoidWorm.AttackMode.CIRCLE;

        private int modeTicks = 0;

        private int maxCircleTime = 500;

        private Vec3 moveTo = null;

        public AIAttack() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityVoidWorm.this.m_5448_() != null && EntityVoidWorm.this.m_5448_().isAlive();
        }

        @Override
        public void stop() {
            this.mode = EntityVoidWorm.AttackMode.CIRCLE;
            this.modeTicks = 0;
        }

        @Override
        public void start() {
            this.mode = EntityVoidWorm.AttackMode.CIRCLE;
            this.maxCircleTime = 60 + EntityVoidWorm.this.f_19796_.nextInt(200);
        }

        @Override
        public void tick() {
            LivingEntity target = EntityVoidWorm.this.m_5448_();
            boolean flag = false;
            float speed = 1.0F;
            for (Entity entity : EntityVoidWorm.this.m_9236_().m_45976_(LivingEntity.class, EntityVoidWorm.this.m_20191_().inflate(2.0))) {
                if (!entity.is(EntityVoidWorm.this) && !(entity instanceof EntityVoidWormPart) && !entity.isAlliedTo(EntityVoidWorm.this) && entity != EntityVoidWorm.this) {
                    if (EntityVoidWorm.this.isMouthOpen()) {
                        EntityVoidWorm.this.launch(entity, true);
                        flag = true;
                        EntityVoidWorm.this.wormAttack(entity, EntityVoidWorm.this.m_269291_().mobAttack(EntityVoidWorm.this), 8.0F + EntityVoidWorm.this.f_19796_.nextFloat() * 8.0F);
                    } else {
                        EntityVoidWorm.this.openMouth(15);
                    }
                }
            }
            if (target != null) {
                if (this.mode == EntityVoidWorm.AttackMode.CIRCLE) {
                    if (this.moveTo == null || EntityVoidWorm.this.m_20238_(this.moveTo) < 16.0 || EntityVoidWorm.this.f_19862_) {
                        this.moveTo = EntityVoidWorm.this.getBlockInViewAway(target.m_20182_(), 0.4F + EntityVoidWorm.this.f_19796_.nextFloat() * 0.2F);
                    }
                    int interval = EntityVoidWorm.this.m_21223_() < EntityVoidWorm.this.m_21233_() && !EntityVoidWorm.this.isSplitter() ? 15 : 40;
                    if (this.modeTicks % interval == 0) {
                        EntityVoidWorm.this.spit(new Vec3(3.0, 3.0, 0.0), false);
                        EntityVoidWorm.this.spit(new Vec3(-3.0, 3.0, 0.0), false);
                        EntityVoidWorm.this.spit(new Vec3(3.0, -3.0, 0.0), false);
                        EntityVoidWorm.this.spit(new Vec3(-3.0, -3.0, 0.0), false);
                    }
                    this.modeTicks++;
                    if (this.modeTicks > this.maxCircleTime) {
                        this.maxCircleTime = 60 + EntityVoidWorm.this.f_19796_.nextInt(200);
                        this.mode = EntityVoidWorm.AttackMode.SLAM_RISE;
                        this.modeTicks = 0;
                        this.moveTo = null;
                    }
                } else if (this.mode == EntityVoidWorm.AttackMode.SLAM_RISE) {
                    if (this.moveTo == null) {
                        this.moveTo = EntityVoidWorm.this.getBlockInViewAwaySlam(target.m_20182_(), 20 + EntityVoidWorm.this.f_19796_.nextInt(20));
                    }
                    if (this.moveTo != null && EntityVoidWorm.this.m_20186_() > target.m_20186_() + 15.0) {
                        this.moveTo = null;
                        this.modeTicks = 0;
                        this.mode = EntityVoidWorm.AttackMode.SLAM_FALL;
                    }
                } else if (this.mode == EntityVoidWorm.AttackMode.SLAM_FALL) {
                    speed = 2.0F;
                    EntityVoidWorm.this.m_21391_(target, 360.0F, 360.0F);
                    this.moveTo = target.m_20182_();
                    if (EntityVoidWorm.this.f_19862_) {
                        this.moveTo = new Vec3(target.m_20185_(), EntityVoidWorm.this.m_20186_() + 3.0, target.m_20189_());
                    }
                    EntityVoidWorm.this.openMouth(20);
                    if (EntityVoidWorm.this.m_20238_(this.moveTo) < 4.0 || flag) {
                        this.mode = EntityVoidWorm.AttackMode.CIRCLE;
                        this.moveTo = null;
                        this.modeTicks = 0;
                    }
                }
            }
            if (!EntityVoidWorm.this.m_142582_(target) && EntityVoidWorm.this.f_19796_.nextInt(100) == 0 && EntityVoidWorm.this.makePortalCooldown == 0) {
                Vec3 to = new Vec3(target.m_20185_(), target.m_20191_().maxY + 0.1, target.m_20189_());
                EntityVoidWorm.this.createPortal(EntityVoidWorm.this.m_20182_().add(EntityVoidWorm.this.m_20154_().scale(20.0)), to, Direction.UP);
                EntityVoidWorm.this.makePortalCooldown = 50;
                this.mode = EntityVoidWorm.AttackMode.SLAM_FALL;
            }
            if (this.moveTo != null && EntityVoidWorm.this.portalTarget == null) {
                EntityVoidWorm.this.m_21566_().setWantedPosition(this.moveTo.x, this.moveTo.y, this.moveTo.z, (double) speed);
            }
        }
    }

    public class AIEnterPortal extends Goal {

        public AIEnterPortal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityVoidWorm.this.portalTarget != null;
        }

        @Override
        public void tick() {
            if (EntityVoidWorm.this.portalTarget != null) {
                EntityVoidWorm.this.f_19794_ = true;
                double centerX = EntityVoidWorm.this.portalTarget.m_20185_();
                double centerY = EntityVoidWorm.this.portalTarget.m_20227_(0.5);
                double centerZ = EntityVoidWorm.this.portalTarget.m_20189_();
                double d0 = centerX - EntityVoidWorm.this.m_20185_();
                double d1 = centerY - EntityVoidWorm.this.m_20227_(0.5);
                double d2 = centerZ - EntityVoidWorm.this.m_20189_();
                Vec3 vec = new Vec3(d0, d1, d2);
                if (vec.length() > 1.0) {
                    vec = vec.normalize();
                }
                vec = vec.scale(0.4F);
                EntityVoidWorm.this.m_20256_(EntityVoidWorm.this.m_20184_().add(vec));
            }
        }

        @Override
        public void stop() {
            EntityVoidWorm.this.f_19794_ = false;
        }
    }

    private class AIFlyIdle extends Goal {

        protected final EntityVoidWorm voidWorm;

        protected double x;

        protected double y;

        protected double z;

        public AIFlyIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.voidWorm = EntityVoidWorm.this;
        }

        @Override
        public boolean canUse() {
            if (!this.voidWorm.m_20160_() && this.voidWorm.portalTarget == null && (this.voidWorm.m_5448_() == null || !this.voidWorm.m_5448_().isAlive()) && !this.voidWorm.m_20159_()) {
                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            this.voidWorm.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.voidWorm.m_20182_();
            return this.voidWorm.getBlockInViewAway(vector3d, 1.0F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.voidWorm.m_20275_(this.x, this.y, this.z) > 20.0 && this.voidWorm.portalTarget == null && !this.voidWorm.f_19862_ && (this.voidWorm.m_5448_() == null || !this.voidWorm.m_5448_().isAlive());
        }

        @Override
        public void start() {
            this.voidWorm.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
        }

        @Override
        public void stop() {
            this.voidWorm.m_21573_().stop();
            super.stop();
        }
    }

    private static enum AttackMode {

        CIRCLE, SLAM_RISE, SLAM_FALL, PORTAL
    }
}