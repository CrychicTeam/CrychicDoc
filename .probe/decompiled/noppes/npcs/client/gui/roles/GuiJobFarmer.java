package noppes.npcs.client.gui.roles;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobFarmer;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class GuiJobFarmer extends GuiNPCInterface2 {

    private JobFarmer job;

    public GuiJobFarmer(EntityNPCInterface npc) {
        super(npc);
        this.job = (JobFarmer) npc.job;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "farmer.itempicked", this.guiLeft + 10, this.guiTop + 20));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 100, this.guiTop + 15, 160, 20, new String[] { "farmer.donothing", "farmer.chest", "farmer.drop" }, this.job.chestMode));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.job.chestMode = guibutton.getValue();
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketNpcJobSave(this.job.save(new CompoundTag())));
    }
}