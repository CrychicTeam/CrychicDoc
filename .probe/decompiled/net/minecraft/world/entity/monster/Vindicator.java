package net.minecraft.world.entity.monster;

import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class Vindicator extends AbstractIllager {

    private static final String TAG_JOHNNY = "Johnny";

    static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = p_34082_ -> p_34082_ == Difficulty.NORMAL || p_34082_ == Difficulty.HARD;

    boolean isJohnny;

    public Vindicator(EntityType<? extends Vindicator> entityTypeExtendsVindicator0, Level level1) {
        super(entityTypeExtendsVindicator0, level1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new Vindicator.VindicatorBreakDoorGoal(this));
        this.f_21345_.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
        this.f_21345_.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.f_21345_.addGoal(4, new Vindicator.VindicatorMeleeAttackGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.f_21346_.addGoal(4, new Vindicator.VindicatorJohnnyAttackGoal(this));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected void customServerAiStep() {
        if (!this.m_21525_() && GoalUtils.hasGroundPathNavigation(this)) {
            boolean $$0 = ((ServerLevel) this.m_9236_()).isRaided(this.m_20183_());
            ((GroundPathNavigation) this.m_21573_()).setCanOpenDoors($$0);
        }
        super.m_8024_();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        if (this.isJohnny) {
            compoundTag0.putBoolean("Johnny", true);
        }
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.m_5912_()) {
            return AbstractIllager.IllagerArmPose.ATTACKING;
        } else {
            return this.m_37888_() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("Johnny", 99)) {
            this.isJohnny = compoundTag0.getBoolean("Johnny");
        }
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        SpawnGroupData $$5 = super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        ((GroundPathNavigation) this.m_21573_()).setCanOpenDoors(true);
        RandomSource $$6 = serverLevelAccessor0.m_213780_();
        this.populateDefaultEquipmentSlots($$6, difficultyInstance1);
        this.m_213946_($$6, difficultyInstance1);
        return $$5;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        if (this.m_37885_() == null) {
            this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
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
    public void setCustomName(@Nullable Component component0) {
        super.m_6593_(component0);
        if (!this.isJohnny && component0 != null && component0.getString().equals("Johnny")) {
            this.isJohnny = true;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
        ItemStack $$2 = new ItemStack(Items.IRON_AXE);
        Raid $$3 = this.m_37885_();
        int $$4 = 1;
        if (int0 > $$3.getNumGroups(Difficulty.NORMAL)) {
            $$4 = 2;
        }
        boolean $$5 = this.f_19796_.nextFloat() <= $$3.getEnchantOdds();
        if ($$5) {
            Map<Enchantment, Integer> $$6 = Maps.newHashMap();
            $$6.put(Enchantments.SHARPNESS, $$4);
            EnchantmentHelper.setEnchantments($$6, $$2);
        }
        this.m_8061_(EquipmentSlot.MAINHAND, $$2);
    }

    static class VindicatorBreakDoorGoal extends BreakDoorGoal {

        public VindicatorBreakDoorGoal(Mob mob0) {
            super(mob0, 6, Vindicator.DOOR_BREAKING_PREDICATE);
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canContinueToUse() {
            Vindicator $$0 = (Vindicator) this.f_25189_;
            return $$0.m_37886_() && super.canContinueToUse();
        }

        @Override
        public boolean canUse() {
            Vindicator $$0 = (Vindicator) this.f_25189_;
            return $$0.m_37886_() && $$0.f_19796_.nextInt(m_186073_(10)) == 0 && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.f_25189_.m_21310_(0);
        }
    }

    static class VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {

        public VindicatorJohnnyAttackGoal(Vindicator vindicator0) {
            super(vindicator0, LivingEntity.class, 0, true, true, LivingEntity::m_5789_);
        }

        @Override
        public boolean canUse() {
            return ((Vindicator) this.f_26135_).isJohnny && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.f_26135_.m_21310_(0);
        }
    }

    class VindicatorMeleeAttackGoal extends MeleeAttackGoal {

        public VindicatorMeleeAttackGoal(Vindicator vindicator0) {
            super(vindicator0, 1.0, false);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity livingEntity0) {
            if (this.f_25540_.m_20202_() instanceof Ravager) {
                float $$1 = this.f_25540_.m_20202_().getBbWidth() - 0.1F;
                return (double) ($$1 * 2.0F * $$1 * 2.0F + livingEntity0.m_20205_());
            } else {
                return super.getAttackReachSqr(livingEntity0);
            }
        }
    }
}