package snownee.kiwi.util.resource;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class AlternativesFileToIdConverter {

    private final String prefix;

    private final List<String> extensions;

    private final int sameExtensionLength;

    public AlternativesFileToIdConverter(String pPrefix, List<String> pExtensions) {
        this.prefix = pPrefix;
        this.extensions = pExtensions;
        this.sameExtensionLength = pExtensions.stream().mapToInt(String::length).distinct().reduce((a, b) -> -1).orElseThrow();
        Preconditions.checkArgument(!this.extensions.isEmpty(), "Extensions cannot be empty");
    }

    public static AlternativesFileToIdConverter yamlOrJson(String pName) {
        return new AlternativesFileToIdConverter(pName, List.of(".yaml", ".json"));
    }

    public ResourceLocation idToFile(ResourceLocation pId) {
        return pId.withPath(this.prefix + "/" + pId.getPath() + (String) this.extensions.get(0));
    }

    public Stream<ResourceLocation> idToAllPossibleFiles(ResourceLocation pId) {
        return this.extensions.stream().map(ext -> pId.withPath(this.prefix + "/" + pId.getPath() + ext));
    }

    public ResourceLocation fileToId(ResourceLocation pFile) {
        if (this.sameExtensionLength >= 0) {
            String s = pFile.getPath();
            return pFile.withPath(s.substring(this.prefix.length() + 1, s.length() - this.sameExtensionLength));
        } else {
            for (String ext : this.extensions) {
                if (pFile.getPath().endsWith(ext)) {
                    String s = pFile.getPath();
                    return pFile.withPath(s.substring(this.prefix.length() + 1, s.length() - ext.length()));
                }
            }
            throw new IllegalArgumentException("Unknown extension for " + pFile);
        }
    }

    public Map<ResourceLocation, Resource> listMatchingResources(ResourceManager pResourceManager) {
        return pResourceManager.listResources(this.prefix, location -> this.extensions.stream().anyMatch(location.getPath()::endsWith));
    }

    public Map<ResourceLocation, List<Resource>> listMatchingResourceStacks(ResourceManager pResourceManager) {
        return pResourceManager.listResourceStacks(this.prefix, location -> this.extensions.stream().anyMatch(location.getPath()::endsWith));
    }
}