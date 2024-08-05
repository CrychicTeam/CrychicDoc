package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.JobSpawner;

public class SPacketNpcJobSpawnerSet extends PacketServerBasic {

    private final int tab;

    private final String name;

    private final int slot;

    public SPacketNpcJobSpawnerSet(int tab, String name, int slot) {
        this.tab = tab;
        this.name = name;
        this.slot = slot;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcJobSpawnerSet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.tab);
        buf.writeUtf(msg.name);
        buf.writeInt(msg.slot);
    }

    public static SPacketNpcJobSpawnerSet decode(FriendlyByteBuf buf) {
        return new SPacketNpcJobSpawnerSet(buf.readInt(), buf.readUtf(32767), buf.readInt());
    }

    @Override
    protected void handle() {
        if (this.npc.job.getType() == 6) {
            JobSpawner job = (JobSpawner) this.npc.job;
            job.setJobCompound(this.slot, this.tab, this.name);
        }
    }
}