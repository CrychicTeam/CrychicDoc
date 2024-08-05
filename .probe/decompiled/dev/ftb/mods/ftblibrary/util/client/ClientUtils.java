package dev.ftb.mods.ftblibrary.util.client;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import dev.ftb.mods.ftblibrary.ui.IScreenWrapper;
import dev.ftb.mods.ftblibrary.util.client.forge.ClientUtilsImpl;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ClientUtils {

    public static final BooleanSupplier IS_CLIENT_OP = () -> Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_20310_(1);

    public static final List<Runnable> RUN_LATER = new ArrayList();

    private static final MethodType EMPTY_METHOD_TYPE = MethodType.methodType(void.class);

    private static final HashMap<String, Optional<MethodHandle>> staticMethodCache = new HashMap();

    public static void execClientCommand(String command, boolean printChat) {
        if (!command.isEmpty() && Minecraft.getInstance().player != null) {
            EventResult res = ClientChatEvent.SEND.invoker().send(command, null);
            if (!res.interruptsFurtherEvaluation()) {
                if (printChat) {
                    Minecraft.getInstance().gui.getChat().addRecentChat(command);
                }
                Minecraft.getInstance().player.connection.sendCommand(command.replace("/", ""));
            }
        }
    }

    public static void runLater(Runnable runnable) {
        RUN_LATER.add(runnable);
    }

    @Nullable
    public static <T> T getGuiAs(Screen gui, Class<T> clazz) {
        if (gui instanceof IScreenWrapper) {
            BaseScreen guiBase = ((IScreenWrapper) gui).getGui();
            if (clazz.isAssignableFrom(guiBase.getClass())) {
                return (T) guiBase;
            }
        }
        return (T) (clazz.isAssignableFrom(gui.getClass()) ? Minecraft.getInstance().screen : null);
    }

    @Nullable
    public static <T> T getCurrentGuiAs(Class<T> clazz) {
        return Minecraft.getInstance().screen == null ? null : getGuiAs(Minecraft.getInstance().screen, clazz);
    }

    public static boolean handleClick(String scheme, String path) {
        switch(scheme) {
            case "http":
            case "https":
                try {
                    URI uri = new URI(scheme + ":" + path);
                    if (Minecraft.getInstance().options.chatLinksPrompt().get()) {
                        Screen currentScreen = Minecraft.getInstance().screen;
                        Minecraft.getInstance().setScreen(new ConfirmLinkScreen(result -> {
                            if (result) {
                                try {
                                    Util.getPlatform().openUri(uri);
                                } catch (Exception var4x) {
                                    var4x.printStackTrace();
                                }
                            }
                            Minecraft.getInstance().setScreen(currentScreen);
                        }, scheme + ":" + path, false));
                    } else {
                        Util.getPlatform().openUri(uri);
                    }
                    return true;
                } catch (Exception var10) {
                    var10.printStackTrace();
                    return false;
                }
            case "file":
                try {
                    Util.getPlatform().openUri(new URI("file:" + path));
                    return true;
                } catch (Exception var9) {
                    var9.printStackTrace();
                    return false;
                }
            case "command":
                execClientCommand(path, false);
                return true;
            case "static_method":
                Optional<MethodHandle> handle = (Optional<MethodHandle>) staticMethodCache.get(path);
                if (handle == null) {
                    handle = Optional.empty();
                    String[] s = path.split(":", 2);
                    try {
                        Class<?> c = Class.forName(s[0]);
                        MethodHandle h = MethodHandles.publicLookup().findStatic(c, s[1], EMPTY_METHOD_TYPE);
                        handle = Optional.ofNullable(h);
                    } catch (Throwable var8) {
                        var8.printStackTrace();
                    }
                    staticMethodCache.put(path, handle);
                }
                if (handle.isPresent()) {
                    try {
                        ((MethodHandle) handle.get()).invoke();
                        return true;
                    } catch (Throwable var11) {
                        var11.printStackTrace();
                    }
                }
                return false;
            case "custom":
                if (ResourceLocation.isValidResourceLocation(path)) {
                    return CustomClickEvent.EVENT.invoker().act(new CustomClickEvent(new ResourceLocation(path))).isPresent();
                }
            default:
                if (ResourceLocation.isValidResourceLocation(scheme + ":" + path)) {
                    return CustomClickEvent.EVENT.invoker().act(new CustomClickEvent(new ResourceLocation(scheme, path))).isPresent();
                } else {
                    FTBLibrary.LOGGER.warn("invalid scheme/path resourcelocation for handleClick(): {}:{}", scheme, path);
                    return false;
                }
        }
    }

    public static HolderLookup.Provider registryAccess() {
        return ((ClientLevel) Objects.requireNonNull(Minecraft.getInstance().level)).m_9598_();
    }

    @ExpectPlatform
    @Transformed
    public static ResourceLocation getStillTexture(FluidStack stack) {
        return ClientUtilsImpl.getStillTexture(stack);
    }

    @ExpectPlatform
    @Transformed
    public static int getFluidColor(FluidStack stack) {
        return ClientUtilsImpl.getFluidColor(stack);
    }
}