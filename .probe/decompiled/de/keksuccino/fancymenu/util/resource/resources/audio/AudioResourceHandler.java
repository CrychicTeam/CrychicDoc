package de.keksuccino.fancymenu.util.resource.resources.audio;

import de.keksuccino.fancymenu.util.file.type.types.AudioFileType;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.resource.ResourceHandler;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AudioResourceHandler extends ResourceHandler<IAudio, AudioFileType> {

    public static final AudioResourceHandler INSTANCE = new AudioResourceHandler();

    @NotNull
    @Override
    public List<AudioFileType> getAllowedFileTypes() {
        return FileTypes.getAllAudioFileTypes();
    }

    @Nullable
    public AudioFileType getFallbackFileType() {
        return null;
    }
}