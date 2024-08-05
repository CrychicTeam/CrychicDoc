package noppes.npcs.packets.server;

import java.util.ArrayList;
import java.util.Vector;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerTransportData;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiOpen;
import noppes.npcs.packets.client.PacketGuiScrollList;
import noppes.npcs.packets.client.PacketNpcRole;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.util.CustomNPCsScheduler;

public class SPacketGuiOpen extends PacketServerBasic {

    private EnumGuiType type;

    private BlockPos pos;

    public SPacketGuiOpen(EnumGuiType type, BlockPos pos) {
        this.type = type;
        this.pos = pos;
    }

    public static void encode(SPacketGuiOpen msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeBlockPos(msg.pos);
    }

    public static SPacketGuiOpen decode(FriendlyByteBuf buf) {
        return new SPacketGuiOpen(buf.readEnum(EnumGuiType.class), buf.readBlockPos());
    }

    @Override
    protected void handle() {
        sendOpenGui(this.player, this.type, this.npc, this.pos);
    }

    public static void sendOpenGui(Player player, EnumGuiType gui, EntityNPCInterface npc, BlockPos pos) {
        if (player instanceof ServerPlayer) {
            NoppesUtilServer.setEditingNpc(player, npc);
            if (gui == EnumGuiType.PlayerFollower || gui == EnumGuiType.PlayerFollowerHire || gui == EnumGuiType.PlayerTrader || gui == EnumGuiType.PlayerTransporter) {
                if (npc.role.getType() == 0) {
                    return;
                }
                CompoundTag comp = new CompoundTag();
                npc.role.save(comp);
                comp.putInt("Role", npc.role.getType());
                Packets.send((ServerPlayer) player, new PacketNpcRole(npc.m_19879_(), comp));
            }
            CustomNPCsScheduler.runTack(() -> player.m_20194_().m_18707_(() -> {
                if (!gui.hasContainer) {
                    Packets.send((ServerPlayer) player, new PacketGuiOpen(gui, pos));
                } else {
                    NoppesUtilServer.openContainerGui((ServerPlayer) player, gui, buffer -> buffer.writeInt(npc.m_19879_()));
                }
                ArrayList<String> list = getScrollData(player, gui, npc);
                if (list != null && !list.isEmpty()) {
                    Packets.send((ServerPlayer) player, new PacketGuiScrollList(new Vector(list)));
                }
            }), 200);
        }
    }

    private static ArrayList<String> getScrollData(Player player, EnumGuiType gui, EntityNPCInterface npc) {
        if (gui == EnumGuiType.PlayerTransporter) {
            RoleTransporter role = (RoleTransporter) npc.role;
            ArrayList<String> list = new ArrayList();
            TransportLocation location = role.getLocation();
            String name = role.getLocation().name;
            for (TransportLocation loc : location.category.getDefaultLocations()) {
                if (!list.contains(loc.name)) {
                    list.add(loc.name);
                }
            }
            PlayerTransportData playerdata = PlayerData.get(player).transportData;
            for (int i : playerdata.transports) {
                TransportLocation locx = TransportController.getInstance().getTransport(i);
                if (locx != null && location.category.locations.containsKey(locx.id) && !list.contains(locx.name)) {
                    list.add(locx.name);
                }
            }
            list.remove(name);
            return list;
        } else {
            return null;
        }
    }
}