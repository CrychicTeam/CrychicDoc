package dev.xkmc.modulargolems.content.menu.registry;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenuPvd;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class OpenEquipmentMenuToServer extends SerialPacketBase {

    @SerialField
    public OpenEquipmentMenuToServer.Type type;

    @SerialField
    public UUID uuid;

    @Deprecated
    public OpenEquipmentMenuToServer() {
    }

    public OpenEquipmentMenuToServer(UUID uuid, OpenEquipmentMenuToServer.Type type) {
        this.uuid = uuid;
        this.type = type;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            if (player.serverLevel().getEntity(this.uuid) instanceof AbstractGolemEntity<?, ?> golem) {
                if (golem.canModify(player)) {
                    IMenuPvd pvd = this.type.construct(golem);
                    if (pvd != null) {
                        CuriosEventHandler.openMenuWrapped(player, () -> NetworkHooks.openScreen(player, pvd, pvd::writeBuffer));
                    }
                }
            }
        }
    }

    public static enum Type {

        EQUIPMENT(EquipmentsMenuPvd::new), CURIOS(CurioCompatRegistry::create);

        private final Function<AbstractGolemEntity<?, ?>, IMenuPvd> func;

        private Type(Function<AbstractGolemEntity<?, ?>, IMenuPvd> func) {
            this.func = func;
        }

        @Nullable
        public IMenuPvd construct(AbstractGolemEntity<?, ?> entry) {
            return (IMenuPvd) this.func.apply(entry);
        }
    }
}