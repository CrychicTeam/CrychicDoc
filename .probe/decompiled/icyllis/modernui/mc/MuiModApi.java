package icyllis.modernui.mc;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.VertexFormat;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.text.GraphemeBreak;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Rarity;

public abstract class MuiModApi {

    static final CopyOnWriteArrayList<MuiModApi.OnScrollListener> sOnScrollListeners = new CopyOnWriteArrayList();

    static final CopyOnWriteArrayList<MuiModApi.OnScreenChangeListener> sOnScreenChangeListeners = new CopyOnWriteArrayList();

    static final CopyOnWriteArrayList<MuiModApi.OnWindowResizeListener> sOnWindowResizeListeners = new CopyOnWriteArrayList();

    static final CopyOnWriteArrayList<MuiModApi.OnDebugDumpListener> sOnDebugDumpListeners = new CopyOnWriteArrayList();

    static final MuiModApi INSTANCE = (MuiModApi) ServiceLoader.load(MuiModApi.class).findFirst().orElseThrow();

    public static final int MAX_GUI_SCALE = 8;

    public static final Pattern EMOJI_SHORTCODE_PATTERN = Pattern.compile("(:(\\w|\\+|-)+:)(?=|[!.?]|$)");

    private static final ChatFormatting[] FORMATTING_TABLE = new ChatFormatting[128];

    public static MuiModApi get() {
        return INSTANCE;
    }

    @MainThread
    public static void openScreen(@Nonnull Fragment fragment) {
        UIManager.getInstance().open(fragment);
    }

    @Nonnull
    public final <T extends Screen & MuiScreen> T createScreen(@Nonnull Fragment fragment) {
        return this.createScreen(fragment, null, null, null);
    }

    @Nonnull
    public final <T extends Screen & MuiScreen> T createScreen(@Nonnull Fragment fragment, @Nullable ScreenCallback callback) {
        return this.createScreen(fragment, callback, null, null);
    }

    @Nonnull
    public final <T extends Screen & MuiScreen> T createScreen(@Nonnull Fragment fragment, @Nullable ScreenCallback callback, @Nullable Screen previousScreen) {
        return this.createScreen(fragment, callback, previousScreen, null);
    }

    @Nonnull
    public abstract <T extends Screen & MuiScreen> T createScreen(@Nonnull Fragment var1, @Nullable ScreenCallback var2, @Nullable Screen var3, @Nullable CharSequence var4);

    @Nonnull
    public abstract <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T> & MuiScreen> U createMenuScreen(@Nonnull Fragment var1, @Nullable ScreenCallback var2, @Nonnull T var3, @Nonnull Inventory var4, @Nonnull Component var5);

    @RenderThread
    public static long getElapsedTime() {
        return UIManager.getElapsedTime();
    }

    @RenderThread
    public static long getFrameTime() {
        return getFrameTimeNanos() / 1000000L;
    }

    @RenderThread
    public static long getFrameTimeNanos() {
        return UIManager.getFrameTimeNanos();
    }

    public static void postToUiThread(@Nonnull Runnable r) {
        Core.getUiHandlerAsync().post(r);
    }

    public static int calcGuiScales() {
        return calcGuiScales(Minecraft.getInstance().getWindow());
    }

    public static int calcGuiScales(@Nonnull Window window) {
        return calcGuiScales(window.getWidth(), window.getHeight());
    }

    public static int calcGuiScales(int framebufferWidth, int framebufferHeight) {
        double w = (double) framebufferWidth / 16.0;
        double h = (double) framebufferHeight / 9.0;
        double base = Math.min(w, h);
        int det = (int) (Math.min((double) framebufferWidth, h * 12.0) / 320.0);
        int min;
        if (det >= 2) {
            min = MathUtil.clamp((int) (base / 64.0), 2, 8);
        } else {
            min = 2;
        }
        int max = MathUtil.clamp(det, 2, 8);
        int auto;
        if (min >= 2) {
            double step = base > 216.0 ? 42.0 : (base > 120.0 ? 36.0 : 30.0);
            int i = (int) (base / step);
            int j = (int) (Math.max(w, h) / step);
            double v1 = base / ((double) i * 30.0);
            if (!(v1 > 1.4) && j <= i) {
                auto = MathUtil.clamp(i, min, max);
            } else {
                auto = MathUtil.clamp(i + 1, min, max);
            }
        } else {
            auto = 2;
        }
        assert min <= auto && auto <= max;
        return min << 8 | auto << 4 | max;
    }

