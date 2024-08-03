package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketATMSetPlayerAccount extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketATMSetPlayerAccount> HANDLER = new CPacketATMSetPlayerAccount.H();

    private final String playerName;

    public CPacketATMSetPlayerAccount(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(this.playerName);
    }

    private static class H extends CustomPacket.Handler<CPacketATMSetPlayerAccount> {

        @Nonnull
        public CPacketATMSetPlayerAccount decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketATMSetPlayerAccount(buffer.readUtf());
        }

        protected void handle(@Nonnull CPacketATMSetPlayerAccount message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof ATMMenu menu) {
                MutableComponent response = menu.SetPlayerAccount(message.playerName);
                new SPacketATMPlayerAccountResponse(response).sendTo(sender);
            }
        }
    }
}