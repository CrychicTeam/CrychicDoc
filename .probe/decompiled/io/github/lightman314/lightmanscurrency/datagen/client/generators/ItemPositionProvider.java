package io.github.lightman314.lightmanscurrency.datagen.client.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBiBundle;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.datagen.client.builders.ItemPositionBuilder;
import io.github.lightman314.lightmanscurrency.datagen.util.CustomPathProvider;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ItemPositionProvider implements DataProvider {

    public static final float MO = 1.0E-4F;

    protected final String name;

    protected final PackOutput output;

    final CustomPathProvider blockPathProvider;

    final CustomPathProvider positionPathProvider;

    Map<ResourceLocation, List<ResourceLocation>> blockValues = new HashMap();

    Map<ResourceLocation, ItemPositionBuilder> positionValues = new HashMap();

    protected ItemPositionProvider(@Nonnull PackOutput output, @Nonnull String modid) {
        this(output, modid, null);
    }

    protected ItemPositionProvider(@Nonnull PackOutput output, @Nonnull String modid, @Nullable String subPack) {
        this.blockPathProvider = new CustomPathProvider(output, PackOutput.Target.RESOURCE_PACK, "lightmanscurrency/item_position_blocks", subPack);
        this.positionPathProvider = new CustomPathProvider(output, PackOutput.Target.RESOURCE_PACK, "lightmanscurrency/item_position_data", subPack);
        this.output = output;
        this.name = subPack != null ? modid + " (" + subPack + ")" : modid;
    }

    protected abstract void addEntries();

    protected final void addDataWithBlocks(@Nonnull ResourceLocation id, @Nonnull ItemPositionBuilder data, @Nonnull Object... blocks) {
        this.addData(id, data);
        this.addBlocks(id, blocks);
    }

    protected final void addData(@Nonnull ResourceLocation id, @Nonnull ItemPositionBuilder data) {
        if (!this.positionValues.containsKey(id)) {
            this.positionValues.put(id, data);
        } else {
            throw new IllegalArgumentException("Data for '" + id + "' is already defined!");
        }
    }

    protected final void addBlocks(@Nonnull ResourceLocation id, @Nonnull Object... blocks) {
        for (Object obj : blocks) {
            if (obj instanceof RegistryObjectBiBundle) {
                RegistryObjectBiBundle<?, ?, ?> bundle = (RegistryObjectBiBundle<?, ?, ?>) obj;
                for (Object b : bundle.getAllSorted()) {
                    if (b instanceof Block block) {
                        this.addBlock(id, block);
                    }
                }
            } else if (obj instanceof RegistryObjectBundle) {
                RegistryObjectBundle<?, ?> bundle = (RegistryObjectBundle<?, ?>) obj;
                for (Object bx : bundle.getAllSorted()) {
                    if (bx instanceof Block block) {
                        this.addBlock(id, block);
                    }
                }
            } else if (obj instanceof RegistryObject) {
                RegistryObject<?> ro = (RegistryObject<?>) obj;
                if (ro.get() instanceof Block block) {
                    this.addBlock(id, block);
                }
            } else if (obj instanceof List) {
                for (Object bxx : (List) obj) {
                    if (bxx instanceof Block block) {
                        this.addBlock(id, block);
                    } else if (bxx instanceof ResourceLocation blockID) {
                        this.addBlock(id, blockID);
                    }
                }
            } else if (obj instanceof ResourceLocation blockID) {
                this.addBlocks(id, blockID);
            }
        }
    }

    protected final void addBlock(@Nonnull ResourceLocation id, @Nonnull Block block) {
        this.addBlock(id, ForgeRegistries.BLOCKS.getKey(block));
    }

    protected final void addBlock(@Nonnull ResourceLocation id, @Nonnull ResourceLocation blockID) {
        List<ResourceLocation> list = (List<ResourceLocation>) this.blockValues.getOrDefault(id, new ArrayList());
        if (blockID != null && !list.contains(blockID)) {
            list.add(blockID);
        }
        this.blockValues.put(id, list);
    }

    @Nonnull
    @Override
    public CompletableFuture<Void> run(@Nonnull CachedOutput cache) {
        this.blockValues.clear();
        this.positionValues.clear();
        this.addEntries();
        List<CompletableFuture<?>> results = new ArrayList();
        this.positionValues.forEach((id, data) -> {
            JsonObject dataJson = data.write();
            Path path = this.positionPathProvider.json(id);
            if (path == null) {
                results.add(CompletableFuture.completedFuture(null));
            } else {
                results.add(DataProvider.saveStable(cache, dataJson, path));
            }
        });
        this.blockValues.forEach((id, blocks) -> {
            JsonObject blockJson = new JsonObject();
            JsonArray blockList = new JsonArray();
            for (ResourceLocation block : blocks) {
                blockList.add(block.toString());
            }
            blockJson.add("values", blockList);
            Path path = this.blockPathProvider.json(id);
            if (path == null) {
                results.add(CompletableFuture.completedFuture(null));
            } else {
                results.add(DataProvider.saveStable(cache, blockJson, path));
            }
        });
        return CompletableFuture.allOf((CompletableFuture[]) results.toArray(CompletableFuture[]::new));
    }

    @Nonnull
    @Override
    public String getName() {
        return "LightmansCurrency Item Positions: " + this.name;
    }
}