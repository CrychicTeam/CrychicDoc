package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.SubGuiNpcName;
import noppes.npcs.client.gui.model.GuiCreationEntities;
import noppes.npcs.client.gui.select.GuiTextureSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.packets.server.SPacketNpRandomNameSet;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcDisplay extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {

    private DataDisplay display;

    public GuiNpcDisplay(EntityNPCInterface npc) {
        super(npc, 1);
        this.display = npc.display;
        Packets.sendServer(new SPacketMenuGet(EnumMenuType.DISPLAY));
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 4;
        this.addLabel(new GuiLabel(0, "gui.name", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 50, y, 206, 20, this.display.getName()));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 253 + 52, y, 110, 20, new String[] { "display.show", "display.hide", "display.showAttacking" }, this.display.getShowName()));
        this.addButton(new GuiButtonNop(this, 14, this.guiLeft + 259, y, 20, 20, Character.toString('↻')));
        this.addButton(new GuiButtonNop(this, 15, this.guiLeft + 259 + 22, y, 20, 20, Character.toString('⋮')));
        y += 23;
        this.addLabel(new GuiLabel(11, "gui.title", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(11, this, this.guiLeft + 50, y, 186, 20, this.display.getTitle()));
        y += 23;
        this.addLabel(new GuiLabel(1, "display.model", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 50, y, 110, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(2, "display.size", this.guiLeft + 175, y + 5));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 203, y, 40, 20, this.display.getSize() + ""));
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(1, 30, 5);
        this.addLabel(new GuiLabel(3, "(1-30)", this.guiLeft + 246, y + 5));
        y += 23;
        this.addLabel(new GuiLabel(4, "display.texture", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 80, y, 200, 20, this.display.skinType == 0 ? this.display.getSkinTexture() : this.display.getSkinUrl()));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 325, y, 38, 20, "mco.template.button.select"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 283, y, 40, 20, new String[] { "display.texture", "display.player", "display.url" }, this.display.skinType));
        this.getButton(3).setEnabled(this.display.skinType == 0);
        if (this.display.skinType == 1 && !this.display.getSkinPlayer().isEmpty()) {
            this.getTextField(3).m_94144_(this.display.getSkinPlayer());
        }
        y += 23;
        this.addLabel(new GuiLabel(8, "display.cape", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(8, this, this.guiLeft + 80, y, 200, 20, this.display.getCapeTexture()));
        this.addButton(new GuiButtonNop(this, 8, this.guiLeft + 283, y, 80, 20, "display.selectTexture"));
        y += 23;
        this.addLabel(new GuiLabel(9, "display.overlay", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(9, this, this.guiLeft + 80, y, 200, 20, this.display.getOverlayTexture()));
        this.addButton(new GuiButtonNop(this, 9, this.guiLeft + 283, y, 80, 20, "display.selectTexture"));
        y += 23;
        this.addLabel(new GuiLabel(5, "display.livingAnimation", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 120, y, 50, 20, new String[] { "gui.yes", "gui.no" }, this.display.getHasLivingAnimation() ? 0 : 1));
        this.addLabel(new GuiLabel(6, "display.tint", this.guiLeft + 180, y + 5));
        String color = Integer.toHexString(this.display.getTint());
        while (color.length() < 6) {
            color = "0" + color;
        }
        this.addTextField(new GuiTextFieldNop(6, this, this.guiLeft + 220, y, 60, 20, color));
        this.getTextField(6).m_94202_(this.display.getTint());
        y += 23;
        this.addLabel(new GuiLabel(7, "display.visible", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 7, this.guiLeft + 40, y, 50, 20, new String[] { "gui.yes", "gui.no", "gui.partly" }, this.display.getVisible()));
        this.addButton(new GuiButtonNop(this, 16, this.guiLeft + 92, y, 78, 20, "availability.name"));
        this.addLabel(new GuiLabel(13, "display.hitbox", this.guiLeft + 180, y + 5));
        this.addButton(new GuiButtonBiDirectional(this, 13, this.guiLeft + 230, y, 100, 20, this.display.getHitboxState(), "stats.normal", "gui.none", "hair.solid"));
        y += 23;
        this.addLabel(new GuiLabel(10, "display.bossbar", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 60, y, 110, 20, new String[] { "display.hide", "display.show", "display.showAttacking" }, this.display.getBossbar()));
        this.addLabel(new GuiLabel(12, "gui.color", this.guiLeft + 180, y + 5));
        this.addButton(new GuiButtonBiDirectional(this, 12, this.guiLeft + 230, y, 100, 20, this.display.getBossColor(), "color.pink", "color.blue", "color.red", "color.green", "color.yellow", "color.purple", "color.white"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            if (!textfield.isEmpty()) {
                this.display.setName(textfield.m_94155_());
            } else {
                textfield.m_94144_(this.display.getName());
            }
        } else if (textfield.id == 2) {
            this.display.setSize(textfield.getInteger());
        } else if (textfield.id == 3) {
            if (this.display.skinType == 2) {
                this.display.setSkinUrl(textfield.m_94155_());
            } else if (this.display.skinType == 1) {
                this.display.setSkinPlayer(textfield.m_94155_());
            } else {
                this.display.setSkinTexture(textfield.m_94155_());
            }
        } else if (textfield.id == 6) {
            int color = 0;
            try {
                color = Integer.parseInt(textfield.m_94155_(), 16);
            } catch (NumberFormatException var4) {
                color = 16777215;
            }
            this.display.setTint(color);
            textfield.m_94202_(this.display.getTint());
        } else if (textfield.id == 8) {
            this.display.setCapeTexture(textfield.m_94155_());
        } else if (textfield.id == 9) {
            this.display.setOverlayTexture(textfield.m_94155_());
        } else if (textfield.id == 11) {
            this.display.setTitle(textfield.m_94155_());
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.display.setShowName(guibutton.getValue());
        }
        if (guibutton.id == 1) {
            NoppesUtil.openGUI(this.player, new GuiCreationEntities((EntityCustomNpc) this.npc));
        }
        if (guibutton.id == 2) {
            this.display.setSkinUrl("");
            this.display.setSkinPlayer(null);
            this.display.skinType = (byte) guibutton.getValue();
            this.init();
        } else if (guibutton.id == 3) {
            this.setSubGui(new GuiTextureSelection(this.npc, this.npc.display.getSkinTexture(), 0));
        } else if (guibutton.id == 5) {
            this.display.setHasLivingAnimation(guibutton.getValue() == 0);
        } else if (guibutton.id == 7) {
            this.display.setVisible(guibutton.getValue());
        } else if (guibutton.id == 8) {
            this.setSubGui(new GuiTextureSelection(this.npc, this.npc.display.getCapeTexture(), 1));
        } else if (guibutton.id == 9) {
            this.setSubGui(new GuiTextureSelection(this.npc, this.npc.display.getOverlayTexture(), 2));
        } else if (guibutton.id == 10) {
            this.display.setBossbar(guibutton.getValue());
        } else if (guibutton.id == 12) {
            this.display.setBossColor(guibutton.getValue());
        } else if (guibutton.id == 13) {
            this.display.setHitboxState((byte) guibutton.getValue());
        } else if (guibutton.id == 14) {
            Packets.sendServer(new SPacketNpRandomNameSet(this.display.getMarkovGeneratorId(), this.display.getMarkovGender()));
        } else if (guibutton.id == 15) {
            this.setSubGui(new SubGuiNpcName(this.display));
        } else if (guibutton.id == 16) {
            this.setSubGui(new SubGuiNpcAvailability(this.display.availability));
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        this.init();
    }

    @Override
    public void save() {
        if (this.display.skinType == 1) {
            this.display.loadProfile();
        }
        this.npc.textureLocation = null;
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.DISPLAY, this.display.save(new CompoundTag())));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.display.readToNBT(compound);
        this.init();
    }
}