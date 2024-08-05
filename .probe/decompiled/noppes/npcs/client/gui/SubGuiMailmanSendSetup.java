package noppes.npcs.client.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.client.gui.player.GuiMailmanWrite;
import noppes.npcs.client.gui.select.GuiQuestSelection;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMailSetup;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiMailmanSendSetup extends GuiBasic implements ITextfieldListener, GuiSelectionListener {

    private PlayerMail mail;

    public SubGuiMailmanSendSetup(PlayerMail mail) {
        this.imageWidth = 256;
        this.setBackground("menubg.png");
        this.mail = mail;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "mailbox.subject", this.guiLeft + 4, this.guiTop + 19));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 60, this.guiTop + 14, 180, 20, this.mail.subject));
        this.addLabel(new GuiLabel(0, "mailbox.sender", this.guiLeft + 4, this.guiTop + 41));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 60, this.guiTop + 36, 180, 20, this.mail.sender));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 29, this.guiTop + 100, "mailbox.write"));
        this.addLabel(new GuiLabel(3, "quest.quest", this.guiLeft + 13, this.guiTop + 135));
        IQuest quest = this.mail.getQuest();
        String title = "gui.select";
        if (quest != null) {
            title = quest.getName();
        }
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 70, this.guiTop + 130, 100, 20, title));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 171, this.guiTop + 130, 20, 20, "X"));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 26, this.guiTop + 190, 100, 20, "gui.done"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 130, this.guiTop + 190, 100, 20, "gui.cancel"));
        if (this.player.f_36096_ instanceof ContainerMail container) {
            this.mail.items = container.mail.items;
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 1) {
            this.mail.questId = -1;
            this.mail.message = new CompoundTag();
            this.close();
        }
        if (id == 2) {
            GuiMailmanWrite.parent = this.getParent();
            GuiMailmanWrite.mail = this.mail;
            Packets.sendServer(new SPacketMailSetup(this.mail.writeNBT()));
        }
        if (id == 3) {
            this.setSubGui(new GuiQuestSelection(this.mail.questId));
        }
        if (id == 4) {
            this.mail.questId = -1;
            this.init();
        }
    }

    @Override
    public void selected(int ob, String name) {
        this.mail.questId = ob;
        this.init();
    }

    @Override
    public void save() {
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            this.mail.sender = textfield.m_94155_();
        }
        if (textfield.id == 1) {
            this.mail.subject = textfield.m_94155_();
        }
    }
}