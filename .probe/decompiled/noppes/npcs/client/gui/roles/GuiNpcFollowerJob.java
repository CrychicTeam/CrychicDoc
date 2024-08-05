package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiNpcFollowerJob extends GuiNPCInterface2 implements ICustomScrollListener {

    private JobFollower job;

    private GuiCustomScrollNop scroll;

    public GuiNpcFollowerJob(EntityNPCInterface npc) {
        super(npc);
        this.job = (JobFollower) npc.job;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "gui.name", this.guiLeft + 6, this.guiTop + 110));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 50, this.guiTop + 105, 200, 20, this.job.name));
        this.scroll = new GuiCustomScrollNop(this, 0);
        this.scroll.setSize(143, 208);
        this.scroll.guiLeft = this.guiLeft + 268;
        this.scroll.guiTop = this.guiTop + 4;
        this.addScroll(this.scroll);
        List<String> names = new ArrayList();
        for (EntityNPCInterface npc : this.npc.m_9236_().m_45976_(EntityNPCInterface.class, this.npc.m_20191_().inflate(40.0, 40.0, 40.0))) {
            if (npc != this.npc && !names.contains(npc.display.getName())) {
                names.add(npc.display.getName());
            }
        }
        this.scroll.setList(names);
    }

    @Override
    public void save() {
        this.job.name = this.getTextField(1).m_94155_();
        Packets.sendServer(new SPacketNpcJobSave(this.job.save(new CompoundTag())));
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        this.getTextField(1).m_94144_(guiCustomScroll.getSelected());
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}