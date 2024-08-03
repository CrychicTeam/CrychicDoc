package net.minecraft.world.level.chunk.storage;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import javax.annotation.Nullable;
import net.minecraft.util.FastBufferedInputStream;

public class RegionFileVersion {

    private static final Int2ObjectMap<RegionFileVersion> VERSIONS = new Int2ObjectOpenHashMap();

    public static final RegionFileVersion VERSION_GZIP = register(new RegionFileVersion(1, p_63767_ -> new FastBufferedInputStream(new GZIPInputStream(p_63767_)), p_63769_ -> new BufferedOutputStream(new GZIPOutputStream(p_63769_))));

    public static final RegionFileVersion VERSION_DEFLATE = register(new RegionFileVersion(2, p_196964_ -> new FastBufferedInputStream(new InflaterInputStream(p_196964_)), p_196966_ -> new BufferedOutputStream(new DeflaterOutputStream(p_196966_))));

    public static final RegionFileVersion VERSION_NONE = register(new RegionFileVersion(3, p_196960_ -> p_196960_, p_196962_ -> p_196962_));

    private final int id;

    private final RegionFileVersion.StreamWrapper<InputStream> inputWrapper;

    private final RegionFileVersion.StreamWrapper<OutputStream> outputWrapper;

    private RegionFileVersion(int int0, RegionFileVersion.StreamWrapper<InputStream> regionFileVersionStreamWrapperInputStream1, RegionFileVersion.StreamWrapper<OutputStream> regionFileVersionStreamWrapperOutputStream2) {
        this.id = int0;
        this.inputWrapper = regionFileVersionStreamWrapperInputStream1;
        this.outputWrapper = regionFileVersionStreamWrapperOutputStream2;
    }

    private static RegionFileVersion register(RegionFileVersion regionFileVersion0) {
        VERSIONS.put(regionFileVersion0.id, regionFileVersion0);
        return regionFileVersion0;
    }

    @Nullable
    public static RegionFileVersion fromId(int int0) {
        return (RegionFileVersion) VERSIONS.get(int0);
    }

    public static boolean isValidVersion(int int0) {
        return VERSIONS.containsKey(int0);
    }

    public int getId() {
        return this.id;
    }

    public OutputStream wrap(OutputStream outputStream0) throws IOException {
        return this.outputWrapper.wrap(outputStream0);
    }

    public InputStream wrap(InputStream inputStream0) throws IOException {
        return this.inputWrapper.wrap(inputStream0);
    }

    @FunctionalInterface
    interface StreamWrapper<O> {

        O wrap(O var1) throws IOException;
    }
}