package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiScrollClick;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class CustomGuiScroll extends GuiCustomScrollNop implements IGuiComponent {

    private GuiCustom parent;

    public CustomGuiScrollWrapper component;

    public CustomGuiScroll(GuiCustom parent, final CustomGuiScrollWrapper component) {
        super(parent, component.getID(), component.isMultiSelect());
        this.component = component;
        this.listener = new ICustomScrollListener() {

            @Override
            public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
                Packets.sendServer(new SPacketCustomGuiScrollClick(component.uniqueId, scroll.getSelectedIndex(), false));
            }

            @Override
            public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
                Packets.sendServer(new SPacketCustomGuiScrollClick(component.uniqueId, scroll.getSelectedIndex(), true));
            }
        };
        this.f_96541_ = Minecraft.getInstance();
        this.f_96547_ = this.f_96541_.font;
        this.parent = parent;
        this.init();
    }

    @Override
    public void init() {
        this.guiLeft = this.component.getPosX();
        this.guiTop = this.component.getPosY();
        this.hasSearch = this.component.getHasSearch();
        this.setSize(this.component.getWidth(), this.component.getHeight());
        this.setUnsortedList(Arrays.asList(this.component.getList()));
        if (this.component.getDefaultSelection() >= 0) {
            int defaultSelect = this.component.getDefaultSelection();
            if (defaultSelect < this.getList().size()) {
                this.setSelected((String) this.list.get(defaultSelect));
            }
        }
        this.visible = this.component.getVisible();
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        boolean hovered = mouseX >= this.guiLeft && mouseY >= this.guiTop && mouseX < this.guiLeft + this.getWidth() && mouseY < this.guiTop + this.getHeight();
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (hovered && this.component.hasHoverText()) {
            this.parent.hoverText = this.component.getHoverTextList();
        }
        matrixStack.popPose();
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }
}