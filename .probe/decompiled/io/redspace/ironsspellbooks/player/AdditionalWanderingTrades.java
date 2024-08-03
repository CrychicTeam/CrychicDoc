package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class AdditionalWanderingTrades {

    public static final int INK_SALE_PRICE_PER_RARITY = 8;

    public static final int INK_BUY_PRICE_PER_RARITY = 5;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addWanderingTrades(WandererTradesEvent event) {
        if (ServerConfigs.ADDITIONAL_WANDERING_TRADER_TRADES.get()) {
            List<VillagerTrades.ItemListing> additionalGenericTrades = List.of(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter()), new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter()), new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter()), new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_COMMON.get()), new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()), new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_RARE.get()), new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_EPIC.get()), new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_LEGENDARY.get()), new AdditionalWanderingTrades.InkSellTrade((InkItem) ItemRegistry.INK_COMMON.get()), new AdditionalWanderingTrades.InkSellTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()), new AdditionalWanderingTrades.InkSellTrade((InkItem) ItemRegistry.INK_RARE.get()), new AdditionalWanderingTrades.InkSellTrade((InkItem) ItemRegistry.INK_EPIC.get()), new AdditionalWanderingTrades.InkSellTrade((InkItem) ItemRegistry.INK_LEGENDARY.get()), new AdditionalWanderingTrades.RandomCurioTrade());
            List<VillagerTrades.ItemListing> additionalRareTrades = List.of(AdditionalWanderingTrades.SimpleTrade.of((trader, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 64 - random.nextIntBetweenInclusive(1, 8)), new ItemStack(Items.ECHO_SHARD, random.nextIntBetweenInclusive(1, 3)), new ItemStack(ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.get()), 8, 0, 0.05F)), AdditionalWanderingTrades.SimpleTrade.of((trader, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 64), new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(48, 64)), new ItemStack(ItemRegistry.HITHER_THITHER_WAND.get()), 1, 0, 0.05F)), new AdditionalWanderingTrades.RandomCurioTrade(), new AdditionalWanderingTrades.RandomCurioTrade(), new AdditionalWanderingTrades.RandomCurioTrade(), new AdditionalWanderingTrades.ScrollPouchTrade(), new AdditionalWanderingTrades.ScrollPouchTrade());
            event.getGenericTrades().addAll(additionalGenericTrades.stream().filter(Objects::nonNull).toList());
            event.getRareTrades().addAll(additionalRareTrades.stream().filter(Objects::nonNull).toList());
        }
    }

    public static class ExilirBuyTrade extends AdditionalWanderingTrades.SimpleTrade {

        public ExilirBuyTrade(boolean onlyLesser, boolean onlyGreater) {
            super((trader, random) -> {
                List<Item> lesser = List.of(ItemRegistry.EVASION_ELIXIR.get(), ItemRegistry.OAKSKIN_ELIXIR.get(), ItemRegistry.INVISIBILITY_ELIXIR.get());
                List<Item> greater = List.of(ItemRegistry.GREATER_EVASION_ELIXIR.get(), ItemRegistry.GREATER_OAKSKIN_ELIXIR.get(), ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get(), ItemRegistry.GREATER_HEALING_POTION.get());
                boolean isGreater;
                if (onlyLesser) {
                    isGreater = false;
                } else if (onlyGreater) {
                    isGreater = true;
                } else {
                    isGreater = random.nextBoolean();
                }
                Item item = isGreater ? (Item) greater.get(random.nextInt(greater.size())) : (Item) lesser.get(random.nextInt(lesser.size()));
                return new MerchantOffer(new ItemStack(item), new ItemStack(Items.EMERALD, 6 + random.nextIntBetweenInclusive(3, 6) * (isGreater ? 2 : 1)), 6, 1, 0.05F);
            });
        }
    }

    public static class ExilirSellTrade extends AdditionalWanderingTrades.SimpleTrade {

        public ExilirSellTrade(boolean onlyLesser, boolean onlyGreater) {
            super((trader, random) -> {
                List<Item> lesser = List.of(ItemRegistry.EVASION_ELIXIR.get(), ItemRegistry.OAKSKIN_ELIXIR.get(), ItemRegistry.INVISIBILITY_ELIXIR.get());
                List<Item> greater = List.of(ItemRegistry.GREATER_EVASION_ELIXIR.get(), ItemRegistry.GREATER_OAKSKIN_ELIXIR.get(), ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get(), ItemRegistry.GREATER_HEALING_POTION.get());
                boolean isGreater;
                if (onlyLesser) {
                    isGreater = false;
                } else if (onlyGreater) {
                    isGreater = true;
                } else {
                    isGreater = random.nextBoolean();
                }
                Item item = isGreater ? (Item) greater.get(random.nextInt(greater.size())) : (Item) lesser.get(random.nextInt(lesser.size()));
                return new MerchantOffer(new ItemStack(Items.EMERALD, 10 + random.nextIntBetweenInclusive(4, 8) * (isGreater ? 2 : 1)), new ItemStack(item), 3, 1, 0.05F);
            });
        }
    }

    public static class InkBuyTrade extends AdditionalWanderingTrades.SimpleTrade {

        public InkBuyTrade(InkItem item) {
            super((trader, random) -> {
                boolean emeralds = random.nextBoolean();
                return new MerchantOffer(new ItemStack(item), new ItemStack((ItemLike) (emeralds ? Items.EMERALD : ItemRegistry.ARCANE_ESSENCE.get()), 5 * item.getRarity().getValue() / (emeralds ? 1 : 2) + random.nextIntBetweenInclusive(2, 3)), 8, 1, 0.05F);
            });
        }
    }

    public static class InkSellTrade extends AdditionalWanderingTrades.SimpleTrade {

        public InkSellTrade(InkItem item) {
            super((trader, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 8 * item.getRarity().getValue() + random.nextIntBetweenInclusive(2, 3)), new ItemStack(item), 4, 1, 0.05F));
        }
    }

    public static class PotionSellTrade extends AdditionalWanderingTrades.SimpleTrade {

        public PotionSellTrade(@Nullable Potion potion) {
            super((trader, random) -> {
                Potion potion1 = potion;
                if (potion == null) {
                    List<Potion> potions = ForgeRegistries.POTIONS.getValues().stream().filter(p -> p.getEffects().size() > 0).toList();
                    potion1 = (Potion) potions.get(random.nextInt(potions.size()));
                }
                if (potion1 == null) {
                    potion1 = Potions.AWKWARD;
                }
                int amplifier = 0;
                int duration = 0;
                List<MobEffectInstance> effects = potion1.getEffects();
                if (effects.size() > 0) {
                    MobEffectInstance effect = (MobEffectInstance) effects.get(0);
                    amplifier = effect.getAmplifier();
                    duration = effect.getDuration() / 1200;
                }
                return new MerchantOffer(new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(12, 16) + random.nextIntBetweenInclusive(4, 6) * amplifier + duration), PotionUtils.setPotion(new ItemStack(Items.POTION), potion1), 3, 1, 0.05F);
            });
        }
    }

    static class RandomCurioTrade extends AdditionalWanderingTrades.SimpleTrade {

        private RandomCurioTrade() {
            super((trader, random) -> {
                if (!trader.level.isClientSide) {
                    LootTable loottable = trader.level.getServer().getLootData().m_278676_(IronsSpellbooks.id("magic_items/basic_curios"));
                    LootParams context = new LootParams.Builder((ServerLevel) trader.level).create(LootContextParamSets.EMPTY);
                    ObjectArrayList<ItemStack> items = loottable.getRandomItems(context);
                    if (!items.isEmpty()) {
                        ItemStack forSale = (ItemStack) items.get(0);
                        ItemStack cost = new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(14, 25));
                        return new MerchantOffer(cost, forSale, 1, 5, 0.5F);
                    }
                }
                return null;
            });
        }
    }

    public static class RandomScrollTrade implements VillagerTrades.ItemListing {

        protected final ItemStack price;

        protected final ItemStack price2;

        protected final ItemStack forSale;

        protected final int maxTrades;

        protected final int xp;

        protected final float priceMult;

        protected final SpellFilter spellFilter;

        protected float minQuality;

        protected float maxQuality;

        public RandomScrollTrade(SpellFilter spellFilter) {
            this.spellFilter = spellFilter;
            this.price = new ItemStack(Items.EMERALD);
            this.price2 = ItemStack.EMPTY;
            this.forSale = new ItemStack(ItemRegistry.SCROLL.get());
            this.maxTrades = 1;
            this.xp = 5;
            this.priceMult = 0.05F;
            this.minQuality = 0.0F;
            this.maxQuality = 1.0F;
        }

        public RandomScrollTrade(SpellFilter filter, float minQuality, float maxQuality) {
            this(filter);
            this.minQuality = minQuality;
            this.maxQuality = maxQuality;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity pTrader, RandomSource random) {
            AbstractSpell spell = this.spellFilter.getRandomSpell(random);
            if (spell == SpellRegistry.none()) {
                return null;
            } else {
                int level = random.nextIntBetweenInclusive(1 + (int) ((float) spell.getMaxLevel() * this.minQuality), (int) ((float) (spell.getMaxLevel() - 1) * this.maxQuality) + 1);
                ISpellContainer.createScrollContainer(spell, level, this.forSale);
                this.price.setCount(spell.getRarity(level).getValue() * 5 + random.nextIntBetweenInclusive(4, 7) + level);
                return new MerchantOffer(this.price, this.price2, this.forSale, this.maxTrades, this.xp, this.priceMult);
            }
        }
    }

    static class ScrollMerchantOffer extends MerchantOffer {

        final SpellRarity scrollRarity;

        public ScrollMerchantOffer(SpellRarity scrollRarity, int emeralds, int pMaxUses, int pXp, float pPriceMultiplier) {
            super(new ItemStack(ItemRegistry.SCROLL.get()).setHoverName(Component.translatable("ui.irons_spellbooks.wandering_trader_scroll", scrollRarity.getDisplayName())), new ItemStack(Items.EMERALD, emeralds), pMaxUses, pXp, pPriceMultiplier);
            this.scrollRarity = scrollRarity;
        }

        @Override
        public boolean satisfiedBy(ItemStack offerA, ItemStack offerB) {
            SpellRarity offerARarity = ISpellContainer.get(offerA).getSpellAtIndex(0).getRarity();
            return offerA.is(ItemRegistry.SCROLL.get()) && offerARarity == this.scrollRarity && offerA.getCount() >= this.m_45358_().getCount() && this.isRequiredItem(offerB, this.m_45364_()) && offerB.getCount() >= this.m_45364_().getCount();
        }

        private boolean isRequiredItem(ItemStack pOffer, ItemStack pCost) {
            if (pCost.isEmpty() && pOffer.isEmpty()) {
                return true;
            } else {
                ItemStack itemstack = pOffer.copy();
                if (itemstack.getItem().isDamageable(itemstack)) {
                    itemstack.setDamageValue(itemstack.getDamageValue());
                }
                return ItemStack.isSameItem(itemstack, pCost) && (!pCost.hasTag() || itemstack.hasTag() && NbtUtils.compareNbt(pCost.getTag(), itemstack.getTag(), false));
            }
        }

        static VillagerTrades.ItemListing createListing(final SpellRarity scrollRarity, final int emeralds, final int pMaxUses, final int pXp, final float pPriceMultiplier) {
            return new VillagerTrades.ItemListing() {

                @Nullable
                @Override
                public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
                    return new AdditionalWanderingTrades.ScrollMerchantOffer(scrollRarity, emeralds, pMaxUses, pXp, pPriceMultiplier);
                }
            };
        }
    }

    static class ScrollPouchTrade extends AdditionalWanderingTrades.SimpleTrade {

        private ScrollPouchTrade() {
            super((trader, random) -> {
                if (!trader.level.isClientSide) {
                    LootTable loottable = trader.level.getServer().getLootData().m_278676_(IronsSpellbooks.id("magic_items/scroll_pouch"));
                    LootParams context = new LootParams.Builder((ServerLevel) trader.level).create(LootContextParamSets.EMPTY);
                    ObjectArrayList<ItemStack> items = loottable.getRandomItems(context);
                    if (!items.isEmpty()) {
                        int quality = 0;
                        ItemStack forSale = new ItemStack(Items.BUNDLE).setHoverName(Component.translatable("item.irons_spellbooks.scroll_pouch"));
                        ListTag itemsTag = new ListTag();
                        ObjectListIterator cost = items.iterator();
                        while (cost.hasNext()) {
                            ItemStack scroll = (ItemStack) cost.next();
                            itemsTag.add(scroll.save(new CompoundTag()));
                            if (scroll.getItem() instanceof Scroll) {
                                quality += ISpellContainer.get(scroll).getSpellAtIndex(0).getRarity().getValue() + 1;
                            }
                        }
                        forSale.getOrCreateTag().put("Items", itemsTag);
                        ItemStack costx = new ItemStack(Items.EMERALD, quality * 4 + random.nextIntBetweenInclusive(8, 16));
                        return new MerchantOffer(costx, forSale, 1, 5, 0.5F);
                    }
                }
                return null;
            });
        }
    }

    public static class SimpleBuy extends AdditionalWanderingTrades.SimpleTrade {

        public SimpleBuy(int tradeCount, ItemStack buy, int minEmeralds, int maxEmeralds) {
            super((trader, random) -> new MerchantOffer(buy, new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(minEmeralds, maxEmeralds)), tradeCount, 0, 0.05F));
        }
    }

    public static class SimpleSell extends AdditionalWanderingTrades.SimpleTrade {

        public SimpleSell(int tradeCount, ItemStack sell, int minEmeralds, int maxEmeralds) {
            super((trader, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(minEmeralds, maxEmeralds)), sell, tradeCount, 0, 0.05F));
        }
    }

    public static class SimpleTrade implements VillagerTrades.ItemListing {

        final BiFunction<Entity, RandomSource, MerchantOffer> getOffer;

        protected SimpleTrade(BiFunction<Entity, RandomSource, MerchantOffer> getOffer) {
            this.getOffer = getOffer;
        }

        public static AdditionalWanderingTrades.SimpleTrade of(BiFunction<Entity, RandomSource, MerchantOffer> getOffer) {
            return new AdditionalWanderingTrades.SimpleTrade(getOffer);
        }

        @Override
        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            return (MerchantOffer) this.getOffer.apply(pTrader, pRandom);
        }
    }
}