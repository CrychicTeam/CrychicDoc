package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountMenu;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CPacketBankInteraction extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketBankInteraction> HANDLER = new CPacketBankInteraction.H();

    boolean isDeposit;

    MoneyValue amount;

    public CPacketBankInteraction(boolean isDeposit, MoneyValue amount) {
        this.isDeposit = isDeposit;
        this.amount = amount;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.isDeposit);
        this.amount.encode(buffer);
    }

    private static final class H extends CustomPacket.Handler<CPacketBankInteraction> {

        @Nonnull
        public CPacketBankInteraction decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketBankInteraction(buffer.readBoolean(), MoneyValue.decode(buffer));
        }

        protected void handle(@Nonnull CPacketBankInteraction message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof IBankAccountMenu menu) {
                if (message.isDeposit) {
                    BankAPI.API.BankDeposit(menu, message.amount);
                } else {
                    BankAPI.API.BankWithdraw(menu, message.amount);
                }
                menu.onDepositOrWithdraw();
            }
        }
    }
}