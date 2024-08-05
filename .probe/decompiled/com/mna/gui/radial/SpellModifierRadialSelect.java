package com.mna.gui.radial;

import com.google.common.collect.Lists;
import com.mna.gui.radial.components.DrawingContext;
import com.mna.gui.radial.components.IRadialMenuHost;
import com.mna.gui.radial.components.RadialMenuItem;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.glfw.GLFW;

public class SpellModifierRadialSelect {

    public static final float OPEN_ANIMATION_LENGTH = 2.5F;

    public final IRadialMenuHost host;

    private final List<RadialMenuItem> items = Lists.newArrayList();

    private final List<RadialMenuItem> visibleItems = Lists.newArrayList();

    private final Minecraft minecraft;

    public int backgroundColor = 1056964608;

    public int backgroundColorHover = 1073741823;

    private SpellModifierRadialSelect.State state = SpellModifierRadialSelect.State.INITIALIZING;

    public double startAnimation;

    public float animProgress;

    public float radiusIn;

    public float radiusOut;

    public float itemRadius;

    public float animTop;

    private Component centralText;

    private static final float PRECISION = 0.0069444445F;

    private static final double TWO_PI = Math.PI * 2;

    public SpellModifierRadialSelect(Minecraft minecraft, IRadialMenuHost host) {
        this.minecraft = minecraft;
        this.host = host;
    }

    public void setCentralText(@Nullable Component centralText) {
        this.centralText = centralText;
    }

    public Component getCentralText() {
        return this.centralText;
    }

