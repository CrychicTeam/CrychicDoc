package icyllis.modernui.mc.forge;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.ModernUIMod;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public sealed class NetworkMessages extends NetworkHandler permits NetworkMessages.Client {

    private static final int S2C_OPEN_MENU = 0;

    static NetworkHandler sNetwork;

    NetworkMessages() {
        super(ModernUIMod.location("network"), "360", true);
    }

    static PacketBuffer openMenu(@Nonnull AbstractContainerMenu menu, @Nullable Consumer<FriendlyByteBuf> writer) {
        PacketBuffer buf = sNetwork.buffer(0);
        buf.m_130130_(menu.containerId);
        buf.m_130130_(BuiltInRegistries.MENU.getId(menu.getType()));
        if (writer != null) {
            writer.accept(buf);
        }
        return buf;
    }

    static NetworkMessages client() {
        return new NetworkMessages.Client();
    }

    static final class Client extends NetworkMessages {

        private Client() {
        }

        @Override
        protected void handleClientMessage(int index, @Nonnull FriendlyByteBuf payload, @Nonnull Supplier<NetworkEvent.Context> source, @Nonnull BlockableEventLoop<?> looper) {
            if (index == 0) {
                openMenu(payload, source, looper);
            }
        }

        private static void openMenu(@Nonnull FriendlyByteBuf payload, @Nonnull Supplier<NetworkEvent.Context> source, @Nonnull BlockableEventLoop<?> looper) {
            int containerId = payload.readVarInt();
            MenuType<?> type = (MenuType<?>) BuiltInRegistries.MENU.m_200957_(payload.readVarInt());
            ResourceLocation key = BuiltInRegistries.MENU.getKey(type);
            assert key != null;
            payload.retain();
            looper.execute(() -> {
                try {
                    LocalPlayer p = getClientPlayer(source);
                    if (p != null) {
                        AbstractContainerMenu menu = type.create(containerId, p.m_150109_(), payload);
                        OpenMenuEvent event = new OpenMenuEvent(menu);
                        ModernUIForge.post(key.getNamespace(), event);
                        Fragment fragment = event.getFragment();
                        if (fragment == null) {
                            p.closeContainer();
                        } else {
                            p.f_36096_ = menu;
                            Minecraft.getInstance().setScreen(new MenuScreen<>(UIManagerForge.getInstance(), fragment, null, menu, p.m_150109_(), CommonComponents.EMPTY));
                        }
                    }
                } finally {
                    payload.release();
                }
            });
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }
    }
}