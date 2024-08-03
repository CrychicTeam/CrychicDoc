package dev.ftb.mods.ftblibrary;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.ftb.mods.ftblibrary.config.FTBLibraryClientConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectImageResourceScreen;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonManager;
import dev.ftb.mods.ftblibrary.sidebar.SidebarGroupGuiButton;
import dev.ftb.mods.ftblibrary.ui.CursorType;
import dev.ftb.mods.ftblibrary.ui.IScreenWrapper;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import java.util.ArrayList;
import me.shedaniel.rei.api.client.config.ConfigObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.Nullable;

public class FTBLibraryClient {

    public static int showButtons = 1;

    public static CursorType lastCursorType = null;

    public static void init() {
        FTBLibraryClientConfig.load();
        if (Platform.isModLoaded("roughlyenoughitems")) {
            showButtons = 3;
        }
        if (Minecraft.getInstance() != null) {
            ClientGuiEvent.INIT_POST.register(FTBLibraryClient::guiInit);
            ClientTickEvent.CLIENT_POST.register(FTBLibraryClient::clientTick);
            ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, SidebarButtonManager.INSTANCE);
            ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, SelectImageResourceScreen.ResourceListener.INSTANCE);
        }
    }

    private static void guiInit(Screen screen, ScreenAccess access) {
        if (areButtonsVisible(screen)) {
            access.addRenderableWidget(new SidebarGroupGuiButton());
        }
    }

    private static void clientTick(Minecraft client) {
        CursorType t = client.screen instanceof IScreenWrapper ? ((IScreenWrapper) client.screen).getGui().getCursor() : null;
        if (lastCursorType != t) {
            lastCursorType = t;
            CursorType.set(t);
        }
        if (!ClientUtils.RUN_LATER.isEmpty()) {
            for (Runnable runnable : new ArrayList(ClientUtils.RUN_LATER)) {
                runnable.run();
            }
            ClientUtils.RUN_LATER.clear();
        }
    }

    public static boolean areButtonsVisible(@Nullable Screen gui) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {
            if (showButtons != 0 && (showButtons != 2 || gui instanceof EffectRenderingInventoryScreen)) {
                return showButtons == 3 && Platform.isModLoaded("roughlyenoughitems") && ConfigObject.getInstance().isFavoritesEnabled() ? false : gui instanceof AbstractContainerScreen && !SidebarButtonManager.INSTANCE.getGroups().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void editConfig(boolean isClientConfig) {
        new EditConfigScreen(FTBLibraryClientConfig.getConfigGroup()).setAutoclose(true).openGui();
    }
}