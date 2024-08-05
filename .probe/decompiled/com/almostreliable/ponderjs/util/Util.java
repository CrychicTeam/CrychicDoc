package com.almostreliable.ponderjs.util;

import com.almostreliable.ponderjs.PonderJS;
import com.google.common.collect.UnmodifiableIterator;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.Selection;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Util {

    public static Selection selectionOf(@Nullable Object o) {
        if (o instanceof Selection) {
            return (Selection) o;
        } else if (o instanceof BoundingBox box) {
            return Selection.of(box);
        } else if (o instanceof BlockPos b) {
            return Selection.of(new BoundingBox(b));
        } else {
            if (o instanceof List<?> l) {
                if (l.stream().anyMatch(Objects::isNull)) {
                    ConsoleJS.CLIENT.warn("Selection was provided as list with invalid values. This may happen if a comma is missing. Please check your code.");
                }
                if (l.size() == 2) {
                    UtilsJS.vec3Of(l.get(0));
                    Vec3 from = UtilsJS.vec3Of(l.get(0));
                    Vec3 to = UtilsJS.vec3Of(l.get(1));
                    return Selection.of(new BoundingBox((int) from.x, (int) from.y, (int) from.z, (int) to.x, (int) to.y, (int) to.z));
                }
                Integer[] values = (Integer[]) l.stream().map(entry -> UtilsJS.parseInt(entry, 0)).toArray(Integer[]::new);
                if (values.length == 6) {
                    return Selection.of(new BoundingBox(values[0], values[1], values[2], values[3], values[4], values[5]));
                }
                if (values.length == 3) {
                    return Selection.of(new BoundingBox(values[0], values[1], values[2], values[0], values[1], values[2]));
                }
            }
            Vec3 v = UtilsJS.vec3Of(o);
            return Selection.of(new BoundingBox(new BlockPos((int) v.x, (int) v.y, (int) v.z)));
        }
    }

    public static AllIcons allIconsOf(@Nullable Object o) {
        if (o instanceof AllIcons) {
            return (AllIcons) o;
        } else {
            return o == null ? AllIcons.I_ACTIVE : PonderJS.getIconByName(o.toString());
        }
    }

    public static PonderTag ponderTagOf(@Nullable Object o) {
        Objects.requireNonNull(o);
        PonderTag ponderTag = (PonderTag) PonderJS.getTagByName(o.toString()).orElse(null);
        if (ponderTag == null) {
            IllegalArgumentException e = new IllegalArgumentException("Invalid PonderTag: " + o);
            PonderErrorHelper.yeet(e);
            throw e;
        } else {
            return ponderTag;
        }
    }

    public static BlockIDPredicate createBlockID(BlockState state) {
        BlockIDPredicate predicate = new BlockIDPredicate(PonderPlatform.getBlockName(state.m_60734_()));
        UnmodifiableIterator var2 = state.m_61148_().entrySet().iterator();
        while (var2.hasNext()) {
            Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>) var2.next();
            predicate.with(((Property) entry.getKey()).getName(), ((Comparable) entry.getValue()).toString());
        }
        return predicate;
    }

    public static BlockState blockStateOf(@Nullable Object o) {
        if (o instanceof BlockState) {
            return (BlockState) o;
        } else if (o instanceof Block block) {
            return block.defaultBlockState();
        } else if (o instanceof BlockIDPredicate predicate) {
            return predicate.getBlockState();
        } else {
            if (o instanceof CharSequence s) {
                ResourceLocation location = ResourceLocation.tryParse(s.toString());
                if (location != null) {
                    Block block = ForgeRegistries.BLOCKS.getValue(location);
                    if (block != null) {
                        return block.defaultBlockState();
                    }
                }
            }
            return Blocks.AIR.defaultBlockState();
        }
    }
}