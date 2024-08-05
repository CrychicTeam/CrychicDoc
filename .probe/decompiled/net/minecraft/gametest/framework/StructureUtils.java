package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.data.structures.StructureUpdater;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class StructureUtils {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String DEFAULT_TEST_STRUCTURES_DIR = "gameteststructures";

    public static String testStructuresDir = "gameteststructures";

    private static final int HOW_MANY_CHUNKS_TO_LOAD_IN_EACH_DIRECTION_OF_STRUCTURE = 4;

    public static Rotation getRotationForRotationSteps(int int0) {
        switch(int0) {
            case 0:
                return Rotation.NONE;
            case 1:
                return Rotation.CLOCKWISE_90;
            case 2:
                return Rotation.CLOCKWISE_180;
            case 3:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                throw new IllegalArgumentException("rotationSteps must be a value from 0-3. Got value " + int0);
        }
    }

    public static int getRotationStepsForRotation(Rotation rotation0) {
        switch(rotation0) {
            case NONE:
                return 0;
            case CLOCKWISE_90:
                return 1;
            case CLOCKWISE_180:
                return 2;
            case COUNTERCLOCKWISE_90:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown rotation value, don't know how many steps it represents: " + rotation0);
        }
    }

    public static void main(String[] string0) throws IOException {
        Bootstrap.bootStrap();
        Files.walk(Paths.get(testStructuresDir)).filter(p_177775_ -> p_177775_.toString().endsWith(".snbt")).forEach(p_177773_ -> {
            try {
                String $$1 = Files.readString(p_177773_);
                CompoundTag $$2 = NbtUtils.snbtToStructure($$1);
                CompoundTag $$3 = StructureUpdater.update(p_177773_.toString(), $$2);
                NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, p_177773_, NbtUtils.structureToSnbt($$3));
            } catch (IOException | CommandSyntaxException var4) {
                LOGGER.error("Something went wrong upgrading: {}", p_177773_, var4);
            }
        });
    }

    public static AABB getStructureBounds(StructureBlockEntity structureBlockEntity0) {
        BlockPos $$1 = structureBlockEntity0.m_58899_();
        BlockPos $$2 = $$1.offset(structureBlockEntity0.getStructureSize().offset(-1, -1, -1));
        BlockPos $$3 = StructureTemplate.transform($$2, Mirror.NONE, structureBlockEntity0.getRotation(), $$1);
        return new AABB($$1, $$3);
    }

    public static BoundingBox getStructureBoundingBox(StructureBlockEntity structureBlockEntity0) {
        BlockPos $$1 = structureBlockEntity0.m_58899_();
        BlockPos $$2 = $$1.offset(structureBlockEntity0.getStructureSize().offset(-1, -1, -1));
        BlockPos $$3 = StructureTemplate.transform($$2, Mirror.NONE, structureBlockEntity0.getRotation(), $$1);
        return BoundingBox.fromCorners($$1, $$3);
    }

    public static void addCommandBlockAndButtonToStartTest(BlockPos blockPos0, BlockPos blockPos1, Rotation rotation2, ServerLevel serverLevel3) {
        BlockPos $$4 = StructureTemplate.transform(blockPos0.offset(blockPos1), Mirror.NONE, rotation2, blockPos0);
        serverLevel3.m_46597_($$4, Blocks.COMMAND_BLOCK.defaultBlockState());
        CommandBlockEntity $$5 = (CommandBlockEntity) serverLevel3.m_7702_($$4);
        $$5.getCommandBlock().setCommand("test runthis");
        BlockPos $$6 = StructureTemplate.transform($$4.offset(0, 0, -1), Mirror.NONE, rotation2, $$4);
        serverLevel3.m_46597_($$6, Blocks.STONE_BUTTON.defaultBlockState().m_60717_(rotation2));
    }

    public static void createNewEmptyStructureBlock(String string0, BlockPos blockPos1, Vec3i vecI2, Rotation rotation3, ServerLevel serverLevel4) {
        BoundingBox $$5 = getStructureBoundingBox(blockPos1, vecI2, rotation3);
        clearSpaceForStructure($$5, blockPos1.m_123342_(), serverLevel4);
        serverLevel4.m_46597_(blockPos1, Blocks.STRUCTURE_BLOCK.defaultBlockState());
        StructureBlockEntity $$6 = (StructureBlockEntity) serverLevel4.m_7702_(blockPos1);
        $$6.setIgnoreEntities(false);
        $$6.setStructureName(new ResourceLocation(string0));
        $$6.setStructureSize(vecI2);
        $$6.setMode(StructureMode.SAVE);
        $$6.setShowBoundingBox(true);
    }

    public static StructureBlockEntity spawnStructure(String string0, BlockPos blockPos1, Rotation rotation2, int int3, ServerLevel serverLevel4, boolean boolean5) {
        Vec3i $$6 = getStructureTemplate(string0, serverLevel4).getSize();
        BoundingBox $$7 = getStructureBoundingBox(blockPos1, $$6, rotation2);
        BlockPos $$8;
        if (rotation2 == Rotation.NONE) {
            $$8 = blockPos1;
        } else if (rotation2 == Rotation.CLOCKWISE_90) {
            $$8 = blockPos1.offset($$6.getZ() - 1, 0, 0);
        } else if (rotation2 == Rotation.CLOCKWISE_180) {
            $$8 = blockPos1.offset($$6.getX() - 1, 0, $$6.getZ() - 1);
        } else {
            if (rotation2 != Rotation.COUNTERCLOCKWISE_90) {
                throw new IllegalArgumentException("Invalid rotation: " + rotation2);
            }
            $$8 = blockPos1.offset(0, 0, $$6.getX() - 1);
        }
        forceLoadChunks(blockPos1, serverLevel4);
        clearSpaceForStructure($$7, blockPos1.m_123342_(), serverLevel4);
        StructureBlockEntity $$13 = createStructureBlock(string0, $$8, rotation2, serverLevel4, boolean5);
        serverLevel4.getBlockTicks().clearArea($$7);
        serverLevel4.clearBlockEvents($$7);
        return $$13;
    }

    private static void forceLoadChunks(BlockPos blockPos0, ServerLevel serverLevel1) {
        ChunkPos $$2 = new ChunkPos(blockPos0);
        for (int $$3 = -1; $$3 < 4; $$3++) {
            for (int $$4 = -1; $$4 < 4; $$4++) {
                int $$5 = $$2.x + $$3;
                int $$6 = $$2.z + $$4;
                serverLevel1.setChunkForced($$5, $$6, true);
            }
        }
    }

    public static void clearSpaceForStructure(BoundingBox boundingBox0, int int1, ServerLevel serverLevel2) {
        BoundingBox $$3 = new BoundingBox(boundingBox0.minX() - 2, boundingBox0.minY() - 3, boundingBox0.minZ() - 3, boundingBox0.maxX() + 3, boundingBox0.maxY() + 20, boundingBox0.maxZ() + 3);
        BlockPos.betweenClosedStream($$3).forEach(p_177748_ -> clearBlock(int1, p_177748_, serverLevel2));
        serverLevel2.getBlockTicks().clearArea($$3);
        serverLevel2.clearBlockEvents($$3);
        AABB $$4 = new AABB((double) $$3.minX(), (double) $$3.minY(), (double) $$3.minZ(), (double) $$3.maxX(), (double) $$3.maxY(), (double) $$3.maxZ());
        List<Entity> $$5 = serverLevel2.m_6443_(Entity.class, $$4, p_177750_ -> !(p_177750_ instanceof Player));
        $$5.forEach(Entity::m_146870_);
    }

    public static BoundingBox getStructureBoundingBox(BlockPos blockPos0, Vec3i vecI1, Rotation rotation2) {
        BlockPos $$3 = blockPos0.offset(vecI1).offset(-1, -1, -1);
        BlockPos $$4 = StructureTemplate.transform($$3, Mirror.NONE, rotation2, blockPos0);
        BoundingBox $$5 = BoundingBox.fromCorners(blockPos0, $$4);
        int $$6 = Math.min($$5.minX(), $$5.maxX());
        int $$7 = Math.min($$5.minZ(), $$5.maxZ());
        return $$5.move(blockPos0.m_123341_() - $$6, 0, blockPos0.m_123343_() - $$7);
    }

    public static Optional<BlockPos> findStructureBlockContainingPos(BlockPos blockPos0, int int1, ServerLevel serverLevel2) {
        return findStructureBlocks(blockPos0, int1, serverLevel2).stream().filter(p_177756_ -> doesStructureContain(p_177756_, blockPos0, serverLevel2)).findFirst();
    }

    @Nullable
    public static BlockPos findNearestStructureBlock(BlockPos blockPos0, int int1, ServerLevel serverLevel2) {
        Comparator<BlockPos> $$3 = Comparator.comparingInt(p_177759_ -> p_177759_.m_123333_(blockPos0));
        Collection<BlockPos> $$4 = findStructureBlocks(blockPos0, int1, serverLevel2);
        Optional<BlockPos> $$5 = $$4.stream().min($$3);
        return (BlockPos) $$5.orElse(null);
    }

    public static Collection<BlockPos> findStructureBlocks(BlockPos blockPos0, int int1, ServerLevel serverLevel2) {
        Collection<BlockPos> $$3 = Lists.newArrayList();
        AABB $$4 = new AABB(blockPos0);
        $$4 = $$4.inflate((double) int1);
        for (int $$5 = (int) $$4.minX; $$5 <= (int) $$4.maxX; $$5++) {
            for (int $$6 = (int) $$4.minY; $$6 <= (int) $$4.maxY; $$6++) {
                for (int $$7 = (int) $$4.minZ; $$7 <= (int) $$4.maxZ; $$7++) {
                    BlockPos $$8 = new BlockPos($$5, $$6, $$7);
                    BlockState $$9 = serverLevel2.m_8055_($$8);
                    if ($$9.m_60713_(Blocks.STRUCTURE_BLOCK)) {
                        $$3.add($$8);
                    }
                }
            }
        }
        return $$3;
    }

    private static StructureTemplate getStructureTemplate(String string0, ServerLevel serverLevel1) {
        StructureTemplateManager $$2 = serverLevel1.getStructureManager();
        Optional<StructureTemplate> $$3 = $$2.get(new ResourceLocation(string0));
        if ($$3.isPresent()) {
            return (StructureTemplate) $$3.get();
        } else {
            String $$4 = string0 + ".snbt";
            Path $$5 = Paths.get(testStructuresDir, $$4);
            CompoundTag $$6 = tryLoadStructure($$5);
            if ($$6 == null) {
                throw new RuntimeException("Could not find structure file " + $$5 + ", and the structure is not available in the world structures either.");
            } else {
                return $$2.readStructure($$6);
            }
        }
    }

    private static StructureBlockEntity createStructureBlock(String string0, BlockPos blockPos1, Rotation rotation2, ServerLevel serverLevel3, boolean boolean4) {
        serverLevel3.m_46597_(blockPos1, Blocks.STRUCTURE_BLOCK.defaultBlockState());
        StructureBlockEntity $$5 = (StructureBlockEntity) serverLevel3.m_7702_(blockPos1);
        $$5.setMode(StructureMode.LOAD);
        $$5.setRotation(rotation2);
        $$5.setIgnoreEntities(false);
        $$5.setStructureName(new ResourceLocation(string0));
        $$5.loadStructure(serverLevel3, boolean4);
        if ($$5.getStructureSize() != Vec3i.ZERO) {
            return $$5;
        } else {
            StructureTemplate $$6 = getStructureTemplate(string0, serverLevel3);
            $$5.loadStructure(serverLevel3, boolean4, $$6);
            if ($$5.getStructureSize() == Vec3i.ZERO) {
                throw new RuntimeException("Failed to load structure " + string0);
            } else {
                return $$5;
            }
        }
    }

    @Nullable
    private static CompoundTag tryLoadStructure(Path path0) {
        try {
            BufferedReader $$1 = Files.newBufferedReader(path0);
            String $$2 = IOUtils.toString($$1);
            return NbtUtils.snbtToStructure($$2);
        } catch (IOException var3) {
            return null;
        } catch (CommandSyntaxException var4) {
            throw new RuntimeException("Error while trying to load structure " + path0, var4);
        }
    }

    private static void clearBlock(int int0, BlockPos blockPos1, ServerLevel serverLevel2) {
        BlockState $$3 = null;
        RegistryAccess $$4 = serverLevel2.m_9598_();
        FlatLevelGeneratorSettings $$5 = FlatLevelGeneratorSettings.getDefault($$4.m_255025_(Registries.BIOME), $$4.m_255025_(Registries.STRUCTURE_SET), $$4.m_255025_(Registries.PLACED_FEATURE));
        List<BlockState> $$6 = $$5.getLayers();
        int $$7 = blockPos1.m_123342_() - serverLevel2.m_141937_();
        if (blockPos1.m_123342_() < int0 && $$7 > 0 && $$7 <= $$6.size()) {
            $$3 = (BlockState) $$6.get($$7 - 1);
        }
        if ($$3 == null) {
            $$3 = Blocks.AIR.defaultBlockState();
        }
        BlockInput $$8 = new BlockInput($$3, Collections.emptySet(), null);
        $$8.place(serverLevel2, blockPos1, 2);
        serverLevel2.blockUpdated(blockPos1, $$3.m_60734_());
    }

    private static boolean doesStructureContain(BlockPos blockPos0, BlockPos blockPos1, ServerLevel serverLevel2) {
        StructureBlockEntity $$3 = (StructureBlockEntity) serverLevel2.m_7702_(blockPos0);
        AABB $$4 = getStructureBounds($$3).inflate(1.0);
        return $$4.contains(Vec3.atCenterOf(blockPos1));
    }
}