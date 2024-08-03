package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.common.menus.providers.TerminalMenuProvider;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.SimpleValidator;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketOpenNetworkTerminal extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenNetworkTerminal> HANDLER = new CPacketOpenNetworkTerminal.H();

    private final boolean ignoreExistingValidation;

    public CPacketOpenNetworkTerminal() {
        this(false);
    }

    public CPacketOpenNetworkTerminal(boolean ignoreExistingValidation) {
        this.ignoreExistingValidation = ignoreExistingValidation;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.ignoreExistingValidation);
    }

    private static class H extends CustomPacket.Handler<CPacketOpenNetworkTerminal> {

        @Nonnull
        public CPacketOpenNetworkTerminal decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketOpenNetworkTerminal(buffer.readBoolean());
        }

        protected void handle(@Nonnull CPacketOpenNetworkTerminal message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                MenuValidator validator = SimpleValidator.NULL;
                if (sender.f_36096_ instanceof IValidatedMenu menu && !message.ignoreExistingValidation) {
                    validator = menu.getValidator();
                }
                TerminalMenuProvider.OpenMenu(sender, validator);
            }
        }
    }
}