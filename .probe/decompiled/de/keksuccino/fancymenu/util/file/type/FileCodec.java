package de.keksuccino.fancymenu.util.file.type;

import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileCodec<T> {

    @NotNull
    public static <T> FileCodec<T> empty(@NotNull Class<T> type) {
        return new FileCodec<T>() {

            @Nullable
            @Override
            public T read(@NotNull InputStream in) {
                return null;
            }

            @Nullable
            @Override
            public T readLocation(@NotNull ResourceLocation location) {
                return null;
            }

            @Nullable
            @Override
            public T readLocal(@NotNull File file) {
                return null;
            }

            @Nullable
            @Override
            public T readWeb(@NotNull String fileUrl) {
                return null;
            }
        };
    }

    @NotNull
    public static <T> FileCodec<T> generic(@NotNull Class<T> type, @NotNull ConsumingSupplier<InputStream, T> streamReader) {
        ConsumingSupplier<ResourceLocation, T> locationReader = consumes -> {
            try {
                InputStream in = Minecraft.getInstance().getResourceManager().m_215595_(consumes);
                return streamReader.get(in);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        };
        return basic(type, streamReader, locationReader);
    }

    @NotNull
    public static <T> FileCodec<T> basic(@NotNull Class<T> type, @NotNull final ConsumingSupplier<InputStream, T> streamReader, @NotNull final ConsumingSupplier<ResourceLocation, T> locationReader) {
        Objects.requireNonNull(type);
        return new FileCodec<T>() {

            @Nullable
            @Override
            public T read(@NotNull InputStream in) {
                Objects.requireNonNull(in);
                return streamReader.get(in);
            }

            @Nullable
            @Override
            public T readLocation(@NotNull ResourceLocation location) {
                Objects.requireNonNull(location);
                return locationReader.get(location);
            }

            @Nullable
            @Override
            public T readLocal(@NotNull File file) {
                Objects.requireNonNull(file);
                try {
                    return streamReader.get(new FileInputStream(file));
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }
            }

            @Nullable
            @Override
            public T readWeb(@NotNull String fileUrl) {
                Objects.requireNonNull(fileUrl);
                InputStream in = WebUtils.openResourceStream(fileUrl);
                return in != null ? streamReader.get(in) : null;
            }
        };
    }

    @NotNull
    public static <T> FileCodec<T> basicWithLocal(@NotNull Class<T> type, @NotNull final ConsumingSupplier<InputStream, T> streamReader, @NotNull final ConsumingSupplier<ResourceLocation, T> locationReader, @NotNull final ConsumingSupplier<File, T> fileReader) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(streamReader);
        Objects.requireNonNull(fileReader);
        return new FileCodec<T>() {

            @Nullable
            @Override
            public T read(@NotNull InputStream in) {
                Objects.requireNonNull(in);
                return streamReader.get(in);
            }

            @Nullable
            @Override
            public T readLocation(@NotNull ResourceLocation location) {
                Objects.requireNonNull(location);
                return locationReader.get(location);
            }

            @Nullable
            @Override
            public T readLocal(@NotNull File file) {
                Objects.requireNonNull(file);
                return fileReader.get(file);
            }

            @Nullable
            @Override
            public T readWeb(@NotNull String fileUrl) {
                Objects.requireNonNull(fileUrl);
                InputStream in = WebUtils.openResourceStream(fileUrl);
                return in != null ? streamReader.get(in) : null;
            }
        };
    }

    @NotNull
    public static <T> FileCodec<T> basicWithWeb(@NotNull Class<T> type, @NotNull final ConsumingSupplier<InputStream, T> streamReader, @NotNull final ConsumingSupplier<ResourceLocation, T> locationReader, @NotNull final ConsumingSupplier<String, T> urlReader) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(streamReader);
        Objects.requireNonNull(urlReader);
        return new FileCodec<T>() {

            @Nullable
            @Override
            public T read(@NotNull InputStream in) {
                Objects.requireNonNull(in);
                return streamReader.get(in);
            }

            @Nullable
            @Override
            public T readLocation(@NotNull ResourceLocation location) {
                Objects.requireNonNull(location);
                return locationReader.get(location);
            }

            @Nullable
            @Override
            public T readLocal(@NotNull File file) {
                Objects.requireNonNull(file);
                try {
                    return streamReader.get(new FileInputStream(file));
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }
            }

            @Nullable
            @Override
            public T readWeb(@NotNull String fileUrl) {
                Objects.requireNonNull(fileUrl);
                return urlReader.get(fileUrl);
            }
        };
    }

    @NotNull
    public static <T> FileCodec<T> advanced(@NotNull Class<T> type, @NotNull final ConsumingSupplier<InputStream, T> streamReader, @NotNull final ConsumingSupplier<ResourceLocation, T> locationReader, @NotNull final ConsumingSupplier<File, T> fileReader, @NotNull final ConsumingSupplier<String, T> urlReader) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(streamReader);
        Objects.requireNonNull(fileReader);
        Objects.requireNonNull(urlReader);
        return new FileCodec<T>() {

            @Nullable
            @Override
            public T read(@NotNull InputStream in) {
                Objects.requireNonNull(in);
                return streamReader.get(in);
            }

            @Nullable
            @Override
            public T readLocation(@NotNull ResourceLocation location) {
                Objects.requireNonNull(location);
                return locationReader.get(location);
            }

            @Nullable
            @Override
            public T readLocal(@NotNull File file) {
                Objects.requireNonNull(file);
                return fileReader.get(file);
            }

            @Nullable
            @Override
            public T readWeb(@NotNull String fileUrl) {
                Objects.requireNonNull(fileUrl);
                return urlReader.get(fileUrl);
            }
        };
    }

    @Nullable
    public abstract T read(@NotNull InputStream var1);

    @Nullable
    public abstract T readLocation(@NotNull ResourceLocation var1);

    @Nullable
    public abstract T readLocal(@NotNull File var1);

    @Nullable
    public abstract T readWeb(@NotNull String var1);

    @Nullable
    public T read(@NotNull ResourceSource resourceSource) {
        Objects.requireNonNull(resourceSource);
        try {
            if (resourceSource.getSourceType() == ResourceSourceType.LOCATION) {
                ResourceLocation loc = ResourceLocation.tryParse(resourceSource.getSourceWithoutPrefix());
                return loc != null ? this.readLocation(loc) : null;
            }
            if (resourceSource.getSourceType() == ResourceSourceType.LOCAL) {
                return this.readLocal(new File(resourceSource.getSourceWithoutPrefix()));
            }
            if (resourceSource.getSourceType() == ResourceSourceType.WEB) {
                return this.readWeb(resourceSource.getSourceWithoutPrefix());
            }
        } catch (Exception var3) {
        }
        return null;
    }
}