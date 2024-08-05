package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.file.GameDirectoryUtils;
import de.keksuccino.fancymenu.util.file.type.FileType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ResourceHandler<R extends Resource, F extends FileType<R>> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Map<String, R> resources = new HashMap();

    protected List<String> failedSources = new ArrayList();

    @Nullable
    public R get(@NotNull String resourceSource) {
        Objects.requireNonNull(resourceSource);
        return this.get(ResourceSource.of(resourceSource));
    }

    @Nullable
    public R get(@NotNull ResourceSource resourceSource) {
        Objects.requireNonNull(resourceSource);
        try {
            R registered = this.getFromMapAndClearClosed(resourceSource.getSourceWithPrefix());
            if (registered != null) {
                return registered;
            } else if (this.getFailedSourcesList().contains(resourceSource.getSourceWithPrefix())) {
                return null;
            } else {
                F fileType = null;
                for (F type : this.getAllowedFileTypes()) {
                    if (type.isFileType(resourceSource, false)) {
                        fileType = type;
                        break;
                    }
                }
                if (fileType == null && resourceSource.getSourceType() == ResourceSourceType.WEB) {
                    for (F typex : this.getAllowedFileTypes()) {
                        if (typex.isFileTypeWebAdvanced(resourceSource.getSourceWithoutPrefix())) {
                            fileType = typex;
                            break;
                        }
                    }
                }
                if (fileType == null) {
                    fileType = this.getFallbackFileType();
                }
                if (fileType == null) {
                    LOGGER.error("[FANCYMENU] Failed to register resource! Unsupported file type or failed to identify file type: " + resourceSource + " (RESOURCE HANDLER: " + this.getClass() + ")");
                    this.addToFailedSources(resourceSource);
                    return null;
                } else if (resourceSource.getSourceType() == ResourceSourceType.WEB) {
                    if (!fileType.isWebAllowed()) {
                        LOGGER.error("[FANCYMENU] Failed to register web resource! File type does not support web sources: " + fileType + " (Source: " + resourceSource + ") (RESOURCE HANDLER: " + this.getClass() + ")");
                        this.addToFailedSources(resourceSource);
                        return null;
                    } else {
                        return this.putAndReturn(fileType.getCodec().readWeb(resourceSource.getSourceWithoutPrefix()), resourceSource);
                    }
                } else if (resourceSource.getSourceType() == ResourceSourceType.LOCATION) {
                    if (!fileType.isLocationAllowed()) {
                        LOGGER.error("[FANCYMENU] Failed to register location resource! File type does not support location sources: " + fileType + " (Source: " + resourceSource + ") (RESOURCE HANDLER: " + this.getClass() + ")");
                        this.addToFailedSources(resourceSource);
                        return null;
                    } else {
                        ResourceLocation loc = ResourceLocation.tryParse(resourceSource.getSourceWithoutPrefix());
                        if (loc == null) {
                            LOGGER.error("[FANCYMENU] Failed to register location resource! Unable to parse ResourceLocation: " + resourceSource + " (RESOURCE HANDLER: " + this.getClass() + ")");
                            this.addToFailedSources(resourceSource);
                            return null;
                        } else {
                            return this.putAndReturn(fileType.getCodec().readLocation(loc), resourceSource);
                        }
                    }
                } else if (!fileType.isLocalAllowed()) {
                    LOGGER.error("[FANCYMENU] Failed to register local resource! File type does not support local sources: " + fileType + " (Source: " + resourceSource + ") (RESOURCE HANDLER: " + this.getClass() + ")");
                    this.addToFailedSources(resourceSource);
                    return null;
                } else {
                    return this.putAndReturn(fileType.getCodec().readLocal(new File(resourceSource.getSourceWithoutPrefix())), resourceSource);
                }
            }
        } catch (Exception var6) {
            LOGGER.error("[FANCYMENU] Failed to register resource: " + resourceSource + " (RESOURCE HANDLER: " + this.getClass() + ")", var6);
            this.addToFailedSources(resourceSource);
            return null;
        }
    }

    @Nullable
    public R getIfRegistered(@NotNull String key) {
        return (R) this.getResourceMap().get(Objects.requireNonNull(key));
    }

    public void registerIfKeyAbsent(@NotNull String key, @NotNull R resource) {
        if (!this.hasResource(key)) {
            LOGGER.debug("[FANCYMENU] Registering resource with key: " + key + " (RESOURCE HANDLER: " + this.getClass() + ")");
            this.getResourceMap().put(key, (Resource) Objects.requireNonNull(resource));
        }
    }

    public boolean hasResource(@NotNull String key) {
        return this.getResourceMap().containsKey(Objects.requireNonNull(key));
    }

    @Nullable
    protected R getFromMapAndClearClosed(@Nullable String resourceSource) {
        if (resourceSource == null) {
            return null;
        } else {
            if (this.getResourceMap().containsKey(resourceSource)) {
                R resource = (R) this.getResourceMap().get(resourceSource);
                if (!resource.isClosed()) {
                    return resource;
                }
                CloseableUtils.closeQuietly(resource);
                this.getResourceMap().remove(resourceSource);
            }
            return null;
        }
    }

    @Nullable
    protected R putAndReturn(@Nullable R resource, @NotNull ResourceSource resourceSource) {
        Objects.requireNonNull(resourceSource);
        if (resource != null) {
            LOGGER.debug("[FANCYMENU] Registering resource with source: " + resourceSource + " (RESOURCE HANDLER: " + this.getClass() + ")");
            this.getResourceMap().put(resourceSource.getSourceWithPrefix(), resource);
        } else if (!this.getFailedSourcesList().contains(resourceSource.getSourceWithPrefix())) {
            this.getFailedSourcesList().add(resourceSource.getSourceWithPrefix());
            LOGGER.error("[FANCYMENU] Failed to register resource! Resource was NULL: " + resourceSource + " (RESOURCE HANDLER: " + this.getClass() + ")");
        }
        return resource;
    }

    protected void addToFailedSources(@NotNull ResourceSource resourceSource) {
        if (!this.getFailedSourcesList().contains(resourceSource.getSourceWithPrefix())) {
            this.getFailedSourcesList().add(resourceSource.getSourceWithPrefix());
        }
    }

    @NotNull
    protected Map<String, R> getResourceMap() {
        return this.resources;
    }

    @NotNull
    protected List<String> getFailedSourcesList() {
        return this.failedSources;
    }

    @NotNull
    public abstract List<F> getAllowedFileTypes();

    @Nullable
    public abstract F getFallbackFileType();

    public void release(@NotNull String key, boolean isKeyResourceSource) {
        Objects.requireNonNull(key);
        if (isKeyResourceSource) {
            String finalKey = key;
            this.getFailedSourcesList().removeIf(s -> s.equals(finalKey));
            ResourceSourceType sourceType = ResourceSourceType.getSourceTypeOf(key);
            if (sourceType == ResourceSourceType.LOCAL) {
                key = GameDirectoryUtils.getAbsoluteGameDirectoryPath(ResourceSourceType.getWithoutSourcePrefix(key));
                key = sourceType.getSourcePrefix() + key;
            }
        }
        R resource = (R) this.getResourceMap().get(key);
        if (resource != null) {
            CloseableUtils.closeQuietly(resource);
        }
        this.getResourceMap().remove(key);
    }

    public void release(@NotNull R resource) {
        String key = null;
        for (Entry<String, R> m : this.getResourceMap().entrySet()) {
            if (m.getValue() == resource) {
                key = (String) m.getKey();
                break;
            }
        }
        CloseableUtils.closeQuietly(resource);
        if (key != null) {
            this.getResourceMap().remove(key);
        }
    }

    public void releaseAll() {
        this.getResourceMap().values().forEach(CloseableUtils::closeQuietly);
        this.getResourceMap().clear();
        this.getFailedSourcesList().clear();
    }
}