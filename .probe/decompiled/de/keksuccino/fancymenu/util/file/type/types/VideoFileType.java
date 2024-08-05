package de.keksuccino.fancymenu.util.file.type.types;

import de.keksuccino.fancymenu.util.file.type.FileCodec;
import de.keksuccino.fancymenu.util.file.type.FileMediaType;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.resource.resources.video.IVideo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VideoFileType extends FileType<IVideo> {

    public VideoFileType(@NotNull FileCodec<IVideo> codec, @Nullable String mimeType, @NotNull String... extensions) {
        super(codec, mimeType, FileMediaType.VIDEO, extensions);
    }

    public VideoFileType addExtension(@NotNull String extension) {
        return (VideoFileType) super.addExtension(extension);
    }

    public VideoFileType removeExtension(@NotNull String extension) {
        return (VideoFileType) super.removeExtension(extension);
    }

    public VideoFileType setCodec(@NotNull FileCodec<IVideo> codec) {
        return (VideoFileType) super.setCodec(codec);
    }

    public VideoFileType setLocationAllowed(boolean allowLocation) {
        return (VideoFileType) super.setLocationAllowed(allowLocation);
    }

    public VideoFileType setLocalAllowed(boolean allowLocal) {
        return (VideoFileType) super.setLocalAllowed(allowLocal);
    }

    public VideoFileType setWebAllowed(boolean allowWeb) {
        return (VideoFileType) super.setWebAllowed(allowWeb);
    }

    public VideoFileType setCustomDisplayName(@Nullable Component name) {
        return (VideoFileType) super.setCustomDisplayName(name);
    }
}