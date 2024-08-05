package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import com.mrcrayfish.configured.util.ConfigHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public abstract class ListMenuScreen extends TooltipScreen implements IBackgroundTexture {

    public static final ResourceLocation CONFIGURED_LOGO = new ResourceLocation("configured", "textures/gui/logo.png");

    protected final Screen parent;

    protected final ResourceLocation background;

    protected final int itemHeight;

    protected ListMenuScreen.EntryList list;

    protected List<ListMenuScreen.Item> entries;

    protected ListMenuScreen.FocusedEditBox activeTextField;

    protected ListMenuScreen.FocusedEditBox searchTextField;

    protected ListMenuScreen(Screen parent, Component title, ResourceLocation background, int itemHeight) {
        super(title);
        this.parent = parent;
        this.background = background;
        this.itemHeight = itemHeight;
    }

    protected abstract void constructEntries(List<ListMenuScreen.Item> var1);

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.background;
    }

    @Override
    protected void init() {
        List<ListMenuScreen.Item> entries = new ArrayList();
        this.constructEntries(entries);
        this.entries = ImmutableList.copyOf(entries);
        this.list = new ListMenuScreen.EntryList(this.entries);
        this.list.m_93488_(!ConfigHelper.isPlayingGame());
        this.m_7787_(this.list);
        this.searchTextField = new ListMenuScreen.FocusedEditBox(this.f_96547_, this.f_96543_ / 2 - 110, 22, 220, 20, Component.translatable("configured.gui.search"));
        this.searchTextField.setClearable(true);
        this.searchTextField.m_94151_(s -> this.updateSearchResults());
        this.m_7787_(this.searchTextField);
        ScreenUtil.updateSearchTextFieldSuggestion(this.searchTextField, "", this.entries);
    }

    protected void updateSearchResults() {
        String query = this.searchTextField.m_94155_();
        ScreenUtil.updateSearchTextFieldSuggestion(this.searchTextField, query, this.entries);
        this.list.replaceEntries((Collection<ListMenuScreen.Item>) (query.isEmpty() ? this.entries : this.getSearchResults(query)));
        if (!query.isEmpty()) {
            this.list.m_93410_(0.0);
        }
    }

    protected Collection<ListMenuScreen.Item> getSearchResults(String s) {
        return (Collection<ListMenuScreen.Item>) this.entries.stream().filter(item -> !(item instanceof ListMenuScreen.IIgnoreSearch) && item.getLabel().toLowerCase(Locale.ENGLISH).contains(s.toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
    }

    protected void updateTooltip(int mouseX, int mouseY) {
        if (ScreenUtil.isMouseWithin(10, 13, 23, 23, mouseX, mouseY)) {
            this.setActiveTooltip(Component.translatable("configured.gui.info"));
        }
    }

    @Override
    public void tick() {
        this.searchTextField.m_94120_();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.resetTooltip();
        this.m_280273_(graphics);
        this.list.render(graphics, mouseX, mouseY, partialTicks);
        this.searchTextField.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 7, 16777215);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.renderForeground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(CONFIGURED_LOGO, 10, 13, 0, 0.0F, 0.0F, 23, 23, 32, 32);
        graphics.blit(IconButton.ICONS, this.f_96543_ / 2 - 128, 26, 14, 14, 22.0F, 11.0F, 10, 10, 64, 64);
        this.updateTooltip(mouseX, mouseY);
        if (this.tooltipText != null) {
            this.drawTooltip(graphics, mouseX, mouseY);
        } else {
            for (GuiEventListener widget : this.m_6702_()) {
                if (widget instanceof Button && ((Button) widget).m_198029_()) {
                    break;
                }
            }
        }
    }

    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ScreenUtil.isMouseWithin(10, 13, 23, 23, (int) mouseX, (int) mouseY)) {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/configured"));
            this.m_5561_(style);
            return true;
        } else {
            if (this.activeTextField != null && !this.activeTextField.m_5953_(mouseX, mouseY)) {
                this.activeTextField.setFocused(false);
            }
            return super.m_6375_(mouseX, mouseY, button);
        }
    }

    protected class EntryList extends ContainerObjectSelectionList<ListMenuScreen.Item> implements IBackgroundTexture {

        public EntryList(List<ListMenuScreen.Item> entries) {
            super(ListMenuScreen.this.f_96541_, ListMenuScreen.this.f_96543_, ListMenuScreen.this.f_96544_, 50, ListMenuScreen.this.f_96544_ - 36, ListMenuScreen.this.itemHeight);
            entries.forEach(x$0 -> this.m_7085_(x$0));
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93388_ / 2 + 144;
        }

        @Override
        public int getRowWidth() {
            return 260;
        }

        @Override
        public ResourceLocation getBackgroundTexture() {
            return ListMenuScreen.this.background;
        }

        @Override
        public void replaceEntries(Collection<ListMenuScreen.Item> entries) {
            super.m_5988_(entries);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.m_88315_(graphics, mouseX, mouseY, partialTicks);
            this.renderToolTips(graphics, mouseX, mouseY);
        }

        private void renderToolTips(GuiGraphics graphics, int mouseX, int mouseY) {
            this.m_6702_().forEach(item -> item.children().forEach(o -> {
                if (o instanceof Button) {
                }
            }));
        }
    }

    protected class FocusedEditBox extends EditBox {

        private boolean clearable = false;

        public FocusedEditBox(Font font, int x, int y, int width, int height, Component label) {
            super(font, x, y, width, height, label);
        }

        public ListMenuScreen.FocusedEditBox setClearable(boolean clearable) {
            this.clearable = clearable;
            return this;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (focused) {
                if (ListMenuScreen.this.activeTextField != null && ListMenuScreen.this.activeTextField != this) {
                    ListMenuScreen.this.activeTextField.setFocused(false);
                }
                ListMenuScreen.this.activeTextField = this;
            }
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            super.m_88315_(graphics, mouseX, mouseY, partialTick);
            if (this.clearable && !this.m_94155_().isEmpty()) {
                RenderSystem.setShader(GameRenderer::m_172820_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                boolean hovered = ScreenUtil.isMouseWithin(this.m_252754_() + this.f_93618_ - 15, this.m_252907_() + 5, 9, 9, mouseX, mouseY);
                graphics.blit(IconButton.ICONS, this.m_252754_() + this.f_93618_ - 15, this.m_252907_() + 5, 9, 9, hovered ? 9.0F : 0.0F, 55.0F, 9, 9, 64, 64);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.clearable && !this.m_94155_().isEmpty() && button == 0 && ScreenUtil.isMouseWithin(this.m_252754_() + this.f_93618_ - 15, this.m_252907_() + 5, 9, 9, (int) mouseX, (int) mouseY)) {
                this.m_7435_(ListMenuScreen.this.f_96541_.getSoundManager());
                this.m_94144_("");
                return true;
            } else {
                return super.m_6375_(mouseX, mouseY, button);
            }
        }
    }

    protected interface IIgnoreSearch {
    }

    protected abstract class Item extends ContainerObjectSelectionList.Entry<ListMenuScreen.Item> implements ILabelProvider, Comparable<ListMenuScreen.Item> {

        protected final Component label;

        @Nullable
        protected List<FormattedCharSequence> tooltip;

        public Item(Component label) {
            this.label = label;
        }

        public Item(String label) {
            this.label = Component.literal(label);
        }

        @Override
        public String getLabel() {
            return this.label.getString();
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks) {
            if (this.m_5953_((double) mouseX, (double) mouseY)) {
                ListMenuScreen.this.setActiveTooltip(this.tooltip);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, Item.this.label);
                }
            });
        }

        public int compareTo(ListMenuScreen.Item o) {
            return this.label.getString().compareTo(o.label.getString());
        }
    }

    public class MultiTextItem extends ListMenuScreen.Item implements ListMenuScreen.IIgnoreSearch {

        private final Component bottomText;

        public MultiTextItem(Component topText, Component bottomText) {
            super(topText);
            this.bottomText = bottomText;
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks) {
            graphics.drawCenteredString(ListMenuScreen.this.f_96541_.font, this.label, left + width / 2, top, -1);
            graphics.drawCenteredString(ListMenuScreen.this.f_96541_.font, this.bottomText, left + width / 2, top + 12, -1);
            if (this.m_5953_((double) mouseX, (double) mouseY)) {
                Style style = this.bottomText.getStyle();
                HoverEvent event = style.getHoverEvent();
                if (event != null && event.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    ListMenuScreen.this.setActiveTooltip(event.getValue(HoverEvent.Action.SHOW_TEXT), -219136);
                }
            }
        }
    }

    public class TitleItem extends ListMenuScreen.Item implements ListMenuScreen.IIgnoreSearch {

        public TitleItem(Component title) {
            super(title);
        }

        public TitleItem(String title) {
            super(Component.literal(title).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.YELLOW));
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks) {
            graphics.drawCenteredString(ListMenuScreen.this.f_96541_.font, this.label, left + width / 2, top + 5, 16777215);
        }
    }
}