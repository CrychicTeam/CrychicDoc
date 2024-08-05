package de.keksuccino.fancymenu.util.resource.preload;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.panorama.LocalTexturePanoramaRenderer;
import de.keksuccino.fancymenu.customization.panorama.PanoramaHandler;
import de.keksuccino.fancymenu.customization.slideshow.ExternalTextureSlideshowRenderer;
import de.keksuccino.fancymenu.customization.slideshow.SlideshowHandler;
import de.keksuccino.fancymenu.util.resource.Resource;
import de.keksuccino.fancymenu.util.resource.ResourceHandler;
import de.keksuccino.fancymenu.util.resource.ResourceHandlers;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcePreLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SOURCE_SEPARATOR = "%!source_end!%";

    public static final String CUBIC_PANORAMA_SOURCE_PREFIX = "[cubic_panorama]";

    public static final String SLIDESHOW_SOURCE_PREFIX = "[slideshow]";

    public static void preLoadAll(long waitForCompletedMillis) {
        LOGGER.info("[FANCYMENU] Pre-loading resources..");
        for (ResourceSource source : getRegisteredResourceSources(null)) {
            try {
                if (source instanceof ResourcePreLoader.CubicPanoramaSource s) {
                    preLoadCubicPanorama(s, waitForCompletedMillis);
                } else if (source instanceof ResourcePreLoader.SlideshowSource s) {
                    preLoadSlideshow(s, waitForCompletedMillis);
                } else {
                    ResourceHandler<?, ?> handler = ResourceHandlers.findHandlerForSource(source, true);
                    if (handler != null) {
                        Resource resource = handler.get(source);
                        if (resource != null) {
                            if (waitForCompletedMillis > 0L) {
                                resource.waitForLoadingCompletedOrFailed(waitForCompletedMillis);
                                if (resource.isLoadingFailed()) {
                                    LOGGER.error("[FANCYMENU] Failed to pre-load resource! Loading failed for: " + source.getSourceWithPrefix());
                                }
                                if (!resource.isLoadingFailed() && !resource.isLoadingCompleted()) {
                                    LOGGER.error("[FANCYMENU] Pre-loading resource took too long! Will not finish pre-loading resource: " + source.getSourceWithPrefix(), new TimeoutException("Resource pre-loading timed out! Took longer than millis: " + waitForCompletedMillis));
                                }
                            }
                        } else {
                            LOGGER.error("[FANCYMENU] Failed to pre-load resource! Resource returned by ResourceHandler was NULL for: " + source.getSourceWithPrefix());
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to pre-load resource! No ResourceHandler found for: " + source.getSourceWithPrefix());
                    }
                }
            } catch (Exception var8) {
                LOGGER.error("[FANCYMENU] Error while pre-loading resource: " + source.getSourceWithPrefix(), var8);
            }
        }
    }

    protected static void preLoadSlideshow(@NotNull ResourcePreLoader.SlideshowSource s, long waitForCompletedMillis) {
        ExternalTextureSlideshowRenderer slideshow = SlideshowHandler.getSlideshow(s.getSlideshowName());
        if (slideshow != null) {
            if (waitForCompletedMillis > 0L) {
                for (ResourceSupplier<ITexture> image : slideshow.images) {
                    ITexture resource = image.get();
                    if (resource != null) {
                        resource.waitForLoadingCompletedOrFailed(waitForCompletedMillis);
                        if (resource.isLoadingFailed()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load image of slideshow!", new IllegalStateException("ITexture loading failed for: " + s.getSlideshowName()));
                        }
                        if (!resource.isLoadingFailed() && !resource.isLoadingCompleted()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load image of slideshow!", new TimeoutException("ITexture loading took too long for: " + s.getSlideshowName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to pre-load image of slideshow!", new NullPointerException("ITexture of image was NULL for: " + s.getSlideshowName()));
                    }
                }
                if (slideshow.overlayTexture != null) {
                    ITexture resource = slideshow.overlayTexture.get();
                    if (resource != null) {
                        resource.waitForLoadingCompletedOrFailed(waitForCompletedMillis);
                        if (resource.isLoadingFailed()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of slideshow!", new IllegalStateException("ITexture loading failed for: " + s.getSlideshowName()));
                        }
                        if (!resource.isLoadingFailed() && !resource.isLoadingCompleted()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of slideshow!", new TimeoutException("ITexture loading took too long for: " + s.getSlideshowName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of slideshow!", new NullPointerException("ITexture was NULL for: " + s.getSlideshowName()));
                    }
                }
            }
        } else {
            LOGGER.error("[FANCYMENU] Failed to pre-load slideshow!", new NullPointerException("Slideshow not found: " + s.getSlideshowName()));
        }
    }

    protected static void preLoadCubicPanorama(@NotNull ResourcePreLoader.CubicPanoramaSource s, long waitForCompletedMillis) {
        LocalTexturePanoramaRenderer panorama = PanoramaHandler.getPanorama(s.getPanoramaName());
        if (panorama != null) {
            if (waitForCompletedMillis > 0L) {
                for (ResourceSupplier<ITexture> image : panorama.panoramaImageSuppliers) {
                    ITexture resource = image.get();
                    if (resource != null) {
                        resource.waitForLoadingCompletedOrFailed(waitForCompletedMillis);
                        if (resource.isLoadingFailed()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load image of cubic panorama!", new IllegalStateException("ITexture loading failed for: " + s.getPanoramaName()));
                        }
                        if (!resource.isLoadingFailed() && !resource.isLoadingCompleted()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load image of cubic panorama!", new TimeoutException("ITexture loading took too long for: " + s.getPanoramaName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to pre-load image of cubic panorama!", new NullPointerException("ITexture of image was NULL for: " + s.getPanoramaName()));
                    }
                }
                if (panorama.overlayTextureSupplier != null) {
                    ITexture resource = panorama.overlayTextureSupplier.get();
                    if (resource != null) {
                        resource.waitForLoadingCompletedOrFailed(waitForCompletedMillis);
                        if (resource.isLoadingFailed()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of cubic panorama!", new IllegalStateException("ITexture loading failed for: " + s.getPanoramaName()));
                        }
                        if (!resource.isLoadingFailed() && !resource.isLoadingCompleted()) {
                            LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of cubic panorama!", new TimeoutException("ITexture loading took too long for: " + s.getPanoramaName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to pre-load overlay texture of cubic panorama!", new NullPointerException("ITexture was NULL for: " + s.getPanoramaName()));
                    }
                }
            }
        } else {
            LOGGER.error("[FANCYMENU] Failed to pre-load cubic panorama!", new NullPointerException("Panorama not found: " + s.getPanoramaName()));
        }
    }

    @NotNull
    public static List<ResourceSource> getRegisteredResourceSources(@Nullable String serialized) {
        List<ResourceSource> sources = new ArrayList();
        try {
            if (serialized == null) {
                serialized = FancyMenu.getOptions().preLoadResources.getValue();
            }
            if (serialized.trim().isEmpty()) {
                return sources;
            }
            if (!serialized.contains("%!source_end!%")) {
                sources.add(buildSourceFromString(serialized));
                return sources;
            }
            for (String s : serialized.split("%!source_end!%")) {
                if (!s.trim().isEmpty()) {
                    sources.add(buildSourceFromString(s));
                }
            }
        } catch (Exception var6) {
            LOGGER.error("[FANCYMENU] Failed to get resource sources for pre-loading!", var6);
        }
        return sources;
    }

    public static boolean isResourceSourceRegistered(@NotNull ResourceSource source, @Nullable String serialized) {
        for (ResourceSource s : getRegisteredResourceSources(serialized)) {
            if (StringUtils.equals(s.getSerializationSource(), source.getSerializationSource())) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static String addResourceSource(@NotNull ResourceSource source, @Nullable String serialized, boolean syncToConfig) {
        if (serialized == null) {
            serialized = FancyMenu.getOptions().preLoadResources.getValue();
        }
        if (!isResourceSourceRegistered(source, serialized)) {
            String ret = serialized + source.getSerializationSource() + "%!source_end!%";
            if (syncToConfig) {
                FancyMenu.getOptions().preLoadResources.setValue(ret);
            }
            return ret;
        } else {
            return serialized;
        }
    }

    @NotNull
    public static String removeResourceSource(@NotNull ResourceSource source, @Nullable String serialized, boolean syncToConfig) {
        List<ResourceSource> sources = getRegisteredResourceSources(serialized);
        sources.removeIf(resourceSource -> StringUtils.equals(resourceSource.getSerializationSource(), source.getSerializationSource()));
        StringBuilder builder = new StringBuilder();
        for (ResourceSource s2 : sources) {
            builder.append(s2.getSerializationSource()).append("%!source_end!%");
        }
        String ret = builder.toString();
        if (syncToConfig) {
            FancyMenu.getOptions().preLoadResources.setValue(ret);
        }
        return ret;
    }

    @NotNull
    public static ResourceSource buildSourceFromString(@NotNull String resourceSource) {
        if (resourceSource.startsWith("[cubic_panorama]")) {
            return new ResourcePreLoader.CubicPanoramaSource(resourceSource);
        } else {
            return (ResourceSource) (resourceSource.startsWith("[slideshow]") ? new ResourcePreLoader.SlideshowSource(resourceSource) : ResourceSource.of(resourceSource));
        }
    }

    public static class CubicPanoramaSource extends ResourceSource {

        protected CubicPanoramaSource(@NotNull String panoramaSource) {
            this.sourceType = ResourceSourceType.LOCAL;
            this.resourceSourceWithoutPrefix = panoramaSource.replace("[cubic_panorama]", "");
        }

        @NotNull
        public String getPanoramaName() {
            return this.resourceSourceWithoutPrefix;
        }

        @NotNull
        @Override
        public String getSourceWithPrefix() {
            return this.getSerializationSource();
        }

        @NotNull
        @Override
        public String getSerializationSource() {
            return "[cubic_panorama]" + this.resourceSourceWithoutPrefix;
        }
    }

    public static class SlideshowSource extends ResourceSource {

        protected SlideshowSource(@NotNull String slideshowSource) {
            this.sourceType = ResourceSourceType.LOCAL;
            this.resourceSourceWithoutPrefix = slideshowSource.replace("[slideshow]", "");
        }

        @NotNull
        public String getSlideshowName() {
            return this.resourceSourceWithoutPrefix;
        }

        @NotNull
        @Override
        public String getSourceWithPrefix() {
            return this.getSerializationSource();
        }

        @NotNull
        @Override
        public String getSerializationSource() {
            return "[slideshow]" + this.resourceSourceWithoutPrefix;
        }
    }
}