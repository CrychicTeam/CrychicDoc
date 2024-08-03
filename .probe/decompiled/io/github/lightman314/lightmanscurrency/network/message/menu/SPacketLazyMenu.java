package io.github.lightman314.lightmanscurrency.network.message.menu;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.menus.LazyMessageMenu;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketLazyMenu extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketLazyMenu> HANDLER = new SPacketLazyMenu.H();

    private final LazyPacketData data;

    public SPacketLazyMenu(LazyPacketData data) {
        this.data = data;
    }

    public SPacketLazyMenu(LazyPacketData.Builder data) {
        this(data.build());
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        this.data.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<SPacketLazyMenu> {

        @Nonnull
        public SPacketLazyMenu decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketLazyMenu(LazyPacketData.decode(buffer));
        }

        protected void handle(@Nonnull SPacketLazyMenu message, @Nullable ServerPlayer sender) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.f_36096_ instanceof LazyMessageMenu menu) {
                menu.HandleMessage(message.data);
            }
        }
    }
}