    public int getHovered() {
        for (int i = 0; i < this.visibleItems.size(); i++) {
            if (((RadialMenuItem) this.visibleItems.get(i)).isHovered()) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    public RadialMenuItem getHoveredItem() {
        for (RadialMenuItem item : this.visibleItems) {
            if (item.isHovered()) {
                return item;
            }
        }
        return null;
    }

    public void setHovered(int which) {
        for (int i = 0; i < this.visibleItems.size(); i++) {
            ((RadialMenuItem) this.visibleItems.get(i)).setHovered(i == which);
        }
    }

    public int getVisibleItemCount() {
        return this.visibleItems.size();
    }

    public void clickItem() {
        switch(this.state) {
            case NORMAL:
                RadialMenuItem item = this.getHoveredItem();
                if (item != null) {
                    item.onClick();
                    return;
                }
            default:
                this.onClickOutside();
        }
    }

    public void onClickOutside() {
    }

    public boolean isClosed() {
        return this.state == SpellModifierRadialSelect.State.CLOSED;
    }

    public boolean isReady() {
        return this.state == SpellModifierRadialSelect.State.NORMAL;
    }

    public void visibilityChanged(RadialMenuItem item) {
        this.visibleItems.clear();
        for (RadialMenuItem radialMenuItem : this.items) {
            if (radialMenuItem.isVisible()) {
                this.visibleItems.add(radialMenuItem);
            }
        }
    }

    public void add(RadialMenuItem item) {
        this.items.add(item);
        if (item.isVisible()) {
            this.visibleItems.add(item);
        }
    }

    public void addAll(Collection<? extends RadialMenuItem> cachedMenuItems) {
        this.items.addAll(cachedMenuItems);
        for (RadialMenuItem cachedMenuItem : cachedMenuItems) {
            if (cachedMenuItem.isVisible()) {
                this.visibleItems.add(cachedMenuItem);
            }
        }
    }

    public void clear() {
        this.items.clear();
        this.visibleItems.clear();
    }

    public void close() {
        this.state = SpellModifierRadialSelect.State.CLOSING;
        this.startAnimation = (double) this.minecraft.level.m_46467_() + (double) this.minecraft.getFrameTime();
        this.animProgress = 1.0F;
        this.setHovered(-1);
    }

    public void tick() {
        if (this.state == SpellModifierRadialSelect.State.INITIALIZING) {
            this.startAnimation = (double) this.minecraft.level.m_46467_() + (double) this.minecraft.getFrameTime();
            this.state = SpellModifierRadialSelect.State.OPENING;
            this.animProgress = 0.0F;
        }
    }

    public void draw(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.updateAnimationState(partialTicks);
        if (!this.isClosed()) {
            if (this.isReady()) {
                this.processMouse(mouseX, mouseY);
            }
            Screen owner = this.host.getScreen();
            Font fontRenderer = this.host.getFontRenderer();
            boolean animated = this.state == SpellModifierRadialSelect.State.OPENING || this.state == SpellModifierRadialSelect.State.CLOSING;
            this.radiusIn = animated ? Math.max(0.1F, 30.0F * this.animProgress) : 30.0F;
            this.radiusOut = this.radiusIn * 2.0F;
            this.itemRadius = (this.radiusIn + this.radiusOut) * 0.5F;
            this.animTop = animated ? (1.0F - this.animProgress) * (float) owner.height / 2.0F : 0.0F;
            int x = owner.width / 2;
            int y = owner.height / 2;
            float z = 0.0F;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0F, this.animTop, 0.0F);
            this.drawBackground((float) x, (float) y, z, this.radiusIn, this.radiusOut);
            pGuiGraphics.pose().popPose();
            if (this.isReady()) {
                this.drawItems(pGuiGraphics, x, y, z, owner.width, owner.height, fontRenderer);
                Component currentCentralText = this.centralText;
                for (int i = 0; i < this.visibleItems.size(); i++) {
                    RadialMenuItem item = (RadialMenuItem) this.visibleItems.get(i);
                    if (item.isHovered()) {
                        if (item.getCentralText() != null) {
                            currentCentralText = item.getCentralText();
                        }
                        break;
                    }
                }
                if (currentCentralText != null) {
                    String text = currentCentralText.getString();
                    pGuiGraphics.drawString(fontRenderer, text, (int) ((float) (owner.width - fontRenderer.width(text)) / 2.0F), (int) ((float) (owner.height - 9) / 2.0F), -1);
                }
                this.drawTooltips(pGuiGraphics, mouseX, mouseY);
            }
        }
    }

    private void updateAnimationState(float partialTicks) {
        float openAnimation = 0.0F;
        switch(this.state) {
            case OPENING:
                openAnimation = (float) (((double) ((float) this.minecraft.level.m_46467_() + partialTicks) - this.startAnimation) / 2.5);
                if ((double) openAnimation >= 1.0 || this.getVisibleItemCount() == 0) {
                    openAnimation = 1.0F;
                    this.state = SpellModifierRadialSelect.State.NORMAL;
                }
                break;
            case CLOSING:
                openAnimation = 1.0F - (float) (((double) ((float) this.minecraft.level.m_46467_() + partialTicks) - this.startAnimation) / 2.5);
                if (openAnimation <= 0.0F || this.getVisibleItemCount() == 0) {
                    openAnimation = 0.0F;
                    this.state = SpellModifierRadialSelect.State.CLOSED;
                }
        }
        this.animProgress = openAnimation;
    }

    private void drawTooltips(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        Screen owner = this.host.getScreen();
        Font fontRenderer = this.host.getFontRenderer();
        for (int i = 0; i < this.visibleItems.size(); i++) {
            RadialMenuItem item = (RadialMenuItem) this.visibleItems.get(i);
            if (item.isHovered()) {
                DrawingContext context = new DrawingContext(pGuiGraphics, owner.width, owner.height, (float) mouseX, (float) mouseY, 0.0F, fontRenderer, this.host);
                item.drawTooltips(context);
            }
        }
    }

    private void drawItems(GuiGraphics pGuiGraphics, int x, int y, float z, int width, int height, Font font) {
        this.iterateVisible((item, s, e) -> {
            float middle = (s + e) * 0.5F;
            float posX = (float) x + this.itemRadius * (float) Math.cos((double) middle);
            float posY = (float) y + this.itemRadius * (float) Math.sin((double) middle);
            DrawingContext context = new DrawingContext(pGuiGraphics, width, height, posX, posY, z, font, this.host);
            item.draw(context);
        });
    }

    private void iterateVisible(TriConsumer<RadialMenuItem, Float, Float> consumer) {
        int numItems = this.visibleItems.size();
        for (int i = 0; i < numItems; i++) {
            float s = (float) this.getAngleFor((double) i - 0.5, numItems);
            float e = (float) this.getAngleFor((double) i + 0.5, numItems);
            RadialMenuItem item = (RadialMenuItem) this.visibleItems.get(i);
            consumer.accept(item, s, e);
        }
    }

    private void drawBackground(float x, float y, float z, float radiusIn, float radiusOut) {
        RenderSystem.enableBlend();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        this.iterateVisible((item, s, e) -> {
            int color = item.isHovered() ? this.backgroundColorHover : this.backgroundColor;
            this.drawPieArc(buffer, x, y, z, radiusIn, radiusOut, s, e, color);
        });
        tessellator.end();
        RenderSystem.disableBlend();
    }

    private void drawPieArc(BufferBuilder buffer, float x, float y, float z, float radiusIn, float radiusOut, float startAngle, float endAngle, int color) {
        float angle = endAngle - startAngle;
        int sections = Math.max(1, Mth.ceil(angle / 0.0069444445F));
        angle = endAngle - startAngle;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color >> 0 & 0xFF;
        int a = color >> 24 & 0xFF;
        for (int i = 0; i < sections; i++) {
            float angle1 = startAngle + (float) i / (float) sections * angle;
            float angle2 = startAngle + (float) (i + 1) / (float) sections * angle;
            float pos1InX = x + radiusIn * (float) Math.cos((double) angle1);
            float pos1InY = y + radiusIn * (float) Math.sin((double) angle1);
            float pos1OutX = x + radiusOut * (float) Math.cos((double) angle1);
            float pos1OutY = y + radiusOut * (float) Math.sin((double) angle1);
            float pos2OutX = x + radiusOut * (float) Math.cos((double) angle2);
            float pos2OutY = y + radiusOut * (float) Math.sin((double) angle2);
            float pos2InX = x + radiusIn * (float) Math.cos((double) angle2);
            float pos2InY = y + radiusIn * (float) Math.sin((double) angle2);
            buffer.m_5483_((double) pos1OutX, (double) pos1OutY, (double) z).color(r, g, b, a).endVertex();
            buffer.m_5483_((double) pos1InX, (double) pos1InY, (double) z).color(r, g, b, a).endVertex();
            buffer.m_5483_((double) pos2InX, (double) pos2InY, (double) z).color(r, g, b, a).endVertex();
            buffer.m_5483_((double) pos2OutX, (double) pos2OutY, (double) z).color(r, g, b, a).endVertex();
        }
    }

    public void cyclePrevious() {
        int numItems = this.getVisibleItemCount();
        int which = this.getHovered();
        if (--which < 0) {
            which = numItems - 1;
        }
        this.setHovered(which);
        this.moveMouseToItem(which, numItems);
    }

    public void cycleNext() {
        int numItems = this.getVisibleItemCount();
        int which = this.getHovered();
        if (which < 0) {
            which = 0;
        } else if (++which >= numItems) {
            which = 0;
        }
        this.moveMouseToItem(which, numItems);
        this.setHovered(which);
    }

    private void moveMouseToItem(int which, int numItems) {
        Screen owner = this.host.getScreen();
        int x = owner.width / 2;
        int y = owner.height / 2;
        float angle = (float) this.getAngleFor((double) which, numItems);
        this.setMousePosition((double) x + (double) this.itemRadius * Math.cos((double) angle), (double) y + (double) this.itemRadius * Math.sin((double) angle));
    }

    private void setMousePosition(double x, double y) {
        Screen owner = this.host.getScreen();
        Window mainWindow = this.minecraft.getWindow();
        GLFW.glfwSetCursorPos(mainWindow.getWindow(), (double) ((int) (x * (double) mainWindow.getScreenWidth() / (double) owner.width)), (double) ((int) (y * (double) mainWindow.getScreenHeight() / (double) owner.height)));
    }

    private void processMouse(int mouseX, int mouseY) {
        if (this.isReady()) {
            int numItems = this.getVisibleItemCount();
            Screen owner = this.host.getScreen();
            int x = owner.width / 2;
            int y = owner.height / 2;
            double a = Math.atan2((double) (mouseY - y), (double) (mouseX - x));
            double d = Math.sqrt(Math.pow((double) (mouseX - x), 2.0) + Math.pow((double) (mouseY - y), 2.0));
            if (numItems > 0) {
                double s0 = this.getAngleFor(-0.5, numItems);
                double s1 = this.getAngleFor((double) numItems - 0.5, numItems);
                while (a < s0) {
                    a += Math.PI * 2;
                }
                while (a >= s1) {
                    a -= Math.PI * 2;
                }
            }
            int hovered = -1;
            for (int i = 0; i < numItems; i++) {
                float s = (float) this.getAngleFor((double) i - 0.5, numItems);
                float e = (float) this.getAngleFor((double) i + 0.5, numItems);
                if (a >= (double) s && a < (double) e && d >= (double) this.radiusIn && d < (double) this.radiusOut) {
                    hovered = i;
                    break;
                }
            }
            this.setHovered(hovered);
            Window mainWindow = this.minecraft.getWindow();
            int windowWidth = mainWindow.getScreenWidth();
            int windowHeight = mainWindow.getScreenHeight();
            double[] xPos = new double[1];
            double[] yPos = new double[1];
            GLFW.glfwGetCursorPos(mainWindow.getWindow(), xPos, yPos);
            double scaledX = xPos[0] - (double) ((float) windowWidth / 2.0F);
            double scaledY = yPos[0] - (double) ((float) windowHeight / 2.0F);
            double distance = Math.sqrt(scaledX * scaledX + scaledY * scaledY);
            double radius = (double) (this.radiusOut * ((float) windowWidth / (float) owner.width)) * 0.975;
            if (distance > radius) {
                double fixedX = scaledX * radius / distance;
                double fixedY = scaledY * radius / distance;
                GLFW.glfwSetCursorPos(mainWindow.getWindow(), (double) ((int) ((double) (windowWidth / 2) + fixedX)), (double) ((int) ((double) (windowHeight / 2) + fixedY)));
            }
        }
    }

    private double getAngleFor(double i, int numItems) {
        return numItems == 0 ? 0.0 : (i / (double) numItems + 0.25) * (Math.PI * 2) + Math.PI;
    }

    public static enum State {

        INITIALIZING, OPENING, NORMAL, CLOSING, CLOSED
    }
}