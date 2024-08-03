package io.github.lightman314.lightmanscurrency.api.money.value.builtin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import io.github.lightman314.lightmanscurrency.util.NumberUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class CoinValueParser extends MoneyValueParser {

    public static final MoneyValueParser INSTANCE = new CoinValueParser();

    private CoinValueParser() {
        super("coin");
    }

    @Override
    protected boolean tryParse(@Nullable String prefix) {
        return prefix == null || super.tryParse(prefix);
    }

    @Override
    public MoneyValue parseValueArgument(@Nonnull StringReader reader) throws CommandSyntaxException {
        MoneyValue value = MoneyValue.empty();
        while (reader.canRead()) {
            String s1 = MoneyValueParser.readStringUntil(reader, '-', ',');
            if (NumberUtil.IsInteger(s1)) {
                int count = NumberUtil.GetIntegerValue(s1, 1);
                String s2 = MoneyValueParser.readStringUntil(reader, ',');
                value = TryParseCoin(value, reader, s2, count);
            } else {
                value = TryParseCoin(value, reader, s1, 1);
            }
        }
        return value;
    }

    @Nullable
    @Override
    protected String writeValueArgument(@Nonnull MoneyValue value) {
        if (value instanceof CoinValue coinValue) {
            StringBuilder builder = new StringBuilder();
            boolean comma = false;
            for (CoinValuePair pair : coinValue.getEntries()) {
                if (comma) {
                    builder.append(',');
                } else {
                    comma = true;
                }
                builder.append(pair.amount).append('-').append(ForgeRegistries.ITEMS.getKey(pair.coin).toString());
            }
            return builder.toString();
        } else {
            return null;
        }
    }

    private static MoneyValue TryParseCoin(MoneyValue result, StringReader reader, String coinIDString, int count) throws CommandSyntaxException {
        if (ResourceLocation.isValidResourceLocation(coinIDString)) {
            ResourceLocation coinID = new ResourceLocation(coinIDString);
            Item coin = ForgeRegistries.ITEMS.getValue(coinID);
            ChainData chainData = CoinAPI.API.ChainDataOfCoin(coin);
            if (chainData == null) {
                throw NotACoinException(coinID.toString(), reader);
            } else {
                CoinEntry entry = chainData.findEntry(coin);
                if (entry != null && !entry.isSideChain()) {
                    return result.addValue(CoinValue.fromNumber(chainData.chain, entry.getCoreValue() * (long) count));
                } else {
                    throw NotACoinException(coinID.toString(), reader);
                }
            }
        } else {
            throw NotACoinException(coinIDString, reader);
        }
    }

    public static CommandSyntaxException NotACoinException(String item, StringReader reader) {
        return new CommandSyntaxException(MoneyValueParser.EXCEPTION_TYPE, LCText.ARGUMENT_MONEY_VALUE_NOT_A_COIN.get(item), reader.getString(), reader.getCursor());
    }

    @Nonnull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(@Nonnull CommandContext<S> context, @Nonnull SuggestionsBuilder builder, @Nonnull String trail, @Nonnull HolderLookup<Item> lookup) {
        String[] parts = trail.split(",");
        String finalPart = parts[parts.length - 1];
        String[] split = finalPart.split("-");
        if (finalPart.indexOf(45) < 0) {
            return Suggestions.empty();
        } else {
            String idPart = split.length > 1 ? split[1] : "";
            StringBuilder previousParts = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) {
                    previousParts.append(",");
                }
                previousParts.append(parts[i]);
            }
            try {
                MoneyValueParser.parse(new StringReader(previousParts.toString()), true);
            } catch (CommandSyntaxException var15) {
                return Suggestions.empty();
            }
            String previous = previousParts.append(split[0]).append("-").toString();
            for (ResourceLocation coin : this.lookupCoinList(lookup)) {
                String coinString = coin.toString();
                if (matchesSubStr(idPart, coinString) || idPart.isEmpty()) {
                    builder.suggest(previous + coinString);
                }
            }
            return builder.buildFuture();
        }
    }

    private static boolean matchesSubStr(String input, String resource) {
        for (int i = 0; !resource.startsWith(input, i); i++) {
            i = resource.indexOf(95, i);
            if (i < 0) {
                return false;
            }
        }
        return true;
    }

    private List<ResourceLocation> lookupCoinList(@Nonnull HolderLookup<Item> lookup) {
        return lookup.listElementIds().map(ResourceKey::m_135782_).filter(CoinValueParser::isCoin).toList();
    }

    private static boolean isCoin(ResourceLocation itemID) {
        return CoinAPI.API.IsCoin(ForgeRegistries.ITEMS.getValue(itemID), false);
    }

    @Override
    public void addExamples(@Nonnull List<String> examples) {
        for (ChainData chain : CoinAPI.API.AllChainData()) {
            int suggestedCount = 1;
            StringBuilder result = new StringBuilder(this.prefix).append(";");
            List<Item> coins = new ArrayList();
            for (CoinEntry entry : chain.getAllEntries(false)) {
                coins.add(entry.getCoin());
            }
            for (int i = 0; i < coins.size(); i++) {
                Item coin = (Item) coins.get(i);
                if (i > 0) {
                    result.append(",");
                }
                result.append(suggestedCount++).append("-").append(ForgeRegistries.ITEMS.getKey(coin).toString());
            }
            examples.add(result.toString());
        }
    }
}