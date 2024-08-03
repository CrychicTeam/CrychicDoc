package noppes.npcs.client.gui.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.api.gui.IComponentsWrapper;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.wrapper.gui.CustomGuiAssetsSelectorWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonListWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiEntityDisplayWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiSliderWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextAreaWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiAssetsSelector;
import noppes.npcs.client.gui.custom.components.CustomGuiButton;
import noppes.npcs.client.gui.custom.components.CustomGuiButtonList;
import noppes.npcs.client.gui.custom.components.CustomGuiEntityDisplay;
import noppes.npcs.client.gui.custom.components.CustomGuiLabel;
import noppes.npcs.client.gui.custom.components.CustomGuiScroll;
import noppes.npcs.client.gui.custom.components.CustomGuiSlider;
import noppes.npcs.client.gui.custom.components.CustomGuiTextArea;
import noppes.npcs.client.gui.custom.components.CustomGuiTextField;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;

public class GuiCustomComponents {

    public static final ResourceLocation resource = new ResourceLocation("customnpcs:textures/gui/components.png");

    public Map<Integer, IGuiComponent> components = new HashMap();

    protected List<IItemSlot> slots = new ArrayList();

    protected int draggingId = -1;

    public void setComponents(GuiCustom gui, IComponentsWrapper comps) {
        Map<Integer, IGuiComponent> components = new HashMap();
        for (ICustomGuiComponent comp : comps.getComponents()) {
            switch(comp.getType()) {
                case 0:
                    CustomGuiButton button = new CustomGuiButton(gui, (CustomGuiButtonWrapper) comp);
                    components.put(button.getID(), button);
                    break;
                case 1:
                    CustomGuiLabel lbl = new CustomGuiLabel(gui, (CustomGuiLabelWrapper) comp);
                    components.put(lbl.getID(), lbl);
                    break;
                case 2:
                    CustomGuiTexturedRect rect = new CustomGuiTexturedRect(gui, (CustomGuiTexturedRectWrapper) comp);
                    components.put(rect.getID(), rect);
                    break;
                case 3:
                    CustomGuiTextField textField = new CustomGuiTextField(gui, (CustomGuiTextFieldWrapper) comp);
                    components.put(textField.id, textField);
                    break;
                case 4:
                    CustomGuiScroll scroll = new CustomGuiScroll(gui, (CustomGuiScrollWrapper) comp);
                    components.put(scroll.getID(), scroll);
                case 5:
                default:
                    break;
                case 6:
                    CustomGuiTextArea textArea = new CustomGuiTextArea(gui, (CustomGuiTextAreaWrapper) comp);
                    components.put(textArea.id, textArea);
                    break;
                case 7:
                    components.put(comp.getID(), new CustomGuiButtonList(gui, (CustomGuiButtonListWrapper) comp));
                    break;
                case 8:
                    CustomGuiSlider slider = new CustomGuiSlider(gui, (CustomGuiSliderWrapper) comp);
                    components.put(slider.getID(), slider);
                    break;
                case 9:
                    CustomGuiEntityDisplay display = new CustomGuiEntityDisplay(gui, (CustomGuiEntityDisplayWrapper) comp);
                    components.put(display.getID(), display);
                    break;
                case 10:
                    CustomGuiAssetsSelector assets = new CustomGuiAssetsSelector(gui, (CustomGuiAssetsSelectorWrapper) comp);
                    components.put(assets.getID(), assets);
            }
        }
        this.components = components;
        List<IItemSlot> slots = new ArrayList();
        slots.addAll(comps.getSlots());
        slots.addAll(comps.getPlayerSlots());
        this.slots = slots;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (IGuiComponent component : this.components.values()) {
            component.onRender(graphics, mouseX, mouseY, partialTicks);
        }
        for (IItemSlot slot : this.slots) {
            if (slot.getGuiType() > 0) {
                this.renderSlot(graphics, slot);
            }
        }
        for (IGuiComponent component : this.components.values()) {
            component.onRenderPost(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public void renderSlot(GuiGraphics graphics, IItemSlot slot) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resource);
        graphics.blit(resource, slot.getPosX() - 1, slot.getPosY() - 1, 0, 80.0F, (float) ((slot.getGuiType() - 1) * 18), 18, 18, 256, 256);
    }

    public void containerTick() {
        for (IGuiComponent component : this.components.values()) {
            if (component instanceof EditBox editBox) {
                editBox.tick();
            }
            if (component instanceof CustomGuiSlider slider) {
                slider.tick();
            }
        }
    }

    public boolean charTyped(char typedChar, int keyCode) {
        for (IGuiComponent comp : this.components.values()) {
            if (comp instanceof GuiEventListener guiEvent && guiEvent.charTyped(typedChar, keyCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        for (IGuiComponent comp : this.components.values()) {
            if (comp instanceof GuiEventListener guiEvent && guiEvent.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
                return true;
            }
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (IGuiComponent comp : this.components.values()) {
            if (comp instanceof GuiEventListener guiEvent && guiEvent.mouseClicked(mouseX, mouseY, mouseButton)) {
                if (mouseButton == 0) {
                    this.draggingId = comp.getID();
                }
                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        for (IGuiComponent comp : this.components.values()) {
            if (comp instanceof GuiEventListener guiEvent && comp.getID() == this.draggingId && guiEvent.mouseDragged(x, y, button, dx, dy)) {
                return true;
            }
        }
        return false;
    }

    public boolean mouseReleased(double x, double y, int button) {
        for (IGuiComponent comp : this.components.values()) {
            if (comp.getID() == this.draggingId && comp instanceof GuiEventListener guiEvent && guiEvent.mouseReleased(x, y, button)) {
                this.draggingId = -1;
                return true;
            }
        }
        this.draggingId = -1;
        return false;
    }
}