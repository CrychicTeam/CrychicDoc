package noppes.npcs.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketSchematicsStore;
import noppes.npcs.packets.server.SPacketTileEntityGet;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiBlockCopy extends GuiNPCInterface implements IGuiData, ITextfieldListener {

    private BlockPos pos;

    private TileCopy tile;

    public GuiBlockCopy(BlockPos pos) {
        this.pos = pos;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
        this.tile = (TileCopy) this.player.m_9236_().getBlockEntity(pos);
        Packets.sendServer(new SPacketTileEntityGet(pos));
    }

    @Override
    public void init() {
        super.m_7856_();
        int y = this.guiTop + 4;
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 104, y, 50, 20, this.tile.height + ""));
        this.addLabel(new GuiLabel(0, "schematic.height", this.guiLeft + 5, y + 5));
        this.getTextField(0).numbersOnly = true;
        this.getTextField(0).setMinMaxDefault(0, 100, 10);
        int var10005 = this.guiLeft + 104;
        y += 23;
        this.addTextField(new GuiTextFieldNop(1, this, var10005, y, 50, 20, this.tile.width + ""));
        this.addLabel(new GuiLabel(1, "schematic.width", this.guiLeft + 5, y + 5));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(0, 100, 10);
        var10005 = this.guiLeft + 104;
        y += 23;
        this.addTextField(new GuiTextFieldNop(2, this, var10005, y, 50, 20, this.tile.length + ""));
        this.addLabel(new GuiLabel(2, "schematic.length", this.guiLeft + 5, y + 5));
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(0, 100, 10);
        var10005 = this.guiLeft + 104;
        y += 23;
        this.addTextField(new GuiTextFieldNop(5, this, var10005, y, 100, 20, ""));
        this.addLabel(new GuiLabel(5, "gui.name", this.guiLeft + 5, y + 5));
        var10005 = this.guiLeft + 5;
        y += 30;
        this.addButton(new GuiButtonNop(this, 0, var10005, y, 60, 20, "gui.save"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 67, y, 60, 20, "gui.cancel"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            Packets.sendServer(new SPacketSchematicsStore(this.getTextField(5).m_94155_(), this.tile.m_187480_()));
            this.close();
        }
        if (guibutton.id == 1) {
            this.close();
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketTileEntitySave(this.tile.m_187480_()));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.tile.load(compound);
        this.init();
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            this.tile.height = (short) textfield.getInteger();
        }
        if (textfield.id == 1) {
            this.tile.width = (short) textfield.getInteger();
        }
        if (textfield.id == 2) {
            this.tile.length = (short) textfield.getInteger();
        }
    }
}