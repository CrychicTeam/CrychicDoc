package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.common.bank.BankSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CPacketSelectBankAccount extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketSelectBankAccount> HANDLER = new CPacketSelectBankAccount.H();

    final BankReference account;

    public CPacketSelectBankAccount(BankReference account) {
        this.account = account;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        this.account.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<CPacketSelectBankAccount> {

        @Nonnull
        public CPacketSelectBankAccount decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketSelectBankAccount(BankReference.decode(buffer));
        }

        protected void handle(@Nonnull CPacketSelectBankAccount message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                BankSaveData.SetSelectedBankAccount(sender, message.account);
            }
        }
    }
}