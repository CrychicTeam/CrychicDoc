package net.minecraftforge.network;

import io.netty.buffer.Unpooled;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class PlayMessages {

    public static class OpenContainer {

        private final int id;

        private final int windowId;

        private final Component name;

        private final FriendlyByteBuf additionalData;

        OpenContainer(MenuType<?> id, int windowId, Component name, FriendlyByteBuf additionalData) {
            this(BuiltInRegistries.MENU.getId(id), windowId, name, additionalData);
        }

        private OpenContainer(int id, int windowId, Component name, FriendlyByteBuf additionalData) {
            this.id = id;
            this.windowId = windowId;
            this.name = name;
            this.additionalData = additionalData;
        }

        public static void encode(PlayMessages.OpenContainer msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.id);
            buf.writeVarInt(msg.windowId);
            buf.writeComponent(msg.name);
            buf.writeByteArray(msg.additionalData.readByteArray());
        }

        public static PlayMessages.OpenContainer decode(FriendlyByteBuf buf) {
            return new PlayMessages.OpenContainer(buf.readVarInt(), buf.readVarInt(), buf.readComponent(), new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray(32600))));
        }

        public static void handle(PlayMessages.OpenContainer msg, Supplier<NetworkEvent.Context> ctx) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                try {
                    MenuScreens.getScreenFactory(msg.getType(), Minecraft.getInstance(), msg.getWindowId(), msg.getName()).ifPresent(f -> {
                        AbstractContainerMenu c = msg.getType().create(msg.getWindowId(), Minecraft.getInstance().player.m_150109_(), msg.getAdditionalData());
                        Screen s = f.create(c, Minecraft.getInstance().player.m_150109_(), msg.getName());
                        Minecraft.getInstance().player.f_36096_ = ((MenuAccess) s).getMenu();
                        Minecraft.getInstance().setScreen(s);
                    });
                } finally {
                    msg.getAdditionalData().release();
                }
            });
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        }

        public final MenuType<?> getType() {
            return (MenuType<?>) BuiltInRegistries.MENU.m_7942_(this.id);
        }

        public int getWindowId() {
            return this.windowId;
        }

        public Component getName() {
            return this.name;
        }

        public FriendlyByteBuf getAdditionalData() {
            return this.additionalData;
        }
    }

    public static class SpawnEntity {

        private final Entity entity;

        private final int typeId;

        private final int entityId;

        private final UUID uuid;

        private final double posX;

        private final double posY;

        private final double posZ;

        private final byte pitch;

        private final byte yaw;

        private final byte headYaw;

        private final int velX;

        private final int velY;

        private final int velZ;

        private final FriendlyByteBuf buf;

        SpawnEntity(Entity e) {
            this.entity = e;
            this.typeId = BuiltInRegistries.ENTITY_TYPE.m_7447_(e.getType());
            this.entityId = e.getId();
            this.uuid = e.getUUID();
            this.posX = e.getX();
            this.posY = e.getY();
            this.posZ = e.getZ();
            this.pitch = (byte) Mth.floor(e.getXRot() * 256.0F / 360.0F);
            this.yaw = (byte) Mth.floor(e.getYRot() * 256.0F / 360.0F);
            this.headYaw = (byte) ((int) (e.getYHeadRot() * 256.0F / 360.0F));
            Vec3 vec3d = e.getDeltaMovement();
            double d1 = Mth.clamp(vec3d.x, -3.9, 3.9);
            double d2 = Mth.clamp(vec3d.y, -3.9, 3.9);
            double d3 = Mth.clamp(vec3d.z, -3.9, 3.9);
            this.velX = (int) (d1 * 8000.0);
            this.velY = (int) (d2 * 8000.0);
            this.velZ = (int) (d3 * 8000.0);
            this.buf = null;
        }

        private SpawnEntity(int typeId, int entityId, UUID uuid, double posX, double posY, double posZ, byte pitch, byte yaw, byte headYaw, int velX, int velY, int velZ, FriendlyByteBuf buf) {
            this.entity = null;
            this.typeId = typeId;
            this.entityId = entityId;
            this.uuid = uuid;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.pitch = pitch;
            this.yaw = yaw;
            this.headYaw = headYaw;
            this.velX = velX;
            this.velY = velY;
            this.velZ = velZ;
            this.buf = buf;
        }

        public static void encode(PlayMessages.SpawnEntity msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.typeId);
            buf.writeInt(msg.entityId);
            buf.writeLong(msg.uuid.getMostSignificantBits());
            buf.writeLong(msg.uuid.getLeastSignificantBits());
            buf.writeDouble(msg.posX);
            buf.writeDouble(msg.posY);
            buf.writeDouble(msg.posZ);
            buf.writeByte(msg.pitch);
            buf.writeByte(msg.yaw);
            buf.writeByte(msg.headYaw);
            buf.writeShort(msg.velX);
            buf.writeShort(msg.velY);
            buf.writeShort(msg.velZ);
            if (msg.entity instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData) {
                FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());
                entityAdditionalSpawnData.writeSpawnData(spawnDataBuffer);
                buf.writeVarInt(spawnDataBuffer.readableBytes());
                buf.writeBytes(spawnDataBuffer);
                spawnDataBuffer.release();
            } else {
                buf.writeVarInt(0);
            }
        }

        public static PlayMessages.SpawnEntity decode(FriendlyByteBuf buf) {
            return new PlayMessages.SpawnEntity(buf.readVarInt(), buf.readInt(), new UUID(buf.readLong(), buf.readLong()), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readShort(), buf.readShort(), buf.readShort(), readSpawnDataPacket(buf));
        }

        private static FriendlyByteBuf readSpawnDataPacket(FriendlyByteBuf buf) {
            int count = buf.readVarInt();
            if (count > 0) {
                FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());
                spawnDataBuffer.writeBytes(buf, count);
                return spawnDataBuffer;
            } else {
                return new FriendlyByteBuf(Unpooled.buffer());
            }
        }

        public static void handle(PlayMessages.SpawnEntity msg, Supplier<NetworkEvent.Context> ctx) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                try {
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.byId(msg.typeId);
                    Optional<Level> world = LogicalSidedProvider.CLIENTWORLD.get(((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide());
                    Entity e = (Entity) world.map(w -> type.customClientSpawn(msg, w)).orElse(null);
                    if (e != null) {
                        e.syncPacketPositionCodec(msg.posX, msg.posY, msg.posZ);
                        e.absMoveTo(msg.posX, msg.posY, msg.posZ, (float) (msg.yaw * 360) / 256.0F, (float) (msg.pitch * 360) / 256.0F);
                        e.setYHeadRot((float) (msg.headYaw * 360) / 256.0F);
                        e.setYBodyRot((float) (msg.headYaw * 360) / 256.0F);
                        e.setId(msg.entityId);
                        e.setUUID(msg.uuid);
                        world.filter(ClientLevel.class::isInstance).ifPresent(w -> ((ClientLevel) w).putNonPlayerEntity(msg.entityId, e));
                        e.lerpMotion((double) msg.velX / 8000.0, (double) msg.velY / 8000.0, (double) msg.velZ / 8000.0);
                        if (e instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData) {
                            entityAdditionalSpawnData.readSpawnData(msg.buf);
                        }
                        return;
                    }
                } finally {
                    msg.buf.release();
                }
            });
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        }

        public Entity getEntity() {
            return this.entity;
        }

        public int getTypeId() {
            return this.typeId;
        }

        public int getEntityId() {
            return this.entityId;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public double getPosX() {
            return this.posX;
        }

        public double getPosY() {
            return this.posY;
        }

        public double getPosZ() {
            return this.posZ;
        }

        public byte getPitch() {
            return this.pitch;
        }

        public byte getYaw() {
            return this.yaw;
        }

        public byte getHeadYaw() {
            return this.headYaw;
        }

        public int getVelX() {
            return this.velX;
        }

        public int getVelY() {
            return this.velY;
        }

        public int getVelZ() {
            return this.velZ;
        }

        public FriendlyByteBuf getAdditionalData() {
            return this.buf;
        }
    }
}