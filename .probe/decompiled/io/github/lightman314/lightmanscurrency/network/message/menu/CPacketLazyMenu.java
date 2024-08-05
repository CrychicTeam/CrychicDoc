package io.github.lightman314.lightmanscurrency.network.message.menu;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.menus.LazyMessageMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketLazyMenu extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketLazyMenu> HANDLER = new CPacketLazyMenu.H();

    private final LazyPacketData data;

    public CPacketLazyMenu(LazyPacketData data) {
        this.data = data;
    }

    public CPacketLazyMenu(LazyPacketData.Builder data) {
        this(data.build());
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        this.data.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<CPacketLazyMenu> {

        @Nonnull
        public CPacketLazyMenu decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketLazyMenu(LazyPacketData.decode(buffer));
        }

        protected void handle(@Nonnull CPacketLazyMenu message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof LazyMessageMenu menu) {
                menu.HandleMessage(message.data);
            }
        }
    }
}