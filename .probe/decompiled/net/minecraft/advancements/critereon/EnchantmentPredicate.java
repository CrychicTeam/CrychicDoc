package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentPredicate {

    public static final EnchantmentPredicate ANY = new EnchantmentPredicate();

    public static final EnchantmentPredicate[] NONE = new EnchantmentPredicate[0];

    @Nullable
    private final Enchantment enchantment;

    private final MinMaxBounds.Ints level;

    public EnchantmentPredicate() {
        this.enchantment = null;
        this.level = MinMaxBounds.Ints.ANY;
    }

    public EnchantmentPredicate(@Nullable Enchantment enchantment0, MinMaxBounds.Ints minMaxBoundsInts1) {
        this.enchantment = enchantment0;
        this.level = minMaxBoundsInts1;
    }

    public boolean containedIn(Map<Enchantment, Integer> mapEnchantmentInteger0) {
        if (this.enchantment != null) {
            if (!mapEnchantmentInteger0.containsKey(this.enchantment)) {
                return false;
            }
            int $$1 = (Integer) mapEnchantmentInteger0.get(this.enchantment);
            if (this.level != MinMaxBounds.Ints.ANY && !this.level.matches($$1)) {
                return false;
            }
        } else if (this.level != MinMaxBounds.Ints.ANY) {
            for (Integer $$2 : mapEnchantmentInteger0.values()) {
                if (this.level.matches($$2)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.enchantment != null) {
                $$0.addProperty("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this.enchantment).toString());
            }
            $$0.add("levels", this.level.m_55328_());
            return $$0;
        }
    }

    public static EnchantmentPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "enchantment");
            Enchantment $$2 = null;
            if ($$1.has("enchantment")) {
                ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString($$1, "enchantment"));
                $$2 = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional($$3).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + $$3 + "'"));
            }
            MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson($$1.get("levels"));
            return new EnchantmentPredicate($$2, $$4);
        } else {
            return ANY;
        }
    }

    public static EnchantmentPredicate[] fromJsonArray(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonArray $$1 = GsonHelper.convertToJsonArray(jsonElement0, "enchantments");
            EnchantmentPredicate[] $$2 = new EnchantmentPredicate[$$1.size()];
            for (int $$3 = 0; $$3 < $$2.length; $$3++) {
                $$2[$$3] = fromJson($$1.get($$3));
            }
            return $$2;
        } else {
            return NONE;
        }
    }
}