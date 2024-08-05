package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;

public class ItemPredicate {

    public static final ItemPredicate ANY = new ItemPredicate();

    @Nullable
    private final TagKey<Item> tag;

    @Nullable
    private final Set<Item> items;

    private final MinMaxBounds.Ints count;

    private final MinMaxBounds.Ints durability;

    private final EnchantmentPredicate[] enchantments;

    private final EnchantmentPredicate[] storedEnchantments;

    @Nullable
    private final Potion potion;

    private final NbtPredicate nbt;

    public ItemPredicate() {
        this.tag = null;
        this.items = null;
        this.potion = null;
        this.count = MinMaxBounds.Ints.ANY;
        this.durability = MinMaxBounds.Ints.ANY;
        this.enchantments = EnchantmentPredicate.NONE;
        this.storedEnchantments = EnchantmentPredicate.NONE;
        this.nbt = NbtPredicate.ANY;
    }

    public ItemPredicate(@Nullable TagKey<Item> tagKeyItem0, @Nullable Set<Item> setItem1, MinMaxBounds.Ints minMaxBoundsInts2, MinMaxBounds.Ints minMaxBoundsInts3, EnchantmentPredicate[] enchantmentPredicate4, EnchantmentPredicate[] enchantmentPredicate5, @Nullable Potion potion6, NbtPredicate nbtPredicate7) {
        this.tag = tagKeyItem0;
        this.items = setItem1;
        this.count = minMaxBoundsInts2;
        this.durability = minMaxBoundsInts3;
        this.enchantments = enchantmentPredicate4;
        this.storedEnchantments = enchantmentPredicate5;
        this.potion = potion6;
        this.nbt = nbtPredicate7;
    }

