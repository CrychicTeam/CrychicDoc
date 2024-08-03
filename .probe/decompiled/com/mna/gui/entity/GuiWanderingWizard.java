package com.mna.gui.entity;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.SearchableGui;
import com.mna.gui.containers.entity.ContainerWanderingWizard;
import com.mna.network.ClientMessageDispatcher;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiWanderingWizard extends SearchableGui<ContainerWanderingWizard> {

    private boolean hasinitialized = false;

    private ItemStack tooltipStack = ItemStack.EMPTY;

    private GuiWanderingWizard.WanderingWizardOffersList list;

    private int selectedRecipeIndex = -1;

    public GuiWanderingWizard(ContainerWanderingWizard screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.tooltipStack = ItemStack.EMPTY;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int x, int y) {
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, i, j, 0.0F, 0.0F, this.f_97726_, 193, this.f_97726_, this.f_97727_);
        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, i + 40, j + 193, 40.0F, 193.0F, 176, 63, this.f_97726_, this.f_97727_);
        if (this.list != null) {
            this.list.render(pGuiGraphics, x, y, partialTicks);
        }
        if (this.searchBox != null) {
            RenderSystem.disableDepthTest();
            this.searchBox.m_88315_(pGuiGraphics, x, y, partialTicks);
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
        if (!this.hasinitialized && ((ContainerWanderingWizard) this.f_97732_).isFinalized()) {
            this.initialize();
        }
        if (!this.tooltipStack.isEmpty()) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.tooltipStack, x - this.f_97735_, y - this.f_97736_);
        }
    }

    @Override
    protected void searchTermChanged(String newTerm) {
        this.currentSearchTerm = newTerm;
        this.list.clear();
        this.list.reInit(this.currentSearchTerm);
        this.list.m_93410_(0.0);
    }

    private void initialize() {
        this.hasinitialized = true;
        this.list = new GuiWanderingWizard.WanderingWizardOffersList(((ContainerWanderingWizard) this.f_97732_).getOffers());
        this.m_7787_(this.list);
        int tbWidth = 130;
        int x = this.f_97735_ + 6;
        int y = this.f_97736_ + 6;
        this.initSearch(x, y, tbWidth, 16);
    }

    private void OnItemClicked(MerchantOffer offer, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        index = ((ContainerWanderingWizard) this.f_97732_).calculateIndexFor(offer, index, this.currentSearchTerm);
        this.selectedRecipeIndex = index;
        ((ContainerWanderingWizard) this.f_97732_).setCurrentRecipeIndex(this.selectedRecipeIndex);
        ((ContainerWanderingWizard) this.f_97732_).setCurrentTradeRecipeItems(this.selectedRecipeIndex);
        ClientMessageDispatcher.sendTradeSelected(index);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return this.list != null && this.list._scrolling ? this.list.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) : super.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @OnlyIn(Dist.CLIENT)
    class WanderingWizardOffersList extends ObjectSelectionList<GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry> {

        private boolean _scrolling = false;

        public boolean _active = true;

        private final Collection<MerchantOffer> initialOffers;

        public WanderingWizardOffersList(MerchantOffers merchantOffers) {
            super(GuiWanderingWizard.this.f_96541_, 244, 141, GuiWanderingWizard.this.f_97736_ + 26, GuiWanderingWizard.this.f_97736_ + 167, 20);
            this.m_93471_(false);
            this.m_93473_(false, 20);
            this.f_93393_ = GuiWanderingWizard.this.f_97735_ - 2;
            this.f_93392_ = this.f_93393_ + 250;
            this.initialOffers = merchantOffers;
            this.reInit("");
        }

        private void addItems(Collection<MerchantOffer> offers, BiConsumer<MerchantOffer, Integer> clickHandler) {
            ArrayList<MerchantOffer> segment = new ArrayList();
            int index = 0;
            for (MerchantOffer part : offers) {
                segment.add(part);
                if (segment.size() == 3) {
                    this.m_7085_(new GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry(segment, clickHandler, index));
                    index += segment.size();
                    segment.clear();
                }
            }
            if (segment.size() > 0) {
                this.m_7085_(new GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry(segment, clickHandler, index));
            }
        }

        public void clear() {
            this.m_93516_();
        }

        public void reInit(String searchTerm) {
            this.addItems((Collection<MerchantOffer>) this.initialOffers.stream().filter(o -> searchTerm == "" || o.getResult().getTooltipLines(this.f_93386_.player, TooltipFlag.Default.f_256752_).stream().anyMatch(l -> l.toString().toLowerCase().contains(searchTerm.toLowerCase()))).collect(Collectors.toList()), (o, i) -> GuiWanderingWizard.this.OnItemClicked(o, i));
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (this._active) {
                int scrollBarStartX = this.getScrollbarPosition();
                this.m_6702_().forEach(c -> c.isHovered = false);
                GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry hov = this.getEntryAtPos((double) mouseX, (double) mouseY);
                if (hov != null) {
                    hov.isHovered = true;
                }
                this.getEntryAtPos((double) mouseX, (double) mouseY);
                this.m_239227_(pGuiGraphics, mouseX, mouseY, partialTicks);
                if (this.getMaxScroll() > 0) {
                    int scrollBarHeight = (int) ((float) ((this.f_93391_ - this.f_93390_) * (this.f_93391_ - this.f_93390_)) / (float) this.m_5775_());
                    scrollBarHeight = Mth.clamp(scrollBarHeight, 32, this.f_93391_ - this.f_93390_ - 8);
                    int top = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - scrollBarHeight) / this.getMaxScroll() + this.f_93390_;
                    if (top < this.f_93390_) {
                        top = this.f_93390_;
                    }
                    pGuiGraphics.blit(new ResourceLocation("textures/gui/container/villager2.png"), scrollBarStartX, top, 25.0F, 20.0F, 3, scrollBarHeight, 512, 256);
                }
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                int i = GuiWanderingWizard.this.f_97735_;
                int j = GuiWanderingWizard.this.f_97736_;
                pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, i, j, 0, 0, GuiWanderingWizard.this.f_97726_, 26);
                pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, i, j + 167, 0, 167, GuiWanderingWizard.this.f_97726_, 26);
                RenderSystem.enableDepthTest();
            }
        }

        @Nullable
        protected final GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry getEntryAtPos(double mouseX, double mouseY) {
            int lowerXBound = this.m_5747_();
            int upperXBound = lowerXBound + this.getRowWidth();
            int adjustedY = Mth.floor(mouseY - (double) this.f_93390_) + (int) this.m_93517_();
            int index = adjustedY / this.f_93387_;
            if (mouseX < (double) this.f_93393_ || mouseX > (double) this.f_93392_ || mouseY < (double) this.f_93390_ || mouseY > (double) this.f_93391_) {
                return null;
            } else {
                return index >= 0 && adjustedY >= 0 && index < this.m_5773_() && mouseX < (double) this.getScrollbarPosition() && mouseX >= (double) lowerXBound && mouseX <= (double) upperXBound ? (GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry) this.m_6702_().get(index) : null;
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return this.m_5747_() + this.getRowWidth() + 2;
        }

        @Override
        protected int getRowTop(int p_getRowTop_1_) {
            return this.f_93390_ - (int) this.m_93517_() + p_getRowTop_1_ * this.f_93387_;
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
                    GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry e = this.getEntryAtPos(p_mouseClicked_1_, p_mouseClicked_3_);
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

        public void setSelected(@Nullable GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry selected) {
            super.m_6987_(selected);
        }

        @OnlyIn(Dist.CLIENT)
        public class ItemStackEntry extends ObjectSelectionList.Entry<GuiWanderingWizard.WanderingWizardOffersList.ItemStackEntry> {

            private Collection<MerchantOffer> items;

            private int spacing = 83;

            private MerchantOffer _hoveredComponent;

            private BiConsumer<MerchantOffer, Integer> _clickHandler;

            private int index;

            private int hoveredIndex;

            protected boolean isHovered;

            public ItemStackEntry(Collection<MerchantOffer> parts, BiConsumer<MerchantOffer, Integer> clickHandler, int index) {
                this.items = new ArrayList(parts);
                this._hoveredComponent = null;
                this.index = index;
                this._clickHandler = clickHandler;
            }

            @Override
            public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float p_render_9_) {
                int i = 0;
                for (MerchantOffer part : this.items) {
                    if (part != null) {
                        int x = 7 + left + i++ * this.spacing;
                        int y = top + 4;
                        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, x, y, 16, 238, 18, 18);
                        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, x + 18, y, 16, 238, 18, 18);
                        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, x + 37, y + 3, 0, 246, 16, 10);
                        pGuiGraphics.blit(GuiTextures.Entities.WANDERING_WIZARD, x + 54, y, 16, 238, 18, 18);
                        pGuiGraphics.renderItem(part.getBaseCostA(), x + 1, y + 1);
                        pGuiGraphics.renderItem(part.getCostB(), x + 19, y + 1);
                        pGuiGraphics.renderItem(part.getResult(), x + 55, y + 1);
                        if (this.isHovered && mouseX >= x && mouseX <= x + this.spacing) {
                            GuiWanderingWizard.this.tooltipStack = part.getResult();
                            this._hoveredComponent = part;
                            this.hoveredIndex = i - 1;
                        }
                    }
                }
            }

            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                WanderingWizardOffersList.this.setSelected(this);
                if (this._clickHandler != null && this._hoveredComponent != null) {
                    this._clickHandler.accept(this._hoveredComponent, this.index + this.hoveredIndex);
                }
                return true;
            }

            @Override
            public Component getNarration() {
                return Component.translatable("");
            }
        }
    }
}