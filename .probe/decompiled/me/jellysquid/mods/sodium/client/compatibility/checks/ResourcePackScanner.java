package me.jellysquid.mods.sodium.client.compatibility.checks;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import me.jellysquid.mods.sodium.client.gui.console.Console;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcePackScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger("Sodium-ResourcePackScanner");

    private static final Set<String> SHADER_PROGRAM_BLACKLIST = Set.of("rendertype_solid.vsh", "rendertype_solid.fsh", "rendertype_solid.json", "rendertype_cutout_mipped.vsh", "rendertype_cutout_mipped.fsh", "rendertype_cutout_mipped.json", "rendertype_cutout.vsh", "rendertype_cutout.fsh", "rendertype_cutout.json", "rendertype_translucent.vsh", "rendertype_translucent.fsh", "rendertype_translucent.json", "rendertype_tripwire.vsh", "rendertype_tripwire.fsh", "rendertype_tripwire.json");

    private static final Set<String> SHADER_INCLUDE_BLACKLIST = Set.of("light.glsl", "fog.glsl");

    public static void checkIfCoreShaderLoaded(ResourceManager manager) {
        List<ResourcePackScanner.ScannedResourcePack> outputs = manager.listPacks().filter(ResourcePackScanner::isExternalResourcePack).map(ResourcePackScanner::scanResources).toList();
        printToasts(outputs);
        printCompatibilityReport(outputs);
    }

    private static void printToasts(Collection<ResourcePackScanner.ScannedResourcePack> resourcePacks) {
        ReferenceLinkedOpenHashSet<ResourcePackScanner.ScannedResourcePack> incompatibleResourcePacks = (ReferenceLinkedOpenHashSet<ResourcePackScanner.ScannedResourcePack>) resourcePacks.stream().filter(pack -> !pack.shaderPrograms.isEmpty()).collect(Collectors.toCollection(ReferenceLinkedOpenHashSet::new));
        List<ResourcePackScanner.ScannedResourcePack> likelyIncompatibleResourcePacks = resourcePacks.stream().filter(pack -> !pack.shaderIncludes.isEmpty()).filter(pack -> !incompatibleResourcePacks.contains(pack)).toList();
        boolean shown = false;
        if (!incompatibleResourcePacks.isEmpty()) {
            showConsoleMessage(Component.translatable("sodium.console.core_shaders_error"), MessageLevel.SEVERE);
            ObjectListIterator var4 = incompatibleResourcePacks.iterator();
            while (var4.hasNext()) {
                ResourcePackScanner.ScannedResourcePack entry = (ResourcePackScanner.ScannedResourcePack) var4.next();
                showConsoleMessage(Component.literal(getResourcePackName(entry.resourcePack)), MessageLevel.SEVERE);
            }
            shown = true;
        }
        if (!likelyIncompatibleResourcePacks.isEmpty()) {
            showConsoleMessage(Component.translatable("sodium.console.core_shaders_warn"), MessageLevel.WARN);
            for (ResourcePackScanner.ScannedResourcePack entry : likelyIncompatibleResourcePacks) {
                showConsoleMessage(Component.literal(getResourcePackName(entry.resourcePack)), MessageLevel.WARN);
            }
            shown = true;
        }
        if (shown) {
            showConsoleMessage(Component.translatable("sodium.console.core_shaders_info"), MessageLevel.INFO);
        }
    }

    private static void printCompatibilityReport(Collection<ResourcePackScanner.ScannedResourcePack> scanResults) {
        StringBuilder builder = new StringBuilder();
        for (ResourcePackScanner.ScannedResourcePack entry : scanResults) {
            if (!entry.shaderPrograms.isEmpty() || !entry.shaderIncludes.isEmpty()) {
                builder.append("- Resource pack: ").append(getResourcePackName(entry.resourcePack)).append("\n");
                if (!entry.shaderPrograms.isEmpty()) {
                    emitProblem(builder, "The resource pack replaces terrain shaders, which are not supported", "https://github.com/CaffeineMC/sodium-fabric/wiki/Resource-Packs", entry.shaderPrograms);
                }
                if (!entry.shaderIncludes.isEmpty()) {
                    emitProblem(builder, "The resource pack modifies shader include files, which are not fully supported", "https://github.com/CaffeineMC/sodium-fabric/wiki/Resource-Packs", entry.shaderIncludes);
                }
            }
        }
        if (!builder.isEmpty()) {
            LOGGER.error("The following compatibility issues were found with installed resource packs:\n{}", builder);
        }
    }

    private static void emitProblem(StringBuilder builder, String description, String url, List<String> resources) {
        builder.append("\t- Problem found: ").append("\n");
        builder.append("\t\t- Description:\n\t\t\t").append(description).append("\n");
        builder.append("\t\t- More information: ").append(url).append("\n");
        builder.append("\t\t- Files: ").append("\n");
        for (String resource : resources) {
            builder.append("\t\t\t- ").append(resource).append("\n");
        }
    }

    @NotNull
    private static ResourcePackScanner.ScannedResourcePack scanResources(PackResources pack) {
        List<String> ignoredShaders = determineIgnoredShaders(pack);
        if (!ignoredShaders.isEmpty()) {
            LOGGER.warn("Resource pack '{}' indicates the following shaders should be ignored: {}", getResourcePackName(pack), String.join(", ", ignoredShaders));
        }
        ArrayList<String> unsupportedShaderPrograms = new ArrayList();
        ArrayList<String> unsupportedShaderIncludes = new ArrayList();
        pack.listResources(PackType.CLIENT_RESOURCES, "minecraft", "shaders", (identifier, supplier) -> {
            String path = identifier.getPath();
            String name = path.substring(path.lastIndexOf(47) + 1);
            if (!ignoredShaders.contains(name)) {
                if (SHADER_PROGRAM_BLACKLIST.contains(name)) {
                    unsupportedShaderPrograms.add(path);
                } else if (SHADER_INCLUDE_BLACKLIST.contains(name)) {
                    unsupportedShaderIncludes.add(path);
                }
            }
        });
        return new ResourcePackScanner.ScannedResourcePack(pack, unsupportedShaderPrograms, unsupportedShaderIncludes);
    }

    private static boolean isExternalResourcePack(PackResources pack) {
        return pack instanceof FilePackResources;
    }

    private static String getResourcePackName(PackResources pack) {
        String path = pack.packId();
        return path.startsWith("file/") ? path.substring(5) : path;
    }

    private static List<String> determineIgnoredShaders(PackResources resourcePack) {
        ArrayList<String> ignoredShaders = new ArrayList();
        try {
            SodiumResourcePackMetadata meta = resourcePack.getMetadataSection(SodiumResourcePackMetadata.SERIALIZER);
            if (meta != null) {
                ignoredShaders.addAll(meta.ignoredShaders());
            }
        } catch (IOException var3) {
            LOGGER.error("Failed to load pack.mcmeta file for resource pack '{}'", resourcePack.packId());
        }
        return ignoredShaders;
    }

    private static void showConsoleMessage(MutableComponent message, MessageLevel messageLevel) {
        Console.instance().logMessage(messageLevel, message, 12.5);
    }

    private static record ScannedResourcePack(PackResources resourcePack, ArrayList<String> shaderPrograms, ArrayList<String> shaderIncludes) {
    }
}