package mezz.jei.gui.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.common.util.MathUtil;
import mezz.jei.gui.ingredients.IngredientLookupState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Unmodifiable;

public class RecipeGuiLogic implements IRecipeGuiLogic {

    private final IRecipeManager recipeManager;

    private final IRecipeTransferManager recipeTransferManager;

    private final IRecipeLogicStateListener stateListener;

    private boolean initialState = true;

    private IngredientLookupState state;

    private final Stack<IngredientLookupState> history = new Stack();

    private final IFocusFactory focusFactory;

    public RecipeGuiLogic(IRecipeManager recipeManager, IRecipeTransferManager recipeTransferManager, IRecipeLogicStateListener stateListener, IFocusFactory focusFactory) {
        this.recipeManager = recipeManager;
        this.recipeTransferManager = recipeTransferManager;
        this.stateListener = stateListener;
        this.state = IngredientLookupState.createWithFocus(recipeManager, focusFactory.getEmptyFocusGroup());
        this.focusFactory = focusFactory;
    }

    @Override
    public boolean setFocus(IFocusGroup focuses) {
        IngredientLookupState state = IngredientLookupState.createWithFocus(this.recipeManager, focuses);
        List<IRecipeCategory<?>> recipeCategories = state.getRecipeCategories();
        if (recipeCategories.isEmpty()) {
            return false;
        } else {
            int recipeCategoryIndex = getRecipeCategoryIndexToShowFirst(recipeCategories, this.recipeTransferManager);
            state.setRecipeCategoryIndex(recipeCategoryIndex);
            this.setState(state, true);
            return true;
        }
    }

