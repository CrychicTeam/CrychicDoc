package io.github.lightman314.lightmanscurrency.api.money.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.types.CurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.types.builtin.NullCurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.display.EmptyPriceEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class MoneyValue {

    private static MoneyValue FREE = null;

    private static MoneyValue EMPTY = null;

    private String uniqueName;

    @Nonnull
    public static MoneyValue free() {
        if (FREE == null) {
            FREE = new MoneyValue.NullValue(true);
        }
        return FREE;
    }

    @Nonnull
    public static MoneyValue empty() {
        if (EMPTY == null) {
            EMPTY = new MoneyValue.NullValue(false);
        }
        return EMPTY;
    }

    @Nonnull
    protected abstract ResourceLocation getType();

    public CurrencyType getCurrency() {
        return MoneyAPI.API.GetRegisteredCurrencyType(this.getType());
    }

    @Nonnull
    protected String generateUniqueName() {
        return this.getType().toString();
    }

    @Nonnull
    public final String getUniqueName() {
        if (this.uniqueName == null) {
            this.uniqueName = this.generateUniqueName();
        }
        return this.uniqueName;
    }

    @Nonnull
    protected final String generateCustomUniqueName(@Nonnull String addon) {
        return generateCustomUniqueName(this.getType(), addon);
    }

    @Nonnull
    public static String generateCustomUniqueName(@Nonnull ResourceLocation type, @Nonnull String addon) {
        return addon.isEmpty() ? type.toString() : type + "!" + addon;
    }

    public boolean isFree() {
        return false;
    }

    public abstract boolean isEmpty();

    public boolean isValidPrice() {
        return this.isFree() || !this.isEmpty();
    }

    public boolean isInvalid() {
        return false;
    }

    public boolean sameType(@Nonnull MoneyValue otherValue) {
        return otherValue.getUniqueName().equals(this.getUniqueName()) || this instanceof MoneyValue.NullValue || otherValue instanceof MoneyValue.NullValue;
    }

    public abstract long getCoreValue();

    @Nonnull
    public final String getString() {
        return this.getString("");
    }

    @Nonnull
    public String getString(@Nonnull String emptyText) {
        return this.getText(EasyText.literal(emptyText)).getString();
    }

    @Nonnull
    public final MutableComponent getText() {
        return this.getText(EasyText.empty());
    }

    @Nonnull
    public final MutableComponent getText(@Nonnull String emptyText) {
        return this.getText(EasyText.literal(emptyText));
    }

    public abstract MutableComponent getText(@Nonnull MutableComponent var1);

    public abstract MoneyValue addValue(@Nonnull MoneyValue var1);

    public abstract boolean containsValue(@Nonnull MoneyValue var1);

    public abstract MoneyValue subtractValue(@Nonnull MoneyValue var1);

    @Nonnull
    public final MoneyValue percentageOfValue(int percentage) {
        return this.percentageOfValue(percentage, false);
    }

    public abstract MoneyValue percentageOfValue(int var1, boolean var2);

    @Nonnull
    public abstract MoneyValue multiplyValue(double var1);

    @Nonnull
    public abstract List<ItemStack> onBlockBroken(@Nonnull Level var1, @Nonnull OwnerData var2);

    @Nonnull
    public abstract MoneyValue getSmallestValue();

    @Nonnull
    public final CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        tag.putString("type", this.getType().toString());
        return tag;
    }

    protected abstract void saveAdditional(@Nonnull CompoundTag var1);

    public final void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.save());
    }

    public final JsonObject toJson() {
        JsonObject json = new JsonObject();
        this.writeAdditionalToJson(json);
        json.addProperty("type", this.getType().toString());
        return json;
    }

    protected abstract void writeAdditionalToJson(@Nonnull JsonObject var1);

    @Nonnull
    public static MoneyValue decode(@Nonnull FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readAnySizeNbt();
        MoneyValue loadedValue = load(tag);
        return (MoneyValue) Objects.requireNonNullElse(loadedValue, EMPTY);
    }

    @Nullable
    public static MoneyValue load(@Nonnull CompoundTag tag) {
        if (tag.contains("type", 8)) {
            ResourceLocation valueType;
            try {
                valueType = new ResourceLocation(tag.getString("type"));
            } catch (ResourceLocationException var3) {
                return null;
            }
            CurrencyType currencyType = MoneyAPI.API.GetRegisteredCurrencyType(valueType);
            if (currencyType != null) {
                return currencyType.loadMoneyValue(tag);
            } else {
                LightmansCurrency.LogError("No CurrencyType " + valueType + " could be found. Could not load the stored value!");
                return null;
            }
        } else {
            return CoinValue.loadDeprecated(tag);
        }
    }

    @Nonnull
    public static MoneyValue safeLoad(@Nonnull CompoundTag parentTag, @Nonnull String tagName) {
        if (parentTag.contains(tagName, 10)) {
            MoneyValue result = load(parentTag.getCompound(tagName));
            return result == null ? empty() : result;
        } else {
            MoneyValue result = CoinValue.loadDeprecated(parentTag, tagName);
            return result == null ? empty() : result;
        }
    }

    public static MoneyValue loadFromJson(@Nonnull JsonElement json) throws JsonSyntaxException, ResourceLocationException {
        return !json.isJsonArray() && !json.isJsonPrimitive() ? loadFromJson(GsonHelper.convertToJsonObject(json, "Price")) : CoinValue.loadDeprecated(json);
    }

    public static MoneyValue loadFromJson(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        if (json.has("type")) {
            ResourceLocation valueType = new ResourceLocation(GsonHelper.getAsString(json, "type"));
            CurrencyType currencyType = MoneyAPI.API.GetRegisteredCurrencyType(valueType);
            if (currencyType != null) {
                return currencyType.loadMoneyValueJson(json);
            } else {
                throw new JsonSyntaxException("No CurrencyType " + valueType + " could be found. Could not load the stored json value!");
            }
        } else {
            return CoinValue.loadDeprecated(json);
        }
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public abstract DisplayEntry getDisplayEntry(@Nullable List<Component> var1, boolean var2);

    public boolean equals(Object obj) {
        return !(obj instanceof MoneyValue otherVal) ? super.equals(obj) : this.getUniqueName().equals(otherVal.getUniqueName()) && this.getCoreValue() == otherVal.getCoreValue() && this.isFree() == otherVal.isFree();
    }

    private static final class NullValue extends MoneyValue {

        private final boolean free;

        private NullValue(boolean free) {
            this.free = free;
        }

        @Nonnull
        @Override
        protected ResourceLocation getType() {
            return NullCurrencyType.TYPE;
        }

        @Nonnull
        @Override
        protected String generateUniqueName() {
            return this.free ? "null!free" : "null!empty";
        }

        @Override
        public boolean isFree() {
            return this.free;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public long getCoreValue() {
            return 0L;
        }

        @Nonnull
        @Override
        public MutableComponent getText(@Nonnull MutableComponent emptyText) {
            return this.free ? LCText.GUI_MONEY_VALUE_FREE.get() : emptyText;
        }

        @Override
        public MoneyValue addValue(@Nonnull MoneyValue addedValue) {
            return addedValue;
        }

        @Nonnull
        @Override
        public MoneyValue multiplyValue(double multiplier) {
            return this;
        }

        @Override
        public boolean containsValue(@Nonnull MoneyValue queryValue) {
            return queryValue.isFree() || queryValue.isEmpty();
        }

        @Override
        public MoneyValue subtractValue(@Nonnull MoneyValue removedValue) {
            return !removedValue.isFree() && !removedValue.isEmpty() ? null : this;
        }

        @Override
        public MoneyValue percentageOfValue(int percentage, boolean roundUp) {
            return MoneyValue.FREE;
        }

        @Nonnull
        @Override
        public List<ItemStack> onBlockBroken(@Nonnull Level level, @Nonnull OwnerData owner) {
            return new ArrayList();
        }

        @Override
        protected void saveAdditional(@Nonnull CompoundTag tag) {
            tag.putBoolean("Free", this.isFree());
        }

        @Override
        protected void writeAdditionalToJson(@Nonnull JsonObject json) {
            if (this.isFree()) {
                json.addProperty("Free", true);
            }
        }

        @Nonnull
        @Override
        public MoneyValue getSmallestValue() {
            return this;
        }

        @Nonnull
        @Override
        public DisplayEntry getDisplayEntry(@Nullable List<Component> tooltips, boolean tooltipOverride) {
            return new EmptyPriceEntry(this, tooltips);
        }

        public String toString() {
            return "NullMoneyValue:" + (this.free ? "Free" : "Empty");
        }
    }
}