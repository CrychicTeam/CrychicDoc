package mezz.jei.gui.recipes;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.gui.PageNavigation;
import mezz.jei.gui.input.IPaged;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.handlers.CombinedInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class RecipeGuiTabs implements IPaged {

    private static final int TAB_GUI_OVERLAP = 3;

    private static final int TAB_HORIZONTAL_INSET = 2;

    private static final int NAVIGATION_HEIGHT = 20;

    private final IRecipeGuiLogic recipeGuiLogic;

    private final List<RecipeGuiTab> tabs = new ArrayList();

    private final PageNavigation pageNavigation;

    private final Textures textures;

    private final IIngredientManager ingredientManager;

    private IUserInputHandler inputHandler;

    private ImmutableRect2i area = ImmutableRect2i.EMPTY;

    private int pageCount = 1;

    private int pageNumber = 0;

    private int categoriesPerPage = 1;

    public RecipeGuiTabs(IRecipeGuiLogic recipeGuiLogic, Textures textures, IIngredientManager ingredientManager) {
        this.recipeGuiLogic = recipeGuiLogic;
        this.pageNavigation = new PageNavigation(this, true, textures);
        this.textures = textures;
        this.ingredientManager = ingredientManager;
        this.inputHandler = this.pageNavigation.createInputHandler();
    }

    public void initLayout(ImmutableRect2i recipeGuiArea) {
        List<IRecipeCategory<?>> categories = this.recipeGuiLogic.getRecipeCategories();
        if (!categories.isEmpty()) {
            ImmutableRect2i tabsArea = recipeGuiArea.keepTop(24).moveUp(21).cropLeft(2).cropRight(2);
            this.categoriesPerPage = Math.min(tabsArea.getWidth() / 24, categories.size());
            int tabsWidth = this.categoriesPerPage * 24;
            this.area = tabsArea.keepLeft(tabsWidth);
            this.pageCount = MathUtil.divideCeil(categories.size(), this.categoriesPerPage);
            IRecipeCategory<?> currentCategory = this.recipeGuiLogic.getSelectedRecipeCategory();
            int categoryIndex = categories.indexOf(currentCategory);
            this.pageNumber = categoryIndex / this.categoriesPerPage;
            ImmutableRect2i navigationArea = tabsArea.keepTop(20).moveUp(22);
            this.pageNavigation.updateBounds(navigationArea);
            this.updateLayout();
        }
    }

    private void updateLayout() {
        this.tabs.clear();
        List<IUserInputHandler> inputHandlers = new ArrayList();
        List<IRecipeCategory<?>> categories = this.recipeGuiLogic.getRecipeCategories();
        int tabX = this.area.getX();
        int startIndex = this.pageNumber * this.categoriesPerPage;
        for (int i = 0; i < this.categoriesPerPage; i++) {
            int index = i + startIndex;
            if (index >= categories.size()) {
                break;
            }
            IRecipeCategory<?> category = (IRecipeCategory<?>) categories.get(index);
            RecipeGuiTab tab = new RecipeCategoryTab(this.recipeGuiLogic, category, this.textures, tabX, this.area.getY(), this.ingredientManager);
            this.tabs.add(tab);
            inputHandlers.add(tab);
            tabX += 24;
        }
        inputHandlers.add(this.pageNavigation.createInputHandler());
        this.inputHandler = new CombinedInputHandler(inputHandlers);
        this.pageNavigation.updatePageNumber();
    }

    public void draw(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, IModIdHelper modIdHelper) {
        IRecipeCategory<?> selectedCategory = this.recipeGuiLogic.getSelectedRecipeCategory();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RecipeGuiTab hovered = null;
        RenderSystem.disableDepthTest();
        for (RecipeGuiTab tab : this.tabs) {
            boolean selected = tab.isSelected(selectedCategory);
            tab.draw(selected, guiGraphics, mouseX, mouseY);
            if (tab.isMouseOver((double) mouseX, (double) mouseY)) {
                hovered = tab;
            }
        }
        RenderSystem.enableDepthTest();
        this.pageNavigation.draw(minecraft, guiGraphics, mouseX, mouseY, minecraft.getFrameTime());
        if (hovered != null) {
            List<Component> tooltip = hovered.getTooltip(modIdHelper);
            TooltipRenderer.drawHoveringText(guiGraphics, tooltip, mouseX, mouseY);
        }
    }

    public IUserInputHandler getInputHandler() {
        return this.inputHandler;
    }

    @Override
    public boolean nextPage() {
        if (this.hasNext()) {
            this.pageNumber++;
        } else {
            this.pageNumber = 0;
        }
        this.updateLayout();
        return true;
    }

    @Override
    public boolean hasNext() {
        return this.pageNumber + 1 < this.pageCount;
    }

    @Override
    public boolean previousPage() {
        if (this.hasPrevious()) {
            this.pageNumber--;
        } else {
            this.pageNumber = this.pageCount - 1;
        }
        this.updateLayout();
        return true;
    }

    @Override
    public boolean hasPrevious() {
        return this.pageNumber > 0;
    }

    @Override
    public int getPageCount() {
        return this.pageCount;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }
}