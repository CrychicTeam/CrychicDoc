package vectorwing.farmersdelight.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

public class ChanceResult {

    public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1.0F);

    private final ItemStack stack;

    private final float chance;

    public ChanceResult(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public float getChance() {
        return this.chance;
    }

    public ItemStack rollOutput(RandomSource rand, int fortuneLevel) {
        int outputAmount = this.stack.getCount();
        double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * (double) fortuneLevel;
        for (int roll = 0; roll < this.stack.getCount(); roll++) {
            if ((double) rand.nextFloat() > (double) this.chance + fortuneBonus) {
                outputAmount--;
            }
        }
        if (outputAmount == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack out = this.stack.copy();
            out.setCount(outputAmount);
            return out;
        }
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(this.stack.getItem());
        json.addProperty("item", resourceLocation.toString());
        int count = this.stack.getCount();
        if (count != 1) {
            json.addProperty("count", count);
        }
        if (this.stack.hasTag()) {
            json.add("nbt", new JsonParser().parse(this.stack.getTag().toString()));
        }
        if (this.chance != 1.0F) {
            json.addProperty("chance", this.chance);
        }
        return json;
    }

    public static ChanceResult deserialize(JsonElement je) {
        if (!je.isJsonObject()) {
            throw new JsonSyntaxException("Must be a json object");
        } else {
            JsonObject json = je.getAsJsonObject();
            String itemId = GsonHelper.getAsString(json, "item");
            int count = GsonHelper.getAsInt(json, "count", 1);
            float chance = GsonHelper.getAsFloat(json, "chance", 1.0F);
            ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);
            if (GsonHelper.isValidPrimitive(json, "nbt")) {
                try {
                    JsonElement element = json.get("nbt");
                    itemstack.setTag(TagParser.parseTag(element.isJsonObject() ? FarmersDelight.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
                } catch (CommandSyntaxException var7) {
                    var7.printStackTrace();
                }
            }
            return new ChanceResult(itemstack, chance);
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.getStack());
        buf.writeFloat(this.getChance());
    }

    public static ChanceResult read(FriendlyByteBuf buf) {
        return new ChanceResult(buf.readItem(), buf.readFloat());
    }
}