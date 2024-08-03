package de.keksuccino.fancymenu.util.resource.resources.texture;

import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.ImageFileType;
import de.keksuccino.fancymenu.util.resource.ResourceHandler;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImageResourceHandler extends ResourceHandler<ITexture, ImageFileType> {

    public static final ImageResourceHandler INSTANCE = new ImageResourceHandler();

    @NotNull
    @Override
    public List<ImageFileType> getAllowedFileTypes() {
        return FileTypes.getAllImageFileTypes();
    }

    @Nullable
    public ImageFileType getFallbackFileType() {
        return FileTypes.PNG_IMAGE;
    }
}