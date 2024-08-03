package io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.AlchemistAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class ApothecaristEntity extends NeutralWizard implements IMerchantWizard {

    @Nullable
    private Player tradingPlayer;

    @Nullable
    protected MerchantOffers offers;

    private long lastRestockGameTime;

    private int numberOfRestocksToday;

    private long lastRestockCheckDayTime;

    private static final List<MerchantOffer> fillerOffers = List.of(new MerchantOffer(new ItemStack(Items.EMERALD, 4), ItemStack.EMPTY, new ItemStack(Items.MAGMA_CREAM, 1), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 6), ItemStack.EMPTY, new ItemStack(Items.HONEY_BOTTLE, 2), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 10), ItemStack.EMPTY, new ItemStack(Items.NETHER_WART, 5), 0, 5, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 3), ItemStack.EMPTY, new ItemStack(Items.GLOWSTONE_DUST), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 3), ItemStack.EMPTY, new ItemStack(Items.REDSTONE), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, new ItemStack(Items.GLOW_INK_SAC), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 4), ItemStack.EMPTY, new ItemStack(Items.HONEYCOMB), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 7), ItemStack.EMPTY, new ItemStack(Items.FERMENTED_SPIDER_EYE, 2), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 12), ItemStack.EMPTY, new ItemStack(Items.RABBIT_FOOT, 1), 0, 3, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 9), ItemStack.EMPTY, new ItemStack(Items.GLISTERING_MELON_SLICE, 2), 0, 4, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 12), ItemStack.EMPTY, new ItemStack(Items.CRIMSON_FUNGUS, 4), 0, 4, 5, 0.01F), new MerchantOffer(new ItemStack(Items.EMERALD, 12), ItemStack.EMPTY, new ItemStack(Items.WARPED_FUNGUS, 4), 0, 4, 5, 0.01F), new MerchantOffer(new ItemStack(Items.APPLE, 12), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 6), 0, 6, 5, 0.01F), new MerchantOffer(new ItemStack(Items.BEETROOT, 10), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 8), 0, 6, 5, 0.01F), new MerchantOffer(new ItemStack(Items.CARROT, 6), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 4), 0, 6, 5, 0.01F), new MerchantOffer(new ItemStack(Items.PORKCHOP, 6), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 6), 0, 6, 5, 0.01F), new MerchantOffer(new ItemStack(Items.DRAGON_BREATH, 1), ItemStack.EMPTY, new ItemStack(ItemRegistry.ARCANE_ESSENCE.get(), 8), 0, 8, 5, 0.01F), new MerchantOffer(new ItemStack(Items.AXOLOTL_BUCKET, 1), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 16), 0, 1, 5, 0.01F));

    public ApothecaristEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 25;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new AlchemistAttackGoal(this, 1.25, 30, 70, 12.0F, 0.5F).setSpells(List.of(SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.ACID_ORB_SPELL.get(), SpellRegistry.POISON_BREATH_SPELL.get(), SpellRegistry.STOMP_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get()), List.of(SpellRegistry.ROOT_SPELL.get()), List.of(), List.of(SpellRegistry.OAKSKIN_SPELL.get(), SpellRegistry.STOMP_SPELL.get())).setDrinksPotions().setSingleUseSpell(SpellRegistry.FIREFLY_SWARM_SPELL.get(), 80, 200, 4, 6).setSpellQuality(0.25F, 0.6F));
        this.f_21345_.addGoal(3, new PatrolNearLocationGoal(this, 30.0F, 0.75));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(10, new WizardRecoverGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, AbstractPiglin.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isHostileTowards));
        this.f_21346_.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19853_.isClientSide && this.f_20913_ > 0) {
            this.f_20913_--;
        }
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.f_20913_ = 10;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.m_6518_(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.m_8061_(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.PLAGUED_CHESTPLATE.get()));
        this.m_21409_(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return pSource.is(DamageTypes.MAGIC) && pSource.getEntity() == this ? false : super.hurt(pSource, pAmount);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pEffectInstance) {
        return !AlchemistAttackGoal.ATTACK_POTIONS.contains(pEffectInstance.getEffect());
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.f_19797_ % 60 == 0) {
            this.f_19853_.m_45976_(AbstractPiglin.class, this.m_20191_().inflate(this.m_21133_(Attributes.FOLLOW_RANGE))).forEach(piggy -> {
                if (PiglinAi.getAngerTarget(piggy).isEmpty() && TargetingConditions.forCombat().test(piggy, this)) {
                    PiglinAi.setAngerTarget(piggy, this);
                }
            });
        }
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        boolean preventTrade = this.getOffers().isEmpty() || this.m_5448_() != null || this.m_21674_(pPlayer);
        if (pHand == InteractionHand.MAIN_HAND && preventTrade && !this.f_19853_.isClientSide) {
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

    private void startTrading(Player pPlayer) {
        this.setTradingPlayer(pPlayer);
        this.f_21365_.setLookAt(pPlayer);
        this.m_45301_(pPlayer, this.m_5446_(), 0);
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

    @Override
    public void setTradingPlayer(@org.jetbrains.annotations.Nullable Player pTradingPlayer) {
        this.tradingPlayer = pTradingPlayer;
    }

    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.offers.addAll(this.createRandomOffers(3, 4));
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()).m_213663_(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_RARE.get()).m_213663_(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_EPIC.get()).m_213663_(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.5F) {
                this.offers.add(new AdditionalWanderingTrades.ExilirBuyTrade(true, false).m_213663_(this, this.f_19796_));
            }
            int j = this.f_19796_.nextIntBetweenInclusive(1, 3);
            for (int i = 0; i < j; i++) {
                this.offers.add(this.f_19796_.nextBoolean() ? new AdditionalWanderingTrades.PotionSellTrade(null).m_213663_(this, this.f_19796_) : new AdditionalWanderingTrades.ExilirSellTrade(true, false).m_213663_(this, this.f_19796_));
            }
            this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.NATURE.get()), 0.0F, 0.4F).getOffer(this, this.f_19796_));
            if (this.f_19796_.nextFloat() < 0.65F) {
                this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.NATURE.get()), 0.5F, 0.9F).getOffer(this, this.f_19796_));
            }
            this.offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 16), ItemStack.EMPTY, new ItemStack(ItemRegistry.NETHERWARD_TINCTURE.get(), 1), 0, 8, 5, 0.01F));
            this.offers.removeIf(Objects::isNull);
            this.numberOfRestocksToday++;
        }
        return this.offers;
    }

    private Collection<MerchantOffer> createRandomOffers(int min, int max) {
        Set<Integer> set = Sets.newHashSet();
        int fillerTrades = this.f_19796_.nextIntBetweenInclusive(min, max);
        for (int i = 0; i < 10 && set.size() < fillerTrades; i++) {
            set.add(this.f_19796_.nextInt(fillerOffers.size()));
        }
        Collection<MerchantOffer> offers = new ArrayList();
        for (Integer integer : set) {
            offers.add((MerchantOffer) fillerOffers.get(integer));
        }
        return offers;
    }

    @Override
    public void overrideOffers(MerchantOffers pOffers) {
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.isTrading();
    }

    @Override
    public void notifyTrade(MerchantOffer pOffer) {
        pOffer.increaseUses();
        this.f_21363_ = -this.getAmbientSoundInterval();
    }

    @Override
    public void notifyTradeUpdated(ItemStack pStack) {
        if (!this.f_19853_.isClientSide && this.f_21363_ > -this.getAmbientSoundInterval() + 20) {
            this.f_21363_ = -this.getAmbientSoundInterval();
            this.m_5496_(this.getTradeUpdatedSound(!pStack.isEmpty()), this.m_6121_(), this.m_6100_());
        }
    }

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? SoundEvents.PIGLIN_ADMIRING_ITEM : SoundEvents.PIGLIN_JEALOUS;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.PIGLIN_ADMIRING_ITEM;
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundEvents.PIGLIN_BRUTE_ANGRY);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.m_5496_(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.serializeMerchant(pCompound, this.offers, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.deserializeMerchant(pCompound, c -> this.offers = c);
    }
}