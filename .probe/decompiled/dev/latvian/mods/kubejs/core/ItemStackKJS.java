package dev.latvian.mods.kubejs.core;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.Tags;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.JsonSerializable;
import dev.latvian.mods.rhino.mod.util.NBTSerializable;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import dev.latvian.mods.rhino.util.SpecialEquality;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ItemStackKJS extends SpecialEquality, NBTSerializable, JsonSerializable, IngredientSupplierKJS {

    default ItemStack kjs$self() {
        return (ItemStack) this;
    }

    @Override
    default boolean specialEquals(Object o, boolean shallow) {
        if (o instanceof CharSequence) {
            return this.kjs$getId().equals(UtilsJS.getID(o.toString()));
        } else {
            return o instanceof ItemStack s ? this.kjs$equalsIgnoringCount(s) : this.kjs$equalsIgnoringCount(ItemStackJS.of(o));
        }
    }

    default boolean kjs$equalsIgnoringCount(ItemStack stack) {
        ItemStack self = this.kjs$self();
        if (self == stack) {
            return true;
        } else {
            return self.isEmpty() ? stack.isEmpty() : ItemStack.isSameItemSameTags(self, stack);
        }
    }

    default ResourceLocation kjs$getIdLocation() {
        return this.kjs$self().getItem().kjs$getIdLocation();
    }

    default String kjs$getId() {
        return this.kjs$self().getItem().kjs$getId();
    }

    default Collection<ResourceLocation> kjs$getTags() {
        return (Collection<ResourceLocation>) Tags.byItem(this.kjs$self().getItem()).map(TagKey::f_203868_).collect(Collectors.toSet());
    }

    default boolean kjs$hasTag(ResourceLocation tag) {
        return this.kjs$self().is(Tags.item(tag));
    }

    default boolean kjs$isBlock() {
        return this.kjs$self().getItem() instanceof BlockItem;
    }

    default ItemStack kjs$withCount(int c) {
        if (c > 0 && !this.kjs$self().isEmpty()) {
            ItemStack is = this.kjs$self().copy();
            is.setCount(c);
            return is;
        } else {
            return ItemStack.EMPTY;
        }
    }

    default void kjs$removeTag() {
        this.kjs$self().setTag(null);
    }

    default String kjs$getNbtString() {
        return String.valueOf(this.kjs$self().getTag());
    }

    default ItemStack kjs$withNBT(CompoundTag nbt) {
        ItemStack is = this.kjs$self().copy();
        CompoundTag tag0 = is.getTag();
        if (tag0 == null) {
            is.setTag(nbt);
        } else {
            is.setTag(tag0.merge(nbt));
        }
        return is;
    }

    default ItemStack kjs$withName(@Nullable Component displayName) {
        ItemStack is = this.kjs$self().copy();
        if (displayName != null) {
            is.setHoverName(displayName);
        } else {
            is.resetHoverName();
        }
        return is;
    }

    default Map<String, Integer> kjs$getEnchantments() {
        HashMap<String, Integer> map = new HashMap();
        for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(this.kjs$self()).entrySet()) {
            ResourceLocation id = RegistryInfo.ENCHANTMENT.getId((Enchantment) entry.getKey());
            if (id != null) {
                map.put(id.toString(), (Integer) entry.getValue());
            }
        }
        return map;
    }

    default boolean kjs$hasEnchantment(Enchantment enchantment, int level) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, this.kjs$self()) >= level;
    }

    @RemapForJS("enchant")
    default ItemStack kjs$enchantCopy(Map<?, ?> enchantments) {
        ItemStack is = this.kjs$self();
        for (Entry<?, ?> entry : enchantments.entrySet()) {
            Enchantment enchantment = RegistryInfo.ENCHANTMENT.getValue(UtilsJS.getMCID(null, entry.getKey()));
            if (enchantment != null && entry.getValue() instanceof Number number) {
                is = is.kjs$enchantCopy(enchantment, number.intValue());
            }
        }
        return is;
    }

    @RemapForJS("enchant")
    default ItemStack kjs$enchantCopy(Enchantment enchantment, int level) {
        ItemStack is = this.kjs$self().copy();
        if (is.getItem() == Items.ENCHANTED_BOOK) {
            EnchantedBookItem.addEnchantment(is, new EnchantmentInstance(enchantment, level));
        } else {
            is.enchant(enchantment, level);
        }
        return is;
    }

    default String kjs$getMod() {
        return this.kjs$self().getItem().kjs$getMod();
    }

    @Deprecated
    default Ingredient kjs$ignoreNBT() {
        ConsoleJS console = ConsoleJS.getCurrent(ConsoleJS.SERVER);
        console.warn("You don't need to call .ignoreNBT() anymore, all item ingredients ignore NBT by default!");
        return this.kjs$self().getItem().kjs$asIngredient();
    }

    default Ingredient kjs$weakNBT() {
        return IngredientPlatformHelper.get().weakNBT(this.kjs$self());
    }

    default Ingredient kjs$strongNBT() {
        return IngredientPlatformHelper.get().strongNBT(this.kjs$self());
    }

    default boolean kjs$areItemsEqual(ItemStack other) {
        return this.kjs$self().getItem() == other.getItem();
    }

    default boolean kjs$isNBTEqual(ItemStack other) {
        if (this.kjs$self().hasTag() == other.hasTag()) {
            CompoundTag nbt = this.kjs$self().getTag();
            CompoundTag nbt2 = other.getTag();
            return Objects.equals(nbt, nbt2);
        } else {
            return false;
        }
    }

    default float kjs$getHarvestSpeed(@Nullable BlockContainerJS block) {
        return this.kjs$self().getDestroySpeed(block == null ? Blocks.AIR.defaultBlockState() : block.getBlockState());
    }

    default float kjs$getHarvestSpeed() {
        return this.kjs$getHarvestSpeed(null);
    }

    @RemapForJS("toNBT")
    default CompoundTag toNBTJS() {
        return this.kjs$self().save(new CompoundTag());
    }

    default CompoundTag kjs$getTypeData() {
        return this.kjs$self().getItem().kjs$getTypeData();
    }

    default String kjs$toItemString() {
        ItemStack is = this.kjs$self();
        StringBuilder builder = new StringBuilder();
        int count = is.getCount();
        boolean hasNbt = is.hasTag();
        if (count > 1 && !hasNbt) {
            builder.append('\'');
            builder.append(count);
            builder.append("x ");
            builder.append(this.kjs$getId());
            builder.append('\'');
        } else if (hasNbt) {
            builder.append("Item.of('");
            builder.append(is.kjs$getId());
            builder.append('\'');
            List<Pair<String, Integer>> enchants = null;
            if (count > 1) {
                builder.append(", ");
                builder.append(count);
            }
            CompoundTag t = is.getTag();
            if (t != null && !t.isEmpty()) {
                String key = is.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "Enchantments";
                if (t.contains(key, 9)) {
                    ListTag l = t.getList(key, 10);
                    enchants = new ArrayList(l.size());
                    for (int i = 0; i < l.size(); i++) {
                        CompoundTag t1 = l.getCompound(i);
                        enchants.add(Pair.of(t1.getString("id"), t1.getInt("lvl")));
                    }
                    t = t.copy();
                    t.remove(key);
                    if (t.isEmpty()) {
                        t = null;
                    }
                }
            }
            if (t != null) {
                builder.append(", ");
                NBTUtils.quoteAndEscapeForJS(builder, t.toString());
            }
            builder.append(')');
            if (enchants != null) {
                for (Pair<String, Integer> e : enchants) {
                    builder.append(".enchant('");
                    builder.append((String) e.getKey());
                    builder.append("', ");
                    builder.append(e.getValue());
                    builder.append(')');
                }
            }
        } else {
            builder.append('\'');
            builder.append(this.kjs$getId());
            builder.append('\'');
        }
        return builder.toString();
    }

    @Override
    default Ingredient kjs$asIngredient() {
        return this.kjs$self().getItem().kjs$asIngredient();
    }

    default JsonObject toJsonJS() {
        JsonObject json = new JsonObject();
        json.addProperty("item", this.kjs$getId());
        json.addProperty("count", this.kjs$self().getCount());
        CompoundTag tag = this.kjs$self().getTag();
        if (tag != null) {
            json.addProperty("nbt", tag.toString());
        }
        return json;
    }

    default OutputItem kjs$withChance(double chance) {
        return OutputItem.of(this.kjs$self(), chance);
    }

    default ItemStack kjs$withLore(Component[] text) {
        ItemStack is = this.kjs$self().copy();
        if (text.length > 0) {
            CompoundTag tag = is.getOrCreateTag();
            CompoundTag display = tag.getCompound("display");
            ListTag lore = display.getList("Lore", 8);
            for (Component t : text) {
                lore.add(StringTag.valueOf(Component.Serializer.toJson(t)));
            }
            display.put("Lore", lore);
            tag.put("display", display);
        }
        return is;
    }
}