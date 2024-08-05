package se.mickelus.tetra.module.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.data.deserializer.ItemPredicateDeserializer;

@ParametersAreNonnullByDefault
public class OutcomeMaterial {

    private static final JsonArray emptyArray = new JsonArray();

    public int count = 1;

    protected Collection<ItemStack> itemStacks = Collections.emptyList();

    protected TagKey<Item> tagLocation;

    private ItemPredicate predicate;

    public OutcomeMaterial offsetCount(float multiplier, int offset) {
        OutcomeMaterial result = new OutcomeMaterial();
        result.count = Math.round((float) this.count * multiplier) + offset;
        result.itemStacks = (Collection<ItemStack>) this.itemStacks.stream().map(ItemStack::m_41777_).peek(result::setCount).collect(Collectors.toList());
        result.tagLocation = this.tagLocation;
        result.predicate = this.predicate;
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public Component[] getDisplayNames() {
        if (this.getPredicate() == null) {
            return new Component[] { Component.literal("Unknown material") };
        } else if (this.itemStacks != null) {
            return (Component[]) this.itemStacks.stream().map(ItemStack::m_41786_).toArray(Component[]::new);
        } else {
            return this.tagLocation != null ? (Component[]) ForgeRegistries.ITEMS.tags().getTag(this.tagLocation).stream().map(item -> item.getName(item.getDefaultInstance())).toArray(Component[]::new) : new Component[] { Component.literal("Unknown material") };
        }
    }

    public ItemStack[] getApplicableItemStacks() {
        if (this.getPredicate() == null) {
            return new ItemStack[0];
        } else if (this.itemStacks != null && !this.itemStacks.isEmpty()) {
            return (ItemStack[]) this.itemStacks.toArray(ItemStack[]::new);
        } else {
            return this.tagLocation != null ? (ItemStack[]) ForgeRegistries.ITEMS.tags().getTag(this.tagLocation).stream().map(Item::m_7968_).map(this::setCount).toArray(ItemStack[]::new) : new ItemStack[0];
        }
    }

    @Nullable
    public ItemPredicate getPredicate() {
        return this.predicate;
    }

    private ItemStack setCount(ItemStack itemStack) {
        itemStack.setCount(this.count);
        return itemStack;
    }

    public boolean isTagged() {
        return this.tagLocation != null;
    }

    public boolean isValid() {
        return this.predicate != null;
    }

    public static class Deserializer implements JsonDeserializer<OutcomeMaterial> {

        public OutcomeMaterial deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) {
            OutcomeMaterial material = new OutcomeMaterial();
            if (element != null && !element.isJsonNull()) {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(element, "material");
                material.count = GsonHelper.getAsInt(jsonObject, "count", 1);
                if (jsonObject.has("items")) {
                    try {
                        material.itemStacks = (Collection<ItemStack>) StreamSupport.stream(GsonHelper.getAsJsonArray(jsonObject, "items", OutcomeMaterial.emptyArray).spliterator(), false).map(jsonElement -> GsonHelper.convertToString(jsonElement, "item")).map(ResourceLocation::new).map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).map(item -> new ItemStack(item, material.count)).collect(Collectors.toList());
                    } catch (JsonSyntaxException var8) {
                        material.itemStacks = Collections.emptyList();
                    }
                    if (!material.itemStacks.isEmpty() && jsonObject.has("nbt")) {
                        try {
                            CompoundTag compoundnbt = TagParser.parseTag(GsonHelper.convertToString(jsonObject.get("nbt"), "nbt"));
                            material.itemStacks.forEach(itemStack -> itemStack.setTag(compoundnbt));
                        } catch (CommandSyntaxException var7) {
                            throw new JsonSyntaxException("Encountered invalid nbt tag when parsing material: " + var7.getMessage());
                        }
                    }
                } else if (jsonObject.has("tag")) {
                    material.tagLocation = ItemTags.create(new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag")));
                }
                if (!jsonObject.has("type") && jsonObject.has("tag")) {
                    material.predicate = this.deserializeTagPredicate(jsonObject);
                } else {
                    JsonObject copy = jsonObject.deepCopy();
                    copy.remove("count");
                    material.predicate = ItemPredicateDeserializer.deserialize(copy);
                }
            }
            return material;
        }

        private ItemPredicate deserializeTagPredicate(JsonObject jsonObject) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag"));
            TagKey<Item> tagKey = ItemTags.create(resourceLocation);
            MinMaxBounds.Ints durability = MinMaxBounds.Ints.fromJson(jsonObject.get("durability"));
            EnchantmentPredicate[] enchantments = EnchantmentPredicate.fromJsonArray(jsonObject.get("enchantments"));
            EnchantmentPredicate[] storedEnchantments = EnchantmentPredicate.fromJsonArray(jsonObject.get("stored_enchantments"));
            NbtPredicate nbt = NbtPredicate.fromJson(jsonObject.get("nbt"));
            return new ItemPredicate(tagKey, null, MinMaxBounds.Ints.ANY, durability, enchantments, storedEnchantments, null, nbt);
        }
    }
}