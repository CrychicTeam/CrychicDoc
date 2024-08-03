package icyllis.modernui.mc.forge;

import icyllis.modernui.ModernUI;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.text.MuiTextCommand;
import icyllis.modernui.mc.text.TextLayoutEngine;
import javax.annotation.Nonnull;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class ModernUIText {

    private ModernUIText() {
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().register(ModernUIText.class);
    }

    @SubscribeEvent
    static void setupClient(@Nonnull FMLClientSetupEvent event) {
        MuiModApi.addOnWindowResizeListener(TextLayoutEngine.getInstance());
        MuiModApi.addOnDebugDumpListener(TextLayoutEngine.getInstance());
        MinecraftForge.EVENT_BUS.register(ModernUIText.EventHandler.class);
        ModernUI.LOGGER.info(ModernUI.MARKER, "Loaded modern text engine");
    }

    static {
        assert FMLEnvironment.dist.isClient();
    }

    static class EventHandler {

        @SubscribeEvent
        static void onClientTick(@Nonnull TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                TextLayoutEngine.getInstance().onEndClientTick();
            }
        }

        @SubscribeEvent
        static void onRegisterClientCommands(@Nonnull RegisterClientCommandsEvent event) {
            MuiTextCommand.register(event.getDispatcher());
        }
    }
}