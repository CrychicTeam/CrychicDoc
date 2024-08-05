package net.minecraft.server.packs.resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ResourceProvider {

    Optional<Resource> getResource(ResourceLocation var1);

    default Resource getResourceOrThrow(ResourceLocation resourceLocation0) throws FileNotFoundException {
        return (Resource) this.getResource(resourceLocation0).orElseThrow(() -> new FileNotFoundException(resourceLocation0.toString()));
    }

    default InputStream open(ResourceLocation resourceLocation0) throws IOException {
        return this.getResourceOrThrow(resourceLocation0).open();
    }

    default BufferedReader openAsReader(ResourceLocation resourceLocation0) throws IOException {
        return this.getResourceOrThrow(resourceLocation0).openAsReader();
    }

    static ResourceProvider fromMap(Map<ResourceLocation, Resource> mapResourceLocationResource0) {
        return p_248274_ -> Optional.ofNullable((Resource) mapResourceLocationResource0.get(p_248274_));
    }
}