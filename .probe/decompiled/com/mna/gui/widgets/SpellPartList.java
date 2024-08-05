package com.mna.gui.widgets;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.ItemAndPatternRecipe;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

@OnlyIn(Dist.CLIENT)
public class SpellPartList extends ObjectSelectionList<SpellPartList.SpellPartEntry> {

    private boolean _scrolling = false;

    public boolean _active = true;

    private final int tier;

    private final IPlayerRoteSpells rote;

    private Consumer<Shape> shapeClick;

    private Consumer<SpellEffect> componentClick;

    private Consumer<Modifier> modifierClick;

    private Consumer<Component> tooltip;

    public ISpellComponent _selected;

    private final boolean includeModifiers;

    private final boolean roteOnly;

    private String searchTerm;

    private boolean tierChecks;

    public SpellPartList(boolean includeModifiers, boolean roteOnly, int screenLeft, int screenTop, int x, int y, int width, int height, Consumer<Shape> onShapeClicked, Consumer<SpellEffect> onComponentClicked, Consumer<Modifier> onModifierClicked, Consumer<Component> tooltip) {
        super(Minecraft.getInstance(), width, height, screenTop + y, screenTop + y + height, 24);
        this.m_93471_(false);
        this.m_93473_(false, 24);
        this.rote = (IPlayerRoteSpells) this.f_93386_.player.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
        this.shapeClick = onShapeClicked;
        this.componentClick = onComponentClicked;
        this.modifierClick = onModifierClicked;
        this.tooltip = tooltip;
        this.includeModifiers = includeModifiers;
        this.roteOnly = roteOnly;
        this.tierChecks = false;
        this.f_93393_ = screenLeft + x;
        this.f_93392_ = screenLeft + x + width;
        this.tier = ((IPlayerProgression) this.f_93386_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null)).getTier();
        this.reInit("");
    }

    public SpellPartList enableTierChecks() {
        this.tierChecks = true;
        return this;
    }

    public void clear() {
        this.m_93516_();
    }

    public boolean isScrolling() {
        return this._scrolling;
    }

    private int compareSpellParts(ISpellComponent a, ISpellComponent b) {
        String aName = Component.translatable(a.getRegistryName().toString()).getString();
        String bName = Component.translatable(b.getRegistryName().toString()).getString();
        return aName.compareTo(bName);
    }

    private boolean bySearchTerm(ISpellComponent comp) {
        String translatedName = I18n.get(comp.getRegistryName().toString()).toLowerCase();
        return this.searchTerm == "" || translatedName.contains(this.searchTerm);
    }

    private boolean validParts(ISpellComponent p) {
        if (!this.f_93386_.player.m_7500_() && this.roteOnly && !this.rote.isRote(p)) {
            return false;
        } else if (!this.roteOnly && p.isSilverSpell()) {
            return false;
        } else {
            boolean valid = p.isCraftable(new SpellCraftingContext(this.f_93386_.player));
            Optional<? extends Recipe<?>> pattern = this.f_93386_.level.getRecipeManager().getRecipes().stream().filter(r -> r.getId().equals(p.getRegistryName())).findFirst();
            if (pattern.isPresent() && pattern.get() instanceof ItemAndPatternRecipe) {
                valid &= ((ItemAndPatternRecipe) pattern.get()).getTier() <= this.tier;
            }
            return valid;
        }
    }

    public void reInit(String searchTerm) {
        this.searchTerm = searchTerm;
        this.addIconsForAll((Collection) ((IForgeRegistry) Registries.Shape.get()).getValues().stream().sorted(this::compareSpellParts).filter(this::validParts).filter(this::bySearchTerm).collect(Collectors.toList()), s -> this.shapeClick.accept((Shape) s));
        this.addIconsForAll((Collection) ((IForgeRegistry) Registries.SpellEffect.get()).getValues().stream().sorted(this::compareSpellParts).filter(this::validParts).filter(this::bySearchTerm).collect(Collectors.toList()), s -> this.componentClick.accept((SpellEffect) s));
        if (this.includeModifiers) {
            this.addIconsForAll((Collection) ((IForgeRegistry) Registries.Modifier.get()).getValues().stream().sorted(this::compareSpellParts).filter(this::validParts).filter(this::bySearchTerm).collect(Collectors.toList()), s -> this.modifierClick.accept((Modifier) s));
        }
    }

    private <T extends ISpellComponent> void addIconsForAll(Collection<T> parts, Consumer<ISpellComponent> clickHandler) {
        int itemsPerRow = (this.f_93388_ + 1) / 20;
        ArrayList<ISpellComponent> segment = new ArrayList();
        for (ISpellComponent part : parts) {
            segment.add(part);
            if (segment.size() == itemsPerRow) {
                this.m_7085_(new SpellPartList.SpellPartEntry(segment, clickHandler));
                segment.clear();
            }
        }
        if (segment.size() > 0) {
            this.m_7085_(new SpellPartList.SpellPartEntry(segment, clickHandler));
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this._active) {
            int scrollBarStartX = this.getScrollbarPosition();
            double scale = this.f_93386_.getWindow().getGuiScale();
            RenderSystem.enableScissor((int) ((double) this.f_93393_ * scale), (int) ((double) this.f_93386_.getWindow().getHeight() - (double) this.f_93391_ * scale), (int) ((double) this.f_93388_ * scale), (int) ((double) this.f_93389_ * scale));
            this.renderList(pGuiGraphics, mouseX, mouseY, partialTicks);
            if (this.getMaxScroll() > 0) {
                int scrollBarHeight = 20;
                int top = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - scrollBarHeight) / this.getMaxScroll() + this.f_93390_;
                if (top < this.f_93390_) {
                    top = this.f_93390_;
                }
                pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, scrollBarStartX, top, 25.0F, 20.0F, 4, scrollBarHeight, 128, 128);
            }
            RenderSystem.disableScissor();
        }
    }

    @Override
    protected void renderList(GuiGraphics pGuiGraphics, int pX, int pY, float pPartialTick) {
        int i = this.m_5773_();
        for (int j = 0; j < i; j++) {
            int k = this.getRowTop(j);
            int l = this.getRowBottom(j);
            if (l > this.f_93390_ && k < this.f_93391_ - 10) {
                int j1 = this.f_93387_ - 4;
                SpellPartList.SpellPartEntry e = (SpellPartList.SpellPartEntry) this.m_93500_(j);
                int k1 = this.getRowWidth();
                int j2 = this.getRowLeft();
                e.render(pGuiGraphics, j, k, j2, k1, j1, pX, pY, this.m_5953_((double) pX, (double) pY) && Objects.equals(this.m_93412_((double) pX, (double) pY), e), pPartialTick);
            }
        }
    }

    @Nullable
    protected final SpellPartList.SpellPartEntry getEntryAtPos(double mouseX, double mouseY) {
        int lowerXBound = this.getRowLeft();
        int upperXBound = lowerXBound + this.getRowWidth();
        int adjustedY = Mth.floor(mouseY - (double) this.f_93390_) + (int) this.m_93517_();
        int index = adjustedY / this.f_93387_;
        return index >= 0 && adjustedY >= 0 && index < this.m_5773_() && mouseX < (double) this.getScrollbarPosition() && mouseX >= (double) lowerXBound && mouseX <= (double) upperXBound ? (SpellPartList.SpellPartEntry) this.m_6702_().get(index) : null;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowLeft() + this.getRowWidth() - 4;
    }

    @Override
    protected int getRowTop(int p_getRowTop_1_) {
        return this.f_93390_ - (int) this.m_93517_() + p_getRowTop_1_ * this.f_93387_ - 4;
    }

    @Override
    protected int getRowBottom(int p_230948_1_) {
        return this.getRowTop(p_230948_1_) + this.f_93387_;
    }

    @Override
    public int getRowLeft() {
        return this.f_93393_;
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_;
    }

    @Override
    protected void updateScrollingState(double p_updateScrollingState_1_, double p_updateScrollingState_3_, int p_updateScrollingState_5_) {
        super.m_93481_(p_updateScrollingState_1_, p_updateScrollingState_3_, p_updateScrollingState_5_);
        this._scrolling = p_updateScrollingState_5_ == 0 && p_updateScrollingState_1_ >= (double) this.getScrollbarPosition() && p_updateScrollingState_1_ < (double) (this.getScrollbarPosition() + 6);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (!this._active) {
            return false;
        } else {
            this.updateScrollingState(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
            if (!this.m_5953_(p_mouseClicked_1_, p_mouseClicked_3_)) {
                return false;
            } else {
                SpellPartList.SpellPartEntry e = this.getEntryAtPos(p_mouseClicked_1_, p_mouseClicked_3_);
                if (e != null) {
                    if (e.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
                        e.m_93692_(true);
                        return true;
                    }
                } else if (p_mouseClicked_5_ == 0) {
                    this.m_7897_(true);
                    return true;
                }
                return this._scrolling;
            }
        }
    }

    @Override
    public int getMaxScroll() {
        return Math.max(0, this.m_5775_() - (this.f_93391_ - this.f_93390_ - 4));
    }

    public void setSelected(@Nullable SpellPartList.SpellPartEntry selected) {
        super.m_6987_(selected);
    }

    @OnlyIn(Dist.CLIENT)
    public class SpellPartEntry extends ObjectSelectionList.Entry<SpellPartList.SpellPartEntry> {

        private Collection<ISpellComponent> parts;

        private int spacing = 18;

        private ISpellComponent _hoveredComponent;

        private Consumer<ISpellComponent> _clickHandler;

        public SpellPartEntry(Collection<ISpellComponent> parts, Consumer<ISpellComponent> clickHandler) {
            this.parts = new ArrayList(parts);
            this._hoveredComponent = null;
            this._clickHandler = clickHandler;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float p_render_9_) {
            int i = 0;
            for (ISpellComponent part : this.parts) {
                if (part != null) {
                    boolean crossFaction = false;
                    if (SpellPartList.this.tierChecks) {
                        IFaction playerFaction = ((IPlayerProgression) ManaAndArtifice.instance.proxy.getClientPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null)).getAlliedFaction();
                        crossFaction = playerFaction != null && part.getFactionRequirement() != null && playerFaction != part.getFactionRequirement();
                    }
                    int x = 5 + left + i++ * this.spacing;
                    int y = top + 5;
                    if (isHovered && mouseX >= x && mouseX <= x + this.spacing) {
                        if (SpellPartList.this.tooltip != null) {
                            if (part.isSilverSpell()) {
                                SpellPartList.this.tooltip.accept(Component.translatable(part.getRegistryName().toString()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
                            } else {
                                SpellPartList.this.tooltip.accept(Component.translatable(part.getRegistryName().toString()));
                            }
                            if (crossFaction) {
                                SpellPartList.this.tooltip.accept(Component.translatable("gui.mna.inscription.wrong_faction"));
                            }
                        }
                        this._hoveredComponent = part;
                        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK, x - 1, y - 1, 0, 208, 20, 20);
                    }
                    if (SpellPartList.this._selected == part) {
                        pGuiGraphics.blit(GuiTextures.WizardLab.THESIS_DESK, x - 1, y - 1, 0, 208, 20, 20);
                    }
                    this.renderIcon(pGuiGraphics, x, y, part.getGuiIcon());
                    if (part.isSilverSpell()) {
                        GuiRenderUtils.renderSilverSpellBorder(pGuiGraphics, x, y, 16, 16);
                    }
                    if (crossFaction) {
                        int size = 16;
                        pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, x, y, 0, 0.0F, 112.0F, size, size, 128, 128);
                    }
                }
            }
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            SpellPartList.this.setSelected(this);
            SpellPartList.this._selected = this._hoveredComponent;
            if (this._clickHandler != null && this._hoveredComponent != null) {
                this._clickHandler.accept(this._hoveredComponent);
            }
            return true;
        }

        private void renderIcon(GuiGraphics pGuiGraphics, int x, int y, ResourceLocation item) {
            int size = 16;
            pGuiGraphics.blit(item, x, y, 0, 0.0F, 0.0F, size, size, size, size);
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", this._hoveredComponent != null ? this._hoveredComponent.getRegistryName().toString() : "none");
        }
    }
}