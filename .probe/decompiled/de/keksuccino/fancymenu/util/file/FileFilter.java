package de.keksuccino.fancymenu.util.file;

import de.keksuccino.fancymenu.util.file.type.types.AudioFileType;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.file.type.types.ImageFileType;
import de.keksuccino.fancymenu.util.file.type.types.TextFileType;
import de.keksuccino.fancymenu.util.file.type.types.VideoFileType;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import java.io.File;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FileFilter {

    FileFilter RESOURCE_NAME_FILTER = file -> {
        String name = GameDirectoryUtils.getPathWithoutGameDirectory(file.getAbsolutePath()).replace("/", "").replace("\\", "");
        return CharacterFilter.buildResourceNameFilter().isAllowedText(name);
    };

    FileFilter IMAGE_FILE_FILTER = file -> {
        for (ImageFileType type : FileTypes.getAllImageFileTypes()) {
            if (type.isFileTypeLocal(file)) {
                return true;
            }
        }
        return false;
    };

    FileFilter AUDIO_FILE_FILTER = file -> {
        for (AudioFileType type : FileTypes.getAllAudioFileTypes()) {
            if (type.isFileTypeLocal(file)) {
                return true;
            }
        }
        return false;
    };

    FileFilter VIDEO_FILE_FILTER = file -> {
        for (VideoFileType type : FileTypes.getAllVideoFileTypes()) {
            if (type.isFileTypeLocal(file)) {
                return true;
            }
        }
        return false;
    };

    FileFilter TEXT_FILE_FILTER = file -> {
        for (TextFileType type : FileTypes.getAllTextFileTypes()) {
            if (type.isFileTypeLocal(file)) {
                return true;
            }
        }
        return false;
    };

    boolean checkFile(@NotNull File var1);
}