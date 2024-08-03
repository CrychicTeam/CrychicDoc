package com.simibubi.create.content.processing.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.Random;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ProcessingOutput {

    public static final ProcessingOutput EMPTY = new ProcessingOutput(ItemStack.EMPTY, 1.0F);

    private static final Random r = new Random();

    private final ItemStack stack;

    private final float chance;

    private Pair<ResourceLocation, Integer> compatDatagenOutput;

    public ProcessingOutput(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public ProcessingOutput(Pair<ResourceLocation, Integer> item, float chance) {
        this.stack = ItemStack.EMPTY;
        this.compatDatagenOutput = item;
        this.chance = chance;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public float getChance() {
        return this.chance;
    }

    public ItemStack rollOutput() {
        int outputAmount = this.stack.getCount();
        for (int roll = 0; roll < this.stack.getCount(); roll++) {
            if (r.nextFloat() > this.chance) {
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
        ResourceLocation resourceLocation = this.compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(this.stack.getItem()) : this.compatDatagenOutput.getFirst();
        json.addProperty("item", resourceLocation.toString());
        int count = this.compatDatagenOutput == null ? this.stack.getCount() : this.compatDatagenOutput.getSecond();
        if (count != 1) {
            json.addProperty("count", count);
        }
        if (this.stack.hasTag()) {
            json.add("nbt", JsonParser.parseString(this.stack.getTag().toString()));
        }
        if (this.chance != 1.0F) {
            json.addProperty("chance", this.chance);
        }
        return json;
    }

    public static ProcessingOutput deserialize(JsonElement je) {
        if (!je.isJsonObject()) {
            throw new JsonSyntaxException("ProcessingOutput must be a json object");
        } else {
            JsonObject json = je.getAsJsonObject();
            String itemId = GsonHelper.getAsString(json, "item");
            int count = GsonHelper.getAsInt(json, "count", 1);
            float chance = GsonHelper.isValidNode(json, "chance") ? GsonHelper.getAsFloat(json, "chance") : 1.0F;
            ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);
            if (GsonHelper.isValidNode(json, "nbt")) {
                try {
                    JsonElement element = json.get("nbt");
                    itemstack.setTag(TagParser.parseTag(element.isJsonObject() ? Create.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
                } catch (CommandSyntaxException var7) {
                    var7.printStackTrace();
                }
            }
            return new ProcessingOutput(itemstack, chance);
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.getStack());
        buf.writeFloat(this.getChance());
    }

    public static ProcessingOutput read(FriendlyByteBuf buf) {
        return new ProcessingOutput(buf.readItem(), buf.readFloat());
    }
}