package com.simibubi.create.content.schematics;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SchematicInstances {

    private static final WorldAttached<Cache<Integer, SchematicWorld>> LOADED_SCHEMATICS = new WorldAttached<>($ -> CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES).build());

    @Nullable
    public static SchematicWorld get(Level world, ItemStack schematic) {
        Cache<Integer, SchematicWorld> map = LOADED_SCHEMATICS.get(world);
        int hash = getHash(schematic);
        SchematicWorld ifPresent = (SchematicWorld) map.getIfPresent(hash);
        if (ifPresent != null) {
            return ifPresent;
        } else {
            SchematicWorld loadWorld = loadWorld(world, schematic);
            if (loadWorld == null) {
                return null;
            } else {
                map.put(hash, loadWorld);
                return loadWorld;
            }
        }
    }

    private static SchematicWorld loadWorld(Level wrapped, ItemStack schematic) {
        if (schematic != null && schematic.hasTag()) {
            if (!schematic.getTag().getBoolean("Deployed")) {
                return null;
            } else {
                StructureTemplate activeTemplate = SchematicItem.loadSchematic(wrapped.m_246945_(Registries.BLOCK), schematic);
                if (activeTemplate.getSize().equals(Vec3i.ZERO)) {
                    return null;
                } else {
                    BlockPos anchor = NbtUtils.readBlockPos(schematic.getTag().getCompound("Anchor"));
                    SchematicWorld world = new SchematicWorld(anchor, wrapped);
                    StructurePlaceSettings settings = SchematicItem.getSettings(schematic);
                    activeTemplate.placeInWorld(world, anchor, anchor, settings, wrapped.getRandom(), 2);
                    StructureTransform transform = new StructureTransform(settings.getRotationPivot(), Direction.Axis.Y, settings.getRotation(), settings.getMirror());
                    for (BlockEntity be : world.getBlockEntities()) {
                        transform.apply(be);
                    }
                    return world;
                }
            }
        } else {
            return null;
        }
    }

    public static void clearHash(ItemStack schematic) {
        if (schematic != null && schematic.hasTag()) {
            schematic.getTag().remove("SchematicHash");
        }
    }

    public static int getHash(ItemStack schematic) {
        if (schematic != null && schematic.hasTag()) {
            CompoundTag tag = schematic.getTag();
            if (!tag.contains("SchematicHash")) {
                tag.putInt("SchematicHash", tag.toString().hashCode());
            }
            return tag.getInt("SchematicHash");
        } else {
            return -1;
        }
    }
}