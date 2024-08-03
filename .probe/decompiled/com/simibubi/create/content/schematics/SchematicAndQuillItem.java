package com.simibubi.create.content.schematics;

import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SchematicAndQuillItem extends Item {

    public SchematicAndQuillItem(Item.Properties properties) {
        super(properties);
    }

    public static void replaceStructureVoidWithAir(CompoundTag nbt) {
        String air = RegisteredObjects.getKeyOrThrow(Blocks.AIR).toString();
        String structureVoid = RegisteredObjects.getKeyOrThrow(Blocks.STRUCTURE_VOID).toString();
        NBTHelper.iterateCompoundList(nbt.getList("palette", 10), c -> {
            if (c.contains("Name") && c.getString("Name").equals(structureVoid)) {
                c.putString("Name", air);
            }
        });
    }

    public static void clampGlueBoxes(Level level, AABB aabb, CompoundTag nbt) {
        ListTag listtag = nbt.getList("entities", 10).copy();
        Iterator<Tag> iterator = listtag.iterator();
        while (iterator.hasNext()) {
            Tag tag = (Tag) iterator.next();
            if (tag instanceof CompoundTag) {
                CompoundTag compoundtag = (CompoundTag) tag;
                if (compoundtag.contains("nbt") && new ResourceLocation(compoundtag.getCompound("nbt").getString("id")).equals(AllEntityTypes.SUPER_GLUE.getId())) {
                    iterator.remove();
                }
            }
        }
        for (SuperGlueEntity entity : SuperGlueEntity.collectCropped(level, aabb)) {
            Vec3 vec3 = new Vec3(entity.m_20185_() - aabb.minX, entity.m_20186_() - aabb.minY, entity.m_20189_() - aabb.minZ);
            CompoundTag compoundtag = new CompoundTag();
            entity.m_20223_(compoundtag);
            BlockPos blockpos = BlockPos.containing(vec3);
            CompoundTag entityTag = new CompoundTag();
            entityTag.put("pos", newDoubleList(vec3.x, vec3.y, vec3.z));
            entityTag.put("blockPos", newIntegerList(blockpos.m_123341_(), blockpos.m_123342_(), blockpos.m_123343_()));
            entityTag.put("nbt", compoundtag.copy());
            listtag.add(entityTag);
        }
        nbt.put("entities", listtag);
    }

    private static ListTag newIntegerList(int... pValues) {
        ListTag listtag = new ListTag();
        for (int i : pValues) {
            listtag.add(IntTag.valueOf(i));
        }
        return listtag;
    }

    private static ListTag newDoubleList(double... pValues) {
        ListTag listtag = new ListTag();
        for (double d0 : pValues) {
            listtag.add(DoubleTag.valueOf(d0));
        }
        return listtag;
    }
}