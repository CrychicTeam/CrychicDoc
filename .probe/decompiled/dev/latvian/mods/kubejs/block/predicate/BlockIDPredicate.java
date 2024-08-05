package dev.latvian.mods.kubejs.block.predicate;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockIDPredicate implements BlockPredicate {

    private final ResourceLocation id;

    private final Map<String, String> properties;

    private Block cachedBlock;

    private List<BlockIDPredicate.PropertyObject> cachedProperties;

    public BlockIDPredicate(ResourceLocation i) {
        this.id = i;
        this.properties = new HashMap();
    }

    public String toString() {
        if (this.properties.isEmpty()) {
            return this.id.toString();
        } else {
            StringBuilder sb = new StringBuilder(this.id.toString());
            sb.append('[');
            boolean first = true;
            for (Entry<String, String> entry : this.properties.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append((String) entry.getKey());
                sb.append('=');
                sb.append((String) entry.getValue());
            }
            sb.append(']');
            return sb.toString();
        }
    }

    public BlockIDPredicate with(String key, String value) {
        this.properties.put(key, value);
        this.cachedBlock = null;
        this.cachedProperties = null;
        return this;
    }

    private Block getBlock() {
        if (this.cachedBlock == null) {
            this.cachedBlock = RegistryInfo.BLOCK.getValue(this.id);
            if (this.cachedBlock == null) {
                this.cachedBlock = Blocks.AIR;
            }
        }
        return this.cachedBlock;
    }

    public List<BlockIDPredicate.PropertyObject> getBlockProperties() {
        if (this.cachedProperties == null) {
            this.cachedProperties = new LinkedList();
            Map<String, Property<?>> map = new HashMap();
            for (Property<?> property : this.getBlock().defaultBlockState().m_61147_()) {
                map.put(property.getName(), property);
            }
            for (Entry<String, String> entry : this.properties.entrySet()) {
                Property<? extends Comparable<?>> property = (Property<? extends Comparable<?>>) map.get(entry.getKey());
                if (property != null) {
                    Optional<?> o = property.getValue((String) entry.getValue());
                    if (o.isPresent()) {
                        BlockIDPredicate.PropertyObject po = new BlockIDPredicate.PropertyObject(property, o.get());
                        this.cachedProperties.add(po);
                    }
                }
            }
        }
        return this.cachedProperties;
    }

    public BlockState getBlockState() {
        BlockState state = this.getBlock().defaultBlockState();
        for (BlockIDPredicate.PropertyObject object : this.getBlockProperties()) {
            state = (BlockState) state.m_61124_(object.property, UtilsJS.cast(object.value));
        }
        return state;
    }

    @Override
    public boolean check(BlockContainerJS b) {
        return this.getBlock() != Blocks.AIR && this.checkState(b.getBlockState());
    }

    public boolean checkState(BlockState state) {
        if (state.m_60734_() != this.getBlock()) {
            return false;
        } else if (this.properties.isEmpty()) {
            return true;
        } else {
            for (BlockIDPredicate.PropertyObject object : this.getBlockProperties()) {
                if (!state.m_61143_(object.property).equals(object.value)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static record PropertyObject(Property<?> property, Object value) {
    }
}