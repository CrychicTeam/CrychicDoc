package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobPuppet;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiSliderNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.ISliderListener;

public class GuiNpcPuppet extends GuiNPCInterface implements ISliderListener, ICustomScrollListener {

    private Screen parent;

    private JobPuppet job;

    private String selectedName;

    private boolean isStart = true;

    public HashMap<String, JobPuppet.PartConfig> data = new HashMap();

    private GuiCustomScrollNop scroll;

    public GuiNpcPuppet(Screen parent, EntityCustomNpc npc) {
        super(npc);
        this.parent = parent;
        this.imageHeight = 230;
        this.imageWidth = 400;
        this.job = (JobPuppet) npc.job;
    }

    @Override
    public void init() {
        super.m_7856_();
        int y = this.guiTop;
        int var10005 = this.guiLeft + 110;
        y += 14;
        this.addButton(new GuiButtonNop(this, 30, var10005, y, 60, 20, new String[] { "gui.yes", "gui.no" }, this.job.whileStanding ? 0 : 1));
        this.addLabel(new GuiLabel(30, "puppet.standing", this.guiLeft + 10, y + 5, 16777215));
        var10005 = this.guiLeft + 110;
        y += 22;
        this.addButton(new GuiButtonNop(this, 31, var10005, y, 60, 20, new String[] { "gui.yes", "gui.no" }, this.job.whileMoving ? 0 : 1));
        this.addLabel(new GuiLabel(31, "puppet.walking", this.guiLeft + 10, y + 5, 16777215));
        var10005 = this.guiLeft + 110;
        y += 22;
        this.addButton(new GuiButtonNop(this, 32, var10005, y, 60, 20, new String[] { "gui.yes", "gui.no" }, this.job.whileAttacking ? 0 : 1));
        this.addLabel(new GuiLabel(32, "puppet.attacking", this.guiLeft + 10, y + 5, 16777215));
        var10005 = this.guiLeft + 110;
        y += 22;
        this.addButton(new GuiButtonNop(this, 33, var10005, y, 60, 20, new String[] { "gui.yes", "gui.no" }, this.job.animate ? 0 : 1));
        this.addLabel(new GuiLabel(33, "puppet.animation", this.guiLeft + 10, y + 5, 16777215));
        if (this.job.animate) {
            this.addButton(new GuiButtonBiDirectional(this, 34, this.guiLeft + 240, y, 60, 20, new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }, this.job.animationSpeed));
            this.addLabel(new GuiLabel(34, "stats.speed", this.guiLeft + 190, y + 5, 16777215));
        }
        y += 34;
        HashMap<String, JobPuppet.PartConfig> data = new HashMap();
        if (this.isStart) {
            data.put("model.head", this.job.head);
            data.put("model.body", this.job.body);
            data.put("model.larm", this.job.larm);
            data.put("model.rarm", this.job.rarm);
            data.put("model.lleg", this.job.lleg);
            data.put("model.rleg", this.job.rleg);
        } else {
            data.put("model.head", this.job.head2);
            data.put("model.body", this.job.body2);
            data.put("model.larm", this.job.larm2);
            data.put("model.rarm", this.job.rarm2);
            data.put("model.lleg", this.job.lleg2);
            data.put("model.rleg", this.job.rleg2);
        }
        this.data = data;
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
        }
        this.scroll.setList(new ArrayList(data.keySet()));
        this.scroll.guiLeft = this.guiLeft + 10;
        this.scroll.guiTop = y;
        this.scroll.setSize(80, 100);
        this.addScroll(this.scroll);
        if (this.selectedName != null) {
            this.scroll.setSelected(this.selectedName);
            this.drawSlider(y, (JobPuppet.PartConfig) data.get(this.selectedName));
        }
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + this.imageWidth - 22, this.guiTop, 20, 20, "X"));
        if (this.job.animate) {
            this.addButton(new GuiButtonNop(this, 67, this.guiLeft + 10, y + 110, 70, 20, "gui.start"));
            this.addButton(new GuiButtonNop(this, 68, this.guiLeft + 90, y + 110, 70, 20, "gui.end"));
            this.getButton(67).f_93623_ = !this.isStart;
            this.getButton(68).f_93623_ = this.isStart;
        }
    }

    private void drawSlider(int y, JobPuppet.PartConfig config) {
        this.addButton(new GuiButtonNop(this, 29, this.guiLeft + 140, y, 80, 20, new String[] { "gui.enabled", "gui.disabled" }, config.disabled ? 1 : 0));
        y += 22;
        this.addLabel(new GuiLabel(10, "X", this.guiLeft + 100, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 10, this.guiLeft + 120, y, (config.rotationX + 1.0F) / 2.0F));
        y += 22;
        this.addLabel(new GuiLabel(11, "Y", this.guiLeft + 100, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 11, this.guiLeft + 120, y, (config.rotationY + 1.0F) / 2.0F));
        y += 22;
        this.addLabel(new GuiLabel(12, "Z", this.guiLeft + 100, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 12, this.guiLeft + 120, y, (config.rotationZ + 1.0F) / 2.0F));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.drawNpc(graphics, this.npc, 320, 200, 3.0F, 0);
    }

    @Override
    public void buttonEvent(GuiButtonNop btn) {
        if (btn instanceof GuiButtonNop) {
            if (btn.id == 29) {
                ((JobPuppet.PartConfig) this.data.get(this.selectedName)).disabled = btn.getValue() == 1;
            }
            if (btn.id == 30) {
                this.job.whileStanding = btn.getValue() == 0;
            }
            if (btn.id == 31) {
                this.job.whileMoving = btn.getValue() == 0;
            }
            if (btn.id == 32) {
                this.job.whileAttacking = btn.getValue() == 0;
            }
            if (btn.id == 33) {
                this.job.animate = btn.getValue() == 0;
                this.isStart = true;
                this.init();
            }
            if (btn.id == 34) {
                this.job.animationSpeed = btn.getValue();
            }
            if (btn.id == 66) {
                this.close();
            }
            if (btn.id == 67) {
                this.isStart = true;
                this.init();
            }
            if (btn.id == 68) {
                this.isStart = false;
                this.init();
            }
        }
    }

    @Override
    public void close() {
        this.f_96541_.setScreen(this.parent);
        Packets.sendServer(new SPacketNpcJobSave(this.job.save(new CompoundTag())));
    }

    @Override
    public void mouseDragged(GuiSliderNop slider) {
        int percent = (int) (slider.sliderValue * 360.0F);
        slider.setString(percent + "%");
        JobPuppet.PartConfig part = (JobPuppet.PartConfig) this.data.get(this.selectedName);
        if (slider.id == 10) {
            part.rotationX = (slider.sliderValue - 0.5F) * 2.0F;
        }
        if (slider.id == 11) {
            part.rotationY = (slider.sliderValue - 0.5F) * 2.0F;
        }
        if (slider.id == 12) {
            part.rotationZ = (slider.sliderValue - 0.5F) * 2.0F;
        }
        this.npc.m_6210_();
    }

    @Override
    public void mousePressed(GuiSliderNop slider) {
    }

    @Override
    public void mouseReleased(GuiSliderNop slider) {
    }

    @Override
    public void save() {
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        this.selectedName = guiCustomScroll.getSelected();
        this.init();
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}