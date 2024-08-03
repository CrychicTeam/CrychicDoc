package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AlchemistCauldronRecipeCategory implements IRecipeCategory<AlchemistCauldronJeiRecipe> {

    public static final RecipeType<AlchemistCauldronJeiRecipe> ALCHEMIST_CAULDRON_RECIPE_TYPE = RecipeType.create("irons_spellbooks", "alchemist_cauldron", AlchemistCauldronJeiRecipe.class);

    private final IDrawable background;

    private final IDrawable cauldron_block_icon;

    private final String inputSlotName = "inputSlot";

    private final String catalystSlotName = "catalystSlot";

    private final String outputSlotName = "outputSlot";

    private final int paddingBottom = 20;

    public AlchemistCauldronRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(JeiPlugin.ALCHEMIST_CAULDRON_GUI, 0, 0, 125, 18).addPadding(0, 20, 0, 0).build();
        this.cauldron_block_icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.ALCHEMIST_CAULDRON.get()));
    }

    @Override
    public RecipeType<AlchemistCauldronJeiRecipe> getRecipeType() {
        return ALCHEMIST_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return BlockRegistry.ALCHEMIST_CAULDRON.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.cauldron_block_icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AlchemistCauldronJeiRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inputs = recipe.inputs();
        List<ItemStack> catalysts = recipe.catalysts();
        List<ItemStack> outputs = recipe.outputs();
        IRecipeSlotBuilder leftInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(inputs).setSlotName("inputSlot");
        IRecipeSlotBuilder rightInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 54, 1).addItemStacks(catalysts).setSlotName("catalystSlot");
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStacks(outputs).setSlotName("outputSlot");
        if (inputs.size() == catalysts.size()) {
            if (inputs.size() == outputs.size()) {
                builder.createFocusLink(leftInputSlot, rightInputSlot, outputSlot);
            }
        } else if (inputs.size() == outputs.size() && catalysts.size() == 1) {
            builder.createFocusLink(leftInputSlot, outputSlot);
        } else if (catalysts.size() == outputs.size() && inputs.size() == 1) {
            builder.createFocusLink(rightInputSlot, outputSlot);
        }
    }

    public void draw(@NotNull AlchemistCauldronJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiHelper, double mouseX, double mouseY) {
        Optional<ItemStack> leftStack = recipeSlotsView.findSlotByName("inputSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional<ItemStack> rightStack = recipeSlotsView.findSlotByName("catalystSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional<ItemStack> outputStack = recipeSlotsView.findSlotByName("outputSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        guiHelper.pose().pushPose();
        guiHelper.pose().translate((float) (this.getWidth() / 2) - 11.2F, (float) (this.getHeight() / 2 - 2), 0.0F);
        guiHelper.pose().scale(1.4F, 1.4F, 1.4F);
        this.cauldron_block_icon.draw(guiHelper);
        guiHelper.pose().popPose();
        if (leftStack.isPresent() && ((ItemStack) leftStack.get()).is(ItemRegistry.SCROLL.get())) {
            String inputText = String.format("%s%%", (int) (ServerConfigs.SCROLL_RECYCLE_CHANCE.get() * 100.0));
            Font font = Minecraft.getInstance().font;
            int y = this.getHeight() / 2 - 14;
            int x = (this.getWidth() - font.width(inputText)) / 2;
            guiHelper.drawString(font, inputText, x, y, Math.min(ServerConfigs.SCROLL_RECYCLE_CHANCE.get(), 1.0) == 1.0 ? ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor());
        }
    }
}