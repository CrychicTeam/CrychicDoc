package net.mehvahdjukaar.moonlight.api.set;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class BlockTypeRegistry<T extends BlockType> {

    protected boolean frozen = false;

    private final String name;

    private final List<BlockType.SetFinder<T>> finders = new ArrayList();

    private final List<ResourceLocation> notInclude = new ArrayList();

    protected final List<T> builder = new ArrayList();

    private final Class<T> typeClass;

    private Map<ResourceLocation, T> types = new LinkedHashMap();

    private final Object2ObjectOpenHashMap<Object, T> childrenToType = new Object2ObjectOpenHashMap();

    protected BlockTypeRegistry(Class<T> typeClass, String name) {
        this.typeClass = typeClass;
        this.name = name;
    }

    public Class<T> getType() {
        return this.typeClass;
    }

    public T getFromNBT(String name) {
        return (T) this.types.getOrDefault(new ResourceLocation(name), this.getDefaultType());
    }

    @Nullable
    public T get(ResourceLocation res) {
        return (T) this.types.get(res);
    }

    public abstract T getDefaultType();

    public Collection<T> getValues() {
        return Collections.unmodifiableCollection(this.types.values());
    }

    public String typeName() {
        return this.name;
    }

    public abstract Optional<T> detectTypeFromBlock(Block var1, ResourceLocation var2);

    public void registerBlockType(T newType) {
        if (this.frozen) {
            throw new UnsupportedOperationException("Tried to register a wood types after registry events");
        } else {
            this.builder.add(newType);
        }
    }

    public Collection<BlockType.SetFinder<T>> getFinders() {
        return this.finders;
    }

    public void addFinder(BlockType.SetFinder<T> finder) {
        if (this.frozen) {
            throw new UnsupportedOperationException("Tried to register a block type finder after registry events");
        } else {
            this.finders.add(finder);
        }
    }

    public void addRemover(ResourceLocation id) {
        if (this.frozen) {
            throw new UnsupportedOperationException("Tried remove a block type after registry events");
        } else {
            this.notInclude.add(id);
        }
    }

    protected void finalizeAndFreeze() {
        if (this.frozen) {
            throw new UnsupportedOperationException("Block types are already finalized");
        } else {
            LinkedHashMap<ResourceLocation, T> linkedHashMap = new LinkedHashMap();
            List<String> modOrder = new ArrayList();
            modOrder.add("minecraft");
            this.builder.forEach(e -> {
                String modIdx = e.getNamespace();
                if (!modOrder.contains(modIdx)) {
                    modOrder.add(modIdx);
                }
            });
            for (String modId : modOrder) {
                this.builder.forEach(e -> {
                    if (Objects.equals(e.getNamespace(), modId) && !linkedHashMap.containsKey(e.getId())) {
                        linkedHashMap.put(e.getId(), e);
                    }
                });
            }
            this.types = ImmutableMap.copyOf(linkedHashMap);
            this.builder.clear();
            this.frozen = true;
        }
    }

    @Internal
    public void onBlockInit() {
        this.types.values().forEach(BlockType::initializeChildrenBlocks);
    }

    @Internal
    public void onItemInit() {
        this.types.values().forEach(BlockType::initializeChildrenItems);
    }

    @Internal
    public void buildAll() {
        if (!this.frozen) {
            this.registerBlockType(this.getDefaultType());
            this.finders.stream().map(BlockType.SetFinder::get).forEach(f -> f.ifPresent(this::registerBlockType));
            for (Block b : BuiltInRegistries.BLOCK) {
                this.detectTypeFromBlock(b, Utils.getID(b)).ifPresent(t -> {
                    if (!this.notInclude.contains(t.getId())) {
                        this.registerBlockType((T) t);
                    }
                });
            }
            this.finders.clear();
            this.notInclude.clear();
            this.finalizeAndFreeze();
        }
    }

    public void addTypeTranslations(AfterLanguageLoadEvent language) {
    }

    @Nullable
    public T getBlockTypeOf(ItemLike itemLike) {
        return (T) this.childrenToType.getOrDefault(itemLike, null);
    }

    protected void mapObjectToType(Object itemLike, BlockType type) {
        this.childrenToType.put(itemLike, type);
        if (itemLike instanceof BlockItem bi && !this.childrenToType.containsKey(bi.m_5456_())) {
            this.childrenToType.put(bi.m_5456_(), type);
        }
    }
}