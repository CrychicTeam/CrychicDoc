package noppes.npcs.client.gui.mainmenu;

import net.minecraft.core.BlockPos;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNpcManagePlayerData;
import noppes.npcs.client.gui.global.GuiNpcNaturalSpawns;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiNPCGlobalMainMenu extends GuiNPCInterface2 {

    public GuiNPCGlobalMainMenu(EntityNPCInterface npc) {
        super(npc, 6);
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 10;
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 85, y, "global.banks"));
        int var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 3, var10005, y, "menu.factions"));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 4, var10005, y, "dialog.dialogs"));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 11, var10005, y, "quest.quests"));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 12, var10005, y, "global.transport"));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 13, var10005, y, "global.playerdata"));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 14, var10005, y, NoppesStringUtils.translate("global.recipes", "(Broken)")));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 15, var10005, y, NoppesStringUtils.translate("global.naturalspawn", "(WIP)")));
        var10005 = this.guiLeft + 85;
        y += 22;
        this.addButton(new GuiButtonNop(this, 16, var10005, y, "global.linked"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 11) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageQuests);
        }
        if (id == 2) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageBanks);
        }
        if (id == 3) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageFactions);
        }
        if (id == 4) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageDialogs);
        }
        if (id == 12) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageTransport);
        }
        if (id == 13) {
            NoppesUtil.openGUI(this.player, new GuiNpcManagePlayerData(this.npc, this));
        }
        if (id == 14) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, new BlockPos(4, 0, 0));
        }
        if (id == 15) {
            NoppesUtil.openGUI(this.player, new GuiNpcNaturalSpawns(this.npc));
        }
        if (id == 16) {
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageLinked);
        }
    }

    @Override
    public void save() {
    }
}