package snownee.kiwi.customization;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import snownee.kiwi.util.Util;
import snownee.kiwi.util.resource.AlternativesFileToIdConverter;
import snownee.kiwi.util.resource.OneTimeLoader;

public record CustomizationMetadata(ImmutableListMultimap<String, String> registryOrder) {

    public static final Codec<CustomizationMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).fieldOf("registry_order").forGetter($ -> {
        Map<String, List<String>> map = Maps.newHashMap();
        UnmodifiableIterator var2 = $.registryOrder().asMap().entrySet().iterator();
        while (var2.hasNext()) {
            Entry<String, Collection<String>> entry = (Entry<String, Collection<String>>) var2.next();
            map.put((String) entry.getKey(), List.copyOf((Collection) entry.getValue()));
        }
        return map;
    })).apply(instance, CustomizationMetadata::create));

    public static CustomizationMetadata create(Map<String, List<String>> map) {
        Builder<String, String> builder = ImmutableListMultimap.builder();
        map.forEach(builder::putAll);
        return new CustomizationMetadata(builder.build());
    }

    public static Map<String, CustomizationMetadata> loadMap(ResourceManager resourceManager, OneTimeLoader.Context context) {
        CustomizationMetadata emptyMetadata = new CustomizationMetadata(ImmutableListMultimap.of());
        AlternativesFileToIdConverter fileToIdConverter = AlternativesFileToIdConverter.yamlOrJson("kiwi");
        Map<String, CustomizationMetadata> metadataMap = Maps.newHashMap();
        for (String namespace : resourceManager.getNamespaces()) {
            ResourceLocation file = fileToIdConverter.idToFile(new ResourceLocation(namespace, "metadata"));
            Optional<Resource> resource = resourceManager.m_213713_(file);
            if (resource.isEmpty()) {
                metadataMap.put(namespace, emptyMetadata);
            } else {
                DataResult<CustomizationMetadata> result = OneTimeLoader.parseFile(file, (Resource) resource.get(), CODEC, context);
                if (result == null) {
                    context.addDisabledNamespace(namespace);
                } else {
                    result.result().ifPresentOrElse(customizationMetadata -> metadataMap.put(namespace, customizationMetadata), () -> context.addDisabledNamespace(namespace));
                }
            }
        }
        return metadataMap;
    }

    public static <T> void sortedForEach(Map<String, CustomizationMetadata> metadataMap, String key, Map<ResourceLocation, T> values, BiConsumer<ResourceLocation, T> action) {
        Set<ResourceLocation> order = Sets.newLinkedHashSet();
        metadataMap.forEach((namespace, metadata) -> {
            UnmodifiableIterator var4x = metadata.registryOrder().get(key).iterator();
            while (var4x.hasNext()) {
                String s = (String) var4x.next();
                order.add(Util.RL(s, namespace));
            }
        });
        for (ResourceLocation id : order) {
            T value = (T) values.get(id);
            if (value != null) {
                action.accept(id, value);
            }
        }
        values.forEach((idx, valuex) -> {
            if (!order.contains(idx)) {
                action.accept(idx, valuex);
            }
        });
    }
}