package yesman.epicfight.network.server;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SPModifyPlayerData {

    SPModifyPlayerData.PacketType packetType;

    private final int entityId;

    private final Map<String, Object> data;

    public SPModifyPlayerData() {
        this.packetType = null;
        this.entityId = 0;
        this.data = Maps.newHashMap();
    }

    public SPModifyPlayerData(int entityId, float yaw) {
        this(SPModifyPlayerData.PacketType.YAW_CORRECTION, entityId);
        this.addData("yaw", yaw);
    }

    public SPModifyPlayerData(int entityId, boolean lastAttackSuccess) {
        this(SPModifyPlayerData.PacketType.LAST_ATTACK_RESULT, entityId);
        this.addData("lastAttackSuccess", lastAttackSuccess);
    }

    public SPModifyPlayerData(int entityId, PlayerPatch.PlayerMode mode) {
        this(SPModifyPlayerData.PacketType.MODE, entityId);
        this.addData("mode", mode);
    }

    public SPModifyPlayerData(int entityId, Entity grapplingTarget) {
        this(SPModifyPlayerData.PacketType.SET_GRAPPLE_TARGET, entityId);
        this.addData("grapplingTarget", grapplingTarget == null ? -1 : grapplingTarget.getId());
    }

    public SPModifyPlayerData(SPModifyPlayerData.PacketType packetType, int entityId) {
        this.packetType = packetType;
        this.entityId = entityId;
        this.data = Maps.newHashMap();
    }

    public SPModifyPlayerData addData(String key, Object val) {
        this.data.put(key, val);
        return this;
    }

    public static SPModifyPlayerData fromBytes(FriendlyByteBuf buf) {
        SPModifyPlayerData.PacketType packetType = SPModifyPlayerData.PacketType.values()[buf.readInt()];
        SPModifyPlayerData packet = new SPModifyPlayerData(packetType, buf.readInt());
        packetType.decoder.accept(packet, buf);
        return packet;
    }

    public static void toBytes(SPModifyPlayerData msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.packetType.ordinal());
        buf.writeInt(msg.entityId);
        msg.packetType.encoder.accept(msg, buf);
    }

    public static void handle(SPModifyPlayerData msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.player.m_9236_().getEntity(msg.entityId);
            if (entity != null && entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null) instanceof PlayerPatch<?> playerpatch) {
                switch(msg.packetType) {
                    case YAW_CORRECTION:
                        playerpatch.changeModelYRot((Float) msg.data.get("yaw"));
                        break;
                    case MODE:
                        playerpatch.toMode((PlayerPatch.PlayerMode) msg.data.get("mode"), false);
                        break;
                    case LAST_ATTACK_RESULT:
                        playerpatch.setLastAttackSuccess((Boolean) msg.data.get("lastAttackSuccess"));
                        break;
                    case SET_GRAPPLE_TARGET:
                        Entity grapplingTarget = mc.player.m_9236_().getEntity((Integer) msg.data.get("grapplingTarget"));
                        if (grapplingTarget instanceof LivingEntity) {
                            playerpatch.setGrapplingTarget((LivingEntity) grapplingTarget);
                        } else {
                            playerpatch.setGrapplingTarget(null);
                        }
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum PacketType {

        YAW_CORRECTION((packet, buffer) -> buffer.writeFloat((Float) packet.data.get("yaw")), (packet, buffer) -> packet.addData("yaw", buffer.readFloat())), MODE((packet, buffer) -> buffer.writeInt(((PlayerPatch.PlayerMode) packet.data.get("mode")).ordinal()), (packet, buffer) -> packet.addData("mode", PlayerPatch.PlayerMode.values()[buffer.readInt()])), LAST_ATTACK_RESULT((packet, buffer) -> buffer.writeBoolean((Boolean) packet.data.get("lastAttackSuccess")), (packet, buffer) -> packet.addData("lastAttackSuccess", buffer.readBoolean())), SET_GRAPPLE_TARGET((packet, buffer) -> buffer.writeInt((Integer) packet.data.get("grapplingTarget")), (packet, buffer) -> packet.addData("grapplingTarget", buffer.readInt()));

        BiConsumer<SPModifyPlayerData, FriendlyByteBuf> encoder;

        BiConsumer<SPModifyPlayerData, FriendlyByteBuf> decoder;

        private PacketType(BiConsumer<SPModifyPlayerData, FriendlyByteBuf> encoder, BiConsumer<SPModifyPlayerData, FriendlyByteBuf> decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }
    }
}