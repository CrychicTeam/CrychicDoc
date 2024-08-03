package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.api.traders.menu.IMoneyCollectionMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketCollectCoins extends ClientToServerPacket.Simple {

    private static final CPacketCollectCoins INSTANCE = new CPacketCollectCoins();

    public static final CustomPacket.Handler<CPacketCollectCoins> HANDLER = new CPacketCollectCoins.H();

    private CPacketCollectCoins() {
    }

    public static void sendToServer() {
        INSTANCE.send();
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketCollectCoins> {

        protected H() {
            super(CPacketCollectCoins.INSTANCE);
        }

        protected void handle(@Nonnull CPacketCollectCoins message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof IMoneyCollectionMenu menu) {
                menu.CollectStoredMoney();
            }
        }
    }
}