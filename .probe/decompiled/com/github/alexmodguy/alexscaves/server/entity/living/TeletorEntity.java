package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TeletorEntity extends Monster {

    private static ImmutableList<Item> HELD_ITEM_POSSIBILITIES = null;

    private static final EntityDataAccessor<Optional<UUID>> WEAPON_UUID = SynchedEntityData.defineId(TeletorEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> WEAPON_ID = SynchedEntityData.defineId(TeletorEntity.class, EntityDataSerializers.INT);

    private float prevControlProgress = 0.0F;

    private float controlProgress = 0.0F;

    private Vec3[][] trailPositions = new Vec3[64][2];

    private int trailPointer = -1;

    private int floatingTicks = 0;

    public TeletorEntity(EntityType<? extends Monster> teletor, Level level) {
        super(teletor, level);
        this.f_21342_ = new TeletorEntity.MoveController();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(WEAPON_UUID, Optional.empty());
        this.f_19804_.define(WEAPON_ID, -1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new TeletorEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level0) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.f_26495_.getBlockState(pos.below()).m_60795_();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return this.m_9236_().getBlockState(pos).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Nullable
    public UUID getWeaponUUID() {
        return (UUID) this.f_19804_.get(WEAPON_UUID).orElse(null);
    }

    public void setWeaponUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(WEAPON_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getWeapon() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getWeaponUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(WEAPON_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("WeaponUUID")) {
            this.setWeaponUUID(compound.getUUID("WeaponUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getWeaponUUID() != null) {
            compound.putUUID("WeaponUUID", this.getWeaponUUID());
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FLYING_SPEED, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 18.0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.55F * dimensions.height;
    }

    public boolean areLegsCrossed(float limbSwing) {
        return this.m_6084_() && limbSwing <= 0.35F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevControlProgress = this.controlProgress;
        if (this.getWeapon() instanceof MagneticWeaponEntity magneticWeapon) {
            this.f_19804_.set(WEAPON_ID, magneticWeapon.m_19879_());
            magneticWeapon.setControllerUUID(this.m_20148_());
            Entity e = magneticWeapon.getTarget();
            boolean control = e != null && e.isAlive();
            if (control && this.controlProgress < 5.0F) {
                this.controlProgress++;
            }
            if (!control && this.controlProgress > 0.0F) {
                this.controlProgress--;
            }
        }
        if (this.m_9236_().isClientSide) {
            this.tickVisual();
        }
        if (this.floatingTicks-- <= 0) {
            this.floatingTicks = 30;
            this.m_216990_(ACSoundRegistry.TELETOR_FLOAT.get());
        }
        this.m_20256_(this.m_20184_().multiply(0.98F, 0.98F, 0.98F));
    }

    public void tickVisual() {
        Vec3 blue = this.getHelmetPosition(0);
        Vec3 red = this.getHelmetPosition(1);
        if (this.trailPointer == -1) {
            for (int i = 0; i < this.trailPositions.length; i++) {
                this.trailPositions[i][0] = blue;
                this.trailPositions[i][1] = red;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer][0] = blue;
        this.trailPositions[this.trailPointer][1] = red;
    }

    public boolean hasTrail() {
        return this.trailPointer != -1;
    }

    public float getControlProgress(float partialTick) {
        return (this.prevControlProgress + (this.controlProgress - this.prevControlProgress) * partialTick) * 0.2F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public Vec3 getTrailPosition(int pointer, int side, float partialTick) {
        if (this.m_213877_()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j][side];
        Vec3 d1 = this.trailPositions[i][side].subtract(d0);
        return d0.add(d1.scale((double) partialTick));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(this.m_9236_());
        ItemStack stack = this.createItemStack(this.m_9236_().getRandom());
        float f = difficultyIn.getSpecialMultiplier();
        if (this.m_9236_().getRandom().nextFloat() < 0.25F * (f + 0.5F)) {
            stack = EnchantmentHelper.enchantItem(this.m_9236_().getRandom(), stack, (int) (5.0F + f * (float) this.m_9236_().getRandom().nextInt(18)), false);
        }
        magneticWeapon.setItemStack(stack);
        magneticWeapon.m_146884_(this.getWeaponPosition());
        magneticWeapon.setControllerUUID(this.m_20148_());
        this.setWeaponUUID(magneticWeapon.m_20148_());
        this.m_9236_().m_7967_(magneticWeapon);
        return super.m_6518_(level, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public ItemStack createItemStack(RandomSource random) {
        if (HELD_ITEM_POSSIBILITIES == null || HELD_ITEM_POSSIBILITIES.isEmpty()) {
            HELD_ITEM_POSSIBILITIES = (ImmutableList<Item>) ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(ACTagRegistry.TELETOR_SPAWNS_WITH)).collect(ImmutableList.toImmutableList());
        }
        if (HELD_ITEM_POSSIBILITIES.size() <= 0 || random.nextFloat() < 0.3F) {
            return new ItemStack(Items.IRON_SWORD);
        } else {
            return HELD_ITEM_POSSIBILITIES.size() == 1 ? new ItemStack((ItemLike) HELD_ITEM_POSSIBILITIES.get(0)) : new ItemStack((ItemLike) HELD_ITEM_POSSIBILITIES.get(random.nextInt(HELD_ITEM_POSSIBILITIES.size())));
        }
    }

    public Vec3 getWeaponPosition() {
        return this.m_146892_().add(0.0, 1.4F - Math.sin((double) ((float) this.f_19797_ * 0.1F)) * 0.2F, 0.0);
    }

    public Vec3 getHelmetPosition(int offsetFlag) {
        Vec3 helmet = new Vec3(offsetFlag == 0 ? -0.65F : 0.65F, 1.1F, 0.0).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0));
        return this.m_146892_().add(helmet);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.getWeapon() instanceof MagneticWeaponEntity magneticWeapon) {
            ItemStack itemstack = magneticWeapon.getItemStack();
            float f = this.m_21519_(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && this.f_19796_.nextFloat() < f) {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getMaxDamage() - this.f_19796_.nextInt(1 + this.f_19796_.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }
                this.m_19983_(itemstack);
            }
            magneticWeapon.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.TELETOR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.TELETOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.TELETOR_DEATH.get();
    }

    private class MeleeGoal extends Goal {

        private int executionTime = 0;

        private BlockPos strafeOrigin = null;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = TeletorEntity.this.m_5448_();
            return target != null && target.isAlive() && TeletorEntity.this.getWeapon() != null;
        }

        @Override
        public void start() {
            this.executionTime = 0;
            this.strafeOrigin = null;
        }

        @Override
        public void tick() {
            this.executionTime++;
            LivingEntity target = TeletorEntity.this.m_5448_();
            double dist = (double) TeletorEntity.this.m_20270_(target);
            if (dist < 2.0) {
                this.strafeOrigin = TeletorEntity.this.m_20183_().offset(TeletorEntity.this.f_19796_.nextInt(16) - 8, TeletorEntity.this.f_19796_.nextInt(8), TeletorEntity.this.f_19796_.nextInt(16) - 8);
            }
            if (dist < 16.0) {
                Vec3 lookDist = target.m_146892_().subtract(TeletorEntity.this.m_146892_());
                float targetXRot = (float) (-(Mth.atan2(lookDist.y, lookDist.horizontalDistance()) * 180.0F / (float) Math.PI));
                float targetYRot = (float) (-Mth.atan2(lookDist.x, lookDist.z) * 180.0F / (float) Math.PI);
                TeletorEntity.this.m_21573_().stop();
                float f = (float) this.executionTime * 0.1F;
                Vec3 strafe = new Vec3(Math.sin((double) f) * 5.0, Math.cos((double) f) * 2.0, 0.0).yRot(-targetYRot * (float) (Math.PI / 180.0));
                if (this.strafeOrigin == null) {
                    this.strafeOrigin = TeletorEntity.this.m_20183_();
                }
                Vec3 moveTo = Vec3.atCenterOf(this.strafeOrigin).add(strafe);
                TeletorEntity.this.m_21566_().setWantedPosition(moveTo.x, moveTo.y, moveTo.z, 1.0);
                TeletorEntity.this.m_146926_(targetXRot);
                TeletorEntity.this.m_146922_(targetYRot);
            } else {
                this.strafeOrigin = null;
                TeletorEntity.this.m_21573_().moveTo(target, 1.0);
            }
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = TeletorEntity.this;

        public MoveController() {
            super(TeletorEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                LivingEntity attackTarget = this.parentEntity.getTarget();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.025 / d0);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1));
                if (d0 < width * 0.3F) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (d0 >= width && attackTarget == null) {
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    if (TeletorEntity.this.m_5448_() != null) {
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
                }
            }
        }
    }
}