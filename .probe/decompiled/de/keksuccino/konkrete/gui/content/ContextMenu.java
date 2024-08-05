package de.keksuccino.konkrete.gui.content;

import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.rendering.RenderUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class ContextMenu implements IMenu {

    protected int width;

    protected int buttonHeight;

    protected int x = 0;

    protected int y = 0;

    protected List<AdvancedButton> content = new ArrayList();

    protected List<ContextMenu> children = new ArrayList();

    protected ContextMenu parent;

    protected AdvancedButton parentButton;

    protected boolean opened = false;

    protected boolean hovered = false;

    protected boolean autoclose = false;

    protected boolean autoalignment = true;

    protected int space;

    protected boolean alwaysOnTop = false;

    protected boolean up = false;

    protected boolean left = false;

    protected int lastHeight = 0;

    public float menuScale = 1.0F;

    protected boolean autocloseChilds = true;

    public ContextMenu(int width, int buttonHeight, int space) {
        this.width = width;
        this.buttonHeight = buttonHeight;
        this.space = space;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, int screenWidth, int screenHeight) {
        PoseStack matrix = graphics.pose();
        this.updateHovered(mouseX, mouseY);
        float ticks = Minecraft.getInstance().getFrameTime();
        int stackedHeight = 0;
        if (this.opened) {
            if (this.alwaysOnTop) {
                RenderUtils.setZLevelPre(matrix, 400);
            }
            for (AdvancedButton b : this.content) {
                b.setHandleClick(true);
                b.setWidth(this.getScaledWidth());
                b.setHeight(this.getScaledButtonHeight());
                b.labelScale = this.menuScale;
                if (this.parent != null) {
                    this.buttonHeight = this.parent.buttonHeight;
                    if (this.parent.left) {
                        this.left = true;
                        this.x = this.parent.x - this.parent.getScaledWidth() - this.getScaledWidth() - 2;
                    } else {
                        this.x = this.parent.x + this.parent.getScaledWidth() + 2;
                    }
                    if (this.autoalignment && this.x + this.getScaledWidth() > screenWidth) {
                        this.x = this.parent.x - this.getScaledWidth() - 2;
                        this.left = true;
                    }
                    b.setX(this.x);
                    if (!this.autoalignment) {
                        if (this.up) {
                            b.setY(this.y + stackedHeight - this.lastHeight + this.getScaledButtonHeight() + this.space);
                        } else {
                            b.setY(this.y + stackedHeight);
                        }
                    } else {
                        b.setY(this.y + stackedHeight);
                    }
                } else {
                    if (this.left) {
                        b.setX(this.x - this.getScaledWidth());
                    } else {
                        b.setX(this.x);
                    }
                    if (!this.autoalignment) {
                        if (this.up) {
                            b.setY(this.y + stackedHeight - this.lastHeight);
                        } else {
                            b.setY(this.y + stackedHeight);
                        }
                    } else {
                        b.setY(this.y + stackedHeight);
                    }
                }
                b.m_88315_(graphics, mouseX, mouseY, ticks);
                stackedHeight += this.getScaledButtonHeight() + this.space;
            }
            for (ContextMenu m : this.children) {
                m.render(graphics, mouseX, mouseY, screenWidth, screenHeight);
            }
            if (this.alwaysOnTop) {
                RenderUtils.setZLevelPost(matrix);
            }
            if (this.autoclose && !this.isHoveredOrFocused() && !this.isChildHovered() && !this.isParentButtonHovered() && (MouseInput.isLeftMouseDown() || MouseInput.isRightMouseDown()) && (this.parent == null || this.parentButton != null)) {
                this.opened = false;
            }
        }
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY) {
        Screen c = Minecraft.getInstance().screen;
        if (c != null) {
            this.render(graphics, mouseX, mouseY, c.width, c.height);
        }
    }

    public int getScaledWidth() {
        return (int) ((float) this.width * this.menuScale);
    }

    private int getScaledButtonHeight() {
        return (int) ((float) this.buttonHeight * this.menuScale);
    }

    private boolean isChildHovered() {
        for (ContextMenu m : this.children) {
            if (m.isOpen() && (m.isHoveredOrFocused() || m.isChildHovered())) {
                return true;
            }
        }
        return false;
    }

    private boolean isParentButtonHovered() {
        return this.parentButton != null ? this.parentButton.m_198029_() : false;
    }

    public void setParentButton(AdvancedButton parent) {
        this.parentButton = parent;
    }

    public AdvancedButton getParentButton() {
        return this.parentButton;
    }

    private void updateHovered(int mouseX, int mouseY) {
        for (AdvancedButton b : this.content) {
            if (mouseX >= b.getX() && mouseX <= b.getX() + b.getWidth() && mouseY >= b.getY() && mouseY <= b.getY() + b.m_93694_()) {
                this.hovered = true;
                return;
            }
        }
        this.hovered = false;
    }

    public boolean isLeftClicked() {
        for (AdvancedButton b : this.content) {
            if (b.m_198029_() && MouseInput.isLeftMouseDown()) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean isHoveredOrFocused() {
        return this.isHovered();
    }

    public boolean isHovered() {
        return !this.isOpen() ? false : this.hovered;
    }

    @Override
    public boolean isOpen() {
        return this.opened;
    }

    public void setAutoclose(boolean b) {
        this.autoclose = b;
    }

    @Override
    public void setUseable(boolean b) {
        for (AdvancedButton bt : this.content) {
            bt.setUseable(b);
        }
        if (!b) {
            this.opened = false;
        }
    }

    @Override
    public boolean isUseable() {
        return this.content != null && !this.content.isEmpty() ? ((AdvancedButton) this.content.get(0)).isUseable() : false;
    }

    public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
        if (this.parent != null) {
            this.autoalignment = this.parent.autoalignment;
            this.autocloseChilds = this.parent.autocloseChilds;
        }
        for (ContextMenu m : this.children) {
            m.closeMenu();
            if (this.autocloseChilds) {
                m.autoclose = true;
            }
            m.menuScale = this.menuScale;
        }
        this.x = x;
        this.y = y;
        this.lastHeight = (this.getScaledButtonHeight() + this.space) * this.content.size();
        if (this.autoalignment) {
            if (this.y + this.lastHeight > screenHeight) {
                int i = this.y + this.lastHeight - screenHeight;
                this.y -= i;
            }
            if (this.parent != null && this.parent.parent != null) {
                this.left = this.parent.left;
            } else if (this.x + this.getScaledWidth() > screenWidth) {
                this.left = true;
            } else {
                this.left = false;
            }
        } else if (this.parent != null) {
            this.left = this.parent.left;
            this.up = this.parent.up;
        }
        this.opened = true;
    }

    public void openMenuAt(int x, int y) {
        Screen c = Minecraft.getInstance().screen;
        if (c != null) {
            this.openMenuAt(x, y, c.width, c.height);
        }
    }

    @Override
    public void closeMenu() {
        this.opened = false;
        for (ContextMenu m : this.children) {
            m.closeMenu();
        }
    }

    public void closeChilds() {
        for (ContextMenu m : this.children) {
            m.closeMenu();
        }
    }

    public void addContent(AdvancedButton button) {
        button.ignoreBlockedInput = true;
        this.content.add(button);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public int getLastHeight() {
        return this.lastHeight;
    }

    public boolean isRenderedLeft() {
        return this.left;
    }

    public boolean isRenderedUp() {
        return this.up;
    }

    public void addChild(ContextMenu menu) {
        if (!this.children.contains(menu)) {
            this.children.add(menu);
            menu.parent = this;
        }
    }

    public void removeChild(ContextMenu menu) {
        if (this.children.contains(menu)) {
            this.children.remove(menu);
            menu.parent = null;
        }
    }

    public void setAutoAlignment(boolean autoalign) {
        this.autoalignment = autoalign;
    }

    public void setAlignment(boolean up, boolean left) {
        this.up = up;
        this.left = left;
    }

    public void setAlwaysOnTop(boolean b) {
        this.alwaysOnTop = b;
    }

    public void setButtonHeight(int height) {
        this.buttonHeight = height;
    }

    public void autoCloseChilds(boolean autoclose) {
        this.autocloseChilds = autoclose;
    }
}