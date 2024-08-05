package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAIRangedAttack;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAITargetBalloons;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwner;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityCapuchinMonkey extends TamableAnimal implements IAnimatedEntity, IFollower, ITargetsDroppedItems {

    public static final Animation ANIMATION_THROW = Animation.create(12);

    public static final Animation ANIMATION_HEADTILT = Animation.create(15);

    public static final Animation ANIMATION_SCRATCH = Animation.create(20);

    protected static final EntityDataAccessor<Boolean> DART = SynchedEntityData.defineId(EntityCapuchinMonkey.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityCapuchinMonkey.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityCapuchinMonkey.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityCapuchinMonkey.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DART_TARGET = SynchedEntityData.defineId(EntityCapuchinMonkey.class, EntityDataSerializers.INT);

    public float prevSitProgress;

    public float sitProgress;

    public boolean forcedSit = false;

    public boolean attackDecision = false;

    private int animationTick;

    private Animation currentAnimation;

    private int sittingTime = 0;

    private int maxSitTime = 75;

    private boolean hasSlowed = false;

    private int rideCooldown = 0;

    private Ingredient temptItems = null;

    protected EntityCapuchinMonkey(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.4F);
    }

    public static <T extends Mob> boolean canCapuchinSpawn(EntityType<EntityCapuchinMonkey> gorilla, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return (blockstate.m_204336_(BlockTags.LEAVES) || blockstate.m_60713_(Blocks.GRASS_BLOCK) || blockstate.m_204336_(BlockTags.LOGS) || blockstate.m_60713_(Blocks.MANGROVE_ROOTS) || blockstate.m_60713_(Blocks.MUDDY_MANGROVE_ROOTS) || blockstate.m_60713_(Blocks.AIR)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.capuchinMonkeySpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public Ingredient getAllFoods() {
        if (this.temptItems == null) {
            this.temptItems = Ingredient.fromValues(Stream.of(new Ingredient.TagValue(AMTagRegistry.INSECT_ITEMS), new Ingredient.ItemValue(new ItemStack(Items.EGG))));
        }
        return this.temptItems;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 4.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new CapuchinAIMelee(this, 1.0, true));
        this.f_21345_.addGoal(3, new CapuchinAIRangedAttack(this, 1.0, 20, 15.0F));
        this.f_21345_.addGoal(6, new TameableAIFollowOwner(this, 1.0, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.merge(ImmutableList.of(Ingredient.of(AMTagRegistry.BANANAS))), true) {

            @Override
            public void tick() {
                super.tick();
                if (this.f_25924_.m_20280_(this.f_25925_) < 6.25 && this.f_25924_.m_217043_().nextInt(14) == 0) {
                    ((EntityCapuchinMonkey) this.f_25924_).setAnimation(EntityCapuchinMonkey.ANIMATION_HEADTILT);
                }
            }
        });
        this.f_21345_.addGoal(7, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this, EntityCapuchinMonkey.class, EntityTossedItem.class).setAlertOthers());
        this.f_21346_.addGoal(5, new CapuchinAITargetBalloons(this, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.CAPUCHIN_MONKEY_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.CAPUCHIN_MONKEY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.CAPUCHIN_MONKEY_HURT.get();
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("MonkeySitting", this.isSitting());
        compound.putBoolean("HasDart", this.hasDart());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putInt("Command", this.getCommand());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("MonkeySitting"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.setCommand(compound.getInt("Command"));
        this.setDart(compound.getBoolean("HasDart"));
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 10.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (!this.forcedSit && this.isSitting() && ++this.sittingTime > this.maxSitTime) {
            this.setOrderedToSit(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && !this.isSitting() && this.getCommand() != 1 && this.f_19796_.nextInt(1500) == 0) {
            this.maxSitTime = 300 + this.f_19796_.nextInt(250);
            this.setOrderedToSit(true);
        }
        this.m_274367_(2.0F);
        if (!this.forcedSit && this.isSitting() && (this.getDartTarget() != null || this.getCommand() == 1)) {
            this.setOrderedToSit(false);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_5448_() != null && this.getAnimation() == ANIMATION_SCRATCH && this.getAnimationTick() == 10) {
                float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
                this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.3F), 0.0, (double) (Mth.cos(f1) * 0.3F)));
                this.m_5448_().knockback(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                this.setAttackDecision(this.m_5448_());
            }
            if (this.getDartTarget() != null && this.getDartTarget().isAlive() && this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() == 5) {
                Vec3 vector3d = this.getDartTarget().getDeltaMovement();
                double d0 = this.getDartTarget().getX() + vector3d.x - this.m_20185_();
                double d1 = this.getDartTarget().getEyeY() - 1.1F - this.m_20186_();
                double d2 = this.getDartTarget().getZ() + vector3d.z - this.m_20189_();
                float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));
                EntityTossedItem tossedItem = new EntityTossedItem(this.m_9236_(), this);
                tossedItem.setDart(this.hasDart());
                tossedItem.m_146926_(tossedItem.m_146909_() - 20.0F);
                tossedItem.m_6686_(d0, d1 + (double) (f * 0.2F), d2, this.hasDart() ? 1.15F : 0.75F, 8.0F);
                if (!this.m_20067_()) {
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_THROW, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
                    this.m_146850_(GameEvent.PROJECTILE_SHOOT);
                }
                this.m_9236_().m_7967_(tossedItem);
                this.setAttackDecision(this.getDartTarget());
            }
        }
        if (this.rideCooldown > 0) {
            this.rideCooldown--;
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && this.m_217043_().nextInt(300) == 0) {
            this.setAnimation(ANIMATION_HEADTILT);
        }
        if (!this.m_9236_().isClientSide && this.isSitting()) {
            this.m_21573_().stop();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SCRATCH);
        }
        return true;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.hasDart()) {
            this.m_19998_(AMItemRegistry.ANCIENT_DART.get());
        }
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.isAlive()) {
            this.m_8127_();
        } else if (this.m_21824_() && entity instanceof LivingEntity && this.m_21830_((LivingEntity) entity)) {
            this.m_20334_(0.0, 0.0, 0.0);
            this.tick();
            if (this.m_20159_()) {
                Entity mount = this.m_20202_();
                if (mount instanceof Player player) {
                    this.f_20883_ = player.f_20883_;
                    this.m_146922_(player.m_146908_());
                    this.f_20885_ = player.f_20885_;
                    this.f_19859_ = player.f_20885_;
                    float radius = 0.0F;
                    float angle = (float) (Math.PI / 180.0) * (((LivingEntity) mount).yBodyRot - 180.0F);
                    double extraX = (double) (0.0F * Mth.sin((float) Math.PI + angle));
                    double extraZ = (double) (0.0F * Mth.cos(angle));
                    this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) mount.getBbHeight() + 0.1, mount.getY()), mount.getZ() + extraZ);
                    this.attackDecision = true;
                    if (!mount.isAlive() || this.rideCooldown == 0 && mount.isShiftKeyDown()) {
                        this.m_6038_();
                        this.attackDecision = false;
                    }
                }
            }
        } else {
            super.m_6083_();
        }
    }

    public void setAttackDecision(Entity target) {
        if (!(target instanceof Monster) && !this.hasDart()) {
            this.attackDecision = !this.attackDecision;
        } else {
            this.attackDecision = true;
        }
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean hasDartTarget() {
        return this.f_19804_.get(DART_TARGET) != -1 && this.hasDart();
    }

    public void setDartTarget(Entity entity) {
        this.f_19804_.set(DART_TARGET, entity == null ? -1 : entity.getId());
        if (entity instanceof LivingEntity target) {
            this.m_6710_(target);
        }
    }

    @Nullable
    public Entity getDartTarget() {
        if (!this.hasDartTarget()) {
            return this.m_5448_();
        } else {
            Entity entity = this.m_9236_().getEntity(this.f_19804_.get(DART_TARGET));
            return (Entity) (entity != null && entity.isAlive() ? entity : this.m_5448_());
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(DART_TARGET, -1);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(DART, false);
        this.f_19804_.define(VARIANT, 0);
    }

    public boolean hasDart() {
        return this.f_19804_.get(DART);
    }

    public void setDart(boolean dart) {
        this.f_19804_.set(DART, dart);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        EntityCapuchinMonkey monkey = AMEntityRegistry.CAPUCHIN_MONKEY.get().create(p_241840_1_);
        monkey.setVariant(this.getVariant());
        return monkey;
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
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (EntityGorilla.isBanana(itemstack)) {
            if (!this.m_21824_()) {
                this.m_142075_(player, hand, itemstack);
                if (this.m_217043_().nextInt(5) == 0) {
                    this.m_21828_(player);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.SUCCESS;
            }
            if (this.m_21824_() && this.getAllFoods().test(itemstack) && !this.isFood(itemstack) && this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
                return InteractionResult.SUCCESS;
            }
        }
        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack) || EntityGorilla.isBanana(itemstack) || this.getAllFoods().test(itemstack)) {
            return type;
        } else if (!this.hasDart() && itemstack.getItem() == AMItemRegistry.ANCIENT_DART.get()) {
            this.setDart(true);
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.CONSUME;
        } else if (this.hasDart() && itemstack.getItem() == Items.SHEARS) {
            this.setDart(false);
            itemstack.hurtAndBreak(1, this, p_233654_0_ -> {
            });
            return InteractionResult.SUCCESS;
        } else if (player.m_6144_() && player.m_20197_().isEmpty()) {
            this.m_20329_(player);
            this.rideCooldown = 20;
            return InteractionResult.SUCCESS;
        } else {
            this.setCommand(this.getCommand() + 1);
            if (this.getCommand() == 3) {
                this.setCommand(0);
            }
            player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
            boolean sit = this.getCommand() == 2;
            if (sit) {
                this.forcedSit = true;
                this.setOrderedToSit(true);
            } else {
                this.forcedSit = false;
                this.setOrderedToSit(false);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_THROW, ANIMATION_SCRATCH };
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return this.getAllFoods().test(stack) || EntityGorilla.isBanana(stack);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_5634_(5.0F);
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
        if (EntityGorilla.isBanana(e.getItem())) {
            if (this.m_217043_().nextInt(4) == 0) {
                this.m_19983_(new ItemStack(AMBlockRegistry.BANANA_PEEL.get()));
            }
            Entity itemThrower = e.getOwner();
            if (itemThrower != null && !this.m_21824_()) {
                if (this.m_217043_().nextInt(5) == 0) {
                    this.m_7105_(true);
                    this.m_21816_(itemThrower.getUUID());
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance diff, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        int i;
        if (data instanceof EntityCapuchinMonkey.CapuchinGroupData) {
            i = ((EntityCapuchinMonkey.CapuchinGroupData) data).variant;
        } else {
            i = this.f_19796_.nextInt(4);
            data = new EntityCapuchinMonkey.CapuchinGroupData(i);
        }
        this.setVariant(i);
        return super.m_6518_(world, diff, spawnType, data, tag);
    }

    public static class CapuchinGroupData extends AgeableMob.AgeableMobGroupData {

        public final int variant;

        CapuchinGroupData(int variant) {
            super(true);
            this.variant = variant;
        }
    }
}