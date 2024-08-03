package snownee.loquat;

import com.google.common.collect.Streams;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.area.Area;
import snownee.loquat.duck.LoquatServerPlayer;
import snownee.loquat.duck.LoquatStructurePiece;
import snownee.loquat.placement.LoquatPlacements;
import snownee.loquat.util.CommonProxy;
import snownee.loquat.util.TransformUtil;

public interface Hooks {

    static void fillFromWorld(AreaManager manager, BlockPos pos, Vec3i size, List<Area> areas) {
        AABB aabb = new AABB(pos, pos.offset(size));
        StructurePlaceSettings settings = new StructurePlaceSettings();
        for (Area area : manager.areas()) {
            if (area.inside(aabb)) {
                area = TransformUtil.transform(settings, pos.multiply(-1), area);
                area.setUuid(null);
                areas.add(area);
            }
        }
    }

    static void placeInWorld(AreaManager manager, BlockPos pos, BlockPos blockPos, List<Area> areas, StructurePlaceSettings settings, Vec3i size) {
        manager.removeAllInside(TransformUtil.transform(settings, pos, new AABB(pos, pos.offset(size))));
        for (Area area : areas) {
            area = TransformUtil.transform(settings, pos, area);
            if (settings.getBoundingBox() == null || AABB.of(settings.getBoundingBox()).contains(area.getOrigin())) {
                LoquatStructurePiece piece = LoquatStructurePiece.current();
                if (piece != null && piece.loquat$getAttachedData() != null) {
                    CompoundTag data = piece.loquat$getAttachedData();
                    if (data.contains("Tags")) {
                        for (Tag tag : data.getList("Tags", 8)) {
                            area.getTags().add(tag.getAsString());
                        }
                    }
                    if (data.contains("Data")) {
                        area.getOrCreateAttachedData().merge(data.getCompound("Data"));
                    }
                }
                manager.add(area);
            }
        }
    }

    static void tickServerPlayer(ServerPlayer player, LoquatServerPlayer loquatPlayer) {
        AreaManager manager = AreaManager.of(player.serverLevel());
        long chunkPos = ChunkPos.asLong(player.m_20183_());
        Set<Area> areasIn = loquatPlayer.loquat$getAreasIn();
        Streams.concat(new Stream[] { manager.byChunk(chunkPos), areasIn.stream() }).distinct().toList().forEach(area -> {
            boolean inside = area.contains(player.m_20191_());
            if (inside && areasIn.add(area)) {
                CommonProxy.postPlayerEnterArea(player, area);
            } else if (!inside && areasIn.remove(area)) {
                CommonProxy.postPlayerLeaveArea(player, area);
            }
        });
    }

    static void prePlaceStructure(ServerLevel serverLevel, ChunkPos chunkPos, ChunkPos chunkPos2) {
        ChunkPos.rangeClosed(chunkPos, chunkPos2).forEach(chunkPos3 -> serverLevel.getChunkSource().addRegionTicket(LoquatPlacements.TICKET_TYPE, chunkPos3, 1, chunkPos3));
        serverLevel.getChunkSource().tick(() -> true, false);
    }

    static void postPlaceStructure(ServerLevel serverLevel, ChunkPos chunkPos, ChunkPos chunkPos2) {
        ChunkPos.rangeClosed(chunkPos, chunkPos2).forEach(chunkPos3 -> serverLevel.getChunkSource().removeRegionTicket(LoquatPlacements.TICKET_TYPE, chunkPos3, 1, chunkPos3));
    }

