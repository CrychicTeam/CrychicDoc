package org.violetmoon.quark.content.tweaks.client.emote;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.module.EmotesModule;

public class CustomEmoteIconResourcePack extends AbstractPackResources {

    private final List<String> verifiedNames = new ArrayList();

    private final List<String> existingNames = new ArrayList();

    public CustomEmoteIconResourcePack() {
        super("quark-emote-pack", true);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(@NotNull String... file) {
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, ResourceLocation name) {
        if (name.getPath().equals("pack.mcmeta")) {
            try {
                return IoSupplier.create(Path.of(Quark.class.getResource("/proxypack.mcmeta").toURI()));
            } catch (URISyntaxException var5) {
                throw new RuntimeException(var5);
            }
        } else if (name.getPath().equals("pack.png")) {
            try {
                return IoSupplier.create(Path.of(Quark.class.getResource("/proxypack.png").toURI()));
            } catch (URISyntaxException var6) {
                throw new RuntimeException(var6);
            }
        } else {
            File file = this.getFile(name.getPath());
            if (!file.exists()) {
                try {
                    throw new FileNotFoundException(name.getPath());
                } catch (FileNotFoundException var7) {
                    throw new RuntimeException(var7);
                }
            } else {
                return IoSupplier.create(file.toPath());
            }
        }
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String thing, @NotNull String ugh, @NotNull PackResources.ResourceOutput resourceOutput) {
    }

    @NotNull
    @Override
    public Set<String> getNamespaces(@NotNull PackType type) {
        return type == PackType.CLIENT_RESOURCES ? ImmutableSet.of("quark_custom") : ImmutableSet.of();
    }

    @NotNull
    public Collection<ResourceLocation> getResources(@NotNull PackType type, @NotNull String pathIn, @NotNull String idk, @NotNull Predicate<ResourceLocation> filter) {
        File rootPath = new File(this.getFile(idk), type.getDirectory());
        List<ResourceLocation> allResources = Lists.newArrayList();
        for (String namespace : this.getNamespaces(type)) {
            this.crawl(new File(new File(rootPath, namespace), pathIn), 32, namespace, allResources, pathIn + "/", filter);
        }
        return allResources;
    }

    private void crawl(File rootPath, int maxDepth, String namespace, List<ResourceLocation> allResources, String path, Predicate<ResourceLocation> filter) {
        File[] files = rootPath.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (maxDepth > 0) {
                        this.crawl(file, maxDepth - 1, namespace, allResources, path + file.getName() + "/", filter);
                    }
                } else if (!file.getName().endsWith(".mcmeta") && filter.test(new ResourceLocation(namespace, path + file.getName()))) {
                    try {
                        allResources.add(new ResourceLocation(namespace, path + file.getName()));
                    } catch (ResourceLocationException var13) {
                        Quark.LOG.error(var13.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void close() {
    }

    protected boolean hasResource(@NotNull String name) {
        if (!this.verifiedNames.contains(name)) {
            File file = this.getFile(name);
            if (file.exists()) {
                this.existingNames.add(name);
            }
            this.verifiedNames.add(name);
        }
        return this.existingNames.contains(name);
    }

    private File getFile(String name) {
        String filename = name.substring(name.indexOf(":") + 1) + ".png";
        filename = filename.replaceAll("(.+/)+", "");
        return new File(EmotesModule.emotesDir, filename);
    }

    public boolean isHidden() {
        return true;
    }
}