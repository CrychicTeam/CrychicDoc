package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketPlayerMailDelete;
import noppes.npcs.packets.server.SPacketPlayerMailGet;
import noppes.npcs.packets.server.SPacketPlayerMailOpen;
import noppes.npcs.packets.server.SPacketPlayerMailRead;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiMailbox extends GuiNPCInterface implements IGuiData, ICustomScrollListener {

    private GuiCustomScrollNop scroll;

    private PlayerMailData data;

    private PlayerMail selected;

    public GuiMailbox() {
        this.imageWidth = 256;
        this.setBackground("menubg.png");
        Packets.sendServer(new SPacketPlayerMailGet());
    }

    @Override
    public void init() {
        super.m_7856_();
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(165, 186);
        }
        this.scroll.guiLeft = this.guiLeft + 4;
        this.scroll.guiTop = this.guiTop + 4;
        this.addScroll(this.scroll);
        String title = I18n.get("mailbox.name");
        int x = (this.imageWidth - this.f_96547_.width(title)) / 2;
        this.addLabel(new GuiLabel(0, title, this.guiLeft + x, this.guiTop - 8));
        if (this.selected != null) {
            this.addLabel(new GuiLabel(3, I18n.get("mailbox.sender") + ":", this.guiLeft + 170, this.guiTop + 6));
            this.addLabel(new GuiLabel(1, this.selected.sender, this.guiLeft + 174, this.guiTop + 18));
            this.addLabel(new GuiLabel(2, I18n.get("mailbox.timesend", this.getTimePast()), this.guiLeft + 174, this.guiTop + 30));
        }
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, this.guiTop + 192, 82, 20, "mailbox.read"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 88, this.guiTop + 192, 82, 20, "selectServer.delete"));
        this.getButton(1).setEnabled(this.selected != null);
    }

    private String getTimePast() {
        if (this.selected.timePast > 86400000L) {
            int days = (int) (this.selected.timePast / 86400000L);
            return days == 1 ? days + " " + I18n.get("mailbox.day") : days + " " + I18n.get("mailbox.days");
        } else if (this.selected.timePast > 3600000L) {
            int hours = (int) (this.selected.timePast / 3600000L);
            return hours == 1 ? hours + " " + I18n.get("mailbox.hour") : hours + " " + I18n.get("mailbox.hours");
        } else {
            int minutes = (int) (this.selected.timePast / 60000L);
            return minutes == 1 ? minutes + " " + I18n.get("mailbox.minutes") : minutes + " " + I18n.get("mailbox.minutes");
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (this.scroll.hasSelected()) {
            if (id == 0) {
                GuiMailmanWrite.parent = this;
                GuiMailmanWrite.mail = this.selected;
                Packets.sendServer(new SPacketPlayerMailOpen(this.selected.time, this.selected.sender));
                this.selected = null;
                this.scroll.clearSelection();
            }
            if (id == 1) {
                ConfirmScreen guiyesno = new ConfirmScreen(bo -> {
                    if (bo && this.selected != null) {
                        Packets.sendServer(new SPacketPlayerMailDelete(this.selected.time, this.selected.sender));
                        this.selected = null;
                    }
                    NoppesUtil.openGUI(this.player, this);
                }, Component.translatable(""), Component.translatable("gui.deleteMessage"));
                this.setScreen(guiyesno);
            }
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        super.m_6375_(i, j, k);
        this.scroll.mouseClicked(i, j, k);
        return true;
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        PlayerMailData data = new PlayerMailData();
        data.loadNBTData(compound);
        List<String> list = new ArrayList();
        Collections.sort(data.playermail, (o1, o2) -> {
            if (o1.time == o2.time) {
                return 0;
            } else {
                return o1.time > o2.time ? -1 : 1;
            }
        });
        for (PlayerMail mail : data.playermail) {
            list.add(mail.subject);
        }
        this.data = data;
        this.scroll.clear();
        this.selected = null;
        this.scroll.setUnsortedList(list);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        this.selected = (PlayerMail) this.data.playermail.get(guiCustomScroll.getSelectedIndex());
        this.init();
        if (this.selected != null && !this.selected.beenRead) {
            this.selected.beenRead = true;
            Packets.sendServer(new SPacketPlayerMailRead(this.selected.time, this.selected.sender));
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}