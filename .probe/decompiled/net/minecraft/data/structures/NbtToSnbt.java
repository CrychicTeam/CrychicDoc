package net.minecraft.data.structures;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import org.slf4j.Logger;

public class NbtToSnbt implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Iterable<Path> inputFolders;

    private final PackOutput output;

    public NbtToSnbt(PackOutput packOutput0, Collection<Path> collectionPath1) {
        this.inputFolders = collectionPath1;
        this.output = packOutput0;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        Path $$1 = this.output.getOutputFolder();
        List<CompletableFuture<?>> $$2 = new ArrayList();
        for (Path $$3 : this.inputFolders) {
            $$2.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Stream<Path> $$3x = Files.walk($$3);
                    CompletableFuture var4;
                    try {
                        var4 = CompletableFuture.allOf((CompletableFuture[]) $$3x.filter(p_126430_ -> p_126430_.toString().endsWith(".nbt")).map(p_253418_ -> CompletableFuture.runAsync(() -> convertStructure(cachedOutput0, p_253418_, getName($$3, p_253418_), $$1), Util.ioPool())).toArray(CompletableFuture[]::new));
                    } catch (Throwable var7) {
                        if ($$3x != null) {
                            try {
                                $$3x.close();
                            } catch (Throwable var6) {
                                var7.addSuppressed(var6);
                            }
                        }
                        throw var7;
                    }
                    if ($$3x != null) {
                        $$3x.close();
                    }
                    return var4;
                } catch (IOException var8) {
                    LOGGER.error("Failed to read structure input directory", var8);
                    return CompletableFuture.completedFuture(null);
                }
            }, Util.backgroundExecutor()).thenCompose(p_253420_ -> p_253420_));
        }
        return CompletableFuture.allOf((CompletableFuture[]) $$2.toArray(CompletableFuture[]::new));
    }

    @Override
    public final String getName() {
        return "NBT -> SNBT";
    }

    private static String getName(Path path0, Path path1) {
        String $$2 = path0.relativize(path1).toString().replaceAll("\\\\", "/");
        return $$2.substring(0, $$2.length() - ".nbt".length());
    }

    @Nullable
    public static Path convertStructure(CachedOutput cachedOutput0, Path path1, String string2, Path path3) {
        try {
            InputStream $$4 = Files.newInputStream(path1);
            Path var6;
            try {
                Path $$5 = path3.resolve(string2 + ".snbt");
                writeSnbt(cachedOutput0, $$5, NbtUtils.structureToSnbt(NbtIo.readCompressed($$4)));
                LOGGER.info("Converted {} from NBT to SNBT", string2);
                var6 = $$5;
            } catch (Throwable var8) {
                if ($$4 != null) {
                    try {
                        $$4.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if ($$4 != null) {
                $$4.close();
            }
            return var6;
        } catch (IOException var9) {
            LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", new Object[] { string2, path1, var9 });
            return null;
        }
    }

    public static void writeSnbt(CachedOutput cachedOutput0, Path path1, String string2) throws IOException {
        ByteArrayOutputStream $$3 = new ByteArrayOutputStream();
        HashingOutputStream $$4 = new HashingOutputStream(Hashing.sha1(), $$3);
        $$4.write(string2.getBytes(StandardCharsets.UTF_8));
        $$4.write(10);
        cachedOutput0.writeIfNeeded(path1, $$3.toByteArray(), $$4.hash());
    }
}