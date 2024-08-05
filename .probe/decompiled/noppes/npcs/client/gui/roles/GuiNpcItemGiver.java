package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobItemGiver;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiNpcItemGiver extends GuiContainerNPCInterface2<ContainerNpcItemGiver> {

    private JobItemGiver role;

    public GuiNpcItemGiver(ContainerNpcItemGiver container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.f_97727_ = 200;
        this.role = (JobItemGiver) this.npc.job;
        this.setBackground("npcitemgiver.png");
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 6, this.guiTop + 6, 140, 20, new String[] { "Random Item", "All Items", "Give Not Owned Items", "Give When Doesnt Own Any", "Chained" }, this.role.givingMethod));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 6, this.guiTop + 29, 140, 20, new String[] { "Timer", "Give Only Once", "Daily" }, this.role.cooldownType));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 55, this.guiTop + 54, 90, 20, this.role.cooldown + ""));
        this.getTextField(0).numbersOnly = true;
        this.addLabel(new GuiLabel(0, "Cooldown:", this.guiLeft + 6, this.guiTop + 59));
        this.addLabel(new GuiLabel(1, "Items to give", this.guiLeft + 46, this.guiTop + 79));
        this.getTextField(0).numbersOnly = true;
        int i = 0;
        for (String line : this.role.lines) {
            this.addTextField(new GuiTextFieldNop(i + 1, this, this.guiLeft + 150, this.guiTop + 6 + i * 24, 236, 20, line));
            i++;
        }
        while (i < 3) {
            this.addTextField(new GuiTextFieldNop(i + 1, this, this.guiLeft + 150, this.guiTop + 6 + i * 24, 236, 20, ""));
            i++;
        }
        this.getTextField(0).enabled = this.role.isOnTimer();
        this.getLabel(0).enabled = this.role.isOnTimer();
        this.addLabel(new GuiLabel(4, "availability.options", this.guiLeft + 180, this.guiTop + 101));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 280, this.guiTop + 96, 50, 20, "selectServer.edit"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.role.givingMethod = guibutton.getValue();
        }
        if (guibutton.id == 1) {
            this.role.cooldownType = guibutton.getValue();
            this.getTextField(0).enabled = this.role.isOnTimer();
            this.getLabel(0).enabled = this.role.isOnTimer();
        }
        if (guibutton.id == 4) {
            this.setSubGui(new SubGuiNpcAvailability(this.role.availability));
        }
    }

    @Override
    public void save() {
        List<String> lines = new ArrayList();
        for (int i = 1; i < 4; i++) {
            GuiTextFieldNop tf = this.getTextField(i);
            if (!tf.isEmpty()) {
                lines.add(tf.m_94155_());
            }
        }
        this.role.lines = lines;
        int cc = 10;
        if (!this.getTextField(0).isEmpty() && this.getTextField(0).isInteger()) {
            cc = this.getTextField(0).getInteger();
        }
        this.role.cooldown = cc;
        Packets.sendServer(new SPacketNpcJobSave(this.role.save(new CompoundTag())));
    }
}