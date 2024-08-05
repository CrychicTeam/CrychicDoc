package noppes.npcs.client.gui.mainmenu;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.advanced.GuiNPCAdvancedLinkedNpc;
import noppes.npcs.client.gui.advanced.GuiNPCDialogNpcOptions;
import noppes.npcs.client.gui.advanced.GuiNPCFactionSetup;
import noppes.npcs.client.gui.advanced.GuiNPCLinesMenu;
import noppes.npcs.client.gui.advanced.GuiNPCMarks;
import noppes.npcs.client.gui.advanced.GuiNPCNightSetup;
import noppes.npcs.client.gui.advanced.GuiNPCScenes;
import noppes.npcs.client.gui.advanced.GuiNPCSoundsMenu;
import noppes.npcs.client.gui.roles.GuiJobFarmer;
import noppes.npcs.client.gui.roles.GuiNpcBankSetup;
import noppes.npcs.client.gui.roles.GuiNpcBard;
import noppes.npcs.client.gui.roles.GuiNpcCompanion;
import noppes.npcs.client.gui.roles.GuiNpcConversation;
import noppes.npcs.client.gui.roles.GuiNpcFollowerJob;
import noppes.npcs.client.gui.roles.GuiNpcGuard;
import noppes.npcs.client.gui.roles.GuiNpcHealer;
import noppes.npcs.client.gui.roles.GuiNpcPuppet;
import noppes.npcs.client.gui.roles.GuiNpcSpawner;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.gui.roles.GuiRoleDialog;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.packets.server.SPacketNpcJobGet;
import noppes.npcs.packets.server.SPacketNpcRoleGet;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiNpcAdvanced extends GuiNPCInterface2 implements IGuiData {

    private boolean hasChanges = false;

    public GuiNpcAdvanced(EntityNPCInterface npc) {
        super(npc, 5);
        Packets.sendServer(new SPacketMenuGet(EnumMenuType.ADVANCED));
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 8;
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 85 + 160, y, 52, 20, "selectServer.edit"));
        this.addButton(new GuiButtonBiDirectional(this, 8, this.guiLeft + 85, y, 155, 20, new String[] { "role.none", "role.trader", "role.follower", "role.bank", "role.transporter", "role.mailman", NoppesStringUtils.translate("role.companion", "(WIP)"), "dialog.dialog" }, this.npc.role.getType()));
        this.getButton(3).setEnabled(this.npc.role.getType() != 0 && this.npc.role.getType() != 5);
        int var10005 = this.guiLeft + 85 + 160;
        y += 22;
        this.addButton(new GuiButtonNop(this, 4, var10005, y, 52, 20, "selectServer.edit"));
        this.addButton(new GuiButtonBiDirectional(this, 5, this.guiLeft + 85, y, 155, 20, new String[] { "job.none", "job.bard", "job.healer", "job.guard", "job.itemgiver", "role.follower", "job.spawner", "job.conversation", "job.chunkloader", "job.puppet", "job.builder", "job.farmer" }, this.npc.job.getType()));
        this.getButton(4).setEnabled(this.npc.job.getType() != 0 && this.npc.job.getType() != 8 && this.npc.job.getType() != 10);
        var10005 = this.guiLeft + 15;
        y += 22;
        this.addButton(new GuiButtonNop(this, 7, var10005, y, 190, 20, "advanced.lines"));
        this.addButton(new GuiButtonNop(this, 9, this.guiLeft + 208, y, 190, 20, "menu.factions"));
        var10005 = this.guiLeft + 15;
        y += 22;
        this.addButton(new GuiButtonNop(this, 10, var10005, y, 190, 20, "dialog.dialogs"));
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 208, y, 190, 20, "advanced.sounds"));
        var10005 = this.guiLeft + 15;
        y += 22;
        this.addButton(new GuiButtonNop(this, 12, var10005, y, 190, 20, "advanced.night"));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 208, y, 190, 20, "global.linked"));
        var10005 = this.guiLeft + 15;
        y += 22;
        this.addButton(new GuiButtonNop(this, 14, var10005, y, 190, 20, "advanced.scenes"));
        this.addButton(new GuiButtonNop(this, 15, this.guiLeft + 208, y, 190, 20, "advanced.marks"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 3) {
            this.save();
            Packets.sendServer(new SPacketNpcRoleGet());
        }
        if (guibutton.id == 8) {
            this.hasChanges = true;
            this.npc.advanced.setRole(guibutton.getValue());
            Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
            this.getButton(3).setEnabled(this.npc.role.getType() != 0 && this.npc.role.getType() != 5);
        }
        if (guibutton.id == 4) {
            this.save();
            Packets.sendServer(new SPacketNpcJobGet());
        }
        if (guibutton.id == 5) {
            this.hasChanges = true;
            this.npc.advanced.setJob(guibutton.getValue());
            Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
            this.getButton(4).setEnabled(this.npc.job.getType() != 0 && this.npc.job.getType() != 8 && this.npc.job.getType() != 10);
        }
        if (guibutton.id == 9) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCFactionSetup(this.npc));
        }
        if (guibutton.id == 10) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCDialogNpcOptions(this.npc, this));
        }
        if (guibutton.id == 11) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCSoundsMenu(this.npc));
        }
        if (guibutton.id == 7) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCLinesMenu(this.npc));
        }
        if (guibutton.id == 12) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCNightSetup(this.npc));
        }
        if (guibutton.id == 13) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCAdvancedLinkedNpc(this.npc));
        }
        if (guibutton.id == 14) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCScenes(this.npc));
        }
        if (guibutton.id == 15) {
            this.save();
            NoppesUtil.openGUI(this.player, new GuiNPCMarks(this.npc));
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        if (compound.contains("RoleData")) {
            this.npc.role.load(compound);
            if (this.npc.role.getType() == 1) {
                NoppesUtil.requestOpenGUI(EnumGuiType.SetupTrader);
            } else if (this.npc.role.getType() == 2) {
                NoppesUtil.requestOpenGUI(EnumGuiType.SetupFollower);
            } else if (this.npc.role.getType() == 3) {
                this.setScreen(new GuiNpcBankSetup(this.npc));
            } else if (this.npc.role.getType() == 4) {
                this.setScreen(new GuiNpcTransporter(this.npc));
            } else if (this.npc.role.getType() == 6) {
                this.setScreen(new GuiNpcCompanion(this.npc));
            } else if (this.npc.role.getType() == 7) {
                NoppesUtil.openGUI(this.player, new GuiRoleDialog(this.npc));
            }
        } else if (compound.contains("JobData")) {
            this.npc.job.load(compound);
            if (this.npc.job.getType() == 1) {
                NoppesUtil.openGUI(this.player, new GuiNpcBard(this.npc));
            } else if (this.npc.job.getType() == 2) {
                NoppesUtil.openGUI(this.player, new GuiNpcHealer(this.npc));
            } else if (this.npc.job.getType() == 3) {
                NoppesUtil.openGUI(this.player, new GuiNpcGuard(this.npc));
            } else if (this.npc.job.getType() == 4) {
                NoppesUtil.requestOpenGUI(EnumGuiType.SetupItemGiver);
            } else if (this.npc.job.getType() == 5) {
                NoppesUtil.openGUI(this.player, new GuiNpcFollowerJob(this.npc));
            } else if (this.npc.job.getType() == 6) {
                NoppesUtil.openGUI(this.player, new GuiNpcSpawner(this.npc));
            } else if (this.npc.job.getType() == 7) {
                NoppesUtil.openGUI(this.player, new GuiNpcConversation(this.npc));
            } else if (this.npc.job.getType() == 9) {
                NoppesUtil.openGUI(this.player, new GuiNpcPuppet(this, (EntityCustomNpc) this.npc));
            } else if (this.npc.job.getType() == 11) {
                NoppesUtil.openGUI(this.player, new GuiJobFarmer(this.npc));
            }
        } else {
            this.npc.advanced.readToNBT(compound);
            this.init();
        }
    }

    @Override
    public void save() {
        if (this.hasChanges) {
            Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
            this.hasChanges = false;
        }
    }
}