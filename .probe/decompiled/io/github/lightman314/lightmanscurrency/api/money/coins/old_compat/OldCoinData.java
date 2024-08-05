package io.github.lightman314.lightmanscurrency.api.money.coins.old_compat;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

@Deprecated
public class OldCoinData {

    public final Item coinItem;

    public final String chain;

    public final Item worthOtherCoin;

    public final int worthOtherCoinCount;

    public final String initialTranslation;

    public final String pluralTranslation;

    public final boolean isHidden;

    private OldCoinData(Item coinItem, String chain, Item worthOtherCoin, int worthOtherCoinCount, String initialTranslation, String pluralTranslation, boolean hidden) {
        this.coinItem = coinItem;
        this.chain = chain;
        this.worthOtherCoin = worthOtherCoin;
        this.worthOtherCoinCount = worthOtherCoinCount;
        this.initialTranslation = initialTranslation;
        this.pluralTranslation = pluralTranslation;
        this.isHidden = hidden;
    }

    public static OldCoinData parse(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        Item coinItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "coinitem")));
        String chain = GsonHelper.getAsString(json, "chain");
        Item otherCoin = null;
        int otherCoinCount = 0;
        if (json.has("worth")) {
            JsonObject worthData = json.get("worth").getAsJsonObject();
            otherCoin = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(worthData, "coin")));
            otherCoinCount = GsonHelper.getAsInt(worthData, "count");
        }
        String initial = null;
        if (json.has("initial")) {
            initial = GsonHelper.getAsString(json, "initial");
        }
        String plural = null;
        if (json.has("plural")) {
            plural = GsonHelper.getAsString(json, "plural");
        }
        boolean hidden = json.has("hidden") && GsonHelper.getAsBoolean(json, "hidden");
        return new OldCoinData(coinItem, chain, otherCoin, otherCoinCount, initial, plural, hidden);
    }
}