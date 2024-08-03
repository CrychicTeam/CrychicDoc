package com.almostreliable.morejs.features.structure;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

public class EntityInfoWrapper {

    private final List<StructureTemplate.StructureEntityInfo> entities;

    private final Vec3i borderSize;

    public EntityInfoWrapper(List<StructureTemplate.StructureEntityInfo> entities, Vec3i borderSize) {
        this.entities = entities;
        this.borderSize = borderSize;
    }

    public void forEach(Consumer<StructureTemplate.StructureEntityInfo> consumer) {
        this.entities.forEach(consumer);
    }

    public void removeIf(Predicate<StructureTemplate.StructureEntityInfo> predicate) {
        this.entities.removeIf(predicate);
    }

    public void add(CompoundTag tag) {
        Preconditions.checkNotNull(tag, "Invalid tag");
        if (!tag.contains("id")) {
            throw new IllegalArgumentException("Invalid tag, missing entity id");
        } else {
            ListTag motionTag = tag.getList("Motion", 6);
            if (motionTag.size() != 3) {
                throw new IllegalArgumentException("Invalid or missing tag, `Motion` tag must have 3 entries");
            } else {
                ListTag rotationTag = tag.getList("Rotation", 5);
                if (rotationTag.size() != 2) {
                    throw new IllegalArgumentException("Invalid or missing tag, `Rotation` tag must have 2 entries");
                } else {
                    ListTag posTag = tag.getList("Pos", 6);
                    if (posTag.size() != 3) {
                        throw new IllegalArgumentException("Invalid or missing tag, `Pos` tag must have 3 entries");
                    } else {
                        Vec3 pos = new Vec3(Mth.clamp(posTag.getDouble(0), 0.0, (double) this.borderSize.getX()), Mth.clamp(posTag.getDouble(1), 0.0, (double) this.borderSize.getY()), Mth.clamp(posTag.getDouble(2), 0.0, (double) this.borderSize.getZ()));
                        BlockPos blockPos = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));
                        this.entities.add(new StructureTemplate.StructureEntityInfo(pos, blockPos, tag));
                    }
                }
            }
        }
    }
}