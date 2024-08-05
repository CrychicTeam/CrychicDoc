package noppes.npcs.client.gui;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketTileEntityGet;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcRedstoneBlock extends GuiNPCInterface implements IGuiData {

    private TileRedstoneBlock tile;

    public GuiNpcRedstoneBlock(BlockPos pos) {
        this.tile = (TileRedstoneBlock) this.player.m_9236_().getBlockEntity(pos);
        Packets.sendServer(new SPacketTileEntityGet(pos));
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 40, this.guiTop + 20, 120, 20, "availability.options"));
        this.addLabel(new GuiLabel(11, "gui.detailed", this.guiLeft + 40, this.guiTop + 47, 16777215));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 110, this.guiTop + 42, 50, 20, new String[] { "gui.no", "gui.yes" }, this.tile.isDetailed ? 1 : 0));
        if (this.tile.isDetailed) {
            this.addLabel(new GuiLabel(0, I18n.get("bard.ondistance") + " X:", this.guiLeft + 1, this.guiTop + 76, 16777215));
            this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 80, this.guiTop + 71, 30, 20, this.tile.onRangeX + ""));
            this.getTextField(0).numbersOnly = true;
            this.getTextField(0).setMinMaxDefault(0, 50, 6);
            this.addLabel(new GuiLabel(1, "Y:", this.guiLeft + 113, this.guiTop + 76, 16777215));
            this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 122, this.guiTop + 71, 30, 20, this.tile.onRangeY + ""));
            this.getTextField(1).numbersOnly = true;
            this.getTextField(1).setMinMaxDefault(0, 50, 6);
            this.addLabel(new GuiLabel(2, "Z:", this.guiLeft + 155, this.guiTop + 76, 16777215));
            this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 164, this.guiTop + 71, 30, 20, this.tile.onRangeZ + ""));
            this.getTextField(2).numbersOnly = true;
            this.getTextField(2).setMinMaxDefault(0, 50, 6);
            this.addLabel(new GuiLabel(3, I18n.get("bard.offdistance") + " X:", this.guiLeft - 3, this.guiTop + 99, 16777215));
            this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 80, this.guiTop + 94, 30, 20, this.tile.offRangeX + ""));
            this.getTextField(3).numbersOnly = true;
            this.getTextField(3).setMinMaxDefault(0, 50, 10);
            this.addLabel(new GuiLabel(4, "Y:", this.guiLeft + 113, this.guiTop + 99, 16777215));
            this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 122, this.guiTop + 94, 30, 20, this.tile.offRangeY + ""));
            this.getTextField(4).numbersOnly = true;
            this.getTextField(4).setMinMaxDefault(0, 50, 10);
            this.addLabel(new GuiLabel(5, "Z:", this.guiLeft + 155, this.guiTop + 99, 16777215));
            this.addTextField(new GuiTextFieldNop(5, this, this.guiLeft + 164, this.guiTop + 94, 30, 20, this.tile.offRangeZ + ""));
            this.getTextField(5).numbersOnly = true;
            this.getTextField(5).setMinMaxDefault(0, 50, 10);
        } else {
            this.addLabel(new GuiLabel(0, "bard.ondistance", this.guiLeft + 1, this.guiTop + 76, 16777215));
            this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 80, this.guiTop + 71, 30, 20, this.tile.onRange + ""));
            this.getTextField(0).numbersOnly = true;
            this.getTextField(0).setMinMaxDefault(0, 50, 6);
            this.addLabel(new GuiLabel(3, "bard.offdistance", this.guiLeft - 3, this.guiTop + 99, 16777215));
            this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 80, this.guiTop + 94, 30, 20, this.tile.offRange + ""));
            this.getTextField(3).numbersOnly = true;
            this.getTextField(3).setMinMaxDefault(0, 50, 10);
        }
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 40, this.guiTop + 190, 120, 20, "Done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 1) {
            this.tile.isDetailed = guibutton.getValue() == 1;
            this.init();
        }
        if (id == 4) {
            this.save();
            this.setSubGui(new SubGuiNpcAvailability(this.tile.availability));
        }
    }

    @Override
    public void save() {
        if (this.tile != null) {
            if (this.tile.isDetailed) {
                this.tile.onRangeX = this.getTextField(0).getInteger();
                this.tile.onRangeY = this.getTextField(1).getInteger();
                this.tile.onRangeZ = this.getTextField(2).getInteger();
                this.tile.offRangeX = this.getTextField(3).getInteger();
                this.tile.offRangeY = this.getTextField(4).getInteger();
                this.tile.offRangeZ = this.getTextField(5).getInteger();
                if (this.tile.onRangeX > this.tile.offRangeX) {
                    this.tile.offRangeX = this.tile.onRangeX;
                }
                if (this.tile.onRangeY > this.tile.offRangeY) {
                    this.tile.offRangeY = this.tile.onRangeY;
                }
                if (this.tile.onRangeZ > this.tile.offRangeZ) {
                    this.tile.offRangeZ = this.tile.onRangeZ;
                }
            } else {
                this.tile.onRange = this.getTextField(0).getInteger();
                this.tile.offRange = this.getTextField(3).getInteger();
                if (this.tile.onRange > this.tile.offRange) {
                    this.tile.offRange = this.tile.onRange;
                }
            }
            CompoundTag compound = this.tile.m_187480_();
            compound.remove("BlockActivated");
            Packets.sendServer(new SPacketTileEntitySave(compound));
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.tile.load(compound);
        this.init();
    }
}