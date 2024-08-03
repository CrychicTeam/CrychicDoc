package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class EntityTroll extends Monster implements IAnimatedEntity, IVillagerFear, IHumanoid, IHasCustomizableAttributes {

    public static final Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);

    public static final Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);

    public static final Animation ANIMATION_SPEAK = Animation.create(10);

    public static final Animation ANIMATION_ROAR = Animation.create(25);

    public static final ResourceLocation FOREST_LOOT = new ResourceLocation("iceandfire", "entities/troll_forest");

    public static final ResourceLocation FROST_LOOT = new ResourceLocation("iceandfire", "entities/troll_frost");

    public static final ResourceLocation MOUNTAIN_LOOT = new ResourceLocation("iceandfire", "entities/troll_mountain");

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityTroll.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> WEAPON = SynchedEntityData.defineId(EntityTroll.class, EntityDataSerializers.INT);

    public float stoneProgress;

    private int animationTick;

    private Animation currentAnimation;

    private boolean avoidSun = true;

    public EntityTroll(EntityType<EntityTroll> t, Level worldIn) {
        super(t, worldIn);
    }

    public static boolean canTrollSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && m_219009_(worldIn, pos, randomIn) && m_217057_(IafEntityRegistry.TROLL.get(), worldIn, reason, pos, randomIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.trollMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, IafConfig.trollAttackStrength).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ARMOR, 9.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.trollMaxHealth);
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.trollAttackStrength);
    }

    private void setAvoidSun(boolean day) {
        if (day && !this.avoidSun) {
            ((GroundPathNavigation) this.m_21573_()).setAvoidSun(true);
            this.avoidSun = true;
        }
        if (!day && this.avoidSun) {
            ((GroundPathNavigation) this.m_21573_()).setAvoidSun(false);
            this.avoidSun = false;
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, @NotNull MobSpawnType spawnReasonIn) {
        BlockPos pos = this.m_20183_();
        BlockPos heightAt = worldIn.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        boolean rngCheck = true;
        return rngCheck && pos.m_123342_() < heightAt.m_123342_() - 10 && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new TrollAIFleeSun(this, 1.0));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.setAvoidSun(true);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.m_217043_().nextBoolean()) {
            this.setAnimation(ANIMATION_STRIKE_VERTICAL);
        } else {
            this.setAnimation(ANIMATION_STRIKE_HORIZONTAL);
        }
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(WEAPON, 0);
    }

    private int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public EnumTroll getTrollType() {
        return EnumTroll.values()[this.getVariant()];
    }

    public void setTrollType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    private int getWeapon() {
        return this.f_19804_.get(WEAPON);
    }

    private void setWeapon(int variant) {
        this.f_19804_.set(WEAPON, variant);
    }

    public EnumTroll.Weapon getWeaponType() {
        return EnumTroll.Weapon.values()[this.getWeapon()];
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("Weapon", this.getWeapon());
        compound.putFloat("StoneProgress", this.stoneProgress);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setWeapon(compound.getInt("Weapon"));
        this.stoneProgress = compound.getFloat("StoneProgress");
        this.setConfigurableAttributes();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setTrollType(EnumTroll.getBiomeType(this.m_9236_().m_204166_(this.m_20183_())));
        this.setWeaponType(EnumTroll.getWeaponForType(this.getTrollType()));
        return spawnDataIn;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return source.getMsgId().contains("arrow") ? false : super.m_6469_(source, damage);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        switch(this.getTrollType()) {
            case MOUNTAIN:
                return MOUNTAIN_LOOT;
            case FROST:
                return FROST_LOOT;
            case FOREST:
                return FOREST_LOOT;
            default:
                return null;
        }
    }

    @Override
    public int getExperienceReward() {
        return 15;
    }

    @Override
    protected void tickDeath() {
        super.m_6153_();
        if (this.f_20919_ == 20 && !this.m_9236_().isClientSide && IafConfig.trollsDropWeapon) {
            if (this.m_217043_().nextInt(3) == 0) {
                ItemStack weaponStack = new ItemStack((ItemLike) this.getWeaponType().item.get(), 1);
                weaponStack.hurt(this.m_217043_().nextInt(250), this.m_217043_(), null);
                this.dropItemAt(weaponStack, this.m_20185_(), this.m_20186_(), this.m_20189_());
            } else {
                ItemStack brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                ItemStack brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                if (this.getWeaponType() == EnumTroll.Weapon.AXE) {
                    brokenDrop = new ItemStack(Items.STICK, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.m_217043_().nextInt(2) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.COLUMN) {
                    brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.COLUMN_FOREST) {
                    brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.COLUMN_FROST) {
                    brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Items.SNOWBALL, this.m_217043_().nextInt(4) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.HAMMER) {
                    brokenDrop = new ItemStack(Items.BONE, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.m_217043_().nextInt(2) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.TRUNK) {
                    brokenDrop = new ItemStack(Blocks.OAK_LOG, this.m_217043_().nextInt(2) + 1);
                    brokenDrop2 = new ItemStack(Blocks.OAK_LOG, this.m_217043_().nextInt(2) + 1);
                }
                if (this.getWeaponType() == EnumTroll.Weapon.TRUNK_FROST) {
                    brokenDrop = new ItemStack(Blocks.SPRUCE_LOG, this.m_217043_().nextInt(4) + 1);
                    brokenDrop2 = new ItemStack(Items.SNOWBALL, this.m_217043_().nextInt(4) + 1);
                }
                this.dropItemAt(brokenDrop, this.m_20185_(), this.m_20186_(), this.m_20189_());
                this.dropItemAt(brokenDrop2, this.m_20185_(), this.m_20186_(), this.m_20189_());
            }
        }
    }

    @Nullable
    private ItemEntity dropItemAt(ItemStack stack, double x, double y, double z) {
        if (stack.getCount() > 0) {
            ItemEntity entityitem = new ItemEntity(this.m_9236_(), x, y, z, stack);
            entityitem.setDefaultPickUpDelay();
            this.m_9236_().m_7967_(entityitem);
            return entityitem;
        } else {
            return null;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        boolean stone = EntityGorgon.isStoneMob(this);
        if (stone && this.stoneProgress < 20.0F) {
            this.stoneProgress += 2.0F;
        } else if (!stone && this.stoneProgress > 0.0F) {
            this.stoneProgress -= 2.0F;
        }
        if (!stone && this.getAnimation() == NO_ANIMATION && this.m_5448_() != null && this.m_217043_().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.m_5496_(IafSoundRegistry.TROLL_ROAR, 1.0F, 1.0F);
        }
        if (!stone && this.m_21223_() < this.m_21233_() && this.f_19797_ % 30 == 0) {
            this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 30, 1, false, false));
        }
        this.setAvoidSun(this.m_9236_().isDay());
        if (this.m_9236_().isDay() && !this.m_9236_().isClientSide) {
            float f = (float) this.m_9236_().m_45517_(LightLayer.SKY, this.m_20183_());
            BlockPos blockpos = this.m_20202_() instanceof Boat ? new BlockPos(this.m_146903_(), this.m_146904_(), this.m_146907_()).above() : new BlockPos(this.m_146903_(), this.m_146904_(), this.m_146907_());
            if (f > 0.5F && this.m_9236_().m_45527_(blockpos)) {
                this.m_20334_(0.0, 0.0, 0.0);
                this.setAnimation(NO_ANIMATION);
                this.m_5496_(IafSoundRegistry.TURN_STONE, 1.0F, 1.0F);
                this.stoneProgress = 20.0F;
                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(this);
                statue.getTrappedTag().putFloat("StoneProgress", 20.0F);
                statue.m_19890_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(statue);
                }
                statue.f_19859_ = this.m_146908_();
                statue.m_146922_(this.m_146908_());
                statue.f_20885_ = this.m_146908_();
                statue.f_20883_ = this.m_146908_();
                statue.f_20884_ = this.m_146908_();
                this.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            float weaponX = (float) (this.m_20185_() + (double) (1.9F * Mth.cos((float) ((double) (this.f_20883_ + 90.0F) * Math.PI / 180.0))));
            float weaponZ = (float) (this.m_20189_() + (double) (1.9F * Mth.sin((float) ((double) (this.f_20883_ + 90.0F) * Math.PI / 180.0))));
            float weaponY = (float) (this.m_20186_() + 0.2F);
            BlockState state = this.m_9236_().getBlockState(BlockPos.containing((double) weaponX, (double) (weaponY - 1.0F), (double) weaponZ));
            for (int i = 0; i < 20; i++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                if (state.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double) (weaponX + (this.m_217043_().nextFloat() - 0.5F)), (double) (weaponY + (this.m_217043_().nextFloat() - 0.5F)), (double) (weaponZ + (this.m_217043_().nextFloat() - 0.5F)), motionX, motionY, motionZ);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.m_5448_() != null && this.m_20280_(this.m_5448_()) < 4.0 && this.getAnimationTick() == 10 && this.f_20919_ <= 0) {
            this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.m_5448_() != null && this.m_20280_(this.m_5448_()) < 4.0 && this.getAnimationTick() == 10 && this.f_20919_ <= 0) {
            LivingEntity target = this.m_5448_();
            target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            float f1 = 0.5F;
            float f2 = target.zza;
            float f3 = 0.6F;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }
            f4 = f1 / f4;
            f2 *= f4;
            f3 *= f4;
            float f5 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
            float f6 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
            target.m_20334_((double) f5, (double) f6, 0.4F);
        }
        if (this.m_21573_().isDone() && this.m_5448_() != null && this.m_20280_(this.m_5448_()) > 3.0 && this.m_20280_(this.m_5448_()) < 30.0 && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            this.m_21391_(this.m_5448_(), 30.0F, 30.0F);
            if (this.getAnimation() == NO_ANIMATION && this.f_19796_.nextInt(15) == 0) {
                this.setAnimation(ANIMATION_STRIKE_VERTICAL);
            }
            if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
                float weaponX = (float) (this.m_20185_() + (double) (1.9F * Mth.cos((float) ((double) (this.f_20883_ + 90.0F) * Math.PI / 180.0))));
                float weaponZ = (float) (this.m_20189_() + (double) (1.9F * Mth.sin((float) ((double) (this.f_20883_ + 90.0F) * Math.PI / 180.0))));
                float weaponY = (float) (this.m_20186_() + (double) (this.m_20192_() / 2.0F));
                Explosion explosion = new Explosion(this.m_9236_(), this, (double) weaponX, (double) weaponY, (double) weaponZ, 1.0F + this.m_217043_().nextFloat(), new ArrayList());
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, (double) weaponX, (double) weaponY, (double) weaponZ))) {
                    explosion.explode();
                    explosion.finalizeExplosion(true);
                }
                this.m_5496_(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F);
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            this.m_5496_(SoundEvents.PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAnimationTick() == 10) {
            this.m_5496_(SoundEvents.PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
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

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.TROLL_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.TROLL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.TROLL_DIE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL, ANIMATION_SPEAK, ANIMATION_ROAR };
    }
}