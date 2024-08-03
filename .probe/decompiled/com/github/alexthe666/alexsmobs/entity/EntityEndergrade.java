package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EndergradeAIBreakFlowers;
import com.github.alexthe666.alexsmobs.entity.ai.EndergradeAITargetItems;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityEndergrade extends Animal implements FlyingAnimal {

    private static final EntityDataAccessor<Integer> BITE_TICK = SynchedEntityData.defineId(EntityEndergrade.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(EntityEndergrade.class, EntityDataSerializers.BOOLEAN);

    public float tartigradePitch = 0.0F;

    public float prevTartigradePitch = 0.0F;

    public float biteProgress = 0.0F;

    public float prevBiteProgress = 0.0F;

    public boolean stopWandering = false;

    public boolean hasItemTarget = false;

    protected EntityEndergrade(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new EntityEndergrade.MoveHelperController(this);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    public static boolean canEndergradeSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return !worldIn.m_8055_(pos.below()).m_60795_();
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new DirectPathNavigator(this, worldIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSaddled(compound.getBoolean("Saddled"));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BITE_TICK, 0);
        this.f_19804_.define(SADDLED, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new EndergradeAIBreakFlowers(this));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.2) {

            @Override
            public void start() {
                super.m_8056_();
                EntityEndergrade.this.stopWandering = true;
            }

            @Override
            public void stop() {
                super.stop();
                EntityEndergrade.this.stopWandering = false;
            }
        });
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.CHORUS_FRUIT), false) {

            @Override
            public void start() {
                super.start();
                EntityEndergrade.this.stopWandering = true;
            }

            @Override
            public void stop() {
                super.stop();
                EntityEndergrade.this.stopWandering = false;
            }
        });
        this.f_21345_.addGoal(4, new EntityEndergrade.RandomFlyGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new EndergradeAITargetItems(this, true));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player player && (player.m_21205_().getItem() == AMItemRegistry.CHORUS_ON_A_STICK.get() || player.m_21206_().getItem() == AMItemRegistry.CHORUS_ON_A_STICK.get())) {
                return player;
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ENDERGRADE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ENDERGRADE_HURT.get();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (item == Items.SADDLE && !this.isSaddled()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setSaddled(true);
            return InteractionResult.SUCCESS;
        } else if (item == Items.CHORUS_FRUIT && this.m_21023_(AMEffectRegistry.ENDER_FLU.get())) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.m_5634_(8.0F);
            this.m_21195_(AMEffectRegistry.ENDER_FLU.get());
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult type = super.mobInteract(player, hand);
            if (type != InteractionResult.SUCCESS && !this.isFood(itemstack) && !player.m_6144_() && this.isSaddled()) {
                player.m_20329_(this);
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.CHORUS_FRUIT;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float radius = -0.25F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.m_20189_() + extraZ);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.f_267362_.speed());
        float f1 = this.f_267362_.position();
        return (double) this.m_20206_() - 0.1 + (double) (0.12F * Mth.cos(f1 * 0.7F) * 0.7F * f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isSaddled() {
        return this.f_19804_.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.f_19804_.set(SADDLED, saddled);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevTartigradePitch = this.tartigradePitch;
        this.prevBiteProgress = this.biteProgress;
        float f2 = (float) (-((double) ((float) this.m_20184_().y * 3.0F) * 180.0F / (float) Math.PI));
        this.tartigradePitch = f2;
        if (this.m_20184_().lengthSqr() > 0.005F) {
            float angleMotion = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraXMotion = (double) (-0.2F * Mth.sin((float) (Math.PI + (double) angleMotion)));
            double extraZMotion = (double) (-0.2F * Mth.cos(angleMotion));
            this.m_9236_().addParticle(ParticleTypes.END_ROD, this.m_20208_(0.5), this.m_20186_() + 0.3, this.m_20262_(0.5), extraXMotion, 0.0, extraZMotion);
        }
        int tick = this.f_19804_.get(BITE_TICK);
        if (tick > 0) {
            this.f_19804_.set(BITE_TICK, tick - 1);
            this.biteProgress++;
        } else if (this.biteProgress > 0.0F) {
            this.biteProgress--;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private BlockPos getGroundPosition(BlockPos radialPos) {
        while (radialPos.m_123342_() > 1 && this.m_9236_().m_46859_(radialPos)) {
            radialPos = radialPos.below();
        }
        return radialPos.m_123342_() <= 1 ? new BlockPos(radialPos.m_123341_(), this.m_9236_().getSeaLevel(), radialPos.m_123343_()) : radialPos;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem() == Items.CHORUS_FRUIT || stack.getItem() == Items.CHORUS_FLOWER;
    }

    public void onGetItem(ItemEntity targetEntity) {
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
        this.m_5634_(5.0F);
    }

    public void bite() {
        this.f_19804_.set(BITE_TICK, 5);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.endergradeSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.ENDERGRADE.get().create(p_241840_1_);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isSaddled() && !this.m_9236_().isClientSide) {
            this.m_19998_(Items.SADDLE);
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        if (player.f_20902_ != 0.0F) {
            this.m_6858_(true);
            Vec3 lookVec = player.m_20154_();
            if (player.f_20902_ < 0.0F) {
                lookVec = lookVec.yRot((float) Math.PI);
            }
            double y = lookVec.y * 0.35F;
            return new Vec3((double) player.f_20900_, y, (double) player.f_20902_);
        } else {
            this.m_6858_(false);
            return Vec3.ZERO;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
            this.m_274367_(1.0F);
            this.m_21573_().stop();
            this.m_6710_(null);
            this.m_6858_(true);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) (this.m_21133_(Attributes.MOVEMENT_SPEED) * (double) (this.m_20096_() ? 0.2F : 0.8F));
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    static class MoveHelperController extends MoveControl {

        private final EntityEndergrade parentEntity;

        public MoveHelperController(EntityEndergrade sunbird) {
            super(sunbird);
            this.parentEntity = sunbird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.STRAFE) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(0.0, vector3d.scale(this.f_24978_ * 0.05 / d0).y(), 0.0));
                float f = (float) this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.f_24978_ * f;
                this.f_24979_ = 1.0F;
                this.f_24980_ = 0.0F;
                this.f_24974_.setSpeed(f1);
                this.f_24974_.setZza(this.f_24979_);
                this.f_24974_.setXxa(this.f_24980_);
                this.f_24981_ = MoveControl.Operation.WAIT;
            } else if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                if (d0 < this.parentEntity.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    double localSpeed = this.f_24978_;
                    if (this.parentEntity.m_20160_()) {
                        localSpeed *= 1.5;
                    }
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(localSpeed * 0.005 / d0)));
                    if (this.parentEntity.m_5448_() == null) {
                        Vec3 vector3d1 = this.parentEntity.m_20184_();
                        this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    } else {
                        double d2 = this.parentEntity.m_5448_().m_20185_() - this.parentEntity.m_20185_();
                        double d1 = this.parentEntity.m_5448_().m_20189_() - this.parentEntity.m_20189_();
                        this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
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

    static class RandomFlyGoal extends Goal {

        private final EntityEndergrade parentEntity;

        private BlockPos target = null;

        public RandomFlyGoal(EntityEndergrade mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (this.parentEntity.stopWandering || this.parentEntity.hasItemTarget) {
                return false;
            } else if (movementcontroller.hasWanted() && this.target != null) {
                return false;
            } else {
                this.target = this.getBlockInViewEndergrade();
                if (this.target != null) {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                }
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && !this.parentEntity.stopWandering && !this.parentEntity.hasItemTarget && this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) > 2.4 && this.parentEntity.m_21566_().hasWanted() && !this.parentEntity.f_19862_;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.target = this.getBlockInViewEndergrade();
            }
            if (this.target != null) {
                this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewEndergrade() {
            float radius = (float) (1 + this.parentEntity.m_217043_().nextInt(5));
            float neg = this.parentEntity.m_217043_().nextBoolean() ? 1.0F : -1.0F;
            float renderYawOffset = this.parentEntity.f_20883_;
            float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.parentEntity.m_217043_().nextFloat() * neg;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_() + 2.0, this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getGroundPosition(radialPos);
            BlockPos newPos = ground.above(1 + this.parentEntity.m_217043_().nextInt(6));
            return !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.parentEntity.m_20238_(Vec3.atCenterOf(newPos)) > 6.0 ? newPos : null;
        }
    }
}