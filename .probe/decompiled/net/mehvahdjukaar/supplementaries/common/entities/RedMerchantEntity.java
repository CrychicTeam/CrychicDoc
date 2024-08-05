package net.mehvahdjukaar.supplementaries.common.entities;

import java.util.EnumSet;
import java.util.OptionalInt;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.entities.goals.EquipAndRangeAttackGoal;
import net.mehvahdjukaar.supplementaries.common.entities.goals.ShowWaresGoal;
import net.mehvahdjukaar.supplementaries.common.entities.trades.ModVillagerTrades;
import net.mehvahdjukaar.supplementaries.common.inventories.RedMerchantMenu;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSyncTradesPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RedMerchantEntity extends AbstractVillager implements RangedAttackMob {

    @Nullable
    private BlockPos wanderTarget;

    private int despawnDelay;

    private int attackCooldown = 0;

    public RedMerchantEntity(EntityType<? extends RedMerchantEntity> type, Level world) {
        super(type, world);
    }

    public RedMerchantEntity(Level world) {
        this((EntityType<? extends RedMerchantEntity>) ModEntities.RED_MERCHANT.get(), world);
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(2, new EquipAndRangeAttackGoal(this, 0.35, 60, 10, 20, 15.0F, new ItemStack((ItemLike) ModRegistry.BOMB_ITEM.get())));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Mob.class, 8, true, false, mob -> mob instanceof Raider || mob instanceof Zombie || mob instanceof Zoglin));
        this.f_21345_.addGoal(3, new TradeWithPlayerGoal(this));
        this.f_21345_.addGoal(3, new LookAtTradingPlayerGoal(this));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Zombie.class, 6.0F, 0.5, 0.5));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Vex.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Creeper.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Raider.class, 11.0F, 0.5, 0.5));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Zoglin.class, 8.0F, 0.5, 0.5));
        this.f_21345_.addGoal(4, new ShowWaresGoal(this, 400, 1600));
        this.f_21345_.addGoal(4, new RedMerchantEntity.MoveToGoal(this, 2.0, 0.35));
        this.f_21345_.addGoal(5, new MoveTowardsRestrictionGoal(this, 0.35));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.m_6084_() && !this.m_35306_() && !this.m_6162_()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            Level level = this.m_9236_();
            if (!this.m_6616_().isEmpty() && !level.isClientSide) {
                this.m_7189_(player);
                this.openTradingScreen(player, this.m_5446_(), 1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.m_6071_(player, hand);
        }
    }

    @Override
    public void updateTrades() {
        MerchantOffers merchantoffers = this.m_6616_();
        this.m_35277_(merchantoffers, ModVillagerTrades.getRedMerchantTrades(), 7);
    }

    @Override
    public void openTradingScreen(Player player, Component name, int level) {
        OptionalInt optionalint = player.openMenu(new SimpleMenuProvider((i, p, m) -> new RedMerchantMenu(i, p, this), name));
        if (optionalint.isPresent() && player instanceof ServerPlayer serverPlayer) {
            MerchantOffers merchantoffers = this.m_6616_();
            if (!merchantoffers.isEmpty()) {
                ModNetwork.CHANNEL.sendToClientPlayer(serverPlayer, new ClientBoundSyncTradesPacket(optionalint.getAsInt(), merchantoffers, level, this.m_7809_(), this.showProgressBar(), this.m_7862_()));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("DespawnDelay", this.despawnDelay);
        if (this.wanderTarget != null) {
            compound.put("WanderTarget", NbtUtils.writeBlockPos(this.wanderTarget));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("DespawnDelay", 99)) {
            this.despawnDelay = compound.getInt("DespawnDelay");
        }
        if (compound.contains("WanderTarget")) {
            this.wanderTarget = NbtUtils.readBlockPos(compound.getCompound("WanderTarget"));
        }
        this.m_146762_(Math.max(0, this.m_146764_()));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        if (merchantOffer.shouldRewardExp()) {
            int i = 3 + this.f_19796_.nextInt(4);
            this.m_9236_().m_7967_(new ExperienceOrb(this.m_9236_(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), i));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_35306_() ? SoundEvents.WANDERING_TRADER_TRADE : SoundEvents.WANDERING_TRADER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }

    @Override
    protected SoundEvent getDrinkingSound(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.MILK_BUCKET ? SoundEvents.WANDERING_TRADER_DRINK_MILK : SoundEvents.WANDERING_TRADER_DRINK_POTION;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean isYesSound) {
        return isYesSound ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    public void setDespawnDelay(int i) {
        this.despawnDelay = i;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
            this.maybeDespawn();
        }
    }

    private void maybeDespawn() {
        if (this.despawnDelay > 0 && !this.m_35306_() && --this.despawnDelay == 0) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void setWanderTarget(@Nullable BlockPos pos) {
        this.wanderTarget = pos;
    }

    @Nullable
    private BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        Vec3 vector3d = target.m_20184_();
        double d0 = target.m_20185_() + vector3d.x - this.m_20185_();
        double d1 = target.m_20188_() - 3.5 - this.m_20186_();
        double d2 = target.m_20189_() + vector3d.z - this.m_20189_();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        Level level = this.m_9236_();
        BombEntity bomb = new BombEntity(level, this, BombEntity.BombType.NORMAL);
        bomb.m_6686_(d0, d1 + (double) (f * 0.24F), d2, 1.25F, 0.9F);
        if (!this.m_20067_()) {
            level.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_THROW, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
        }
        level.m_7967_(bomb);
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        amount = super.m_6515_(source, amount);
        if (source.getEntity() == this) {
            amount = 0.0F;
        }
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            amount = (float) ((double) amount * 0.2);
        }
        return amount;
    }

    class MoveToGoal extends Goal {

        final RedMerchantEntity trader;

        final double stopDistance;

        final double speedModifier;

        MoveToGoal(RedMerchantEntity redMerchantEntity, double v, double v1) {
            this.trader = redMerchantEntity;
            this.stopDistance = v;
            this.speedModifier = v1;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            this.trader.setWanderTarget(null);
            RedMerchantEntity.this.f_21344_.stop();
        }

        @Override
        public boolean canUse() {
            BlockPos blockpos = this.trader.getWanderTarget();
            return blockpos != null && this.isTooFarAway(blockpos, this.stopDistance);
        }

        @Override
        public void tick() {
            BlockPos blockpos = this.trader.getWanderTarget();
            if (blockpos != null && RedMerchantEntity.this.f_21344_.isDone()) {
                if (this.isTooFarAway(blockpos, 10.0)) {
                    Vec3 vector3d = new Vec3((double) blockpos.m_123341_() - this.trader.m_20185_(), (double) blockpos.m_123342_() - this.trader.m_20186_(), (double) blockpos.m_123343_() - this.trader.m_20189_()).normalize();
                    Vec3 vector3d1 = vector3d.scale(10.0).add(this.trader.m_20185_(), this.trader.m_20186_(), this.trader.m_20189_());
                    RedMerchantEntity.this.f_21344_.moveTo(vector3d1.x, vector3d1.y, vector3d1.z, this.speedModifier);
                } else {
                    RedMerchantEntity.this.f_21344_.moveTo((double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), this.speedModifier);
                }
            }
        }

        private boolean isTooFarAway(BlockPos pos, double v) {
            return !pos.m_203195_(this.trader.m_20182_(), v);
        }
    }
}