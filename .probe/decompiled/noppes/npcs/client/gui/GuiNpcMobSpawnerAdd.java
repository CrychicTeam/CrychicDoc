package noppes.npcs.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import noppes.npcs.client.controllers.ClientCloneController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCloneNameCheck;
import noppes.npcs.packets.server.SPacketCloneSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcMobSpawnerAdd extends GuiNPCInterface implements IGuiData {

    private Entity toClone;

    private CompoundTag compound;

    private static boolean serverSide = true;

    private static int tab = 1;

    public GuiNpcMobSpawnerAdd(CompoundTag compound) {
        this.toClone = (Entity) EntityType.create(compound, Minecraft.getInstance().level).orElse(null);
        this.compound = compound;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.m_7856_();
        String name = this.toClone.getName().getString();
        this.addLabel(new GuiLabel(0, "Save as", this.guiLeft + 4, this.guiTop + 6));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 4, this.guiTop + 18, 200, 20, name));
        this.addLabel(new GuiLabel(1, "Tab", this.guiLeft + 10, this.guiTop + 50));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 40, this.guiTop + 45, 20, 20, new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, tab - 1));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 4, this.guiTop + 95, new String[] { "clone.client", "clone.server" }, serverSide ? 1 : 0));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, this.guiTop + 70, 80, 20, "gui.save"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 86, this.guiTop + 70, 80, 20, "gui.cancel"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            String name = this.getTextField(0).m_94155_();
            if (name.isEmpty()) {
                return;
            }
            int tab = guibutton.getValue() + 1;
            if (!serverSide) {
                if (ClientCloneController.Instance.getCloneData(null, name, tab) != null) {
                    this.setScreen(new ConfirmScreen(this::accept, Component.translatable(""), Component.translatable("clone.overwrite")));
                } else {
                    this.accept(true);
                }
            } else {
                Packets.sendServer(new SPacketCloneNameCheck(name, tab));
            }
        }
        if (id == 1) {
            this.close();
        }
        if (id == 2) {
            GuiNpcMobSpawnerAdd.tab = guibutton.getValue() + 1;
        }
        if (id == 3) {
            serverSide = guibutton.getValue() == 1;
        }
    }

    public void accept(boolean confirm) {
        if (confirm) {
            String name = this.getTextField(0).m_94155_();
            if (!serverSide) {
                ClientCloneController.Instance.addClone(this.compound, name, tab);
            } else {
                Packets.sendServer(new SPacketCloneSave(name, tab));
            }
            this.close();
        } else {
            this.setScreen(this);
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        if (compound.contains("NameExists")) {
            if (compound.getBoolean("NameExists")) {
                this.setScreen(new ConfirmScreen(this::accept, Component.translatable(""), Component.translatable("clone.overwrite")));
            } else {
                this.accept(true);
            }
        }
    }
}