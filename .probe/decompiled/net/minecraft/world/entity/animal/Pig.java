package net.minecraft.world.entity.animal;

import com.google.common.collect.UnmodifiableIterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Pig extends Animal implements ItemSteerable, Saddleable {

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.INT);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);

    private final ItemBasedSteering steering = new ItemBasedSteering(this.f_19804_, DATA_BOOST_TIME, DATA_SADDLE_ID);

    public Pig(EntityType<? extends Pig> entityTypeExtendsPig0, Level level1) {
        super(entityTypeExtendsPig0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.25));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return !this.isSaddled() || !(this.m_146895_() instanceof Player $$0) || !$$0.m_21205_().is(Items.CARROT_ON_A_STICK) && !$$0.m_21206_().is(Items.CARROT_ON_A_STICK) ? null : $$0;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_BOOST_TIME.equals(entityDataAccessor0) && this.m_9236_().isClientSide) {
            this.steering.onSynced();
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_SADDLE_ID, false);
        this.f_19804_.define(DATA_BOOST_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.steering.addAdditionalSaveData(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.steering.readAdditionalSaveData(compoundTag0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        boolean $$2 = this.isFood(player0.m_21120_(interactionHand1));
        if (!$$2 && this.isSaddled() && !this.m_20160_() && !player0.isSecondaryUseActive()) {
            if (!this.m_9236_().isClientSide) {
                player0.m_20329_(this);
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult $$3 = super.mobInteract(player0, interactionHand1);
            if (!$$3.consumesAction()) {
                ItemStack $$4 = player0.m_21120_(interactionHand1);
                return $$4.is(Items.SADDLE) ? $$4.interactLivingEntity(player0, this, interactionHand1) : InteractionResult.PASS;
            } else {
                return $$3;
            }
        }
    }

    @Override
    public boolean isSaddleable() {
        return this.m_6084_() && !this.m_6162_();
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isSaddled()) {
            this.m_19998_(Items.SADDLE);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource0) {
        this.steering.setSaddle(true);
        if (soundSource0 != null) {
            this.m_9236_().playSound(null, this, SoundEvents.PIG_SADDLE, soundSource0, 0.5F, 1.0F);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Direction $$1 = this.m_6374_();
        if ($$1.getAxis() == Direction.Axis.Y) {
            return super.m_7688_(livingEntity0);
        } else {
            int[][] $$2 = DismountHelper.offsetsForDirection($$1);
            BlockPos $$3 = this.m_20183_();
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            UnmodifiableIterator var6 = livingEntity0.getDismountPoses().iterator();
            while (var6.hasNext()) {
                Pose $$5 = (Pose) var6.next();
                AABB $$6 = livingEntity0.getLocalBoundsForPose($$5);
                for (int[] $$7 : $$2) {
                    $$4.set($$3.m_123341_() + $$7[0], $$3.m_123342_(), $$3.m_123343_() + $$7[1]);
                    double $$8 = this.m_9236_().m_45573_($$4);
                    if (DismountHelper.isBlockFloorValid($$8)) {
                        Vec3 $$9 = Vec3.upFromBottomCenterOf($$4, $$8);
                        if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity0, $$6.move($$9))) {
                            livingEntity0.m_20124_($$5);
                            return $$9;
                        }
                    }
                }
            }
            return super.m_7688_(livingEntity0);
        }
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (serverLevel0.m_46791_() != Difficulty.PEACEFUL) {
            ZombifiedPiglin $$2 = EntityType.ZOMBIFIED_PIGLIN.create(serverLevel0);
            if ($$2 != null) {
                $$2.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                $$2.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
                $$2.m_21557_(this.m_21525_());
                $$2.m_6863_(this.m_6162_());
                if (this.m_8077_()) {
                    $$2.m_6593_(this.m_7770_());
                    $$2.m_20340_(this.m_20151_());
                }
                $$2.m_21530_();
                serverLevel0.addFreshEntity($$2);
                this.m_146870_();
            } else {
                super.m_8038_(serverLevel0, lightningBolt1);
            }
        } else {
            super.m_8038_(serverLevel0, lightningBolt1);
        }
    }

    @Override
    protected void tickRidden(Player player0, Vec3 vec1) {
        super.m_274498_(player0, vec1);
        this.m_19915_(player0.m_146908_(), player0.m_146909_() * 0.5F);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        this.steering.tickBoost();
    }

    @Override
    protected Vec3 getRiddenInput(Player player0, Vec3 vec1) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    @Override
    protected float getRiddenSpeed(Player player0) {
        return (float) (this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.225 * (double) this.steering.boostFactor());
    }

    @Override
    public boolean boost() {
        return this.steering.boost(this.m_217043_());
    }

    @Nullable
    public Pig getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.PIG.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return FOOD_ITEMS.test(itemStack0);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.6F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }
}