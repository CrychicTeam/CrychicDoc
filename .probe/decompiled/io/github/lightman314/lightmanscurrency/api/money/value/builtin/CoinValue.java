package io.github.lightman314.lightmanscurrency.api.money.value.builtin;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.types.builtin.CoinCurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.display.CoinPriceEntry;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public final class CoinValue extends MoneyValue {

    public final ImmutableList<CoinValuePair> coinValues;

    private final String chain;

    private CompoundTag backup = null;

    @Nonnull
    public String getChain() {
        return this.chain;
    }

    @Override
    public boolean isEmpty() {
        return this.coinValues.isEmpty();
    }

    @Override
    public boolean isInvalid() {
        return this.backup != null;
    }

    public boolean isValid() {
        return this.isFree() || !this.coinValues.isEmpty();
    }

    @Nonnull
    @Override
    protected String generateUniqueName() {
        return this.generateCustomUniqueName(this.chain);
    }

    @Nonnull
    @Override
    protected ResourceLocation getType() {
        return CoinCurrencyType.TYPE;
    }

    private CoinValue(@Nonnull String chain, @Nonnull CompoundTag backup) {
        this.chain = chain;
        this.coinValues = ImmutableList.of();
        this.backup = backup;
    }

    private CoinValue(@Nonnull String chain, @Nonnull List<CoinValuePair> values) {
        this.chain = chain;
        this.coinValues = ImmutableList.copyOf(roundValue(this.chain, values));
    }

    public static MoneyValue create(@Nonnull String chain, @Nonnull List<CoinValuePair> coinValues) {
        return (MoneyValue) (coinValues.isEmpty() ? MoneyValue.empty() : new CoinValue(chain, coinValues));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag tag) {
        if (this.backup != null) {
            tag.merge(this.backup);
        } else {
            tag.putString("Chain", this.chain);
            ListTag valueList = new ListTag();
            UnmodifiableIterator var3 = this.coinValues.iterator();
            while (var3.hasNext()) {
                CoinValuePair pair = (CoinValuePair) var3.next();
                valueList.add(pair.save());
            }
            tag.put("Value", valueList);
        }
    }

    @Nonnull
    public static MoneyValue loadCoinValue(@Nonnull CompoundTag tag) {
        if (tag.contains("Chain", 8)) {
            boolean dataLoaded = true;
            if (CoinAPI.API.NoDataAvailable()) {
                LightmansCurrency.LogWarning("Coin Value loaded before receiving the Chain Data packet. Will assume all value pairs are valid and don't need rounding!");
                dataLoaded = false;
            }
            String chain = tag.getString("Chain");
            ChainData chainData = CoinAPI.API.ChainData(chain);
            if (chainData == null && dataLoaded) {
                return new CoinValue(chain, tag);
            }
            if (tag.contains("Value", 9)) {
                ListTag valueList = tag.getList("Value", 10);
                List<CoinValuePair> pairList = new ArrayList();
                for (int i = 0; i < valueList.size(); i++) {
                    try {
                        pairList.add(CoinValuePair.load(chainData, valueList.getCompound(i)));
                    } catch (RuntimeException var8) {
                    }
                }
                return create(chain, pairList);
            }
        }
        return MoneyValue.empty();
    }

    @Nullable
    public static MoneyValue loadDeprecated(@Nonnull CompoundTag tag) {
        if (tag.contains("Free", 1)) {
            return MoneyValue.free();
        } else {
            if (tag.contains("Value", 9)) {
                ListTag valueList = tag.getList("Value", 10);
                if (valueList.isEmpty()) {
                    return MoneyValue.empty();
                }
                List<CoinValuePair> pairList = new ArrayList();
                ChainData chainData = null;
                for (int i = 0; i < valueList.size(); i++) {
                    CompoundTag entry = valueList.getCompound(i);
                    Item coin = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getString("Coin")));
                    int amount = entry.getInt("Amount");
                    if (chainData == null) {
                        chainData = CoinAPI.API.ChainDataOfCoin(coin);
                    }
                    if (chainData != null && chainData.containsEntry(coin)) {
                        pairList.add(new CoinValuePair(coin, amount));
                    }
                }
                if (chainData != null) {
                    return create(chainData.chain, pairList);
                }
            }
            return MoneyValue.empty();
        }
    }

    @Nullable
    public static MoneyValue loadDeprecated(@Nonnull CompoundTag parentTag, @Nonnull String key) {
        if (parentTag.contains(key, 3)) {
            return fromNumber("main", (long) parentTag.getInt(key));
        } else {
            if (parentTag.contains(key, 9)) {
                ListTag listNBT = parentTag.getList(key, 10);
                if (listNBT.isEmpty()) {
                    return MoneyValue.empty();
                }
                List<CoinValuePair> pairList = new ArrayList();
                ChainData chainData = null;
                for (int i = 0; i < listNBT.size(); i++) {
                    CompoundTag thisCompound = listNBT.getCompound(i);
                    Item coin = ForgeRegistries.ITEMS.getValue(new ResourceLocation(thisCompound.getString("id")));
                    int amount = thisCompound.getInt("amount");
                    if (chainData == null) {
                        chainData = CoinAPI.API.ChainDataOfCoin(coin);
                    }
                    if (chainData != null && chainData.containsEntry(coin)) {
                        pairList.add(new CoinValuePair(coin, amount));
                    }
                }
                if (chainData != null) {
                    return create(chainData.chain, pairList);
                }
            } else if (parentTag.contains(key, 1) && parentTag.getBoolean(key)) {
                return MoneyValue.free();
            }
            return MoneyValue.empty();
        }
    }

    public static MoneyValue fromNumber(@Nonnull String chain, long valueNumber) {
        return fromNumber(CoinAPI.API.ChainData(chain), valueNumber);
    }

    public static MoneyValue fromNumber(ChainData chainData, long valueNumber) {
        if (valueNumber <= 0L) {
            return MoneyValue.empty();
        } else if (chainData == null) {
            return MoneyValue.empty();
        } else {
            long pendingValue = valueNumber;
            List<CoinEntry> entries = chainData.getAllEntries(false, ChainData.SORT_HIGHEST_VALUE_FIRST);
            List<CoinValuePair> pairList = new ArrayList();
            for (CoinEntry entry : entries) {
                long entryValue = entry.getCoreValue();
                if (pendingValue >= entryValue && entryValue != 0L) {
                    long thisCount = pendingValue / entryValue;
                    pendingValue %= entryValue;
                    if (thisCount > 0L) {
                        if (thisCount >= 2147483647L) {
                            LightmansCurrency.LogWarning("Value count of " + new ItemStack(entry.getCoin()).getHoverName().getString() + " is greater than the maximum integer value!");
                        }
                        pairList.add(new CoinValuePair(entry.getCoin(), (int) thisCount));
                    }
                    if (pendingValue <= 0L) {
                        break;
                    }
                }
            }
            return create(chainData.chain, pairList);
        }
    }

    @Nonnull
    public static MoneyValue fromItemOrValue(Item coin, long value) {
        return fromItemOrValue(coin, 1, value);
    }

    @Nonnull
    public static MoneyValue fromItemOrValue(Item coin, int itemCount, long value) {
        ChainData chainData = CoinAPI.API.ChainDataOfCoin(coin);
        return (MoneyValue) (chainData != null ? new CoinValue(chainData.chain, Lists.newArrayList(new CoinValuePair[] { new CoinValuePair(coin, itemCount) })) : fromNumber("main", value));
    }

    @Override
    public MoneyValue addValue(@Nonnull MoneyValue addedValue) {
        return this.sameType(addedValue) ? fromNumber(this.chain, this.getCoreValue() + addedValue.getCoreValue()) : null;
    }

    @Override
    public boolean containsValue(@Nonnull MoneyValue queryValue) {
        return this.sameType(queryValue) ? this.getCoreValue() >= queryValue.getCoreValue() : false;
    }

    @Override
    public MoneyValue subtractValue(@Nonnull MoneyValue removedValue) {
        return this.sameType(removedValue) && this.containsValue(removedValue) ? fromNumber(this.chain, this.getCoreValue() - removedValue.getCoreValue()) : null;
    }

    @Nonnull
    @Override
    public MoneyValue multiplyValue(double multiplier) {
        BigDecimal value = BigDecimal.valueOf(this.getCoreValue());
        BigDecimal result = value.multiply(BigDecimal.valueOf(multiplier));
        if (result.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            return MoneyValue.empty();
        } else if (result.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0) {
            return fromNumber(this.chain, Long.MAX_VALUE);
        } else {
            long rounding = 0L;
            if (result.remainder(BigDecimal.ONE).compareTo(BigDecimal.valueOf(0.5)) >= 0) {
                rounding = 1L;
            }
            return fromNumber(this.chain, result.longValue() + rounding);
        }
    }

    @Override
    public MoneyValue percentageOfValue(int percentage, boolean roundUp) {
        if (percentage == 100) {
            return this;
        } else if (percentage == 0) {
            return MoneyValue.free();
        } else {
            long value = this.getCoreValue();
            long newValue = value * (long) MathUtil.clamp(percentage, 0, 1000) / 100L;
            if (roundUp) {
                long partial = value * (long) MathUtil.clamp(percentage, 0, 1000) % 100L;
                if (partial > 0L) {
                    newValue++;
                }
            }
            return newValue == 0L ? MoneyValue.free() : fromNumber(this.chain, newValue);
        }
    }

    @Nonnull
    @Override
    public MoneyValue getSmallestValue() {
        return fromNumber(this.chain, 1L);
    }

    @Nonnull
    @Override
    public List<ItemStack> onBlockBroken(@Nonnull Level level, @Nonnull OwnerData owner) {
        return this.getAsSeperatedItemList();
    }

    private static List<CoinValuePair> roundValue(@Nonnull String chain, @Nonnull List<CoinValuePair> list) {
        ChainData chainData = CoinAPI.API.ChainData(chain);
        if (chainData == null) {
            return list;
        } else {
            while (needsRounding(chainData, list)) {
                for (int i = 0; i < list.size(); i++) {
                    if (needsRounding(chainData, list, i)) {
                        CoinValuePair pair = (CoinValuePair) list.get(i);
                        CoinEntry entry = chainData.findEntry(pair.coin);
                        Pair<CoinEntry, Integer> exchange = entry.getUpperExchange();
                        int largeAmount;
                        for (largeAmount = 0; pair.amount >= exchange.getSecond(); pair = pair.removeAmount((Integer) exchange.getSecond())) {
                            largeAmount++;
                        }
                        if (pair.amount == 0) {
                            list.remove(i);
                            i--;
                        } else {
                            list.set(i, pair);
                        }
                        for (int j = 0; j < list.size(); j++) {
                            if (((CoinEntry) exchange.getFirst()).matches(((CoinValuePair) list.get(j)).coin)) {
                                list.set(j, ((CoinValuePair) list.get(j)).addAmount(largeAmount));
                                largeAmount = 0;
                            }
                        }
                        if (largeAmount > 0) {
                            list.add(new CoinValuePair(((CoinEntry) exchange.getFirst()).getCoin(), largeAmount));
                        }
                    }
                }
            }
            return sortValue(chainData, list);
        }
    }

    private static List<CoinValuePair> sortValue(@Nonnull ChainData chainData, List<CoinValuePair> list) {
        List<CoinValuePair> newList = new ArrayList();
        while (!list.isEmpty()) {
            long largestValue = chainData.getCoreValue(((CoinValuePair) list.get(0)).coin);
            int largestIndex = 0;
            for (int i = 1; i < list.size(); i++) {
                long thisValue = chainData.getCoreValue(((CoinValuePair) list.get(i)).coin);
                if (thisValue > largestValue) {
                    largestIndex = i;
                    largestValue = thisValue;
                }
            }
            newList.add((CoinValuePair) list.get(largestIndex));
            list.remove(largestIndex);
        }
        return newList;
    }

    private static boolean needsRounding(@Nonnull ChainData chainData, @Nonnull List<CoinValuePair> list) {
        for (int i = 0; i < list.size(); i++) {
            if (needsRounding(chainData, list, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean needsRounding(@Nonnull ChainData chainData, @Nonnull List<CoinValuePair> list, int index) {
        CoinValuePair pair = (CoinValuePair) list.get(index);
        Pair<CoinEntry, Integer> exchange = chainData.getUpperExchange(pair.coin);
        return exchange != null ? pair.amount >= (Integer) exchange.getSecond() : false;
    }

    public List<CoinValuePair> getEntries() {
        return this.coinValues;
    }

    public List<ItemStack> getAsItemList() {
        List<ItemStack> items = new ArrayList();
        UnmodifiableIterator var2 = this.coinValues.iterator();
        while (var2.hasNext()) {
            CoinValuePair entry = (CoinValuePair) var2.next();
            items.addAll(getStacks(entry, false));
        }
        return items;
    }

    private static List<ItemStack> getStacks(@Nonnull CoinValuePair entry, boolean limitToMaxStackSize) {
        List<ItemStack> result = new ArrayList();
        int max = limitToMaxStackSize ? new ItemStack(entry.coin).getMaxStackSize() : Integer.MAX_VALUE;
        long temp;
        for (temp = (long) entry.amount; temp > (long) max; temp -= (long) max) {
            result.add(new ItemStack(entry.coin, max));
        }
        if (temp > 0L) {
            result.add(new ItemStack(entry.coin, (int) temp));
        }
        return result;
    }

    public List<ItemStack> getAsSeperatedItemList() {
        List<ItemStack> items = new ArrayList();
        UnmodifiableIterator var2 = this.coinValues.iterator();
        while (var2.hasNext()) {
            CoinValuePair entry = (CoinValuePair) var2.next();
            items.addAll(getStacks(entry, true));
        }
        return items;
    }

    public long getEntry(Item coinItem) {
        UnmodifiableIterator var2 = this.coinValues.iterator();
        while (var2.hasNext()) {
            CoinValuePair pair = (CoinValuePair) var2.next();
            if (pair.coin == coinItem) {
                return (long) pair.amount;
            }
        }
        return 0L;
    }

    @Nonnull
    @Override
    public MutableComponent getText(@Nonnull MutableComponent emptyText) {
        ChainData chainData = CoinAPI.API.ChainData(this.chain);
        return chainData == null ? EasyText.literal("ERROR") : chainData.formatValue(this, emptyText);
    }

    @Override
    public long getCoreValue() {
        ChainData chainData = CoinAPI.API.ChainData(this.chain);
        if (chainData == null) {
            return 0L;
        } else {
            long value = 0L;
            UnmodifiableIterator var4 = this.coinValues.iterator();
            while (var4.hasNext()) {
                CoinValuePair pricePair = (CoinValuePair) var4.next();
                value += chainData.getCoreValue(pricePair.coin) * (long) pricePair.amount;
            }
            return value;
        }
    }

    @Override
    protected void writeAdditionalToJson(@Nonnull JsonObject json) {
        JsonArray array = new JsonArray();
        UnmodifiableIterator var3 = this.coinValues.iterator();
        while (var3.hasNext()) {
            CoinValuePair pair = (CoinValuePair) var3.next();
            array.add(pair.toJson());
        }
        json.add("Value", array);
        json.addProperty("Chain", this.chain);
    }

    public static MoneyValue loadCoinValue(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        String chain = GsonHelper.getAsString(json, "Chain");
        ChainData data = CoinAPI.API.ChainData(chain);
        if (data == null) {
            throw new JsonSyntaxException("No " + chain + " chain has been registered!");
        } else {
            List<CoinValuePair> valuePairs = new ArrayList();
            JsonArray entryArray = GsonHelper.getAsJsonArray(json, "Value");
            for (int i = 0; i < entryArray.size(); i++) {
                try {
                    valuePairs.add(CoinValuePair.fromJson(data, GsonHelper.convertToJsonObject(entryArray.get(i), "Value[" + i + "]")));
                } catch (ResourceLocationException | JsonSyntaxException var7) {
                    LightmansCurrency.LogError("Error parsing Coin Value entry #" + (i + 1) + "!", var7);
                }
            }
            if (valuePairs.isEmpty()) {
                throw new JsonSyntaxException("Coin Value entry has no valid coin/count entries to parse.");
            } else {
                return create(chain, valuePairs);
            }
        }
    }

    public static MoneyValue loadDeprecated(JsonElement json) throws JsonSyntaxException, ResourceLocationException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return fromNumber("main", primitive.getAsNumber().longValue());
            }
            if (primitive.isBoolean() && primitive.getAsBoolean()) {
                return MoneyValue.empty();
            }
            if (primitive.isString()) {
                double displayValue = Double.parseDouble(primitive.getAsString());
                ChainData mainChain = CoinAPI.API.ChainData("main");
                if (mainChain != null) {
                    return mainChain.getDisplayData().parseDisplayInput(displayValue);
                }
            }
        } else {
            if (json.isJsonArray()) {
                JsonArray list = json.getAsJsonArray();
                List<CoinValuePair> valuePairs = new ArrayList();
                ChainData chainData = null;
                for (int i = 0; i < list.size(); i++) {
                    try {
                        JsonObject coinData = list.get(i).getAsJsonObject();
                        Item coin = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(coinData, "Coin")));
                        if (chainData == null) {
                            chainData = CoinAPI.API.ChainDataOfCoin(coin);
                        }
                        int quantity = GsonHelper.getAsInt(coinData, "Count", 1);
                        if (quantity <= 0) {
                            throw new JsonSyntaxException("Count was less than 1");
                        }
                        if (chainData == null) {
                            throw new JsonSyntaxException("Coin Item was not a valid coin!");
                        }
                        if (chainData.findEntry(coin) == null) {
                            throw new JsonSyntaxException("Coin Item is not a valid coin, or is on a different chain from the rest of the value!");
                        }
                        if (chainData.findEntry(coin).isSideChain()) {
                            throw new JsonSyntaxException("Coin Item is from a side-chain, and thus cannot be used for value storage!");
                        }
                        valuePairs.add(new CoinValuePair(coin, quantity));
                    } catch (ResourceLocationException | JsonSyntaxException var9) {
                        LightmansCurrency.LogError("Error parsing CoinValue entry #" + (i + 1), var9);
                    }
                }
                if (!valuePairs.isEmpty() && chainData != null) {
                    return create(chainData.chain, valuePairs);
                }
                throw new JsonSyntaxException("Coin Value entry has no valid coin/count entries to parse.");
            }
            if (json.isJsonObject()) {
                JsonObject j = json.getAsJsonObject();
                if (j.has("Free")) {
                    JsonElement f = j.get("Free");
                    if (f.isJsonPrimitive()) {
                        JsonPrimitive f2 = f.getAsJsonPrimitive();
                        if (f2.isBoolean() && f2.getAsBoolean()) {
                            return MoneyValue.free();
                        }
                    }
                }
                if (j.has("Value")) {
                    JsonArray valueList = j.get("Value").getAsJsonArray();
                    ChainData chainData = null;
                    List<CoinValuePair> pairs = new ArrayList();
                    for (int i = 0; i < valueList.size(); i++) {
                        try {
                            JsonObject coinDatax = valueList.get(i).getAsJsonObject();
                            Item coinx = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(coinDatax, "Coin")));
                            if (chainData == null) {
                                chainData = CoinAPI.API.ChainDataOfCoin(coinx);
                            }
                            int quantityx = GsonHelper.getAsInt(coinDatax, "Count", 1);
                            if (quantityx <= 0) {
                                throw new JsonSyntaxException("Count was less than 1");
                            }
                            if (chainData == null) {
                                throw new JsonSyntaxException("Coin Item was not a valid coin!");
                            }
                            if (chainData.findEntry(coinx) == null) {
                                throw new JsonSyntaxException("Coin Item is not a valid coin, or is on a different chain from the rest of the value!");
                            }
                            if (chainData.findEntry(coinx).isSideChain()) {
                                throw new JsonSyntaxException("Coin Item is from a side-chain, and thus cannot be used for value storage!");
                            }
                            pairs.add(new CoinValuePair(coinx, quantityx));
                        } catch (ResourceLocationException | JsonSyntaxException var10) {
                            LightmansCurrency.LogError("Error parsing CoinValue entry #" + (i + 1), var10);
                        }
                    }
                    if (!pairs.isEmpty() && chainData != null) {
                        return create(chainData.chain, pairs);
                    }
                    throw new JsonSyntaxException("Coin Value entry has no valid coin/count entries to parse.");
                }
            }
        }
        throw new JsonSyntaxException("Coin Value entry input is not a valid Json Element.");
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.isFree(), this.coinValues });
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return !(other instanceof CoinValue coinValue) ? false : coinValue.getCoreValue() == this.getCoreValue() && coinValue.getChain().equals(this.getChain());
        }
    }

    @Nonnull
    @Override
    public DisplayEntry getDisplayEntry(@Nullable List<Component> additionalTooltips, boolean tooltipOverride) {
        return new CoinPriceEntry(this, additionalTooltips, tooltipOverride);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        UnmodifiableIterator var2 = this.coinValues.iterator();
        while (var2.hasNext()) {
            CoinValuePair pair = (CoinValuePair) var2.next();
            if (!sb.isEmpty()) {
                sb.append(',');
            }
            sb.append(pair.amount).append('x').append(ForgeRegistries.ITEMS.getKey(pair.coin));
        }
        return "CoinValue:" + sb;
    }
}