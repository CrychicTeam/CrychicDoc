package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcRoleCompanionUpdate;
import noppes.npcs.packets.server.SPacketNpcRoleSave;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiSliderNop;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ISliderListener;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcCompanion extends GuiNPCInterface2 implements ITextfieldListener, ISliderListener {

    private RoleCompanion role;

    private List<GuiNpcCompanionTalents.GuiTalent> talents = new ArrayList();

    public GuiNpcCompanion(EntityNPCInterface npc) {
        super(npc);
        this.role = (RoleCompanion) npc.role;
    }

    @Override
    public void init() {
        super.init();
        this.talents = new ArrayList();
        int y = this.guiTop + 4;
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 70, y, 90, 20, new String[] { EnumCompanionStage.BABY.name, EnumCompanionStage.CHILD.name, EnumCompanionStage.TEEN.name, EnumCompanionStage.ADULT.name, EnumCompanionStage.FULLGROWN.name }, this.role.stage.ordinal()));
        this.addLabel(new GuiLabel(0, "companion.stage", this.guiLeft + 4, y + 5));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 162, y, 90, 20, "gui.update"));
        int var10005 = this.guiLeft + 70;
        y += 22;
        this.addButton(new GuiButtonNop(this, 2, var10005, y, 90, 20, new String[] { "gui.no", "gui.yes" }, this.role.canAge ? 1 : 0));
        this.addLabel(new GuiLabel(2, "companion.age", this.guiLeft + 4, y + 5));
        if (this.role.canAge) {
            this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 162, y, 140, 20, this.role.ticksActive + ""));
            this.getTextField(2).numbersOnly = true;
            this.getTextField(2).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
        }
        var10005 = this.guiLeft + 4;
        y += 26;
        this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.INVENTORY, var10005, y));
        this.addSlider(new GuiSliderNop(this, 10, this.guiLeft + 30, y + 2, 100, 20, (float) this.role.getExp(EnumCompanionTalent.INVENTORY) / 5000.0F));
        var10005 = this.guiLeft + 4;
        y += 26;
        this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.ARMOR, var10005, y));
        this.addSlider(new GuiSliderNop(this, 11, this.guiLeft + 30, y + 2, 100, 20, (float) this.role.getExp(EnumCompanionTalent.ARMOR) / 5000.0F));
        var10005 = this.guiLeft + 4;
        y += 26;
        this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.SWORD, var10005, y));
        this.addSlider(new GuiSliderNop(this, 12, this.guiLeft + 30, y + 2, 100, 20, (float) this.role.getExp(EnumCompanionTalent.SWORD) / 5000.0F));
        for (GuiNpcCompanionTalents.GuiTalent gui : this.talents) {
            gui.m_6575_(this.f_96541_, this.f_96543_, this.f_96544_);
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.role.matureTo(EnumCompanionStage.values()[guibutton.getValue()]);
            if (this.role.canAge) {
                this.role.ticksActive = (long) this.role.stage.matureAge;
            }
            this.init();
        }
        if (guibutton.id == 1) {
            Packets.sendServer(new SPacketNpcRoleCompanionUpdate(this.role.stage));
        }
        if (guibutton.id == 2) {
            this.role.canAge = guibutton.getValue() == 1;
            this.init();
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 2) {
            this.role.ticksActive = (long) textfield.getInteger();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        for (GuiNpcCompanionTalents.GuiTalent talent : new ArrayList(this.talents)) {
            talent.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void elementClicked() {
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketNpcRoleSave(this.role.save(new CompoundTag())));
    }

    @Override
    public void mouseDragged(GuiSliderNop slider) {
        if (slider.sliderValue <= 0.0F) {
            slider.m_93666_(Component.translatable("gui.disabled"));
            this.role.talents.remove(EnumCompanionTalent.values()[slider.id - 10]);
        } else {
            slider.m_93666_(Component.translatable((int) (slider.sliderValue * 50.0F) * 100 + " exp"));
            this.role.setExp(EnumCompanionTalent.values()[slider.id - 10], (int) (slider.sliderValue * 50.0F) * 100);
        }
    }

    @Override
    public void mousePressed(GuiSliderNop slider) {
    }

    @Override
    public void mouseReleased(GuiSliderNop slider) {
    }
}