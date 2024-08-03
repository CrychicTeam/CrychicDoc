package io.github.lightman314.lightmanscurrency.network.message.emergencyejection;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.menus.EjectionRecoveryMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CPacketOpenEjectionMenu extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenEjectionMenu> HANDLER = new CPacketOpenEjectionMenu.H();

    private CPacketOpenEjectionMenu() {
    }

    public static void sendToServer() {
        new CPacketOpenEjectionMenu().send();
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
    }

    private static class H extends CustomPacket.Handler<CPacketOpenEjectionMenu> {

        @Nonnull
        public CPacketOpenEjectionMenu decode(@Nonnull FriendlyByteBuf buffer) {
            LightmansCurrency.LogDebug("Decoded ejection packet!");
            return new CPacketOpenEjectionMenu();
        }

        protected void handle(@Nonnull CPacketOpenEjectionMenu message, @Nullable ServerPlayer sender) {
            LightmansCurrency.LogDebug("Opening ejection menu!");
            if (sender != null) {
                NetworkHooks.openScreen(sender, EjectionRecoveryMenu.PROVIDER);
            }
        }
    }
}