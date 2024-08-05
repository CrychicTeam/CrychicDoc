package noppes.npcs.client.gui.custom;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiSubGuiClosed;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiCustom extends AbstractContainerScreen<ContainerCustomGui> implements IGuiData {

    protected CustomGuiTexturedRect background;

    public CustomGuiWrapper guiWrapper;

    public List<Component> hoverText;

    protected GuiCustomComponents components = new GuiCustomComponents();

    protected GuiCustomScrollingPanel scrollingPanel = new GuiCustomScrollingPanel();

    public GuiCustom subgui = null;

    public GuiCustom parent = null;

    public Inventory inv;

    public GuiCustom.InitCallback initCallback;

    public GuiCustom(ContainerCustomGui container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.inv = inv;
    }

    @Override
    public void init() {
        super.init();
        if (this.guiWrapper != null) {
            this.scrollingPanel.setComponents(this, this.guiWrapper.getScrollingPanel());
            this.components.setComponents(this, this.guiWrapper);
        }
        if (this.initCallback != null) {
            this.initCallback.init();
        }
        if (this.subgui != null) {
            this.subgui.init();
        }
    }

    @Override
    public void containerTick() {
        if (this.subgui != null) {
            this.subgui.containerTick();
        } else {
            this.components.containerTick();
            this.scrollingPanel.containerTick();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.hoverText = null;
        PoseStack matrixStack = graphics.pose();
        this.m_280273_(graphics);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((float) this.getGuiLeft(), (float) this.getGuiTop(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        matrixStack.pushPose();
        if (this.background != null) {
            this.background.onRender(graphics, mouseX, mouseY, partialTicks);
        }
        this.components.render(graphics, mouseX - this.getGuiLeft(), mouseY - this.getGuiTop(), partialTicks);
        this.scrollingPanel.render(graphics, mouseX - this.getGuiLeft(), mouseY - this.getGuiTop(), partialTicks);
        if (this.hoverText != null && !this.hoverText.isEmpty() && this.subgui == null) {
            graphics.renderTooltip(this.f_96547_, this.hoverText, Optional.empty(), mouseX - this.getGuiLeft(), mouseY - this.getGuiTop());
        }
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.subgui == null) {
            this.m_280072_(graphics, mouseX, mouseY);
        }
        matrixStack.popPose();
        if (this.subgui != null) {
            matrixStack.pushPose();
            posestack.pushPose();
            posestack.translate(0.0F, 0.0F, 40.0F);
            RenderSystem.applyModelViewMatrix();
            matrixStack.translate(0.0F, 0.0F, 40.0F);
            this.subgui.render(graphics, mouseX, mouseY, partialTicks);
            matrixStack.popPose();
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (this.subgui != null) {
            return this.subgui.charTyped(typedChar, keyCode);
        } else if (this.components.charTyped(typedChar, keyCode)) {
            return true;
        } else {
            return this.scrollingPanel.charTyped(typedChar, keyCode) ? true : super.m_5534_(typedChar, keyCode);
        }
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (this.subgui != null) {
            return this.subgui.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
        } else if (this.components.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
            return true;
        } else if (this.scrollingPanel.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
            return true;
        } else {
            return this.f_96541_.options.keyInventory.isActiveAndMatches(InputConstants.getKey(key, p_keyPressed_2_)) ? true : super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.subgui != null) {
            return this.subgui.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (this.components.mouseClicked(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton)) {
            return true;
        } else {
            return this.scrollingPanel.mouseClicked(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton) ? true : super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double mouseScrolled) {
        if (this.subgui != null) {
            return this.subgui.mouseScrolled(mouseX, mouseY, mouseScrolled);
        } else {
            return super.m_6050_(mouseX, mouseY, mouseScrolled) ? true : this.scrollingPanel.mouseScrolled(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseScrolled);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dx, double dy) {
        if (this.subgui != null) {
            return this.subgui.mouseDragged(mouseX, mouseY, mouseButton, dx, dy);
        } else if (this.components.mouseDragged(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton, dx, dy)) {
            return true;
        } else {
            return this.scrollingPanel.mouseDragged(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton, dx, dy) ? true : super.mouseDragged(mouseX, mouseY, mouseButton, dx, dy);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.subgui != null) {
            return this.subgui.mouseReleased(mouseX, mouseY, mouseButton);
        } else if (this.components.mouseReleased(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton)) {
            return true;
        } else {
            return this.scrollingPanel.mouseReleased(mouseX - (double) this.getGuiLeft(), mouseY - (double) this.getGuiTop(), mouseButton) ? true : super.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return this.guiWrapper != null ? this.guiWrapper.getDoesPauseGame() : true;
    }

    @Override
    public void onClose() {
        if (this.subgui == null) {
            if (this.parent == null) {
                super.onClose();
            } else {
                Packets.sendServer(new SPacketCustomGuiSubGuiClosed());
                this.parent.subgui = null;
            }
        } else {
            this.subgui.onClose();
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.setGuiWrapper((CustomGuiWrapper) new CustomGuiWrapper((IPlayer) NpcAPI.Instance().getIEntity(Minecraft.getInstance().player)).fromNBT(compound));
        this.init();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.m_6574_(minecraft, width, height);
        if (this.subgui != null) {
            this.subgui.resize(minecraft, width, height);
        }
    }

    public void setGuiWrapper(CustomGuiWrapper guiWrapper) {
        this.guiWrapper = guiWrapper;
        this.f_97726_ = guiWrapper.getWidth();
        this.f_97727_ = guiWrapper.getHeight();
        this.background = new CustomGuiTexturedRect(this, (CustomGuiTexturedRectWrapper) guiWrapper.getBackgroundRect());
        if (guiWrapper.hasSubGui()) {
            if (this.subgui == null) {
                this.subgui = new GuiCustom((ContainerCustomGui) this.f_97732_, Minecraft.getInstance().player.m_150109_(), Component.empty());
                this.subgui.m_6575_(this.f_96541_, this.f_96543_, this.f_96544_);
            }
            this.subgui.parent = this;
            this.subgui.setGuiWrapper(guiWrapper.getSubGui());
        } else {
            ((ContainerCustomGui) this.f_97732_).setGui(guiWrapper, Minecraft.getInstance().player);
            this.subgui = null;
            if (this.parent == null) {
                this.init();
            }
        }
    }

    public IGuiComponent getComponent(UUID id) {
        Optional<IGuiComponent> c = this.components.components.values().stream().filter(t -> t.component() != null && t.component().getUniqueID().equals(id)).findFirst();
        if (c.isPresent()) {
            return (IGuiComponent) c.get();
        } else {
            c = this.scrollingPanel.components.values().stream().filter(t -> t.component() != null && t.component().getUniqueID().equals(id)).findFirst();
            if (c.isPresent()) {
                return (IGuiComponent) c.get();
            } else {
                return this.subgui != null ? this.subgui.getComponent(id) : null;
            }
        }
    }

    public int getTotalGuiLeft() {
        return this.parent != null ? this.parent.getTotalGuiLeft() + this.getGuiLeft() : this.getGuiLeft();
    }

    public int getTotalGuiTop() {
        return this.parent != null ? this.parent.getTotalGuiTop() + this.getGuiTop() : this.getGuiTop();
    }

    public void add(IGuiComponent component) {
        this.components.components.put(component.getID(), component);
    }

    public void addPanel(IGuiComponent component) {
        this.scrollingPanel.components.put(component.getID(), component);
    }

    public interface InitCallback {

        void init();
    }
}