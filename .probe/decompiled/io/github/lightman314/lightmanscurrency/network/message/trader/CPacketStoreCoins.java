package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketStoreCoins extends ClientToServerPacket.Simple {

    private static final CPacketStoreCoins INSTANCE = new CPacketStoreCoins();

    public static final CustomPacket.Handler<CPacketStoreCoins> HANDLER = new CPacketStoreCoins.H();

    private CPacketStoreCoins() {
    }

    public static void sendToServer() {
        INSTANCE.send();
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketStoreCoins> {

        protected H() {
            super(CPacketStoreCoins.INSTANCE);
        }

        protected void handle(@Nonnull CPacketStoreCoins message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof TraderStorageMenu menu) {
                menu.AddCoins();
            }
        }
    }
}