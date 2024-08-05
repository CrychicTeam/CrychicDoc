package net.minecraft.resources;

import java.util.List;
import java.util.Map;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class FileToIdConverter {

    private final String prefix;

    private final String extension;

    public FileToIdConverter(String string0, String string1) {
        this.prefix = string0;
        this.extension = string1;
    }

    public static FileToIdConverter json(String string0) {
        return new FileToIdConverter(string0, ".json");
    }

    public ResourceLocation idToFile(ResourceLocation resourceLocation0) {
        return resourceLocation0.withPath(this.prefix + "/" + resourceLocation0.getPath() + this.extension);
    }

    public ResourceLocation fileToId(ResourceLocation resourceLocation0) {
        String $$1 = resourceLocation0.getPath();
        return resourceLocation0.withPath($$1.substring(this.prefix.length() + 1, $$1.length() - this.extension.length()));
    }

    public Map<ResourceLocation, Resource> listMatchingResources(ResourceManager resourceManager0) {
        return resourceManager0.listResources(this.prefix, p_251986_ -> p_251986_.getPath().endsWith(this.extension));
    }

    public Map<ResourceLocation, List<Resource>> listMatchingResourceStacks(ResourceManager resourceManager0) {
        return resourceManager0.listResourceStacks(this.prefix, p_248700_ -> p_248700_.getPath().endsWith(this.extension));
    }
}