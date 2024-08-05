package net.minecraft.world.entity.npc;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractVillager extends AgeableMob implements InventoryCarrier, Npc, Merchant {

    private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);

    public static final int VILLAGER_SLOT_OFFSET = 300;

    private static final int VILLAGER_INVENTORY_SIZE = 8;

    @Nullable
    private Player tradingPlayer;

    @Nullable
    protected MerchantOffers offers;

    private final SimpleContainer inventory = new SimpleContainer(8);

    public AbstractVillager(EntityType<? extends AbstractVillager> entityTypeExtendsAbstractVillager0, Level level1) {
        super(entityTypeExtendsAbstractVillager0, level1);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(false);
        }
        return super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public int getUnhappyCounter() {
        return this.f_19804_.get(DATA_UNHAPPY_COUNTER);
    }

    public void setUnhappyCounter(int int0) {
        this.f_19804_.set(DATA_UNHAPPY_COUNTER, int0);
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? 0.81F : 1.62F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_UNHAPPY_COUNTER, 0);
    }

    @Override
    public void setTradingPlayer(@Nullable Player player0) {
        this.tradingPlayer = player0;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.updateTrades();
        }
        return this.offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers merchantOffers0) {
    }

    @Override
    public void overrideXp(int int0) {
    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer0) {
        merchantOffer0.increaseUses();
        this.f_21363_ = -this.m_8100_();
        this.rewardTradeXp(merchantOffer0);
        if (this.tradingPlayer instanceof ServerPlayer) {
            CriteriaTriggers.TRADE.trigger((ServerPlayer) this.tradingPlayer, this, merchantOffer0.getResult());
        }
    }

    protected abstract void rewardTradeXp(MerchantOffer var1);

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack0) {
        if (!this.m_9236_().isClientSide && this.f_21363_ > -this.m_8100_() + 20) {
            this.f_21363_ = -this.m_8100_();
            this.m_5496_(this.getTradeUpdatedSound(!itemStack0.isEmpty()), this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    protected SoundEvent getTradeUpdatedSound(boolean boolean0) {
        return boolean0 ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }

    public void playCelebrateSound() {
        this.m_5496_(SoundEvents.VILLAGER_CELEBRATE, this.m_6121_(), this.m_6100_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        MerchantOffers $$1 = this.getOffers();
        if (!$$1.isEmpty()) {
            compoundTag0.put("Offers", $$1.createTag());
        }
        this.m_252802_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("Offers", 10)) {
            this.offers = new MerchantOffers(compoundTag0.getCompound("Offers"));
        }
        this.m_253224_(compoundTag0);
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel0) {
        this.stopTrading();
        return super.m_5489_(serverLevel0);
    }

    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    @Override
    public void die(DamageSource damageSource0) {
        super.m_6667_(damageSource0);
        this.stopTrading();
    }

    protected void addParticlesAroundSelf(ParticleOptions particleOptions0) {
        for (int $$1 = 0; $$1 < 5; $$1++) {
            double $$2 = this.f_19796_.nextGaussian() * 0.02;
            double $$3 = this.f_19796_.nextGaussian() * 0.02;
            double $$4 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(particleOptions0, this.m_20208_(1.0), this.m_20187_() + 1.0, this.m_20262_(1.0), $$2, $$3, $$4);
        }
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return false;
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public SlotAccess getSlot(int int0) {
        int $$1 = int0 - 300;
        return $$1 >= 0 && $$1 < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, $$1) : super.m_141942_(int0);
    }

    protected abstract void updateTrades();

    protected void addOffersFromItemListings(MerchantOffers merchantOffers0, VillagerTrades.ItemListing[] villagerTradesItemListing1, int int2) {
        Set<Integer> $$3 = Sets.newHashSet();
        if (villagerTradesItemListing1.length > int2) {
            while ($$3.size() < int2) {
                $$3.add(this.f_19796_.nextInt(villagerTradesItemListing1.length));
            }
        } else {
            for (int $$4 = 0; $$4 < villagerTradesItemListing1.length; $$4++) {
                $$3.add($$4);
            }
        }
        for (Integer $$5 : $$3) {
            VillagerTrades.ItemListing $$6 = villagerTradesItemListing1[$$5];
            MerchantOffer $$7 = $$6.getOffer(this, this.f_19796_);
            if ($$7 != null) {
                merchantOffers0.add($$7);
            }
        }
    }

    @Override
    public Vec3 getRopeHoldPosition(float float0) {
        float $$1 = Mth.lerp(float0, this.f_20884_, this.f_20883_) * (float) (Math.PI / 180.0);
        Vec3 $$2 = new Vec3(0.0, this.m_20191_().getYsize() - 1.0, 0.2);
        return this.m_20318_(float0).add($$2.yRot(-$$1));
    }

    @Override
    public boolean isClientSide() {
        return this.m_9236_().isClientSide;
    }
}