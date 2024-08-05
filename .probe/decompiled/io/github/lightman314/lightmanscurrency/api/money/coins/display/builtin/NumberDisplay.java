package io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayData;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplaySerializer;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;
import java.text.DecimalFormat;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NumberDisplay extends ValueDisplayData {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "number");

    public static final ValueDisplaySerializer SERIALIZER = new NumberDisplay.Serializer();

    private final Component format;

    private final Component wordyFormat;

    private final Item baseItem;

    private CoinEntry baseEntry = null;

    public final Component getFormat() {
        return this.format.copy();
    }

    public final Component getWordyFormat() {
        return (Component) (this.wordyFormat != null ? this.wordyFormat.copy() : this.getFormat());
    }

    @Nullable
    private CoinEntry getBaseEntry() {
        if (this.baseEntry == null) {
            ChainData parent = this.getParent();
            if (parent != null) {
                this.baseEntry = parent.findEntry(this.baseItem);
            }
        }
        return this.baseEntry;
    }

    public NumberDisplay(@Nonnull TextEntry format, @Nonnull Item baseItem) {
        this(format.get(), baseItem);
    }

    public NumberDisplay(@Nonnull Component format, @Nonnull Item baseItem) {
        this.format = format;
        this.wordyFormat = format;
        this.baseItem = baseItem;
    }

    public NumberDisplay(@Nonnull TextEntry format, @Nullable TextEntry wordyFormat, @Nonnull Item baseItem) {
        this(format.get(), wordyFormat.get(), baseItem);
    }

    public NumberDisplay(@Nonnull Component format, @Nullable Component wordyFormat, @Nonnull Item baseItem) {
        this.format = format;
        this.wordyFormat = wordyFormat;
        this.baseItem = baseItem;
    }

    @Nonnull
    @Override
    public ValueDisplaySerializer getSerializer() {
        return SERIALIZER;
    }

    public double getDisplayValue(long coreValue) {
        CoinEntry baseUnit = this.getBaseEntry();
        return baseUnit != null && baseUnit.getCoreValue() > 0L ? (double) coreValue / (double) baseUnit.getCoreValue() : 0.0;
    }

    private double getDisplayValue(@Nonnull Item item) {
        ChainData parent = this.getParent();
        return parent == null ? 0.0 : this.getDisplayValue(parent.getCoreValue(item));
    }

    private String formatDisplay(double value) {
        return this.format.getString().replace("{value}", this.formatDisplayNumber(value));
    }

    private String formatWordyDisplay(double value) {
        return this.getWordyFormat().getString().replace("{value}", this.formatDisplayNumber(value));
    }

    private String formatDisplayNumber(double value) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(this.getMaxDecimal());
        return df.format(value);
    }

    private int getMaxDecimal() {
        double minFraction = this.getDisplayValue(1L) % 1.0;
        return minFraction > 0.0 ? Double.toString(minFraction).length() - 2 : 0;
    }

    @Nonnull
    @Override
    public MutableComponent formatValue(@Nonnull CoinValue value, @Nonnull MutableComponent emptyText) {
        return EasyText.literal(this.formatDisplay(this.getDisplayValue(value.getCoreValue())));
    }

    @Override
    public void formatCoinTooltip(@Nonnull ItemStack stack, @Nonnull List<Component> tooltip) {
        double value = this.getDisplayValue(stack.getItem());
        tooltip.add(LCText.TOOLTIP_COIN_WORTH_VALUE.get(this.formatWordyDisplay(value)).withStyle(ChatFormatting.YELLOW));
        if (stack.getCount() > 1) {
            tooltip.add(LCText.TOOLTIP_COIN_WORTH_VALUE_STACK.get(this.formatWordyDisplay(value * (double) stack.getCount())).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Nonnull
    @Override
    public MoneyValue parseDisplayInput(double displayInput) {
        CoinEntry baseUnit = this.getBaseEntry();
        if (baseUnit == null) {
            return MoneyValue.empty();
        } else {
            long baseCoinValue = baseUnit.getCoreValue();
            double totalValue = displayInput * (double) baseCoinValue;
            long value = (long) totalValue;
            if (totalValue % 1.0 >= 0.5) {
                value++;
            }
            return CoinValue.fromNumber(this.getChain(), value);
        }
    }

    protected static class Serializer extends ValueDisplaySerializer {

        private Component format = null;

        private Component wordyFormat = null;

        private Item baseUnit = null;

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return NumberDisplay.TYPE;
        }

        @Override
        public void resetBuilder() {
            this.format = null;
            this.wordyFormat = null;
            this.baseUnit = null;
        }

        @Override
        public void parseAdditional(@Nonnull JsonObject chainJson) {
            this.format = Component.Serializer.fromJson(chainJson.get("displayFormat"));
            if (chainJson.has("displayFormatWordy")) {
                this.wordyFormat = Component.Serializer.fromJson(chainJson.get("displayFormatWordy"));
            }
        }

        @Override
        public void parseAdditionalFromCoin(@Nonnull CoinEntry coin, @Nonnull JsonObject coinEntry) {
            if (GsonHelper.getAsBoolean(coinEntry, "baseUnit", false)) {
                if (this.baseUnit != null) {
                    throw new JsonSyntaxException("Cannot have two baseUnit entries!");
                }
                this.baseUnit = coin.getCoin();
            }
        }

        @Override
        public void writeAdditional(@Nonnull ValueDisplayData data, @Nonnull JsonObject chainJson) {
            if (data instanceof NumberDisplay display) {
                chainJson.add("displayFormat", Component.Serializer.toJsonTree(display.format));
                if (display.wordyFormat != null) {
                    chainJson.add("displayFormatWordy", Component.Serializer.toJsonTree(display.wordyFormat));
                }
            }
        }

        @Override
        public void writeAdditionalToCoin(@Nonnull ValueDisplayData data, @Nonnull CoinEntry coin, @Nonnull JsonObject coinEntry) {
            if (data instanceof NumberDisplay display && coin.matches(display.baseItem)) {
                coinEntry.addProperty("baseUnit", true);
            }
        }

        @Nonnull
        public NumberDisplay build() throws JsonSyntaxException {
            if (this.format == null) {
                throw new JsonSyntaxException("displayFormat entry is missing or cannot be parsed!");
            } else if (this.baseUnit == null) {
                throw new JsonSyntaxException("No coin entry has the 'baseUnit: true' flag!");
            } else {
                return new NumberDisplay(this.format, this.wordyFormat, this.baseUnit);
            }
        }
    }
}