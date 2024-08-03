package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.EtherealMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.MonsterAIWalkThroughHallsOfStructure;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.Tags;

public class EntityUnderminer extends PathfinderMob {

    protected static final EntityDataAccessor<Optional<BlockPos>> TARGETED_BLOCK_POS = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> DWARF = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> MINING_PROGRESS = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> VISUALLY_MINING = SynchedEntityData.defineId(EntityUnderminer.class, EntityDataSerializers.BOOLEAN);

    private int mineCooldown = 100;

    private int resetStackTime = 0;

    private ItemStack lastGivenStack = null;

    public float hidingProgress = 0.0F;

    public float prevHidingProgress = 0.0F;

    private boolean mineAIFlag = false;

    private BlockPos lastPosition = this.m_20183_();

    public EntityUnderminer(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.f_21342_ = new EtherealMoveController(this, 1.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new EntityUnderminer.PathNavigator(this, this.m_9236_());
    }

    public static <T extends Mob> boolean checkUnderminerSpawnRules(EntityType<EntityUnderminer> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        } else {
            int j = 3;
            if (pos.m_123342_() >= iServerWorld.m_5736_()) {
                return false;
            } else {
                if (AlexsMobs.isHalloween()) {
                    j = 7;
                } else if (random.nextBoolean()) {
                    return false;
                }
                int i = iServerWorld.m_46803_(pos);
                return i > random.nextInt(j) ? false : m_217057_(entityType, iServerWorld, reason, pos, random);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence() && !this.m_8077_();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.lastGivenStack != null;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.underminerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DWARF, true);
        this.f_19804_.define(HIDING, false);
        this.f_19804_.define(VISUALLY_MINING, false);
        this.f_19804_.define(TARGETED_BLOCK_POS, Optional.empty());
        this.f_19804_.define(MINING_PROGRESS, 0.0F);
        this.f_19804_.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Dwarf", this.isDwarf());
        compound.putBoolean("Hiding", this.isHiding());
        compound.putInt("Variant", this.getVariant());
        compound.putInt("ResetItemTime", this.resetStackTime);
        compound.putInt("MineCooldown", this.mineCooldown);
        if (this.lastGivenStack != null) {
            compound.put("MineStack", this.lastGivenStack.serializeNBT());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setDwarf(compound.getBoolean("Dwarf"));
        this.setHiding(compound.getBoolean("Hiding"));
        this.setVariant(compound.getInt("Variant"));
        this.resetStackTime = compound.getInt("ResetItemTime");
        this.mineCooldown = compound.getInt("MineCooldown");
        if (compound.contains("MineStack")) {
            this.lastGivenStack = ItemStack.of(compound.getCompound("MineStack"));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.UNDERMINER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.UNDERMINER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.UNDERMINER_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    public boolean isDwarf() {
        return this.f_19804_.get(DWARF) && !this.isExtraSpooky();
    }

    public void setDwarf(boolean phasing) {
        this.f_19804_.set(DWARF, phasing);
    }

    public int getVariant() {
        return this.isExtraSpooky() ? 1 : this.f_19804_.get(VARIANT);
    }

    public void setVariant(int i) {
        this.f_19804_.set(VARIANT, i);
    }

    public boolean isHiding() {
        return this.f_19804_.get(HIDING);
    }

    public void setHiding(boolean phasing) {
        this.f_19804_.set(HIDING, phasing);
    }

    @Nullable
    public BlockPos getMiningPos() {
        return (BlockPos) this.m_20088_().get(TARGETED_BLOCK_POS).orElse(null);
    }

    public void setMiningPos(@Nullable BlockPos beamTarget) {
        this.m_20088_().set(TARGETED_BLOCK_POS, Optional.ofNullable(beamTarget));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.f_21345_.addGoal(2, new EntityUnderminer.MineGoal());
        this.f_21345_.addGoal(3, new MonsterAIWalkThroughHallsOfStructure(this, 0.5, 60, StructureTags.MINESHAFT, 50.0));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(DamageTypes.MAGIC) && source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.isCreativePlayer() || super.m_6673_(source);
    }

    private float calculateDistanceToFloor() {
        BlockPos floor = AMBlockPos.fromCoords(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
        while (!this.m_9236_().getBlockState(floor).m_60783_(this.m_9236_(), floor, Direction.UP) && floor.m_123342_() > this.m_9236_().m_141937_()) {
            floor = floor.below();
        }
        return (float) (this.m_20191_().minY - (double) (floor.m_123342_() + 1));
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().multiply(0.9, 0.6, 0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        super.m_213945_(randomSource0, difficultyInstance1);
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(AMItemRegistry.GHOSTLY_PICKAXE.get()));
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? 0.5F : super.m_21519_(slot);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
        spawnData = super.m_6518_(level, difficultyInstance, mobSpawnType, spawnData, tag);
        RandomSource randomsource = level.m_213780_();
        this.populateDefaultEquipmentSlots(randomsource, difficultyInstance);
        if (this.f_19796_.nextFloat() < 0.3F) {
            this.setVariant(this.f_19796_.nextInt(2));
            this.setDwarf(false);
        } else {
            this.setDwarf(true);
        }
        return spawnData;
    }

    public boolean isFullyHidden() {
        return this.isHiding() && this.hidingProgress >= 10.0F;
    }

    @Override
    public void tick() {
        this.f_19794_ = true;
        super.m_8119_();
        this.prevHidingProgress = this.hidingProgress;
        this.f_19794_ = false;
        if (this.isHiding() && this.hidingProgress < 10.0F) {
            this.hidingProgress++;
        }
        if (!this.isHiding() && this.hidingProgress > 0.0F) {
            this.hidingProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            double xzSpeed = this.m_20184_().horizontalDistance();
            double distToFloor = (double) Mth.clamp(this.calculateDistanceToFloor(), -1.0F, 1.0F);
            if (Math.abs(distToFloor) > 0.01 && xzSpeed < 0.05 && !this.isActuallyInAWall()) {
                if (distToFloor < 0.0) {
                    this.m_20256_(this.m_20184_().add(0.0, -Math.min(distToFloor * 0.1F, 0.0), 0.0));
                } else if (distToFloor > 0.0) {
                    this.m_20256_(this.m_20184_().add(0.0, -Math.max(distToFloor * 0.1F, 0.0), 0.0));
                }
            }
            if (this.lastPosition != null && this.lastPosition.m_123331_(this.m_20183_()) > 2.5 && Math.abs(distToFloor) < 0.5) {
                this.m_5496_(AMSoundRegistry.UNDERMINER_STEP.get(), 1.0F, 0.75F + this.f_19796_.nextFloat() * 0.25F);
                this.lastPosition = this.m_20183_();
                if (this.f_19796_.nextFloat() < 0.015F && !this.m_9236_().m_45527_(this.lastPosition)) {
                    this.m_5496_((SoundEvent) SoundEvents.AMBIENT_CAVE.get(), 3.0F, 0.75F + this.f_19796_.nextFloat() * 0.25F);
                }
            }
            Player player = this.m_9236_().m_45924_(this.m_20185_(), this.m_20186_(), this.m_20189_(), AMConfig.underminerDisappearDistance, true);
            if (player == null || this.lastGivenStack != null || this.m_5448_() != null && this.m_5448_().isAlive()) {
                this.setHiding(false);
            } else {
                this.setHiding(true);
                this.m_21391_(player, 360.0F, 360.0F);
            }
        }
        this.m_5618_(this.m_146908_());
        if (this.mineCooldown > 0) {
            this.mineCooldown--;
        }
        if (this.resetStackTime > 0) {
            this.resetStackTime--;
            if (this.resetStackTime == 0) {
                this.lastGivenStack = null;
            }
        }
        if (this.f_19804_.get(VISUALLY_MINING)) {
            this.m_6674_(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return stack.is(AMTagRegistry.UNDERMINER_ORES);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (itemstack.is(AMTagRegistry.UNDERMINER_ORES)) {
            this.m_21053_(itemEntity);
            this.m_7938_(itemEntity, itemstack.getCount());
            itemEntity.m_146870_();
            this.mineAIFlag = this.lastGivenStack == null || !ItemStack.isSameItem(this.lastGivenStack, itemEntity.getItem());
            this.lastGivenStack = itemEntity.getItem();
            this.resetStackTime = 2000 + this.f_19796_.nextInt(1200);
            this.mineCooldown = 0;
        } else {
            super.m_7581_(itemEntity);
        }
    }

    @Override
    protected void jumpFromGround() {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isExtraSpooky() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return AlexsMobs.isAprilFools() || AlexsMobs.isHalloween() || s != null && s.toLowerCase().contains("herobrine");
    }

    private boolean isActuallyInAWall() {
        float f = this.m_6972_(this.m_20089_()).width * 0.1F;
        AABB aabb = AABB.ofSize(this.m_146892_(), (double) f, 1.0E-6, (double) f);
        return BlockPos.betweenClosedStream(aabb).anyMatch(p_201942_ -> {
            BlockState blockstate = this.m_9236_().getBlockState(p_201942_);
            return !blockstate.m_60795_() && blockstate.m_60828_(this.m_9236_(), p_201942_) && Shapes.joinIsNotEmpty(blockstate.m_60812_(this.m_9236_(), p_201942_).move((double) p_201942_.m_123341_(), (double) p_201942_.m_123342_(), (double) p_201942_.m_123343_()), Shapes.create(aabb), BooleanOp.AND);
        });
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public float getBrightness() {
        return 1.0F;
    }

    public float getMiningProgress() {
        return this.f_19804_.get(MINING_PROGRESS);
    }

    public void setMiningProgress(float f) {
        this.f_19804_.set(MINING_PROGRESS, f);
    }

    private List<BlockPos> getNearbyObscuredOres(int range, int maxOres) {
        List<BlockPos> obscuredBlocks = new ArrayList();
        BlockPos blockpos = this.m_20183_();
        int half = range / 2;
        for (int i = 0; i <= half && i >= -half; i = (i <= 0 ? 1 : 0) - i) {
            for (int j = 0; j <= range && j >= -range; j = (j <= 0 ? 1 : 0) - j) {
                for (int k = 0; k <= range && k >= -range; k = (k <= 0 ? 1 : 0) - k) {
                    BlockPos offset = blockpos.offset(j, i, k);
                    BlockState state = this.m_9236_().getBlockState(offset);
                    if (this.isValidMiningBlock(state)) {
                        if (obscuredBlocks.size() >= maxOres) {
                            break;
                        }
                        BlockPos obscured = this.getObscuringBlockOf(offset);
                        if (obscured != null) {
                            obscuredBlocks.add(obscured);
                        }
                    }
                }
            }
        }
        return obscuredBlocks;
    }

    private boolean isValidMiningBlock(BlockState state) {
        return this.lastGivenStack != null ? this.lastGivenStack.getItem() == state.m_60734_().asItem() : state.m_204336_(Tags.Blocks.ORES);
    }

    @Override
    public void aiStep() {
        this.m_21203_();
        super.m_8107_();
    }

    @Override
    public boolean isAttackable() {
        return !this.isFullyHidden() && super.m_6097_();
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return this.isFullyHidden() || super.m_7313_(entity);
    }

    private BlockPos getObscuringBlockOf(BlockPos target) {
        Vec3 eyes = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        HitResult hitResult = this.m_9236_().m_45547_(new ClipContext(eyes, Vec3.atCenterOf(target), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult instanceof BlockHitResult && !((BlockHitResult) hitResult).getBlockPos().equals(target)) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
            return pos.m_123331_(target) > 4.0 ? null : pos;
        } else {
            return null;
        }
    }

    private boolean hasPick() {
        return this.m_21120_(InteractionHand.MAIN_HAND).is(AMItemRegistry.GHOSTLY_PICKAXE.get());
    }

    private class MineGoal extends Goal {

        private BlockPos minePretendPos = null;

        private BlockState minePretendStartState = null;

        private int mineTime = 0;

        public MineGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (EntityUnderminer.this.mineCooldown == 0 && EntityUnderminer.this.hasPick() && !EntityUnderminer.this.isHiding() && !EntityUnderminer.this.isActuallyInAWall() && EntityUnderminer.this.m_217043_().nextInt(30) == 0) {
                List<BlockPos> obscuredOres = EntityUnderminer.this.getNearbyObscuredOres(16, 8);
                BlockPos nearest = null;
                double nearestDist = Double.MAX_VALUE;
                if (!obscuredOres.isEmpty()) {
                    for (BlockPos obscuredPos : obscuredOres) {
                        double dist = EntityUnderminer.this.m_20182_().distanceTo(Vec3.atCenterOf(obscuredPos));
                        if (nearestDist > dist) {
                            nearest = obscuredPos;
                            nearestDist = dist;
                        }
                    }
                }
                EntityUnderminer.this.mineAIFlag = false;
                this.minePretendPos = nearest;
                return this.minePretendPos != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.minePretendPos != null && EntityUnderminer.this.hasPick() && !EntityUnderminer.this.isHiding() && !EntityUnderminer.this.mineAIFlag && this.minePretendStartState != null && this.minePretendStartState.equals(EntityUnderminer.this.m_9236_().getBlockState(this.minePretendPos)) && this.mineTime < 200;
        }

        @Override
        public void start() {
            if (this.minePretendPos != null) {
                this.minePretendStartState = EntityUnderminer.this.m_9236_().getBlockState(this.minePretendPos);
            }
        }

        @Override
        public void stop() {
            if (this.minePretendPos != null && this.minePretendStartState != null && !this.minePretendStartState.equals(EntityUnderminer.this.m_9236_().getBlockState(this.minePretendPos))) {
                for (ServerPlayer serverplayerentity : EntityUnderminer.this.m_9236_().m_45976_(ServerPlayer.class, EntityUnderminer.this.m_20191_().inflate(12.0, 12.0, 12.0))) {
                    AMAdvancementTriggerRegistry.UNDERMINE_UNDERMINER.trigger(serverplayerentity);
                }
            }
            this.minePretendPos = null;
            this.minePretendStartState = null;
            this.mineTime = 0;
            EntityUnderminer.this.f_19804_.set(EntityUnderminer.VISUALLY_MINING, false);
            EntityUnderminer.this.setMiningPos(null);
            EntityUnderminer.this.setMiningProgress(0.0F);
            if (EntityUnderminer.this.resetStackTime > 0) {
                EntityUnderminer.this.mineCooldown = 40;
            } else {
                EntityUnderminer.this.mineCooldown = 200 + EntityUnderminer.this.f_19796_.nextInt(200);
            }
        }

        @Override
        public void tick() {
            if (this.minePretendPos != null && this.minePretendStartState != null) {
                this.mineTime++;
                double distSqr = EntityUnderminer.this.m_20275_((double) ((float) this.minePretendPos.m_123341_() + 0.5F), (double) ((float) this.minePretendPos.m_123342_() + 0.5F), (double) ((float) this.minePretendPos.m_123343_() + 0.5F));
                if (distSqr < 6.5) {
                    EntityUnderminer.this.m_21573_().stop();
                    if (EntityUnderminer.this.m_21573_().isDone()) {
                        EntityUnderminer.this.setMiningPos(this.minePretendPos);
                        EntityUnderminer.this.setMiningProgress((1.0F + (float) Math.cos((double) ((float) this.mineTime * 0.1F) + Math.PI)) * 0.5F);
                        double d1 = (double) ((float) this.minePretendPos.m_123343_() + 0.5F) - EntityUnderminer.this.m_20189_();
                        double d3 = (double) ((float) this.minePretendPos.m_123342_() + 0.5F) - EntityUnderminer.this.m_20186_();
                        double d2 = (double) ((float) this.minePretendPos.m_123341_() + 0.5F) - EntityUnderminer.this.m_20185_();
                        float f = Mth.sqrt((float) (d2 * d2 + d1 * d1));
                        EntityUnderminer.this.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                        EntityUnderminer.this.m_146926_((float) (Mth.atan2(d3, (double) f) * 180.0F / (float) Math.PI) + (float) Math.sin((double) ((float) EntityUnderminer.this.f_19797_ * 0.1F)));
                        EntityUnderminer.this.f_19804_.set(EntityUnderminer.VISUALLY_MINING, true);
                        if (this.mineTime % 10 == 0) {
                            SoundType soundType = this.minePretendStartState.m_60734_().getSoundType(this.minePretendStartState, EntityUnderminer.this.m_9236_(), this.minePretendPos, EntityUnderminer.this);
                            EntityUnderminer.this.m_216990_(soundType.getHitSound());
                        }
                    }
                } else {
                    EntityUnderminer.this.f_19804_.set(EntityUnderminer.VISUALLY_MINING, false);
                    EntityUnderminer.this.setMiningPos(null);
                    EntityUnderminer.this.m_21573_().moveTo((double) ((float) this.minePretendPos.m_123341_() + 0.5F), (double) ((float) this.minePretendPos.m_123342_() + 0.5F), (double) ((float) this.minePretendPos.m_123343_() + 0.5F), 1.0);
                }
            }
        }
    }

    private class PathNavigator extends GroundPathNavigation {

        public PathNavigator(EntityUnderminer underminer, Level level) {
            super(underminer, EntityUnderminer.this.m_9236_());
        }

        @Override
        protected boolean canUpdatePath() {
            return !this.f_26494_.m_20159_();
        }

        @Override
        protected Vec3 getTempMobPos() {
            return this.f_26494_.m_20182_();
        }
    }
}