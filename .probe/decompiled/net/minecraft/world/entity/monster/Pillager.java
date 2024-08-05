package net.minecraft.world.entity.monster;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;

public class Pillager extends AbstractIllager implements CrossbowAttackMob, InventoryCarrier {

    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);

    private static final int INVENTORY_SIZE = 5;

    private static final int SLOT_OFFSET = 300;

    private static final float CROSSBOW_POWER = 1.6F;

    private final SimpleContainer inventory = new SimpleContainer(5);

    public Pillager(EntityType<? extends Pillager> entityTypeExtendsPillager0, Level level1) {
        super(entityTypeExtendsPillager0, level1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(2, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.f_21345_.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0, 8.0F));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(IS_CHARGING_CROSSBOW, false);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem0) {
        return projectileWeaponItem0 == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.f_19804_.get(IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean boolean0) {
        this.f_19804_.set(IS_CHARGING_CROSSBOW, boolean0);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.f_20891_ = 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        this.m_252802_(compoundTag0);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.m_21055_(Items.CROSSBOW)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.m_5912_() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.m_253224_(compoundTag0);
        this.m_21553_(true);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return 0.0F;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        this.populateDefaultEquipmentSlots($$5, difficultyInstance1);
        this.m_213946_($$5, difficultyInstance1);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    @Override
    protected void enchantSpawnedWeapon(RandomSource randomSource0, float float1) {
        super.m_214095_(randomSource0, float1);
        if (randomSource0.nextInt(300) == 0) {
            ItemStack $$2 = this.m_21205_();
            if ($$2.is(Items.CROSSBOW)) {
                Map<Enchantment, Integer> $$3 = EnchantmentHelper.getEnchantments($$2);
                $$3.putIfAbsent(Enchantments.PIERCING, 1);
                EnchantmentHelper.setEnchantments($$3, $$2);
                this.m_8061_(EquipmentSlot.MAINHAND, $$2);
            }
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity0) {
        if (super.m_7307_(entity0)) {
            return true;
        } else {
            return entity0 instanceof LivingEntity && ((LivingEntity) entity0).getMobType() == MobType.ILLAGER ? this.m_5647_() == null && entity0.getTeam() == null : false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        this.m_32336_(this, 1.6F);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity0, ItemStack itemStack1, Projectile projectile2, float float3) {
        this.m_32322_(this, livingEntity0, projectile2, float3, 1.6F);
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity0) {
        ItemStack $$1 = itemEntity0.getItem();
        if ($$1.getItem() instanceof BannerItem) {
            super.m_7581_(itemEntity0);
        } else if (this.wantsItem($$1)) {
            this.m_21053_(itemEntity0);
            ItemStack $$2 = this.inventory.addItem($$1);
            if ($$2.isEmpty()) {
                itemEntity0.m_146870_();
            } else {
                $$1.setCount($$2.getCount());
            }
        }
    }

    private boolean wantsItem(ItemStack itemStack0) {
        return this.m_37886_() && itemStack0.is(Items.WHITE_BANNER);
    }

    @Override
    public SlotAccess getSlot(int int0) {
        int $$1 = int0 - 300;
        return $$1 >= 0 && $$1 < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, $$1) : super.m_141942_(int0);
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
        Raid $$2 = this.m_37885_();
        boolean $$3 = this.f_19796_.nextFloat() <= $$2.getEnchantOdds();
        if ($$3) {
            ItemStack $$4 = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> $$5 = Maps.newHashMap();
            if (int0 > $$2.getNumGroups(Difficulty.NORMAL)) {
                $$5.put(Enchantments.QUICK_CHARGE, 2);
            } else if (int0 > $$2.getNumGroups(Difficulty.EASY)) {
                $$5.put(Enchantments.QUICK_CHARGE, 1);
            }
            $$5.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments($$5, $$4);
            this.m_8061_(EquipmentSlot.MAINHAND, $$4);
        }
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }
}