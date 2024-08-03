package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeBookPage {

    public static final int ITEMS_PER_PAGE = 20;

    private final List<RecipeButton> buttons = Lists.newArrayListWithCapacity(20);

    @Nullable
    private RecipeButton hoveredButton;

    private final OverlayRecipeComponent overlay = new OverlayRecipeComponent();

    private Minecraft minecraft;

    private final List<RecipeShownListener> showListeners = Lists.newArrayList();

    private List<RecipeCollection> recipeCollections = ImmutableList.of();

    private StateSwitchingButton forwardButton;

    private StateSwitchingButton backButton;

    private int totalPages;

    private int currentPage;

    private RecipeBook recipeBook;

    @Nullable
    private Recipe<?> lastClickedRecipe;

    @Nullable
    private RecipeCollection lastClickedRecipeCollection;

    public RecipeBookPage() {
        for (int $$0 = 0; $$0 < 20; $$0++) {
            this.buttons.add(new RecipeButton());
        }
    }

    public void init(Minecraft minecraft0, int int1, int int2) {
        this.minecraft = minecraft0;
        this.recipeBook = minecraft0.player.getRecipeBook();
        for (int $$3 = 0; $$3 < this.buttons.size(); $$3++) {
            ((RecipeButton) this.buttons.get($$3)).m_264152_(int1 + 11 + 25 * ($$3 % 5), int2 + 31 + 25 * ($$3 / 5));
        }
        this.forwardButton = new StateSwitchingButton(int1 + 93, int2 + 137, 12, 17, false);
        this.forwardButton.initTextureValues(1, 208, 13, 18, RecipeBookComponent.RECIPE_BOOK_LOCATION);
        this.backButton = new StateSwitchingButton(int1 + 38, int2 + 137, 12, 17, true);
        this.backButton.initTextureValues(1, 208, 13, 18, RecipeBookComponent.RECIPE_BOOK_LOCATION);
    }

    public void addListener(RecipeBookComponent recipeBookComponent0) {
        this.showListeners.remove(recipeBookComponent0);
        this.showListeners.add(recipeBookComponent0);
    }

    public void updateCollections(List<RecipeCollection> listRecipeCollection0, boolean boolean1) {
        this.recipeCollections = listRecipeCollection0;
        this.totalPages = (int) Math.ceil((double) listRecipeCollection0.size() / 20.0);
        if (this.totalPages <= this.currentPage || boolean1) {
            this.currentPage = 0;
        }
        this.updateButtonsForPage();
    }

    private void updateButtonsForPage() {
        int $$0 = 20 * this.currentPage;
        for (int $$1 = 0; $$1 < this.buttons.size(); $$1++) {
            RecipeButton $$2 = (RecipeButton) this.buttons.get($$1);
            if ($$0 + $$1 < this.recipeCollections.size()) {
                RecipeCollection $$3 = (RecipeCollection) this.recipeCollections.get($$0 + $$1);
                $$2.init($$3, this);
                $$2.f_93624_ = true;
            } else {
                $$2.f_93624_ = false;
            }
        }
        this.updateArrowButtons();
    }

    private void updateArrowButtons() {
        this.forwardButton.f_93624_ = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
        this.backButton.f_93624_ = this.totalPages > 1 && this.currentPage > 0;
    }

    public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, float float5) {
        if (this.totalPages > 1) {
            String $$6 = this.currentPage + 1 + "/" + this.totalPages;
            int $$7 = this.minecraft.font.width($$6);
            guiGraphics0.drawString(this.minecraft.font, $$6, int1 - $$7 / 2 + 73, int2 + 141, -1, false);
        }
        this.hoveredButton = null;
        for (RecipeButton $$8 : this.buttons) {
            $$8.m_88315_(guiGraphics0, int3, int4, float5);
            if ($$8.f_93624_ && $$8.m_198029_()) {
                this.hoveredButton = $$8;
            }
        }
        this.backButton.m_88315_(guiGraphics0, int3, int4, float5);
        this.forwardButton.m_88315_(guiGraphics0, int3, int4, float5);
        this.overlay.render(guiGraphics0, int3, int4, float5);
    }

    public void renderTooltip(GuiGraphics guiGraphics0, int int1, int int2) {
        if (this.minecraft.screen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
            guiGraphics0.renderComponentTooltip(this.minecraft.font, this.hoveredButton.getTooltipText(), int1, int2);
        }
    }

    @Nullable
    public Recipe<?> getLastClickedRecipe() {
        return this.lastClickedRecipe;
    }

    @Nullable
    public RecipeCollection getLastClickedRecipeCollection() {
        return this.lastClickedRecipeCollection;
    }

    public void setInvisible() {
        this.overlay.setVisible(false);
    }

    public boolean mouseClicked(double double0, double double1, int int2, int int3, int int4, int int5, int int6) {
        this.lastClickedRecipe = null;
        this.lastClickedRecipeCollection = null;
        if (this.overlay.isVisible()) {
            if (this.overlay.mouseClicked(double0, double1, int2)) {
                this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
                this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
            } else {
                this.overlay.setVisible(false);
            }
            return true;
        } else if (this.forwardButton.m_6375_(double0, double1, int2)) {
            this.currentPage++;
            this.updateButtonsForPage();
            return true;
        } else if (this.backButton.m_6375_(double0, double1, int2)) {
            this.currentPage--;
            this.updateButtonsForPage();
            return true;
        } else {
            for (RecipeButton $$7 : this.buttons) {
                if ($$7.m_6375_(double0, double1, int2)) {
                    if (int2 == 0) {
                        this.lastClickedRecipe = $$7.getRecipe();
                        this.lastClickedRecipeCollection = $$7.getCollection();
                    } else if (int2 == 1 && !this.overlay.isVisible() && !$$7.isOnlyOption()) {
                        this.overlay.init(this.minecraft, $$7.getCollection(), $$7.m_252754_(), $$7.m_252907_(), int3 + int5 / 2, int4 + 13 + int6 / 2, (float) $$7.getWidth());
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public void recipesShown(List<Recipe<?>> listRecipe0) {
        for (RecipeShownListener $$1 : this.showListeners) {
            $$1.recipesShown(listRecipe0);
        }
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public RecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    protected void listButtons(Consumer<AbstractWidget> consumerAbstractWidget0) {
        consumerAbstractWidget0.accept(this.forwardButton);
        consumerAbstractWidget0.accept(this.backButton);
        this.buttons.forEach(consumerAbstractWidget0);
    }
}