    static void collideWithLoquatAreas(@Nullable Entity entity, AABB expanded, Consumer<VoxelShape> consumer) {
        if (entity != null && entity.getType() == EntityType.PLAYER) {
            RestrictInstance instance = RestrictInstance.of((Player) entity);
            if (!instance.isEmpty()) {
                MutableObject<RestrictInstance.RestrictBehavior> behavior = new MutableObject();
                MutableObject<Area> areaIn = new MutableObject();
                instance.areaStream().filter(area -> instance.isRestricted(area, RestrictInstance.RestrictBehavior.EXIT) && area.contains(entity.getBoundingBox())).findFirst().filter(area -> {
                    areaIn.setValue(area);
                    if (!area.contains(entity.getBoundingBox().inflate(0.1))) {
                        behavior.setValue(RestrictInstance.RestrictBehavior.EXIT);
                    }
                    return true;
                }).flatMap(Area::getVoxelShape).ifPresent(shape -> {
                    shape = Shapes.join(shape, Shapes.INFINITY, BooleanOp.ONLY_SECOND);
                    consumer.accept(shape);
                });
                instance.areaStream().filter(area -> !area.equals(areaIn.getValue()) && instance.isRestricted(area, RestrictInstance.RestrictBehavior.ENTER) && area.intersects(expanded)).forEach(area -> {
                    area.getVoxelShape().ifPresent(consumer);
                    behavior.setValue(RestrictInstance.RestrictBehavior.ENTER);
                });
                if (behavior.getValue() != null) {
                    CommonProxy.notifyRestriction((Player) entity, (RestrictInstance.RestrictBehavior) behavior.getValue());
                }
            }
        }
    }

    static boolean teleportServerPlayer(ServerPlayer player, LoquatServerPlayer loquatServerPlayer, double x, double y, double z) {
        RestrictInstance restrictInstance = RestrictInstance.of(player);
        if (restrictInstance.isEmpty()) {
            return false;
        } else {
            Vec3 pos = player.m_20182_();
            AABB expected = player.m_20191_().move(x - pos.x, y - pos.y, z - pos.z);
            return restrictInstance.areaStream().filter(area -> restrictInstance.isRestricted(area, RestrictInstance.RestrictBehavior.EXIT) && area.contains(player.m_20191_())).findFirst().map(area -> !area.contains(expected)).orElse(false) ? true : restrictInstance.areaStream().anyMatch(area -> restrictInstance.isRestricted(area, RestrictInstance.RestrictBehavior.ENTER) && !loquatServerPlayer.loquat$getAreasIn().contains(area) && area.intersects(expected));
        }
    }

    static void addDynamicProcessors(StructurePlaceSettings settings, RegistryAccess registryAccess, CompoundTag data, String key) {
        if (data.contains(key)) {
            ListTag list = data.getList(key, 8);
            for (int i = 0; i < list.size(); i++) {
                String s = list.getString(i);
                StructureProcessorList processorList = (StructureProcessorList) registryAccess.registryOrThrow(Registries.PROCESSOR_LIST).getOptional(ResourceLocation.tryParse(s)).orElseThrow(() -> new IllegalStateException("Unknown processor list: " + s));
                processorList.list().forEach(settings::m_74383_);
            }
        }
    }

    static boolean checkServerPlayerRestriction(ServerPlayer player, LoquatServerPlayer loquatServerPlayer) {
        if (player.isSpectator()) {
            return false;
        } else {
            RestrictInstance restrictInstance = RestrictInstance.of(player);
            if (restrictInstance.isEmpty()) {
                return false;
            } else {
                Vec3 pos = player.m_20182_();
                Vec3 lastPos = (Vec3) Objects.requireNonNull(loquatServerPlayer.loquat$getLastGoodPos());
                AABB lastBox = player.m_20191_().move(lastPos.x - pos.x, lastPos.y - pos.y, lastPos.z - pos.z);
                return restrictInstance.areaStream().filter(area -> restrictInstance.isRestricted(area, RestrictInstance.RestrictBehavior.EXIT) && area.contains(lastBox)).findFirst().map(area -> !area.contains(player.m_20191_())).orElse(false) ? true : restrictInstance.areaStream().anyMatch(area -> restrictInstance.isRestricted(area, RestrictInstance.RestrictBehavior.ENTER) && !loquatServerPlayer.loquat$getAreasIn().contains(area) && area.intersects(player.m_20191_()));
            }
        }
    }
}