package noppes.npcs.roles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.data.role.IRoleTransporter;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerTransportData;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketChatBubble;
import noppes.npcs.packets.server.SPacketDimensionTeleport;

public class RoleTransporter extends RoleInterface implements IRoleTransporter {

    public int transportId = -1;

    public String name;

    private int ticks = 10;

    public RoleTransporter(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("TransporterId", this.transportId);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.transportId = nbttagcompound.getInt("TransporterId");
        TransportLocation loc = this.getLocation();
        if (loc != null) {
            this.name = loc.name;
        }
    }

    @Override
    public boolean aiShouldExecute() {
        this.ticks--;
        if (this.ticks > 0) {
            return false;
        } else {
            this.ticks = 10;
            if (!this.hasTransport()) {
                return false;
            } else {
                TransportLocation loc = this.getLocation();
                if (loc.type != 0) {
                    return false;
                } else {
                    for (Player player : this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(6.0, 6.0, 6.0))) {
                        if (this.npc.canNpcSee(player)) {
                            this.unlock(player, loc);
                        }
                    }
                    return false;
                }
            }
        }
    }

    @Override
    public void interact(Player player) {
        if (this.hasTransport()) {
            TransportLocation loc = this.getLocation();
            if (loc.type == 2) {
                this.unlock(player, loc);
            }
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTransporter, this.npc);
        }
    }

    public void transport(ServerPlayer player, String location) {
        TransportLocation loc = TransportController.getInstance().getTransport(location);
        PlayerTransportData playerdata = PlayerData.get(player).transportData;
        if (loc != null && (loc.isDefault() || playerdata.transports.contains(loc.id))) {
            RoleEvent.TransporterUseEvent event = new RoleEvent.TransporterUseEvent(player, this.npc.wrappedNPC, loc);
            if (!EventHooks.onNPCRole(this.npc, event)) {
                SPacketDimensionTeleport.teleportPlayer(player, (double) loc.pos.m_123341_(), (double) loc.pos.m_123342_(), (double) loc.pos.m_123343_(), loc.dimension);
            }
        }
    }

    private void unlock(Player player, TransportLocation loc) {
        PlayerTransportData data = PlayerData.get(player).transportData;
        if (!data.transports.contains(this.transportId)) {
            RoleEvent.TransporterUnlockedEvent event = new RoleEvent.TransporterUnlockedEvent(player, this.npc.wrappedNPC);
            if (!EventHooks.onNPCRole(this.npc, event)) {
                data.transports.add(this.transportId);
                this.npc.say(player, new Line("mailbox.gotmail"));
                Packets.send((ServerPlayer) player, new PacketChatBubble(this.npc.m_19879_(), Component.translatable("transporter.unlock", loc.name), true));
            }
        }
    }

    public TransportLocation getLocation() {
        return this.npc.isClientSide() ? null : TransportController.getInstance().getTransport(this.transportId);
    }

    public boolean hasTransport() {
        TransportLocation loc = this.getLocation();
        return loc != null && loc.id == this.transportId;
    }

    public void setTransport(TransportLocation location) {
        this.transportId = location.id;
        this.name = location.name;
    }

    @Override
    public int getType() {
        return 4;
    }
}