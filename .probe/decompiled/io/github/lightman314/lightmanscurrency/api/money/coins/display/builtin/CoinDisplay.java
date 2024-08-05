package io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayData;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplaySerializer;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValuePair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CoinDisplay extends ValueDisplayData {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "coin");

    public static final ValueDisplaySerializer SERIALIZER = new CoinDisplay.Serializer();

    private final List<CoinDisplay.ItemData> displayData;

    @Nonnull
    @Override
    public ValueDisplaySerializer getSerializer() {
        return SERIALIZER;
    }

    @Nonnull
    private CoinDisplay.ItemData getDataForCoin(@Nonnull CoinEntry entry) {
        for (CoinDisplay.ItemData data : this.displayData) {
            if (entry.matches(data.coin)) {
                return data;
            }
        }
        return new CoinDisplay.ItemData(entry.getCoin());
    }

    @Nullable
    private CoinDisplay.ItemData getDataForItem(@Nonnull ItemStack item) {
        for (CoinDisplay.ItemData data : this.displayData) {
            if (item.getItem() == data.coin) {
                return data;
            }
        }
        return null;
    }

    protected CoinDisplay(@Nonnull List<CoinDisplay.ItemData> displayData) {
        this.displayData = displayData;
    }

    @Nonnull
    @Override
    public MutableComponent formatValue(@Nonnull CoinValue value, @Nonnull MutableComponent emptyText) {
        if (value.getEntries().isEmpty()) {
            return emptyText;
        } else {
            MutableComponent result = EasyText.empty();
            for (CoinValuePair pair : value.getEntries()) {
                long amount = (long) pair.amount;
                CoinDisplay.ItemData data = this.getDataForCoin(this.getParent().findEntry(pair.coin));
                result.append(EasyText.literal(Long.toString(amount))).append(data.getInitial());
            }
            return result;
        }
    }

    @Override
    public void formatCoinTooltip(@Nonnull ItemStack stack, @Nonnull List<Component> tooltip) {
        ChainData parent = this.getParent();
        if (parent != null) {
            CoinDisplay.ItemData data = this.getDataForItem(stack);
            if (data != null) {
                Pair<CoinEntry, Integer> lowerExchange = parent.getLowerExchange(data.coin);
                if (lowerExchange != null) {
                    CoinDisplay.ItemData otherData = this.getDataForCoin((CoinEntry) lowerExchange.getFirst());
                    tooltip.add(LCText.TOOLTIP_COIN_WORTH_DOWN.get(lowerExchange.getSecond(), otherData.getPlural()).withStyle(ChatFormatting.YELLOW));
                }
                Pair<CoinEntry, Integer> upperExchange = parent.getUpperExchange(data.coin);
                if (upperExchange != null) {
                    tooltip.add(LCText.TOOLTIP_COIN_WORTH_UP.get(upperExchange.getSecond(), ((CoinEntry) upperExchange.getFirst()).getName()).withStyle(ChatFormatting.YELLOW));
                }
            }
        }
    }

    public static CoinDisplay easyDefine() {
        return easyDefine(coin -> {
            String type = "item.";
            if (coin instanceof BlockItem) {
                type = "block.";
            }
            ResourceLocation itemID = ForgeRegistries.ITEMS.getKey(coin);
            return Component.translatable(type + itemID.getNamespace() + "." + itemID.getPath() + ".initial");
        }, coin -> {
            String type = "item.";
            if (coin instanceof BlockItem) {
                type = "block.";
            }
            ResourceLocation itemID = ForgeRegistries.ITEMS.getKey(coin);
            return Component.translatable(type + itemID.getNamespace() + "." + itemID.getPath() + ".plural");
        });
    }

    public static CoinDisplay easyDefine(@Nonnull NonNullFunction<Item, Component> initialGenerator, @Nonnull NonNullFunction<Item, Component> pluralGenerator) {
        CoinDisplay.Builder builder = builder();
        for (CoinEntry entry : builder.possibleCoinEntries()) {
            Item coin = entry.getCoin();
            builder.defineFor(entry, initialGenerator.apply(coin), pluralGenerator.apply(coin));
        }
        return builder.build();
    }

    public static CoinDisplay.Builder builder() {
        return new CoinDisplay.Builder();
    }

    public static class Builder {

        private final ChainData.Builder parent = ChainData.Builder.getLatest();

        List<CoinDisplay.ItemData> displayData = new ArrayList();

        private Builder() {
        }

        protected List<CoinEntry> possibleCoinEntries() {
            List<CoinEntry> entries = new ArrayList();
            if (this.parent == null) {
                return entries;
            } else {
                entries.addAll(this.parent.getCoreChain().getEntries());
                for (ChainData.Builder.ChainBuilder sideChain : this.parent.getSideChains()) {
                    entries.addAll(sideChain.getEntries());
                }
                return entries;
            }
        }

        public CoinDisplay.Builder defineFor(@Nonnull RegistryObject<? extends ItemLike> coin, @Nonnull Component initial, @Nonnull Component plural) {
            return this.defineFor(coin.get(), initial, plural);
        }

        private void defineFor(@Nullable CoinEntry coin, @Nonnull Component initial, @Nonnull Component plural) {
            this.defineFor(coin.getCoin(), initial, plural);
        }

        public CoinDisplay.Builder defineFor(@Nullable ItemLike coin, @Nonnull Component initial, @Nonnull Component plural) {
            if (coin == null) {
                return this;
            } else {
                CoinDisplay.ItemData data = new CoinDisplay.ItemData(coin.asItem());
                data.initial = initial;
                data.plural = plural;
                this.displayData.add(data);
                return this;
            }
        }

        public CoinDisplay build() {
            return new CoinDisplay(this.displayData);
        }
    }

    public static class ItemData {

        private final Item coin;

        protected Component initial = null;

        protected Component plural = null;

        @Nonnull
        public Component getInitial() {
            return (Component) Objects.requireNonNullElseGet(this.initial, () -> {
                String name = new ItemStack(this.coin).getHoverName().getString();
                return !name.isEmpty() ? EasyText.literal(name.substring(0, 1).toLowerCase()) : EasyText.literal("X");
            });
        }

        @Nonnull
        public Component getPlural() {
            return (Component) Objects.requireNonNullElseGet(this.plural, () -> LCText.MISC_GENERIC_PLURAL.get(new ItemStack(this.coin).getHoverName()));
        }

        ItemData(@Nonnull Item coin) {
            this.coin = coin;
        }
    }

    protected static class Serializer extends ValueDisplaySerializer {

        private final List<CoinDisplay.ItemData> displayData = new ArrayList();

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return CoinDisplay.TYPE;
        }

        @Override
        public void resetBuilder() {
            this.displayData.clear();
        }

        @Override
        public void parseAdditional(@Nonnull JsonObject chainJson) {
        }

        @Override
        public void writeAdditional(@Nonnull ValueDisplayData data, @Nonnull JsonObject chainJson) {
        }

        @Override
        public void parseAdditionalFromCoin(@Nonnull CoinEntry coin, @Nonnull JsonObject coinEntry) {
            CoinDisplay.ItemData data = new CoinDisplay.ItemData(coin.getCoin());
            if (coinEntry.has("initial")) {
                data.initial = Component.Serializer.fromJson(coinEntry.get("initial"));
            }
            if (coinEntry.has("plural")) {
                data.plural = Component.Serializer.fromJson(coinEntry.get("plural"));
            }
            this.displayData.add(data);
        }

        @Override
        public void writeAdditionalToCoin(@Nonnull ValueDisplayData data, @Nonnull CoinEntry coin, @Nonnull JsonObject coinEntry) {
            if (data instanceof CoinDisplay display) {
                CoinDisplay.ItemData d = display.getDataForCoin(coin);
                if (d.initial != null) {
                    coinEntry.add("initial", Component.Serializer.toJsonTree(d.initial));
                }
                if (d.plural != null) {
                    coinEntry.add("plural", Component.Serializer.toJsonTree(d.plural));
                }
            }
        }

        @Nonnull
        public CoinDisplay build() {
            return new CoinDisplay(ImmutableList.copyOf(this.displayData));
        }
    }
}