package net.minecraft.server.packs.repository;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public abstract class BuiltInPackSource implements RepositorySource {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String VANILLA_ID = "vanilla";

    private final PackType packType;

    private final VanillaPackResources vanillaPack;

    private final ResourceLocation packDir;

    public BuiltInPackSource(PackType packType0, VanillaPackResources vanillaPackResources1, ResourceLocation resourceLocation2) {
        this.packType = packType0;
        this.vanillaPack = vanillaPackResources1;
        this.packDir = resourceLocation2;
    }

    @Override
    public void loadPacks(Consumer<Pack> consumerPack0) {
        Pack $$1 = this.createVanillaPack(this.vanillaPack);
        if ($$1 != null) {
            consumerPack0.accept($$1);
        }
        this.listBundledPacks(consumerPack0);
    }

    @Nullable
    protected abstract Pack createVanillaPack(PackResources var1);

    protected abstract Component getPackTitle(String var1);

    public VanillaPackResources getVanillaPack() {
        return this.vanillaPack;
    }

    private void listBundledPacks(Consumer<Pack> consumerPack0) {
        Map<String, Function<String, Pack>> $$1 = new HashMap();
        this.populatePackList($$1::put);
        $$1.forEach((p_250371_, p_250946_) -> {
            Pack $$3 = (Pack) p_250946_.apply(p_250371_);
            if ($$3 != null) {
                consumerPack0.accept($$3);
            }
        });
    }

    protected void populatePackList(BiConsumer<String, Function<String, Pack>> biConsumerStringFunctionStringPack0) {
        this.vanillaPack.listRawPaths(this.packType, this.packDir, p_250248_ -> this.discoverPacksInPath(p_250248_, biConsumerStringFunctionStringPack0));
    }

    protected void discoverPacksInPath(@Nullable Path path0, BiConsumer<String, Function<String, Pack>> biConsumerStringFunctionStringPack1) {
        if (path0 != null && Files.isDirectory(path0, new LinkOption[0])) {
            try {
                FolderRepositorySource.discoverPacks(path0, true, (p_252012_, p_249772_) -> biConsumerStringFunctionStringPack1.accept(pathToId(p_252012_), (Function) p_250601_ -> this.createBuiltinPack(p_250601_, p_249772_, this.getPackTitle(p_250601_))));
            } catch (IOException var4) {
                LOGGER.warn("Failed to discover packs in {}", path0, var4);
            }
        }
    }

    private static String pathToId(Path path0) {
        return StringUtils.removeEnd(path0.getFileName().toString(), ".zip");
    }

    @Nullable
    protected abstract Pack createBuiltinPack(String var1, Pack.ResourcesSupplier var2, Component var3);
}