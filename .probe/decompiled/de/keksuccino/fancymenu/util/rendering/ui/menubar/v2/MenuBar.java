package de.keksuccino.fancymenu.util.rendering.ui.menubar.v2;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.ScreenUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.TooltipHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuBar implements Renderable, GuiEventListener, NarratableEntry, NavigatableWidget {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static final int ENTRY_LABEL_SPACE_LEFT_RIGHT = 6;

    protected final List<MenuBar.MenuBarEntry> leftEntries = new ArrayList();

    protected final List<MenuBar.MenuBarEntry> rightEntries = new ArrayList();

    protected int height = 28;

    protected float scale = UIBase.getUIScale();

    protected boolean hovered = false;

    protected boolean forceUIScale = true;

    protected boolean expanded = true;

    protected MenuBar.ClickableMenuBarEntry collapseOrExpandEntry;

    protected ResourceSupplier<ITexture> collapseExpandTextureSupplier = ResourceSupplier.image(ResourceSource.of("fancymenu:textures/menubar/icons/collapse_expand.png", ResourceSourceType.LOCATION).getSourceWithPrefix());

    public MenuBar() {
        this.collapseOrExpandEntry = this.addClickableEntry(MenuBar.Side.RIGHT, "collapse_or_expand", Component.empty(), (bar, entry) -> this.setExpanded(!this.expanded)).setIconTextureSupplier((bar, entry) -> this.collapseExpandTextureSupplier.get());
        this.addSpacerEntry(MenuBar.Side.RIGHT, "spacer_after_collapse_or_expand_entry").setWidth(10);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.forceUIScale) {
            this.scale = UIBase.getUIScale();
        }
        float scale = UIBase.calculateFixedScale(this.scale);
        int scaledMouseX = (int) ((float) mouseX / scale);
        int scaledMouseY = (int) ((float) mouseY / scale);
        int y = 0;
        int width = ScreenUtils.getScreenWidth();
        int scaledWidth = width != 0 ? (int) ((float) width / scale) : 0;
        this.collapseOrExpandEntry.x = scaledWidth - this.collapseOrExpandEntry.getWidth();
        this.collapseOrExpandEntry.y = y;
        this.collapseOrExpandEntry.height = this.height;
        this.collapseOrExpandEntry.hovered = this.collapseOrExpandEntry.m_5953_((double) scaledMouseX, (double) scaledMouseY);
        this.hovered = this.isMouseOver((double) mouseX, (double) mouseY);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        UIBase.resetShaderColor(graphics);
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().translate(0.0F, 0.0F, 500.0F / scale);
        if (this.expanded) {
            this.renderBackground(graphics, 0, y, scaledWidth, this.height);
        } else {
            this.renderBackground(graphics, this.collapseOrExpandEntry.x, y, this.collapseOrExpandEntry.x + this.collapseOrExpandEntry.getWidth(), this.height);
        }
        if (this.expanded) {
            int leftX = 0;
            for (MenuBar.MenuBarEntry e : this.leftEntries) {
                e.x = leftX;
                e.y = y;
                e.height = this.height;
                e.hovered = e.isMouseOver((double) scaledMouseX, (double) scaledMouseY);
                if (e.isVisible()) {
                    RenderSystem.enableBlend();
                    UIBase.resetShaderColor(graphics);
                    e.render(graphics, scaledMouseX, scaledMouseY, partial);
                }
                leftX += e.getWidth();
            }
            int rightX = scaledWidth;
            for (MenuBar.MenuBarEntry e : this.rightEntries) {
                e.x = rightX - e.getWidth();
                e.y = y;
                e.height = this.height;
                e.hovered = e.isMouseOver((double) scaledMouseX, (double) scaledMouseY);
                if (e.isVisible()) {
                    RenderSystem.enableBlend();
                    UIBase.resetShaderColor(graphics);
                    e.render(graphics, scaledMouseX, scaledMouseY, partial);
                }
                rightX -= e.getWidth();
            }
        } else {
            this.collapseOrExpandEntry.m_88315_(graphics, scaledMouseX, scaledMouseY, partial);
        }
        if (this.expanded) {
            this.renderBottomLine(graphics, scaledWidth, this.height);
        } else {
            this.renderExpandEntryBorder(graphics, scaledWidth, this.height);
        }
        graphics.pose().popPose();
        UIBase.resetShaderColor(graphics);
        graphics.pose().pushPose();
        RenderSystem.enableDepthTest();
        for (MenuBar.MenuBarEntry e : ListUtils.mergeLists(this.leftEntries, this.rightEntries)) {
            if (e instanceof MenuBar.ContextMenuBarEntry c) {
                c.contextMenu.render(graphics, mouseX, mouseY, partial);
            }
        }
        RenderSystem.disableDepthTest();
        graphics.pose().popPose();
        UIBase.resetShaderColor(graphics);
    }

    protected void renderBackground(GuiGraphics graphics, int xMin, int yMin, int xMax, int yMax) {
        graphics.fill(xMin, yMin, xMax, yMax, UIBase.getUIColorTheme().element_background_color_normal.getColorInt());
        UIBase.resetShaderColor(graphics);
    }

    protected void renderBottomLine(GuiGraphics graphics, int width, int height) {
        graphics.fill(0, height - this.getBottomLineThickness(), width, height, UIBase.getUIColorTheme().menu_bar_bottom_line_color.getColorInt());
        UIBase.resetShaderColor(graphics);
    }

    protected void renderExpandEntryBorder(GuiGraphics graphics, int width, int height) {
        graphics.fill(this.collapseOrExpandEntry.x, height - this.getBottomLineThickness(), width, height, UIBase.getUIColorTheme().menu_bar_bottom_line_color.getColorInt());
        graphics.fill(this.collapseOrExpandEntry.x - this.getBottomLineThickness(), 0, this.collapseOrExpandEntry.x, height, UIBase.getUIColorTheme().menu_bar_bottom_line_color.getColorInt());
        UIBase.resetShaderColor(graphics);
    }

    @NotNull
    public MenuBar.SpacerMenuBarEntry addSpacerEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier) {
        return this.addEntryAfter(addAfterIdentifier, new MenuBar.SpacerMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SpacerMenuBarEntry addSpacerEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier) {
        return this.addEntryBefore(addBeforeIdentifier, new MenuBar.SpacerMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SpacerMenuBarEntry addSpacerEntry(@NotNull MenuBar.Side side, @NotNull String identifier) {
        return this.addEntry(side, new MenuBar.SpacerMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SpacerMenuBarEntry addSpacerEntryAt(int index, @NotNull MenuBar.Side side, @NotNull String identifier) {
        return this.addEntryAt(index, side, new MenuBar.SpacerMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SeparatorMenuBarEntry addSeparatorEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier) {
        return this.addEntryAfter(addAfterIdentifier, new MenuBar.SeparatorMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SeparatorMenuBarEntry addSeparatorEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier) {
        return this.addEntryBefore(addBeforeIdentifier, new MenuBar.SeparatorMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SeparatorMenuBarEntry addSeparatorEntry(@NotNull MenuBar.Side side, @NotNull String identifier) {
        return this.addEntry(side, new MenuBar.SeparatorMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.SeparatorMenuBarEntry addSeparatorEntryAt(int index, @NotNull MenuBar.Side side, @NotNull String identifier) {
        return this.addEntryAt(index, side, new MenuBar.SeparatorMenuBarEntry(identifier, this));
    }

    @NotNull
    public MenuBar.ContextMenuBarEntry addContextMenuEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu contextMenu) {
        return this.addEntryAfter(addAfterIdentifier, new MenuBar.ContextMenuBarEntry(identifier, this, label, contextMenu));
    }

    @NotNull
    public MenuBar.ContextMenuBarEntry addContextMenuEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu contextMenu) {
        return this.addEntryBefore(addBeforeIdentifier, new MenuBar.ContextMenuBarEntry(identifier, this, label, contextMenu));
    }

    @NotNull
    public MenuBar.ContextMenuBarEntry addContextMenuEntry(@NotNull String identifier, @NotNull Component label, @NotNull ContextMenu contextMenu) {
        return this.addEntry(MenuBar.Side.LEFT, new MenuBar.ContextMenuBarEntry(identifier, this, label, contextMenu));
    }

    @NotNull
    public MenuBar.ContextMenuBarEntry addContextMenuEntryAt(int index, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu contextMenu) {
        return this.addEntryAt(index, MenuBar.Side.LEFT, new MenuBar.ContextMenuBarEntry(identifier, this, label, contextMenu));
    }

    @NotNull
    public MenuBar.ClickableMenuBarEntry addClickableEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
        return this.addEntryAfter(addAfterIdentifier, new MenuBar.ClickableMenuBarEntry(identifier, this, label, clickAction));
    }

    @NotNull
    public MenuBar.ClickableMenuBarEntry addClickableEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
        return this.addEntryBefore(addBeforeIdentifier, new MenuBar.ClickableMenuBarEntry(identifier, this, label, clickAction));
    }

    @NotNull
    public MenuBar.ClickableMenuBarEntry addClickableEntry(@NotNull MenuBar.Side side, @NotNull String identifier, @NotNull Component label, @NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
        return this.addEntry(side, new MenuBar.ClickableMenuBarEntry(identifier, this, label, clickAction));
    }

    @NotNull
    public MenuBar.ClickableMenuBarEntry addClickableEntryAt(int index, @NotNull MenuBar.Side side, @NotNull String identifier, @NotNull Component label, @NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
        return this.addEntryAt(index, side, new MenuBar.ClickableMenuBarEntry(identifier, this, label, clickAction));
    }

    @NotNull
    public <T extends MenuBar.MenuBarEntry> T addEntryAfter(@NotNull String addAfterIdentifier, @NotNull T entry) {
        Objects.requireNonNull(addAfterIdentifier);
        int index = this.getEntryIndex(addAfterIdentifier);
        MenuBar.Side side = this.getEntrySide(addAfterIdentifier);
        if (index >= 0 && side != null) {
            index++;
        } else {
            LOGGER.error("[FANCYMENU] Failed to add MenuBar entry (" + entry.identifier + ") after other entry (" + addAfterIdentifier + ")! Target entry not found! Will add the entry at the end of left side instead!");
            index = this.leftEntries.size();
            side = MenuBar.Side.LEFT;
        }
        return this.addEntryAt(index, side, entry);
    }

    @NotNull
    public <T extends MenuBar.MenuBarEntry> T addEntryBefore(@NotNull String addBeforeIdentifier, @NotNull T entry) {
        Objects.requireNonNull(addBeforeIdentifier);
        int index = this.getEntryIndex(addBeforeIdentifier);
        MenuBar.Side side = this.getEntrySide(addBeforeIdentifier);
        if (index < 0 || side == null) {
            LOGGER.error("[FANCYMENU] Failed to add MenuBar entry (" + entry.identifier + ") before other entry (" + addBeforeIdentifier + ")! Target entry not found! Will add the entry at the end of left side instead!");
            index = this.leftEntries.size();
            side = MenuBar.Side.LEFT;
        }
        return this.addEntryAt(index, side, entry);
    }

    @NotNull
    public <T extends MenuBar.MenuBarEntry> T addEntry(@NotNull MenuBar.Side side, @NotNull T entry) {
        int index = side == MenuBar.Side.LEFT ? this.leftEntries.size() : this.rightEntries.size();
        return this.addEntryAt(index, side, entry);
    }

    @NotNull
    public <T extends MenuBar.MenuBarEntry> T addEntryAt(int index, @NotNull MenuBar.Side side, @NotNull T entry) {
        Objects.requireNonNull(side);
        Objects.requireNonNull(entry);
        Objects.requireNonNull(entry.identifier);
        if (this.hasEntry(entry.identifier)) {
            LOGGER.error("[FANCYMENU] Failed to add MenuBar entry! Identifier already in use: " + entry.identifier);
        } else {
            if (side == MenuBar.Side.LEFT) {
                this.leftEntries.add(Math.max(0, Math.min(index, this.leftEntries.size())), entry);
            }
            if (side == MenuBar.Side.RIGHT) {
                this.rightEntries.add(Math.max(0, Math.min(index, this.rightEntries.size())), entry);
            }
        }
        return entry;
    }

    public MenuBar removeEntry(@NotNull String identifier) {
        MenuBar.MenuBarEntry e = this.getEntry(identifier);
        if (e != null) {
            this.leftEntries.remove(e);
            this.rightEntries.remove(e);
        }
        return this;
    }

    public MenuBar clearLeftEntries() {
        this.leftEntries.clear();
        return this;
    }

    public MenuBar clearRightEntries() {
        this.rightEntries.clear();
        return this;
    }

    public MenuBar clearEntries() {
        this.leftEntries.clear();
        this.rightEntries.clear();
        return this;
    }

    public int getEntryIndex(@NotNull String identifier) {
        MenuBar.MenuBarEntry e = this.getEntry(identifier);
        if (e != null) {
            int index = this.leftEntries.indexOf(e);
            if (index == -1) {
                index = this.rightEntries.indexOf(e);
            }
            return index;
        } else {
            return -1;
        }
    }

    @Nullable
    public MenuBar.Side getEntrySide(@NotNull String identifier) {
        MenuBar.MenuBarEntry e = this.getEntry(identifier);
        if (e != null) {
            return this.leftEntries.contains(e) ? MenuBar.Side.LEFT : MenuBar.Side.RIGHT;
        } else {
            return null;
        }
    }

    @Nullable
    public MenuBar.MenuBarEntry getEntry(@NotNull String identifier) {
        Objects.requireNonNull(identifier);
        for (MenuBar.MenuBarEntry e : this.getEntries()) {
            if (e.identifier.equals(identifier)) {
                return e;
            }
        }
        return null;
    }

    public boolean hasEntry(@NotNull String identifier) {
        return this.getEntry(identifier) != null;
    }

    @NotNull
    public List<MenuBar.MenuBarEntry> getLeftEntries() {
        return new ArrayList(this.leftEntries);
    }

    @NotNull
    public List<MenuBar.MenuBarEntry> getRightEntries() {
        return new ArrayList(this.rightEntries);
    }

    @NotNull
    public List<MenuBar.MenuBarEntry> getEntries() {
        return ListUtils.mergeLists(this.leftEntries, this.rightEntries);
    }

    public int getHeight() {
        return this.height;
    }

    public MenuBar setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getBottomLineThickness() {
        return 1;
    }

    public float getScale() {
        return this.scale;
    }

    public MenuBar setScale(float scale) {
        if (this.forceUIScale) {
            LOGGER.error("[FANCYMENU] Unable to set scale of MenuBar while MenuBar#isForceUIScale()!");
        }
        this.scale = scale;
        return this;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public boolean isUserNavigatingInMenuBar() {
        if (this.isHovered()) {
            return true;
        } else {
            for (MenuBar.MenuBarEntry e : ListUtils.mergeLists(this.leftEntries, this.rightEntries)) {
                if (e instanceof MenuBar.ContextMenuBarEntry c && c.contextMenu.isUserNavigatingInMenu()) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isForceUIScale() {
        return this.forceUIScale;
    }

    public MenuBar setForceUIScale(boolean forceUIScale) {
        this.forceUIScale = forceUIScale;
        return this;
    }

    public boolean isEntryContextMenuOpen() {
        for (MenuBar.MenuBarEntry e : this.getEntries()) {
            if (e instanceof MenuBar.ContextMenuBarEntry c && c.contextMenu.isOpen()) {
                return true;
            }
        }
        return false;
    }

    public MenuBar closeAllContextMenus() {
        for (MenuBar.MenuBarEntry e : this.getEntries()) {
            if (e instanceof MenuBar.ContextMenuBarEntry c) {
                c.contextMenu.closeMenu();
            }
        }
        return this;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public MenuBar setExpanded(boolean expanded) {
        this.expanded = expanded;
        if (!this.expanded) {
            this.closeAllContextMenus();
        }
        return this;
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float scale = UIBase.calculateFixedScale(this.scale);
        int scaledMouseX = (int) ((float) mouseX / scale);
        int scaledMouseY = (int) ((float) mouseY / scale);
        boolean entryClick = false;
        if (this.expanded) {
            for (MenuBar.MenuBarEntry e : ListUtils.mergeLists(this.leftEntries, this.rightEntries)) {
                if (e.isVisible()) {
                    if (e instanceof MenuBar.ContextMenuBarEntry) {
                        MenuBar.ContextMenuBarEntry c = (MenuBar.ContextMenuBarEntry) e;
                        if (c.contextMenu.mouseClicked(mouseX, mouseY, button)) {
                            entryClick = true;
                        }
                    }
                    if (e.mouseClicked((double) scaledMouseX, (double) scaledMouseY, button)) {
                        entryClick = true;
                    }
                }
            }
        } else if (this.collapseOrExpandEntry.mouseClicked((double) scaledMouseX, (double) scaledMouseY, button)) {
            entryClick = true;
        }
        return this.isUserNavigatingInMenuBar() ? true : entryClick;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!this.expanded) {
            return this.collapseOrExpandEntry.hovered;
        } else {
            float scale = UIBase.calculateFixedScale(this.scale);
            int width = ScreenUtils.getScreenWidth();
            int scaledHeight = this.getHeight() != 0 ? (int) ((float) this.getHeight() * scale) : 0;
            return UIBase.isXYInArea((int) mouseX, (int) mouseY, 0, 0, width, scaledHeight);
        }
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public void setFocusable(boolean focusable) {
        throw new RuntimeException("MenuBars are not focusable!");
    }

    @Override
    public boolean isNavigatable() {
        return false;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        throw new RuntimeException("ContextMenus are not navigatable!");
    }

    public static class ClickableMenuBarEntry extends MenuBar.MenuBarEntry {

        @NotNull
        protected MenuBar.MenuBarEntry.MenuBarEntrySupplier<Component> labelSupplier;

        @Nullable
        protected MenuBar.MenuBarEntry.MenuBarEntrySupplier<ITexture> iconTextureSupplier;

        @Nullable
        protected Supplier<DrawableColor> iconTextureColor = () -> UIBase.getUIColorTheme().ui_texture_color;

        @NotNull
        protected MenuBar.ClickableMenuBarEntry.ClickAction clickAction;

        protected Font font;

        public ClickableMenuBarEntry(@NotNull String identifier, @NotNull MenuBar menuBar, @NotNull Component label, @NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
            super(identifier, menuBar);
            this.font = Minecraft.getInstance().font;
            this.labelSupplier = (bar, entry) -> label;
            this.clickAction = clickAction;
        }

        @Override
        protected void renderEntry(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.renderBackground(graphics);
            this.renderLabelOrIcon(graphics);
        }

        protected void renderBackground(GuiGraphics graphics) {
            UIBase.resetShaderColor(graphics);
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + this.height, this.getBackgroundColor().getColorInt());
            UIBase.resetShaderColor(graphics);
        }

        protected void renderLabelOrIcon(GuiGraphics graphics) {
            RenderSystem.enableBlend();
            Component label = this.getLabel();
            ITexture iconTexture = this.getIconTexture();
            if (iconTexture != null) {
                int[] size = iconTexture.getAspectRatio().getAspectRatioSizeByMaximumSize(this.getWidth(), this.height);
                UIBase.resetShaderColor(graphics);
                DrawableColor iconColor = this.iconTextureColor != null ? (DrawableColor) this.iconTextureColor.get() : null;
                if (iconColor != null) {
                    UIBase.setShaderColor(graphics, iconColor);
                }
                ResourceLocation loc = iconTexture.getResourceLocation() != null ? iconTexture.getResourceLocation() : ITexture.MISSING_TEXTURE_LOCATION;
                graphics.blit(loc, this.x, this.y, 0.0F, 0.0F, size[0], size[1], size[0], size[1]);
            } else {
                UIBase.drawElementLabel(graphics, this.font, label, this.x + 6, this.y + this.height / 2 - 9 / 2, this.isActive() ? UIBase.getUIColorTheme().element_label_color_normal.getColorInt() : UIBase.getUIColorTheme().element_label_color_inactive.getColorInt());
            }
            UIBase.resetShaderColor(graphics);
        }

        @Override
        protected int getWidth() {
            Component label = this.getLabel();
            ITexture iconTexture = this.getIconTexture();
            return iconTexture != null ? iconTexture.getAspectRatio().getAspectRatioWidth(this.height) : this.font.width(label) + 12;
        }

        public MenuBar.ClickableMenuBarEntry setActive(boolean active) {
            return (MenuBar.ClickableMenuBarEntry) super.setActive(active);
        }

        public MenuBar.ClickableMenuBarEntry setActiveSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier) {
            return (MenuBar.ClickableMenuBarEntry) super.setActiveSupplier(activeSupplier);
        }

        public MenuBar.ClickableMenuBarEntry setVisible(boolean visible) {
            return (MenuBar.ClickableMenuBarEntry) super.setVisible(visible);
        }

        public MenuBar.ClickableMenuBarEntry setVisibleSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier) {
            return (MenuBar.ClickableMenuBarEntry) super.setVisibleSupplier(visibleSupplier);
        }

        public MenuBar.ClickableMenuBarEntry setIconTextureColor(@Nullable Supplier<DrawableColor> iconTextureColor) {
            this.iconTextureColor = iconTextureColor;
            return this;
        }

        @NotNull
        protected DrawableColor getBackgroundColor() {
            return this.isHovered() && this.isActive() ? UIBase.getUIColorTheme().element_background_color_hover : UIBase.getUIColorTheme().element_background_color_normal;
        }

        @NotNull
        protected Component getLabel() {
            Component c = this.labelSupplier.get(this.parent, this);
            return (Component) (c != null ? c : Component.empty());
        }

        public MenuBar.ClickableMenuBarEntry setLabelSupplier(@NotNull MenuBar.MenuBarEntry.MenuBarEntrySupplier<Component> labelSupplier) {
            this.labelSupplier = labelSupplier;
            return this;
        }

        public MenuBar.ClickableMenuBarEntry setLabel(@NotNull Component label) {
            this.labelSupplier = (bar, entry) -> label;
            return this;
        }

        @Nullable
        protected ITexture getIconTexture() {
            return this.iconTextureSupplier != null ? this.iconTextureSupplier.get(this.parent, this) : null;
        }

        @Nullable
        public MenuBar.MenuBarEntry.MenuBarEntrySupplier<ITexture> getIconTextureSupplier() {
            return this.iconTextureSupplier;
        }

        public MenuBar.ClickableMenuBarEntry setIconTextureSupplier(@Nullable MenuBar.MenuBarEntry.MenuBarEntrySupplier<ITexture> iconTextureSupplier) {
            this.iconTextureSupplier = iconTextureSupplier;
            return this;
        }

        public MenuBar.ClickableMenuBarEntry setIconTexture(@Nullable ITexture iconTexture) {
            this.iconTextureSupplier = iconTexture != null ? (bar, entry) -> iconTexture : null;
            return this;
        }

        @NotNull
        public MenuBar.ClickableMenuBarEntry.ClickAction getClickAction() {
            return this.clickAction;
        }

        public MenuBar.ClickableMenuBarEntry setClickAction(@NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
            this.clickAction = clickAction;
            return this;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && this.isActive() && this.isVisible() && this.isHovered()) {
                if (FancyMenu.getOptions().playUiClickSounds.getValue()) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                this.clickAction.onClick(this.parent, this);
                return true;
            } else {
                return false;
            }
        }

        @FunctionalInterface
        public interface ClickAction {

            void onClick(MenuBar var1, MenuBar.MenuBarEntry var2);
        }
    }

    public static class ContextMenuBarEntry extends MenuBar.ClickableMenuBarEntry {

        protected ContextMenu contextMenu;

        public ContextMenuBarEntry(@NotNull String identifier, @NotNull MenuBar menuBar, @NotNull Component label, ContextMenu contextMenu) {
            super(identifier, menuBar, label, (bar, entry) -> {
            });
            this.contextMenu = contextMenu;
            this.contextMenu.setShadow(false);
            this.contextMenu.setKeepDistanceToEdges(false);
            this.contextMenu.setForceUIScale(false);
            this.contextMenu.setForceRawXY(true);
            this.contextMenu.setForceSide(true);
            this.contextMenu.setForceSideSubMenus(false);
            for (ContextMenu.ContextMenuEntry<?> e : this.contextMenu.getEntries()) {
                if (e instanceof ContextMenu.SubMenuContextMenuEntry s) {
                    s.getSubContextMenu().setForceSide(true);
                    s.getSubContextMenu().setForceSideSubMenus(false);
                }
            }
            this.clickAction = (bar, entry) -> this.openContextMenu();
        }

        public void openContextMenu() {
            this.openContextMenu(null);
        }

        public void openContextMenu(@Nullable List<String> entryPath) {
            this.contextMenu.setScale(this.parent.scale);
            float scale = UIBase.calculateFixedScale(this.parent.scale);
            float scaledX = (float) this.x * scale;
            float scaledY = (float) this.y * scale;
            float scaledHeight = (float) this.height * scale;
            this.contextMenu.openMenuAt(scaledX, scaledY + scaledHeight - this.contextMenu.getScaledBorderThickness(), entryPath);
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.contextMenu.setScale(this.parent.scale);
            this.handleOpenOnHover();
            super.m_88315_(graphics, mouseX, mouseY, partial);
        }

        protected void handleOpenOnHover() {
            if (this.isHovered() && this.isActive() && this.isVisible() && !this.contextMenu.isOpen() && this.parent.isEntryContextMenuOpen()) {
                this.parent.closeAllContextMenus();
                this.openContextMenu();
            }
        }

        public ContextMenu getContextMenu() {
            return this.contextMenu;
        }

        public MenuBar.ContextMenuBarEntry setActive(boolean active) {
            return (MenuBar.ContextMenuBarEntry) super.setActive(active);
        }

        public MenuBar.ContextMenuBarEntry setActiveSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier) {
            return (MenuBar.ContextMenuBarEntry) super.setActiveSupplier(activeSupplier);
        }

        public MenuBar.ContextMenuBarEntry setVisible(boolean visible) {
            return (MenuBar.ContextMenuBarEntry) super.setVisible(visible);
        }

        public MenuBar.ContextMenuBarEntry setVisibleSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier) {
            return (MenuBar.ContextMenuBarEntry) super.setVisibleSupplier(visibleSupplier);
        }

        public MenuBar.ContextMenuBarEntry setLabel(@NotNull Component label) {
            return (MenuBar.ContextMenuBarEntry) super.setLabel(label);
        }

        public MenuBar.ContextMenuBarEntry setLabelSupplier(@NotNull MenuBar.MenuBarEntry.MenuBarEntrySupplier<Component> labelSupplier) {
            return (MenuBar.ContextMenuBarEntry) super.setLabelSupplier(labelSupplier);
        }

        public MenuBar.ContextMenuBarEntry setIconTexture(@Nullable ITexture iconTexture) {
            return (MenuBar.ContextMenuBarEntry) super.setIconTexture(iconTexture);
        }

        public MenuBar.ContextMenuBarEntry setIconTextureSupplier(@Nullable MenuBar.MenuBarEntry.MenuBarEntrySupplier<ITexture> iconTextureSupplier) {
            return (MenuBar.ContextMenuBarEntry) super.setIconTextureSupplier(iconTextureSupplier);
        }

        public MenuBar.ContextMenuBarEntry setClickAction(@NotNull MenuBar.ClickableMenuBarEntry.ClickAction clickAction) {
            MenuBar.LOGGER.error("[FANCYMENU] You can't change the click action of ContextMenuBarEntries!");
            return this;
        }

        @NotNull
        @Override
        protected DrawableColor getBackgroundColor() {
            return this.contextMenu.isOpen() ? UIBase.getUIColorTheme().element_background_color_hover : super.getBackgroundColor();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if ((!this.isHovered() || !this.isActive() || !this.isVisible()) && !this.contextMenu.isUserNavigatingInMenu() && this.contextMenu.isOpen()) {
                this.contextMenu.closeMenu();
                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public abstract static class MenuBarEntry implements Renderable, GuiEventListener {

        protected final String identifier;

        @NotNull
        protected MenuBar parent;

        protected int x;

        protected int y;

        protected int height;

        protected boolean hovered = false;

        protected MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier;

        protected MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier;

        @Nullable
        protected ConsumingSupplier<MenuBar.MenuBarEntry, Tooltip> tooltipSupplier;

        public MenuBarEntry(@NotNull String identifier, @NotNull MenuBar parent) {
            this.identifier = identifier;
            this.parent = parent;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.renderEntry(graphics, mouseX, mouseY, partial);
            if (this.hovered && this.tooltipSupplier != null) {
                Tooltip tooltip = this.tooltipSupplier.get(this);
                if (tooltip != null) {
                    tooltip.setDefaultStyle();
                    tooltip.setScale(this.parent.scale);
                    TooltipHandler.INSTANCE.addTooltip(tooltip, () -> true, false, true);
                }
            }
        }

        protected abstract void renderEntry(@NotNull GuiGraphics var1, int var2, int var3, float var4);

        protected int getWidth() {
            return 20;
        }

        public boolean isHovered() {
            return this.hovered;
        }

        public boolean isActive() {
            return this.activeSupplier == null || this.activeSupplier.get(this.parent, this);
        }

        public MenuBar.MenuBarEntry setActive(boolean active) {
            this.activeSupplier = (menuBar, entry) -> active;
            return this;
        }

        public MenuBar.MenuBarEntry setActiveSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier) {
            this.activeSupplier = activeSupplier;
            return this;
        }

        public boolean isVisible() {
            return this.visibleSupplier == null || this.visibleSupplier.get(this.parent, this);
        }

        public MenuBar.MenuBarEntry setVisible(boolean visible) {
            this.visibleSupplier = (menuBar, entry) -> visible;
            return this;
        }

        public MenuBar.MenuBarEntry setVisibleSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier) {
            this.visibleSupplier = visibleSupplier;
            return this;
        }

        public MenuBar.MenuBarEntry setTooltipSupplier(@Nullable ConsumingSupplier<MenuBar.MenuBarEntry, Tooltip> tooltipSupplier) {
            this.tooltipSupplier = tooltipSupplier;
            return this;
        }

        @NotNull
        public String getIdentifier() {
            return this.identifier;
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return UIBase.isXYInArea((int) mouseX, (int) mouseY, this.x, this.y, this.getWidth(), this.height);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
        }

        @FunctionalInterface
        public interface MenuBarEntryBooleanSupplier {

            boolean get(MenuBar var1, MenuBar.MenuBarEntry var2);
        }

        @FunctionalInterface
        public interface MenuBarEntrySupplier<T> {

            T get(MenuBar var1, MenuBar.MenuBarEntry var2);
        }
    }

    public static class SeparatorMenuBarEntry extends MenuBar.MenuBarEntry {

        @NotNull
        protected DrawableColor color = UIBase.getUIColorTheme().element_border_color_normal;

        public SeparatorMenuBarEntry(@NotNull String identifier, @NotNull MenuBar parent) {
            super(identifier, parent);
        }

        @Override
        protected void renderEntry(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            RenderSystem.enableBlend();
            UIBase.resetShaderColor(graphics);
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + this.height, this.color.getColorInt());
            UIBase.resetShaderColor(graphics);
        }

        @Override
        protected int getWidth() {
            return 1;
        }

        public MenuBar.SeparatorMenuBarEntry setActive(boolean active) {
            return (MenuBar.SeparatorMenuBarEntry) super.setActive(active);
        }

        public MenuBar.SeparatorMenuBarEntry setActiveSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier) {
            return (MenuBar.SeparatorMenuBarEntry) super.setActiveSupplier(activeSupplier);
        }

        public MenuBar.SeparatorMenuBarEntry setVisible(boolean visible) {
            return (MenuBar.SeparatorMenuBarEntry) super.setVisible(visible);
        }

        public MenuBar.SeparatorMenuBarEntry setVisibleSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier) {
            return (MenuBar.SeparatorMenuBarEntry) super.setVisibleSupplier(visibleSupplier);
        }

        @NotNull
        public DrawableColor getColor() {
            return this.color;
        }

        public MenuBar.SeparatorMenuBarEntry setColor(@NotNull DrawableColor color) {
            this.color = color;
            return this;
        }
    }

    public static enum Side {

        LEFT, RIGHT
    }

    public static class SpacerMenuBarEntry extends MenuBar.MenuBarEntry {

        protected int width = 10;

        public SpacerMenuBarEntry(@NotNull String identifier, @NotNull MenuBar menuBar) {
            super(identifier, menuBar);
        }

        @Override
        protected void renderEntry(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            RenderSystem.enableBlend();
            UIBase.resetShaderColor(graphics);
            this.renderBackground(graphics);
        }

        protected void renderBackground(GuiGraphics graphics) {
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + this.height, UIBase.getUIColorTheme().element_background_color_normal.getColorInt());
            UIBase.resetShaderColor(graphics);
        }

        @Override
        protected int getWidth() {
            return this.width;
        }

        public MenuBar.SpacerMenuBarEntry setActive(boolean active) {
            return (MenuBar.SpacerMenuBarEntry) super.setActive(active);
        }

        public MenuBar.SpacerMenuBarEntry setActiveSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier activeSupplier) {
            return (MenuBar.SpacerMenuBarEntry) super.setActiveSupplier(activeSupplier);
        }

        public MenuBar.SpacerMenuBarEntry setVisible(boolean visible) {
            return (MenuBar.SpacerMenuBarEntry) super.setVisible(visible);
        }

        public MenuBar.SpacerMenuBarEntry setVisibleSupplier(MenuBar.MenuBarEntry.MenuBarEntryBooleanSupplier visibleSupplier) {
            return (MenuBar.SpacerMenuBarEntry) super.setVisibleSupplier(visibleSupplier);
        }

        public MenuBar.SpacerMenuBarEntry setWidth(int width) {
            this.width = width;
            return this;
        }
    }
}