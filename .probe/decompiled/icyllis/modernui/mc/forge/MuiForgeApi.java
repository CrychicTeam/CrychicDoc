package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.VertexFormat;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.MuiScreen;
import icyllis.modernui.mc.UIManager;
import java.io.IOException;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class MuiForgeApi extends MuiModApi {

    public MuiForgeApi() {
        ModernUI.LOGGER.info(ModernUI.MARKER, "Created MuiForgeAPI");
    }

    @MainThread
    public static void openScreen(@Nonnull Fragment fragment) {
        MuiModApi.openScreen(fragment);
    }

    @Nonnull
    @Override
    public <T extends Screen & MuiScreen> T createScreen(@Nonnull Fragment fragment, @Nullable icyllis.modernui.mc.ScreenCallback callback, @Nullable Screen previousScreen, @Nullable CharSequence title) {
        return (T) (new SimpleScreen(UIManager.getInstance(), fragment, callback, previousScreen, title));
    }

    @Nonnull
    @Override
    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T> & MuiScreen> U createMenuScreen(@Nonnull Fragment fragment, @Nullable icyllis.modernui.mc.ScreenCallback callback, @Nonnull T menu, @Nonnull Inventory inventory, @Nonnull Component title) {
        return (U) (new MenuScreen(UIManager.getInstance(), fragment, callback, menu, inventory, title));
    }

    @RenderThread
    public static long getElapsedTime() {
        return MuiModApi.getElapsedTime();
    }

    @RenderThread
    public static long getFrameTime() {
        return getFrameTimeNanos() / 1000000L;
    }

    @RenderThread
    public static long getFrameTimeNanos() {
        return MuiModApi.getFrameTimeNanos();
    }

    public static void postToUiThread(@Nonnull Runnable r) {
        MuiModApi.postToUiThread(r);
    }

    @Override
    public boolean isGLVersionPromoted() {
        try {
            String version = ImmediateWindowHandler.getGLVersion();
            if (!"3.2".equals(version)) {
                ModernUI.LOGGER.info(ModernUI.MARKER, "Detected OpenGL {} Core Profile from FML Early Window", version);
                return true;
            }
        } catch (Exception var2) {
        }
        return false;
    }

    @Override
    public void loadEffect(GameRenderer gr, ResourceLocation effect) {
        gr.loadEffect(effect);
    }

    @Override
    public ShaderInstance makeShaderInstance(ResourceProvider resourceProvider, ResourceLocation resourceLocation, VertexFormat vertexFormat) throws IOException {
        return new ShaderInstance(resourceProvider, resourceLocation, vertexFormat);
    }

    @Override
    public boolean isKeyBindingMatches(KeyMapping keyMapping, InputConstants.Key key) {
        return keyMapping.isActiveAndMatches(key);
    }

    @Override
    public Style applyRarityTo(Rarity rarity, Style baseStyle) {
        return (Style) rarity.getStyleModifier().apply(baseStyle);
    }

    @Internal
    public static void openMenu(@Nonnull Player player, @Nonnull MenuProvider provider) {
        openMenu(player, provider, (Consumer<FriendlyByteBuf>) null);
    }

    @Internal
    public static void openMenu(@Nonnull Player player, @Nonnull MenuProvider provider, @Nonnull BlockPos pos) {
        openMenu(player, provider, buf -> buf.writeBlockPos(pos));
    }

    @Internal
    public static void openMenu(@Nonnull Player player, @Nonnull MenuProvider provider, @Nullable Consumer<FriendlyByteBuf> writer) {
        if (ModernUIMod.isDeveloperMode()) {
            openMenu0(player, provider, writer);
        } else {
            if (!(player instanceof ServerPlayer p)) {
                ModernUI.LOGGER.warn(ModernUI.MARKER, "openMenu() is not called from logical server", new Exception().fillInStackTrace());
                return;
            }
            NetworkHooks.openScreen(p, provider, writer);
        }
    }

    @Internal
    static void openMenu0(@Nonnull Player player, @Nonnull MenuConstructor provider, @Nullable Consumer<FriendlyByteBuf> writer) {
        if (player instanceof ServerPlayer p) {
            if (p.f_36096_ != p.f_36095_) {
                p.closeContainer();
            }
            p.nextContainerCounter();
            AbstractContainerMenu menu = provider.createMenu(p.containerCounter, p.m_150109_(), p);
            if (menu != null) {
                NetworkMessages.openMenu(menu, writer).sendToPlayer(p);
                p.initMenu(menu);
                p.f_36096_ = menu;
                MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(p, menu));
            }
        } else {
            ModernUI.LOGGER.warn(ModernUI.MARKER, "openMenu() is not called from logical server", new Exception().fillInStackTrace());
        }
    }
}