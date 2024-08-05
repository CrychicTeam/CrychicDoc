package de.keksuccino.fancymenu.util.resource.resources.video;

import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.VideoFileType;
import de.keksuccino.fancymenu.util.resource.ResourceHandler;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VideoResourceHandler extends ResourceHandler<IVideo, VideoFileType> {

    public static final VideoResourceHandler INSTANCE = new VideoResourceHandler();

    @NotNull
    @Override
    public List<VideoFileType> getAllowedFileTypes() {
        return FileTypes.getAllVideoFileTypes();
    }

    @Nullable
    public VideoFileType getFallbackFileType() {
        return null;
    }
}