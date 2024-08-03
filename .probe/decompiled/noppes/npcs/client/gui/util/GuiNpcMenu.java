package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuClose;
import noppes.npcs.packets.server.SPacketNpcDelete;
import noppes.npcs.shared.client.gui.components.GuiMenuTopButton;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiNpcMenu {

    private IGuiInterface parent;

    private GuiMenuTopButton[] topButtons = new GuiMenuTopButton[0];

    private int activeMenu;

    private EntityNPCInterface npc;

    public GuiNpcMenu(IGuiInterface parent, int activeMenu, EntityNPCInterface npc) {
        this.parent = parent;
        this.activeMenu = activeMenu;
        this.npc = npc;
    }

    public void initGui(int guiLeft, int guiTop, int width) {
        final Minecraft mc = Minecraft.getInstance();
        GuiMenuTopButton display = new GuiMenuTopButton(this.parent, 1, guiLeft + 4, guiTop - 17, "menu.display") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 1;
                CustomNpcs.proxy.openGui(GuiNpcMenu.this.npc, EnumGuiType.MainMenuDisplay);
            }
        };
        GuiMenuTopButton stats = new GuiMenuTopButton(this.parent, 2, display.m_252754_() + display.m_5711_(), guiTop - 17, "menu.stats") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 2;
                CustomNpcs.proxy.openGui(GuiNpcMenu.this.npc, EnumGuiType.MainMenuStats);
            }
        };
        GuiMenuTopButton ai = new GuiMenuTopButton(this.parent, 3, stats.m_252754_() + stats.m_5711_(), guiTop - 17, "menu.ai") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 3;
                CustomNpcs.proxy.openGui(GuiNpcMenu.this.npc, EnumGuiType.MainMenuAI);
            }
        };
        GuiMenuTopButton inv = new GuiMenuTopButton(this.parent, 4, ai.m_252754_() + ai.m_5711_(), guiTop - 17, "menu.inventory") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 4;
                NoppesUtil.requestOpenGUI(EnumGuiType.MainMenuInv);
            }
        };
        GuiMenuTopButton advanced = new GuiMenuTopButton(this.parent, 5, inv.m_252754_() + inv.m_5711_(), guiTop - 17, "menu.advanced") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 5;
                CustomNpcs.proxy.openGui(GuiNpcMenu.this.npc, EnumGuiType.MainMenuAdvanced);
            }
        };
        GuiMenuTopButton global = new GuiMenuTopButton(this.parent, 6, advanced.m_252754_() + advanced.m_5711_(), guiTop - 17, "menu.global") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.save();
                GuiNpcMenu.this.activeMenu = 6;
                CustomNpcs.proxy.openGui(GuiNpcMenu.this.npc, EnumGuiType.MainMenuGlobal);
            }
        };
        GuiMenuTopButton close = new GuiMenuTopButton(this.parent, 0, guiLeft + width - 22, guiTop - 17, "X") {

            @Override
            public void onClick(double x, double y) {
                GuiNpcMenu.this.close();
            }
        };
        GuiMenuTopButton delete = new GuiMenuTopButton(this.parent, 66, guiLeft + width - 72, guiTop - 17, "selectServer.delete") {

            @Override
            public void onClick(double x, double y) {
                ConfirmScreen guiyesno = new ConfirmScreen(GuiNpcMenu.this::accept, Component.translatable(""), Component.translatable("gui.deleteMessage", GuiNpcMenu.this.npc.m_5446_().getString()));
                mc.setScreen(guiyesno);
            }
        };
        delete.m_252865_(close.m_252754_() - delete.m_5711_());
        this.topButtons = new GuiMenuTopButton[] { display, stats, ai, inv, advanced, global, close, delete };
        for (GuiMenuTopButton button : this.topButtons) {
            button.active = button.id == this.activeMenu;
        }
    }

    private void save() {
        GuiTextFieldNop.unfocus();
        this.parent.save();
    }

    private void close() {
        ((Screen) this.parent).onClose();
        if (this.npc != null) {
            this.npc.reset();
            Packets.sendServer(new SPacketMenuClose());
        }
    }

    public boolean mouseClicked(double i, double j, int k) {
        if (k == 0) {
            Minecraft mc = Minecraft.getInstance();
            for (GuiMenuTopButton button : this.topButtons) {
                if (button.mouseClicked(i, j, k)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void drawElements(GuiGraphics graphics, Font font, int i, int j, Minecraft mc, float f) {
        for (GuiMenuTopButton button : this.topButtons) {
            button.render(graphics, i, j, f);
        }
    }

    public void accept(boolean flag) {
        Minecraft mc = Minecraft.getInstance();
        if (flag) {
            Packets.sendServer(new SPacketNpcDelete());
            mc.setScreen(null);
            mc.mouseHandler.grabMouse();
        } else {
            NoppesUtil.openGUI(mc.player, this.parent);
        }
    }
}