    @Nonnegative
    private static int getRecipeCategoryIndexToShowFirst(List<IRecipeCategory<?>> recipeCategories, IRecipeTransferManager recipeTransferManager) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null) {
            AbstractContainerMenu openContainer = player.f_36096_;
            if (openContainer != null) {
                for (int i = 0; i < recipeCategories.size(); i++) {
                    IRecipeCategory<?> recipeCategory = (IRecipeCategory<?>) recipeCategories.get(i);
                    Optional<? extends IRecipeTransferHandler<AbstractContainerMenu, ?>> recipeTransferHandler = recipeTransferManager.getRecipeTransferHandler(openContainer, recipeCategory);
                    if (recipeTransferHandler.isPresent()) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public boolean back() {
        if (this.history.empty()) {
            return false;
        } else {
            IngredientLookupState state = (IngredientLookupState) this.history.pop();
            this.setState(state, false);
            return true;
        }
    }

    @Override
    public void clearHistory() {
        while (!this.history.empty()) {
            this.history.pop();
        }
    }

    private void setState(IngredientLookupState state, boolean saveHistory) {
        if (saveHistory && !this.initialState) {
            this.history.push(this.state);
        }
        this.state = state;
        this.initialState = false;
        this.stateListener.onStateChange();
    }

    @Override
    public boolean setCategoryFocus() {
        IRecipeCategory<?> recipeCategory = this.getSelectedRecipeCategory();
        IngredientLookupState state = IngredientLookupState.createWithFocus(this.recipeManager, this.focusFactory.getEmptyFocusGroup());
        state.setRecipeCategory(recipeCategory);
        this.setState(state, true);
        return true;
    }

    @Override
    public boolean setCategoryFocus(List<RecipeType<?>> recipeTypes) {
        List<IRecipeCategory<?>> recipeCategories = this.recipeManager.createRecipeCategoryLookup().limitTypes(recipeTypes).get().toList();
        IngredientLookupState state = IngredientLookupState.createWithCategories(this.recipeManager, this.focusFactory, recipeCategories);
        if (state.getRecipeCategories().isEmpty()) {
            return false;
        } else {
            this.setState(state, true);
            return true;
        }
    }

    @Override
    public Stream<ITypedIngredient<?>> getRecipeCatalysts() {
        IRecipeCategory<?> category = this.getSelectedRecipeCategory();
        return this.getRecipeCatalysts(category);
    }

    @Override
    public Stream<ITypedIngredient<?>> getRecipeCatalysts(IRecipeCategory<?> recipeCategory) {
        RecipeType<?> recipeType = recipeCategory.getRecipeType();
        return this.recipeManager.createRecipeCatalystLookup(recipeType).get();
    }

    @Override
    public void setRecipesPerPage(int recipesPerPage) {
        if (this.state.getRecipesPerPage() != recipesPerPage) {
            this.state.setRecipesPerPage(recipesPerPage);
        }
    }

    @Override
    public IRecipeCategory<?> getSelectedRecipeCategory() {
        return this.state.getFocusedRecipes().getRecipeCategory();
    }

    @Unmodifiable
    @Override
    public List<IRecipeCategory<?>> getRecipeCategories() {
        return this.state.getRecipeCategories();
    }

    @Override
    public List<IRecipeLayoutDrawable<?>> getRecipeLayouts() {
        return this.getRecipeLayouts(this.state.getFocusedRecipes());
    }

    private <T> List<IRecipeLayoutDrawable<?>> getRecipeLayouts(FocusedRecipes<T> selectedRecipes) {
        List<IRecipeLayoutDrawable<?>> recipeLayouts = new ArrayList();
        IRecipeCategory<T> recipeCategory = selectedRecipes.getRecipeCategory();
        List<T> recipes = selectedRecipes.getRecipes();
        List<T> brokenRecipes = new ArrayList();
        int firstRecipeIndex = this.state.getRecipeIndex() - this.state.getRecipeIndex() % this.state.getRecipesPerPage();
        for (int recipeIndex = firstRecipeIndex; recipeIndex < recipes.size() && recipeLayouts.size() < this.state.getRecipesPerPage(); recipeIndex++) {
            T recipe = (T) recipes.get(recipeIndex);
            this.recipeManager.createRecipeLayoutDrawable(recipeCategory, recipe, this.state.getFocuses()).ifPresentOrElse(recipeLayouts::add, () -> brokenRecipes.add(recipe));
        }
        if (!brokenRecipes.isEmpty()) {
            RecipeType<T> recipeType = recipeCategory.getRecipeType();
            this.recipeManager.hideRecipes(recipeType, brokenRecipes);
        }
        return recipeLayouts;
    }

    @Override
    public void nextRecipeCategory() {
        this.state.nextRecipeCategory();
        this.stateListener.onStateChange();
    }

    @Override
    public void setRecipeCategory(IRecipeCategory<?> category) {
        if (this.state.setRecipeCategory(category)) {
            this.stateListener.onStateChange();
        }
    }

    @Override
    public boolean hasMultiplePages() {
        List<?> recipes = this.state.getFocusedRecipes().getRecipes();
        return recipes.size() > this.state.getRecipesPerPage();
    }

    @Override
    public void previousRecipeCategory() {
        this.state.previousRecipeCategory();
        this.stateListener.onStateChange();
    }

    @Override
    public void nextPage() {
        int recipeCount = this.recipeCount();
        this.state.setRecipeIndex(this.state.getRecipeIndex() + this.state.getRecipesPerPage());
        if (this.state.getRecipeIndex() >= recipeCount) {
            this.state.setRecipeIndex(0);
        }
        this.stateListener.onStateChange();
    }

    @Override
    public void previousPage() {
        this.state.setRecipeIndex(this.state.getRecipeIndex() - this.state.getRecipesPerPage());
        if (this.state.getRecipeIndex() < 0) {
            int pageCount = this.pageCount(this.state.getRecipesPerPage());
            this.state.setRecipeIndex((pageCount - 1) * this.state.getRecipesPerPage());
        }
        this.stateListener.onStateChange();
    }

    private int pageCount(int recipesPerPage) {
        int recipeCount = this.recipeCount();
        return recipeCount <= 1 ? 1 : MathUtil.divideCeil(recipeCount, recipesPerPage);
    }

    private int recipeCount() {
        List<?> recipes = this.state.getFocusedRecipes().getRecipes();
        return recipes.size();
    }

    @Override
    public String getPageString() {
        int pageIndex = MathUtil.divideCeil(this.state.getRecipeIndex() + 1, this.state.getRecipesPerPage());
        return pageIndex + "/" + this.pageCount(this.state.getRecipesPerPage());
    }

    @Override
    public boolean hasMultipleCategories() {
        return this.state.getRecipeCategories().size() > 1;
    }

    @Override
    public boolean hasAllCategories() {
        long categoryCount = this.recipeManager.createRecipeCategoryLookup().get().count();
        return (long) this.state.getRecipeCategories().size() == categoryCount;
    }
}