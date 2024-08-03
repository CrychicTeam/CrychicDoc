package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

public abstract class AbstractWidget implements Renderable, GuiEventListener, LayoutElement, NarratableEntry {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");

    private static final double PERIOD_PER_SCROLLED_PIXEL = 0.5;

    private static final double MIN_SCROLL_PERIOD = 3.0;

    protected int width;

    protected int height;

    private int x;

    private int y;

    private Component message;

    protected boolean isHovered;

    public boolean active = true;

    public boolean visible = true;

    protected float alpha = 1.0F;

    private int tabOrderGroup;

    private boolean focused;

    @Nullable
    private Tooltip tooltip;

    private int tooltipMsDelay;

    private long hoverOrFocusedStartTime;

    private boolean wasHoveredOrFocused;

    public AbstractWidget(int int0, int int1, int int2, int int3, Component component4) {
        this.x = int0;
        this.y = int1;
        this.width = int2;
        this.height = int3;
        this.message = component4;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.visible) {
            this.isHovered = int1 >= this.getX() && int2 >= this.getY() && int1 < this.getX() + this.width && int2 < this.getY() + this.height;
            this.renderWidget(guiGraphics0, int1, int2, float3);
            this.updateTooltip();
        }
    }

    private void updateTooltip() {
        if (this.tooltip != null) {
            boolean $$0 = this.isHovered || this.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard();
            if ($$0 != this.wasHoveredOrFocused) {
                if ($$0) {
                    this.hoverOrFocusedStartTime = Util.getMillis();
                }
                this.wasHoveredOrFocused = $$0;
            }
            if ($$0 && Util.getMillis() - this.hoverOrFocusedStartTime > (long) this.tooltipMsDelay) {
                Screen $$1 = Minecraft.getInstance().screen;
                if ($$1 != null) {
                    $$1.setTooltipForNextRenderPass(this.tooltip, this.createTooltipPositioner(), this.isFocused());
                }
            }
        }
    }

    protected ClientTooltipPositioner createTooltipPositioner() {
        return (ClientTooltipPositioner) (!this.isHovered && this.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard() ? new BelowOrAboveWidgetTooltipPositioner(this) : new MenuTooltipPositioner(this));
    }

    public void setTooltip(@Nullable Tooltip tooltip0) {
        this.tooltip = tooltip0;
    }

    @Nullable
    public Tooltip getTooltip() {
        return this.tooltip;
    }

    public void setTooltipDelay(int int0) {
        this.tooltipMsDelay = int0;
    }

    protected MutableComponent createNarrationMessage() {
        return wrapDefaultNarrationMessage(this.getMessage());
    }

    public static MutableComponent wrapDefaultNarrationMessage(Component component0) {
        return Component.translatable("gui.narrate.button", component0);
    }

    protected abstract void renderWidget(GuiGraphics var1, int var2, int var3, float var4);

    protected static void renderScrollingString(GuiGraphics guiGraphics0, Font font1, Component component2, int int3, int int4, int int5, int int6, int int7) {
        int $$8 = font1.width(component2);
        int $$9 = (int4 + int6 - 9) / 2 + 1;
        int $$10 = int5 - int3;
        if ($$8 > $$10) {
            int $$11 = $$8 - $$10;
            double $$12 = (double) Util.getMillis() / 1000.0;
            double $$13 = Math.max((double) $$11 * 0.5, 3.0);
            double $$14 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * $$12 / $$13)) / 2.0 + 0.5;
            double $$15 = Mth.lerp($$14, 0.0, (double) $$11);
            guiGraphics0.enableScissor(int3, int4, int5, int6);
            guiGraphics0.drawString(font1, component2, int3 - (int) $$15, $$9, int7);
            guiGraphics0.disableScissor();
        } else {
            guiGraphics0.drawCenteredString(font1, component2, (int3 + int5) / 2, $$9, int7);
        }
    }

    protected void renderScrollingString(GuiGraphics guiGraphics0, Font font1, int int2, int int3) {
        int $$4 = this.getX() + int2;
        int $$5 = this.getX() + this.getWidth() - int2;
        renderScrollingString(guiGraphics0, font1, this.getMessage(), $$4, this.getY(), $$5, this.getY() + this.getHeight(), int3);
    }

    public void renderTexture(GuiGraphics guiGraphics0, ResourceLocation resourceLocation1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9, int int10) {
        int $$11 = int5;
        if (!this.isActive()) {
            $$11 = int5 + int6 * 2;
        } else if (this.isHoveredOrFocused()) {
            $$11 = int5 + int6;
        }
        RenderSystem.enableDepthTest();
        guiGraphics0.blit(resourceLocation1, int2, int3, (float) int4, (float) $$11, int7, int8, int9, int10);
    }

    public void onClick(double double0, double double1) {
    }

    public void onRelease(double double0, double double1) {
    }

    protected void onDrag(double double0, double double1, double double2, double double3) {
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(int2)) {
                boolean $$3 = this.clicked(double0, double1);
                if ($$3) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(double0, double1);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (this.isValidClickButton(int2)) {
            this.onRelease(double0, double1);
            return true;
        } else {
            return false;
        }
    }

    protected boolean isValidClickButton(int int0) {
        return int0 == 0;
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        if (this.isValidClickButton(int2)) {
            this.onDrag(double0, double1, double3, double4);
            return true;
        } else {
            return false;
        }
    }

    protected boolean clicked(double double0, double double1) {
        return this.active && this.visible && double0 >= (double) this.getX() && double1 >= (double) this.getY() && double0 < (double) (this.getX() + this.width) && double1 < (double) (this.getY() + this.height);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        if (!this.active || !this.visible) {
            return null;
        } else {
            return !this.isFocused() ? ComponentPath.leaf(this) : null;
        }
    }

    @Override
    public boolean isMouseOver(double double0, double double1) {
        return this.active && this.visible && double0 >= (double) this.getX() && double1 >= (double) this.getY() && double0 < (double) (this.getX() + this.width) && double1 < (double) (this.getY() + this.height);
    }

    public void playDownSound(SoundManager soundManager0) {
        soundManager0.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int int0) {
        this.width = int0;
    }

    public void setAlpha(float float0) {
        this.alpha = float0;
    }

    public void setMessage(Component component0) {
        this.message = component0;
    }

    public Component getMessage() {
        return this.message;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public boolean isHovered() {
        return this.isHovered;
    }

    public boolean isHoveredOrFocused() {
        return this.isHovered() || this.isFocused();
    }

    @Override
    public boolean isActive() {
        return this.visible && this.active;
    }

    @Override
    public void setFocused(boolean boolean0) {
        this.focused = boolean0;
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarratableEntry.NarrationPriority.FOCUSED;
        } else {
            return this.isHovered ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
        }
    }

    @Override
    public final void updateNarration(NarrationElementOutput narrationElementOutput0) {
        this.updateWidgetNarration(narrationElementOutput0);
        if (this.tooltip != null) {
            this.tooltip.updateNarration(narrationElementOutput0);
        }
    }

    protected abstract void updateWidgetNarration(NarrationElementOutput var1);

    protected void defaultButtonNarrationText(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
            } else {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
            }
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int int0) {
        this.x = int0;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int int0) {
        this.y = int0;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumerAbstractWidget0) {
        consumerAbstractWidget0.accept(this);
    }

    @Override
    public ScreenRectangle getRectangle() {
        return LayoutElement.super.getRectangle();
    }

    @Override
    public int getTabOrderGroup() {
        return this.tabOrderGroup;
    }

    public void setTabOrderGroup(int int0) {
        this.tabOrderGroup = int0;
    }
}