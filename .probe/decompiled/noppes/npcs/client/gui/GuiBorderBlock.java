package noppes.npcs.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketTileEntityGet;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiBorderBlock extends GuiNPCInterface implements IGuiData {

    private TileBorder tile;

    public GuiBorderBlock(BlockPos pos) {
        this.tile = (TileBorder) this.player.m_9236_().getBlockEntity(pos);
        Packets.sendServer(new SPacketTileEntityGet(pos));
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 40, this.guiTop + 40, 120, 20, "Availability Options"));
        this.addLabel(new GuiLabel(0, "Height", this.guiLeft + 1, this.guiTop + 76, 16777215));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 60, this.guiTop + 71, 40, 20, this.tile.height + ""));
        this.getTextField(0).numbersOnly = true;
        this.getTextField(0).setMinMaxDefault(0, 500, 6);
        this.addLabel(new GuiLabel(1, "Message", this.guiLeft + 1, this.guiTop + 100, 16777215));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 60, this.guiTop + 95, 200, 20, this.tile.message));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 40, this.guiTop + 190, 120, 20, "Done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 4) {
            this.save();
            this.setSubGui(new SubGuiNpcAvailability(this.tile.availability));
        }
    }

    @Override
    public void save() {
        if (this.tile != null) {
            this.tile.height = this.getTextField(0).getInteger();
            this.tile.message = this.getTextField(1).m_94155_();
            Packets.sendServer(new SPacketTileEntitySave(this.tile.m_187480_()));
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.tile.readExtraNBT(compound);
        this.init();
    }
}