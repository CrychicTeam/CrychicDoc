package com.rekindled.embers.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.augment.IAugment;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class AugmentPredicate extends ItemPredicate {

    public static final ResourceLocation ID = new ResourceLocation("embers", "augment");

    protected final IAugment augment;

    protected final int level;

    public AugmentPredicate(IAugment augment, int level) {
        this.augment = augment;
        this.level = level;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack == null ? false : AugmentUtil.hasHeat(stack) && AugmentUtil.getAugmentLevel(stack, this.augment) >= this.level;
    }

    @Override
    public JsonElement serializeToJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", ID.toString());
        json.addProperty("augment", this.augment.getName().toString());
        if (this.level != 1) {
            json.addProperty("level", this.level);
        }
        return json;
    }

    public static AugmentPredicate deserialize(JsonObject json) {
        IAugment augment = AugmentUtil.getAugment(new ResourceLocation(GsonHelper.getAsString(json, "augment")));
        int level = 1;
        if (json.has("level")) {
            level = GsonHelper.getAsInt(json, "level");
        }
        return new AugmentPredicate(augment, level);
    }
}