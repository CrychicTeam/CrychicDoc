package de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.util.cycle.ILocalizedValueCycle;
import de.keksuccino.fancymenu.util.properties.RuntimePropertyContainer;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.TooltipHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.konkrete.input.MouseInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContextMenu implements Renderable, GuiEventListener, NarratableEntry, NavigatableWidget {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation SUB_CONTEXT_MENU_ARROW_ICON = new ResourceLocation("fancymenu", "textures/contextmenu/context_menu_sub_arrow.png");

    private static final ResourceLocation CONTEXT_MENU_TOOLTIP_ICON = new ResourceLocation("fancymenu", "textures/contextmenu/context_menu_tooltip.png");

    private static final DrawableColor SHADOW_COLOR = DrawableColor.of(new Color(43, 43, 43, 100));

    protected final List<ContextMenu.ContextMenuEntry<?>> entries = new ArrayList();

    protected float scale = UIBase.getUIScale();

    protected boolean forceUIScale = true;

    protected boolean open = false;

    protected float rawX;

    protected float rawY;

    protected float rawWidth;

    protected float rawHeight;

    protected ContextMenu.SubMenuContextMenuEntry parentEntry = null;

    protected ContextMenu.SubMenuOpeningSide subMenuOpeningSide = ContextMenu.SubMenuOpeningSide.RIGHT;

    protected boolean shadow = true;

    protected boolean keepDistanceToEdges = true;

    protected boolean forceDefaultTooltipStyle = true;

    protected boolean forceRawXY = false;

    protected boolean forceSide = false;

    protected boolean forceSideSubMenus = true;

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.isOpen()) {
            if (this.forceUIScale) {
                this.scale = UIBase.getUIScale();
            }
            float scale = UIBase.calculateFixedScale(this.getScale());
            RenderSystem.enableBlend();
            UIBase.resetShaderColor(graphics);
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            graphics.pose().translate(0.0F, 0.0F, 500.0F / scale);
            List<ContextMenu.ContextMenuEntry<?>> renderEntries = new ArrayList();
            renderEntries.add(new ContextMenu.SpacerContextMenuEntry("unregistered_spacer_top", this));
            boolean addIconSpace = false;
            for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
                if (e instanceof ContextMenu.ClickableContextMenuEntry<?> c && c.icon != null) {
                    addIconSpace = true;
                    break;
                }
            }
            this.rawWidth = 20.0F;
            this.rawHeight = 0.0F;
            ContextMenu.ContextMenuEntry<?> prev = null;
            for (ContextMenu.ContextMenuEntry<?> ex : this.entries) {
                ex.addSpaceForIcon = addIconSpace;
                if (!(ex instanceof ContextMenu.SeparatorContextMenuEntry) || !(prev instanceof ContextMenu.SeparatorContextMenuEntry) && ex != this.entries.get(0) && ex != this.entries.get(this.entries.size() - 1)) {
                    if (!ex.isVisible()) {
                        prev = ex;
                    } else {
                        if (ex.tickAction != null) {
                            ex.tickAction.run(this, ex, false);
                        }
                        float w = ex.getMinWidth();
                        if (w > this.rawWidth) {
                            this.rawWidth = w;
                        }
                        this.rawHeight = this.rawHeight + ex.getHeight();
                        renderEntries.add(ex);
                        prev = ex;
                    }
                } else {
                    prev = ex;
                }
            }
            this.rawHeight += 8.0F;
            renderEntries.add(new ContextMenu.SpacerContextMenuEntry("unregistered_spacer_bottom", this));
            float x = this.getActualX();
            float y = this.getActualY();
            float scaledX = x / scale + this.getBorderThickness();
            float scaledY = y / scale + this.getBorderThickness();
            float scaledMouseX = (float) mouseX / scale;
            float scaledMouseY = (float) mouseY / scale;
            boolean navigatingInSub = this.isUserNavigatingInSubMenu();
            if (this.hasShadow()) {
                RenderingUtils.fillF(graphics, scaledX + 4.0F, scaledY + 4.0F, scaledX + this.getWidth() + 4.0F, scaledY + this.getHeight() + 4.0F, SHADOW_COLOR.getColorInt());
                UIBase.resetShaderColor(graphics);
            }
            RenderingUtils.fillF(graphics, scaledX, scaledY, scaledX + this.getWidth(), scaledY + this.getHeight(), UIBase.getUIColorTheme().element_background_color_normal.getColorInt());
            UIBase.resetShaderColor(graphics);
            float entryY = scaledY;
            for (ContextMenu.ContextMenuEntry<?> exx : renderEntries) {
                exx.x = scaledX;
                exx.y = entryY;
                exx.width = this.getWidth();
                boolean hover = exx.isHovered();
                exx.setHovered(!navigatingInSub && UIBase.isXYInArea((double) scaledMouseX, (double) scaledMouseY, (double) exx.x, (double) exx.y, (double) exx.width, (double) exx.getHeight()));
                if (!hover && exx.isHovered() && exx.hoverAction != null) {
                    exx.hoverAction.run(this, exx, false);
                }
                RenderSystem.enableBlend();
                UIBase.resetShaderColor(graphics);
                exx.render(graphics, (int) scaledMouseX, (int) scaledMouseY, partial);
                entryY += exx.getHeight();
            }
            UIBase.resetShaderColor(graphics);
            UIBase.renderBorder(graphics, scaledX - this.getBorderThickness(), scaledY - this.getBorderThickness(), scaledX + this.getWidth() + this.getBorderThickness(), scaledY + this.getHeight() + this.getBorderThickness(), this.getBorderThickness(), UIBase.getUIColorTheme().element_border_color_normal.getColorInt(), true, true, true, true);
            for (ContextMenu.ContextMenuEntry<?> exx : renderEntries) {
                if (exx.tickAction != null) {
                    exx.tickAction.run(this, exx, true);
                }
            }
            graphics.pose().popPose();
            for (ContextMenu.ContextMenuEntry<?> exxx : renderEntries) {
                if (exxx instanceof ContextMenu.SubMenuContextMenuEntry) {
                    ContextMenu.SubMenuContextMenuEntry s = (ContextMenu.SubMenuContextMenuEntry) exxx;
                    if (this.forceSideSubMenus) {
                        s.subContextMenu.forceSide = this.forceSide;
                    }
                    s.subContextMenu.forceRawXY = this.forceRawXY;
                    s.subContextMenu.shadow = this.shadow;
                    s.subContextMenu.scale = this.scale;
                    s.subContextMenu.forceUIScale = this.forceUIScale;
                    s.subContextMenu.render(graphics, mouseX, mouseY, partial);
                }
            }
            UIBase.resetShaderColor(graphics);
        }
    }

    @NotNull
    public ContextMenu.SubMenuContextMenuEntry addSubMenuEntryAt(int index, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu subContextMenu) {
        ContextMenu.SubMenuContextMenuEntry e = new ContextMenu.SubMenuContextMenuEntry(identifier, this, label, subContextMenu);
        return this.addEntryAt(index, e);
    }

    @NotNull
    public ContextMenu.SubMenuContextMenuEntry addSubMenuEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu subContextMenu) {
        ContextMenu.SubMenuContextMenuEntry e = new ContextMenu.SubMenuContextMenuEntry(identifier, this, label, subContextMenu);
        return this.addEntryBefore(addBeforeIdentifier, e);
    }

    @NotNull
    public ContextMenu.SubMenuContextMenuEntry addSubMenuEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu subContextMenu) {
        ContextMenu.SubMenuContextMenuEntry e = new ContextMenu.SubMenuContextMenuEntry(identifier, this, label, subContextMenu);
        return this.addEntryAfter(addAfterIdentifier, e);
    }

    @NotNull
    public ContextMenu.SubMenuContextMenuEntry addSubMenuEntry(@NotNull String identifier, @NotNull Component label, @NotNull ContextMenu subContextMenu) {
        ContextMenu.SubMenuContextMenuEntry e = new ContextMenu.SubMenuContextMenuEntry(identifier, this, label, subContextMenu);
        return this.addEntry(e);
    }

    @NotNull
    public ContextMenu.SeparatorContextMenuEntry addSeparatorEntryAt(int index, @NotNull String identifier) {
        ContextMenu.SeparatorContextMenuEntry e = new ContextMenu.SeparatorContextMenuEntry(identifier, this);
        return this.addEntryAt(index, e);
    }

    @NotNull
    public ContextMenu.SeparatorContextMenuEntry addSeparatorEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier) {
        ContextMenu.SeparatorContextMenuEntry e = new ContextMenu.SeparatorContextMenuEntry(identifier, this);
        return this.addEntryBefore(addBeforeIdentifier, e);
    }

    @NotNull
    public ContextMenu.SeparatorContextMenuEntry addSeparatorEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier) {
        ContextMenu.SeparatorContextMenuEntry e = new ContextMenu.SeparatorContextMenuEntry(identifier, this);
        return this.addEntryAfter(addAfterIdentifier, e);
    }

    @NotNull
    public ContextMenu.SeparatorContextMenuEntry addSeparatorEntry(@NotNull String identifier) {
        ContextMenu.SeparatorContextMenuEntry e = new ContextMenu.SeparatorContextMenuEntry(identifier, this);
        return this.addEntry(e);
    }

    @NotNull
    public <T> ContextMenu.ValueCycleContextMenuEntry<T> addValueCycleEntryAt(int index, @NotNull String identifier, @NotNull ILocalizedValueCycle<T> valueCycle) {
        ContextMenu.ValueCycleContextMenuEntry<T> e = new ContextMenu.ValueCycleContextMenuEntry<>(identifier, this, valueCycle);
        return this.addEntryAt(index, e);
    }

    @NotNull
    public <T> ContextMenu.ValueCycleContextMenuEntry<T> addValueCycleEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull ILocalizedValueCycle<T> valueCycle) {
        ContextMenu.ValueCycleContextMenuEntry<T> e = new ContextMenu.ValueCycleContextMenuEntry<>(identifier, this, valueCycle);
        return this.addEntryAfter(addAfterIdentifier, e);
    }

    @NotNull
    public <T> ContextMenu.ValueCycleContextMenuEntry<T> addValueCycleEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull ILocalizedValueCycle<T> valueCycle) {
        ContextMenu.ValueCycleContextMenuEntry<T> e = new ContextMenu.ValueCycleContextMenuEntry<>(identifier, this, valueCycle);
        return this.addEntryBefore(addBeforeIdentifier, e);
    }

    @NotNull
    public <T> ContextMenu.ValueCycleContextMenuEntry<T> addValueCycleEntry(@NotNull String identifier, @NotNull ILocalizedValueCycle<T> valueCycle) {
        ContextMenu.ValueCycleContextMenuEntry<T> e = new ContextMenu.ValueCycleContextMenuEntry<>(identifier, this, valueCycle);
        return this.addEntry(e);
    }

    @NotNull
    public ContextMenu.ClickableContextMenuEntry<?> addClickableEntryAt(int index, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
        ContextMenu.ClickableContextMenuEntry<?> e = new ContextMenu.ClickableContextMenuEntry(identifier, this, label, clickAction);
        return this.addEntryAt(index, e);
    }

    @NotNull
    public ContextMenu.ClickableContextMenuEntry<?> addClickableEntryAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
        ContextMenu.ClickableContextMenuEntry<?> e = new ContextMenu.ClickableContextMenuEntry(identifier, this, label, clickAction);
        return this.addEntryAfter(addAfterIdentifier, e);
    }

    @NotNull
    public ContextMenu.ClickableContextMenuEntry<?> addClickableEntryBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
        ContextMenu.ClickableContextMenuEntry<?> e = new ContextMenu.ClickableContextMenuEntry(identifier, this, label, clickAction);
        return this.addEntryBefore(addBeforeIdentifier, e);
    }

    @NotNull
    public ContextMenu.ClickableContextMenuEntry<?> addClickableEntry(@NotNull String identifier, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
        ContextMenu.ClickableContextMenuEntry<?> e = new ContextMenu.ClickableContextMenuEntry(identifier, this, label, clickAction);
        return this.addEntry(e);
    }

    @NotNull
    public <T extends ContextMenu.ContextMenuEntry<?>> T addEntryAfter(@NotNull String identifier, @NotNull T entry) {
        int index = this.getEntryIndex(identifier);
        if (index >= 0) {
            index++;
        } else {
            LOGGER.error("[FANCYMENU] Failed to add ContextMenu entry (" + entry.identifier + ") after other entry (" + identifier + ")! Target entry not found! Will add the entry at the end instead!");
            index = this.entries.size();
        }
        return this.addEntryAt(index, entry);
    }

    @NotNull
    public <T extends ContextMenu.ContextMenuEntry<?>> T addEntryBefore(@NotNull String identifier, @NotNull T entry) {
        int index = this.getEntryIndex(identifier);
        if (index < 0) {
            LOGGER.error("[FANCYMENU] Failed to add ContextMenu entry (" + entry.identifier + ") before other entry (" + identifier + ")! Target entry not found! Will add the entry at the end instead!");
            index = this.entries.size();
        }
        return this.addEntryAt(index, entry);
    }

    @NotNull
    public <T extends ContextMenu.ContextMenuEntry<?>> T addEntry(@NotNull T entry) {
        return this.addEntryAt(this.entries.size(), entry);
    }

    @NotNull
    public <T extends ContextMenu.ContextMenuEntry<?>> T addEntryAt(int index, @NotNull T entry) {
        if (this.hasEntry(entry.identifier)) {
            LOGGER.error("[FANCYMENU] Failed to add ContextMenu entry! Identifier already in use: " + entry.identifier);
        } else {
            this.entries.add(index, entry);
        }
        return entry;
    }

    public ContextMenu removeEntry(String identifier) {
        ContextMenu.ContextMenuEntry<?> e = this.getEntry(identifier);
        if (e != null) {
            this.entries.remove(e);
            e.onRemoved();
        }
        return this;
    }

    public ContextMenu clearEntries() {
        this.closeMenu();
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            e.onRemoved();
        }
        this.entries.clear();
        return this;
    }

    @Nullable
    public ContextMenu.ContextMenuEntry<?> getEntry(String identifier) {
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            if (e.identifier.equals(identifier)) {
                return e;
            }
        }
        return null;
    }

    public int getEntryIndex(String identifier) {
        ContextMenu.ContextMenuEntry<?> e = this.getEntry(identifier);
        return e != null ? this.entries.indexOf(e) : -1;
    }

    @NotNull
    public List<ContextMenu.ContextMenuEntry<?>> getEntries() {
        return new ArrayList(this.entries);
    }

    public boolean hasEntry(String identifier) {
        return this.getEntry(identifier) != null;
    }

    public float getScale() {
        return this.scale;
    }

    public ContextMenu setScale(float scale) {
        if (this.forceUIScale) {
            LOGGER.error("[FANCYMENU] Unable to set scale of ContextMenu while ContextMenu#isForceUIScale()!");
        }
        this.scale = scale;
        return this;
    }

    public boolean isForceUIScale() {
        return this.forceUIScale;
    }

    public ContextMenu setForceUIScale(boolean forceUIScale) {
        this.forceUIScale = forceUIScale;
        return this;
    }

    protected float getMinDistanceToScreenEdge() {
        return !this.keepDistanceToEdges ? 0.0F : 5.0F;
    }

    protected float getActualX() {
        if (this.isSubMenu()) {
            float cachedScale = this.parentEntry.parent.scale;
            float scale = UIBase.calculateFixedScale(this.scale);
            float scaledOffsetX = 5.0F * scale;
            ContextMenu.SubMenuOpeningSide side = this.getPossibleSubMenuOpeningSide();
            if (side == ContextMenu.SubMenuOpeningSide.LEFT) {
                float actualX = this.parentEntry.parent.getActualX() - this.getScaledWidth() + scaledOffsetX;
                this.parentEntry.parent.scale = cachedScale;
                return actualX;
            }
            if (side == ContextMenu.SubMenuOpeningSide.RIGHT) {
                float actualX = this.parentEntry.parent.getActualX() + this.parentEntry.parent.getScaledWidth() - scaledOffsetX;
                this.parentEntry.parent.scale = cachedScale;
                return actualX;
            }
        }
        if (this.forceRawXY) {
            return this.getX();
        } else {
            return this.getX() + this.getScaledWidthWithBorder() >= (float) getScreenWidth() - this.getMinDistanceToScreenEdge() ? (float) getScreenWidth() - this.getScaledWidthWithBorder() - this.getMinDistanceToScreenEdge() - 1.0F : Math.max(this.getX(), this.getMinDistanceToScreenEdge());
        }
    }

    protected float getActualY() {
        float y = this.getY();
        if (this.isSubMenu()) {
            float scale = UIBase.calculateFixedScale(this.scale);
            int scaledOffsetY = (int) (10.0F * scale);
            y = this.parentEntry.y * scale;
            y += (float) scaledOffsetY;
        }
        if (this.forceRawXY) {
            return y;
        } else {
            return y + this.getScaledHeightWithBorder() >= (float) getScreenHeight() - this.getMinDistanceToScreenEdge() ? (float) getScreenHeight() - this.getScaledHeightWithBorder() - this.getMinDistanceToScreenEdge() - 1.0F : Math.max(y, this.getMinDistanceToScreenEdge());
        }
    }

    @NotNull
    protected ContextMenu.SubMenuOpeningSide getPossibleSubMenuOpeningSide() {
        if (this.forceSide) {
            return this.subMenuOpeningSide;
        } else {
            if (this.isSubMenu()) {
                float potentialX = this.parentEntry.parent.getActualX() - this.getScaledWidth() + 5.0F;
                if (this.subMenuOpeningSide == ContextMenu.SubMenuOpeningSide.LEFT && potentialX < 5.0F) {
                    return ContextMenu.SubMenuOpeningSide.RIGHT;
                }
                potentialX = this.parentEntry.parent.getActualX() + this.parentEntry.parent.getScaledWidth() - 5.0F + this.getScaledWidth();
                if (this.subMenuOpeningSide == ContextMenu.SubMenuOpeningSide.RIGHT && potentialX > (float) (getScreenWidth() - 5)) {
                    return ContextMenu.SubMenuOpeningSide.LEFT;
                }
            }
            return this.subMenuOpeningSide;
        }
    }

    public float getBorderThickness() {
        return 1.0F;
    }

    public float getScaledBorderThickness() {
        float scale = UIBase.calculateFixedScale(this.scale);
        return this.getBorderThickness() * scale;
    }

    public float getX() {
        return this.rawX;
    }

    public float getY() {
        return this.rawY;
    }

    public float getWidth() {
        return this.rawWidth;
    }

    public float getWidthWithBorder() {
        return this.getWidth() + this.getBorderThickness() * 2.0F;
    }

    public float getScaledWidth() {
        float scale = UIBase.calculateFixedScale(this.scale);
        return this.getWidth() * scale;
    }

    public float getScaledWidthWithBorder() {
        return this.getScaledWidth() + this.getScaledBorderThickness() * 2.0F;
    }

    public float getHeight() {
        return this.rawHeight;
    }

    public float getHeightWithBorder() {
        return this.getHeight() + this.getBorderThickness() * 2.0F;
    }

    public float getScaledHeight() {
        float scale = UIBase.calculateFixedScale(this.scale);
        return this.getHeight() * scale;
    }

    public float getScaledHeightWithBorder() {
        return this.getScaledHeight() + this.getScaledBorderThickness() * 2.0F;
    }

    public boolean isHovered() {
        if (!this.isOpen()) {
            return false;
        } else {
            for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
                if (e.isHovered()) {
                    return true;
                }
            }
            return false;
        }
    }

    protected ContextMenu unhoverAllEntries() {
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            e.setHovered(false);
        }
        return this;
    }

    public boolean hasShadow() {
        return this.shadow;
    }

    public ContextMenu setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public boolean isForceDefaultTooltipStyle() {
        return this.forceDefaultTooltipStyle;
    }

    public ContextMenu setForceDefaultTooltipStyle(boolean forceDefaultTooltipStyle) {
        this.forceDefaultTooltipStyle = forceDefaultTooltipStyle;
        return this;
    }

    public boolean isKeepDistanceToEdges() {
        return this.keepDistanceToEdges;
    }

    public ContextMenu setKeepDistanceToEdges(boolean keepDistanceToEdges) {
        this.keepDistanceToEdges = keepDistanceToEdges;
        return this;
    }

    public boolean isForceRawXY() {
        return this.forceRawXY;
    }

    public ContextMenu setForceRawXY(boolean forceRawXY) {
        this.forceRawXY = forceRawXY;
        return this;
    }

    public boolean isForceSide() {
        return this.forceSide;
    }

    public ContextMenu setForceSide(boolean forceSide) {
        this.forceSide = forceSide;
        return this;
    }

    public boolean isForceSideSubMenus() {
        return this.forceSideSubMenus;
    }

    public ContextMenu setForceSideSubMenus(boolean forceSideSubMenus) {
        this.forceSideSubMenus = forceSideSubMenus;
        return this;
    }

    @NotNull
    public ContextMenu.SubMenuOpeningSide getSubMenuOpeningSide() {
        return this.subMenuOpeningSide;
    }

    public ContextMenu setSubMenuOpeningSide(@NotNull ContextMenu.SubMenuOpeningSide subMenuOpeningSide) {
        Objects.requireNonNull(subMenuOpeningSide);
        this.subMenuOpeningSide = subMenuOpeningSide;
        return this;
    }

    public boolean isSubMenu() {
        return this.parentEntry != null;
    }

    @Nullable
    public ContextMenu.SubMenuContextMenuEntry getParentEntry() {
        return this.parentEntry;
    }

    public boolean isSubMenuHovered() {
        if (!this.isOpen()) {
            return false;
        } else {
            for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
                if (e instanceof ContextMenu.SubMenuContextMenuEntry s && s.subContextMenu.isHovered()) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isSubMenuOpen() {
        if (!this.isOpen()) {
            return false;
        } else {
            for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
                if (e instanceof ContextMenu.SubMenuContextMenuEntry s && s.subContextMenu.isOpen()) {
                    return true;
                }
            }
            return false;
        }
    }

    public ContextMenu openMenuAt(float x, float y, @Nullable List<String> entryPath) {
        this.closeSubMenus();
        this.unhoverAllEntries();
        this.rawX = x;
        this.rawY = y;
        this.open = true;
        if (entryPath != null && !entryPath.isEmpty()) {
            String firstId = (String) entryPath.get(0);
            if (this.getEntry(firstId) instanceof ContextMenu.SubMenuContextMenuEntry sub) {
                sub.openSubMenu(entryPath.size() > 1 ? entryPath.subList(1, entryPath.size()) : null);
            }
        }
        return this;
    }

    public ContextMenu openMenuAt(float x, float y) {
        return this.openMenuAt(x, y, null);
    }

    public ContextMenu openMenuAtMouse(@Nullable List<String> entryPath) {
        return this.openMenuAt((float) MouseInput.getMouseX(), (float) MouseInput.getMouseY(), entryPath);
    }

    public ContextMenu openMenuAtMouse() {
        return this.openMenuAtMouse(null);
    }

    public ContextMenu closeMenu() {
        this.closeSubMenus();
        this.unhoverAllEntries();
        this.open = false;
        return this;
    }

    public ContextMenu closeSubMenus() {
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            if (e instanceof ContextMenu.SubMenuContextMenuEntry s) {
                s.subContextMenu.closeMenu();
            }
        }
        return this;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean isUserNavigatingInMenu() {
        return this.isHovered() || this.isUserNavigatingInSubMenu();
    }

    public boolean isUserNavigatingInSubMenu() {
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            if (e instanceof ContextMenu.SubMenuContextMenuEntry s && s.subContextMenu.isUserNavigatingInMenu()) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    protected List<ContextMenu.ContextMenuEntry<?>> getStackableEntries() {
        List<ContextMenu.ContextMenuEntry<?>> l = new ArrayList();
        for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
            if (e.isStackable()) {
                l.add(e);
            }
        }
        return l;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isUserNavigatingInMenu()) {
            return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
        } else {
            float scale = UIBase.calculateFixedScale(this.scale);
            int scaledMouseX = (int) ((float) mouseX / scale);
            int scaledMouseY = (int) ((float) mouseY / scale);
            for (ContextMenu.ContextMenuEntry<?> entry : this.entries) {
                entry.mouseClicked((double) scaledMouseX, (double) scaledMouseY, button);
            }
            for (ContextMenu.ContextMenuEntry<?> e : this.entries) {
                if (e instanceof ContextMenu.SubMenuContextMenuEntry s) {
                    s.subContextMenu.mouseClicked(mouseX, mouseY, button);
                }
            }
            return true;
        }
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
    public boolean isFocusable() {
        return false;
    }

    @Override
    public void setFocusable(boolean focusable) {
        throw new RuntimeException("ContextMenus are not focusable!");
    }

    @Override
    public boolean isNavigatable() {
        return false;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        throw new RuntimeException("ContextMenus are not navigatable!");
    }

    protected static int getScreenWidth() {
        Screen s = Minecraft.getInstance().screen;
        return s != null ? s.width : 1;
    }

    protected static int getScreenHeight() {
        Screen s = Minecraft.getInstance().screen;
        return s != null ? s.height : 1;
    }

    @NotNull
    public static ContextMenu stackContextMenus(@NotNull List<ContextMenu> menusToStack) {
        return stackContextMenus((ContextMenu[]) menusToStack.toArray(new ContextMenu[0]));
    }

    @NotNull
    public static ContextMenu stackContextMenus(@NotNull ContextMenu... menusToStack) {
        ContextMenu stacked = new ContextMenu();
        if (menusToStack.length > 0) {
            stacked.scale = menusToStack[0].scale;
            stacked.subMenuOpeningSide = menusToStack[0].subMenuOpeningSide;
            stacked.shadow = menusToStack[0].shadow;
            stacked.forceDefaultTooltipStyle = menusToStack[0].forceDefaultTooltipStyle;
            stacked.forceUIScale = menusToStack[0].forceUIScale;
            stacked.keepDistanceToEdges = menusToStack[0].keepDistanceToEdges;
            stacked.forceRawXY = menusToStack[0].forceRawXY;
            stacked.forceSide = menusToStack[0].forceSide;
            for (ContextMenu.ContextMenuEntry<?> ignoredEntry : menusToStack[0].getStackableEntries()) {
                RuntimePropertyContainer stackProperties = new RuntimePropertyContainer();
                List<ContextMenu.ContextMenuEntry<?>> entryStack = collectInstancesOfStackableEntryInMenus(ignoredEntry.identifier, menusToStack);
                if (!entryStack.isEmpty() && entryStack.size() == menusToStack.length) {
                    ContextMenu.ContextMenuEntry<?> firstOriginal = (ContextMenu.ContextMenuEntry<?>) entryStack.get(0);
                    List<ContextMenu.ContextMenuEntry<?>> entryStackCopyWithoutFirst = new ArrayList();
                    entryStack.forEach(entry -> {
                        if (entry != firstOriginal) {
                            entryStackCopyWithoutFirst.add(entry.copy());
                        }
                    });
                    ContextMenu.ContextMenuEntry<?> first = firstOriginal.copy();
                    first.stackMeta.firstInStack = true;
                    first.stackMeta.lastInStack = false;
                    first.stackMeta.partOfStack = true;
                    first.stackMeta.properties = stackProperties;
                    first.parent = stacked;
                    stacked.addEntry(first);
                    if (first instanceof ContextMenu.SubMenuContextMenuEntry s) {
                        s.setSubContextMenu(stackContextMenus(getSubContextMenusOfSubMenuEntries(entryStack)));
                        s.stackMeta.lastInStack = true;
                    } else {
                        ContextMenu.ContextMenuEntry<?> prev = first;
                        for (ContextMenu.ContextMenuEntry<?> e2 : entryStackCopyWithoutFirst) {
                            prev.stackMeta.nextInStack = e2;
                            prev = e2;
                            e2.stackMeta.properties = stackProperties;
                            e2.stackMeta.partOfStack = true;
                            e2.stackMeta.firstInStack = false;
                            e2.stackMeta.lastInStack = false;
                            e2.parent = stacked;
                        }
                        prev.stackMeta.lastInStack = true;
                    }
                }
            }
        }
        return stacked;
    }

    protected static List<ContextMenu.ContextMenuEntry<?>> collectInstancesOfStackableEntryInMenus(String entryIdentifier, ContextMenu[] menus) {
        List<ContextMenu.ContextMenuEntry<?>> entries = new ArrayList();
        for (ContextMenu m : menus) {
            ContextMenu.ContextMenuEntry<?> e = m.getEntry(entryIdentifier);
            if (e != null && e.isStackable() && e.isActive()) {
                entries.add(e);
            }
        }
        return entries;
    }

    protected static List<ContextMenu> getSubContextMenusOfSubMenuEntries(List<ContextMenu.ContextMenuEntry<?>> entries) {
        List<ContextMenu> l = new ArrayList();
        for (ContextMenu.ContextMenuEntry<?> e : entries) {
            if (e instanceof ContextMenu.SubMenuContextMenuEntry s) {
                l.add(s.subContextMenu);
            }
        }
        return l;
    }

    @FunctionalInterface
    public interface BooleanSupplier extends ContextMenu.Supplier<Boolean> {

        default boolean getBoolean(ContextMenu menu, ContextMenu.ContextMenuEntry<?> entry) {
            Boolean b = this.get(menu, entry);
            return b != null ? b : false;
        }
    }

    public static class ClickableContextMenuEntry<T extends ContextMenu.ClickableContextMenuEntry<T>> extends ContextMenu.ContextMenuEntry<T> {

        protected static final int ICON_WIDTH_HEIGHT = 10;

        @NotNull
        protected ContextMenu.ClickableContextMenuEntry.ClickAction clickAction;

        @NotNull
        protected ContextMenu.Supplier<Component> labelSupplier;

        @Nullable
        protected ContextMenu.Supplier<Component> shortcutTextSupplier;

        @Nullable
        protected ResourceLocation icon;

        protected boolean tooltipIconHovered = false;

        protected boolean tooltipActive = false;

        protected long tooltipIconHoverStart = -1L;

        protected boolean enableClickSound = true;

        public ClickableContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent, @NotNull Component label, @NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
            super(identifier, parent);
            this.clickAction = clickAction;
            this.labelSupplier = (menu, entry) -> label;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.renderBackground(graphics);
            int labelX = (int) (this.x + 10.0F);
            if (this.icon != null || this.addSpaceForIcon) {
                labelX += 20;
            }
            int labelY = (int) (this.y + this.height / 2.0F - (float) (9 / 2));
            UIBase.drawElementLabel(graphics, this.font, this.getLabel(), labelX, labelY, this.isActive() ? UIBase.getUIColorTheme().element_label_color_normal.getColorInt() : UIBase.getUIColorTheme().element_label_color_inactive.getColorInt());
            int shortcutTextWidth = 0;
            Component shortcutText = this.getShortcutText();
            if (shortcutText != null) {
                shortcutTextWidth = this.font.width(shortcutText);
                int shortcutX = (int) (this.x + this.width - 10.0F - (float) shortcutTextWidth);
                UIBase.drawElementLabel(graphics, this.font, shortcutText, shortcutX, labelY, this.isActive() ? UIBase.getUIColorTheme().element_label_color_normal.getColorInt() : UIBase.getUIColorTheme().element_label_color_inactive.getColorInt());
            }
            this.renderIcon(graphics);
            this.renderTooltipIconAndRegisterTooltip(graphics, mouseX, mouseY, shortcutTextWidth > 0 ? -(shortcutTextWidth + 8) : 0);
        }

        protected void renderIcon(GuiGraphics graphics) {
            if (this.icon != null) {
                RenderSystem.enableBlend();
                UIBase.getUIColorTheme().setUITextureShaderColor(graphics, 1.0F);
                graphics.blit(this.icon, (int) (this.x + 10.0F), (int) (this.y + this.getHeight() / 2.0F - 5.0F), 0.0F, 0.0F, 10, 10, 10, 10);
                UIBase.resetShaderColor(graphics);
            }
        }

        protected void renderTooltipIconAndRegisterTooltip(GuiGraphics graphics, int mouseX, int mouseY, int offsetX) {
            Tooltip tooltip = this.getTooltip();
            if (tooltip != null) {
                this.tooltipIconHovered = this.isTooltipIconHovered(mouseX, mouseY, offsetX);
                if (this.tooltipIconHovered) {
                    if (this.tooltipIconHoverStart == -1L) {
                        this.tooltipIconHoverStart = System.currentTimeMillis();
                    }
                } else {
                    this.tooltipIconHoverStart = -1L;
                }
                this.tooltipActive = this.tooltipIconHoverStart != -1L && this.tooltipIconHoverStart + 200L < System.currentTimeMillis();
                RenderSystem.enableBlend();
                UIBase.getUIColorTheme().setUITextureShaderColor(graphics, this.tooltipIconHovered ? 1.0F : 0.2F);
                graphics.blit(ContextMenu.CONTEXT_MENU_TOOLTIP_ICON, this.getTooltipIconX() + offsetX, this.getTooltipIconY(), 0.0F, 0.0F, 10, 10, 10, 10);
                UIBase.resetShaderColor(graphics);
                if (this.tooltipActive) {
                    if (this.parent.isForceDefaultTooltipStyle()) {
                        tooltip.setDefaultStyle();
                    }
                    tooltip.setScale(this.parent.scale);
                    TooltipHandler.INSTANCE.addTooltip(tooltip, () -> this.tooltipActive, false, true);
                }
            } else {
                this.tooltipIconHovered = false;
                this.tooltipActive = false;
            }
        }

        protected boolean isTooltipIconHovered(int mouseX, int mouseY, int offsetX) {
            return UIBase.isXYInArea(mouseX, mouseY, this.getTooltipIconX() + offsetX, this.getTooltipIconY(), 10, 10);
        }

        protected int getTooltipIconX() {
            return (int) (this.x + this.width - 20.0F);
        }

        protected int getTooltipIconY() {
            return (int) (this.y + 5.0F);
        }

        protected void renderBackground(@NotNull GuiGraphics graphics) {
            if (this.isChangeBackgroundColorOnHover() && this.isHovered() && this.isActive()) {
                RenderingUtils.fillF(graphics, this.x, this.y, this.x + this.width, this.y + this.height, UIBase.getUIColorTheme().element_background_color_hover.getColorInt());
            }
        }

        @Nullable
        public ResourceLocation getIcon() {
            return this.icon;
        }

        public T setIcon(@Nullable ResourceLocation icon) {
            this.icon = icon;
            return (T) this;
        }

        @NotNull
        public T setLabelSupplier(@NotNull ContextMenu.Supplier<Component> labelSupplier) {
            Objects.requireNonNull(labelSupplier);
            this.labelSupplier = labelSupplier;
            return (T) this;
        }

        @NotNull
        public Component getLabel() {
            Component c = this.labelSupplier.get(this.parent, this);
            Objects.requireNonNull(c);
            return c;
        }

        @NotNull
        public T setClickAction(@NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
            Objects.requireNonNull(clickAction);
            this.clickAction = clickAction;
            return (T) this;
        }

        @Nullable
        public Component getShortcutText() {
            return this.shortcutTextSupplier != null ? this.shortcutTextSupplier.get(this.parent, this) : null;
        }

        @NotNull
        public T setShortcutTextSupplier(@Nullable ContextMenu.Supplier<Component> shortcutTextSupplier) {
            this.shortcutTextSupplier = shortcutTextSupplier;
            return (T) this;
        }

        public boolean isClickSoundEnabled() {
            return this.enableClickSound;
        }

        public T setClickSoundEnabled(boolean enabled) {
            this.enableClickSound = enabled;
            return (T) this;
        }

        public ContextMenu.ClickableContextMenuEntry<T> copy() {
            ContextMenu.ClickableContextMenuEntry<T> copy = new ContextMenu.ClickableContextMenuEntry<>(this.identifier, this.parent, Component.literal(""), this.clickAction);
            copy.shortcutTextSupplier = this.shortcutTextSupplier;
            copy.labelSupplier = this.labelSupplier;
            copy.height = this.height;
            copy.tickAction = this.tickAction;
            copy.tooltipSupplier = this.tooltipSupplier;
            copy.activeStateSuppliers = new ArrayList(this.activeStateSuppliers);
            copy.icon = this.icon;
            return copy;
        }

        @Override
        public float getMinWidth() {
            int i = Minecraft.getInstance().font.width(this.getLabel()) + 20;
            if (this.tooltipSupplier != null) {
                i += 30;
            }
            Component shortcutText = this.getShortcutText();
            if (shortcutText != null) {
                i += Minecraft.getInstance().font.width(shortcutText) + 30;
            }
            if (this.icon != null || this.addSpaceForIcon) {
                i += 20;
            }
            return (float) i;
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && this.isHovered() && this.isActive() && !this.parent.isSubMenuHovered() && !this.tooltipIconHovered) {
                if (FancyMenu.getOptions().playUiClickSounds.getValue() && this.enableClickSound) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                this.clickAction.onClick(this.parent, this);
                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        @FunctionalInterface
        public interface ClickAction {

            void onClick(ContextMenu var1, ContextMenu.ClickableContextMenuEntry<?> var2);
        }
    }

    public abstract static class ContextMenuEntry<T extends ContextMenu.ContextMenuEntry<T>> implements Renderable, GuiEventListener {

        protected String identifier;

        protected ContextMenu parent;

        protected float x;

        protected float y;

        protected float width;

        protected float height = 20.0F;

        @Nullable
        protected ContextMenu.ContextMenuEntry.EntryTask tickAction;

        protected ContextMenu.ContextMenuEntry.EntryTask hoverAction;

        protected boolean hovered = false;

        protected ContextMenu.ContextMenuStackMeta stackMeta = new ContextMenu.ContextMenuStackMeta();

        protected List<ContextMenu.BooleanSupplier> activeStateSuppliers = new ArrayList();

        protected List<ContextMenu.BooleanSupplier> visibleStateSuppliers = new ArrayList();

        @Nullable
        protected ContextMenu.Supplier<Tooltip> tooltipSupplier;

        protected Font font;

        protected boolean addSpaceForIcon;

        protected boolean changeBackgroundColorOnHover;

        public ContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent) {
            this.font = Minecraft.getInstance().font;
            this.addSpaceForIcon = false;
            this.changeBackgroundColorOnHover = true;
            this.identifier = identifier;
            this.parent = parent;
        }

        @Override
        public abstract void render(@NotNull GuiGraphics var1, int var2, int var3, float var4);

        @NotNull
        public String getIdentifier() {
            return this.identifier;
        }

        @NotNull
        public ContextMenu getParent() {
            return this.parent;
        }

        public float getHeight() {
            return this.height;
        }

        public T setHeight(float height) {
            this.height = height;
            return (T) this;
        }

        public abstract float getMinWidth();

        protected void setHovered(boolean hovered) {
            this.hovered = hovered;
        }

        public boolean isHovered() {
            return !this.parent.isOpen() ? false : this.hovered;
        }

        public boolean isChangeBackgroundColorOnHover() {
            return this.changeBackgroundColorOnHover;
        }

        public T setChangeBackgroundColorOnHover(boolean changeColor) {
            this.changeBackgroundColorOnHover = changeColor;
            return (T) this;
        }

        public boolean isActive() {
            for (ContextMenu.BooleanSupplier b : this.activeStateSuppliers) {
                if (!b.getBoolean(this.parent, this)) {
                    return false;
                }
            }
            return true;
        }

        @Deprecated(forRemoval = true)
        public T setIsActiveSupplier(@Nullable ContextMenu.BooleanSupplier activeStateSupplier) {
            if (activeStateSupplier != null) {
                this.addIsActiveSupplier(activeStateSupplier);
            }
            return (T) this;
        }

        public T addIsActiveSupplier(@NotNull ContextMenu.BooleanSupplier activeStateSupplier) {
            this.activeStateSuppliers.add((ContextMenu.BooleanSupplier) Objects.requireNonNull(activeStateSupplier));
            return (T) this;
        }

        public boolean isVisible() {
            for (ContextMenu.BooleanSupplier b : this.visibleStateSuppliers) {
                if (!b.getBoolean(this.parent, this)) {
                    return false;
                }
            }
            return true;
        }

        @Deprecated(forRemoval = true)
        public T setIsVisibleSupplier(@Nullable ContextMenu.BooleanSupplier visibleStateSupplier) {
            if (visibleStateSupplier != null) {
                this.addIsVisibleSupplier(visibleStateSupplier);
            }
            return (T) this;
        }

        public T addIsVisibleSupplier(@NotNull ContextMenu.BooleanSupplier visibleStateSupplier) {
            this.visibleStateSuppliers.add((ContextMenu.BooleanSupplier) Objects.requireNonNull(visibleStateSupplier));
            return (T) this;
        }

        public T setTickAction(@Nullable ContextMenu.ContextMenuEntry.EntryTask tickAction) {
            this.tickAction = tickAction;
            return (T) this;
        }

        public T setHoverAction(@Nullable ContextMenu.ContextMenuEntry.EntryTask hoverAction) {
            this.hoverAction = hoverAction;
            return (T) this;
        }

        public T setTooltipSupplier(@Nullable ContextMenu.Supplier<Tooltip> tooltipSupplier) {
            this.tooltipSupplier = tooltipSupplier;
            return (T) this;
        }

        @Nullable
        public Tooltip getTooltip() {
            return this.tooltipSupplier != null ? this.tooltipSupplier.get(this.parent, this) : null;
        }

        public T setStackable(boolean stackable) {
            this.getStackMeta().setStackable(stackable);
            return (T) this;
        }

        public boolean isStackable() {
            return this.getStackMeta().isStackable();
        }

        @NotNull
        public ContextMenu.ContextMenuStackMeta getStackMeta() {
            return this.stackMeta;
        }

        protected void onRemoved() {
        }

        public abstract ContextMenu.ContextMenuEntry<?> copy();

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
        }

        @FunctionalInterface
        public interface EntryTask {

            void run(ContextMenu var1, ContextMenu.ContextMenuEntry<?> var2, boolean var3);
        }
    }

    public static class ContextMenuStackMeta {

        protected RuntimePropertyContainer properties = new RuntimePropertyContainer();

        protected boolean stackable = false;

        protected boolean partOfStack = false;

        protected boolean firstInStack = true;

        protected boolean lastInStack = true;

        protected ContextMenu.ContextMenuEntry<?> nextInStack;

        @NotNull
        public RuntimePropertyContainer getProperties() {
            return this.properties;
        }

        public boolean isPartOfStack() {
            return this.partOfStack;
        }

        public boolean isFirstInStack() {
            return this.firstInStack;
        }

        public boolean isLastInStack() {
            return this.lastInStack;
        }

        public boolean isStackable() {
            return this.stackable;
        }

        public void setStackable(boolean stackable) {
            this.stackable = stackable;
        }
    }

    public static class IconFactory {

        @NotNull
        public static ResourceLocation getIcon(@NotNull String iconName) {
            return new ResourceLocation("fancymenu", "textures/contextmenu/icons/" + iconName + ".png");
        }
    }

    public static class SeparatorContextMenuEntry extends ContextMenu.ContextMenuEntry<ContextMenu.SeparatorContextMenuEntry> {

        public SeparatorContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent) {
            super(identifier, parent);
            this.height = 9.0F;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            RenderingUtils.fillF(graphics, this.x + 10.0F, this.y + 4.0F, this.x + this.width - 10.0F, this.y + 5.0F, UIBase.getUIColorTheme().element_border_color_normal.getColorInt());
        }

        public ContextMenu.SeparatorContextMenuEntry copy() {
            ContextMenu.SeparatorContextMenuEntry copy = new ContextMenu.SeparatorContextMenuEntry(this.identifier, this.parent);
            copy.height = this.height;
            copy.tickAction = this.tickAction;
            copy.tooltipSupplier = this.tooltipSupplier;
            copy.activeStateSuppliers = new ArrayList(this.activeStateSuppliers);
            return copy;
        }

        @Override
        public float getMinWidth() {
            int i = 20;
            if (this.addSpaceForIcon) {
                i += 20;
            }
            return (float) i;
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }

    public static class SpacerContextMenuEntry extends ContextMenu.ContextMenuEntry<ContextMenu.SpacerContextMenuEntry> {

        public SpacerContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent) {
            super(identifier, parent);
            this.height = 4.0F;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        }

        @Override
        public float getMinWidth() {
            int i = 20;
            if (this.addSpaceForIcon) {
                i += 20;
            }
            return (float) i;
        }

        public ContextMenu.SpacerContextMenuEntry copy() {
            ContextMenu.SpacerContextMenuEntry e = new ContextMenu.SpacerContextMenuEntry(this.identifier, this.parent);
            e.height = this.height;
            return e;
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }

    public static class SubMenuContextMenuEntry extends ContextMenu.ClickableContextMenuEntry<ContextMenu.SubMenuContextMenuEntry> {

        @NotNull
        protected ContextMenu subContextMenu;

        protected boolean subMenuHoverTicked = false;

        protected boolean subMenuHoveredAfterOpen = false;

        protected long parentMenuHoverStartTime = -1L;

        protected long entryHoverStartTime = -1L;

        protected long entryNotHoveredStartTime = -1L;

        public SubMenuContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent, @NotNull Component label, @NotNull ContextMenu subContextMenu) {
            super(identifier, parent, label, (menu, entry) -> {
            });
            this.subContextMenu = subContextMenu;
            this.subContextMenu.parentEntry = this;
            this.subContextMenu.forceDefaultTooltipStyle = parent.forceDefaultTooltipStyle;
            this.clickAction = (menu, entry) -> this.openSubMenu();
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.tickEntry();
            super.render(graphics, mouseX, mouseY, partial);
            this.renderSubMenuArrow(graphics);
        }

        protected void renderSubMenuArrow(GuiGraphics graphics) {
            RenderSystem.enableBlend();
            UIBase.getUIColorTheme().setUITextureShaderColor(graphics, 1.0F);
            graphics.blit(ContextMenu.SUB_CONTEXT_MENU_ARROW_ICON, (int) (this.x + this.width - 20.0F), (int) (this.y + 5.0F), 0.0F, 0.0F, 10, 10, 10, 10);
            UIBase.resetShaderColor(graphics);
        }

        @Override
        protected int getTooltipIconX() {
            return super.getTooltipIconX() - 15;
        }

        @Override
        protected void renderBackground(@NotNull GuiGraphics graphics) {
            boolean hover = this.hovered;
            this.hovered = this.hovered || this.subContextMenu.isOpen();
            super.renderBackground(graphics);
            this.hovered = hover;
        }

        protected void tickEntry() {
            if (!this.subContextMenu.isOpen()) {
                this.subMenuHoveredAfterOpen = false;
                this.subMenuHoverTicked = false;
            }
            if (this.subContextMenu.isHovered()) {
                if (!this.subMenuHoverTicked) {
                    this.subMenuHoverTicked = true;
                } else {
                    this.subMenuHoveredAfterOpen = true;
                }
            }
            if (this.parent.isHovered() && !this.isHovered() && this.subContextMenu.isOpen() && !this.subContextMenu.isUserNavigatingInMenu() && this.subMenuHoveredAfterOpen) {
                if (this.parentMenuHoverStartTime == -1L) {
                    this.parentMenuHoverStartTime = System.currentTimeMillis();
                }
                if (this.parentMenuHoverStartTime + 400L < System.currentTimeMillis()) {
                    this.subContextMenu.closeMenu();
                }
            } else {
                this.parentMenuHoverStartTime = -1L;
            }
            if (this.isActive() && this.isHovered() && !this.parent.isSubMenuHovered() && !this.tooltipIconHovered) {
                long now = System.currentTimeMillis();
                if (this.entryHoverStartTime == -1L) {
                    this.entryHoverStartTime = now;
                }
                int openSpeed = 400 / Math.max(1, FancyMenu.getOptions().contextMenuHoverOpenSpeed.getValue());
                if (this.entryHoverStartTime + (long) openSpeed < now && !this.subContextMenu.isOpen()) {
                    this.parent.closeSubMenus();
                    this.openSubMenu();
                }
            } else {
                this.entryHoverStartTime = -1L;
            }
            if (!this.isHovered() && this.parent.isHovered() && !this.parent.isSubMenuHovered()) {
                long nowx = System.currentTimeMillis();
                if (this.entryNotHoveredStartTime == -1L) {
                    this.entryNotHoveredStartTime = nowx;
                }
                if (this.entryNotHoveredStartTime + 400L < nowx && this.subContextMenu.isOpen()) {
                    this.subContextMenu.closeMenu();
                }
            } else {
                this.entryNotHoveredStartTime = -1L;
            }
            if (this.tooltipActive && this.subContextMenu.isOpen()) {
                this.subContextMenu.closeMenu();
            }
        }

        public void openSubMenu(@NotNull List<String> entryPath) {
            this.subContextMenu.openMenuAt(0.0F, 0.0F, entryPath);
        }

        public void openSubMenu() {
            this.subContextMenu.openMenuAt(0.0F, 0.0F);
        }

        @NotNull
        public ContextMenu getSubContextMenu() {
            return this.subContextMenu;
        }

        public void setSubContextMenu(@NotNull ContextMenu subContextMenu) {
            this.subContextMenu.closeMenu();
            this.subContextMenu.parentEntry = null;
            this.subContextMenu = subContextMenu;
            this.subContextMenu.parentEntry = this;
            this.subContextMenu.forceDefaultTooltipStyle = this.parent.forceDefaultTooltipStyle;
        }

        @NotNull
        public ContextMenu.SubMenuOpeningSide getSubMenuOpeningSide() {
            return this.subContextMenu.subMenuOpeningSide;
        }

        public ContextMenu.SubMenuContextMenuEntry setSubMenuOpeningSide(@NotNull ContextMenu.SubMenuOpeningSide subMenuOpeningSide) {
            this.subContextMenu.subMenuOpeningSide = subMenuOpeningSide;
            return this;
        }

        public ContextMenu.SubMenuContextMenuEntry copy() {
            ContextMenu.SubMenuContextMenuEntry copy = new ContextMenu.SubMenuContextMenuEntry(this.identifier, this.parent, Component.literal(""), new ContextMenu());
            copy.height = this.height;
            copy.tickAction = this.tickAction;
            copy.tooltipSupplier = this.tooltipSupplier;
            copy.activeStateSuppliers = new ArrayList(this.activeStateSuppliers);
            copy.labelSupplier = this.labelSupplier;
            copy.icon = this.icon;
            return copy;
        }

        @Override
        protected void onRemoved() {
            this.subContextMenu.closeMenu();
            this.subContextMenu.parentEntry = null;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && this.subContextMenu.isOpen() && !this.subContextMenu.isUserNavigatingInMenu()) {
                this.subContextMenu.closeMenu();
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public float getMinWidth() {
            float i = super.getMinWidth();
            if (this.tooltipSupplier == null) {
                i += 30.0F;
            } else {
                i += 15.0F;
            }
            return i;
        }

        @NotNull
        public ContextMenu.SubMenuContextMenuEntry setClickAction(@NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
            ContextMenu.LOGGER.error("[FANCYMENU] You can't set the click action of SubMenuContextMenuEntries.");
            return this;
        }

        @NotNull
        public ContextMenu.SubMenuContextMenuEntry setShortcutTextSupplier(@Nullable ContextMenu.Supplier<Component> shortcutTextSupplier) {
            ContextMenu.LOGGER.error("[FANCYMENU] You can't set a shortcut text for SubMenuContextMenuEntries.");
            return this;
        }
    }

    public static enum SubMenuOpeningSide {

        LEFT, RIGHT
    }

    @FunctionalInterface
    public interface Supplier<T> {

        T get(ContextMenu var1, ContextMenu.ContextMenuEntry<?> var2);
    }

    public static class ValueCycleContextMenuEntry<V> extends ContextMenu.ClickableContextMenuEntry<ContextMenu.ValueCycleContextMenuEntry<V>> {

        protected final ILocalizedValueCycle<V> valueCycle;

        public ValueCycleContextMenuEntry(@NotNull String identifier, @NotNull ContextMenu parent, @NotNull ILocalizedValueCycle<V> valueCycle) {
            super(identifier, parent, Component.empty(), (menu, entry) -> valueCycle.next());
            this.valueCycle = valueCycle;
            this.labelSupplier = (menu, entry) -> this.valueCycle.getCycleComponent();
        }

        @NotNull
        public ILocalizedValueCycle<V> getValueCycle() {
            return this.valueCycle;
        }

        @NotNull
        public ContextMenu.ValueCycleContextMenuEntry<V> setLabelSupplier(@NotNull ContextMenu.Supplier<Component> labelSupplier) {
            ContextMenu.LOGGER.error("[FANCYMENU] You can't set the label of ValueCycleContextMenuEntries!");
            return this;
        }

        @NotNull
        public ContextMenu.ValueCycleContextMenuEntry<V> setClickAction(@NotNull ContextMenu.ClickableContextMenuEntry.ClickAction clickAction) {
            ContextMenu.LOGGER.error("[FANCYMENU] You can't set the click action of ValueCycleContextMenuEntries!");
            return this;
        }

        public ContextMenu.ValueCycleContextMenuEntry<V> copy() {
            ContextMenu.ValueCycleContextMenuEntry<V> copy = new ContextMenu.ValueCycleContextMenuEntry<>(this.identifier, this.parent, this.valueCycle);
            copy.shortcutTextSupplier = this.shortcutTextSupplier;
            copy.labelSupplier = this.labelSupplier;
            copy.height = this.height;
            copy.tickAction = this.tickAction;
            copy.tooltipSupplier = this.tooltipSupplier;
            copy.activeStateSuppliers = new ArrayList(this.activeStateSuppliers);
            copy.icon = this.icon;
            return copy;
        }
    }
}