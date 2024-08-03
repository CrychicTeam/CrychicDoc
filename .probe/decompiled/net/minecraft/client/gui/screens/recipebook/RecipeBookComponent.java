package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeBookComponent implements PlaceRecipe<Ingredient>, Renderable, GuiEventListener, NarratableEntry, RecipeShownListener {

    protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");

    private static final Component SEARCH_HINT = Component.translatable("gui.recipebook.search_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

    public static final int IMAGE_WIDTH = 147;

    public static final int IMAGE_HEIGHT = 166;

    private static final int OFFSET_X_POSITION = 86;

    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");

    private static final Component ALL_RECIPES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.all");

    private int xOffset;

    private int width;

    private int height;

    protected final GhostRecipe ghostRecipe = new GhostRecipe();

    private final List<RecipeBookTabButton> tabButtons = Lists.newArrayList();

    @Nullable
    private RecipeBookTabButton selectedTab;

    protected StateSwitchingButton filterButton;

    protected RecipeBookMenu<?> menu;

    protected Minecraft minecraft;

    @Nullable
    private EditBox searchBox;

    private String lastSearch = "";

    private ClientRecipeBook book;

    private final RecipeBookPage recipeBookPage = new RecipeBookPage();

    private final StackedContents stackedContents = new StackedContents();

    private int timesInventoryChanged;

    private boolean ignoreTextInput;

    private boolean visible;

    private boolean widthTooNarrow;

    public void init(int int0, int int1, Minecraft minecraft2, boolean boolean3, RecipeBookMenu<?> recipeBookMenu4) {
        this.minecraft = minecraft2;
        this.width = int0;
        this.height = int1;
        this.menu = recipeBookMenu4;
        this.widthTooNarrow = boolean3;
        minecraft2.player.f_36096_ = recipeBookMenu4;
        this.book = minecraft2.player.getRecipeBook();
        this.timesInventoryChanged = minecraft2.player.m_150109_().getTimesChanged();
        this.visible = this.isVisibleAccordingToBookData();
        if (this.visible) {
            this.initVisuals();
        }
    }

    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : 86;
        int $$0 = (this.width - 147) / 2 - this.xOffset;
        int $$1 = (this.height - 166) / 2;
        this.stackedContents.clear();
        this.minecraft.player.m_150109_().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        String $$2 = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.minecraft.font, $$0 + 26, $$1 + 14, 79, 9 + 3, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue($$2);
        this.searchBox.setHint(SEARCH_HINT);
        this.recipeBookPage.init(this.minecraft, $$0, $$1);
        this.recipeBookPage.addListener(this);
        this.filterButton = new StateSwitchingButton($$0 + 110, $$1 + 12, 26, 16, this.book.m_12689_(this.menu));
        this.updateFilterButtonTooltip();
        this.initFilterButtonTextures();
        this.tabButtons.clear();
        for (RecipeBookCategories $$3 : RecipeBookCategories.getCategories(this.menu.getRecipeBookType())) {
            this.tabButtons.add(new RecipeBookTabButton($$3));
        }
        if (this.selectedTab != null) {
            this.selectedTab = (RecipeBookTabButton) this.tabButtons.stream().filter(p_100329_ -> p_100329_.getCategory().equals(this.selectedTab.getCategory())).findFirst().orElse(null);
        }
        if (this.selectedTab == null) {
            this.selectedTab = (RecipeBookTabButton) this.tabButtons.get(0);
        }
        this.selectedTab.m_94635_(true);
        this.updateCollections(false);
        this.updateTabs();
    }

    private void updateFilterButtonTooltip() {
        this.filterButton.m_257544_(this.filterButton.isStateTriggered() ? Tooltip.create(this.getRecipeFilterName()) : Tooltip.create(ALL_RECIPES_TOOLTIP));
    }

    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(152, 41, 28, 18, RECIPE_BOOK_LOCATION);
    }

    public int updateScreenPosition(int int0, int int1) {
        int $$2;
        if (this.isVisible() && !this.widthTooNarrow) {
            $$2 = 177 + (int0 - int1 - 200) / 2;
        } else {
            $$2 = (int0 - int1) / 2;
        }
        return $$2;
    }

    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    public boolean isVisible() {
        return this.visible;
    }

    private boolean isVisibleAccordingToBookData() {
        return this.book.m_12691_(this.menu.getRecipeBookType());
    }

    protected void setVisible(boolean boolean0) {
        if (boolean0) {
            this.initVisuals();
        }
        this.visible = boolean0;
        this.book.m_12693_(this.menu.getRecipeBookType(), boolean0);
        if (!boolean0) {
            this.recipeBookPage.setInvisible();
        }
        this.sendUpdateSettings();
    }

    public void slotClicked(@Nullable Slot slot0) {
        if (slot0 != null && slot0.index < this.menu.getSize()) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
    }

    private void updateCollections(boolean boolean0) {
        List<RecipeCollection> $$1 = this.book.getCollection(this.selectedTab.getCategory());
        $$1.forEach(p_100381_ -> p_100381_.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.book));
        List<RecipeCollection> $$2 = Lists.newArrayList($$1);
        $$2.removeIf(p_100368_ -> !p_100368_.hasKnownRecipes());
        $$2.removeIf(p_100360_ -> !p_100360_.hasFitting());
        String $$3 = this.searchBox.getValue();
        if (!$$3.isEmpty()) {
            ObjectSet<RecipeCollection> $$4 = new ObjectLinkedOpenHashSet(this.minecraft.getSearchTree(SearchRegistry.RECIPE_COLLECTIONS).search($$3.toLowerCase(Locale.ROOT)));
            $$2.removeIf(p_100334_ -> !$$4.contains(p_100334_));
        }
        if (this.book.m_12689_(this.menu)) {
            $$2.removeIf(p_100331_ -> !p_100331_.hasCraftable());
        }
        this.recipeBookPage.updateCollections($$2, boolean0);
    }

    private void updateTabs() {
        int $$0 = (this.width - 147) / 2 - this.xOffset - 30;
        int $$1 = (this.height - 166) / 2 + 3;
        int $$2 = 27;
        int $$3 = 0;
        for (RecipeBookTabButton $$4 : this.tabButtons) {
            RecipeBookCategories $$5 = $$4.getCategory();
            if ($$5 == RecipeBookCategories.CRAFTING_SEARCH || $$5 == RecipeBookCategories.FURNACE_SEARCH) {
                $$4.f_93624_ = true;
                $$4.m_264152_($$0, $$1 + 27 * $$3++);
            } else if ($$4.updateVisibility(this.book)) {
                $$4.m_264152_($$0, $$1 + 27 * $$3++);
                $$4.startAnimation(this.minecraft);
            }
        }
    }

    public void tick() {
        boolean $$0 = this.isVisibleAccordingToBookData();
        if (this.isVisible() != $$0) {
            this.setVisible($$0);
        }
        if (this.isVisible()) {
            if (this.timesInventoryChanged != this.minecraft.player.m_150109_().getTimesChanged()) {
                this.updateStackedContents();
                this.timesInventoryChanged = this.minecraft.player.m_150109_().getTimesChanged();
            }
            this.searchBox.tick();
        }
    }

    private void updateStackedContents() {
        this.stackedContents.clear();
        this.minecraft.player.m_150109_().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        this.updateCollections(false);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.isVisible()) {
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
            int $$4 = (this.width - 147) / 2 - this.xOffset;
            int $$5 = (this.height - 166) / 2;
            guiGraphics0.blit(RECIPE_BOOK_LOCATION, $$4, $$5, 1, 1, 147, 166);
            this.searchBox.m_88315_(guiGraphics0, int1, int2, float3);
            for (RecipeBookTabButton $$6 : this.tabButtons) {
                $$6.m_88315_(guiGraphics0, int1, int2, float3);
            }
            this.filterButton.m_88315_(guiGraphics0, int1, int2, float3);
            this.recipeBookPage.render(guiGraphics0, $$4, $$5, int1, int2, float3);
            guiGraphics0.pose().popPose();
        }
    }

    public void renderTooltip(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        if (this.isVisible()) {
            this.recipeBookPage.renderTooltip(guiGraphics0, int3, int4);
            this.renderGhostRecipeTooltip(guiGraphics0, int1, int2, int3, int4);
        }
    }

    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    private void renderGhostRecipeTooltip(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        ItemStack $$5 = null;
        for (int $$6 = 0; $$6 < this.ghostRecipe.size(); $$6++) {
            GhostRecipe.GhostIngredient $$7 = this.ghostRecipe.get($$6);
            int $$8 = $$7.getX() + int1;
            int $$9 = $$7.getY() + int2;
            if (int3 >= $$8 && int4 >= $$9 && int3 < $$8 + 16 && int4 < $$9 + 16) {
                $$5 = $$7.getItem();
            }
        }
        if ($$5 != null && this.minecraft.screen != null) {
            guiGraphics0.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, $$5), int3, int4);
        }
    }

    public void renderGhostRecipe(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3, float float4) {
        this.ghostRecipe.render(guiGraphics0, this.minecraft, int1, int2, boolean3, float4);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.isVisible() && !this.minecraft.player.m_5833_()) {
            if (this.recipeBookPage.mouseClicked(double0, double1, int2, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
                Recipe<?> $$3 = this.recipeBookPage.getLastClickedRecipe();
                RecipeCollection $$4 = this.recipeBookPage.getLastClickedRecipeCollection();
                if ($$3 != null && $$4 != null) {
                    if (!$$4.isCraftable($$3) && this.ghostRecipe.getRecipe() == $$3) {
                        return false;
                    }
                    this.ghostRecipe.clear();
                    this.minecraft.gameMode.handlePlaceRecipe(this.minecraft.player.f_36096_.containerId, $$3, Screen.hasShiftDown());
                    if (!this.isOffsetNextToMainGUI()) {
                        this.setVisible(false);
                    }
                }
                return true;
            } else if (this.searchBox.m_6375_(double0, double1, int2)) {
                this.searchBox.setFocused(true);
                return true;
            } else {
                this.searchBox.setFocused(false);
                if (this.filterButton.m_6375_(double0, double1, int2)) {
                    boolean $$5 = this.toggleFiltering();
                    this.filterButton.setStateTriggered($$5);
                    this.updateFilterButtonTooltip();
                    this.sendUpdateSettings();
                    this.updateCollections(false);
                    return true;
                } else {
                    for (RecipeBookTabButton $$6 : this.tabButtons) {
                        if ($$6.m_6375_(double0, double1, int2)) {
                            if (this.selectedTab != $$6) {
                                if (this.selectedTab != null) {
                                    this.selectedTab.m_94635_(false);
                                }
                                this.selectedTab = $$6;
                                this.selectedTab.m_94635_(true);
                                this.updateCollections(true);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private boolean toggleFiltering() {
        RecipeBookType $$0 = this.menu.getRecipeBookType();
        boolean $$1 = !this.book.m_12704_($$0);
        this.book.m_12706_($$0, $$1);
        return $$1;
    }

    public boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4, int int5, int int6) {
        if (!this.isVisible()) {
            return true;
        } else {
            boolean $$7 = double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + int4) || double1 >= (double) (int3 + int5);
            boolean $$8 = (double) (int2 - 147) < double0 && double0 < (double) int2 && (double) int3 < double1 && double1 < (double) (int3 + int5);
            return $$7 && !$$8 && !this.selectedTab.m_198029_();
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        this.ignoreTextInput = false;
        if (!this.isVisible() || this.minecraft.player.m_5833_()) {
            return false;
        } else if (int0 == 256 && !this.isOffsetNextToMainGUI()) {
            this.setVisible(false);
            return true;
        } else if (this.searchBox.keyPressed(int0, int1, int2)) {
            this.checkSearchStringUpdate();
            return true;
        } else if (this.searchBox.m_93696_() && this.searchBox.isVisible() && int0 != 256) {
            return true;
        } else if (this.minecraft.options.keyChat.matches(int0, int1) && !this.searchBox.m_93696_()) {
            this.ignoreTextInput = true;
            this.searchBox.setFocused(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyReleased(int int0, int int1, int int2) {
        this.ignoreTextInput = false;
        return GuiEventListener.super.keyReleased(int0, int1, int2);
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        if (this.ignoreTextInput) {
            return false;
        } else if (!this.isVisible() || this.minecraft.player.m_5833_()) {
            return false;
        } else if (this.searchBox.charTyped(char0, int1)) {
            this.checkSearchStringUpdate();
            return true;
        } else {
            return GuiEventListener.super.charTyped(char0, int1);
        }
    }

    @Override
    public boolean isMouseOver(double double0, double double1) {
        return false;
    }

    @Override
    public void setFocused(boolean boolean0) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    private void checkSearchStringUpdate() {
        String $$0 = this.searchBox.getValue().toLowerCase(Locale.ROOT);
        this.pirateSpeechForThePeople($$0);
        if (!$$0.equals(this.lastSearch)) {
            this.updateCollections(false);
            this.lastSearch = $$0;
        }
    }

    private void pirateSpeechForThePeople(String string0) {
        if ("excitedze".equals(string0)) {
            LanguageManager $$1 = this.minecraft.getLanguageManager();
            String $$2 = "en_pt";
            LanguageInfo $$3 = $$1.getLanguage("en_pt");
            if ($$3 == null || $$1.getSelected().equals("en_pt")) {
                return;
            }
            $$1.setSelected("en_pt");
            this.minecraft.options.languageCode = "en_pt";
            this.minecraft.reloadResourcePacks();
            this.minecraft.options.save();
        }
    }

    private boolean isOffsetNextToMainGUI() {
        return this.xOffset == 86;
    }

    public void recipesUpdated() {
        this.updateTabs();
        if (this.isVisible()) {
            this.updateCollections(false);
        }
    }

    @Override
    public void recipesShown(List<Recipe<?>> listRecipe0) {
        for (Recipe<?> $$1 : listRecipe0) {
            this.minecraft.player.removeRecipeHighlight($$1);
        }
    }

    public void setupGhostRecipe(Recipe<?> recipe0, List<Slot> listSlot1) {
        ItemStack $$2 = recipe0.getResultItem(this.minecraft.level.m_9598_());
        this.ghostRecipe.setRecipe(recipe0);
        this.ghostRecipe.addIngredient(Ingredient.of($$2), ((Slot) listSlot1.get(0)).x, ((Slot) listSlot1.get(0)).y);
        this.m_135408_(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe0, recipe0.getIngredients().iterator(), 0);
    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> iteratorIngredient0, int int1, int int2, int int3, int int4) {
        Ingredient $$5 = (Ingredient) iteratorIngredient0.next();
        if (!$$5.isEmpty()) {
            Slot $$6 = (Slot) this.menu.f_38839_.get(int1);
            this.ghostRecipe.addIngredient($$5, $$6.x, $$6.y);
        }
    }

    protected void sendUpdateSettings() {
        if (this.minecraft.getConnection() != null) {
            RecipeBookType $$0 = this.menu.getRecipeBookType();
            boolean $$1 = this.book.m_12684_().isOpen($$0);
            boolean $$2 = this.book.m_12684_().isFiltering($$0);
            this.minecraft.getConnection().send(new ServerboundRecipeBookChangeSettingsPacket($$0, $$1, $$2));
        }
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return this.visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        List<NarratableEntry> $$1 = Lists.newArrayList();
        this.recipeBookPage.listButtons(p_170049_ -> {
            if (p_170049_.isActive()) {
                $$1.add(p_170049_);
            }
        });
        $$1.add(this.searchBox);
        $$1.add(this.filterButton);
        $$1.addAll(this.tabButtons);
        Screen.NarratableSearchResult $$2 = Screen.findNarratableWidget($$1, null);
        if ($$2 != null) {
            $$2.entry.m_142291_(narrationElementOutput0.nest());
        }
    }
}