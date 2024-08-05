package vazkii.patchouli.xplat;

import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

public interface IXplatAbstractions {

    IXplatAbstractions INSTANCE = find();

    void fireDrawBookScreen(ResourceLocation var1, Screen var2, int var3, int var4, float var5, GuiGraphics var6);

    void fireBookReload(ResourceLocation var1);

    void sendReloadContentsMessage(MinecraftServer var1);

    void sendOpenBookGui(ServerPlayer var1, ResourceLocation var2, @Nullable ResourceLocation var3, int var4);

    Collection<XplatModContainer> getAllMods();

    XplatModContainer getModContainer(String var1);

    boolean isModLoaded(String var1);

    boolean isDevEnvironment();

    boolean isPhysicalClient();

    default void signalBooksLoaded() {
    }

    boolean handleRecipeKeybind(int var1, int var2, ItemStack var3);

    private static IXplatAbstractions find() {
        List<Provider<IXplatAbstractions>> providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
        if (providers.size() != 1) {
            String names = (String) providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
        } else {
            Provider<IXplatAbstractions> provider = (Provider<IXplatAbstractions>) providers.get(0);
            PatchouliAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
            return (IXplatAbstractions) provider.get();
        }
    }
}