    public boolean matches(ItemStack itemStack0) {
        if (this == ANY) {
            return true;
        } else if (this.tag != null && !itemStack0.is(this.tag)) {
            return false;
        } else if (this.items != null && !this.items.contains(itemStack0.getItem())) {
            return false;
        } else if (!this.count.matches(itemStack0.getCount())) {
            return false;
        } else if (!this.durability.m_55327_() && !itemStack0.isDamageableItem()) {
            return false;
        } else if (!this.durability.matches(itemStack0.getMaxDamage() - itemStack0.getDamageValue())) {
            return false;
        } else if (!this.nbt.matches(itemStack0)) {
            return false;
        } else {
            if (this.enchantments.length > 0) {
                Map<Enchantment, Integer> $$1 = EnchantmentHelper.deserializeEnchantments(itemStack0.getEnchantmentTags());
                for (EnchantmentPredicate $$2 : this.enchantments) {
                    if (!$$2.containedIn($$1)) {
                        return false;
                    }
                }
            }
            if (this.storedEnchantments.length > 0) {
                Map<Enchantment, Integer> $$3 = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(itemStack0));
                for (EnchantmentPredicate $$4 : this.storedEnchantments) {
                    if (!$$4.containedIn($$3)) {
                        return false;
                    }
                }
            }
            Potion $$5 = PotionUtils.getPotion(itemStack0);
            return this.potion == null || this.potion == $$5;
        }
    }

    public static ItemPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "item");
            MinMaxBounds.Ints $$2 = MinMaxBounds.Ints.fromJson($$1.get("count"));
            MinMaxBounds.Ints $$3 = MinMaxBounds.Ints.fromJson($$1.get("durability"));
            if ($$1.has("data")) {
                throw new JsonParseException("Disallowed data tag found");
            } else {
                NbtPredicate $$4 = NbtPredicate.fromJson($$1.get("nbt"));
                Set<Item> $$5 = null;
                JsonArray $$6 = GsonHelper.getAsJsonArray($$1, "items", null);
                if ($$6 != null) {
                    com.google.common.collect.ImmutableSet.Builder<Item> $$7 = ImmutableSet.builder();
                    for (JsonElement $$8 : $$6) {
                        ResourceLocation $$9 = new ResourceLocation(GsonHelper.convertToString($$8, "item"));
                        $$7.add((Item) BuiltInRegistries.ITEM.m_6612_($$9).orElseThrow(() -> new JsonSyntaxException("Unknown item id '" + $$9 + "'")));
                    }
                    $$5 = $$7.build();
                }
                TagKey<Item> $$10 = null;
                if ($$1.has("tag")) {
                    ResourceLocation $$11 = new ResourceLocation(GsonHelper.getAsString($$1, "tag"));
                    $$10 = TagKey.create(Registries.ITEM, $$11);
                }
                Potion $$12 = null;
                if ($$1.has("potion")) {
                    ResourceLocation $$13 = new ResourceLocation(GsonHelper.getAsString($$1, "potion"));
                    $$12 = (Potion) BuiltInRegistries.POTION.m_6612_($$13).orElseThrow(() -> new JsonSyntaxException("Unknown potion '" + $$13 + "'"));
                }
                EnchantmentPredicate[] $$14 = EnchantmentPredicate.fromJsonArray($$1.get("enchantments"));
                EnchantmentPredicate[] $$15 = EnchantmentPredicate.fromJsonArray($$1.get("stored_enchantments"));
                return new ItemPredicate($$10, $$5, $$2, $$3, $$14, $$15, $$12, $$4);
            }
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.items != null) {
                JsonArray $$1 = new JsonArray();
                for (Item $$2 : this.items) {
                    $$1.add(BuiltInRegistries.ITEM.getKey($$2).toString());
                }
                $$0.add("items", $$1);
            }
            if (this.tag != null) {
                $$0.addProperty("tag", this.tag.location().toString());
            }
            $$0.add("count", this.count.m_55328_());
            $$0.add("durability", this.durability.m_55328_());
            $$0.add("nbt", this.nbt.serializeToJson());
            if (this.enchantments.length > 0) {
                JsonArray $$3 = new JsonArray();
                for (EnchantmentPredicate $$4 : this.enchantments) {
                    $$3.add($$4.serializeToJson());
                }
                $$0.add("enchantments", $$3);
            }
            if (this.storedEnchantments.length > 0) {
                JsonArray $$5 = new JsonArray();
                for (EnchantmentPredicate $$6 : this.storedEnchantments) {
                    $$5.add($$6.serializeToJson());
                }
                $$0.add("stored_enchantments", $$5);
            }
            if (this.potion != null) {
                $$0.addProperty("potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
            }
            return $$0;
        }
    }

    public static ItemPredicate[] fromJsonArray(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonArray $$1 = GsonHelper.convertToJsonArray(jsonElement0, "items");
            ItemPredicate[] $$2 = new ItemPredicate[$$1.size()];
            for (int $$3 = 0; $$3 < $$2.length; $$3++) {
                $$2[$$3] = fromJson($$1.get($$3));
            }
            return $$2;
        } else {
            return new ItemPredicate[0];
        }
    }

    public static class Builder {

        private final List<EnchantmentPredicate> enchantments = Lists.newArrayList();

        private final List<EnchantmentPredicate> storedEnchantments = Lists.newArrayList();

        @Nullable
        private Set<Item> items;

        @Nullable
        private TagKey<Item> tag;

        private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;

        private MinMaxBounds.Ints durability = MinMaxBounds.Ints.ANY;

        @Nullable
        private Potion potion;

        private NbtPredicate nbt = NbtPredicate.ANY;

        private Builder() {
        }

        public static ItemPredicate.Builder item() {
            return new ItemPredicate.Builder();
        }

        public ItemPredicate.Builder of(ItemLike... itemLike0) {
            this.items = (Set<Item>) Stream.of(itemLike0).map(ItemLike::m_5456_).collect(ImmutableSet.toImmutableSet());
            return this;
        }

        public ItemPredicate.Builder of(TagKey<Item> tagKeyItem0) {
            this.tag = tagKeyItem0;
            return this;
        }

        public ItemPredicate.Builder withCount(MinMaxBounds.Ints minMaxBoundsInts0) {
            this.count = minMaxBoundsInts0;
            return this;
        }

        public ItemPredicate.Builder hasDurability(MinMaxBounds.Ints minMaxBoundsInts0) {
            this.durability = minMaxBoundsInts0;
            return this;
        }

        public ItemPredicate.Builder isPotion(Potion potion0) {
            this.potion = potion0;
            return this;
        }

        public ItemPredicate.Builder hasNbt(CompoundTag compoundTag0) {
            this.nbt = new NbtPredicate(compoundTag0);
            return this;
        }

        public ItemPredicate.Builder hasEnchantment(EnchantmentPredicate enchantmentPredicate0) {
            this.enchantments.add(enchantmentPredicate0);
            return this;
        }

        public ItemPredicate.Builder hasStoredEnchantment(EnchantmentPredicate enchantmentPredicate0) {
            this.storedEnchantments.add(enchantmentPredicate0);
            return this;
        }

        public ItemPredicate build() {
            return new ItemPredicate(this.tag, this.items, this.count, this.durability, (EnchantmentPredicate[]) this.enchantments.toArray(EnchantmentPredicate.NONE), (EnchantmentPredicate[]) this.storedEnchantments.toArray(EnchantmentPredicate.NONE), this.potion, this.nbt);
        }
    }
}