package noppes.npcs.shared.client.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.client.gui.custom.components.CustomGuiEntityDisplay;
import noppes.npcs.shared.client.gui.listeners.IGui;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiWrapper {

    public Map<Integer, GuiButtonNop> npcbuttons = new ConcurrentHashMap();

    public Map<Integer, GuiMenuTopButton> topbuttons = new ConcurrentHashMap();

    public Map<Integer, GuiMenuSideButton> sidebuttons = new ConcurrentHashMap();

    public Map<Integer, GuiTextFieldNop> textfields = new ConcurrentHashMap();

    public Map<Integer, GuiLabel> labels = new ConcurrentHashMap();

    public Map<Integer, GuiCustomScrollNop> scrolls = new ConcurrentHashMap();

    public Map<Integer, GuiSliderNop> sliders = new ConcurrentHashMap();

    public Map<Integer, Screen> extra = new ConcurrentHashMap();

    public List<IGui> components = new ArrayList();

    public Screen parent;

    public Screen gui;

    public Screen subgui;

    public int mouseX;

    public int mouseY;

    public GuiWrapper(Screen gui) {
        this.gui = gui;
    }

    public void init(Minecraft mc, int width, int height) {
        GuiTextFieldNop.unfocus();
        if (this.subgui != null) {
            this.subgui.init(mc, width, height);
        }
        this.npcbuttons = new ConcurrentHashMap();
        this.topbuttons = new ConcurrentHashMap();
        this.sidebuttons = new ConcurrentHashMap();
        this.textfields = new ConcurrentHashMap();
        this.labels = new ConcurrentHashMap();
        this.scrolls = new ConcurrentHashMap();
        this.sliders = new ConcurrentHashMap();
        this.extra = new ConcurrentHashMap();
        this.components = new ArrayList();
    }

    public void tick() {
        if (this.subgui != null) {
            this.subgui.tick();
        } else {
            for (GuiTextFieldNop tf : new ArrayList(this.textfields.values())) {
                if (tf.enabled) {
                    tf.m_94120_();
                }
            }
            for (IGui comp : new ArrayList(this.components)) {
                comp.tick();
            }
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        if (this.subgui != null) {
            this.subgui.m_6050_(mouseX, mouseY, scrolled);
            return true;
        } else {
            for (IGui comp : new ArrayList(this.components)) {
                if (comp instanceof GuiEventListener && comp.isActive() && ((GuiEventListener) comp).mouseScrolled(mouseX, mouseY, scrolled)) {
                    return true;
                }
            }
            for (GuiCustomScrollNop scroll : this.scrolls.values()) {
                if (scroll.visible && scroll.mouseScrolled(mouseX, mouseY, scrolled)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean mouseClicked(double i, double j, int k) {
        if (this.subgui != null) {
            this.subgui.m_6375_(i, j, k);
            return true;
        } else {
            for (GuiTextFieldNop tf : new ArrayList(this.textfields.values())) {
                if (tf.mouseClicked(i, j, k)) {
                    return true;
                }
            }
            for (IGui comp : new ArrayList(this.components)) {
                if (comp instanceof GuiEventListener && ((GuiEventListener) comp).mouseClicked(i, j, k)) {
                    return true;
                }
            }
            if (k == 0) {
                for (GuiCustomScrollNop scroll : new ArrayList(this.scrolls.values())) {
                    if (scroll.mouseClicked(i, j, k)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        if (this.subgui != null) {
            this.subgui.m_7979_(x, y, button, dx, dy);
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseReleased(double x, double y, int button) {
        if (this.subgui != null) {
            this.subgui.m_6348_(x, y, button);
            return true;
        } else {
            return false;
        }
    }

    public boolean charTyped(char c, int i) {
        if (this.subgui != null) {
            this.subgui.m_5534_(c, i);
            return true;
        } else {
            for (GuiTextFieldNop tf : new ArrayList(this.textfields.values())) {
                tf.charTyped(c, i);
            }
            for (GuiCustomScrollNop scroll : new ArrayList(this.scrolls.values())) {
                scroll.charTyped(c, i);
            }
            for (IGui comp : new ArrayList(this.components)) {
                if (comp instanceof GuiEventListener) {
                    ((GuiEventListener) comp).charTyped(c, i);
                }
            }
            return true;
        }
    }

    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (this.subgui != null) {
            this.subgui.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
            return true;
        } else {
            boolean active = GuiTextFieldNop.isAnyActive();
            for (IGui gui : this.components) {
                if (gui.isActive()) {
                    active = true;
                    break;
                }
            }
            if (!this.gui.shouldCloseOnEsc() || key != 256 && (active || Minecraft.getInstance().options.keyInventory.getKey().getValue() != key)) {
                for (GuiTextFieldNop tf : this.textfields.values()) {
                    tf.m_7933_(key, p_keyPressed_2_, p_keyPressed_3_);
                }
                for (GuiCustomScrollNop scroll : new ArrayList(this.scrolls.values())) {
                    scroll.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
                }
                for (IGui comp : new ArrayList(this.components)) {
                    if (comp instanceof GuiEventListener && ((GuiEventListener) comp).keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
                        return true;
                    }
                }
                return active;
            } else {
                this.gui.onClose();
                return true;
            }
        }
    }

    public void drawNpc(GuiGraphics graphics, LivingEntity entity, int x, int y, float zoomed, int rotation, int guiLeft, int guiTop) {
        CustomGuiEntityDisplay.drawEntity(graphics, entity, x, y, zoomed, rotation, this.mouseX, this.mouseY, (float) guiLeft, (float) guiTop);
    }

    public void changeFocus(GuiEventListener old, GuiEventListener gui) {
        if (old instanceof GuiSliderNop && gui != old) {
            ((GuiSliderNop) old).onRelease(0.0, 0.0);
        }
    }

    public void setSubgui(Screen subgui) {
        this.gui.m_7522_(null);
        this.subgui = subgui;
        subgui.init(Minecraft.getInstance(), this.gui.width, this.gui.height);
        if (subgui instanceof IGuiInterface) {
            ((IGuiInterface) subgui).getWrapper().parent = this.gui;
        }
    }

    public Screen getSubGui() {
        return this.subgui instanceof IGuiInterface && ((IGuiInterface) this.subgui).hasSubGui() ? ((IGuiInterface) this.subgui).getSubGui() : this.subgui;
    }

    public Screen getParent() {
        return this.parent != null ? ((IGuiInterface) this.parent).getParent() : this.gui;
    }

    public void close() {
        GuiTextFieldNop.unfocus();
        ((IGuiInterface) this.gui).save();
        if (this.parent != null) {
            if (this.parent instanceof IGuiInterface) {
                this.parent.m_7522_(null);
                ((IGuiInterface) this.parent).getWrapper().subgui = null;
                ((IGuiInterface) this.parent).subGuiClosed(this.gui);
                ((IGuiInterface) this.parent).initGui();
            } else {
                this.gui.onClose();
            }
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(this.gui);
            minecraft.mouseHandler.grabMouse();
        }
    }
}