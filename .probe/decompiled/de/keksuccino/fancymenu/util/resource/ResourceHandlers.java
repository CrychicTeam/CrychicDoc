package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.file.type.types.AudioFileType;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.ImageFileType;
import de.keksuccino.fancymenu.util.file.type.types.TextFileType;
import de.keksuccino.fancymenu.util.file.type.types.VideoFileType;
import de.keksuccino.fancymenu.util.resource.resources.audio.AudioResourceHandler;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import de.keksuccino.fancymenu.util.resource.resources.text.TextResourceHandler;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.resource.resources.texture.ImageResourceHandler;
import de.keksuccino.fancymenu.util.resource.resources.video.IVideo;
import de.keksuccino.fancymenu.util.resource.resources.video.VideoResourceHandler;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceHandlers {

    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    protected static ResourceHandler<ITexture, ImageFileType> imageHandler = ImageResourceHandler.INSTANCE;

    @NotNull
    protected static ResourceHandler<IAudio, AudioFileType> audioHandler = AudioResourceHandler.INSTANCE;

    @NotNull
    protected static ResourceHandler<IVideo, VideoFileType> videoHandler = VideoResourceHandler.INSTANCE;

    @NotNull
    protected static ResourceHandler<IText, TextFileType> textHandler = TextResourceHandler.INSTANCE;

    @NotNull
    public static ResourceHandler<ITexture, ImageFileType> getImageHandler() {
        return imageHandler;
    }

    public static void setImageHandler(@NotNull ResourceHandler<ITexture, ImageFileType> imageHandler) {
        ResourceHandlers.imageHandler = (ResourceHandler<ITexture, ImageFileType>) Objects.requireNonNull(imageHandler);
    }

    @NotNull
    public static ResourceHandler<IAudio, AudioFileType> getAudioHandler() {
        return audioHandler;
    }

    public static void setAudioHandler(@NotNull ResourceHandler<IAudio, AudioFileType> audioHandler) {
        ResourceHandlers.audioHandler = (ResourceHandler<IAudio, AudioFileType>) Objects.requireNonNull(audioHandler);
    }

    @NotNull
    public static ResourceHandler<IVideo, VideoFileType> getVideoHandler() {
        return videoHandler;
    }

    public static void setVideoHandler(@NotNull ResourceHandler<IVideo, VideoFileType> videoHandler) {
        ResourceHandlers.videoHandler = (ResourceHandler<IVideo, VideoFileType>) Objects.requireNonNull(videoHandler);
    }

    @NotNull
    public static ResourceHandler<IText, TextFileType> getTextHandler() {
        return textHandler;
    }

    public static void setTextHandler(@NotNull ResourceHandler<IText, TextFileType> textHandler) {
        ResourceHandlers.textHandler = (ResourceHandler<IText, TextFileType>) Objects.requireNonNull(textHandler);
    }

    @NotNull
    public static List<ResourceHandler<?, ?>> getHandlers() {
        return ListUtils.of(imageHandler, audioHandler, videoHandler, textHandler);
    }

    @Nullable
    public static ResourceHandler<?, ?> findHandlerForSource(@NotNull ResourceSource source, boolean doAdvancedWebChecks) {
        FileType<?> type = FileTypes.getType(source, doAdvancedWebChecks);
        if (type instanceof ImageFileType) {
            return getImageHandler();
        } else if (type instanceof AudioFileType) {
            return getAudioHandler();
        } else if (type instanceof VideoFileType) {
            return getVideoHandler();
        } else {
            return type instanceof TextFileType ? getTextHandler() : null;
        }
    }

    public static void reloadAll() {
        LOGGER.info("[FANCYMENU] Reloading resources..");
        getHandlers().forEach(ResourceHandler::releaseAll);
    }
}