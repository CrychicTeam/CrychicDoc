package dev.xkmc.modulargolems.content.menu.registry;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.menu.config.ConfigMenuProvider;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigMenuProvider;
import dev.xkmc.modulargolems.content.menu.path.PathConfigMenuProvider;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigMenuProvider;
import java.util.UUID;
import java.util.function.BiFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

@SerialClass
public class OpenConfigMenuToServer extends SerialPacketBase {

    @SerialField
    public OpenConfigMenuToServer.Type type;

    @SerialField
    public UUID uuid;

    @SerialField
    public int color;

    @Deprecated
    public OpenConfigMenuToServer() {
    }

    public OpenConfigMenuToServer(UUID uuid, int color, OpenConfigMenuToServer.Type type) {
        this.uuid = uuid;
        this.color = color;
        this.type = type;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            GolemConfigEntry entry = GolemConfigStorage.get(player.m_9236_()).getStorage(this.uuid, this.color);
            if (entry != null) {
                if (player.m_20148_().equals(this.uuid)) {
                    IMenuPvd pvd = this.type.construct(player.serverLevel(), entry);
                    NetworkHooks.openScreen(player, pvd, pvd::writeBuffer);
                }
            }
        }
    }

    public static enum Type {

        TOGGLE(ConfigMenuProvider::fromPacket), ITEM(ItemConfigMenuProvider::fromPacket), TARGET(TargetConfigMenuProvider::fromPacket), PATH(PathConfigMenuProvider::fromPacket);

        private final BiFunction<ServerLevel, GolemConfigEntry, IMenuPvd> func;

        private Type(BiFunction<ServerLevel, GolemConfigEntry, IMenuPvd> func) {
            this.func = func;
        }

        public IMenuPvd construct(ServerLevel level, GolemConfigEntry entry) {
            return (IMenuPvd) this.func.apply(level, entry);
        }
    }
}