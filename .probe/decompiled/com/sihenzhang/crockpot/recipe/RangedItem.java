package com.sihenzhang.crockpot.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RangedItem {

    public final Item item;

    public final int min;

    public final int max;

    public RangedItem(Item item, int min, int max) {
        Preconditions.checkArgument(min >= 0 || max >= 0, "The count of RangedItem should not be less than 0");
        if (min == 0 && max == 0) {
            CrockPot.LOGGER.warn("The count of RangedItem is 0, make sure this is intentional!");
        }
        if (min > max) {
            CrockPot.LOGGER.warn("The minimum count of RangedItem is greater than the maximum count, make sure this is intentional!");
        }
        this.item = item;
        this.min = min;
        this.max = max;
    }

    public RangedItem(Item item, int count) {
        this(item, count, count);
    }

    public boolean isRanged() {
        return this.min != this.max;
    }

    public ItemStack getInstance(RandomSource random) {
        return this.isRanged() ? new ItemStack(this.item, Mth.nextInt(random, this.min, this.max)) : new ItemStack(this.item, this.min);
    }

    public static RangedItem fromJson(JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            JsonObject obj = GsonHelper.convertToJsonObject(json, "ranged item");
            Item item = JsonUtils.getAsItem(obj, "item");
            if (item == null) {
                return null;
            } else if (obj.has("count")) {
                JsonElement e = obj.get("count");
                if (e.isJsonObject()) {
                    JsonObject count = e.getAsJsonObject();
                    if (count.has("min") && count.has("max")) {
                        int min = GsonHelper.getAsInt(count, "min");
                        int max = GsonHelper.getAsInt(count, "max");
                        return new RangedItem(item, min, max);
                    } else {
                        int minOrMax = GsonHelper.getAsInt(count, "min", GsonHelper.getAsInt(count, "max", 1));
                        return new RangedItem(item, minOrMax);
                    }
                } else {
                    int count = GsonHelper.getAsInt(obj, "count", 1);
                    return new RangedItem(item, count);
                }
            } else {
                return new RangedItem(item, 1);
            }
        } else {
            throw new JsonSyntaxException("Json cannot be null");
        }
    }

    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", ForgeRegistries.ITEMS.getKey(this.item).toString());
        if (this.isRanged()) {
            JsonObject count = new JsonObject();
            count.addProperty("min", this.min);
            count.addProperty("max", this.max);
            obj.add("count", count);
        } else if (this.min > 1) {
            obj.addProperty("count", this.min);
        }
        return obj;
    }

    public static RangedItem fromNetwork(FriendlyByteBuf buffer) {
        Item item = Item.byId(buffer.readVarInt());
        int min = buffer.readByte();
        int max = buffer.readByte();
        return new RangedItem(item, min, max);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(Item.getId(this.item));
        buffer.writeByte(this.min);
        buffer.writeByte(this.max);
    }
}