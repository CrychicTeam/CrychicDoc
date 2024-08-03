package com.craisinlord.integrated_api.utils;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.mixins.resources.NamespaceResourceManagerAccessor;
import com.craisinlord.integrated_api.mixins.resources.ReloadableResourceManagerImplAccessor;
import com.craisinlord.integrated_api.mixins.structures.JigsawJunctionAccessor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class GeneralUtils {

    private static final Map<BlockState, Boolean> IS_FULLCUBE_MAP = new ConcurrentHashMap();

    private GeneralUtils() {
    }

    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, RandomSource random) {
        double totalWeight = 0.0;
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += (double) ((Integer) pair.getSecond()).intValue();
        }
        int index = 0;
        for (double randomWeightPicked = (double) random.nextFloat() * totalWeight; index < rlList.size() - 1; index++) {
            randomWeightPicked -= (double) ((Integer) ((Pair) rlList.get(index)).getSecond()).intValue();
            if (randomWeightPicked <= 0.0) {
                break;
            }
        }
        return (T) ((Pair) rlList.get(index)).getFirst();
    }

    public static boolean isFullCube(BlockGetter world, BlockPos pos, BlockState state) {
        return state == null ? false : (Boolean) IS_FULLCUBE_MAP.computeIfAbsent(state, stateIn -> Block.isShapeFullBlock(stateIn.m_60768_(world, pos)));
    }

    public static BlockState orientateChest(ServerLevelAccessor blockView, BlockPos blockPos, BlockState blockState) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        Direction wallDirection = (Direction) blockState.m_61143_(HorizontalDirectionalBlock.FACING);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            mutable.set(blockPos).move(facing);
            if (isFullCube(blockView, mutable, blockView.m_8055_(mutable))) {
                wallDirection = facing;
                mutable.move(facing.getOpposite(), 2);
                if (!blockView.m_8055_(mutable).m_280296_()) {
                    break;
                }
            }
        }
        return (BlockState) blockState.m_61124_(HorizontalDirectionalBlock.FACING, wallDirection.getOpposite());
    }

    public static ItemStack enchantRandomly(RandomSource random, ItemStack itemToEnchant, float chance) {
        if (random.nextFloat() < chance) {
            List<Enchantment> list = BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::m_6592_).filter(enchantmentToCheck -> enchantmentToCheck.canEnchant(itemToEnchant)).toList();
            if (!list.isEmpty()) {
                Enchantment enchantment = (Enchantment) list.get(random.nextInt(list.size()));
                int enchantmentLevel = random.nextInt(Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel()) + 1);
                itemToEnchant.enchant(enchantment, enchantmentLevel);
            }
        }
        return itemToEnchant;
    }

    public static int getMaxTerrainLimit(ChunkGenerator chunkGenerator) {
        return chunkGenerator.getMinY() + chunkGenerator.getGenDepth();
    }

    public static BlockPos getHighestLand(ChunkGenerator chunkGenerator, RandomState randomState, BoundingBox boundingBox, LevelHeightAccessor heightLimitView, boolean canBeOnLiquid) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(boundingBox.getCenter().m_123341_(), getMaxTerrainLimit(chunkGenerator) - 40, boundingBox.getCenter().m_123343_());
        NoiseColumn blockView = chunkGenerator.getBaseColumn(mutable.m_123341_(), mutable.m_123343_(), heightLimitView, randomState);
        while (mutable.m_123342_() > chunkGenerator.getSeaLevel()) {
            BlockState currentBlockstate = blockView.getBlock(mutable.m_123342_());
            if (currentBlockstate.m_60815_()) {
                if (blockView.getBlock(mutable.m_123342_() + 3).m_60795_() && (canBeOnLiquid ? !currentBlockstate.m_60795_() : currentBlockstate.m_60815_())) {
                    return mutable;
                }
                mutable.move(Direction.DOWN);
            } else {
                mutable.move(Direction.DOWN);
            }
        }
        return mutable;
    }

    public static BlockPos getLowestLand(ChunkGenerator chunkGenerator, RandomState randomState, BoundingBox boundingBox, LevelHeightAccessor heightLimitView, boolean canBeOnLiquid) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(boundingBox.getCenter().m_123341_(), chunkGenerator.getSeaLevel() + 1, boundingBox.getCenter().m_123343_());
        NoiseColumn blockView = chunkGenerator.getBaseColumn(mutable.m_123341_(), mutable.m_123343_(), heightLimitView, randomState);
        for (BlockState currentBlockstate = blockView.getBlock(mutable.m_123342_()); mutable.m_123342_() <= getMaxTerrainLimit(chunkGenerator) - 40; currentBlockstate = blockView.getBlock(mutable.m_123342_())) {
            if ((canBeOnLiquid ? !currentBlockstate.m_60795_() : currentBlockstate.m_60815_()) && blockView.getBlock(mutable.m_123342_() + 1).m_60795_() && blockView.getBlock(mutable.m_123342_() + 5).m_60795_()) {
                mutable.move(Direction.UP);
                return mutable;
            }
            mutable.move(Direction.UP);
        }
        return mutable.set(mutable.m_123341_(), chunkGenerator.getSeaLevel(), mutable.m_123343_());
    }

    public static int getFirstLandYFromPos(LevelReader worldView, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(pos);
        ChunkAccess currentChunk = worldView.getChunk(mutable);
        for (BlockState currentState = currentChunk.m_8055_(mutable); mutable.m_123342_() >= worldView.getMinBuildHeight() && isReplaceableByStructures(currentState); currentState = currentChunk.m_8055_(mutable)) {
            mutable.move(Direction.DOWN);
        }
        return mutable.m_123342_();
    }

    private static boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.m_60795_() || !blockState.m_60819_().isEmpty() || blockState.m_204336_(BlockTags.REPLACEABLE_BY_TREES);
    }

    public static void centerAllPieces(BlockPos targetPos, List<? extends StructurePiece> pieces) {
        if (!pieces.isEmpty()) {
            Vec3i structureCenter = ((StructurePiece) pieces.get(0)).getBoundingBox().getCenter();
            int xOffset = targetPos.m_123341_() - structureCenter.getX();
            int zOffset = targetPos.m_123343_() - structureCenter.getZ();
            for (StructurePiece structurePiece : pieces) {
                structurePiece.move(xOffset, 0, zOffset);
            }
        }
    }

    public static void movePieceProperly(StructurePiece piece, int x, int y, int z) {
        piece.move(x, y, z);
        if (piece instanceof PoolElementStructurePiece poolElementStructurePiece) {
            poolElementStructurePiece.getJunctions().forEach(junction -> {
                ((JigsawJunctionAccessor) junction).setSourceX(junction.getSourceX() + x);
                ((JigsawJunctionAccessor) junction).setSourceX(junction.getSourceGroundY() + y);
                ((JigsawJunctionAccessor) junction).setSourceX(junction.getSourceZ() + z);
            });
        }
    }

    public static boolean canJigsawsAttach(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        FrontAndTop prop1 = (FrontAndTop) jigsaw1.state().m_61143_(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = (FrontAndTop) jigsaw2.state().m_61143_(JigsawBlock.ORIENTATION);
        String joint = jigsaw1.nbt().getString("joint");
        if (joint.isEmpty()) {
            joint = prop1.front().getAxis().isHorizontal() ? "aligned" : "rollable";
        }
        boolean isRollable = joint.equals("rollable");
        return prop1.front() == prop2.front().getOpposite() && (isRollable || prop1.top() == prop2.top()) && jigsaw1.nbt().getString("target").equals(jigsaw2.nbt().getString("name"));
    }

    public static List<InputStream> getAllFileStreams(ResourceManager resourceManager, ResourceLocation fileID) throws IOException {
        List<InputStream> fileStreams = new ArrayList();
        FallbackResourceManager namespaceResourceManager = (FallbackResourceManager) ((ReloadableResourceManagerImplAccessor) resourceManager).integratedapi_getNamespacedManagers().get(fileID.getNamespace());
        for (FallbackResourceManager.PackEntry packEntry : ((NamespaceResourceManagerAccessor) namespaceResourceManager).integratedapi_getFallbacks()) {
            PackResources resourcePack = packEntry.resources();
            if (resourcePack != null) {
                IoSupplier<InputStream> IoSupplier = resourcePack.getResource(PackType.SERVER_DATA, fileID);
                if (IoSupplier != null) {
                    InputStream inputStream = IoSupplier.get();
                    fileStreams.add(inputStream);
                }
            }
        }
        return fileStreams;
    }

    public static Map<ResourceLocation, List<JsonElement>> getAllDatapacksJSONElement(ResourceManager resourceManager, Gson gson, String dataType, int fileSuffixLength) {
        Map<ResourceLocation, List<JsonElement>> map = new HashMap();
        int dataTypeLength = dataType.length() + 1;
        for (Entry<ResourceLocation, List<Resource>> resourceStackEntry : resourceManager.listResourceStacks(dataType, fileString -> fileString.toString().endsWith(".json")).entrySet()) {
            String identifierPath = ((ResourceLocation) resourceStackEntry.getKey()).getPath();
            ResourceLocation fileID = new ResourceLocation(((ResourceLocation) resourceStackEntry.getKey()).getNamespace(), identifierPath.substring(dataTypeLength, identifierPath.length() - fileSuffixLength));
            try {
                for (Resource resource : (List) resourceStackEntry.getValue()) {
                    InputStream fileStream = resource.open();
                    Reader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8));
                    try {
                        JsonElement countsJSONElement = GsonHelper.fromJson(gson, bufferedReader, JsonElement.class);
                        if (countsJSONElement != null) {
                            if (!map.containsKey(fileID)) {
                                map.put(fileID, new ArrayList());
                            }
                            ((List) map.get(fileID)).add(countsJSONElement);
                        } else {
                            IntegratedAPI.LOGGER.error("(Integrated API {} MERGER) Couldn't load data file {} from {} as it's null or empty", dataType, fileID, resourceStackEntry);
                        }
                    } catch (Throwable var17) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable var16) {
                            var17.addSuppressed(var16);
                        }
                        throw var17;
                    }
                    bufferedReader.close();
                }
            } catch (IOException | JsonParseException | IllegalArgumentException var18) {
                IntegratedAPI.LOGGER.error("(Integrated API {} MERGER) Couldn't parse data file {} from {}", dataType, fileID, resourceStackEntry, var18);
            }
        }
        return map;
    }
}