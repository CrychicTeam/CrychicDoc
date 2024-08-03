package net.minecraft.nbt;

import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.util.FastBufferedInputStream;

public class NbtIo {

    public static CompoundTag readCompressed(File file0) throws IOException {
        InputStream $$1 = new FileInputStream(file0);
        CompoundTag var2;
        try {
            var2 = readCompressed($$1);
        } catch (Throwable var5) {
            try {
                $$1.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }
            throw var5;
        }
        $$1.close();
        return var2;
    }

    private static DataInputStream createDecompressorStream(InputStream inputStream0) throws IOException {
        return new DataInputStream(new FastBufferedInputStream(new GZIPInputStream(inputStream0)));
    }

    public static CompoundTag readCompressed(InputStream inputStream0) throws IOException {
        DataInputStream $$1 = createDecompressorStream(inputStream0);
        CompoundTag var2;
        try {
            var2 = read($$1, NbtAccounter.UNLIMITED);
        } catch (Throwable var5) {
            if ($$1 != null) {
                try {
                    $$1.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if ($$1 != null) {
            $$1.close();
        }
        return var2;
    }

    public static void parseCompressed(File file0, StreamTagVisitor streamTagVisitor1) throws IOException {
        InputStream $$2 = new FileInputStream(file0);
        try {
            parseCompressed($$2, streamTagVisitor1);
        } catch (Throwable var6) {
            try {
                $$2.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        $$2.close();
    }

    public static void parseCompressed(InputStream inputStream0, StreamTagVisitor streamTagVisitor1) throws IOException {
        DataInputStream $$2 = createDecompressorStream(inputStream0);
        try {
            parse($$2, streamTagVisitor1);
        } catch (Throwable var6) {
            if ($$2 != null) {
                try {
                    $$2.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if ($$2 != null) {
            $$2.close();
        }
    }

    public static void writeCompressed(CompoundTag compoundTag0, File file1) throws IOException {
        OutputStream $$2 = new FileOutputStream(file1);
        try {
            writeCompressed(compoundTag0, $$2);
        } catch (Throwable var6) {
            try {
                $$2.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        $$2.close();
    }

    public static void writeCompressed(CompoundTag compoundTag0, OutputStream outputStream1) throws IOException {
        DataOutputStream $$2 = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream1)));
        try {
            write(compoundTag0, $$2);
        } catch (Throwable var6) {
            try {
                $$2.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        $$2.close();
    }

    public static void write(CompoundTag compoundTag0, File file1) throws IOException {
        FileOutputStream $$2 = new FileOutputStream(file1);
        try {
            DataOutputStream $$3 = new DataOutputStream($$2);
            try {
                write(compoundTag0, $$3);
            } catch (Throwable var8) {
                try {
                    $$3.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            $$3.close();
        } catch (Throwable var9) {
            try {
                $$2.close();
            } catch (Throwable var6) {
                var9.addSuppressed(var6);
            }
            throw var9;
        }
        $$2.close();
    }

    @Nullable
    public static CompoundTag read(File file0) throws IOException {
        if (!file0.exists()) {
            return null;
        } else {
            FileInputStream $$1 = new FileInputStream(file0);
            CompoundTag var3;
            try {
                DataInputStream $$2 = new DataInputStream($$1);
                try {
                    var3 = read($$2, NbtAccounter.UNLIMITED);
                } catch (Throwable var7) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                $$2.close();
            } catch (Throwable var8) {
                try {
                    $$1.close();
                } catch (Throwable var5) {
                    var8.addSuppressed(var5);
                }
                throw var8;
            }
            $$1.close();
            return var3;
        }
    }

    public static CompoundTag read(DataInput dataInput0) throws IOException {
        return read(dataInput0, NbtAccounter.UNLIMITED);
    }

    public static CompoundTag read(DataInput dataInput0, NbtAccounter nbtAccounter1) throws IOException {
        Tag $$2 = readUnnamedTag(dataInput0, 0, nbtAccounter1);
        if ($$2 instanceof CompoundTag) {
            return (CompoundTag) $$2;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(CompoundTag compoundTag0, DataOutput dataOutput1) throws IOException {
        writeUnnamedTag(compoundTag0, dataOutput1);
    }

    public static void parse(DataInput dataInput0, StreamTagVisitor streamTagVisitor1) throws IOException {
        TagType<?> $$2 = TagTypes.getType(dataInput0.readByte());
        if ($$2 == EndTag.TYPE) {
            if (streamTagVisitor1.visitRootEntry(EndTag.TYPE) == StreamTagVisitor.ValueResult.CONTINUE) {
                streamTagVisitor1.visitEnd();
            }
        } else {
            switch(streamTagVisitor1.visitRootEntry($$2)) {
                case HALT:
                default:
                    break;
                case BREAK:
                    StringTag.skipString(dataInput0);
                    $$2.skip(dataInput0);
                    break;
                case CONTINUE:
                    StringTag.skipString(dataInput0);
                    $$2.parse(dataInput0, streamTagVisitor1);
            }
        }
    }

    public static void writeUnnamedTag(Tag tag0, DataOutput dataOutput1) throws IOException {
        dataOutput1.writeByte(tag0.getId());
        if (tag0.getId() != 0) {
            dataOutput1.writeUTF("");
            tag0.write(dataOutput1);
        }
    }

    private static Tag readUnnamedTag(DataInput dataInput0, int int1, NbtAccounter nbtAccounter2) throws IOException {
        byte $$3 = dataInput0.readByte();
        if ($$3 == 0) {
            return EndTag.INSTANCE;
        } else {
            StringTag.skipString(dataInput0);
            try {
                return TagTypes.getType($$3).load(dataInput0, int1, nbtAccounter2);
            } catch (IOException var7) {
                CrashReport $$5 = CrashReport.forThrowable(var7, "Loading NBT data");
                CrashReportCategory $$6 = $$5.addCategory("NBT Tag");
                $$6.setDetail("Tag type", $$3);
                throw new ReportedException($$5);
            }
        }
    }
}