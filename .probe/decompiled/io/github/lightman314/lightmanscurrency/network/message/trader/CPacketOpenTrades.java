package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.SimpleValidator;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketOpenTrades extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenTrades> HANDLER = new CPacketOpenTrades.H();

    private final long traderID;

    public CPacketOpenTrades(long traderID) {
        this.traderID = traderID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
    }

    private static class H extends CustomPacket.Handler<CPacketOpenTrades> {

        @Nonnull
        public CPacketOpenTrades decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketOpenTrades(buffer.readLong());
        }

        protected void handle(@Nonnull CPacketOpenTrades message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                MenuValidator validator = SimpleValidator.NULL;
                if (sender.f_36096_ instanceof IValidatedMenu tm) {
                    validator = tm.getValidator();
                }
                TraderData data = TraderSaveData.GetTrader(false, message.traderID);
                if (data != null) {
                    data.openTraderMenu(sender, validator);
                }
            }
        }
    }
}