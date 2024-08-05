package de.keksuccino.fancymenu.util.file.type;

import com.google.common.io.Files;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.file.GameDirectoryUtils;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileType<T> {

    protected final List<String> extensions = new ArrayList();

    @NotNull
    protected FileMediaType mediaType;

    @Nullable
    protected String mimeType;

    @NotNull
    protected FileCodec<T> codec;

    protected boolean allowLocation = true;

    protected boolean allowLocal = true;

    protected boolean allowWeb = true;

    @Nullable
    protected Component customDisplayName;

    protected FileType(@NotNull FileCodec<T> codec) {
        this.mediaType = FileMediaType.OTHER;
        this.codec = codec;
    }

    protected FileType(@NotNull FileCodec<T> codec, @Nullable String mimeType, @NotNull FileMediaType mediaType, @NotNull String... extensions) {
        Arrays.asList(extensions).forEach(this::addExtension);
        this.mediaType = mediaType;
        this.mimeType = mimeType;
        this.codec = codec;
    }

    public boolean isFileTypeLocation(@NotNull ResourceLocation location) {
        return this.extensions.contains(Files.getFileExtension(location.getPath()).toLowerCase());
    }

    public boolean isFileTypeLocal(@NotNull File file) {
        return this.extensions.contains(Files.getFileExtension(file.getPath()).toLowerCase());
    }

    public boolean isFileTypeWeb(@NotNull String fileUrl) {
        if (!TextValidators.BASIC_URL_TEXT_VALIDATOR.get(fileUrl)) {
            return false;
        } else {
            if (fileUrl.endsWith("/")) {
                fileUrl = fileUrl.substring(0, fileUrl.length() - 1);
            }
            fileUrl = fileUrl.toLowerCase();
            for (String extension : this.extensions) {
                if (fileUrl.endsWith("." + extension)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isFileTypeWebAdvanced(@NotNull String fileUrl) {
        return this.mimeType == null ? true : Objects.equals(WebUtils.getMimeType(fileUrl), this.mimeType);
    }

    public boolean isFileType(@NotNull ResourceSource resourceSource, boolean doAdvancedWebChecks) {
        Objects.requireNonNull(resourceSource);
        try {
            if (resourceSource.getSourceType() == ResourceSourceType.LOCATION) {
                ResourceLocation loc = ResourceLocation.tryParse(resourceSource.getSourceWithoutPrefix());
                if (loc != null) {
                    return this.isFileTypeLocation(loc);
                }
            }
            if (resourceSource.getSourceType() == ResourceSourceType.LOCAL) {
                return this.isFileTypeLocal(new File(GameDirectoryUtils.getAbsoluteGameDirectoryPath(resourceSource.getSourceWithoutPrefix())));
            }
            if (resourceSource.getSourceType() == ResourceSourceType.WEB) {
                if (this.isFileTypeWeb(resourceSource.getSourceWithoutPrefix())) {
                    return true;
                }
                if (doAdvancedWebChecks && this.isFileTypeWebAdvanced(resourceSource.getSourceWithoutPrefix())) {
                    return true;
                }
            }
        } catch (Exception var4) {
        }
        return false;
    }

    @NotNull
    public List<String> getExtensions() {
        return new ArrayList(this.extensions);
    }

    public FileType<T> addExtension(@NotNull String extension) {
        extension = ((String) Objects.requireNonNull(extension)).toLowerCase().replace(".", "").replace(" ", "");
        if (this.extensions.contains(extension)) {
            return this;
        } else {
            this.extensions.add(extension);
            return this;
        }
    }

    public FileType<T> removeExtension(@NotNull String extension) {
        extension = ((String) Objects.requireNonNull(extension)).toLowerCase().replace(".", "").replace(" ", "");
        while (this.extensions.contains(extension)) {
            this.extensions.remove(extension);
        }
        return this;
    }

    @NotNull
    public FileMediaType getMediaType() {
        return this.mediaType;
    }

    @Nullable
    public String getMimeType() {
        return this.mimeType;
    }

    @NotNull
    public FileCodec<T> getCodec() {
        return this.codec;
    }

    public FileType<T> setCodec(@NotNull FileCodec<T> codec) {
        this.codec = (FileCodec<T>) Objects.requireNonNull(codec);
        return this;
    }

    public boolean isLocationAllowed() {
        return this.allowLocation;
    }

    public FileType<T> setLocationAllowed(boolean allowLocation) {
        this.allowLocation = allowLocation;
        return this;
    }

    public boolean isLocalAllowed() {
        return this.allowLocal;
    }

    public FileType<T> setLocalAllowed(boolean allowLocal) {
        this.allowLocal = allowLocal;
        return this;
    }

    public boolean isWebAllowed() {
        return this.allowWeb;
    }

    public FileType<T> setWebAllowed(boolean allowWeb) {
        this.allowWeb = allowWeb;
        return this;
    }

    @NotNull
    public Component getDisplayName() {
        if (this.customDisplayName != null) {
            return this.customDisplayName;
        } else {
            return !this.extensions.isEmpty() ? Component.literal(((String) this.extensions.get(0)).toUpperCase()) : Component.empty();
        }
    }

    public FileType<T> setCustomDisplayName(@Nullable Component name) {
        this.customDisplayName = name;
        return this;
    }

    public String toString() {
        return "FileType{extensions=" + this.extensions + ", mediaType=" + this.mediaType + ", mimeType='" + this.mimeType + "', allowLocation=" + this.allowLocation + ", allowLocal=" + this.allowLocal + ", allowWeb=" + this.allowWeb + "}";
    }
}