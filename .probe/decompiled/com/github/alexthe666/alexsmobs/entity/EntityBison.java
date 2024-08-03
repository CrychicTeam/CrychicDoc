package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class EntityBison extends Animal implements IAnimatedEntity, Shearable, IForgeShearable {

    public static final Animation ANIMATION_PREPARE_CHARGE = Animation.create(40);

    public static final Animation ANIMATION_EAT = Animation.create(35);

    public static final Animation ANIMATION_ATTACK = Animation.create(15);

    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EntityBison.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SNOWY = SynchedEntityData.defineId(EntityBison.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityBison.class, EntityDataSerializers.BOOLEAN);

    public float prevChargeProgress;

    public float chargeProgress;

    private int animationTick;

    private Animation currentAnimation;

    private int snowTimer = 0;

    private boolean permSnow = false;

    private int blockBreakCounter;

    private int chargeCooldown = this.f_19796_.nextInt(2000);

    private EntityBison chargePartner;

    private boolean hasChargedSpeed = false;

    private int feedingsSinceLastShear = 0;

    protected EntityBison(EntityType<? extends Animal> animal, Level lvl) {
        super(animal, lvl);
        this.m_274367_(1.1F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_KNOCKBACK, 2.0);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.bisonSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(0.25F);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.BISON_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BISON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BISON_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.COW_STEP, 0.1F, 1.0F);
    }

    public boolean isSnowy() {
        return this.f_19804_.get(SNOWY);
    }

    public void setSnowy(boolean honeyed) {
        this.f_19804_.set(SNOWY, honeyed);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(3, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(Items.WHEAT), false));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(6, new EntityBison.AIChargeFurthest());
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 70, 1.0, 18, 7));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new EntityBison.AIAttackNearPlayers());
        this.f_21346_.addGoal(2, new AnimalAIHurtByTargetNotBaby(this));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SHEARED, false);
        this.f_19804_.define(SNOWY, false);
        this.f_19804_.define(CHARGING, false);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return AMEntityRegistry.BISON.get().create(this.m_9236_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSnowy(compound.getBoolean("Snowy"));
        this.setSheared(compound.getBoolean("Sheared"));
        this.permSnow = compound.getBoolean("SnowPerm");
        this.chargeCooldown = compound.getInt("ChargeCooldown");
        this.feedingsSinceLastShear = compound.getInt("Feedings");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Snowy", this.isSnowy());
        compound.putBoolean("Sheared", this.isSheared());
        compound.putBoolean("SnowPerm", this.permSnow);
        compound.putInt("ChargeCooldown", this.chargeCooldown);
        compound.putInt("Feedings", this.feedingsSinceLastShear);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevChargeProgress = this.chargeProgress;
        if (this.isCharging() && this.chargeProgress < 5.0F) {
            this.chargeProgress++;
        }
        if (!this.isCharging() && this.chargeProgress > 0.0F) {
            this.chargeProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.snowTimer == 0) {
                this.snowTimer = 200 + this.f_19796_.nextInt(400);
                if (this.isSnowy()) {
                    if (!this.permSnow && (this.m_20094_() > 0 || this.m_20072_() || !EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_().above()))) {
                        this.setSnowy(false);
                    }
                } else if (EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_())) {
                    this.setSnowy(true);
                }
            }
            LivingEntity attackTarget = this.m_5448_();
            if (this.m_20184_().lengthSqr() < 0.05 && this.getAnimation() == NO_ANIMATION && (attackTarget == null || !attackTarget.isAlive()) && this.m_217043_().nextInt(600) == 0 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                this.setAnimation(ANIMATION_EAT);
            }
            if (this.getAnimation() == ANIMATION_EAT && this.getAnimationTick() == 30 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                this.feedingsSinceLastShear++;
                BlockPos down = this.m_20183_().below();
                this.m_9236_().m_46796_(2001, down, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                this.m_9236_().setBlock(down, Blocks.DIRT.defaultBlockState(), 2);
            }
            if (this.isCharging()) {
                if (!this.hasChargedSpeed) {
                    this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.65F);
                    this.hasChargedSpeed = true;
                }
            } else if (this.hasChargedSpeed) {
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
                this.hasChargedSpeed = false;
            }
            if (attackTarget != null && attackTarget.isAlive() && this.m_6084_()) {
                double dist = (double) this.m_20270_(attackTarget);
                if (this.m_142582_(attackTarget)) {
                    this.m_21391_(attackTarget, 30.0F, 30.0F);
                    this.f_20883_ = this.m_146908_();
                }
                if (dist < (double) (this.m_20205_() + 3.0F)) {
                    if (this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() > 8 && dist < (double) (this.m_20205_() + 1.0F) && this.m_142582_(attackTarget)) {
                        float dmg = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
                        if (attackTarget instanceof Wolf) {
                            dmg = 2.0F;
                        }
                        this.launch(attackTarget, this.isCharging());
                        if (this.isCharging()) {
                            dmg += 3.0F;
                            this.setCharging(false);
                        }
                        attackTarget.hurt(this.m_269291_().mobAttack(this), dmg);
                    }
                } else if (!this.isCharging()) {
                    Animation animation = this.getAnimation();
                    if (animation == NO_ANIMATION) {
                        this.setAnimation(ANIMATION_PREPARE_CHARGE);
                    } else if (animation == ANIMATION_PREPARE_CHARGE) {
                        this.m_21573_().stop();
                        if (this.getAnimationTick() > 30) {
                            this.setCharging(true);
                        }
                    }
                }
            }
        }
        if (this.chargeCooldown > 0) {
            this.chargeCooldown--;
        }
        if (this.feedingsSinceLastShear >= 5 && this.isSheared()) {
            this.feedingsSinceLastShear = 0;
            this.setSheared(false);
        }
        if (!this.m_9236_().isClientSide && this.isCharging() && (this.m_5448_() == null && this.chargePartner == null || this.m_20072_())) {
            this.setCharging(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    public boolean isSheared() {
        return this.f_19804_.get(SHEARED);
    }

    public void setSheared(boolean b) {
        this.f_19804_.set(SHEARED, b);
    }

    private void launch(Entity launch, boolean huge) {
        float rot = 180.0F + this.m_146908_();
        float hugeScale = huge ? 4.0F : 0.6F;
        float strength = (float) ((double) hugeScale * (1.0 - ((LivingEntity) launch).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        float rotRad = rot * (float) (Math.PI / 180.0);
        float x = Mth.sin(rotRad);
        float z = -Mth.cos(rotRad);
        launch.hasImpulse = true;
        Vec3 vec3 = this.m_20184_();
        Vec3 vec31 = vec3.add(new Vec3((double) x, 0.0, (double) z).normalize().scale((double) strength));
        launch.setDeltaMovement(vec31.x, huge ? 1.0 : 0.5, vec31.z);
        launch.setOnGround(false);
    }

    private void knockbackTarget(LivingEntity entity, float strength, float angle) {
        float rot = this.m_146908_() + angle;
        if (entity != null) {
            entity.knockback((double) strength, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.m_9236_().isClientSide) {
            if (item == Items.SNOW && !this.isSnowy()) {
                this.m_142075_(player, hand, itemstack);
                this.permSnow = true;
                this.setSnowy(true);
                this.m_5496_(SoundEvents.SNOW_PLACE, this.m_6121_(), this.m_6100_());
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                return InteractionResult.SUCCESS;
            }
            if (item instanceof ShovelItem && this.isSnowy()) {
                this.permSnow = false;
                if (!player.isCreative()) {
                    itemstack.hurt(1, this.m_217043_(), player instanceof ServerPlayer ? (ServerPlayer) player : null);
                }
                this.setSnowy(false);
                this.m_5496_(SoundEvents.SNOW_BREAK, this.m_6121_(), this.m_6100_());
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                return InteractionResult.SUCCESS;
            }
        }
        return type;
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.breakBlock();
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
                            Block block = state.m_60734_();
                            if (block == Blocks.SNOW && (Integer) state.m_61143_(SnowLayerBlock.LAYERS) <= 1) {
                                this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                flag = true;
                                this.m_9236_().m_46961_(pos, true);
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCounter = this.isCharging() && this.m_5448_() != null ? 2 : 20;
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
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
        return new Animation[] { ANIMATION_PREPARE_CHARGE, ANIMATION_ATTACK, ANIMATION_EAT };
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.readyForShearing();
    }

    @Override
    public void shear(SoundSource category) {
        this.m_9236_().playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        this.setSheared(true);
        this.feedingsSinceLastShear = 0;
        for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
            this.m_19998_(AMItemRegistry.BISON_FUR.get());
        }
    }

    public boolean isCharging() {
        return this.f_19804_.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.f_19804_.set(CHARGING, charging);
    }

    @Override
    public boolean readyForShearing() {
        return !this.isSheared() && !this.m_6162_();
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        List<ItemStack> list = new ArrayList(6);
        for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
            list.add(new ItemStack(AMItemRegistry.BISON_FUR.get()));
        }
        this.feedingsSinceLastShear = 0;
        this.setSheared(true);
        return list;
    }

    public boolean isValidCharging() {
        return !this.m_6162_() && this.m_6084_() && this.chargeCooldown == 0 && !this.m_20072_();
    }

    public void pushBackJostling(EntityBison bison, float strength) {
        this.applyKnockbackFromBuffalo(strength, bison.m_20185_() - this.m_20185_(), bison.m_20189_() - this.m_20189_());
    }

    private void applyKnockbackFromBuffalo(float strength, double ratioX, double ratioZ) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(this, strength, ratioX, ratioZ);
        if (!event.isCanceled()) {
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            if (!(strength <= 0.0F)) {
                this.f_19812_ = true;
                Vec3 vector3d = this.m_20184_();
                Vec3 vector3d1 = new Vec3(ratioX, 0.0, ratioZ).normalize().scale((double) strength);
                this.m_20334_(vector3d.x / 2.0 - vector3d1.x, 0.3F, vector3d.z / 2.0 - vector3d1.z);
            }
        }
    }

    private void resetChargeCooldown() {
        this.setCharging(false);
        this.chargePartner = null;
        this.chargeCooldown = 1000 + this.f_19796_.nextInt(2000);
    }

    class AIAttackNearPlayers extends NearestAttackableTargetGoal<Player> {

        public AIAttackNearPlayers() {
            super(EntityBison.this, Player.class, 80, true, true, null);
        }

        @Override
        public boolean canUse() {
            return !EntityBison.this.m_6162_() && !EntityBison.this.m_27593_() ? super.canUse() : false;
        }

        @Override
        protected double getFollowDistance() {
            return 3.0;
        }
    }

    private class AIChargeFurthest extends Goal {

        public AIChargeFurthest() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (EntityBison.this.isValidCharging()) {
                if (EntityBison.this.chargePartner != null && EntityBison.this.chargePartner.isValidCharging() && EntityBison.this.chargePartner != EntityBison.this) {
                    EntityBison.this.chargePartner.chargePartner = EntityBison.this;
                    return true;
                }
                if (EntityBison.this.f_19796_.nextInt(100) == 0) {
                    EntityBison furthest = null;
                    for (EntityBison bison : EntityBison.this.m_9236_().m_45976_(EntityBison.class, EntityBison.this.m_20191_().inflate(15.0))) {
                        if (bison.chargeCooldown == 0 && !bison.m_6162_() && !bison.m_7306_(EntityBison.this) && (furthest == null || EntityBison.this.m_20270_(furthest) < EntityBison.this.m_20270_(bison))) {
                            furthest = bison;
                        }
                    }
                    if (furthest != null && furthest != EntityBison.this) {
                        EntityBison.this.chargePartner = furthest;
                        furthest.chargePartner = EntityBison.this;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return EntityBison.this.isValidCharging() && EntityBison.this.chargePartner != null && EntityBison.this.chargePartner.isValidCharging() && !EntityBison.this.chargePartner.m_7306_(EntityBison.this);
        }

        @Override
        public void tick() {
            EntityBison.this.m_21391_(EntityBison.this.chargePartner, 30.0F, 30.0F);
            EntityBison.this.f_20883_ = EntityBison.this.m_146908_();
            if (!EntityBison.this.isCharging()) {
                Animation bisonAnimation = EntityBison.this.getAnimation();
                if (bisonAnimation == IAnimatedEntity.NO_ANIMATION || bisonAnimation == EntityBison.ANIMATION_PREPARE_CHARGE && EntityBison.this.getAnimationTick() > 35) {
                    EntityBison.this.setCharging(true);
                }
            } else {
                float dist = EntityBison.this.m_20270_(EntityBison.this.chargePartner);
                EntityBison.this.m_21573_().moveTo(EntityBison.this.chargePartner, 1.0);
                if (EntityBison.this.m_142582_(EntityBison.this.chargePartner)) {
                    float flingAnimAt = EntityBison.this.m_20205_() + 1.0F;
                    if (!(dist < flingAnimAt) || EntityBison.this.getAnimation() != EntityBison.ANIMATION_ATTACK) {
                        float startFlingAnimAt = EntityBison.this.m_20205_() + 3.0F;
                        if (dist < startFlingAnimAt && EntityBison.this.getAnimation() != EntityBison.ANIMATION_ATTACK) {
                            EntityBison.this.setAnimation(EntityBison.ANIMATION_ATTACK);
                        }
                    } else if (EntityBison.this.getAnimationTick() > 8) {
                        boolean flag = false;
                        if (EntityBison.this.m_20096_()) {
                            EntityBison.this.pushBackJostling(EntityBison.this.chargePartner, 0.2F);
                            flag = true;
                        }
                        if (EntityBison.this.chargePartner.m_20096_()) {
                            EntityBison.this.chargePartner.pushBackJostling(EntityBison.this, 0.9F);
                            flag = true;
                        }
                        if (flag) {
                            EntityBison.this.resetChargeCooldown();
                        }
                    }
                }
            }
        }
    }
}