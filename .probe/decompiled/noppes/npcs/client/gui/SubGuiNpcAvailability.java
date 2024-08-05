package noppes.npcs.client.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketFactionGet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcAvailability extends GuiNPCInterface implements ITextfieldListener, GuiSelectionListener, IGuiData {

    private Availability availabitily;

    private int slot = 0;

    public SubGuiNpcAvailability(Availability availabitily) {
        this.availabitily = availabitily;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addLabel(new GuiLabel(1, "availability.available", this.guiLeft, this.guiTop + 4, this.imageWidth, 0));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 34, this.guiTop + 12, 180, 20, "availability.selectdialog"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 34, this.guiTop + 35, 180, 20, "availability.selectquest"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 34, this.guiTop + 58, 180, 20, "availability.selectscoreboard"));
        this.addButton(new GuiButtonNop(this, 20, this.guiLeft + 4, this.guiTop + 104, 50, 20, new String[] { "availability.always", "availability.is", "availability.isnot" }, this.availabitily.factionAvailable.ordinal()));
        this.addButton(new GuiButtonNop(this, 22, this.guiLeft + 56, this.guiTop + 104, 60, 20, new String[] { "faction.friendly", "faction.neutral", "faction.unfriendly" }, this.availabitily.factionStance.ordinal()));
        this.addButton(new GuiButtonNop(this, 21, this.guiLeft + 118, this.guiTop + 104, 110, 20, "availability.selectfaction"));
        this.getButton(21).setEnabled(this.availabitily.factionAvailable != EnumAvailabilityFactionType.Always);
        this.getButton(22).setEnabled(this.availabitily.factionAvailable != EnumAvailabilityFactionType.Always);
        this.addButton(new GuiButtonNop(this, 23, this.guiLeft + 230, this.guiTop + 104, 20, 20, "X"));
        this.addButton(new GuiButtonNop(this, 24, this.guiLeft + 4, this.guiTop + 126, 50, 20, new String[] { "availability.always", "availability.is", "availability.isnot" }, this.availabitily.faction2Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 27, this.guiLeft + 56, this.guiTop + 126, 60, 20, new String[] { "faction.friendly", "faction.neutral", "faction.unfriendly" }, this.availabitily.faction2Stance.ordinal()));
        this.addButton(new GuiButtonNop(this, 25, this.guiLeft + 118, this.guiTop + 126, 110, 20, "availability.selectfaction"));
        this.getButton(25).setEnabled(this.availabitily.faction2Available != EnumAvailabilityFactionType.Always);
        this.getButton(27).setEnabled(this.availabitily.faction2Available != EnumAvailabilityFactionType.Always);
        this.addButton(new GuiButtonNop(this, 26, this.guiLeft + 230, this.guiTop + 126, 20, 20, "X"));
        this.addLabel(new GuiLabel(50, "availability.daytime", this.guiLeft + 4, this.guiTop + 153));
        this.addButton(new GuiButtonNop(this, 50, this.guiLeft + 50, this.guiTop + 148, 150, 20, new String[] { "availability.wholeday", "availability.night", "availability.day" }, this.availabitily.daytime.ordinal()));
        this.addLabel(new GuiLabel(51, "availability.minlevel", this.guiLeft + 4, this.guiTop + 175));
        this.addTextField(new GuiTextFieldNop(51, this, this.guiLeft + 50, this.guiTop + 170, 90, 20, this.availabitily.minPlayerLevel + ""));
        this.getTextField(51).numbersOnly = true;
        this.getTextField(51).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
        this.updateGuiButtons();
    }

    private void updateGuiButtons() {
        if (this.availabitily.factionId >= 0) {
            Packets.sendServer(new SPacketFactionGet(this.availabitily.factionId));
        }
        if (this.availabitily.faction2Id >= 0) {
            Packets.sendServer(new SPacketFactionGet(this.availabitily.faction2Id));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.setSubGui(new SubGuiNpcAvailabilityDialog(this.availabitily));
        }
        if (guibutton.id == 1) {
            this.setSubGui(new SubGuiNpcAvailabilityQuest(this.availabitily));
        }
        if (guibutton.id == 2) {
            this.setSubGui(new SubGuiNpcAvailabilityScoreboard(this.availabitily));
        }
        if (guibutton.id == 20) {
            this.availabitily.setFactionAvailability(guibutton.getValue());
            if (this.availabitily.factionAvailable == EnumAvailabilityFactionType.Always) {
                this.availabitily.factionId = -1;
            }
            this.init();
        }
        if (guibutton.id == 24) {
            this.availabitily.setFaction2Availability(guibutton.getValue());
            if (this.availabitily.faction2Available == EnumAvailabilityFactionType.Always) {
                this.availabitily.faction2Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 21) {
            this.slot = 1;
            GuiNPCFactionSelection gui = new GuiNPCFactionSelection(this.npc, this.getParent(), this.availabitily.factionId);
            gui.listener = this;
            NoppesUtil.openGUI(this.player, gui);
        }
        if (guibutton.id == 25) {
            this.slot = 2;
            GuiNPCFactionSelection gui = new GuiNPCFactionSelection(this.npc, this.getParent(), this.availabitily.faction2Id);
            gui.listener = this;
            NoppesUtil.openGUI(this.player, gui);
        }
        if (guibutton.id == 22) {
            this.availabitily.setFactionAvailabilityStance(guibutton.getValue());
        }
        if (guibutton.id == 27) {
            this.availabitily.setFaction2AvailabilityStance(guibutton.getValue());
        }
        if (guibutton.id == 23) {
            this.availabitily.factionId = -1;
            this.getButton(21).setDisplayText("availability.selectfaction");
        }
        if (guibutton.id == 26) {
            this.availabitily.faction2Id = -1;
            this.getButton(25).setDisplayText("availability.selectfaction");
        }
        if (guibutton.id == 50) {
            this.availabitily.daytime = EnumDayTime.values()[guibutton.getValue()];
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void selected(int id, String name) {
        if (this.slot == 1) {
            this.availabitily.factionId = id;
        }
        if (this.slot == 2) {
            this.availabitily.faction2Id = id;
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        if (compound.contains("Slot")) {
            Faction faction = new Faction();
            faction.readNBT(compound);
            if (this.availabitily.factionId == faction.id) {
                this.getButton(21).setDisplayText(faction.name);
            }
            if (this.availabitily.faction2Id == faction.id) {
                this.getButton(25).setDisplayText(faction.name);
            }
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 51) {
            this.availabitily.minPlayerLevel = textfield.getInteger();
        }
    }
}