package com.mna.entities.faction;

import com.google.common.collect.ImmutableList;
import com.mna.Registries;
import com.mna.api.events.BrokerSelectingTradesEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.IRelic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemThaumaturgicLink;
import com.mna.items.sorcery.ItemTornJournalPage;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.spells.components.ComponentFling;
import com.mna.tools.BiomeUtils;
import com.mna.tools.StructureUtils;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Broker extends WanderingTrader implements GeoEntity {

    private static List<Item> allFactionItems;

    private MerchantOffers customerLimitedOffers;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public static final int NUM_FRAGMENTS = 5;

    private static final EntityDataAccessor<Boolean> DESPAWNING = SynchedEntityData.defineId(Broker.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DESPAWNING_HOSTILE = SynchedEntityData.defineId(Broker.class, EntityDataSerializers.BOOLEAN);

    public Broker(EntityType<? extends WanderingTrader> type, Level worldIn) {
        super(type, worldIn);
    }

    public Broker(Level world) {
        this(EntityInit.BROKER.get(), world);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new TradeWithPlayerGoal(this));
        this.f_21345_.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SFX.Entity.Broker.DEAL;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean getYesSound) {
        return getYesSound ? SFX.Entity.Broker.DEAL : null;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DESPAWNING, false);
        this.f_19804_.define(DESPAWNING_HOSTILE, false);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.f_19804_.get(DESPAWNING) && this.m_35876_() == 1) {
            this.m_35891_(10);
            this.f_19804_.set(DESPAWNING, true);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Spell.Impact.AoE.ARCANE, SoundSource.NEUTRAL, 1.0F, 1.0F, true);
            for (int i = 0; i < 200; i++) {
                int type = (int) Math.floor(Math.random() * 4.0);
                if (type != 2) {
                    this.m_9236_().addParticle(new MAParticleType(type == 0 ? ParticleInit.ARCANE.get() : (type == 2 ? ParticleInit.DUST.get() : ParticleInit.FLAME.get())), this.m_20185_() + Math.random() * 2.0 - 1.0, this.m_20186_() + Math.random() * 2.5, this.m_20189_() + Math.random() * 2.0 - 1.0, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1);
                } else {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.EARTH.get()), this.m_20185_() + Math.random() * 2.0 - 1.0, this.m_20186_() + Math.random() * 2.5, this.m_20189_() + Math.random() * 2.0 - 1.0, 0.18, 0.3, 0.03);
                }
            }
            if (this.f_19804_.get(DESPAWNING_HOSTILE)) {
                int count = 180;
                double angleRads = 0.0;
                double step = (Math.PI * 2) / (double) count;
                double radius = 6.0;
                for (int ix = 0; ix < count; ix++) {
                    angleRads += step;
                    Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize();
                    Vec3 vel = dir.scale(0.3F * radius);
                    dir = dir.scale(0.2);
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), this.m_20185_() + dir.x, this.m_20186_() + Math.random(), this.m_20189_() + dir.z, vel.x, vel.y, vel.z);
                }
            }
        } else if (this.f_19804_.get(DESPAWNING_HOSTILE)) {
            Vec3 bEyepos = this.m_20182_();
            this.m_9236_().getEntities(this, this.m_20191_().inflate(6.0), e -> e instanceof LivingEntity && e.isAlive() && e.isAddedToWorld()).stream().map(e -> (LivingEntity) e).forEach(e -> {
                Vec3 direction = e.m_146892_().subtract(bEyepos);
                ComponentFling.flingTarget(e, direction, 6.0F, 3.0F, 0.0F);
            });
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return super.m_6469_(source, amount);
        } else {
            if (!this.f_19804_.get(DESPAWNING)) {
                this.m_35891_(10);
                this.f_19804_.set(DESPAWNING, true);
                this.f_19804_.set(DESPAWNING_HOSTILE, true);
            }
            return false;
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        this.f_35261_.stream().filter(o -> o.satisfiedBy(offer.getCostA(), offer.getCostB())).findFirst().ifPresent(o -> o.increaseUses());
        this.m_5496_(this.getTradeUpdatedSound(true), this.m_6121_(), this.m_6100_());
        super.m_6996_(offer);
    }

    @Override
    protected void addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        if (!this.m_9236_().isClientSide()) {
            givenMerchantOffers.clear();
            if (allFactionItems == null) {
                allFactionItems = (List<Item>) ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof IFactionSpecific).collect(Collectors.toList());
            }
            Random rand = new Random();
            ServerLevel serverLevel = (ServerLevel) this.m_9236_();
            for (Item factionItem : allFactionItems) {
                IFaction thisFaction = ((IFactionSpecific) factionItem).getFaction();
                if (thisFaction != null) {
                    ItemStack output = new ItemStack(factionItem);
                    List<Item> opposingFactionItems = (List<Item>) allFactionItems.stream().filter(i -> ((IFactionSpecific) i).getFaction() != null && ((IFactionSpecific) i).getFaction() != thisFaction).collect(Collectors.toList());
                    ItemStack opposingItemPrice = new ItemStack((ItemLike) opposingFactionItems.get(rand.nextInt(opposingFactionItems.size())));
                    ItemStack tokenPrice = this.getRandomOpposingToken(thisFaction);
                    if (!tokenPrice.isEmpty() && !opposingItemPrice.isEmpty()) {
                        tokenPrice.setCount(32 + rand.nextInt(32));
                        MerchantOffer offer = new MerchantOffer(opposingItemPrice, tokenPrice, output, 1, 0, 1.0F);
                        givenMerchantOffers.add(offer);
                    }
                }
            }
            List<IFaction> all_factions = ((IForgeRegistry) Registries.Factions.get()).getValues().stream().toList();
            for (int i = 0; i < 5; i++) {
                IFaction selected = (IFaction) all_factions.get(rand.nextInt(all_factions.size()));
                ItemStack output = ItemTornJournalPage.getRandomPage(rand, selected);
                if (!output.isEmpty()) {
                    ItemStack fragmentPrice = new ItemStack(ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get());
                    MerchantOffer offer = new MerchantOffer(fragmentPrice, ItemStack.EMPTY, output, 1, 1, 1.0F);
                    givenMerchantOffers.add(offer);
                }
            }
            ItemStack costItem = new ItemStack(ItemInit.SPELL_PART_THESIS.get());
            ItemStack costItemB = new ItemStack(ItemInit.SPELL_PART_THESIS.get());
            StructureUtils.getAllStructures(serverLevel).forEach(structureFeature -> {
                if (structureFeature.is(MATags.Structures.BROKER_STRUCTURES)) {
                    ItemStack stack = new ItemStack(ItemInit.THAUMATURGIC_LINK.get());
                    ItemThaumaturgicLink.setStructureKey(serverLevel, stack, structureFeature);
                    MerchantOffer offerx = new MerchantOffer(costItem.copy(), costItemB.copy(), stack, 1, 0, 1.0F);
                    givenMerchantOffers.add(offerx);
                }
            });
            BiomeUtils.getAllBiomes(serverLevel).forEach(biome -> {
                if (biome.is(MATags.Biomes.BROKER_BIOMES)) {
                    ItemStack stack = new ItemStack(ItemInit.THAUMATURGIC_LINK.get());
                    ItemThaumaturgicLink.setBiomeKey(serverLevel, stack, biome);
                    MerchantOffer offerx = new MerchantOffer(costItem.copy(), costItemB.copy(), stack, 1, 0, 1.0F);
                    givenMerchantOffers.add(offerx);
                }
            });
            MinecraftForge.EVENT_BUS.post(new BrokerSelectingTradesEvent(this, givenMerchantOffers, newTrades, maxNumbers, ImmutableList.copyOf(allFactionItems)));
        }
    }

    private ItemStack getRandomOpposingToken(IFaction faction) {
        List<IFaction> enemies = faction.getEnemyFactions();
        if (enemies.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            IFaction selected = (IFaction) enemies.get((int) (Math.random() * (double) enemies.size()));
            return selected.getTokenItem() == null ? ItemStack.EMPTY : new ItemStack(selected.getTokenItem());
        }
    }

    @Override
    protected void updateTrades() {
        MerchantOffers merchantoffers = this.getOffers();
        this.addOffersFromItemListings(merchantoffers, null, 5);
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.f_35261_ == null) {
            this.f_35261_ = new MerchantOffers();
            this.updateTrades();
        }
        return this.customerLimitedOffers != null ? this.customerLimitedOffers : this.f_35261_;
    }

    @Override
    public void setTradingPlayer(Player player) {
        super.m_7189_(player);
        if (player != null) {
            this.getOffers();
            this.customerLimitedOffers = new MerchantOffers(this.f_35261_.createTag());
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                for (int i = 0; i < this.customerLimitedOffers.size(); i++) {
                    MerchantOffer offer = (MerchantOffer) this.customerLimitedOffers.get(i);
                    Item output = offer.assemble().getItem();
                    if (output != ItemInit.TORN_JOURNAL_PAGE.get() && !(output instanceof IRelic)) {
                        int requiredTier = 0;
                        Optional<ManaweavingRecipe> recipe = this.m_9236_().getRecipeManager().<CraftingContainer, ManaweavingRecipe>getAllRecipesFor(RecipeInit.MANAWEAVING_RECIPE_TYPE.get()).stream().filter(r -> r.getResultItem().getItem() == output).findFirst();
                        if (recipe.isPresent()) {
                            requiredTier = ((ManaweavingRecipe) recipe.get()).getTier();
                        } else {
                            Optional<EldrinAltarRecipe> altar_recipe = this.m_9236_().getRecipeManager().<CraftingContainer, EldrinAltarRecipe>getAllRecipesFor(RecipeInit.ELDRIN_ALTAR_TYPE.get()).stream().filter(r -> r.getResultItem().getItem() == output).findFirst();
                            if (altar_recipe.isPresent()) {
                                requiredTier = ((EldrinAltarRecipe) altar_recipe.get()).getTier();
                            }
                        }
                        if (requiredTier > p.getTier()) {
                            offer.setToOutOfStock();
                        }
                    }
                }
            });
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> !this.f_19804_.get(DESPAWNING) ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.idle")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.slam"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}