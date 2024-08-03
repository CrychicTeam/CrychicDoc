package io.github.lightman314.lightmanscurrency.api.money.coins.data.coin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class CoinEntry {

    private final Item coin;

    private final boolean sideChain;

    private long coreValue = 0L;

    private boolean exchangeRatesSet = false;

    private Pair<CoinEntry, Integer> lowerExchange = null;

    private Pair<CoinEntry, Integer> upperExchange = null;

    public final boolean isSideChain() {
        return this.sideChain;
    }

    public long getCoreValue() {
        return this.coreValue;
    }

    public void setCoreValue(long value) {
        if (this.coreValue > 0L) {
            LightmansCurrency.LogError("Should not be overriding a coin entries defined core value once it's already been defined!");
        } else {
            this.coreValue = value;
        }
    }

    @Nullable
    public Pair<CoinEntry, Integer> getLowerExchange() {
        return this.lowerExchange;
    }

    @Nullable
    public Pair<CoinEntry, Integer> getUpperExchange() {
        return this.upperExchange;
    }

    public void defineExchanges(@Nullable Pair<CoinEntry, Integer> lowerExchange, @Nullable Pair<CoinEntry, Integer> upperExchange) {
        if (this.exchangeRatesSet) {
            LightmansCurrency.LogWarning("Attempted to define a coin entries exchange rates after they've already been defined.");
        } else {
            this.lowerExchange = lowerExchange;
            this.upperExchange = upperExchange;
            this.exchangeRatesSet = true;
        }
    }

    public int getExchangeRate() {
        return 0;
    }

    public final Component getName() {
        return new ItemStack(this.coin).getHoverName();
    }

    public final Item getCoin() {
        return this.coin;
    }

    public CoinEntry(@Nonnull Item coin) {
        this(coin, false);
    }

    protected CoinEntry(@Nonnull Item coin, boolean sideChain) {
        this.coin = coin;
        this.sideChain = sideChain;
    }

    public boolean matches(@Nonnull CoinEntry coin) {
        return this == coin || this.coin == coin.coin;
    }

    public boolean matches(@Nonnull Item item) {
        return this.coin == item;
    }

    public boolean matches(@Nonnull ItemStack stack) {
        return this.matches(stack.getItem());
    }

    public boolean matches(@Nonnull CompoundTag tag) {
        return tag.contains("coin") ? this.matches(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("coin")))) : false;
    }

    public final JsonObject serialize(@Nonnull ValueDisplayData displayData) {
        JsonObject json = new JsonObject();
        json.addProperty("Coin", ForgeRegistries.ITEMS.getKey(this.coin).toString());
        this.writeAdditional(json);
        displayData.getSerializer().writeAdditionalToCoin(displayData, this, json);
        return json;
    }

    protected void writeAdditional(@Nonnull JsonObject json) {
    }

    protected static Item parseBase(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        ResourceLocation itemID = new ResourceLocation(GsonHelper.getAsString(json, "Coin"));
        Item item = ForgeRegistries.ITEMS.getValue(itemID);
        if (item != null && item != Items.AIR) {
            return item;
        } else {
            throw new JsonSyntaxException(itemID + " is not a valid item!");
        }
    }

    public static CoinEntry parse(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        return new CoinEntry(parseBase(json));
    }
}