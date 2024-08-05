package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomTeleporter;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketDimensionTeleport extends PacketServerBasic {

    private ResourceLocation id;

    public SPacketDimensionTeleport(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.teleporter;
    }

    public static void encode(SPacketDimensionTeleport msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.id);
    }

    public static SPacketDimensionTeleport decode(FriendlyByteBuf buf) {
        return new SPacketDimensionTeleport(buf.readResourceLocation());
    }

    @Override
    protected void handle() {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, this.id);
        ServerLevel level = this.player.m_20194_().getLevel(dimension);
        BlockPos coords = level.m_220360_();
        if (coords == null) {
            coords = level.m_220360_();
            if (!level.m_46859_(coords)) {
                coords = level.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, coords);
            } else {
                while (level.m_46859_(coords) && coords.m_123342_() > 0) {
                    coords = coords.below();
                }
                if (coords.m_123342_() == 0) {
                    coords = level.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, coords);
                }
            }
        }
        teleportPlayer(this.player, (double) coords.m_123341_(), (double) coords.m_123342_(), (double) coords.m_123343_(), dimension);
    }

    public static void teleportPlayer(ServerPlayer player, double x, double y, double z, ResourceKey<Level> dimension) {
        if (player.m_9236_().dimension() != dimension) {
            MinecraftServer server = player.m_20194_();
            ServerLevel wor = server.getLevel(dimension);
            if (wor == null) {
                player.sendSystemMessage(Component.literal("Broken transporter. Dimenion does not exist"));
                return;
            }
            player.m_7678_(x, y, z, player.m_146908_(), player.m_146909_());
            player.changeDimension(wor, new CustomTeleporter(wor, new Vec3(x, y, z), player.m_146908_(), player.m_146909_()));
        } else {
            player.connection.teleport(x, y, z, player.m_146908_(), player.m_146909_());
        }
    }
}