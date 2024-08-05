package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class WrappedMinMaxBounds {

    public static final WrappedMinMaxBounds ANY = new WrappedMinMaxBounds(null, null);

    public static final SimpleCommandExceptionType ERROR_INTS_ONLY = new SimpleCommandExceptionType(Component.translatable("argument.range.ints"));

    @Nullable
    private final Float min;

    @Nullable
    private final Float max;

    public WrappedMinMaxBounds(@Nullable Float float0, @Nullable Float float1) {
        this.min = float0;
        this.max = float1;
    }

    public static WrappedMinMaxBounds exactly(float float0) {
        return new WrappedMinMaxBounds(float0, float0);
    }

    public static WrappedMinMaxBounds between(float float0, float float1) {
        return new WrappedMinMaxBounds(float0, float1);
    }

    public static WrappedMinMaxBounds atLeast(float float0) {
        return new WrappedMinMaxBounds(float0, null);
    }

    public static WrappedMinMaxBounds atMost(float float0) {
        return new WrappedMinMaxBounds(null, float0);
    }

    public boolean matches(float float0) {
        if (this.min != null && this.max != null && this.min > this.max && this.min > float0 && this.max < float0) {
            return false;
        } else {
            return this.min != null && this.min > float0 ? false : this.max == null || !(this.max < float0);
        }
    }

    public boolean matchesSqr(double double0) {
        if (this.min != null && this.max != null && this.min > this.max && (double) (this.min * this.min) > double0 && (double) (this.max * this.max) < double0) {
            return false;
        } else {
            return this.min != null && (double) (this.min * this.min) > double0 ? false : this.max == null || !((double) (this.max * this.max) < double0);
        }
    }

    @Nullable
    public Float getMin() {
        return this.min;
    }

    @Nullable
    public Float getMax() {
        return this.max;
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else if (this.min != null && this.max != null && this.min.equals(this.max)) {
            return new JsonPrimitive(this.min);
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.min != null) {
                $$0.addProperty("min", this.min);
            }
            if (this.max != null) {
                $$0.addProperty("max", this.min);
            }
            return $$0;
        }
    }

    public static WrappedMinMaxBounds fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 == null || jsonElement0.isJsonNull()) {
            return ANY;
        } else if (GsonHelper.isNumberValue(jsonElement0)) {
            float $$1 = GsonHelper.convertToFloat(jsonElement0, "value");
            return new WrappedMinMaxBounds($$1, $$1);
        } else {
            JsonObject $$2 = GsonHelper.convertToJsonObject(jsonElement0, "value");
            Float $$3 = $$2.has("min") ? GsonHelper.getAsFloat($$2, "min") : null;
            Float $$4 = $$2.has("max") ? GsonHelper.getAsFloat($$2, "max") : null;
            return new WrappedMinMaxBounds($$3, $$4);
        }
    }

    public static WrappedMinMaxBounds fromReader(StringReader stringReader0, boolean boolean1) throws CommandSyntaxException {
        return fromReader(stringReader0, boolean1, p_164413_ -> p_164413_);
    }

    public static WrappedMinMaxBounds fromReader(StringReader stringReader0, boolean boolean1, Function<Float, Float> functionFloatFloat2) throws CommandSyntaxException {
        if (!stringReader0.canRead()) {
            throw MinMaxBounds.ERROR_EMPTY.createWithContext(stringReader0);
        } else {
            int $$3 = stringReader0.getCursor();
            Float $$4 = optionallyFormat(readNumber(stringReader0, boolean1), functionFloatFloat2);
            Float $$5;
            if (stringReader0.canRead(2) && stringReader0.peek() == '.' && stringReader0.peek(1) == '.') {
                stringReader0.skip();
                stringReader0.skip();
                $$5 = optionallyFormat(readNumber(stringReader0, boolean1), functionFloatFloat2);
                if ($$4 == null && $$5 == null) {
                    stringReader0.setCursor($$3);
                    throw MinMaxBounds.ERROR_EMPTY.createWithContext(stringReader0);
                }
            } else {
                if (!boolean1 && stringReader0.canRead() && stringReader0.peek() == '.') {
                    stringReader0.setCursor($$3);
                    throw ERROR_INTS_ONLY.createWithContext(stringReader0);
                }
                $$5 = $$4;
            }
            if ($$4 == null && $$5 == null) {
                stringReader0.setCursor($$3);
                throw MinMaxBounds.ERROR_EMPTY.createWithContext(stringReader0);
            } else {
                return new WrappedMinMaxBounds($$4, $$5);
            }
        }
    }

    @Nullable
    private static Float readNumber(StringReader stringReader0, boolean boolean1) throws CommandSyntaxException {
        int $$2 = stringReader0.getCursor();
        while (stringReader0.canRead() && isAllowedNumber(stringReader0, boolean1)) {
            stringReader0.skip();
        }
        String $$3 = stringReader0.getString().substring($$2, stringReader0.getCursor());
        if ($$3.isEmpty()) {
            return null;
        } else {
            try {
                return Float.parseFloat($$3);
            } catch (NumberFormatException var5) {
                if (boolean1) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(stringReader0, $$3);
                } else {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(stringReader0, $$3);
                }
            }
        }
    }

    private static boolean isAllowedNumber(StringReader stringReader0, boolean boolean1) {
        char $$2 = stringReader0.peek();
        if (($$2 < '0' || $$2 > '9') && $$2 != '-') {
            return boolean1 && $$2 == '.' ? !stringReader0.canRead(2) || stringReader0.peek(1) != '.' : false;
        } else {
            return true;
        }
    }

    @Nullable
    private static Float optionallyFormat(@Nullable Float float0, Function<Float, Float> functionFloatFloat1) {
        return float0 == null ? null : (Float) functionFloatFloat1.apply(float0);
    }
}