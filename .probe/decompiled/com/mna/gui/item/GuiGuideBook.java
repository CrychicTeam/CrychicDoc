package com.mna.gui.item;

import com.google.common.collect.Lists;
import com.mna.Registries;
import com.mna.api.capabilities.CodexBreadcrumb;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.config.ClientConfigValues;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.config.ClientConfig;
import com.mna.gui.GuiTextures;
import com.mna.gui.HUDOverlayRenderer;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.item.ContainerGuideBook;
import com.mna.gui.widgets.ImageItemStackButton;
import com.mna.guide.EntryCategory;
import com.mna.guide.GuideBookEntries;
import com.mna.guide.GuidebookEntry;
import com.mna.guide.RelatedRecipe;
import com.mna.guide.interfaces.IEntrySection;
import com.mna.guide.recipe.RecipeBlank;
import com.mna.recipes.AMRecipeBase;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

@OnlyIn(Dist.CLIENT)
public class GuiGuideBook extends GuiJEIDisable<ContainerGuideBook> {

    private static final int X_PAGE_LEFT = 15;

    private static final int X_PAGE_RIGHT = 133;

    public static final int X_MAX = 108;

    public static final int Y_PAGE_START = 10;

    public static final int Y_MAX = 168;

    GuidebookEntry currentEntry = null;

    ImageButton prevPage = null;

    ImageButton nextPage = null;

    ImageButton back = null;

    ImageButton switchConfig_HUD = null;

    ImageButton switchConfig_HUDPOS = null;

    ImageButton switchConfig_BACK = null;

    ImageButton switchConfig_PINSIZE = null;

    ImageButton search = null;

    Component searchTerm;

    EditBox searchBox;

    RecipeBlank searchBackground;

    RecipeRendererBase currentRecipe = null;

    ImageButton closeRecipe = null;

    ImageButton pinRecipe = null;

    int page = 0;

    int max_pages = 1;

    boolean isSearching = false;

    IPlayerProgression progression = null;

    static List<Component> currentTooltip = new ArrayList();

    private ArrayList<ImageButton> relatedTabs;

    private ArrayList<GuiGuideBook.CategoryItemButton> categoryButtons;

    private ArrayList<GuiGuideBook.CodexSearchResult> searchResults;

    private HashMap<Integer, Collection<AbstractWidget>> pageWidgets;

    private EntryCategory currentCategory = EntryCategory.BASICS;

    public GuiGuideBook(ContainerGuideBook container, Inventory playerInventory, Component textComponent) {
        super(container, playerInventory, textComponent);
        this.f_97726_ = 256;
        this.f_97727_ = 178;
        this.relatedTabs = new ArrayList();
        this.categoryButtons = new ArrayList();
        this.searchResults = new ArrayList();
        this.pageWidgets = new HashMap();
        if (this.f_96541_ == null) {
            this.f_96541_ = Minecraft.getInstance();
        }
        this.progression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
    }

