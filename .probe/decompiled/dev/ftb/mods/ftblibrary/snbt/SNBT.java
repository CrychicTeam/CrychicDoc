package dev.ftb.mods.ftblibrary.snbt;

import dev.ftb.mods.ftblibrary.FTBLibrary;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public class SNBT {

    private static boolean shouldSortKeysOnWrite = false;

    public static SNBTCompoundTag readLines(List<String> lines) {
        return SNBTParser.read(lines);
    }

    public static SNBTCompoundTag tryRead(Path path) throws IOException {
        return readLines(Files.readAllLines(path, StandardCharsets.UTF_8));
    }

    public static void tryWrite(Path path, CompoundTag tag) throws IOException {
        if (Files.notExists(path.getParent(), new LinkOption[0])) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, writeLines(tag));
    }

    @Nullable
    public static SNBTCompoundTag read(Path path) {
        if (!Files.notExists(path, new LinkOption[0]) && !Files.isDirectory(path, new LinkOption[0]) && Files.isReadable(path)) {
            try {
                return readLines(Files.readAllLines(path, StandardCharsets.UTF_8));
            } catch (SNBTSyntaxException var2) {
                FTBLibrary.LOGGER.error("Failed to read " + path + ": " + var2.getMessage());
                return null;
            } catch (Exception var3) {
                FTBLibrary.LOGGER.error("Failed to read " + path + ": " + var3);
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static List<String> writeLines(CompoundTag nbt) {
        SNBTBuilder builder = new SNBTBuilder();
        SNBTTagProperties rootProperties = nbt instanceof SNBTCompoundTag ? ((SNBTCompoundTag) nbt).getProperties("") : SNBTTagProperties.DEFAULT;
        if (!rootProperties.comment.isEmpty()) {
            for (String s : rootProperties.comment.split("\n")) {
                builder.print("# ");
                builder.print(s);
                builder.println();
            }
            builder.println();
        }
        append(builder, nbt);
        builder.println();
        return builder.lines;
    }

    public static boolean write(Path path, CompoundTag nbt) {
        try {
            if (Files.notExists(path.getParent(), new LinkOption[0])) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, writeLines(nbt));
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    private static void append(SNBTBuilder builder, @Nullable Tag nbt) {
        if (nbt == null || nbt instanceof EndTag) {
            builder.print("null");
        } else if (nbt instanceof CompoundTag compound) {
            SNBTCompoundTag snbtCompoundTag = compound instanceof SNBTCompoundTag s ? s : null;
            if (compound.isEmpty()) {
                builder.print("{ }");
                return;
            }
            if (snbtCompoundTag != null && snbtCompoundTag.singleLine) {
                builder.singleLine++;
            }
            boolean singleLine = builder.singleLine > 0;
            builder.print("{");
            if (singleLine) {
                builder.print(" ");
            } else {
                builder.println();
                builder.push();
            }
            int index = 0;
            for (String key : shouldSortKeysOnWrite ? compound.getAllKeys().stream().sorted().toList() : compound.getAllKeys()) {
                index++;
                SNBTTagProperties properties = snbtCompoundTag == null ? SNBTTagProperties.DEFAULT : snbtCompoundTag.getProperties(key);
                if (!properties.comment.isEmpty()) {
                    if (singleLine) {
                        throw new IllegalStateException("Can't have singleLine enabled and a comment at the same time!");
                    }
                    if (index != 1) {
                        builder.println();
                    }
                    for (String sx : properties.comment.split("\n")) {
                        builder.print("# ");
                        builder.print(sx);
                        builder.println();
                    }
                }
                builder.print(SNBTUtils.handleEscape(key));
                builder.print(": ");
                if (properties.valueType == 1) {
                    builder.print("false");
                } else if (properties.valueType == 2) {
                    builder.print("true");
                } else {
                    if (properties.singleLine) {
                        builder.singleLine++;
                    }
                    append(builder, compound.get(key));
                    if (properties.singleLine) {
                        builder.singleLine--;
                    }
                }
                if (singleLine && index != compound.size()) {
                    builder.print(",");
                }
                if (singleLine) {
                    builder.print(" ");
                } else {
                    builder.println();
                }
            }
            if (!singleLine) {
                builder.pop();
            }
            builder.print("}");
            if (snbtCompoundTag != null && snbtCompoundTag.singleLine) {
                builder.singleLine--;
            }
        } else if (nbt instanceof CollectionTag) {
            if (nbt instanceof ByteArrayTag) {
                appendCollection(builder, (CollectionTag<? extends Tag>) nbt, "B;");
            } else if (nbt instanceof IntArrayTag) {
                appendCollection(builder, (CollectionTag<? extends Tag>) nbt, "I;");
            } else if (nbt instanceof LongArrayTag) {
                appendCollection(builder, (CollectionTag<? extends Tag>) nbt, "L;");
            } else {
                appendCollection(builder, (CollectionTag<? extends Tag>) nbt, "");
            }
        } else if (nbt instanceof StringTag) {
            builder.print(SNBTUtils.quoteAndEscape(nbt.getAsString()));
        } else {
            builder.print(nbt.toString());
        }
    }

    private static void appendCollection(SNBTBuilder builder, CollectionTag<? extends Tag> nbt, String opening) {
        if (nbt.isEmpty()) {
            builder.print("[");
            builder.print(opening);
            builder.print(" ]");
        } else if (nbt.size() == 1) {
            builder.print("[");
            builder.print(opening);
            append(builder, (Tag) nbt.get(0));
            builder.print("]");
        } else {
            boolean singleLine = builder.singleLine > 0;
            builder.print("[");
            builder.print(opening);
            if (singleLine) {
                builder.print(" ");
            } else {
                builder.println();
                builder.push();
            }
            int index = 0;
            for (Tag value : nbt) {
                index++;
                append(builder, value);
                if (singleLine && index != nbt.size()) {
                    builder.print(",");
                }
                if (singleLine) {
                    builder.print(" ");
                } else {
                    builder.println();
                }
            }
            if (!singleLine) {
                builder.pop();
            }
            builder.print("]");
        }
    }

    public static boolean shouldSortKeysOnWrite() {
        return shouldSortKeysOnWrite;
    }

    public static boolean setShouldSortKeysOnWrite(boolean shouldSortKeysOnWrite) {
        boolean prev = SNBT.shouldSortKeysOnWrite;
        SNBT.shouldSortKeysOnWrite = shouldSortKeysOnWrite;
        return prev;
    }
}