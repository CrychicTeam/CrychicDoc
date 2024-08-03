package icyllis.modernui.mc.forge;

import icyllis.modernui.core.Core;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.StillAlive;
import icyllis.modernui.mc.testforge.TestContainerMenu;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkHooks;

@EventBusSubscriber(modid = "modernui")
final class EventHandler {

    @SubscribeEvent
    static void onRightClickItem(@Nonnull PlayerInteractEvent.RightClickItem event) {
        boolean diamond;
        if (ModernUIMod.sDevelopment && event.getSide().isServer() && ((diamond = event.getItemStack().is(Items.DIAMOND)) || event.getItemStack().is(Items.EMERALD))) {
            if (event.getEntity().m_6144_()) {
                NetworkHooks.openScreen((ServerPlayer) event.getEntity(), new MenuProvider() {

                    @Nonnull
                    @Override
                    public Component getDisplayName() {
                        return CommonComponents.EMPTY;
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory inventory, @Nonnull Player player) {
                        return new TestContainerMenu(containerId, inventory, player);
                    }
                }, buf -> buf.writeBoolean(diamond));
            } else {
                MuiForgeApi.openMenu0(event.getEntity(), TestContainerMenu::new, buf -> buf.writeBoolean(diamond));
            }
        }
    }

    @EventBusSubscriber(modid = "modernui", value = { Dist.CLIENT })
    static class Client {

        private Client() {
        }

        @SubscribeEvent
        static void onRenderTick(@Nonnull TickEvent.RenderTickEvent event) {
            Core.flushMainCalls();
            Core.flushRenderCalls();
            StillAlive.tick();
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }
    }

    static class ClientDebug {

        private ClientDebug() {
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }
    }
}