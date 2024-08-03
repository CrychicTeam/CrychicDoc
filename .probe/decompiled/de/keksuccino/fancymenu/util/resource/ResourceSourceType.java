package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.input.TextValidators;
import java.util.Objects;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ResourceSourceType implements LocalizedCycleEnum<ResourceSourceType> {

    LOCATION("location"), LOCAL("local"), WEB("web");

    private final String name;

    private ResourceSourceType(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getSourcePrefix() {
        return "[source:" + this.name + "]";
    }

    public static boolean hasSourcePrefix(@NotNull String resourceSource) {
        if (resourceSource.startsWith(LOCATION.getSourcePrefix())) {
            return true;
        } else {
            return resourceSource.startsWith(LOCAL.getSourcePrefix()) ? true : resourceSource.startsWith(WEB.getSourcePrefix());
        }
    }

    @NotNull
    public static String getWithoutSourcePrefix(@NotNull String resourceSource) {
        resourceSource = resourceSource.replace(LOCATION.getSourcePrefix(), "");
        resourceSource = resourceSource.replace(LOCAL.getSourcePrefix(), "");
        return resourceSource.replace(WEB.getSourcePrefix(), "");
    }

    @NotNull
    public static ResourceSourceType getSourceTypeOf(@NotNull String resourceSource) {
        Objects.requireNonNull(resourceSource);
        if (resourceSource.startsWith(LOCAL.getSourcePrefix())) {
            return LOCAL;
        } else if (resourceSource.startsWith(WEB.getSourcePrefix())) {
            return WEB;
        } else if (resourceSource.startsWith(LOCATION.getSourcePrefix())) {
            return LOCATION;
        } else if (TextValidators.BASIC_URL_TEXT_VALIDATOR.get(getWithoutSourcePrefix(resourceSource))) {
            return WEB;
        } else {
            return resourceSource.contains(":") && ResourceLocation.tryParse(getWithoutSourcePrefix(resourceSource)) != null ? LOCATION : LOCAL;
        }
    }

    @NotNull
    @Override
    public Style getValueComponentStyle() {
        return (Style) WARNING_TEXT_STYLE.get();
    }

    @NotNull
    @Override
    public String getLocalizationKeyBase() {
        return "fancymenu.resources.source_type";
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    public ResourceSourceType[] getValues() {
        return values();
    }

    @Nullable
    public ResourceSourceType getByNameInternal(@NotNull String name) {
        return getByName(name);
    }

    @Nullable
    public static ResourceSourceType getByName(@NotNull String name) {
        for (ResourceSourceType t : values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }
}