    @Override
    protected void init() {
        super.m_7856_();
        GuideBookEntries.INSTANCE.reload();
        this.categoryButtons.clear();
        int i = this.f_97735_;
        int j = this.f_97736_;
        this.prevPage = new GuiGuideBook.PaperImageButton(i + 10, j + 158, 14, 14, 0, 0, 14, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.decrementPage());
        this.nextPage = new GuiGuideBook.PaperImageButton(i + 233, j + 158, 14, 14, 14, 0, 14, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.incrementPage());
        this.prevPage.f_93623_ = false;
        this.prevPage.f_93624_ = false;
        this.m_142416_(this.prevPage);
        this.m_142416_(this.nextPage);
        this.search = new GuiGuideBook.PaperImageButton(i + 15, j + this.f_97727_ - 6, 18, 28, 118, 0, 28, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.toggleSearch()).setTooltip(Component.translatable("gui.mna.codex_search"));
        this.searchTerm = Component.translatable("gui.mna.codex_search");
        this.searchBox = new EditBox(this.f_96541_.font, i + this.f_97726_ / 2 - 80, j - 33, 160, 20, this.searchTerm);
        this.searchBox.setMaxLength(60);
        this.searchBox.setResponder(this::runSearch);
        this.searchBackground = new RecipeBlank(this.f_97735_ + (this.f_97726_ - 218) / 2, this.f_97736_ + (this.f_97727_ - 256) / 2);
        this.m_142416_(this.search);
        this.back = new GuiGuideBook.PaperImageButton(i - 20, j + this.f_97727_ / 2 - 14, 20, 38, 84, 0, 38, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.displayBreadcrumb(false));
        this.m_142416_(this.back);
        this.switchConfig_HUD = new GuiGuideBook.PaperImageButton(i + 35, j + this.f_97727_ - 6, 18, 28, 136, 0, 28, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> {
            int cur = ClientConfigValues.CodexBackMode.ordinal();
            ClientConfig.setCodexBackStyle((cur + 1) % ClientConfigValues.CodexMode.values().length);
            ClientConfig.bakeConfig();
            ((GuiGuideBook.PaperImageButton) button).setTooltip(Component.translatable(ClientConfigValues.CodexBackMode.getLocalizationKey()));
        }).setTooltip(Component.translatable(ClientConfigValues.CodexBackMode.getLocalizationKey()));
        this.switchConfig_HUDPOS = new GuiGuideBook.PaperImageButton(i + 55, j + this.f_97727_ - 6, 18, 28, 172, 0, 28, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> {
            int cur = ClientConfigValues.HudPosition.ordinal();
            ClientConfig.setHudPosition((cur + 1) % ClientConfigValues.HudPos.values().length);
            ClientConfig.bakeConfig();
            ((GuiGuideBook.PaperImageButton) button).setTooltip(Component.translatable(ClientConfigValues.HudPosition.getLocalizationKey()));
        }).setTooltip(Component.translatable(ClientConfigValues.HudPosition.getLocalizationKey()));
        this.switchConfig_PINSIZE = new GuiGuideBook.PaperImageButton(i + 75, j + this.f_97727_ - 6, 18, 28, 190, 0, 28, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> {
            int cur = ClientConfigValues.PinnedRecipeScale.ordinal();
            ClientConfig.setPinnedRecipeScale((cur + 1) % ClientConfigValues.HudMode.values().length);
            ClientConfig.bakeConfig();
            ((GuiGuideBook.PaperImageButton) button).setTooltip(Component.translatable(ClientConfigValues.PinnedRecipeScale.getLocalizationKey()));
        }).setTooltip(Component.translatable(ClientConfigValues.PinnedRecipeScale.getLocalizationKey()));
        this.switchConfig_BACK = new GuiGuideBook.PaperImageButton(i + 95, j + this.f_97727_ - 6, 18, 28, 154, 0, 28, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> {
            int cur = ClientConfigValues.ShowHudMode.ordinal();
            ClientConfig.setHudMode((cur + 1) % ClientConfigValues.HudMode.values().length);
            ClientConfig.bakeConfig();
            ((GuiGuideBook.PaperImageButton) button).setTooltip(Component.translatable(ClientConfigValues.ShowHudMode.getLocalizationKey()));
        }).setTooltip(Component.translatable(ClientConfigValues.ShowHudMode.getLocalizationKey()));
        this.m_142416_(this.switchConfig_HUD);
        this.m_142416_(this.switchConfig_HUDPOS);
        this.m_142416_(this.switchConfig_PINSIZE);
        this.m_142416_(this.switchConfig_BACK);
        this.closeRecipe = new GuiGuideBook.PaperImageButton(i + 210, j - 30, 14, 14, 104, 0, 14, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.displayBreadcrumb(false));
        this.pinRecipe = new ImageButton(i + 30, j - 30, 14, 14, 104, 43, 14, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> {
            if (HUDOverlayRenderer.instance.getPinnedrecipe() != null && HUDOverlayRenderer.instance.getPinnedrecipe().equals(this.currentRecipe)) {
                HUDOverlayRenderer.instance.setPinnedRecipe(null);
            } else {
                HUDOverlayRenderer.instance.setPinnedRecipe(this.currentRecipe.clone(0, 0, true));
            }
        });
        this.displayBreadcrumb(false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((ClientConfigValues.CodexBackMode == ClientConfigValues.CodexMode.EscUIBack || ClientConfigValues.CodexBackMode == ClientConfigValues.CodexMode.RMouseUIEscBack) && keyCode == 256) {
            if (this.isSearching) {
                this.clearSearch(false);
            } else if (this.currentRecipe != null) {
                this.clearDisplayedRecipe(false);
            }
            this.displayBreadcrumb(false);
            return true;
        } else if (this.isSearching && keyCode != 256 && this.m_7222_() != null) {
            this.m_7222_().keyPressed(keyCode, scanCode, modifiers);
            return true;
        } else {
            return super.m_7933_(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((ClientConfigValues.CodexBackMode == ClientConfigValues.CodexMode.RMouseUIBackEscOut || ClientConfigValues.CodexBackMode == ClientConfigValues.CodexMode.RMouseUIEscBack) && button == 1) {
            if (this.isSearching) {
                if (this.searchBox.isMouseOver(mouseX, mouseY)) {
                    this.searchBox.setValue("");
                    for (GuiGuideBook.CodexSearchResult r : this.searchResults) {
                        this.m_169411_(r);
                    }
                    this.searchResults.clear();
                    return true;
                }
                this.clearSearch(false);
            } else if (this.currentRecipe != null) {
                this.clearDisplayedRecipe(false);
            }
            this.displayBreadcrumb(false);
            return true;
        } else {
            return super.m_6375_(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.currentRecipe == null && !this.isSearching) {
            if (delta > 0.0) {
                this.decrementPage();
            } else {
                this.incrementPage();
            }
            return true;
        } else {
            return super.m_6050_(mouseX, mouseY, delta);
        }
    }

    private void toggleSearch() {
        if (this.isSearching) {
            this.clearSearch(true);
        } else {
            this.showSearch(true);
        }
    }

    private void showSearch(boolean pushBreadcrumb) {
        if (pushBreadcrumb) {
            this.pushCategoryBreadcrumb();
            this.clearDisplayedRecipe(true);
        }
        for (Renderable widget : this.f_169369_) {
            if (widget instanceof AbstractWidget) {
                ((AbstractWidget) widget).active = false;
            }
        }
        for (AbstractWidget widgetx : (Collection) this.pageWidgets.getOrDefault(this.page, new ArrayList())) {
            if (widgetx instanceof AbstractWidget && !this.relatedTabs.contains(widgetx)) {
                widgetx.active = false;
                widgetx.visible = false;
            }
        }
        for (AbstractWidget widgetxx : (Collection) this.pageWidgets.getOrDefault(this.page + 1, new ArrayList())) {
            if (widgetxx instanceof AbstractWidget && !this.relatedTabs.contains(widgetxx)) {
                widgetxx.active = false;
                widgetxx.visible = false;
            }
        }
        this.m_142416_(this.searchBackground);
        this.m_142416_(this.searchBox);
        this.m_142416_(this.closeRecipe);
        DelayedEventQueue.pushEvent(this.f_96541_.level, new TimedDelayedEvent<>("focus", 10, "focus", this::fixFocus));
        this.isSearching = true;
    }

    private void clearSearch(boolean pushBreadcrumb) {
        if (pushBreadcrumb) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.SEARCH, this.searchBox.getValue());
        }
        this.m_169411_(this.searchBox);
        this.m_169411_(this.searchBackground);
        this.m_169411_(this.closeRecipe);
        for (GuiGuideBook.CodexSearchResult r : this.searchResults) {
            this.m_169411_(r);
        }
        this.searchResults.clear();
        this.searchBox.setValue("");
        for (Renderable widget : this.f_169369_) {
            if (widget instanceof AbstractWidget) {
                ((AbstractWidget) widget).active = true;
            }
        }
        for (AbstractWidget widgetx : (Collection) this.pageWidgets.getOrDefault(this.page, new ArrayList())) {
            if (widgetx instanceof AbstractWidget && !this.relatedTabs.contains(widgetx)) {
                widgetx.active = true;
                widgetx.visible = true;
            }
        }
        for (AbstractWidget widgetxx : (Collection) this.pageWidgets.getOrDefault(this.page + 1, new ArrayList())) {
            if (widgetxx instanceof AbstractWidget && !this.relatedTabs.contains(widgetxx)) {
                widgetxx.active = true;
                widgetxx.visible = true;
            }
        }
        this.isSearching = false;
    }

    private void runSearch(String term) {
        if (term.length() >= 3) {
            for (GuiGuideBook.CodexSearchResult r : this.searchResults) {
                this.m_169411_(r);
            }
            this.searchResults.clear();
            MutableInt yCoord = new MutableInt(this.f_97736_ - 10);
            for (GuideBookEntries.GuidebookSearchResult e : GuideBookEntries.INSTANCE.searchEntries(term, this.progression.getTier(), 24)) {
                if (e.isEntry()) {
                    GuiGuideBook.CodexSearchResult csr = new GuiGuideBook.CodexSearchResult(this.f_97735_ + 35, yCoord.getValue(), e.getEntry().getCategory().getDisplayStack().getHoverName().getString() + " > " + e.getEntry().getFirstTitle(), button -> {
                        this.clearSearch(true);
                        this.setCurrentEntry(e.getEntry().getName(), false);
                    });
                    this.m_142416_(csr);
                    this.searchResults.add(csr);
                    yCoord.add(csr.m_93694_() + 2);
                } else if (e.isRecipe()) {
                    MutableComponent ttc = Component.translatable("gui.mna.codex_search.recipe." + e.getRecipe().getType());
                    GuiGuideBook.CodexSearchResult csr;
                    if (!e.getOutputStack().isEnchanted()) {
                        csr = new GuiGuideBook.CodexSearchResult(this.f_97735_ + 35, yCoord.getValue(), ttc.append(e.getOutputStack().getHoverName()).getString(), button -> {
                            this.clearSearch(true);
                            this.displayRecipe(e.getRecipe(), false);
                        });
                    } else {
                        Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(e.getOutputStack());
                        for (Entry<Enchantment, Integer> enchantment : enchs.entrySet()) {
                            ttc.append(((Enchantment) enchantment.getKey()).getFullname((Integer) enchantment.getValue()));
                        }
                        csr = new GuiGuideBook.CodexSearchResult(this.f_97735_ + 35, yCoord.getValue(), ttc.getString(), button -> {
                            this.clearSearch(true);
                            this.displayRecipe(e.getRecipe(), false);
                        });
                    }
                    this.m_142416_(csr);
                    this.searchResults.add(csr);
                    yCoord.add(csr.m_93694_() + 2);
                }
                if (yCoord.intValue() > this.f_97736_ + 200 - 9) {
                    break;
                }
            }
        }
    }

    private ItemStack lookupResource(ResourceLocation rLoc) {
        Item item = ForgeRegistries.ITEMS.getValue(rLoc);
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        } else {
            Block block = ForgeRegistries.BLOCKS.getValue(rLoc);
            return block != null && block != Blocks.AIR ? new ItemStack(block) : ItemStack.EMPTY;
        }
    }

    private ISpellComponent lookupSpell(ResourceLocation rLoc) {
        ISpellComponent output = null;
        output = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(rLoc);
        if (output != null) {
            return output;
        } else {
            output = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(rLoc);
            return output != null ? output : (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc);
        }
    }

    private void fixFocus(String identifier, String data) {
        this.searchBox.setFocused(true);
        this.m_7522_(this.searchBox);
    }

    private void displayRecipe(RelatedRecipe rr, boolean pushBreadcrumb) {
        if (pushBreadcrumb) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.ENTRY, this.currentEntry.getName());
        }
        this.clearDisplayedRecipe(true);
        this.currentRecipe = rr.constructRenderer(this.f_97735_ + (this.f_97726_ - 218) / 2, this.f_97736_ + (this.f_97727_ - 256) / 2, this::setTooltip);
        if (this.currentRecipe != null) {
            for (Renderable widget : this.f_169369_) {
                if (widget instanceof AbstractWidget && !this.relatedTabs.contains(widget)) {
                    ((AbstractWidget) widget).active = false;
                }
            }
            for (AbstractWidget widgetx : (Collection) this.pageWidgets.getOrDefault(this.page, new ArrayList())) {
                if (widgetx instanceof AbstractWidget && !this.relatedTabs.contains(widgetx)) {
                    widgetx.active = false;
                    widgetx.visible = false;
                }
            }
            for (AbstractWidget widgetxx : (Collection) this.pageWidgets.getOrDefault(this.page + 1, new ArrayList())) {
                if (widgetxx instanceof AbstractWidget && !this.relatedTabs.contains(widgetxx)) {
                    widgetxx.active = false;
                    widgetxx.visible = false;
                }
            }
            this.m_142416_(this.currentRecipe);
            this.m_142416_(this.closeRecipe);
            this.m_142416_(this.pinRecipe);
        }
    }

    private void clearDisplayedRecipe(boolean pushBreadcrumb) {
        if (this.currentRecipe != null) {
            if (pushBreadcrumb) {
                CodexBreadcrumb lastBreadcrumb = this.progression.peekCodexBreadcrumb();
                if (lastBreadcrumb.Type == CodexBreadcrumb.Type.RECIPE) {
                    this.progression.popCodexBreadcrumb();
                }
                this.pushBreadcrumb(CodexBreadcrumb.Type.RECIPE, this.currentRecipe.getRegistryName().toString(), (String[]) ((List) Arrays.asList(this.currentRecipe.getRecipeIds()).stream().map(rLoc -> rLoc.toString()).collect(Collectors.toList())).toArray(new String[0]));
            }
            this.m_169411_(this.currentRecipe);
            this.m_169411_(this.closeRecipe);
            this.m_169411_(this.pinRecipe);
            for (Renderable widget : this.f_169369_) {
                if (widget instanceof AbstractWidget) {
                    ((AbstractWidget) widget).active = true;
                }
            }
            for (AbstractWidget widgetx : (Collection) this.pageWidgets.getOrDefault(this.page, new ArrayList())) {
                if (widgetx instanceof AbstractWidget && !this.relatedTabs.contains(widgetx)) {
                    widgetx.active = true;
                    widgetx.visible = true;
                }
            }
            for (AbstractWidget widgetxx : (Collection) this.pageWidgets.getOrDefault(this.page + 1, new ArrayList())) {
                if (widgetxx instanceof AbstractWidget && !this.relatedTabs.contains(widgetxx)) {
                    widgetxx.active = true;
                    widgetxx.visible = true;
                }
            }
            this.currentRecipe = null;
        }
    }

    private void lookupAndShowRecipe(String recipeId, boolean pushBreadcrumb) {
        ResourceLocation search = new ResourceLocation(recipeId);
        for (Entry<String, GuidebookEntry> e : GuideBookEntries.INSTANCE.getAllEntries()) {
            for (RelatedRecipe rr : ((GuidebookEntry) e.getValue()).getRelatedRecipes()) {
                for (ResourceLocation s : rr.getResourceLocations()) {
                    if (s.equals(search)) {
                        this.displayRecipe(rr, pushBreadcrumb);
                        return;
                    }
                }
            }
        }
    }

    private void decrementPage() {
        if (!this.hasOverlay()) {
            this.page -= 2;
            if (this.max_pages > 1) {
                this.nextPage.f_93624_ = true;
                this.nextPage.f_93623_ = true;
            }
            if (this.page <= 0) {
                this.page = 0;
                this.prevPage.f_93624_ = false;
                this.prevPage.f_93623_ = false;
            }
        }
    }

    private void incrementPage() {
        if (!this.hasOverlay()) {
            this.page += 2;
            if (this.max_pages > 1) {
                this.prevPage.f_93624_ = true;
                this.prevPage.f_93623_ = true;
            }
            if (this.page >= this.max_pages - 1) {
                this.page = this.max_pages - 1;
                if (this.page % 2 == 1) {
                    this.page++;
                }
                this.nextPage.f_93624_ = false;
                this.nextPage.f_93623_ = false;
            }
        }
    }

    private void pushCategoryBreadcrumb() {
        if (this.currentCategory != null && this.currentRecipe == null && !this.isSearching) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.CATEGORY, this.currentCategory.toString());
        }
    }

    private void pushBreadcrumb(CodexBreadcrumb.Type type, String key, String... metadata) {
        this.progression.pushCodexBreadcrumb(type, key, this.page, metadata);
    }

    private void displayBreadcrumb(boolean peek) {
        CodexBreadcrumb breadcrumb = peek ? this.progression.peekCodexBreadcrumb() : this.progression.popCodexBreadcrumb();
        if (breadcrumb == null) {
            this.setupIndexView(false, 0);
        } else {
            this.clearDisplayedRecipe(false);
            this.clearSearch(false);
            switch(breadcrumb.Type) {
                case ENTRY:
                    this.setCurrentEntry(breadcrumb.Key, false);
                    break;
                case RECIPE:
                    this.lookupAndShowRecipe(breadcrumb.Key, false);
                    break;
                case SEARCH:
                    this.showSearch(false);
                    this.searchBox.setValue(breadcrumb.Key);
                    break;
                case CATEGORY:
                    try {
                        this.setupCategoryView(EntryCategory.valueOf(breadcrumb.Key), breadcrumb.Page, false);
                        this.setupIndexView(false, breadcrumb.Page);
                    } catch (Throwable var4) {
                        this.setupIndexView(false, 0);
                    }
                    break;
                case INDEX:
                    this.setupIndexView(false, breadcrumb.Page);
                    break;
                default:
                    return;
            }
        }
    }

    private void setupIndexView(boolean pushBreadcrumb, int page) {
        if (pushBreadcrumb) {
            this.pushCategoryBreadcrumb();
        }
        this.currentEntry = null;
        this.clearRelatedTabs();
        this.setupCategorySelectionButtons();
        this.setupCategoryItemButtons();
        this.setupCategoryView(this.currentCategory, page, false);
        this.pageWidgets.clear();
    }

    private void setupCategoryView(EntryCategory value, boolean pushBreadcrumb) {
        this.setupCategoryView(value, 0, pushBreadcrumb);
    }

    private void setupCategoryView(EntryCategory value, int page, boolean pushBreadcrumb) {
        int playerTier = this.progression.getTier();
        this.max_pages = 1;
        this.categoryButtons.forEach(c -> {
            c.f_93623_ = c.category == value && c.tier <= playerTier;
            c.f_93624_ = c.f_93623_;
            if (c.f_93623_) {
                this.max_pages = Math.max(this.max_pages, c.page);
            }
        });
        this.prevPage.f_93624_ = this.prevPage.f_93623_ = page > 0;
        this.page = Mth.clamp(page, 0, this.max_pages);
        if (this.max_pages > 1) {
            this.nextPage.f_93624_ = true;
            this.nextPage.f_93623_ = true;
        } else {
            this.nextPage.f_93624_ = false;
            this.nextPage.f_93623_ = false;
        }
        this.back.f_93623_ = this.back.f_93624_ = false;
        this.currentCategory = value;
    }

    private void setCurrentEntry(String id, boolean pushBreadcrumb) {
        if (pushBreadcrumb) {
            this.pushCategoryBreadcrumb();
        }
        this.hideCategoryView();
        this.clearRelatedTabs();
        this.currentEntry = GuideBookEntries.INSTANCE.getEntry(id);
        this.setupEntryRelatedButtons();
        this.setupEntryPages();
        this.page = 0;
    }

    private void hideCategoryView() {
        this.categoryButtons.forEach(c -> c.f_93623_ = c.f_93624_ = false);
        this.back.f_93623_ = this.back.f_93624_ = true;
    }

    private void setupCategoryItemButtons() {
        if (this.categoryButtons.size() <= 0) {
            for (EntryCategory value : EntryCategory.values()) {
                int x = this.f_97735_ + 15;
                int y = this.f_97736_ + 10 + 20;
                int y_max = this.f_97736_ + 168;
                int render_page = 0;
                int entryCount = 1;
                List<GuidebookEntry> entries = (List<GuidebookEntry>) GuideBookEntries.INSTANCE.getEntries(value).stream().collect(Collectors.toList());
                Collections.sort(entries, new Comparator<GuidebookEntry>() {

                    public int compare(GuidebookEntry o1, GuidebookEntry o2) {
                        Integer index1 = o1.getIndex();
                        Integer index2 = o2.getIndex();
                        return index1 == index2 ? o1.getName().compareTo(o2.getName()) : index1.compareTo(index2);
                    }
                });
                for (GuidebookEntry e : entries) {
                    if (e.getTier() <= this.progression.getTier()) {
                        String disp = I18n.get(e.getFirstTitle());
                        int countWidth = this.f_96547_.width(" " + entryCount);
                        List<FormattedText> split_lines = this.f_96547_.getSplitter().splitLines(disp, 108 - countWidth, Style.EMPTY);
                        disp = ((FormattedText) split_lines.get(split_lines.size() - 1)).getString();
                        char spacer = '.';
                        int width = this.f_96547_.width(disp);
                        int padding = 15 - countWidth;
                        int spacerWidth = this.f_96547_.width(spacer + "");
                        int nonPaddedWidth = 108 - countWidth - padding;
                        int requiredPaddingWidth = nonPaddedWidth - width;
                        int numSpacers = 0;
                        if (nonPaddedWidth > 0 && requiredPaddingWidth > spacerWidth) {
                            numSpacers = requiredPaddingWidth / spacerWidth;
                        }
                        if (numSpacers > 0) {
                            char[] spacers = new char[numSpacers];
                            Arrays.fill(spacers, spacer);
                            disp = disp + String.copyValueOf(spacers);
                        }
                        split_lines.set(split_lines.size() - 1, Component.literal(disp));
                        List<Component> split_lines_cast = new ArrayList();
                        for (FormattedText split : split_lines) {
                            split_lines_cast.add(Component.literal(split.getString()));
                        }
                        GuiGuideBook.CategoryItemButton btn = new GuiGuideBook.CategoryItemButton(value, e.getTier(), e.getOverrideColor(), e.getTooltip(), render_page, entryCount, x, y, split_lines_cast, button -> {
                            this.hideCategoryView();
                            this.setCurrentEntry(e.getName(), true);
                        });
                        if (y + btn.m_93694_() > y_max) {
                            render_page++;
                            y = this.f_97736_ + 10 + 20;
                            if (render_page % 2 == 1) {
                                x = this.f_97735_ + 133;
                            } else {
                                x = this.f_97735_ + 15;
                            }
                            btn.m_252865_(x);
                            btn.m_253211_(y);
                            btn.page = render_page;
                        }
                        this.m_142416_(btn);
                        this.categoryButtons.add(btn);
                        y += btn.m_93694_();
                        entryCount++;
                    }
                }
            }
        }
    }

    private void clearRelatedTabs() {
        for (ImageButton btn : this.relatedTabs) {
            this.m_169411_(btn);
        }
    }

    private void setupEntryPages() {
        this.max_pages = 1;
        this.pageWidgets.entrySet().forEach(e -> ((Collection) e.getValue()).forEach(widget -> this.m_169411_(widget)));
        this.pageWidgets.clear();
        if (this.currentEntry != null) {
            int x = 15;
            int y = 10;
            int page = -1;
            for (IEntrySection section : (List) this.currentEntry.getSections().stream().sorted((a, b) -> b.getPage() - a.getPage()).collect(Collectors.toList())) {
                if (section.getPage() > this.max_pages) {
                    this.max_pages = section.getPage();
                }
                if (page != section.getPage()) {
                    page = section.getPage();
                    y = this.f_97736_ + 10;
                    x = this.f_97735_ + (page % 2 == 0 ? 15 : 133);
                }
                ArrayList<AbstractWidget> curPageWidgets = (ArrayList<AbstractWidget>) this.pageWidgets.getOrDefault(section.getPage(), new ArrayList());
                curPageWidgets.addAll(section.getWidgets(this, x, y, 108, 168, this::setTooltip, this::lookupAndShowRecipe, this::setCurrentEntry));
                this.pageWidgets.put(section.getPage(), curPageWidgets);
                y += section.getHeight(168);
            }
        }
        this.prevPage.f_93624_ = false;
        this.prevPage.f_93623_ = false;
        if (this.max_pages > 1) {
            this.nextPage.f_93624_ = true;
            this.nextPage.f_93623_ = true;
        } else {
            this.nextPage.f_93624_ = false;
            this.nextPage.f_93623_ = false;
        }
    }

    private void setupCategorySelectionButtons() {
        int step = 19;
        int x = this.f_97735_ + this.f_97726_ - 7;
        int y = this.f_97736_ + 4;
        int x_custom = this.f_97735_ + this.f_97726_ - step - 7;
        int y_custom = this.f_97736_ + this.f_97727_ - 4;
        for (EntryCategory value : EntryCategory.values()) {
            boolean entryShown = false;
            for (GuidebookEntry e : (List) GuideBookEntries.INSTANCE.getEntries(value).stream().collect(Collectors.toList())) {
                if (e.getTier() <= this.progression.getTier()) {
                    entryShown = true;
                    break;
                }
            }
            entryShown = true;
            if (entryShown) {
                ItemStack stack = value.getDisplayStack();
                if (!stack.isEmpty()) {
                    int width = value.isCustom() ? 18 : 28;
                    int height = value.isCustom() ? 28 : 18;
                    int u = value.isCustom() ? 208 : 28;
                    int v = value.isCustom() ? 0 : 54;
                    ImageItemStackButton btn = new ImageItemStackButton(value.isCustom() ? x_custom : x, value.isCustom() ? y_custom : y, width, height, u, v, height, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.setupCategoryView(value, true), this::setTooltip, stack, false);
                    this.m_142416_(btn);
                    this.relatedTabs.add(btn);
                    if (value.isCustom()) {
                        x_custom -= step;
                    } else {
                        y += step;
                    }
                }
            }
        }
    }

    private void setupEntryRelatedButtons() {
        if (this.currentEntry != null) {
            int i = this.f_97735_;
            int j = this.f_97736_;
            int x = i + this.f_97726_ - 7;
            int y = j + 5;
            int y_step = 19;
            if (this.currentEntry.getRelatedRecipes().size() > 0) {
                y_step = Math.min(20, 170 / this.currentEntry.getRelatedRecipes().size());
            }
            int playerTier = this.progression.getTier();
            for (RelatedRecipe rr : this.currentEntry.getRelatedRecipes()) {
                if (rr.getTier() <= playerTier) {
                    if (rr.getType().equals("spell_part")) {
                        ISpellComponent part = this.lookupSpell(rr.getResourceLocations()[0]);
                        if (part == null) {
                            continue;
                        }
                        GuiGuideBook.ImageSpellPartButton btn = new GuiGuideBook.ImageSpellPartButton(x, y, 28, 18, 56, 0, 18, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.displayRecipe(rr, true), part.getGuiIcon(), part.getRegistryName());
                        this.m_142416_(btn);
                        this.relatedTabs.add(btn);
                    } else {
                        ItemStack stack = this.lookupResource(rr.getResourceLocations()[0]);
                        if (stack.isEmpty()) {
                            Optional<? extends Recipe<?>> pattern = this.f_96541_.level.getRecipeManager().byKey(rr.getResourceLocations()[0]);
                            if (pattern.isPresent()) {
                                if (pattern.get() instanceof AMRecipeBase) {
                                    stack = ((AMRecipeBase) pattern.get()).getGuiRepresentationStack();
                                } else {
                                    stack = ((Recipe) pattern.get()).getResultItem(this.f_96541_.level.m_9598_());
                                }
                            }
                        }
                        if (!stack.isEmpty()) {
                            ImageItemStackButton btn = new ImageItemStackButton(x, y, 28, 18, 56, 0, 18, GuiTextures.Items.GUIDE_BOOK_WIDGETS, 256, 256, button -> this.displayRecipe(rr, true), this::setTooltip, stack);
                            this.m_142416_(btn);
                            this.relatedTabs.add(btn);
                        }
                    }
                    y += y_step;
                }
            }
        }
    }

    private boolean hasOverlay() {
        return this.isSearching || this.currentRecipe != null;
    }

    @Override
    public void onClose() {
        if (this.isSearching) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.SEARCH, this.searchBox.getValue());
        } else if (this.currentRecipe != null) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.RECIPE, this.currentRecipe.getRegistryName().toString());
        } else if (this.currentEntry != null) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.ENTRY, this.currentEntry.getName());
        } else if (this.currentCategory != null) {
            this.pushBreadcrumb(CodexBreadcrumb.Type.CATEGORY, this.currentCategory.name());
        }
        super.m_7379_();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        currentTooltip.clear();
        super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTick);
        if (this.isSearching) {
            this.fixFocus("focus", "focus");
        }
        ((Collection) this.pageWidgets.getOrDefault(this.page, new ArrayList())).forEach(w -> w.render(pGuiGraphics, mouseX, mouseY, partialTick));
        ((Collection) this.pageWidgets.getOrDefault(this.page + 1, new ArrayList())).forEach(w -> w.render(pGuiGraphics, mouseX, mouseY, partialTick));
        if (!currentTooltip.isEmpty()) {
            pGuiGraphics.renderTooltip(this.f_96547_, Lists.transform(currentTooltip, Component::m_7532_), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.Items.GUIDE_BOOK, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        if (this.currentRecipe == null && !this.isSearching) {
            if (this.currentEntry == null) {
                this.renderIndex(pGuiGraphics);
            }
        }
    }

    private void renderIndex(GuiGraphics pGuiGraphics) {
        String str = this.currentCategory.getDisplayStack().getHoverName().getString();
        int padding = (108 - this.f_96541_.font.width(str)) / 2;
        pGuiGraphics.drawString(this.f_96547_, str, 15 + padding, 15, 4210752, false);
    }

    private void setTooltip(List<Component> tooltip) {
        currentTooltip.clear();
        currentTooltip.addAll(tooltip);
    }

    public class CategoryItemButton extends Button {

        public int page;

        public final EntryCategory category;

        public final int tier;

        private final String count;

        private final List<Component> textLines;

        private final List<Component> tooltipLines;

        private final int overrideColor;

        public CategoryItemButton(EntryCategory category, int tier, int overrideColor, List<Component> tooltipLines, int page, int count, int x, int y, List<Component> text, Button.OnPress onPress) {
            super(x, y, 108, 9 * text.size() + 5, (Component) text.get(0), onPress, f_252438_);
            this.page = page;
            this.category = category;
            this.count = count + "";
            this.textLines = text;
            this.tier = tier;
            this.overrideColor = overrideColor;
            this.tooltipLines = tooltipLines;
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (GuiGuideBook.this.page == this.page || GuiGuideBook.this.page == this.page - 1) {
                super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTicks);
            }
        }

        @Override
        protected boolean clicked(double mouseX, double mouseY) {
            return (this.page == GuiGuideBook.this.page || this.page - 1 == GuiGuideBook.this.page) && this.f_93623_ && this.f_93624_ && mouseX >= (double) this.m_252754_() && mouseY >= (double) this.m_252907_() && mouseX < (double) (this.m_252754_() + this.f_93618_) && mouseY < (double) (this.m_252907_() + this.f_93619_);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            Minecraft minecraft = Minecraft.getInstance();
            Font fontrenderer = minecraft.font;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
            int j = this.getFGColor();
            int renderY = this.m_252907_();
            for (FormattedText s : this.textLines) {
                pGuiGraphics.drawString(fontrenderer, s.getString(), this.m_252754_(), renderY, j | Mth.ceil(this.f_93625_ * 255.0F) << 24, false);
                renderY += 9;
            }
            renderY -= 9;
            pGuiGraphics.drawString(fontrenderer, this.count, this.m_252754_() + this.f_93618_ - fontrenderer.width(this.count), renderY, j | Mth.ceil(this.f_93625_ * 255.0F) << 24, false);
            if (this.tooltipLines != null && this.f_93622_) {
                GuiGuideBook.this.setTooltip(this.tooltipLines);
            }
        }

        public int getFGColor() {
            if (this.f_93622_ && this.f_93623_) {
                return 11731123;
            } else {
                return this.overrideColor == -1 ? 4473924 : this.overrideColor;
            }
        }
    }

    public class CodexSearchResult extends Button {

        private List<FormattedText> split_lines;

        public CodexSearchResult(int x, int y, String text, Button.OnPress pressedAction) {
            super(x, y, 205, 9 * GuiGuideBook.this.f_96547_.getSplitter().splitLines(Component.literal(text), 200, Style.EMPTY).size(), Component.literal(text), pressedAction, f_252438_);
            this.split_lines = GuiGuideBook.this.f_96547_.getSplitter().splitLines(Component.literal(text), 200, Style.EMPTY);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            Minecraft minecraft = Minecraft.getInstance();
            Font fontrenderer = minecraft.font;
            int renderY = this.m_252907_();
            for (FormattedText props : this.split_lines) {
                pGuiGraphics.drawString(fontrenderer, props.getString(), this.m_252754_(), renderY, this.getFGColor(), false);
                renderY += 9;
            }
        }

        public int getFGColor() {
            return this.m_198029_() ? 11731123 : 4473924;
        }
    }

    public class ImageSpellPartButton extends ImageButton {

        final ResourceLocation spellTexture;

        final String translateKey;

        public ImageSpellPartButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, ResourceLocation spellTexture, ResourceLocation spellName) {
            super(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, Component.literal(""));
            this.spellTexture = spellTexture;
            this.translateKey = spellName.toString();
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            super.renderWidget(pGuiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
            RenderSystem.disableDepthTest();
            pGuiGraphics.blit(this.spellTexture, this.m_252754_() + 3, this.m_252907_() + 1, 0.0F, 0.0F, 16, 16, 16, 16);
            RenderSystem.enableDepthTest();
            if (this.f_93623_ && this.f_93622_) {
                List<Component> tt = new ArrayList();
                tt.add(Component.translatable(this.translateKey));
                GuiGuideBook.currentTooltip = tt;
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }
    }

    public class PaperImageButton extends ImageButton {

        private Component tooltip;

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.OnPress onPressIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        }

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51136_9_, int p_i51136_10_, Button.OnPress onPressIn, Component textIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51136_9_, p_i51136_10_, onPressIn, textIn);
        }

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, Button.OnPress onPressIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn);
        }

        public GuiGuideBook.PaperImageButton setTooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.renderWidget(pGuiGraphics, mouseX, mouseY, partialTicks);
            if (this.f_93623_ && this.f_93624_ && this.m_198029_() && this.tooltip != null) {
                GuiGuideBook.currentTooltip.add(this.tooltip);
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }
    }
}