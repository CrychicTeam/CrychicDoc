package net.minecraft.server.packs;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class FilePackResources extends AbstractPackResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Splitter SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);

    private final File file;

    @Nullable
    private ZipFile zipFile;

    private boolean failedToLoad;

    public FilePackResources(String string0, File file1, boolean boolean2) {
        super(string0, boolean2);
        this.file = file1;
    }

    @Nullable
    private ZipFile getOrCreateZipFile() {
        if (this.failedToLoad) {
            return null;
        } else {
            if (this.zipFile == null) {
                try {
                    this.zipFile = new ZipFile(this.file);
                } catch (IOException var2) {
                    LOGGER.error("Failed to open pack {}", this.file, var2);
                    this.failedToLoad = true;
                    return null;
                }
            }
            return this.zipFile;
        }
    }

    private static String getPathFromLocation(PackType packType0, ResourceLocation resourceLocation1) {
        return String.format(Locale.ROOT, "%s/%s/%s", packType0.getDirectory(), resourceLocation1.getNamespace(), resourceLocation1.getPath());
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... string0) {
        return this.getResource(String.join("/", string0));
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType packType0, ResourceLocation resourceLocation1) {
        return this.getResource(getPathFromLocation(packType0, resourceLocation1));
    }

    @Nullable
    private IoSupplier<InputStream> getResource(String string0) {
        ZipFile $$1 = this.getOrCreateZipFile();
        if ($$1 == null) {
            return null;
        } else {
            ZipEntry $$2 = $$1.getEntry(string0);
            return $$2 == null ? null : IoSupplier.create($$1, $$2);
        }
    }

    @Override
    public Set<String> getNamespaces(PackType packType0) {
        ZipFile $$1 = this.getOrCreateZipFile();
        if ($$1 == null) {
            return Set.of();
        } else {
            Enumeration<? extends ZipEntry> $$2 = $$1.entries();
            Set<String> $$3 = Sets.newHashSet();
            while ($$2.hasMoreElements()) {
                ZipEntry $$4 = (ZipEntry) $$2.nextElement();
                String $$5 = $$4.getName();
                if ($$5.startsWith(packType0.getDirectory() + "/")) {
                    List<String> $$6 = Lists.newArrayList(SPLITTER.split($$5));
                    if ($$6.size() > 1) {
                        String $$7 = (String) $$6.get(1);
                        if ($$7.equals($$7.toLowerCase(Locale.ROOT))) {
                            $$3.add($$7);
                        } else {
                            LOGGER.warn("Ignored non-lowercase namespace: {} in {}", $$7, this.file);
                        }
                    }
                }
            }
            return $$3;
        }
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void close() {
        if (this.zipFile != null) {
            IOUtils.closeQuietly(this.zipFile);
            this.zipFile = null;
        }
    }

    @Override
    public void listResources(PackType packType0, String string1, String string2, PackResources.ResourceOutput packResourcesResourceOutput3) {
        ZipFile $$4 = this.getOrCreateZipFile();
        if ($$4 != null) {
            Enumeration<? extends ZipEntry> $$5 = $$4.entries();
            String $$6 = packType0.getDirectory() + "/" + string1 + "/";
            String $$7 = $$6 + string2 + "/";
            while ($$5.hasMoreElements()) {
                ZipEntry $$8 = (ZipEntry) $$5.nextElement();
                if (!$$8.isDirectory()) {
                    String $$9 = $$8.getName();
                    if ($$9.startsWith($$7)) {
                        String $$10 = $$9.substring($$6.length());
                        ResourceLocation $$11 = ResourceLocation.tryBuild(string1, $$10);
                        if ($$11 != null) {
                            packResourcesResourceOutput3.accept($$11, IoSupplier.create($$4, $$8));
                        } else {
                            LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", string1, $$10);
                        }
                    }
                }
            }
        }
    }
}