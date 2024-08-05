package net.minecraft.server.packs;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

public abstract class AbstractPackResources implements PackResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;

    private final boolean isBuiltin;

    protected AbstractPackResources(String string0, boolean boolean1) {
        this.name = string0;
        this.isBuiltin = boolean1;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializerT0) throws IOException {
        IoSupplier<InputStream> $$1 = this.m_8017_(new String[] { "pack.mcmeta" });
        if ($$1 == null) {
            return null;
        } else {
            InputStream $$2 = $$1.get();
            Object var4;
            try {
                var4 = getMetadataFromStream(metadataSectionSerializerT0, $$2);
            } catch (Throwable var7) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return (T) var4;
        }
    }

    @Nullable
    public static <T> T getMetadataFromStream(MetadataSectionSerializer<T> metadataSectionSerializerT0, InputStream inputStream1) {
        JsonObject $$3;
        try {
            BufferedReader $$2 = new BufferedReader(new InputStreamReader(inputStream1, StandardCharsets.UTF_8));
            try {
                $$3 = GsonHelper.parse($$2);
            } catch (Throwable var8) {
                try {
                    $$2.close();
                } catch (Throwable var6) {
                    var8.addSuppressed(var6);
                }
                throw var8;
            }
            $$2.close();
        } catch (Exception var9) {
            LOGGER.error("Couldn't load {} metadata", metadataSectionSerializerT0.getMetadataSectionName(), var9);
            return null;
        }
        if (!$$3.has(metadataSectionSerializerT0.getMetadataSectionName())) {
            return null;
        } else {
            try {
                return metadataSectionSerializerT0.fromJson(GsonHelper.getAsJsonObject($$3, metadataSectionSerializerT0.getMetadataSectionName()));
            } catch (Exception var7) {
                LOGGER.error("Couldn't load {} metadata", metadataSectionSerializerT0.getMetadataSectionName(), var7);
                return null;
            }
        }
    }

    @Override
    public String packId() {
        return this.name;
    }

    @Override
    public boolean isBuiltin() {
        return this.isBuiltin;
    }
}