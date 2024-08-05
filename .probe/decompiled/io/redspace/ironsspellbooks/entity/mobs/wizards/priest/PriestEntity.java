package io.redspace.ironsspellbooks.entity.mobs.wizards.priest;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.FindSupportableTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericDefendVillageTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GustDefenseGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.HomeOwner;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.ReturnToHomeAtNightGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.RoamVillageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardSupportGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PriestEntity extends NeutralWizard implements VillagerDataHolder, SupportMob, HomeOwner, IMerchantWizard {

    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.VILLAGER_DATA);

    private static final EntityDataAccessor<Boolean> DATA_VILLAGER_UNHAPPY = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.BOOLEAN);

    public GoalSelector supportTargetSelector;

    private int unhappyTimer;

    LivingEntity supportTarget;

    BlockPos homePos;

    @Nullable
    private Player tradingPlayer;

    @Nullable
    protected MerchantOffers offers;

    private long lastRestockGameTime;

    private int numberOfRestocksToday;

    private long lastRestockCheckDayTime;

    public PriestEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 15;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new OpenDoorGoal(this, true));
        this.f_21345_.addGoal(1, new GustDefenseGoal(this));
        this.f_21345_.addGoal(2, new WizardSupportGoal<>(this, 1.25, 100, 180).setSpells(List.of(SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), SpellRegistry.HEALING_CIRCLE_SPELL.get()), List.of(SpellRegistry.FORTIFY_SPELL.get())));
        this.f_21345_.addGoal(3, new WizardAttackGoal(this, 1.25, 35, 70).setSpells(List.of(SpellRegistry.WISP_SPELL.get(), SpellRegistry.GUIDING_BOLT_SPELL.get()), List.of(SpellRegistry.GUST_SPELL.get()), List.of(), List.of(SpellRegistry.HEAL_SPELL.get())).setSpellQuality(0.3F, 0.5F).setDrinksPotions());
        this.f_21345_.addGoal(5, new RoamVillageGoal(this, 30.0F, 1.0));
        this.f_21345_.addGoal(6, new ReturnToHomeAtNightGoal<>(this, 1.0));
        this.f_21345_.addGoal(7, new PatrolNearLocationGoal(this, 30.0F, 1.0));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(10, new WizardRecoverGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new GenericDefendVillageTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isHostileTowards));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 5, false, false, mob -> mob instanceof Enemy && !(mob instanceof Creeper)));
        this.f_21346_.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
        this.supportTargetSelector = new GoalSelector(this.f_19853_.getProfilerSupplier());
        this.supportTargetSelector.addGoal(0, new FindSupportableTargetGoal<>(this, LivingEntity.class, true, mob -> !this.m_21674_(mob) && mob.getHealth() * 1.25F < mob.getMaxHealth() && (mob.m_6095_().is(ModTags.VILLAGE_ALLIES) || mob instanceof Player)));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        if (this.f_19853_ instanceof ServerLevel serverLevel) {
            Optional<BlockPos> optional1 = serverLevel.getPoiManager().find(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), blockPos -> true, this.m_20183_(), 100, PoiManager.Occupancy.ANY);
            optional1.ifPresent(blockPos -> this.setHome(blockPos));
        }
        return super.m_6518_(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.m_8061_(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.PRIEST_HELMET.get()));
        this.m_8061_(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.PRIEST_CHESTPLATE.get()));
        this.m_21409_(EquipmentSlot.HEAD, 0.0F);
        this.m_21409_(EquipmentSlot.CHEST, 0.0F);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(AttributeRegistry.CAST_TIME_REDUCTION.get(), 1.5).add(Attributes.MOVEMENT_SPEED, 0.23);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new GroundPathNavigation(this, pLevel) {

            @Override
            protected PathFinder createPathFinder(int pMaxVisitedNodes) {
                this.f_26508_ = new WalkNodeEvaluator();
                this.f_26508_.setCanPassDoors(true);
                this.f_26508_.setCanOpenDoors(true);
                return new PathFinder(this.f_26508_, pMaxVisitedNodes);
            }
        };
    }

    @javax.annotation.Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.m_5803_()) {
            return null;
        } else {
            return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
        this.f_19804_.define(DATA_VILLAGER_UNHAPPY, false);
    }

    @Override
    public void setVillagerData(VillagerData villagerdata) {
        villagerdata.setProfession(VillagerProfession.NONE);
        this.f_19804_.set(DATA_VILLAGER_DATA, villagerdata);
    }

    public boolean isUnhappy() {
        return this.f_19804_.get(DATA_VILLAGER_UNHAPPY);
    }

    @NotNull
    @Override
    public VillagerData getVillagerData() {
        return this.f_19804_.get(DATA_VILLAGER_DATA);
    }

    @Nullable
    @Override
    public LivingEntity getSupportTarget() {
        return this.supportTarget;
    }

    @Override
    public void setSupportTarget(LivingEntity target) {
        this.supportTarget = target;
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.f_19797_ % 4 == 0 && this.f_19797_ > 1) {
            this.supportTargetSelector.tick();
        }
        if (this.f_19797_ % 60 == 0) {
            this.f_19853_.getEntities(this, this.m_20191_().inflate(this.m_21133_(Attributes.FOLLOW_RANGE)), entity -> entity instanceof Enemy && !(entity instanceof Creeper) && !(entity instanceof MagicSummon) && !(entity instanceof TamableAnimal)).forEach(enemy -> {
                if (enemy instanceof Mob mob && mob.getTarget() == null && TargetingConditions.forCombat().test(mob, this)) {
                    mob.setTarget(this);
                }
            });
        }
        if (this.unhappyTimer > 0 && --this.unhappyTimer == 0) {
            this.f_19804_.set(DATA_VILLAGER_UNHAPPY, false);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        boolean preventTrade = this.getOffers().isEmpty() || this.m_5448_() != null || this.m_21674_(pPlayer);
        if (pHand == InteractionHand.MAIN_HAND && preventTrade && !this.f_19853_.isClientSide) {
            this.setUnhappy();
        }
        if (!preventTrade) {
            if (!this.f_19853_.isClientSide && !this.getOffers().isEmpty()) {
                if (this.shouldRestock()) {
                    this.restock();
                }
                this.startTrading(pPlayer);
            }
            return InteractionResult.sidedSuccess(this.f_19853_.isClientSide);
        } else {
            return super.m_6071_(pPlayer, pHand);
        }
    }

    public void setUnhappy() {
        if (!this.f_19853_.isClientSide) {
            this.m_5496_(SoundEvents.VILLAGER_NO, this.m_6121_(), this.m_6100_());
            this.unhappyTimer = 20;
            this.f_19804_.set(DATA_VILLAGER_UNHAPPY, true);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.serializeHome(this, pCompound);
        this.serializeMerchant(pCompound, this.offers, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.deserializeHome(this, pCompound);
        this.deserializeMerchant(pCompound, c -> this.offers = c);
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundEvents.VILLAGER_NO);
    }

    @Nullable
    @Override
    public BlockPos getHome() {
        return this.homePos;
    }

    @Override
    public void setHome(BlockPos homePos) {
        this.homePos = homePos;
    }

    @Override
    public void setTradingPlayer(@Nullable Player pTradingPlayer) {
        this.tradingPlayer = pTradingPlayer;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 24), ItemStack.EMPTY, FurledMapItem.of(IronsSpellbooks.id("evoker_fort"), Component.translatable("item.irons_spellbooks.evoker_fort_battle_plans")), 0, 1, 5, 10.0F));
            this.offers.add(new MerchantOffer(new ItemStack(ItemRegistry.GREATER_HEALING_POTION.get()), new ItemStack(Items.EMERALD, 18), 3, 0, 0.2F));
            this.offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 6), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING), 2, 0, 0.2F));
            this.offers.add(new PriestEntity.BibleTrade().m_213663_(this, this.f_19796_));
            this.offers.removeIf(Objects::isNull);
            this.numberOfRestocksToday++;
        }
        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers pOffers) {
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.isTrading();
    }

    @Override
    public void notifyTrade(MerchantOffer pOffer) {
        pOffer.increaseUses();
        this.f_21363_ = -this.m_8100_();
    }

    @Override
    public void notifyTradeUpdated(ItemStack pStack) {
        if (!this.f_19853_.isClientSide && this.f_21363_ > -this.m_8100_() + 20) {
            this.f_21363_ = -this.m_8100_();
            this.m_5496_(this.getTradeUpdatedSound(!pStack.isEmpty()), this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }

    private void startTrading(Player pPlayer) {
        this.setTradingPlayer(pPlayer);
        this.f_21365_.setLookAt(pPlayer);
        this.m_45301_(pPlayer, this.m_5446_(), this.getVillagerData().getLevel());
    }

    @Override
    public int getRestocksToday() {
        return this.numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocks) {
        this.numberOfRestocksToday = restocks;
    }

    @Override
    public long getLastRestockGameTime() {
        return this.lastRestockGameTime;
    }

    @Override
    public void setLastRestockGameTime(long time) {
        this.lastRestockGameTime = time;
    }

    @Override
    public long getLastRestockCheckDayTime() {
        return this.lastRestockCheckDayTime;
    }

    @Override
    public void setLastRestockCheckDayTime(long time) {
        this.lastRestockCheckDayTime = time;
    }

    @Override
    public Level level() {
        return this.f_19853_;
    }

    static class BibleTrade extends AdditionalWanderingTrades.SimpleTrade {

        private BibleTrade() {
            super((trader, random) -> {
                if (!trader.level.isClientSide) {
                    LootTable loottable = trader.level.getServer().getLootData().m_278676_(IronsSpellbooks.id("magic_items/archevoker_logbook_translated"));
                    LootParams context = new LootParams.Builder((ServerLevel) trader.level).create(LootContextParamSets.EMPTY);
                    ObjectArrayList<ItemStack> items = loottable.getRandomItems(context);
                    if (!items.isEmpty()) {
                        ItemStack cost = (ItemStack) items.get(0);
                        ItemStack forSale = new ItemStack(ItemRegistry.VILLAGER_SPELL_BOOK.get());
                        return new MerchantOffer(cost, forSale, 1, 5, 0.5F);
                    }
                }
                return new MerchantOffer(ItemStack.EMPTY, ItemStack.EMPTY, 0, 0, 0.0F);
            });
        }
    }
}