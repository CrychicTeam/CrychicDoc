package noppes.npcs.shared.client.gui.components;

import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import noppes.npcs.shared.client.gui.listeners.IGui;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiBasicContainer<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IGuiInterface {

    public boolean drawDefaultBackground = true;

    public int guiLeft;

    public int guiTop;

    public LocalPlayer player;

    public GuiWrapper wrapper = new GuiWrapper(this);

    public String title;

    public boolean closeOnEsc = true;

    public int mouseX;

    public int mouseY;

    public GuiBasicContainer(T cont, Inventory inv, Component titleIn) {
        super(cont, inv, titleIn);
        this.player = Minecraft.getInstance().player;
        this.title = "";
        this.f_96541_ = Minecraft.getInstance();
        this.f_96547_ = this.f_96541_.font;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.closeOnEsc;
    }

    @Override
    public void init() {
        super.init();
        this.setFocused(null);
        this.guiLeft = (this.f_96543_ - this.f_97726_) / 2;
        this.guiTop = (this.f_96544_ - this.f_97727_) / 2;
        this.f_169369_.clear();
        this.m_6702_().clear();
        this.wrapper.init(this.f_96541_, this.f_96543_, this.f_96544_);
    }

    public ResourceLocation getResource(String texture) {
        return new ResourceLocation("customnpcs", "textures/gui/" + texture);
    }

    @Override
    public void containerTick() {
        this.wrapper.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        return this.wrapper.mouseScrolled(mouseX, mouseY, scrolled) ? true : super.m_6050_(mouseX, mouseY, scrolled);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        return this.wrapper.mouseClicked(i, j, k) ? true : super.mouseClicked(i, j, k);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        if (this.wrapper.mouseDragged(x, y, button, dx, dy)) {
            return true;
        } else if (this.getFocused() != null && this.m_7282_() && button == 0) {
            this.getFocused().mouseDragged(x, y, button, dx, dy);
            return true;
        } else {
            return super.mouseDragged(x, y, button, dx, dy);
        }
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return this.wrapper.mouseReleased(x, y, button) ? true : super.mouseReleased(x, y, button);
    }

    @Override
    public void elementClicked() {
        if (this.wrapper.subgui != null) {
            ((IGuiInterface) this.wrapper.subgui).elementClicked();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
    }

    @Override
    public GuiWrapper getWrapper() {
        return this.wrapper;
    }

    @Override
    public void initGui() {
        this.init();
    }

    public boolean isInventoryKey(int i) {
        return this.f_96541_.options.keyInventory.getKey().getValue() == i;
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.wrapper.charTyped(c, i) ? true : super.m_5534_(c, i);
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        return this.wrapper.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_) ? true : super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
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

    public void buttonEvent(Button guibutton) {
    }

    public void close() {
        this.save();
        this.player.closeContainer();
        this.setScreen(null);
        this.f_96541_.mouseHandler.grabMouse();
    }

    @Override
    public void onClose() {
        this.close();
        GuiTextFieldNop.unfocus();
    }

    public void addButton(GuiButtonNop button) {
        this.wrapper.npcbuttons.put(button.id, button);
        super.m_142416_(button);
    }

    public void addTopButton(GuiMenuTopButton button) {
        this.wrapper.topbuttons.put(button.id, button);
        super.m_142416_(button);
    }

    public void addSideButton(GuiMenuSideButton button) {
        this.wrapper.sidebuttons.put(button.id, button);
        super.m_142416_(button);
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
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
    }

    @Override
    public void save() {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.wrapper.mouseX = mouseX;
        this.wrapper.mouseY = mouseY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        ArrayList<Slot> slots = new ArrayList(this.f_97732_.slots);
        if (this.wrapper.subgui != null) {
            this.f_97732_.slots.clear();
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawCenteredString(this.getFontRenderer(), I18n.get(this.title), this.f_96543_ / 2, this.guiTop - 8, 16777215);
        for (GuiLabel label : new ArrayList(this.wrapper.labels.values())) {
            label.render(graphics, mouseX, mouseY, partialTicks);
        }
        for (GuiTextFieldNop tf : new ArrayList(this.wrapper.textfields.values())) {
            tf.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
        for (GuiCustomScrollNop scroll : new ArrayList(this.wrapper.scrolls.values())) {
            scroll.render(graphics, mouseX, mouseY, partialTicks);
        }
        for (IGui comp : new ArrayList(this.wrapper.components)) {
            comp.render(graphics, mouseX, mouseY);
            for (Screen gui : new ArrayList(this.wrapper.extra.values())) {
                gui.render(graphics, mouseX, mouseY, partialTicks);
            }
        }
        if (this.wrapper.subgui != null) {
            this.f_97732_.slots.addAll(slots);
            this.wrapper.subgui.render(graphics, mouseX, mouseY, partialTicks);
        } else {
            this.m_280072_(graphics, mouseX, mouseY);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        if (this.drawDefaultBackground && this.wrapper.subgui == null) {
            super.m_280273_(graphics);
        }
    }

    public Font getFontRenderer() {
        return this.f_96547_;
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

    @Override
    public int getWidth() {
        return this.f_96543_;
    }

    @Override
    public int getHeight() {
        return this.f_96544_;
    }

    @Override
    public Screen getParent() {
        return this.wrapper.getParent();
    }
}