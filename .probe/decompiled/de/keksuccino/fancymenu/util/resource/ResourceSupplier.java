package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.file.type.FileMediaType;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.resource.resources.video.IVideo;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceSupplier<R extends Resource> {

    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    protected String source;

    @NotNull
    protected Class<R> resourceType;

    @NotNull
    FileMediaType mediaType;

    @Nullable
    protected R current;

    @Nullable
    protected String lastGetterSource;

    @Nullable
    protected Consumer<R> onUpdateCurrent = null;

    protected boolean empty = false;

    @NotNull
    public static <R extends Resource> ResourceSupplier<R> empty(@NotNull Class<R> resourceType, @NotNull FileMediaType mediaType) {
        ResourceSupplier<R> supplier = new ResourceSupplier<>(resourceType, mediaType, "");
        supplier.empty = true;
        return supplier;
    }

    @NotNull
    public static ResourceSupplier<ITexture> image(@NotNull String source) {
        return new ResourceSupplier<>(ITexture.class, FileMediaType.IMAGE, source);
    }

    @NotNull
    public static ResourceSupplier<IAudio> audio(@NotNull String source) {
        return new ResourceSupplier<IAudio>(IAudio.class, FileMediaType.AUDIO, source).setOnUpdateResourceTask(PlayableResource::stop);
    }

    @NotNull
    public static ResourceSupplier<IVideo> video(@NotNull String source) {
        return new ResourceSupplier<>(IVideo.class, FileMediaType.VIDEO, source);
    }

    @NotNull
    public static ResourceSupplier<IText> text(@NotNull String source) {
        return new ResourceSupplier<>(IText.class, FileMediaType.TEXT, source);
    }

    public ResourceSupplier(@NotNull Class<R> resourceType, @NotNull FileMediaType mediaType, @NotNull String source) {
        this.source = (String) Objects.requireNonNull(source);
        this.resourceType = (Class<R>) Objects.requireNonNull(resourceType);
        this.mediaType = (FileMediaType) Objects.requireNonNull(mediaType);
    }

    @Nullable
    public R get() {
        if (this.empty) {
            return null;
        } else {
            if (this.current != null && this.current.isClosed()) {
                this.current = null;
            }
            String getterSource = PlaceholderParser.replacePlaceholders(this.source, false);
            if (!getterSource.equals(this.lastGetterSource)) {
                if (this.onUpdateCurrent != null && this.current != null) {
                    this.onUpdateCurrent.accept(this.current);
                }
                this.current = null;
            }
            this.lastGetterSource = getterSource;
            if (this.current == null) {
                ResourceSource resourceSource = ResourceSource.of(getterSource);
                try {
                    ResourceHandler<?, ?> handler = this.getResourceHandler();
                    if (handler != null) {
                        this.current = (R) handler.get(resourceSource);
                    }
                } catch (Exception var4) {
                    LOGGER.error("[FANCYMENU] ResourceSupplier failed to get resource: " + resourceSource + " (" + this.source + ")", var4);
                }
            }
            return this.current;
        }
    }

    @Nullable
    public ResourceHandler<?, ?> getResourceHandler() {
        if (this.mediaType == FileMediaType.IMAGE) {
            return ResourceHandlers.getImageHandler();
        } else if (this.mediaType == FileMediaType.AUDIO) {
            return ResourceHandlers.getAudioHandler();
        } else if (this.mediaType == FileMediaType.VIDEO) {
            return ResourceHandlers.getVideoHandler();
        } else {
            return this.mediaType == FileMediaType.TEXT ? ResourceHandlers.getTextHandler() : null;
        }
    }

    public void forRenderable(@NotNull BiConsumer<R, ResourceLocation> task) {
        R resource = this.get();
        if (resource instanceof RenderableResource r) {
            ResourceLocation loc = r.getResourceLocation();
            if (loc != null) {
                task.accept(resource, loc);
            }
        }
    }

    @NotNull
    public Class<R> getResourceType() {
        return this.resourceType;
    }

    @NotNull
    public FileMediaType getMediaType() {
        return this.mediaType;
    }

    @NotNull
    public ResourceSourceType getSourceType() {
        return this.empty ? ResourceSourceType.LOCAL : ResourceSourceType.getSourceTypeOf(PlaceholderParser.replacePlaceholders(this.source, false));
    }

    @NotNull
    public String getSourceWithoutPrefix() {
        return this.empty ? "" : ResourceSourceType.getWithoutSourcePrefix(this.source);
    }

    @NotNull
    public String getSourceWithPrefix() {
        if (this.empty) {
            return "";
        } else {
            return ResourceSourceType.hasSourcePrefix(this.source) ? this.source : this.getSourceType().getSourcePrefix() + this.source;
        }
    }

    public void setSource(@NotNull String source) {
        if (!this.empty) {
            this.source = (String) Objects.requireNonNull(source);
        }
    }

    public ResourceSupplier<R> setOnUpdateResourceTask(@Nullable Consumer<R> oldResourceConsumer) {
        this.onUpdateCurrent = oldResourceConsumer;
        return this;
    }

    public boolean isEmpty() {
        return this.empty;
    }
}