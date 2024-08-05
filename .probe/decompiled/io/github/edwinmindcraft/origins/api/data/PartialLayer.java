package io.github.edwinmindcraft.origins.api.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.ConditionedOrigin;
import io.github.edwinmindcraft.origins.api.origin.GuiTitle;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.api.util.JsonUtils;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PartialLayer(@Nullable Integer order, @NotNull Set<ConditionedOrigin> origins, @Nullable Boolean enabled, boolean replace, @Nullable String name, @Nullable String missingName, @Nullable String missingDescription, @Nullable Boolean allowRandom, @Nullable Boolean allowRandomUnchoosable, @NotNull Set<ResourceLocation> excludeRandom, boolean replaceExcludeRandom, @Nullable ResourceLocation defaultOrigin, @Nullable Boolean autoChoose, int loadingPriority, @Nullable Boolean hidden, @Nullable PartialGuiTitle title) {

    public static final Comparator<PartialLayer> LOADING_COMPARATOR = Comparator.comparingInt(PartialLayer::loadingPriority);

    public PartialLayer merge(PartialLayer other) {
        PartialLayer.Builder builder = builder().replace(false).replaceExcludeRandom(false).loadingPriority(0).order(other.order() != null ? other.order() : this.order()).enabled(other.enabled() != null ? other.enabled() : this.enabled()).name(other.name() != null ? other.name() : this.name()).missingName(other.missingName() != null ? other.missingName() : this.missingName()).missingDescription(other.missingDescription() != null ? other.missingDescription() : this.missingDescription()).allowRandom(other.allowRandom() != null ? other.allowRandom() : this.allowRandom()).allowRandomUnchoosable(other.allowRandomUnchoosable() != null ? other.allowRandomUnchoosable() : this.allowRandomUnchoosable()).origins(other.origins()).excludeRandom(other.excludeRandom()).defaultOrigin(other.defaultOrigin() != null ? other.defaultOrigin() : this.defaultOrigin()).autoChoose(other.autoChoose() != null ? other.autoChoose() : this.autoChoose()).hidden(other.hidden() != null ? other.hidden() : this.hidden()).title(PartialGuiTitle.merge(this.title(), other.title()));
        if (!other.replace()) {
            builder.origins(this.origins());
        }
        if (!other.replaceExcludeRandom()) {
            builder.excludeRandom(this.excludeRandom());
        }
        return builder.build();
    }

    public OriginLayer create(ResourceLocation registryName) {
        return new OriginLayer(this.order() != null ? this.order() : 0, ImmutableSet.copyOf(this.origins()), this.enabled() != null ? this.enabled() : true, Component.translatable(this.name() != null ? this.name() : "layer.%s.%s.name".formatted(registryName.getNamespace(), registryName.getPath())), Component.translatable(this.missingName() != null ? this.missingName() : "layer.%s.%s.missing_origin.name".formatted(registryName.getNamespace(), registryName.getPath())), Component.translatable(this.missingDescription() != null ? this.missingDescription() : "layer.%s.%s.missing_origin.description".formatted(registryName.getNamespace(), registryName.getPath())), this.allowRandom() != null ? this.allowRandom() : false, this.allowRandomUnchoosable() != null ? this.allowRandomUnchoosable() : false, ImmutableSet.copyOf(this.excludeRandom()), OriginsAPI.getOriginsRegistry().m_246971_(this.defaultOrigin() == null ? (ResourceKey) Objects.requireNonNull(OriginRegisters.EMPTY.getKey()) : ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, this.defaultOrigin())), this.autoChoose() != null ? this.autoChoose() : false, this.hidden() != null ? this.hidden() : false, this.title() != null ? this.title().create(registryName) : GuiTitle.DEFAULT);
    }

    public static PartialLayer.Builder builder() {
        return new PartialLayer.Builder();
    }

    public static final class Builder {

        private Integer order;

        private final Set<ConditionedOrigin> origins = new HashSet();

        private Boolean enabled;

        private boolean replace = false;

        private String name;

        private String missingName;

        private String missingDescription;

        private Boolean allowRandom;

        private Boolean allowRandomUnchoosable;

        private final Set<ResourceLocation> excludeRandom = new HashSet();

        private boolean replaceExcludeRandom = false;

        private ResourceLocation defaultOrigin;

        private Boolean autoChoose;

        private Boolean hidden;

        private int loadingPriority;

        private PartialGuiTitle title;

        private Builder() {
        }

        @Contract("_ -> this")
        public PartialLayer.Builder order(@Nullable Integer order) {
            this.order = order;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder origins(Collection<ConditionedOrigin> origins) {
            this.origins.addAll(origins);
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder enabled(@Nullable Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder name(@Nullable String name) {
            this.name = name;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder missingName(@Nullable String missingName) {
            this.missingName = missingName;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder missingDescription(@Nullable String missingDescription) {
            this.missingDescription = missingDescription;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder allowRandom(@Nullable Boolean allowRandom) {
            this.allowRandom = allowRandom;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder allowRandomUnchoosable(@Nullable Boolean allowRandomUnchoosable) {
            this.allowRandomUnchoosable = allowRandomUnchoosable;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder excludeRandom(Collection<ResourceLocation> excludeRandom) {
            this.excludeRandom.addAll(excludeRandom);
            return this;
        }

        @Deprecated(forRemoval = true, since = "2.3.3.1")
        @Contract("_ -> this")
        public PartialLayer.Builder replaceExcludeRandom(Boolean replaceExcludeRandom) {
            return this.replaceExcludeRandom(replaceExcludeRandom.booleanValue());
        }

        @Contract("_ -> this")
        public PartialLayer.Builder replaceExcludeRandom(boolean replaceExcludeRandom) {
            this.replaceExcludeRandom = replaceExcludeRandom;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder defaultOrigin(@Nullable ResourceLocation defaultOrigin) {
            this.defaultOrigin = defaultOrigin;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder autoChoose(@Nullable Boolean autoChoose) {
            this.autoChoose = autoChoose;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder hidden(@Nullable Boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder loadingPriority(int loadingPriority) {
            this.loadingPriority = loadingPriority;
            return this;
        }

        @Contract("_ -> this")
        public PartialLayer.Builder title(@Nullable PartialGuiTitle title) {
            this.title = title;
            return this;
        }

        public PartialLayer build() {
            return new PartialLayer(this.order, this.origins, this.enabled, this.replace, this.name, this.missingName, this.missingDescription, this.allowRandom, this.allowRandomUnchoosable, this.excludeRandom, this.replaceExcludeRandom, this.defaultOrigin, this.autoChoose, this.loadingPriority, this.hidden, this.title);
        }
    }

    public static enum Serializer implements JsonSerializer<PartialLayer>, JsonDeserializer<PartialLayer> {

        INSTANCE;

        public PartialLayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            PartialLayer.Builder builder = PartialLayer.builder();
            JsonObject root = GsonHelper.convertToJsonObject(json, "root element");
            JsonUtils.getOptional(root, "order", GsonHelper::m_13927_).ifPresent(builder::order);
            JsonUtils.getOptional(root, "enabled", GsonHelper::m_13912_).ifPresent(builder::enabled);
            JsonUtils.getOptional(root, "replace", GsonHelper::m_13912_).ifPresent(builder::replace);
            JsonUtils.getOptional(root, "name", GsonHelper::m_13906_).ifPresent(builder::name);
            JsonUtils.getOptional(root, "missing_name", GsonHelper::m_13906_).ifPresent(builder::missingName);
            JsonUtils.getOptional(root, "missing_description", GsonHelper::m_13906_).ifPresent(builder::missingDescription);
            JsonUtils.getOptional(root, "allow_random", GsonHelper::m_13912_).ifPresent(builder::allowRandom);
            JsonUtils.getOptional(root, "allow_random_unchoosable", GsonHelper::m_13912_).ifPresent(builder::allowRandomUnchoosable);
            JsonUtils.getOptional(root, "default_origin", GsonHelper::m_13906_).map(JsonUtils.rethrow(ResourceLocation::new, "\"default_origin\" isn't a valid identifier")).ifPresent(builder::defaultOrigin);
            JsonUtils.getOptional(root, "auto_choose", GsonHelper::m_13912_).ifPresent(builder::autoChoose);
            JsonUtils.getOptional(root, "replace_exclude_random", GsonHelper::m_13912_).ifPresent(x -> builder.replaceExcludeRandom(x.booleanValue()));
            JsonUtils.getOptional(root, "hidden", GsonHelper::m_13912_).ifPresent(builder::hidden);
            JsonUtils.getOptional(root, "loading_priority", GsonHelper::m_13927_).ifPresent(builder::loadingPriority);
            JsonUtils.getOptional(root, "gui_title", (jsonObject, s) -> (PartialGuiTitle) context.deserialize(jsonObject.get(s), PartialGuiTitle.class)).ifPresent(builder::title);
            builder.origins(JsonUtils.<ConditionedOrigin>getOptionalList(root, "origins", (jsonElement, s) -> (ConditionedOrigin) context.deserialize(jsonElement, ConditionedOrigin.class)));
            builder.excludeRandom(JsonUtils.getIdentifierList(root, "exclude_random"));
            return builder.build();
        }

        public JsonElement serialize(PartialLayer src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            if (src.order() != null) {
                root.addProperty("order", src.order());
            }
            if (src.enabled() != null) {
                root.addProperty("enabled", src.enabled());
            }
            root.addProperty("replace", src.replace());
            if (src.name() != null) {
                root.addProperty("name", src.name());
            }
            if (src.missingName() != null) {
                root.addProperty("missing_name", src.missingName());
            }
            if (src.allowRandom() != null) {
                root.addProperty("allow_random", src.allowRandom());
            }
            if (src.allowRandomUnchoosable() != null) {
                root.addProperty("allow_random_unchoosable", src.allowRandomUnchoosable());
            }
            if (src.defaultOrigin() != null) {
                root.addProperty("default_origin", src.defaultOrigin().toString());
            }
            if (src.autoChoose() != null) {
                root.addProperty("auto_choose", src.autoChoose());
            }
            root.addProperty("replace_exclude_random", src.replaceExcludeRandom());
            if (src.hidden() != null) {
                root.addProperty("hidden", src.hidden());
            }
            JsonArray origins = (JsonArray) src.origins().stream().map(x -> context.serialize(x, ConditionedOrigin.class)).collect(JsonUtils.toJsonArray());
            if (origins.size() > 0) {
                root.add("origins", origins);
            }
            JsonArray excludeRandom = (JsonArray) src.excludeRandom().stream().map(x -> new JsonPrimitive(x.toString())).collect(JsonUtils.toJsonArray());
            if (excludeRandom.size() > 0) {
                root.add("exclude_random", excludeRandom);
            }
            if (src.loadingPriority() != 0) {
                root.addProperty("loading_priority", src.loadingPriority());
            }
            root.add("gui_title", context.serialize(src.title(), PartialGuiTitle.class));
            return root;
        }
    }
}