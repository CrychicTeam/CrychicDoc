package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.LootCrateTextRenderer;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrate;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class LootCrateCategory implements IRecipeCategory<WrappedLootCrate> {

    private final IDrawable background;

    private final IDrawable icon;

    public LootCrateCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(144, 144);
        this.icon = new IDrawable() {

            @Override
            public int getWidth() {
                return 16;
            }

            @Override
            public int getHeight() {
                return 16;
            }

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                poseStack.translate((float) (xOffset + 8), (float) (yOffset + 8), 100.0F);
                List<WrappedLootCrate> crates = LootCrateRecipeManagerPlugin.INSTANCE.getWrappedLootCrates();
                if (!crates.isEmpty()) {
                    GuiHelper.drawItem(graphics, ((WrappedLootCrate) crates.get((int) (System.currentTimeMillis() / 1000L % (long) crates.size()))).crateStack, 0, true, null);
                } else {
                    GuiHelper.drawItem(graphics, new ItemStack((ItemLike) FTBQuestsItems.LOOTCRATE.get()), 0, true, null);
                }
                poseStack.popPose();
            }
        };
    }

    @Override
    public RecipeType<WrappedLootCrate> getRecipeType() {
        return JEIRecipeTypes.LOOT_CRATE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.ftbquests.lootcrates");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, WrappedLootCrate recipe, IFocusGroup focuses) {
        for (int slot = 0; slot < Math.min(48, recipe.outputs.size()); slot++) {
            int finalSlot = slot;
            builder.addSlot(RecipeIngredientRole.OUTPUT, slot % 8 * 18, slot / 8 * 18 + 36).addIngredients(() -> Ingredient.class, recipe.outputIngredients()).addTooltipCallback((recipeSlotView, tooltip) -> recipeSlotView.getDisplayedIngredient().flatMap(ingr -> ingr.getIngredient(VanillaTypes.ITEM_STACK)).ifPresent(stack -> {
                if (ItemStack.isSameItemSameTags(stack, (ItemStack) recipe.outputs.get(finalSlot))) {
                    String chanceStr = ChatFormatting.GOLD + WeightedReward.chanceString(((WeightedReward) recipe.sortedRewards.get(finalSlot)).getWeight(), recipe.crate.getTable().getTotalWeight(true));
                    tooltip.add(Component.translatable("jei.ftbquests.lootcrates.chance", chanceStr).withStyle(ChatFormatting.GRAY));
                }
            }));
        }
    }

    public void draw(WrappedLootCrate recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        LootCrateTextRenderer.drawText(graphics, recipe.crate, 0, this.background.getWidth());
    }
}