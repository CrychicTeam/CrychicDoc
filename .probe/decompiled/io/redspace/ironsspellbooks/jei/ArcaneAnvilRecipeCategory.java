package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ArcaneAnvilRecipeCategory implements IRecipeCategory<ArcaneAnvilRecipe> {

    public static final RecipeType<ArcaneAnvilRecipe> ARCANE_ANVIL_RECIPE_RECIPE_TYPE = RecipeType.create("irons_spellbooks", "arcane_anvil", ArcaneAnvilRecipe.class);

    private final IDrawable background;

    private final IDrawable icon;

    private final String leftSlotName = "leftSlot";

    private final String rightSlotName = "rightSlot";

    private final String outputSlotName = "outputSlot";

    private final int paddingBottom = 15;

    public ArcaneAnvilRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(JeiPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18).addPadding(0, 15, 0, 0).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.ARCANE_ANVIL_BLOCK.get()));
    }

    @Override
    public RecipeType<ArcaneAnvilRecipe> getRecipeType() {
        return ARCANE_ANVIL_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return BlockRegistry.ARCANE_ANVIL_BLOCK.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneAnvilRecipe recipe, IFocusGroup focuses) {
        ArcaneAnvilRecipe.Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> recipeitems = recipe.getRecipeItems();
        List<ItemStack> leftInputs = recipeitems.a();
        List<ItemStack> rightInputs = recipeitems.b();
        List<ItemStack> outputs = recipeitems.c();
        IRecipeSlotBuilder leftInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(leftInputs).setSlotName("leftSlot");
        IRecipeSlotBuilder rightInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addItemStacks(rightInputs).setSlotName("rightSlot");
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStacks(outputs).setSlotName("outputSlot");
        if (leftInputs.size() == rightInputs.size()) {
            if (leftInputs.size() == outputs.size()) {
                builder.createFocusLink(leftInputSlot, rightInputSlot, outputSlot);
            }
        } else if (leftInputs.size() == outputs.size() && rightInputs.size() == 1) {
            builder.createFocusLink(leftInputSlot, outputSlot);
        } else if (rightInputs.size() == outputs.size() && leftInputs.size() == 1) {
            builder.createFocusLink(rightInputSlot, outputSlot);
        }
    }

    public void draw(ArcaneAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Optional<ItemStack> leftStack = recipeSlotsView.findSlotByName("leftSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional<ItemStack> rightStack = recipeSlotsView.findSlotByName("rightSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional<ItemStack> outputStack = recipeSlotsView.findSlotByName("outputSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        if (!leftStack.isEmpty() && !rightStack.isEmpty() && !outputStack.isEmpty()) {
            if (((ItemStack) leftStack.get()).getItem() instanceof Scroll leftScroll && ((ItemStack) rightStack.get()).getItem() instanceof Scroll rightScroll && ((ItemStack) outputStack.get()).getItem() instanceof Scroll outputScroll) {
                Minecraft minecraft = Minecraft.getInstance();
                this.drawScrollInfo(minecraft, guiGraphics, ISpellContainer.get((ItemStack) leftStack.get()), ISpellContainer.get((ItemStack) outputStack.get()));
            }
        }
    }

    private void drawScrollInfo(Minecraft minecraft, GuiGraphics guiGraphics, ISpellContainer leftScroll, ISpellContainer outputScroll) {
        SpellData inputSpellData = leftScroll.getSpellAtIndex(0);
        String inputText = String.format("L%d", inputSpellData.getLevel());
        int inputColor = inputSpellData.getSpell().getRarity(inputSpellData.getLevel()).getChatFormatting().getColor();
        SpellData outputSpellData = outputScroll.getSpellAtIndex(0);
        String outputText = String.format("L%d", outputSpellData.getLevel());
        int outputColor = outputSpellData.getSpell().getRarity(outputSpellData.getLevel()).getChatFormatting().getColor();
        int y = this.getHeight() / 2 + 7 + 9 / 2 - 4;
        int x = 3;
        guiGraphics.drawString(minecraft.font, inputText, x, y, inputColor);
        x += 50;
        guiGraphics.drawString(minecraft.font, inputText, x, y, inputColor);
        int outputWidth = minecraft.font.width(outputText);
        guiGraphics.drawString(minecraft.font, outputText, this.getWidth() - (outputWidth + 3), y, outputColor);
    }
}