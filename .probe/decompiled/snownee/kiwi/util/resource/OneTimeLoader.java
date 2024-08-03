package snownee.kiwi.util.resource;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.BufferedReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.util.KEval;
import snownee.kiwi.util.Util;
import snownee.kiwi.util.codec.JavaOps;

public class OneTimeLoader {

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public static <T> Map<ResourceLocation, T> load(ResourceManager resourceManager, String directory, Codec<T> codec, OneTimeLoader.Context context) {
        AlternativesFileToIdConverter fileToIdConverter = AlternativesFileToIdConverter.yamlOrJson(directory);
        Map<ResourceLocation, T> results = Maps.newHashMap();
        for (Entry<ResourceLocation, Resource> entry : fileToIdConverter.listMatchingResources(resourceManager).entrySet()) {
            ResourceLocation key = (ResourceLocation) entry.getKey();
            if (!context.isNamespaceDisabled(key.getNamespace())) {
                DataResult<T> result = parseFile(key, (Resource) entry.getValue(), codec, context);
                if (result != null) {
                    if (result.error().isPresent()) {
                        Kiwi.LOGGER.error("Failed to parse " + key + ": " + result.error().get());
                    } else {
                        ResourceLocation id = fileToIdConverter.fileToId(key);
                        results.put(id, result.result().orElseThrow());
                    }
                }
            }
        }
        return results;
    }

    @Nullable
    public static <T> T loadFile(ResourceManager resourceManager, String directory, ResourceLocation id, Codec<T> codec, @Nullable OneTimeLoader.Context context) {
        AlternativesFileToIdConverter fileToIdConverter = AlternativesFileToIdConverter.yamlOrJson(directory);
        ResourceLocation file = fileToIdConverter.idToFile(id);
        Optional<Resource> resource = resourceManager.m_213713_(file);
        if (resource.isEmpty()) {
            return null;
        } else {
            DataResult<T> result = parseFile(file, (Resource) resource.get(), codec, context);
            if (result == null) {
                return null;
            } else if (result.error().isPresent()) {
                Kiwi.LOGGER.error("Failed to parse " + file + ": " + result.error().get());
                return null;
            } else {
                return (T) result.result().orElseThrow();
            }
        }
    }

    @Nullable
    public static <T> DataResult<T> parseFile(ResourceLocation file, Resource resource, Codec<T> codec, @Nullable OneTimeLoader.Context context) {
        String ext = file.getPath().substring(file.getPath().length() - 5);
        try {
            BufferedReader reader = resource.openAsReader();
            Object var9;
            label76: {
                DataResult var16;
                label77: {
                    try {
                        label81: {
                            Dynamic<?> dynamic;
                            if (ext.equals(".json")) {
                                JsonElement value = (JsonElement) GSON.fromJson(reader, JsonElement.class);
                                dynamic = new Dynamic(JsonOps.INSTANCE, value);
                            } else {
                                if (!ext.equals(".yaml")) {
                                    var16 = DataResult.error(() -> "Unknown extension: " + ext);
                                    break label81;
                                }
                                Object value = Util.loadYaml(reader, Object.class);
                                dynamic = new Dynamic(JavaOps.INSTANCE, value);
                            }
                            if (context != null) {
                                Optional<String> condition = dynamic.get("condition").asString().result();
                                if (condition.isPresent()) {
                                    try {
                                        Expression expression = context.getExpression((String) condition.get());
                                        if (expression.evaluate().getBooleanValue() != Boolean.FALSE) {
                                            var9 = null;
                                            break label76;
                                        }
                                    } catch (Exception var11) {
                                        Kiwi.LOGGER.error("Failed to parse condition in " + file + ": " + var11);
                                    }
                                }
                            }
                            var16 = codec.parse(dynamic);
                            break label77;
                        }
                    } catch (Throwable var12) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable var10) {
                                var12.addSuppressed(var10);
                            }
                        }
                        throw var12;
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    return var16;
                }
                if (reader != null) {
                    reader.close();
                }
                return var16;
            }
            if (reader != null) {
                reader.close();
            }
            return (DataResult<T>) var9;
        } catch (Exception var13) {
            return DataResult.error(() -> "Failed to load " + file + ": " + var13);
        }
    }

    public static class Context {

        private Map<String, Expression> cachedExpressions;

        private Set<String> disabledNamespaces;

        public Expression getExpression(String expression) {
            if (this.cachedExpressions == null) {
                this.cachedExpressions = Maps.newHashMap();
            }
            return (Expression) this.cachedExpressions.computeIfAbsent(expression, $ -> new Expression($, KEval.config()));
        }

        public void addDisabledNamespace(String namespace) {
            if (this.disabledNamespaces == null) {
                this.disabledNamespaces = Sets.newHashSet();
            }
            this.disabledNamespaces.add(namespace);
        }

        public boolean isNamespaceDisabled(String namespace) {
            return this.disabledNamespaces != null && this.disabledNamespaces.contains(namespace);
        }
    }
}