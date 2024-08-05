package net.minecraft.world.entity.npc;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WanderingTrader extends AbstractVillager {

    private static final int NUMBER_OF_TRADE_OFFERS = 5;

    @Nullable
    private BlockPos wanderTarget;

    private int despawnDelay;

    public WanderingTrader(EntityType<? extends WanderingTrader> entityTypeExtendsWanderingTrader0, Level level1) {
        super(entityTypeExtendsWanderingTrader0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new UseItemGoal<>(this, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, p_289486_ -> this.m_9236_().isNight() && !p_289486_.m_20145_()));
        this.f_21345_.addGoal(0, new UseItemGoal<>(this, new ItemStack(Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, p_289487_ -> this.m_9236_().isDay() && p_289487_.m_20145_()));
        this.f_21345_.addGoal(1, new TradeWithPlayerGoal(this));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Zombie.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Evoker.class, 12.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Vindicator.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Vex.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Pillager.class, 15.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Illusioner.class, 12.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new AvoidEntityGoal(this, Zoglin.class, 10.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, new PanicGoal(this, 0.5));
        this.f_21345_.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.f_21345_.addGoal(2, new WanderingTrader.WanderToPositionGoal(this, 2.0, 0.35));
        this.f_21345_.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.f_21345_.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return null;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (!$$2.is(Items.VILLAGER_SPAWN_EGG) && this.m_6084_() && !this.m_35306_() && !this.m_6162_()) {
            if (interactionHand1 == InteractionHand.MAIN_HAND) {
                player0.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (this.m_6616_().isEmpty()) {
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            } else {
                if (!this.m_9236_().isClientSide) {
                    this.m_7189_(player0);
                    this.m_45301_(player0, this.m_5446_(), 1);
                }
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
        } else {
            return super.m_6071_(player0, interactionHand1);
        }
    }

    @Override
    protected void updateTrades() {
        VillagerTrades.ItemListing[] $$0 = (VillagerTrades.ItemListing[]) VillagerTrades.WANDERING_TRADER_TRADES.get(1);
        VillagerTrades.ItemListing[] $$1 = (VillagerTrades.ItemListing[]) VillagerTrades.WANDERING_TRADER_TRADES.get(2);
        if ($$0 != null && $$1 != null) {
            MerchantOffers $$2 = this.m_6616_();
            this.m_35277_($$2, $$0, 5);
            int $$3 = this.f_19796_.nextInt($$1.length);
            VillagerTrades.ItemListing $$4 = $$1[$$3];
            MerchantOffer $$5 = $$4.getOffer(this, this.f_19796_);
            if ($$5 != null) {
                $$2.add($$5);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("DespawnDelay", this.despawnDelay);
        if (this.wanderTarget != null) {
            compoundTag0.put("WanderTarget", NbtUtils.writeBlockPos(this.wanderTarget));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("DespawnDelay", 99)) {
            this.despawnDelay = compoundTag0.getInt("DespawnDelay");
        }
        if (compoundTag0.contains("WanderTarget")) {
            this.wanderTarget = NbtUtils.readBlockPos(compoundTag0.getCompound("WanderTarget"));
        }
        this.m_146762_(Math.max(0, this.m_146764_()));
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return false;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer merchantOffer0) {
        if (merchantOffer0.shouldRewardExp()) {
            int $$1 = 3 + this.f_19796_.nextInt(4);
            this.m_9236_().m_7967_(new ExperienceOrb(this.m_9236_(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), $$1));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_35306_() ? SoundEvents.WANDERING_TRADER_TRADE : SoundEvents.WANDERING_TRADER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }

    @Override
    protected SoundEvent getDrinkingSound(ItemStack itemStack0) {
        return itemStack0.is(Items.MILK_BUCKET) ? SoundEvents.WANDERING_TRADER_DRINK_MILK : SoundEvents.WANDERING_TRADER_DRINK_POTION;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean boolean0) {
        return boolean0 ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    public void setDespawnDelay(int int0) {
        this.despawnDelay = int0;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            this.maybeDespawn();
        }
    }

    private void maybeDespawn() {
        if (this.despawnDelay > 0 && !this.m_35306_() && --this.despawnDelay == 0) {
            this.m_146870_();
        }
    }

    public void setWanderTarget(@Nullable BlockPos blockPos0) {
        this.wanderTarget = blockPos0;
    }

    @Nullable
    BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    class WanderToPositionGoal extends Goal {

        final WanderingTrader trader;

        final double stopDistance;

        final double speedModifier;

        WanderToPositionGoal(WanderingTrader wanderingTrader0, double double1, double double2) {
            this.trader = wanderingTrader0;
            this.stopDistance = double1;
            this.speedModifier = double2;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            this.trader.setWanderTarget(null);
            WanderingTrader.this.f_21344_.stop();
        }

        @Override
        public boolean canUse() {
            BlockPos $$0 = this.trader.getWanderTarget();
            return $$0 != null && this.isTooFarAway($$0, this.stopDistance);
        }

        @Override
        public void tick() {
            BlockPos $$0 = this.trader.getWanderTarget();
            if ($$0 != null && WanderingTrader.this.f_21344_.isDone()) {
                if (this.isTooFarAway($$0, 10.0)) {
                    Vec3 $$1 = new Vec3((double) $$0.m_123341_() - this.trader.m_20185_(), (double) $$0.m_123342_() - this.trader.m_20186_(), (double) $$0.m_123343_() - this.trader.m_20189_()).normalize();
                    Vec3 $$2 = $$1.scale(10.0).add(this.trader.m_20185_(), this.trader.m_20186_(), this.trader.m_20189_());
                    WanderingTrader.this.f_21344_.moveTo($$2.x, $$2.y, $$2.z, this.speedModifier);
                } else {
                    WanderingTrader.this.f_21344_.moveTo((double) $$0.m_123341_(), (double) $$0.m_123342_(), (double) $$0.m_123343_(), this.speedModifier);
                }
            }
        }

        private boolean isTooFarAway(BlockPos blockPos0, double double1) {
            return !blockPos0.m_203195_(this.trader.m_20182_(), double1);
        }
    }
}