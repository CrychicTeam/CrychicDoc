package de.keksuccino.fancymenu.util.resource.resources.text;

import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.TextFileType;
import de.keksuccino.fancymenu.util.resource.ResourceHandler;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextResourceHandler extends ResourceHandler<IText, TextFileType> {

    public static final TextResourceHandler INSTANCE = new TextResourceHandler();

    @NotNull
    @Override
    public List<TextFileType> getAllowedFileTypes() {
        return FileTypes.getAllTextFileTypes();
    }

    @Nullable
    public TextFileType getFallbackFileType() {
        return FileTypes.TXT_TEXT;
    }
}