package net.minecraft.data.structures;

import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class SnbtToNbt implements DataProvider {

    @Nullable
    private static final Path DUMP_SNBT_TO = null;

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PackOutput output;

    private final Iterable<Path> inputFolders;

    private final List<SnbtToNbt.Filter> filters = Lists.newArrayList();

    public SnbtToNbt(PackOutput packOutput0, Iterable<Path> iterablePath1) {
        this.output = packOutput0;
        this.inputFolders = iterablePath1;
    }

    public SnbtToNbt addFilter(SnbtToNbt.Filter snbtToNbtFilter0) {
        this.filters.add(snbtToNbtFilter0);
        return this;
    }

    private CompoundTag applyFilters(String string0, CompoundTag compoundTag1) {
        CompoundTag $$2 = compoundTag1;
        for (SnbtToNbt.Filter $$3 : this.filters) {
            $$2 = $$3.apply(string0, $$2);
        }
        return $$2;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        Path $$1 = this.output.getOutputFolder();
        List<CompletableFuture<?>> $$2 = Lists.newArrayList();
        for (Path $$3 : this.inputFolders) {
            $$2.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Stream<Path> $$3x = Files.walk($$3);
                    CompletableFuture var5x;
                    try {
                        var5x = CompletableFuture.allOf((CompletableFuture[]) $$3x.filter(p_126464_ -> p_126464_.toString().endsWith(".snbt")).map(p_253432_ -> CompletableFuture.runAsync(() -> {
                            SnbtToNbt.TaskResult $$4 = this.readStructure(p_253432_, this.getName($$3, p_253432_));
                            this.storeStructureIfChanged(cachedOutput0, $$4, $$1);
                        }, Util.backgroundExecutor())).toArray(CompletableFuture[]::new));
                    } catch (Throwable var8) {
                        if ($$3x != null) {
                            try {
                                $$3x.close();
                            } catch (Throwable var7) {
                                var8.addSuppressed(var7);
                            }
                        }
                        throw var8;
                    }
                    if ($$3x != null) {
                        $$3x.close();
                    }
                    return var5x;
                } catch (Exception var9) {
                    throw new RuntimeException("Failed to read structure input directory, aborting", var9);
                }
            }, Util.backgroundExecutor()).thenCompose(p_253441_ -> p_253441_));
        }
        return Util.sequenceFailFast($$2);
    }

    @Override
    public final String getName() {
        return "SNBT -> NBT";
    }

    private String getName(Path path0, Path path1) {
        String $$2 = path0.relativize(path1).toString().replaceAll("\\\\", "/");
        return $$2.substring(0, $$2.length() - ".snbt".length());
    }

    private SnbtToNbt.TaskResult readStructure(Path path0, String string1) {
        try {
            BufferedReader $$2 = Files.newBufferedReader(path0);
            SnbtToNbt.TaskResult var11;
            try {
                String $$3 = IOUtils.toString($$2);
                CompoundTag $$4 = this.applyFilters(string1, NbtUtils.snbtToStructure($$3));
                ByteArrayOutputStream $$5 = new ByteArrayOutputStream();
                HashingOutputStream $$6 = new HashingOutputStream(Hashing.sha1(), $$5);
                NbtIo.writeCompressed($$4, $$6);
                byte[] $$7 = $$5.toByteArray();
                HashCode $$8 = $$6.hash();
                String $$9;
                if (DUMP_SNBT_TO != null) {
                    $$9 = NbtUtils.structureToSnbt($$4);
                } else {
                    $$9 = null;
                }
                var11 = new SnbtToNbt.TaskResult(string1, $$7, $$9, $$8);
            } catch (Throwable var13) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }
                throw var13;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var11;
        } catch (Throwable var14) {
            throw new SnbtToNbt.StructureConversionException(path0, var14);
        }
    }

    private void storeStructureIfChanged(CachedOutput cachedOutput0, SnbtToNbt.TaskResult snbtToNbtTaskResult1, Path path2) {
        if (snbtToNbtTaskResult1.snbtPayload != null) {
            Path $$3 = DUMP_SNBT_TO.resolve(snbtToNbtTaskResult1.name + ".snbt");
            try {
                NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, $$3, snbtToNbtTaskResult1.snbtPayload);
            } catch (IOException var7) {
                LOGGER.error("Couldn't write structure SNBT {} at {}", new Object[] { snbtToNbtTaskResult1.name, $$3, var7 });
            }
        }
        Path $$5 = path2.resolve(snbtToNbtTaskResult1.name + ".nbt");
        try {
            cachedOutput0.writeIfNeeded($$5, snbtToNbtTaskResult1.payload, snbtToNbtTaskResult1.hash);
        } catch (IOException var6) {
            LOGGER.error("Couldn't write structure {} at {}", new Object[] { snbtToNbtTaskResult1.name, $$5, var6 });
        }
    }

    @FunctionalInterface
    public interface Filter {

        CompoundTag apply(String var1, CompoundTag var2);
    }

    static class StructureConversionException extends RuntimeException {

        public StructureConversionException(Path path0, Throwable throwable1) {
            super(path0.toAbsolutePath().toString(), throwable1);
        }
    }

    static record TaskResult(String f_126482_, byte[] f_126483_, @Nullable String f_126484_, HashCode f_126485_) {

        private final String name;

        private final byte[] payload;

        @Nullable
        private final String snbtPayload;

        private final HashCode hash;

        TaskResult(String f_126482_, byte[] f_126483_, @Nullable String f_126484_, HashCode f_126485_) {
            this.name = f_126482_;
            this.payload = f_126483_;
            this.snbtPayload = f_126484_;
            this.hash = f_126485_;
        }
    }
}