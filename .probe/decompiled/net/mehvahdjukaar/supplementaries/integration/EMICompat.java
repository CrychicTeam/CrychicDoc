package net.mehvahdjukaar.supplementaries.integration;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import java.util.List;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.items.crafting.SpecialRecipeDisplays;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

@EmiEntrypoint
public class EMICompat implements EmiPlugin {

    public void register(EmiRegistry registry) {
        SpecialRecipeDisplays.registerCraftingRecipes(recipes -> recipes.stream().map(r -> new EmiCraftingRecipe(r.m_7527_().stream().map(EmiIngredient::of).toList(), EmiStack.of(r.m_8043_(null)), r.m_6423_(), r instanceof ShapelessRecipe)).forEach(registry::addRecipe));
        registry.addRecipe(EmiWorldInteractionRecipe.builder().id(Supplementaries.res("tilling/raked_gravel")).leftInput(EmiStack.of(Blocks.GRAVEL)).rightInput(EmiStack.of(Items.IRON_HOE), true).output(EmiStack.of((ItemLike) ModRegistry.RAKED_GRAVEL.get())).build());
        registry.addRecipe(new EMICompat.Grind(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, Supplementaries.res("unenchanted_golden_apple")));
        registry.addRecipe(new EMICompat.Grind((Item) ModRegistry.BOMB_BLUE_ITEM.get(), (Item) ModRegistry.BOMB_ITEM.get(), Supplementaries.res("unenchanted_golden_apple")));
        registry.addRecipe(EmiWorldInteractionRecipe.builder().id(Supplementaries.res("ash_burn")).leftInput(EmiIngredient.of(ItemTags.LOGS_THAT_BURN)).rightInput(EmiStack.EMPTY, false, slotWidget -> slotWidget.customBackground(new ResourceLocation("textures/block/stone.png"), 0, 0, 256, 1)).output(EmiStack.of((ItemLike) ModRegistry.ASH_BLOCK.get())).build());
        registry.setDefaultComparison(EmiStack.of((ItemLike) ModRegistry.BAMBOO_SPIKES_TIPPED_ITEM.get()), Comparison.compareNbt());
    }

    public static class Grind implements EmiRecipe {

        private static final ResourceLocation BACKGROUND = new ResourceLocation("minecraft", "textures/gui/container/grindstone.png");

        private final ResourceLocation id;

        private final EmiStack to;

        private final EmiStack from;

        public Grind(Item from, Item to, ResourceLocation id) {
            this.id = id;
            this.from = EmiStack.of(from);
            this.to = EmiStack.of(to);
        }

        public EmiRecipeCategory getCategory() {
            return VanillaEmiRecipeCategories.GRINDING;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public List<EmiIngredient> getInputs() {
            return List.of(this.from);
        }

        public List<EmiStack> getOutputs() {
            return List.of(this.to);
        }

        public boolean supportsRecipeTree() {
            return false;
        }

        public int getDisplayWidth() {
            return 116;
        }

        public int getDisplayHeight() {
            return 56;
        }

        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(BACKGROUND, 0, 0, 116, 56, 30, 15);
            widgets.addText(this.getExp(), 114, 39, -1, true).horizontalAlign(Alignment.END);
            widgets.addSlot(this.from, 18, 3).drawBack(false);
            widgets.addSlot(this.to, 98, 18).drawBack(false).recipeContext(this);
        }

        private FormattedCharSequence getExp() {
            int minPower = 500;
            int minXP = (int) Math.ceil((double) minPower / 2.0);
            int maxXP = 2 * minXP - 1;
            return EmiPort.ordered(EmiPort.translatable("emi.grinding.experience", new Object[] { minXP, maxXP }));
        }
    }
}