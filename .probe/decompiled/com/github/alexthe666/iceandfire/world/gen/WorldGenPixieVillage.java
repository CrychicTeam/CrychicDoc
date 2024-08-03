package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenPixieVillage extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    public WorldGenPixieVillage(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        if (rand.nextInt(IafConfig.spawnPixiesChance) == 0 && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            int maxRoads = IafConfig.pixieVillageSize + rand.nextInt(5);
            BlockPos buildPosition = position;
            for (int placedRoads = 0; placedRoads < maxRoads; placedRoads++) {
                int roadLength = 10 + rand.nextInt(15);
                Direction buildingDirection = Direction.from2DDataValue(rand.nextInt(3));
                for (int i = 0; i < roadLength; i++) {
                    BlockPos buildPosition2 = buildPosition.relative(buildingDirection, i);
                    buildPosition2 = worldIn.m_5452_(Heightmap.Types.WORLD_SURFACE_WG, buildPosition2).below();
                    if (worldIn.m_8055_(buildPosition2).m_60819_().isEmpty()) {
                        worldIn.m_7731_(buildPosition2, Blocks.DIRT_PATH.defaultBlockState(), 2);
                    } else {
                        worldIn.m_7731_(buildPosition2, Blocks.SPRUCE_PLANKS.defaultBlockState(), 2);
                    }
                    if (rand.nextInt(8) == 0) {
                        Direction houseDir = rand.nextBoolean() ? buildingDirection.getClockWise() : buildingDirection.getCounterClockWise();
                        BlockState houseState = IafBlockRegistry.PIXIE_HOUSE_OAK.get().defaultBlockState();
                        int houseColor = rand.nextInt(5);
                        houseState = switch(houseColor) {
                            case 0 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            case 1 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            case 2 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_OAK.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            case 3 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_BIRCH.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            case 4 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            case 5 ->
                                (BlockState) IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get().defaultBlockState().m_61124_(BlockPixieHouse.FACING, houseDir.getOpposite());
                            default ->
                                houseState;
                        };
                        EntityPixie pixie = IafEntityRegistry.PIXIE.get().create(worldIn.m_6018_());
                        pixie.finalizeSpawn(worldIn, worldIn.m_6436_(buildPosition2.above()), MobSpawnType.SPAWNER, null, null);
                        pixie.m_6034_((double) buildPosition2.m_123341_(), (double) (buildPosition2.m_123342_() + 2), (double) buildPosition2.m_123343_());
                        pixie.m_21530_();
                        worldIn.m_7967_(pixie);
                        worldIn.m_7731_(buildPosition2.relative(houseDir).above(), houseState, 2);
                        if (!worldIn.m_8055_(buildPosition2.relative(houseDir)).m_60815_()) {
                            worldIn.m_7731_(buildPosition2.relative(houseDir), Blocks.COARSE_DIRT.defaultBlockState(), 2);
                            worldIn.m_7731_(buildPosition2.relative(houseDir).below(), Blocks.COARSE_DIRT.defaultBlockState(), 2);
                        }
                    }
                }
                buildPosition = buildPosition.relative(buildingDirection, rand.nextInt(roadLength));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "pixie_village";
    }
}