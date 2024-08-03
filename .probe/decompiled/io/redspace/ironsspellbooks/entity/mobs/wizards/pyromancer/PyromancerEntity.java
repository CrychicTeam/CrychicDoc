package io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class PyromancerEntity extends NeutralWizard implements IMerchantWizard {

    @Nullable
    private Player tradingPlayer;

    @Nullable
    protected MerchantOffers offers;

    private long lastRestockGameTime;

    private int numberOfRestocksToday;

    private long lastRestockCheckDayTime;

    private static final List<VillagerTrades.ItemListing> fillerOffers = List.of(new AdditionalWanderingTrades.SimpleBuy(16, new ItemStack(Items.CANDLE, 1), 2, 2), new AdditionalWanderingTrades.SimpleSell(8, new ItemStack(Items.CANDLE, 4), 10, 14), new AdditionalWanderingTrades.SimpleSell(8, new ItemStack(Items.FIRE_CHARGE, 3), 9, 13), new AdditionalWanderingTrades.SimpleSell(12, new ItemStack(Items.LANTERN, 3), 6, 10), new AdditionalWanderingTrades.SimpleBuy(16, new ItemStack(Items.HONEY_BOTTLE, 1), 3, 5), new AdditionalWanderingTrades.SimpleBuy(16, new ItemStack(Items.BLAZE_ROD, 1), 4, 6), new AdditionalWanderingTrades.SimpleSell(5, createFireworkStack(), 3, 4));

    public PyromancerEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 25;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new WizardAttackGoal(this, 1.25, 25, 50).setSpells(List.of(SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIRE_BREATH_SPELL.get(), SpellRegistry.BLAZE_STORM_SPELL.get()), List.of(), List.of(SpellRegistry.BURNING_DASH_SPELL.get()), List.of()).setDrinksPotions().setSingleUseSpell(SpellRegistry.MAGMA_BOMB_SPELL.get(), 80, 200, 4, 6));
        this.f_21345_.addGoal(3, new PatrolNearLocationGoal(this, 30.0F, 0.75));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(10, new WizardRecoverGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isHostileTowards));
        this.f_21346_.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.m_6518_(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.m_8061_(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.PYROMANCER_HELMET.get()));
        this.m_8061_(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.PYROMANCER_CHESTPLATE.get()));
        this.m_21409_(EquipmentSlot.HEAD, 0.0F);
        this.m_21409_(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundRegistry.TRADER_NO.get());
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
            this.offers.addAll(this.createRandomOffers(2, 3));
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_COMMON.get()).m_213663_(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()).m_213663_(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.25F) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_RARE.get()).m_213663_(this, this.f_19796_));
            }
            this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.0F, 0.25F).getOffer(this, this.f_19796_));
            if (this.f_19796_.nextFloat() < 0.8F) {
                this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.3F, 0.7F).getOffer(this, this.f_19796_));
            }
            if (this.f_19796_.nextFloat() < 0.8F) {
                this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.8F, 1.0F).getOffer(this, this.f_19796_));
            }
            this.offers.add(new AdditionalWanderingTrades.SimpleSell(3, new ItemStack(ItemRegistry.FIRE_ALE.get()), 12, 16).m_213663_(this, this.f_19796_));
            this.offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 24), ItemStack.EMPTY, FurledMapItem.of(IronsSpellbooks.id("mangrove_hut"), Component.translatable("item.irons_spellbooks.alchemical_trade_route")), 0, 1, 5, 10.0F));
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
            offers.add(((VillagerTrades.ItemListing) fillerOffers.get(integer)).getOffer(this, this.f_19796_));
        }
        return offers;
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

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? SoundRegistry.TRADER_YES.get() : SoundRegistry.TRADER_NO.get();
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundRegistry.TRADER_YES.get();
    }

    private static ItemStack createFireworkStack() {
        CompoundTag properties = new CompoundTag();
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET, 5);
        ListTag explosions = new ListTag();
        CompoundTag explosion = new CompoundTag();
        explosion.putByte("Type", (byte) 4);
        explosion.putByte("Trail", (byte) 1);
        explosion.putByte("Flicker", (byte) 1);
        explosion.putIntArray("Colors", new int[] { 11743535, 15435844, 14602026 });
        explosions.add(explosion);
        properties.put("Explosions", explosions);
        properties.putByte("Flight", (byte) 3);
        rocket.addTagElement("Fireworks", properties);
        return rocket;
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