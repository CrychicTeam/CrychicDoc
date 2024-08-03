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

public class CPacketOpenStorage extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenStorage> HANDLER = new CPacketOpenStorage.H();

    private final long traderID;

    public CPacketOpenStorage(long traderID) {
        this.traderID = traderID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
    }

    private static class H extends CustomPacket.Handler<CPacketOpenStorage> {

        @Nonnull
        public CPacketOpenStorage decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketOpenStorage(buffer.readLong());
        }

        protected void handle(@Nonnull CPacketOpenStorage message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                MenuValidator validator = SimpleValidator.NULL;
                if (sender.f_36096_ instanceof IValidatedMenu tm) {
                    validator = tm.getValidator();
                }
                TraderData trader = TraderSaveData.GetTrader(false, message.traderID);
                if (trader != null) {
                    trader.openStorageMenu(sender, validator);
                }
            }
        }
    }
}