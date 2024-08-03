package com.mna.items.manaweaving;

import com.mna.api.items.TieredItem;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternSerializer;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemManaweavingPattern extends TieredItem {

    private static final String NBT_RECIPEID = "manaweave_pattern_id";

    public ItemManaweavingPattern() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ResourceLocation recipe = this.getRecipe(stack, worldIn);
        if (recipe != null) {
            tooltip.add(Component.translatable(recipe.toString()));
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        if (ManaweavingPatternSerializer.ALL_RECIPES.size() > 0) {
            setRecipe(stack, (ManaweavingPattern) ManaweavingPatternSerializer.ALL_RECIPES.values().iterator().next());
        }
        return stack;
    }

    @Nullable
    public ResourceLocation getRecipe(ItemStack stack, Level world) {
        if (stack.hasTag() && stack.getItem() == this) {
            CompoundTag nbt = stack.getTag();
            return !nbt.contains("manaweave_pattern_id") ? null : new ResourceLocation(nbt.get("manaweave_pattern_id").getAsString());
        } else {
            return null;
        }
    }

    public static void setRecipe(ItemStack stack, ManaweavingPattern recipe) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("manaweave_pattern_id", recipe.m_6423_().toString());
    }
}