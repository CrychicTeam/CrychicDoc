package snownee.loquat.util;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.area.Zone;

public interface TransformUtil {

    static BlockPos calculateRelativePosition(StructurePlaceSettings settings, BlockPos offset, BlockPos pos) {
        return StructureTemplate.transform(pos, settings.getMirror(), settings.getRotation(), settings.getRotationPivot()).offset(offset);
    }

    static Vec3 calculateRelativePosition(StructurePlaceSettings settings, BlockPos offset, Vec3 pos) {
        return StructureTemplate.transform(pos, settings.getMirror(), settings.getRotation(), settings.getRotationPivot()).add((double) offset.m_123341_(), (double) offset.m_123342_(), (double) offset.m_123343_());
    }

    static AABB transform(StructurePlaceSettings settings, BlockPos offset, AABB aabb) {
        return new AABB(calculateRelativePosition(settings, offset, new Vec3(aabb.minX, aabb.minY, aabb.minZ)), calculateRelativePosition(settings, offset, new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ)));
    }

    static Zone transform(StructurePlaceSettings settings, BlockPos offset, Zone zone) {
        return new Zone(zone.aabbs().stream().map(aabb -> transform(settings, offset, aabb)).toList());
    }

    static Area transform(StructurePlaceSettings settings, BlockPos offset, Area area) {
        Area newArea = area.transform(settings, offset);
        newArea.getTags().addAll(area.getTags());
        area.getZones().forEach((key, value) -> newArea.getZones().put(key, transform(settings, offset, value)));
        newArea.setUuid(UUID.randomUUID());
        return newArea;
    }

    static StructurePlaceSettings getSettings(StructureBlockEntity be) {
        StructurePlaceSettings settings = new StructurePlaceSettings();
        settings.setIgnoreEntities(be.isIgnoreEntities());
        settings.setMirror(be.getMirror());
        settings.setRotation(be.getRotation());
        return settings;
    }

    static AABB getAABB(StructureBlockEntity be) {
        StructurePlaceSettings settings = getSettings(be);
        BlockPos start = be.m_58899_().offset(be.getStructurePos());
        BlockPos end = calculateRelativePosition(settings, start, new BlockPos(be.getStructureSize()));
        start = calculateRelativePosition(settings, start, BlockPos.ZERO);
        return new AABB(start, end);
    }
}