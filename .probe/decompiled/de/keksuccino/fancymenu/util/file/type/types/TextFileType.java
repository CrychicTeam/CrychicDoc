package de.keksuccino.fancymenu.util.file.type.types;

import de.keksuccino.fancymenu.util.file.type.FileCodec;
import de.keksuccino.fancymenu.util.file.type.FileMediaType;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextFileType extends FileType<IText> {

    public TextFileType(@NotNull FileCodec<IText> codec, @Nullable String mimeType, @NotNull String... extensions) {
        super(codec, mimeType, FileMediaType.TEXT, extensions);
    }

    public TextFileType addExtension(@NotNull String extension) {
        return (TextFileType) super.addExtension(extension);
    }

    public TextFileType removeExtension(@NotNull String extension) {
        return (TextFileType) super.removeExtension(extension);
    }

    public TextFileType setCodec(@NotNull FileCodec<IText> codec) {
        return (TextFileType) super.setCodec(codec);
    }

    public TextFileType setLocationAllowed(boolean allowLocation) {
        return (TextFileType) super.setLocationAllowed(allowLocation);
    }

    public TextFileType setLocalAllowed(boolean allowLocal) {
        return (TextFileType) super.setLocalAllowed(allowLocal);
    }

    public TextFileType setWebAllowed(boolean allowWeb) {
        return (TextFileType) super.setWebAllowed(allowWeb);
    }

    public TextFileType setCustomDisplayName(@Nullable Component name) {
        return (TextFileType) super.setCustomDisplayName(name);
    }
}