package com.craisinlord.integrated_api.datagen;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.google.common.hash.Hashing;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerUpper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class StructureNbtUpdater implements DataProvider {

    private final String basePath;

    private final String modid;

    private final PackOutput output;

    private final MultiPackResourceManager resources;

    public StructureNbtUpdater(String basePath, String modid, ExistingFileHelper helper, PackOutput output) {
        this.basePath = basePath;
        this.modid = modid;
        this.output = output;
        try {
            Field serverData = ExistingFileHelper.class.getDeclaredField("serverData");
            serverData.setAccessible(true);
            this.resources = (MultiPackResourceManager) serverData.get(helper);
        } catch (IllegalAccessException | NoSuchFieldException var6) {
            throw new RuntimeException(var6);
        }
    }

    @NotNull
    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        try {
            for (Entry<ResourceLocation, Resource> entry : this.resources.listResources(this.basePath, $ -> true).entrySet()) {
                if (((ResourceLocation) entry.getKey()).getNamespace().equals(this.modid)) {
                    this.process((ResourceLocation) entry.getKey(), (Resource) entry.getValue(), cache);
                }
            }
            return CompletableFuture.completedFuture(null);
        } catch (IOException var4) {
            return CompletableFuture.failedFuture(var4);
        }
    }

    private void process(ResourceLocation loc, Resource resource, CachedOutput cache) throws IOException {
        CompoundTag inputNBT = NbtIo.readCompressed(resource.open());
        CompoundTag converted = updateNBT(inputNBT);
        if (!converted.equals(inputNBT)) {
            IntegratedAPI.LOGGER.info("Found outdated NBT file: {}", loc);
            Class<? extends DataFixer> fixerClass = DataFixers.getDataFixer().getClass();
            if (!fixerClass.equals(DataFixerUpper.class)) {
                throw new RuntimeException("Structures are not up to date, but unknown data fixer is in use: " + fixerClass.getName());
            }
            this.writeNBTTo(loc, converted, cache);
        }
    }

    private void writeNBTTo(ResourceLocation loc, CompoundTag data, CachedOutput cache) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        NbtIo.writeCompressed(data, bytearrayoutputstream);
        byte[] bytes = bytearrayoutputstream.toByteArray();
        Path outputPath = this.output.getOutputFolder().resolve("data/" + loc.getNamespace() + "/" + loc.getPath());
        cache.writeIfNeeded(outputPath, bytes, Hashing.sha1().hashBytes(bytes));
    }

    private static CompoundTag updateNBT(CompoundTag nbt) {
        CompoundTag updatedNBT = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), nbt, nbt.getInt("DataVersion"));
        StructureTemplate template = new StructureTemplate();
        template.load(BuiltInRegistries.BLOCK.m_255303_(), updatedNBT);
        return template.save(new CompoundTag());
    }

    @Nonnull
    @Override
    public String getName() {
        return "Update structure files in " + this.basePath;
    }
}