package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PewenBranchBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.RelicheirusMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.RelicheirusNibblePewensGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.RelicheirusPushTreesGoal;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolverQuadruped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RelicheirusEntity extends DinosaurEntity implements IAnimatedEntity {

    public LegSolverQuadruped legSolver = new LegSolverQuadruped(-0.15F, 0.6F, 0.5F, 0.75F, 1.0F);

    private static final EntityDataAccessor<Integer> PECK_Y = SynchedEntityData.defineId(RelicheirusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> HELD_MOB_ID = SynchedEntityData.defineId(RelicheirusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> PUSHING_TREES_FOR = SynchedEntityData.defineId(RelicheirusEntity.class, EntityDataSerializers.INT);

    private Animation currentAnimation;

    private int animationTick;

    public static final Animation ANIMATION_SPEAK_1 = Animation.create(13);

    public static final Animation ANIMATION_SPEAK_2 = Animation.create(20);

    public static final Animation ANIMATION_EAT_TREE = Animation.create(40);

    public static final Animation ANIMATION_EAT_TRILOCARIS = Animation.create(50);

    public static final Animation ANIMATION_PUSH_TREE = Animation.create(60);

    public static final Animation ANIMATION_SCRATCH_1 = Animation.create(60);

    public static final Animation ANIMATION_SCRATCH_2 = Animation.create(40);

    public static final Animation ANIMATION_SHAKE = Animation.create(30);

    public static final Animation ANIMATION_MELEE_SLASH_1 = Animation.create(20);

    public static final Animation ANIMATION_MELEE_SLASH_2 = Animation.create(20);

    private float prevRaiseArmsAmount = 0.0F;

    private float raiseArmsAmount = 0.0F;

    public RelicheirusEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PECK_Y, 0);
        this.f_19804_.define(HELD_MOB_ID, -1);
        this.f_19804_.define(PUSHING_TREES_FOR, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 12.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 120.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new RelicheirusMeleeGoal(this));
        this.f_21345_.addGoal(2, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(ACBlockRegistry.TREE_STAR.get()), false));
        this.f_21345_.addGoal(5, new RelicheirusPushTreesGoal(this, 25));
        this.f_21345_.addGoal(6, new RelicheirusNibblePewensGoal(this, 20));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, RelicheirusEntity.class));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, TrilocarisEntity.class, 100, true, false, null));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.RELICHEIRUS_STEP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.99F * dimensions.height;
    }

    @Override
    public boolean onFeedMixture(ItemStack itemStack, Player player) {
        if (itemStack.is(ACItemRegistry.PRIMORDIAL_SOUP.get())) {
            this.setPushingTreesFor(1200);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult prev = super.mobInteract(player, hand);
        ItemStack itemstack = player.m_21120_(hand);
        if (!prev.consumesAction() && itemstack.is(ACItemRegistry.PRIMORDIAL_SOUP.get())) {
            if (!itemstack.getCraftingRemainingItem().isEmpty()) {
                this.m_19983_(itemstack.getCraftingRemainingItem().copy());
            }
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        } else {
            return prev;
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimation() != ANIMATION_EAT_TRILOCARIS) {
            super.m_5997_(x, y, z);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimation() != ANIMATION_EAT_TREE) {
            this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.getHeadRotSpeed());
        }
        this.prevRaiseArmsAmount = this.raiseArmsAmount;
        this.legSolver.update(this, this.f_20883_, this.getScale());
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.shouldRaiseArms() && this.raiseArmsAmount < 5.0F) {
            this.raiseArmsAmount++;
        }
        if (!this.shouldRaiseArms() && this.raiseArmsAmount > 0.0F) {
            this.raiseArmsAmount--;
        }
        if (this.f_19797_ % 100 == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(2.0F);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isStillEnough() && this.f_19796_.nextInt(200) == 0 && this.getAnimation() == NO_ANIMATION && !this.isDancing()) {
                float rand = this.f_19796_.nextFloat();
                Animation idle;
                if (rand < 0.15F) {
                    idle = ANIMATION_SCRATCH_1;
                } else if (rand < 0.3F) {
                    idle = ANIMATION_SCRATCH_2;
                } else {
                    idle = ANIMATION_SHAKE;
                }
                this.setAnimation(idle);
            }
            boolean held = false;
            LivingEntity target = this.m_5448_();
            if (target != null && target.m_20270_(this) < 10.0F && target instanceof TrilocarisEntity && this.getAnimation() == ANIMATION_EAT_TRILOCARIS) {
                if (this.getAnimationTick() < 20) {
                    held = true;
                    this.setHeldMobId(target.m_19879_());
                } else if (this.getAnimationTick() <= 50) {
                    Vec3 trilocarisPos = this.getTrilocarisPos();
                    target.m_146884_(trilocarisPos);
                    if (this.getAnimationTick() >= 45 && target.isAlive()) {
                        target.hurt(this.m_269291_().mobAttack(this), 20.0F);
                    }
                    held = true;
                    target.f_19789_ = 0.0F;
                }
            }
            if (!held && this.getHeldMobId() != -1) {
                this.setHeldMobId(-1);
            }
            if (this.getPushingTreesFor() > 0) {
                this.setPushingTreesFor(this.getPushingTreesFor() - 1);
            }
        }
        if (this.getAnimation() == ANIMATION_SPEAK_1 && this.getAnimationTick() == 1 || this.getAnimation() == ANIMATION_SPEAK_2 && this.getAnimationTick() == 1) {
            this.actuallyPlayAmbientSound();
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0, 3.0, 3.0);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ACEntityRegistry.RELICHEIRUS.get().create(level);
    }

    private Vec3 getTrilocarisPos() {
        Vec3 triloUp = new Vec3(0.0, 0.0, 1.5);
        if (this.getAnimation() == ANIMATION_EAT_TRILOCARIS && (float) this.getAnimationTick() >= 15.0F) {
            float anim1 = Math.min((float) this.getAnimationTick() - 15.0F, 15.0F) / 15.0F;
            float anim2 = Math.min((float) this.getAnimationTick(), 15.0F) / 15.0F;
            triloUp = triloUp.add(0.0, (double) ((this.m_20192_() + 1.0F) * anim1), (double) (anim2 * -1.0F + 1.0F));
        }
        Vec3 head = triloUp.xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0));
        return this.m_20182_().add(head);
    }

    private boolean isStillEnough() {
        return this.m_20184_().horizontalDistance() < 0.05;
    }

    public boolean shouldRaiseArms() {
        return this.getAnimation() == ANIMATION_EAT_TREE || this.getAnimation() == ANIMATION_PUSH_TREE || this.getAnimation() == ANIMATION_SCRATCH_1 || this.getAnimation() == ANIMATION_SCRATCH_2 || this.getAnimation() == ANIMATION_MELEE_SLASH_1 || this.getAnimation() == ANIMATION_MELEE_SLASH_2;
    }

    public void setPeckY(int y) {
        this.f_19804_.set(PECK_Y, y);
    }

    public int getPeckY() {
        return this.f_19804_.get(PECK_Y);
    }

    public void setHeldMobId(int i) {
        this.f_19804_.set(HELD_MOB_ID, i);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.getAnimation() == ANIMATION_EAT_TRILOCARIS || this.isDancing()) {
            vec3d = Vec3.ZERO;
        }
        super.travel(vec3d);
    }

    public int getHeldMobId() {
        return this.f_19804_.get(HELD_MOB_ID);
    }

    public Entity getHeldMob() {
        int id = this.getHeldMobId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    public void setPushingTreesFor(int time) {
        this.f_19804_.set(PUSHING_TREES_FOR, time);
    }

    public int getPushingTreesFor() {
        return this.f_19804_.get(PUSHING_TREES_FOR);
    }

    public float getRaiseArmsAmount(float partialTick) {
        return (this.prevRaiseArmsAmount + (this.raiseArmsAmount - this.prevRaiseArmsAmount) * partialTick) * 0.2F;
    }

    @Override
    public int getHeadRotSpeed() {
        return 5;
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_SPEAK_2 : ANIMATION_SPEAK_1);
        }
    }

    public void actuallyPlayAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.m_5496_(soundevent, this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SPEAK_1, ANIMATION_SPEAK_2, ANIMATION_EAT_TREE, ANIMATION_EAT_TRILOCARIS, ANIMATION_PUSH_TREE, ANIMATION_SCRATCH_1, ANIMATION_SCRATCH_2, ANIMATION_SHAKE, ANIMATION_MELEE_SLASH_1, ANIMATION_MELEE_SLASH_2 };
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.25F : 1.0F;
    }

    public BlockPos getStandAtTreePos(BlockPos target) {
        Vec3 vec3 = Vec3.atCenterOf(target).subtract(this.m_20182_());
        float f = -((float) Mth.atan2(vec3.x, vec3.z)) * 180.0F / (float) Math.PI;
        BlockState state = this.m_9236_().getBlockState(target);
        Direction dir = Direction.fromYRot((double) f);
        if (state.m_60713_(ACBlockRegistry.PEWEN_BRANCH.get())) {
            dir = Direction.fromYRot((double) ((Integer) state.m_61143_(PewenBranchBlock.ROTATION) * 45));
        }
        if (this.m_9236_().getBlockState(target.below()).m_60795_()) {
            target = target.relative(dir);
        }
        return target.relative(dir.getOpposite(), 4).atY((int) this.m_20186_());
    }

    public boolean lockTreePosition(BlockPos target) {
        Vec3 vec3 = Vec3.atCenterOf(target).subtract(this.m_20182_());
        float f = -((float) Mth.atan2(vec3.x, vec3.z)) * 180.0F / (float) Math.PI;
        BlockState state = this.m_9236_().getBlockState(target);
        Direction dir = Direction.fromYRot((double) f);
        if (state.m_60713_(ACBlockRegistry.PEWEN_BRANCH.get())) {
            dir = Direction.fromYRot((double) ((Integer) state.m_61143_(PewenBranchBlock.ROTATION) * 45));
        }
        float targetRot = Mth.approachDegrees(this.m_146908_(), dir.toYRot(), 20.0F);
        this.m_146922_(targetRot);
        this.m_5616_(targetRot);
        this.f_20883_ = targetRot;
        if (this.m_9236_().getBlockState(target.below()).m_60795_()) {
            target = target.relative(dir);
        }
        Vec3 vec31 = Vec3.atCenterOf(target.relative(dir.getOpposite(), 2));
        Vec3 vec32 = vec31.subtract(this.m_20182_());
        if (vec32.length() > 1.0) {
            vec32 = vec32.normalize();
        }
        Vec3 delta = new Vec3(vec32.x * 0.1F, 0.0, vec32.z * 0.1F);
        this.m_20256_(this.m_20184_().add(delta));
        return this.m_20275_(vec31.x, this.m_20186_(), vec31.z) < 4.0 && Mth.degreesDifferenceAbs(this.m_146908_(), dir.toYRot()) < 7.0F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ACBlockRegistry.TREE_STAR.get().asItem());
    }

    @Override
    public BlockState createEggBlockState() {
        return ACBlockRegistry.RELICHEIRUS_EGG.get().defaultBlockState();
    }

    public float getStepHeight() {
        return 1.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.RELICHEIRUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.RELICHEIRUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.RELICHEIRUS_DEATH.get();
    }
}