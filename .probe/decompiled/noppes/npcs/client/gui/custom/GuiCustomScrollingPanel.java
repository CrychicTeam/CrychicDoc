package noppes.npcs.client.gui.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.mixin.MouseHelperMixin;
import noppes.npcs.util.ValueUtil;

public class GuiCustomScrollingPanel extends GuiCustomComponents {

    public GuiComponentsScrollableWrapper comps;

    private int maxSize = 0;

    private int scrollMaxHeight = 0;

    private int scrollPercentage = 0;

    private GuiCustom gui;

    private boolean isScrolling = false;

    private final CustomGuiTexturedRect scrollbar = new CustomGuiTexturedRect(null, new CustomGuiTexturedRectWrapper(-1, resource.toString(), 0, 0, 14, 64, 65, 0).setRepeatingTexture(14, 64, 1));

    private final CustomGuiTexturedRect button = new CustomGuiTexturedRect(null, new CustomGuiTexturedRectWrapper(-1, resource.toString(), 0, 0, 12, 15, 0, 214));

    public void setComponents(GuiCustom gui, GuiComponentsScrollableWrapper comps) {
        super.setComponents(gui, comps);
        this.gui = gui;
        this.comps = comps;
        this.button.x = comps.width - 13;
        this.scrollbar.x = comps.width - 14;
        this.scrollbar.height = comps.height;
        this.scrollMaxHeight = comps.height - 17;
        this.maxSize = comps.getComponents().stream().mapToInt(v -> v.getPosY() + v.getHeight()).max().orElse(0);
        if (!this.canScroll()) {
            this.scrollPercentage = 0;
            comps.scrollAmount = 0;
        } else {
            this.setScrollAmount(this.scrollPercentage * (this.maxSize - comps.height) / 100);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        mouseX -= this.comps.x;
        mouseY -= this.comps.y;
        matrixStack.pushPose();
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((float) this.comps.x, (float) this.comps.y, 10.0F);
        RenderSystem.applyModelViewMatrix();
        if (this.canScroll()) {
            this.scrollbar.onRender(graphics, mouseX, mouseY, partialTicks);
            if (this.isScrolling) {
                if (((MouseHelperMixin) Minecraft.getInstance().mouseHandler).getActiveButton() == 0) {
                    this.scrollPercentage = ValueUtil.CorrectInt((mouseY - 7) * 100 / this.scrollMaxHeight, 0, 100);
                } else {
                    this.isScrolling = false;
                }
            }
            this.button.textureX = 0;
            if (this.scrollButtonHovered((double) mouseX, (double) mouseY) || this.isScrolling) {
                this.button.textureX = 24;
            }
            this.button.y = 1 + this.scrollPercentage * this.scrollMaxHeight / 100;
            this.button.onRender(graphics, mouseX, mouseY, partialTicks);
            this.setScrollAmount(this.scrollPercentage * (this.maxSize - this.comps.height) / 100);
            matrixStack.translate(0.0F, (float) (-this.comps.scrollAmount), 0.0F);
            for (ICustomGuiComponent component : this.comps.getComponents()) {
                if (this.comps.isVisible(component)) {
                    ((IGuiComponent) this.components.get(component.getID())).onRender(graphics, mouseX, mouseY + this.comps.scrollAmount, partialTicks);
                }
            }
            for (IItemSlot slot : this.slots) {
                if (this.comps.isVisible(slot) && slot.getGuiType() > 0) {
                    this.renderSlot(graphics, slot);
                }
            }
            for (ICustomGuiComponent componentx : this.comps.getComponents()) {
                if (this.comps.isVisible(componentx)) {
                    ((IGuiComponent) this.components.get(componentx.getID())).onRenderPost(graphics, mouseX, mouseY + this.comps.scrollAmount, partialTicks);
                }
            }
        } else {
            super.render(graphics, mouseX, mouseY, partialTicks);
        }
        matrixStack.popPose();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private void setScrollAmount(int amount) {
        if (amount != this.comps.scrollAmount) {
            this.comps.scrollAmount = amount;
            ((ContainerCustomGui) this.gui.m_6262_()).update();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        mouseX -= (double) this.comps.x;
        mouseY -= (double) this.comps.y;
        if (!this.canScroll()) {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (this.scrollBarHovered(mouseX, mouseY) && mouseButton == 0) {
            this.isScrolling = true;
            this.scrollPercentage = ValueUtil.CorrectInt((int) (mouseY - 7.0) * 100 / this.scrollMaxHeight, 0, 100);
            return true;
        } else {
            boolean clicked = false;
            for (ICustomGuiComponent component : this.comps.getComponents()) {
                if (this.comps.isVisible(component)) {
                    IGuiComponent comp = (IGuiComponent) this.components.get(component.getID());
                    if (comp instanceof GuiEventListener) {
                        GuiEventListener guiEvent = (GuiEventListener) comp;
                        if (guiEvent.mouseClicked(mouseX, mouseY + (double) this.comps.scrollAmount, mouseButton)) {
                            if (mouseButton == 0) {
                                this.draggingId = comp.getID();
                            }
                            clicked = true;
                        }
                    }
                }
            }
            return clicked;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dx, double dy) {
        if (!this.isScrolling && this.draggingId >= 0) {
            mouseX -= (double) this.comps.x;
            mouseY -= (double) this.comps.y;
            if (!this.canScroll()) {
                return super.mouseDragged(mouseX, mouseY, mouseButton, dx, dy);
            } else {
                for (ICustomGuiComponent component : this.comps.getComponents()) {
                    if (this.comps.isVisible(component)) {
                        IGuiComponent comp = (IGuiComponent) this.components.get(component.getID());
                        if (comp instanceof GuiEventListener guiEvent && component.getID() == this.draggingId && guiEvent.mouseDragged(mouseX, mouseY + (double) this.comps.scrollAmount, mouseButton, dx, dy)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        mouseX -= (double) this.comps.x;
        mouseY -= (double) this.comps.y;
        if (!this.canScroll()) {
            return super.mouseReleased(mouseX, mouseY, mouseButton);
        } else {
            for (ICustomGuiComponent component : this.comps.getComponents()) {
                if (this.comps.isVisible(component)) {
                    IGuiComponent comp = (IGuiComponent) this.components.get(component.getID());
                    if (comp instanceof GuiEventListener guiEvent && component.getID() == this.draggingId && guiEvent.mouseReleased(mouseX, mouseY + (double) this.comps.scrollAmount, mouseButton)) {
                        this.draggingId = -1;
                        return true;
                    }
                }
            }
            this.draggingId = -1;
            return false;
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double mouseScrolled) {
        if (mouseScrolled != 0.0 && this.panelHovered(mouseX - (double) this.comps.x, mouseY - (double) this.comps.y)) {
            this.scrollPercentage += mouseScrolled > 0.0 ? -4 : 4;
            this.scrollPercentage = ValueUtil.CorrectInt(this.scrollPercentage, 0, 100);
            return true;
        } else {
            return false;
        }
    }

    public boolean canScroll() {
        return this.maxSize > this.comps.height;
    }

    public boolean panelHovered(double x, double y) {
        return this.canScroll() && x >= 0.0 && y >= 0.0 && x < (double) this.comps.width && y < (double) this.comps.height;
    }

    private boolean scrollBarHovered(double x, double y) {
        return this.panelHovered(x, y) && x >= (double) this.scrollbar.x && y >= (double) this.scrollbar.y && x < (double) (this.scrollbar.x + this.scrollbar.width) && y < (double) (this.scrollbar.y + this.scrollbar.height);
    }

    private boolean scrollButtonHovered(double x, double y) {
        return this.scrollBarHovered(x, y) && y > (double) this.button.y && y < (double) (this.button.y + 15);
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }
}