package net.mehvahdjukaar.moonlight.api.set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.set.BlockSetInternal;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockType {

    private final BiMap<String, Object> children = HashBiMap.create();

    public final ResourceLocation id;

    protected BlockType(ResourceLocation resourceLocation) {
        this.id = resourceLocation;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getTypeName() {
        return this.id.getPath();
    }

    public String getNamespace() {
        return this.id.getNamespace();
    }

    public String getAppendableId() {
        return this.getNamespace() + "/" + this.getTypeName();
    }

    public String toString() {
        return this.id.toString();
    }

    public abstract String getTranslationKey();

    public String getVariantId(String baseName) {
        String namespace = this.isVanilla() ? "" : this.getNamespace() + "/";
        return baseName.contains("%s") ? namespace + String.format(baseName, this.getTypeName()) : namespace + baseName + "_" + this.getTypeName();
    }

    public String getVariantId(String baseName, boolean prefix) {
        return this.getVariantId(prefix ? baseName + "_%s" : "%s_" + baseName);
    }

    public String getVariantId(String postfix, String prefix) {
        return this.getVariantId(prefix + "_%s_" + postfix);
    }

    public String getReadableName() {
        return LangBuilder.getReadableName(this.getTypeName());
    }

    public boolean isVanilla() {
        return this.getNamespace().equals("minecraft");
    }

    @Nullable
    protected <V> V findRelatedEntry(String appendedName, Registry<V> reg) {
        return this.findRelatedEntry(appendedName, "", reg);
    }

    @Nullable
    protected <V> V findRelatedEntry(String append, String postPend, Registry<V> reg) {
        String post = postPend.isEmpty() ? "" : "_" + postPend;
        ResourceLocation[] targets = new ResourceLocation[] { new ResourceLocation(this.id.getNamespace(), "wood/planks/" + this.id.getPath() + "_" + append), new ResourceLocation(this.id.getNamespace(), "wood/" + append + post + "/" + this.id.getPath()), new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "_" + append + post), new ResourceLocation(this.id.getNamespace(), append + "_" + this.id.getPath() + post), new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "_planks_" + append + post) };
        V found = null;
        for (ResourceLocation r : targets) {
            if (reg.containsKey(r)) {
                found = reg.get(r);
                break;
            }
        }
        return found;
    }

    public Set<Entry<String, Object>> getChildren() {
        return this.children.entrySet();
    }

    @Nullable
    public Item getItemOfThis(String key) {
        return this.getChild(key) instanceof ItemLike i ? i.asItem() : null;
    }

    @Nullable
    public Block getBlockOfThis(String key) {
        Object v = this.getChild(key);
        if (v instanceof BlockItem bi) {
            return bi.getBlock();
        } else {
            return v instanceof Block b ? b : null;
        }
    }

    @Nullable
    public Object getChild(String key) {
        return this.children.get(key);
    }

    public void addChild(String genericName, @Nullable Object obj) {
        if (obj != Items.AIR && obj != Blocks.AIR) {
            if (obj != null) {
                try {
                    this.children.put(genericName, obj);
                    BlockTypeRegistry<?> registry = BlockSetInternal.getRegistry(this.getClass());
                    if (registry != null) {
                        registry.mapObjectToType(obj, this);
                    }
                } catch (Exception var4) {
                    Moonlight.LOGGER.error("Failed to add block type child: value already present. Key {}, Object {}, BlockType {}", genericName, obj, this);
                }
            }
        } else {
            throw new IllegalStateException("Tried to add air block/item to Block Type. Key " + genericName + ". This is a Moonlight bug, please report me");
        }
    }

    protected abstract void initializeChildrenBlocks();

    protected abstract void initializeChildrenItems();

    public abstract ItemLike mainChild();

    @Nullable
    public String getChildKey(Object child) {
        return (String) this.children.inverse().get(child);
    }

    @Nullable
    public static Object changeType(Object current, BlockType originalMat, BlockType destinationMat) {
        if (destinationMat == originalMat) {
            return current;
        } else {
            String key = originalMat.getChildKey(current);
            return key != null ? destinationMat.getChild(key) : null;
        }
    }

    @Nullable
    public static Item changeItemType(Item current, BlockType originalMat, BlockType destinationMat) {
        Object changed = changeType(current, originalMat, destinationMat);
        if (changed == null && current instanceof BlockItem bi && changeType(bi.getBlock(), originalMat, destinationMat) instanceof Block il) {
            Item i = il.asItem();
            if (i != Items.AIR) {
                changed = i;
            }
        }
        if (changed instanceof ItemLike ilx) {
            if (ilx.asItem() == current) {
                Moonlight.LOGGER.error("Somehow changed an item type into itself. How? Target mat {}, destination map {}, item {}", destinationMat, originalMat, ilx);
            }
            return ilx.asItem();
        } else {
            return null;
        }
    }

    @Nullable
    public static Block changeBlockType(@NotNull Block current, BlockType originalMat, BlockType destinationMat) {
        Object changed = changeType(current, originalMat, destinationMat);
        if (changed == null && current.asItem() != Items.AIR && changeType(current.asItem(), originalMat, destinationMat) instanceof BlockItem bi) {
            Item i = bi.m_5456_();
            if (i != Items.AIR) {
                changed = i;
            }
        }
        return changed instanceof Block ? (Block) changed : null;
    }

    public SoundType getSound() {
        return this.mainChild() instanceof Block b ? b.getSoundType(b.defaultBlockState()) : SoundType.STONE;
    }

    @FunctionalInterface
    public interface SetFinder<T extends BlockType> extends Supplier<Optional<T>> {

        Optional<T> get();
    }
}