    public static int offsetByGrapheme(String value, int cursor, int dir) {
        int op;
        if (dir < 0) {
            op = 2;
        } else if (dir == 0) {
            op = 3;
        } else {
            op = 0;
        }
        int offset = Util.offsetByCodepoints(value, cursor, dir);
        cursor = GraphemeBreak.getTextRunCursor(value, ModernUI.getSelectedLocale(), 0, value.length(), cursor, op);
        return dir > 0 ? Math.max(offset, cursor) : Math.min(offset, cursor);
    }

    @Nullable
    public static ChatFormatting getFormattingByCode(char code) {
        return code < 128 ? FORMATTING_TABLE[code] : null;
    }

    public abstract boolean isGLVersionPromoted();

    public abstract void loadEffect(GameRenderer var1, ResourceLocation var2);

    public abstract ShaderInstance makeShaderInstance(ResourceProvider var1, ResourceLocation var2, VertexFormat var3) throws IOException;

    public abstract boolean isKeyBindingMatches(KeyMapping var1, InputConstants.Key var2);

    public abstract Style applyRarityTo(Rarity var1, Style var2);

    public static void addOnScrollListener(@Nonnull MuiModApi.OnScrollListener listener) {
        sOnScrollListeners.addIfAbsent(listener);
    }

    public static void removeOnScrollListener(@Nonnull MuiModApi.OnScrollListener listener) {
        sOnScrollListeners.remove(listener);
    }

    public static void addOnScreenChangeListener(@Nonnull MuiModApi.OnScreenChangeListener listener) {
        sOnScreenChangeListeners.addIfAbsent(listener);
    }

    public static void removeOnScreenChangeListener(@Nonnull MuiModApi.OnScreenChangeListener listener) {
        sOnScreenChangeListeners.remove(listener);
    }

    public static void addOnWindowResizeListener(@Nonnull MuiModApi.OnWindowResizeListener listener) {
        sOnWindowResizeListeners.addIfAbsent(listener);
    }

    public static void removeOnWindowResizeListener(@Nonnull MuiModApi.OnWindowResizeListener listener) {
        sOnWindowResizeListeners.remove(listener);
    }

    public static void addOnDebugDumpListener(@Nonnull MuiModApi.OnDebugDumpListener listener) {
        sOnDebugDumpListeners.addIfAbsent(listener);
    }

    public static void removeOnDebugDumpListener(@Nonnull MuiModApi.OnDebugDumpListener listener) {
        sOnDebugDumpListeners.remove(listener);
    }

    public static void dispatchOnScroll(double scrollX, double scrollY) {
        for (MuiModApi.OnScrollListener l : sOnScrollListeners) {
            l.onScroll(scrollX, scrollY);
        }
    }

    public static void dispatchOnScreenChange(@Nullable Screen oldScreen, @Nullable Screen newScreen) {
        for (MuiModApi.OnScreenChangeListener l : sOnScreenChangeListeners) {
            l.onScreenChange(oldScreen, newScreen);
        }
    }

    public static void dispatchOnWindowResize(int width, int height, int guiScale, int oldGuiScale) {
        for (MuiModApi.OnWindowResizeListener l : sOnWindowResizeListeners) {
            l.onWindowResize(width, height, guiScale, oldGuiScale);
        }
    }

    public static void dispatchOnDebugDump(@Nonnull PrintWriter writer) {
        for (MuiModApi.OnDebugDumpListener l : sOnDebugDumpListeners) {
            l.onDebugDump(writer);
        }
    }

    static {
        for (ChatFormatting f : ChatFormatting.values()) {
            FORMATTING_TABLE[f.getChar()] = f;
            FORMATTING_TABLE[Character.toUpperCase(f.getChar())] = f;
        }
    }

    @FunctionalInterface
    public interface OnDebugDumpListener {

        void onDebugDump(@Nonnull PrintWriter var1);
    }

    @FunctionalInterface
    public interface OnScreenChangeListener {

        void onScreenChange(@Nullable Screen var1, @Nullable Screen var2);
    }

    @FunctionalInterface
    public interface OnScrollListener {

        void onScroll(double var1, double var3);
    }

    @FunctionalInterface
    public interface OnWindowResizeListener {

        void onWindowResize(int var1, int var2, int var3, int var4);
    }
}