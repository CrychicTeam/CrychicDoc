package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntitySoulVulture extends Monster implements FlyingAnimal {

    public static final ResourceLocation SOUL_LOOT = new ResourceLocation("alexsmobs", "entities/soul_vulture_heart");

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntitySoulVulture.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TACKLING = SynchedEntityData.defineId(EntitySoulVulture.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> PERCH_POS = SynchedEntityData.defineId(EntitySoulVulture.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Integer> SOUL_LEVEL = SynchedEntityData.defineId(EntitySoulVulture.class, EntityDataSerializers.INT);

    public float prevFlyProgress;

    public float flyProgress;

    public float prevTackleProgress;

    public float tackleProgress;

    private boolean isLandNavigator;

    private int perchSearchCooldown = 0;

    private int landingCooldown = 0;

    private int tackleCooldown = 0;

    protected EntitySoulVulture(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.hasSoulHeart() ? SOUL_LOOT : super.m_7582_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.soulVultureSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canVultureSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.below();
        boolean spawnBlock = worldIn.m_8055_(blockpos).m_204336_(AMTagRegistry.SOUL_VULTURE_SPAWNS);
        return reason == MobSpawnType.SPAWNER || spawnBlock && m_217057_(AMEntityRegistry.SOUL_VULTURE.get(), worldIn, reason, pos, randomIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SOUL_VULTURE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SOUL_VULTURE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SOUL_VULTURE_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 12.0).add(Attributes.FOLLOW_RANGE, 18.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public boolean isPerchBlock(BlockPos pos, BlockState state) {
        return this.m_9236_().m_46859_(pos.above()) && this.m_9236_().m_46859_(pos.above(2)) && state.m_204336_(AMTagRegistry.SOUL_VULTURE_PERCHES);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new EntitySoulVulture.AICirclePerch(this));
        this.f_21345_.addGoal(2, new EntitySoulVulture.AIFlyRandom(this));
        this.f_21345_.addGoal(3, new EntitySoulVulture.AITackleMelee(this));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 20.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntitySoulVulture.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, true));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, AbstractPiglin.class, true));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, AbstractVillager.class, true));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return true;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntitySoulVulture.MoveHelper(this);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(TACKLING, false);
        this.f_19804_.define(PERCH_POS, Optional.empty());
        this.f_19804_.define(SOUL_LEVEL, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Flying", this.isFlying());
        if (this.getPerchPos() != null) {
            compound.putInt("PerchX", this.getPerchPos().m_123341_());
            compound.putInt("PerchY", this.getPerchPos().m_123342_());
            compound.putInt("PerchZ", this.getPerchPos().m_123343_());
        }
        compound.putInt("SoulLevel", this.getSoulLevel());
        compound.putInt("LandingCooldown", this.landingCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setSoulLevel(compound.getInt("SoulLevel"));
        this.landingCooldown = compound.getInt("LandingCooldown");
        if (compound.contains("PerchX") && compound.contains("PerchY") && compound.contains("PerchZ")) {
            this.setPerchPos(new BlockPos(compound.getInt("PerchX"), compound.getInt("PerchY"), compound.getInt("PerchZ")));
        }
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isTackling() {
        return this.f_19804_.get(TACKLING);
    }

    public void setTackling(boolean tackling) {
        this.f_19804_.set(TACKLING, tackling);
    }

    public BlockPos getPerchPos() {
        return (BlockPos) this.f_19804_.get(PERCH_POS).orElse(null);
    }

    public void setPerchPos(BlockPos pos) {
        this.f_19804_.set(PERCH_POS, Optional.ofNullable(pos));
    }

    public int getSoulLevel() {
        return this.f_19804_.get(SOUL_LEVEL);
    }

    public void setSoulLevel(int tackling) {
        this.f_19804_.set(SOUL_LEVEL, tackling);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevTackleProgress = this.tackleProgress;
        this.prevFlyProgress = this.flyProgress;
        if (!this.m_9236_().isClientSide) {
            if (this.perchSearchCooldown > 0) {
                this.perchSearchCooldown--;
            }
            if (this.m_5448_() != null && this.m_5448_().isAlive()) {
                this.setPerchPos(this.m_5448_().m_20183_().above(7));
            } else if (this.getPerchPos() != null && !this.isPerchBlock(this.getPerchPos(), this.m_9236_().getBlockState(this.getPerchPos()))) {
                this.setPerchPos(null);
            }
            if (this.getPerchPos() == null && this.perchSearchCooldown == 0) {
                this.perchSearchCooldown = 20 + this.f_19796_.nextInt(20);
                this.setPerchPos(this.findNewPerchPos());
            }
            if (!this.isFlying() && this.landingCooldown == 0 && (this.getPerchPos() == null || this.shouldLeavePerch(this.getPerchPos()))) {
                this.setFlying(true);
            }
            if (!this.isFlying() && this.m_5448_() != null) {
                this.setFlying(true);
            }
            if (this.landingCooldown > 0 && this.isFlying() && this.m_20096_() && this.m_5448_() == null) {
                this.setFlying(false);
            }
        }
        boolean flying = this.isFlying();
        if (flying) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else {
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.flyProgress > 0.0F) {
                this.flyProgress--;
            }
        }
        if (this.isTackling()) {
            if (this.tackleProgress < 5.0F) {
                this.tackleProgress++;
            }
        } else if (this.tackleProgress > 0.0F) {
            this.tackleProgress--;
        }
        if (this.landingCooldown > 0) {
            this.landingCooldown--;
        }
        if (this.tackleCooldown > 0) {
            this.tackleCooldown--;
        }
        if (this.isFlying()) {
            this.m_20242_(true);
        } else {
            this.m_20242_(false);
        }
        if (this.m_9236_().isClientSide && this.hasSoulHeart()) {
            float radius = 0.25F + this.f_19796_.nextFloat() * 1.0F;
            float fly = this.flyProgress * 0.2F;
            float wingSpread = 15.0F + 65.0F * fly + (float) this.f_19796_.nextInt(5);
            float angle = (float) (Math.PI / 180.0) * ((float) (this.f_19796_.nextBoolean() ? -1 : 1) * (wingSpread + 180.0F) + this.f_20883_);
            float angleMotion = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            double mov = this.m_20184_().length();
            double extraXMotion = -mov * (double) Mth.sin((float) (Math.PI + (double) angleMotion));
            double extraZMotion = -mov * (double) Mth.cos(angleMotion);
            double yRandom = (double) (0.2F + this.f_19796_.nextFloat() * 0.3F);
            this.m_9236_().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.m_20185_() + extraX, this.m_20186_() + yRandom, this.m_20189_() + extraZ, extraXMotion, (double) (this.f_19796_.nextFloat() * 0.1F), extraZMotion);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 68) {
            for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.SOUL, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
        } else {
            super.m_7822_(id);
        }
    }

    public BlockPos findNewPerchPos() {
        BlockState beneathState = this.m_9236_().getBlockState(this.m_20099_());
        if (this.isPerchBlock(this.m_20099_(), beneathState)) {
            return this.m_20099_();
        } else {
            BlockPos blockpos = null;
            Random random = new Random();
            int range = 14;
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = this.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
                while (this.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                    blockpos1 = blockpos1.below();
                }
                if (this.isPerchBlock(blockpos1, this.m_9236_().getBlockState(blockpos1))) {
                    blockpos = blockpos1;
                }
            }
            return blockpos;
        }
    }

    private boolean shouldLeavePerch(BlockPos perchPos) {
        return this.m_20238_(Vec3.atCenterOf(perchPos)) > 13.0 || this.landingCooldown == 0;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean shouldSwoop() {
        return this.m_5448_() != null && this.tackleCooldown == 0;
    }

    public boolean hasSoulHeart() {
        return this.getSoulLevel() > 2;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    class AICirclePerch extends Goal {

        private final EntitySoulVulture vulture;

        float speed = 1.0F;

        float circlingTime = 0.0F;

        float circleDistance = 5.0F;

        float maxCirclingTime = 80.0F;

        boolean clockwise = false;

        private BlockPos targetPos;

        private int yLevel = 1;

        public AICirclePerch(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !this.vulture.shouldSwoop() && this.vulture.isFlying() && this.vulture.getPerchPos() != null;
        }

        @Override
        public void start() {
            this.circlingTime = 0.0F;
            this.speed = 0.8F + EntitySoulVulture.this.f_19796_.nextFloat() * 0.4F;
            this.yLevel = this.vulture.f_19796_.nextInt(3);
            this.maxCirclingTime = (float) (360 + this.vulture.f_19796_.nextInt(80));
            this.circleDistance = 5.0F + this.vulture.f_19796_.nextFloat() * 5.0F;
            this.clockwise = this.vulture.f_19796_.nextBoolean();
        }

        @Override
        public void stop() {
            this.circlingTime = 0.0F;
            this.speed = 0.8F + EntitySoulVulture.this.f_19796_.nextFloat() * 0.4F;
            this.yLevel = this.vulture.f_19796_.nextInt(3);
            this.maxCirclingTime = (float) (360 + this.vulture.f_19796_.nextInt(80));
            this.circleDistance = 5.0F + this.vulture.f_19796_.nextFloat() * 5.0F;
            this.clockwise = this.vulture.f_19796_.nextBoolean();
            this.vulture.tackleCooldown = 0;
        }

        @Override
        public void tick() {
            BlockPos encircle = this.vulture.getPerchPos();
            double localSpeed = (double) this.speed;
            if (this.vulture.m_5448_() != null) {
                localSpeed *= 1.55;
            }
            if (encircle != null) {
                this.circlingTime++;
                if (this.circlingTime > 360.0F) {
                    this.vulture.m_21566_().setWantedPosition((double) encircle.m_123341_() + 0.5, (double) encircle.m_123342_() + 1.1, (double) encircle.m_123343_() + 0.5, localSpeed);
                    if (this.vulture.f_19863_ || this.vulture.m_20275_((double) encircle.m_123341_() + 0.5, (double) encircle.m_123342_() + 1.1, (double) encircle.m_123343_() + 0.5) < 1.0) {
                        this.vulture.setFlying(false);
                        this.vulture.m_20256_(Vec3.ZERO);
                        this.vulture.landingCooldown = 400 + EntitySoulVulture.this.f_19796_.nextInt(1200);
                        this.stop();
                    }
                } else {
                    BlockPos circlePos = this.getVultureCirclePos(encircle);
                    if (circlePos != null) {
                        this.vulture.m_21566_().setWantedPosition((double) circlePos.m_123341_() + 0.5, (double) circlePos.m_123342_() + 0.5, (double) circlePos.m_123343_() + 0.5, localSpeed);
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        public BlockPos getVultureCirclePos(BlockPos target) {
            float angle = 0.05235988F * (this.clockwise ? -this.circlingTime : this.circlingTime);
            double extraX = (double) (this.circleDistance * Mth.sin(angle));
            double extraZ = (double) (this.circleDistance * Mth.cos(angle));
            BlockPos pos = new BlockPos((int) ((double) target.m_123341_() + extraX), target.m_123342_() + 1 + this.yLevel, (int) ((double) target.m_123343_() + extraZ));
            return this.vulture.m_9236_().m_46859_(pos) ? pos : null;
        }
    }

    private class AIFlyRandom extends Goal {

        private final EntitySoulVulture vulture;

        private BlockPos target = null;

        public AIFlyRandom(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.vulture.getPerchPos() == null && !this.vulture.shouldSwoop()) {
                MoveControl movementcontroller = this.vulture.m_21566_();
                if (movementcontroller.hasWanted() && this.target != null) {
                    return false;
                } else {
                    this.target = this.getBlockInViewVulture();
                    if (this.target != null) {
                        this.vulture.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.vulture.getPerchPos() == null && !this.vulture.shouldSwoop() ? this.target != null && this.vulture.m_20238_(Vec3.atCenterOf(this.target)) > 2.4 && this.vulture.m_21566_().hasWanted() && !this.vulture.f_19862_ : false;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.target = this.getBlockInViewVulture();
            }
            if (this.target != null) {
                this.vulture.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                if (this.vulture.m_20238_(Vec3.atCenterOf(this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewVulture() {
            float radius = -9.45F - (float) this.vulture.m_217043_().nextInt(10);
            float neg = this.vulture.m_217043_().nextBoolean() ? 1.0F : -1.0F;
            float renderYawOffset = this.vulture.f_20883_;
            float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.vulture.m_217043_().nextFloat() * neg;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = new BlockPos((int) (this.vulture.m_20185_() + extraX), (int) this.vulture.m_20186_(), (int) (this.vulture.m_20189_() + extraZ));
            while (EntitySoulVulture.this.m_9236_().m_46859_(radialPos) && radialPos.m_123342_() > 2) {
                radialPos = radialPos.below();
            }
            BlockPos newPos = radialPos.above(this.vulture.m_20186_() - (double) radialPos.m_123342_() > 16.0 ? 4 : this.vulture.m_217043_().nextInt(5) + 5);
            return !this.vulture.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.vulture.m_20238_(Vec3.atCenterOf(newPos)) > 6.0 ? newPos : null;
        }
    }

    private class AITackleMelee extends Goal {

        private final EntitySoulVulture vulture;

        public AITackleMelee(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.vulture.m_5448_() != null && this.vulture.shouldSwoop()) {
                this.vulture.setFlying(true);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void stop() {
            this.vulture.setTackling(false);
        }

        @Override
        public void tick() {
            if (this.vulture.isFlying()) {
                this.vulture.setTackling(true);
            } else {
                this.vulture.setTackling(false);
            }
            if (this.vulture.m_5448_() != null) {
                this.vulture.m_21566_().setWantedPosition(this.vulture.m_5448_().m_20185_(), this.vulture.m_5448_().m_20186_() + (double) this.vulture.m_5448_().m_20192_(), this.vulture.m_5448_().m_20189_(), 2.0);
                double d0 = this.vulture.m_20185_() - this.vulture.m_5448_().m_20185_();
                double d2 = this.vulture.m_20189_() - this.vulture.m_5448_().m_20189_();
                float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                this.vulture.m_146922_(f);
                this.vulture.f_20883_ = this.vulture.m_146908_();
                if (this.vulture.m_20191_().inflate(0.3F, 0.3F, 0.3F).intersects(this.vulture.m_5448_().m_20191_()) && this.vulture.tackleCooldown == 0) {
                    EntitySoulVulture.this.tackleCooldown = 100 + EntitySoulVulture.this.f_19796_.nextInt(200);
                    float dmg = (float) this.vulture.m_21051_(Attributes.ATTACK_DAMAGE).getValue();
                    if (this.vulture.m_5448_().hurt(this.vulture.m_269291_().mobAttack(this.vulture), dmg) && this.vulture.m_21223_() < this.vulture.m_21233_() - dmg && this.vulture.getSoulLevel() < 5) {
                        this.vulture.setSoulLevel(this.vulture.getSoulLevel() + 1);
                        this.vulture.m_5634_(dmg);
                        this.vulture.m_9236_().broadcastEntityEvent(this.vulture, (byte) 68);
                    }
                    this.stop();
                }
            }
        }
    }

    static class MoveHelper extends MoveControl {

        private final EntitySoulVulture parentEntity;

        public MoveHelper(EntitySoulVulture bird) {
            super(bird);
            this.parentEntity = bird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < 0.3) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.05 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }

        private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }
}