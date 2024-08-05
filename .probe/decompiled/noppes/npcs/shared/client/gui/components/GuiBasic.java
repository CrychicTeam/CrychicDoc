package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.net.URI;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.shared.client.gui.listeners.IGui;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiBasic extends Screen implements IGuiInterface {

    public LocalPlayer player;

    public boolean drawDefaultBackground = true;

    public String title;

    public ResourceLocation background = null;

    public boolean closeOnEsc = true;

    public int guiLeft;

    public int guiTop;

    public int imageWidth;

    public int imageHeight;

    public float bgScale = 1.0F;

    public GuiWrapper wrapper = new GuiWrapper(this);

    public GuiBasic() {
        super(Component.empty());
        this.player = Minecraft.getInstance().player;
        this.f_96541_ = Minecraft.getInstance();
        this.title = "";
        this.imageWidth = 200;
        this.imageHeight = 222;
        this.f_96541_ = Minecraft.getInstance();
        this.f_96547_ = this.f_96541_.font;
    }

    public void setBackground(String texture) {
        this.background = new ResourceLocation("customnpcs", "textures/gui/" + texture);
    }

    public ResourceLocation getResource(String texture) {
        return new ResourceLocation("customnpcs", "textures/gui/" + texture);
    }

    @Override
    public void init() {
        super.init();
        this.setFocused(null);
        this.guiLeft = (this.f_96543_ - this.imageWidth) / 2;
        this.guiTop = (this.f_96544_ - this.imageHeight) / 2;
        this.f_169369_.clear();
        this.m_6702_().clear();
        this.wrapper.init(this.f_96541_, this.f_96543_, this.f_96544_);
    }

    @Override
    public GuiWrapper getWrapper() {
        return this.wrapper;
    }

    @Override
    public void initGui() {
        this.init();
    }

    @Override
    public void tick() {
        this.wrapper.tick();
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        return this.wrapper.mouseScrolled(mouseX, mouseY, scrolled) ? true : super.m_6050_(mouseX, mouseY, scrolled);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        return this.wrapper.mouseClicked(i, j, k) ? true : super.m_6375_(i, j, k);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        return this.wrapper.mouseDragged(x, y, button, dx, dy) ? true : super.m_7979_(x, y, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return this.wrapper.mouseReleased(x, y, button) ? true : super.m_6348_(x, y, button);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener gui) {
        if (this.wrapper.subgui != null) {
            this.wrapper.subgui.m_7522_(gui);
        } else {
            if (gui != null && !this.m_6702_().contains(gui)) {
                return;
            }
            this.wrapper.changeFocus(this.getFocused(), gui);
            super.m_7522_(gui);
        }
    }

    @Override
    public GuiEventListener getFocused() {
        return this.wrapper.subgui != null ? this.wrapper.subgui.m_7222_() : super.m_7222_();
    }

    @Override
    public void elementClicked() {
        if (this.wrapper.subgui != null && this.wrapper.subgui instanceof GuiBasic) {
            ((GuiBasic) this.wrapper.subgui).elementClicked();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.wrapper.charTyped(c, i) ? true : super.m_5534_(c, i);
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        return this.wrapper.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_) ? true : super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
    }

    public boolean isInventoryKey(int i) {
        return this.f_96541_.options.keyInventory.getKey().getValue() == i;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.closeOnEsc;
    }

    public void close() {
        this.onClose();
    }

    @Override
    public void onClose() {
        this.wrapper.close();
    }

    public void addButton(GuiButtonNop button) {
        this.wrapper.npcbuttons.put(button.id, button);
        super.addRenderableWidget(button);
    }

    public void addTopButton(GuiMenuTopButton button) {
        this.wrapper.topbuttons.put(button.id, button);
        super.addRenderableWidget(button);
    }

    public void addSideButton(GuiMenuSideButton button) {
        this.wrapper.sidebuttons.put(button.id, button);
        super.addRenderableWidget(button);
    }

    public GuiButtonNop getButton(int i) {
        return (GuiButtonNop) this.wrapper.npcbuttons.get(i);
    }

    public GuiMenuSideButton getSideButton(int i) {
        return (GuiMenuSideButton) this.wrapper.sidebuttons.get(i);
    }

    public GuiMenuTopButton getTopButton(int i) {
        return (GuiMenuTopButton) this.wrapper.topbuttons.get(i);
    }

    public void addTextField(GuiTextFieldNop tf) {
        this.wrapper.textfields.put(tf.id, tf);
    }

    public GuiTextFieldNop getTextField(int i) {
        return (GuiTextFieldNop) this.wrapper.textfields.get(i);
    }

    public void add(IGui gui) {
        this.wrapper.components.add(gui);
    }

    public IGui get(int id) {
        for (IGui comp : this.wrapper.components) {
            if (comp.getID() == id) {
                return comp;
            }
        }
        return null;
    }

    public void addLabel(GuiLabel label) {
        this.wrapper.labels.put(label.id, label);
    }

    public GuiLabel getLabel(int i) {
        return (GuiLabel) this.wrapper.labels.get(i);
    }

    public void addSlider(GuiSliderNop slider) {
        this.wrapper.sliders.put(slider.id, slider);
        this.m_142416_(slider);
    }

    public GuiSliderNop getSlider(int i) {
        return (GuiSliderNop) this.wrapper.sliders.get(i);
    }

    public void addScroll(GuiCustomScrollNop scroll) {
        scroll.m_6575_(this.f_96541_, scroll.f_96543_, scroll.f_96544_);
        this.wrapper.scrolls.put(scroll.id, scroll);
    }

    public GuiCustomScrollNop getScroll(int id) {
        return (GuiCustomScrollNop) this.wrapper.scrolls.get(id);
    }

    @Override
    public void save() {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        this.wrapper.mouseX = mouseX;
        this.wrapper.mouseY = mouseY;
        int x = mouseX;
        int y = mouseY;
        if (this.wrapper.subgui != null) {
            y = 0;
            x = 0;
        }
        if (this.drawDefaultBackground && this.wrapper.subgui == null) {
            this.m_280273_(graphics);
        }
        if (this.background != null) {
            matrixStack.pushPose();
            matrixStack.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
            matrixStack.scale(this.bgScale, this.bgScale, this.bgScale);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.background);
            if (this.imageWidth > 256) {
                graphics.blit(this.background, 0, 0, 0, 0, 250, this.imageHeight);
                graphics.blit(this.background, 250, 0, 256 - (this.imageWidth - 250), 0, this.imageWidth - 250, this.imageHeight);
            } else {
                graphics.blit(this.background, 0, 0, 0, 0, this.imageWidth, this.imageHeight);
            }
            matrixStack.popPose();
        }
        graphics.drawCenteredString(this.f_96547_, this.title, this.f_96543_ / 2, 8, 16777215);
        for (GuiLabel label : new ArrayList(this.wrapper.labels.values())) {
            label.render(graphics, mouseX, mouseY, partialTicks);
        }
        for (GuiTextFieldNop tf : new ArrayList(this.wrapper.textfields.values())) {
            tf.renderWidget(graphics, x, y, partialTicks);
        }
        for (GuiCustomScrollNop scroll : new ArrayList(this.wrapper.scrolls.values())) {
            scroll.render(graphics, x, y, partialTicks);
        }
        for (IGui comp : new ArrayList(this.wrapper.components)) {
            comp.render(graphics, x, y);
        }
        for (Screen gui : new ArrayList(this.wrapper.extra.values())) {
            gui.render(graphics, x, y, partialTicks);
        }
        super.render(graphics, x, y, partialTicks);
        if (this.wrapper.subgui != null) {
            matrixStack.translate(0.0F, 0.0F, 60.0F);
            this.wrapper.subgui.render(graphics, mouseX, mouseY, partialTicks);
            matrixStack.translate(0.0F, 0.0F, -60.0F);
        }
    }

    public Font getFontRenderer() {
        return this.f_96547_;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void doubleClicked() {
    }

    public void setScreen(Screen gui) {
        this.f_96541_.setScreen(gui);
    }

    public void setSubGui(Screen gui) {
        this.wrapper.setSubgui(gui);
        this.init();
    }

    @Override
    public boolean hasSubGui() {
        return this.wrapper.subgui != null;
    }

    @Override
    public Screen getSubGui() {
        return this.wrapper.getSubGui();
    }

    public void drawNpc(GuiGraphics graphics, LivingEntity entity, int x, int y, float zoomed, int rotation) {
        this.wrapper.drawNpc(graphics, entity, x, y, zoomed, rotation, this.guiLeft, this.guiTop);
    }

    @Override
    public int getWidth() {
        return this.f_96543_;
    }

    @Override
    public int getHeight() {
        return this.f_96544_;
    }

    public void openLink(String link) {
        try {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke(null);
            oclass.getMethod("browse", URI.class).invoke(object, new URI(link));
        } catch (Throwable var4) {
        }
    }

    @Override
    public Screen getParent() {
        return this.wrapper.getParent();
    }
}