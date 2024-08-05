package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import com.mojang.blaze3d.platform.InputConstants;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuest;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class QuestCategory implements IRecipeCategory<WrappedQuest> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("ftbquests:textures/gui/jei/quest.png");

    private final IDrawable background;

    private final IDrawable icon;

    public QuestCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 144, 74);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack((ItemLike) FTBQuestsItems.BOOK.get()));
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ftbquests.quests");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public RecipeType<WrappedQuest> getRecipeType() {
        return JEIRecipeTypes.QUEST;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, WrappedQuest recipe, IFocusGroup focuses) {
        int inputSize = Math.min(9, recipe.input.size());
        for (int i = 0; i < inputSize; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, i % 3 * 18 + 1, i / 3 * 18 + 21).addItemStacks((List<ItemStack>) recipe.input.get(i));
        }
        int outputSize = Math.min(9, recipe.output.size());
        for (int i = 0; i < outputSize; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, i % 3 * 18 + 90 + 1, i / 3 * 18 + 21).addItemStacks((List<ItemStack>) recipe.output.get(i));
        }
    }

    public void draw(WrappedQuest recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Component text = recipe.quest.getTitle().copy().withStyle(ChatFormatting.UNDERLINE);
        Font font = Minecraft.getInstance().font;
        int w = font.width(text);
        int x = (this.background.getWidth() - w) / 2;
        int y = 3;
        boolean highlight = mouseX >= (double) x && mouseY >= (double) y && mouseX < (double) (x + w) && mouseY < (double) (y + 9);
        graphics.drawString(font, text, x, y, highlight ? -5735842 : -12636637, false);
    }

    public boolean handleInput(WrappedQuest recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() == InputConstants.Type.MOUSE && mouseY >= 0.0 && mouseY < 20.0) {
            recipe.openQuestGui();
            return true;
        } else {
            return false;
        }
    }
}