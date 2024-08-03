package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketDimensionTeleport;
import noppes.npcs.packets.server.SPacketDimensionsGet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IScrollData;

public class GuiNpcDimension extends GuiNPCInterface implements IScrollData {

    private GuiCustomScrollNop scroll;

    private Map<String, Integer> data = new HashMap();

    public GuiNpcDimension() {
        this.imageWidth = 256;
        this.setBackground("menubg.png");
        Packets.sendServer(new SPacketDimensionsGet());
    }

    @Override
    public void init() {
        super.m_7856_();
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(165, 208);
        }
        this.scroll.guiLeft = this.guiLeft + 4;
        this.scroll.guiTop = this.guiTop + 4;
        this.addScroll(this.scroll);
        String title = I18n.get("gui.dimensions");
        int x = (this.imageWidth - this.f_96547_.width(title)) / 2;
        this.addLabel(new GuiLabel(0, title, this.guiLeft + x, this.guiTop - 8));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 170, this.guiTop + 72, 82, 20, "remote.tp"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (this.data.containsKey(this.scroll.getSelected())) {
            if (id == 4) {
                Packets.sendServer(new SPacketDimensionTeleport(new ResourceLocation(this.scroll.getSelected())));
                this.close();
            }
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        this.scroll.mouseClicked(i, j, k);
        return super.m_6375_(i, j, k);
    }

    @Override
    public void save() {
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        this.scroll.setList(list);
        this.data = data;
    }

    @Override
    public void setSelected(String selected) {
        this.getButton(3).setDisplayText(selected);
    }
}