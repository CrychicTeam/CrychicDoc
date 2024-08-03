package noppes.npcs.client.gui.advanced;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiNPCMarks extends GuiNPCInterface2 {

    private final String[] marks = new String[] { "gui.none", "mark.question", "mark.exclamation", "mark.pointer", "mark.skull", "mark.cross", "mark.star" };

    private MarkData data;

    private MarkData.Mark selectedMark;

    public GuiNPCMarks(EntityNPCInterface npc) {
        super(npc);
        this.data = MarkData.get(npc);
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 14;
        for (int i = 0; i < this.data.marks.size(); i++) {
            MarkData.Mark mark = (MarkData.Mark) this.data.marks.get(i);
            this.addButton(new GuiButtonBiDirectional(this, 1 + i * 10, this.guiLeft + 6, y, 120, 20, this.marks, mark.type));
            String color = Integer.toHexString(mark.color);
            while (color.length() < 6) {
                color = "0" + color;
            }
            this.addButton(new GuiButtonNop(this, 2 + i * 10, this.guiLeft + 128, y, 60, 20, color));
            this.getButton(2 + i * 10).setFGColor(mark.color);
            this.addButton(new GuiButtonNop(this, 3 + i * 10, this.guiLeft + 190, y, 120, 20, "availability.options"));
            this.addButton(new GuiButtonNop(this, 4 + i * 10, this.guiLeft + 312, y, 40, 20, "X"));
            y += 22;
        }
        if (this.data.marks.size() < 9) {
            this.addButton(new GuiButtonNop(this, 101, this.guiLeft + 6, y + 2, 60, 20, "gui.add"));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id < 90) {
            this.selectedMark = (MarkData.Mark) this.data.marks.get(button.id / 10);
            if (button.id % 10 == 1) {
                this.selectedMark.type = button.getValue();
            }
            if (button.id % 10 == 2) {
                this.setSubGui(new SubGuiColorSelector(this.selectedMark.color));
            }
            if (button.id % 10 == 3) {
                this.setSubGui(new SubGuiNpcAvailability(this.selectedMark.availability));
            }
            if (button.id % 10 == 4) {
                this.data.marks.remove(this.selectedMark);
                this.init();
            }
        }
        if (button.id == 101) {
            this.data.addMark(0);
            this.init();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        if (subgui instanceof SubGuiColorSelector) {
            this.selectedMark.color = ((SubGuiColorSelector) subgui).color;
            this.init();
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.MARK, this.data.getNBT()));
    }
}