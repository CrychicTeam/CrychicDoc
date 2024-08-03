package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.util.file.GameDirectoryUtils;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceSource {

    protected ResourceSourceType sourceType;

    protected String resourceSourceWithoutPrefix;

    @NotNull
    public static ResourceSource of(@NotNull String resourceSource, @Nullable ResourceSourceType sourceType) {
        Objects.requireNonNull(resourceSource);
        resourceSource = resourceSource.trim();
        ResourceSource source = new ResourceSource();
        source.sourceType = sourceType != null ? sourceType : ResourceSourceType.getSourceTypeOf(resourceSource);
        source.resourceSourceWithoutPrefix = ResourceSourceType.getWithoutSourcePrefix(resourceSource);
        if (source.sourceType == ResourceSourceType.LOCAL) {
            source.resourceSourceWithoutPrefix = GameDirectoryUtils.getAbsoluteGameDirectoryPath(source.resourceSourceWithoutPrefix);
        }
        return source;
    }

    @NotNull
    public static ResourceSource of(@NotNull String resourceSource) {
        return of(resourceSource, null);
    }

    protected ResourceSource() {
    }

    @NotNull
    public ResourceSourceType getSourceType() {
        return this.sourceType;
    }

    @NotNull
    public String getSerializationSource() {
        String source = this.resourceSourceWithoutPrefix;
        if (this.sourceType == ResourceSourceType.LOCAL) {
            source = GameDirectoryUtils.getPathWithoutGameDirectory(source);
        }
        return this.sourceType.getSourcePrefix() + source;
    }

    @NotNull
    public String getSourceWithPrefix() {
        return this.sourceType.getSourcePrefix() + this.resourceSourceWithoutPrefix;
    }

    @NotNull
    public String getSourceWithoutPrefix() {
        return this.resourceSourceWithoutPrefix;
    }

    public String toString() {
        return "ResourceSource{sourceType=" + this.sourceType + ", source='" + this.resourceSourceWithoutPrefix + "'}";
    }
}