package dev.latvian.mods.kubejs.server;

import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.platform.RecipePlatformHelper;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.CustomIngredientAction;
import dev.latvian.mods.kubejs.recipe.special.SpecialRecipeSerializerManager;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import dev.latvian.mods.kubejs.server.tag.PreTagEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.jetbrains.annotations.Nullable;

public class ServerScriptManager extends ScriptManager {

    public static ServerScriptManager instance;

    public final MinecraftServer server;

    public final Map<ResourceKey<?>, PreTagEventJS> preTagEvents = new ConcurrentHashMap();

    public static ScriptManager getScriptManager() {
        return instance;
    }

    public ServerScriptManager(@Nullable MinecraftServer server) {
        super(ScriptType.SERVER);
        this.server = server;
        try {
            if (Files.notExists(KubeJSPaths.DATA, new LinkOption[0])) {
                Files.createDirectories(KubeJSPaths.DATA);
            }
        } catch (Throwable var3) {
            throw new RuntimeException("KubeJS failed to register it's script loader!", var3);
        }
    }

    public void updateResources(ReloadableServerResources serverResources, RegistryAccess registryAccess) {
        KubeJSReloadListener.resources = serverResources;
        KubeJSReloadListener.recipeContext = RecipePlatformHelper.get().createRecipeContext(serverResources);
        UtilsJS.staticRegistryAccess = registryAccess;
    }

    public MultiPackResourceManager wrapResourceManager(CloseableResourceManager original) {
        VirtualKubeJSDataPack virtualDataPackLow = new VirtualKubeJSDataPack(false);
        VirtualKubeJSDataPack virtualDataPackHigh = new VirtualKubeJSDataPack(true);
        LinkedList<PackResources> list = new LinkedList(original instanceof MultiPackResourceManager mp ? mp.packs : original.m_7536_().toList());
        list.addFirst(virtualDataPackLow);
        list.addLast(new GeneratedServerResourcePack());
        for (File file : (File[]) Objects.requireNonNull(KubeJSPaths.DATA.toFile().listFiles())) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                list.addLast(new FilePackResources(file.getName(), file, false));
            }
        }
        list.addLast(virtualDataPackHigh);
        MultiPackResourceManager wrappedResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, list);
        ConsoleJS.SERVER.setCapturingErrors(true);
        this.reload(wrappedResourceManager);
        ServerEvents.LOW_DATA.post(ScriptType.SERVER, new DataPackEventJS(virtualDataPackLow, wrappedResourceManager));
        ServerEvents.HIGH_DATA.post(ScriptType.SERVER, new DataPackEventJS(virtualDataPackHigh, wrappedResourceManager));
        ConsoleJS.SERVER.info("Scripts loaded");
        RecipesEventJS.customIngredientMap = new HashMap();
        CustomIngredientAction.MAP.clear();
        SpecialRecipeSerializerManager.INSTANCE.reset();
        ServerEvents.SPECIAL_RECIPES.post(ScriptType.SERVER, SpecialRecipeSerializerManager.INSTANCE);
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::onServerReload);
        PreTagEventJS.handle(this.preTagEvents);
        if (ServerEvents.RECIPES.hasListeners()) {
            RecipesEventJS.instance = new RecipesEventJS();
        }
        return wrappedResourceManager;
    }
}