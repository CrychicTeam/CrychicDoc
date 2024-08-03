package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketToolMounter extends PacketServerBasic {

    private int type;

    private String name = "";

    private int tab = -1;

    private CompoundTag compound = new CompoundTag();

    private SPacketToolMounter(int type, String name, int tab, CompoundTag compound) {
        this.type = type;
        this.name = name;
        this.tab = tab;
        this.compound = compound;
    }

    public SPacketToolMounter(int type, String name, int tab) {
        this.type = type;
        this.name = name;
        this.tab = tab;
    }

    public SPacketToolMounter(int type, CompoundTag compound) {
        this.type = type;
        this.compound = compound;
    }

    public SPacketToolMounter() {
        this.type = 3;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.mount;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.TOOL_MOUNTER;
    }

    public static void encode(SPacketToolMounter msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.type);
        buf.writeUtf(msg.name);
        buf.writeInt(msg.tab);
        buf.writeNbt(msg.compound);
    }

    public static SPacketToolMounter decode(FriendlyByteBuf buf) {
        return new SPacketToolMounter(buf.readInt(), buf.readUtf(32767), buf.readInt(), buf.readNbt());
    }

    @Override
    protected void handle() {
        PlayerData data = PlayerData.get(this.player);
        if (data.mounted != null) {
            if (this.type == 0) {
                Entity entity = (Entity) EntityType.create(this.compound, this.player.m_9236_()).get();
                entity.setPos(data.mounted.getX(), data.mounted.getY(), data.mounted.getZ());
                this.player.m_9236_().m_7967_(entity);
                entity.startRiding(data.mounted, true);
            } else if (this.type == 1) {
                Entity entity = (Entity) EntityType.create(ServerCloneController.Instance.getCloneData(this.player.m_20203_(), this.name, this.tab), this.player.m_9236_()).get();
                entity.setPos(data.mounted.getX(), data.mounted.getY(), data.mounted.getZ());
                this.player.m_9236_().m_7967_(entity);
                entity.startRiding(data.mounted, true);
            } else if (this.type == 2) {
                ResourceLocation loc = (ResourceLocation) EntityUtil.getAllEntities(this.player.m_9236_(), false).get(this.name);
                EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(loc);
                Entity entity = type.create(this.player.m_9236_());
                if (entity == null) {
                    return;
                }
                entity.setPos(data.mounted.getX(), data.mounted.getY(), data.mounted.getZ());
                this.player.m_9236_().m_7967_(entity);
                entity.startRiding(data.mounted, true);
            } else {
                this.player.startRiding(data.mounted, true);
            }
        }
    }
}